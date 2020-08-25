package com.prince.project.controller;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.base.rest.PayPalRESTException;
import com.prince.project.DAO.RoleDAO;
import com.prince.project.DAO.UserDAO;
import com.prince.project.config.AppConfig;
import com.prince.project.config.PaypalPaymentIntent;
import com.prince.project.config.PaypalPaymentMethod;
import com.prince.project.model.AppUser;
import com.prince.project.model.Post;
import com.prince.project.model.TransactionHistory;
import com.prince.project.repository.PostRepository;
import com.prince.project.repository.TransactionRepository;
import com.prince.project.repository.UserRepository;
import com.prince.project.service.AppUserService;
import com.prince.project.service.PaypalService;
import com.prince.project.service.PostService;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	
	public static final String PAYPAL_SUCCESS_URL = "/success";
	public static final String PAYPAL_CANCEL_URL = "/cancel";
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PaypalService paypalService;
	
	@Autowired
    PostService postService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private TransactionRepository tranRepository;
	
	@Autowired
	private AppUserService userService;
	
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private RoleDAO roleDAO;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mav, HttpServletRequest request){
		mav.addObject("listPayment", postService.listPayment);
		mav.setViewName("step3");
		return mav;
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteOrder(ModelAndView mav,@PathVariable int id) {
		postService.deleteOrder(id);
		return new ModelAndView("redirect:/payment");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/create")
	public ModelAndView pay( ModelAndView mav, HttpServletRequest request ){

		String cancelUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/payment" + PAYPAL_CANCEL_URL;
		String successUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() +"/payment" + PAYPAL_SUCCESS_URL;
		try {
			List<Post> listPayment = postService.listPayment;
			Payment payment = paypalService.createPayment(listPayment, "USD", PaypalPaymentMethod.paypal, PaypalPaymentIntent.sale, "payment discription", cancelUrl, successUrl);
			for(Links links : payment.getLinks()){
				if(links.getRel().equals("approval_url")){
					AppUser user = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
					List<String> roles = roleDAO.getRoleNames(user.getId());
					if(!roles.contains("CREATER")) {userService.addRole(user, AppConfig.CREATER);}
					return new ModelAndView("redirect:" + links.getHref());
				}
			}
		} catch (PayPalRESTException e) {
			mav.addObject(AppConfig.MESSAGE, "Error");
			mav.setViewName("notify");
			log.error(e.getMessage());
		}
		return mav;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/payout")
	public String payout( ModelAndView mav, RedirectAttributes redirectAttributes, HttpServletRequest request, @RequestParam(value = "score") int score ){
			AppUser user = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
			DecimalFormat format = new DecimalFormat("#.##");
			String money = format.format(score);
			String email = request.getUserPrincipal().getName();
			PayoutBatch payout = paypalService.createPayout(email, money, "USD"); 
			TransactionHistory th = new TransactionHistory();
			th.setAuthor(user);
			th.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			th.setScore(score*1000);
			if(payout == null) {
				th.setStatus(AppConfig.FAILED);
				tranRepository.save(th);
				redirectAttributes.addFlashAttribute("status", false);
				return "redirect:/profile";
				
			}
			if(payout != null) {
				th.setStatus(AppConfig.SUCCESS);
				user.setScore(user.getScore() - score*1000);
				tranRepository.save(th);
				userRepository.save(user);
				redirectAttributes.addFlashAttribute("status", true);
				return "redirect:/profile";
			}	
			return null;
		
	}

	@RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
	public String cancelPay(){
		return "redirect:/payment";
	}

	@RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request){
		List<Post> listPayment = postService.listPayment;
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			for(Post post:listPayment) {
					post.setStatus(AppConfig.ACTIVE);
					postRepository.save(post);
			}
			postService.initListPayment();
			if(payment.getState().equals("approved")){
				return "redirect:/post/list";
			}
		} catch (PayPalRESTException e) {
			log.error(e.getMessage());
		}
		return "redirect:/";
	}
	
}
