package com.prince.project.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prince.project.DAO.TranDAO;
import com.prince.project.DAO.UserDAO;
import com.prince.project.config.AppConfig;
import com.prince.project.model.AppUser;
import com.prince.project.model.Post;
import com.prince.project.model.ResetPassword;
import com.prince.project.repository.TransactionRepository;
import com.prince.project.repository.UserRepository;
import com.prince.project.repository.UserRoleRepository;
import com.prince.project.service.AppUserService;
import com.prince.project.service.EmailSenderService;
import com.prince.project.service.StorageException;
import com.prince.project.service.StorageService;
import com.prince.project.utils.EncrytedPasswordUtils;
import com.prince.project.utils.WebUtils;

@Controller
public class AuthController {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private EmailSenderService emailSenderService;
	@Autowired
	private TranDAO tranDAO;
	@Autowired
    StorageService storageService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {
		
		return "login";
	}
	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		
		return "login";
	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String displayRegistration(AppUser appUser) {

		return "register";
	}
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView registerUser(ModelAndView mav, @Valid AppUser appUser, BindingResult bindingResult) {
		mav.setViewName("register");
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getAllErrors());
			mav.setViewName("register");
		} else {
			AppUser userbyEmail = userDAO.findUserAccountByEmail(appUser.getEmail());
			AppUser userbyName = userDAO.findUserAccountByName(appUser.getName());
			if (userbyEmail != null) {
				mav.addObject(AppConfig.MESSAGE, "email này đã tồn tại!");
				mav.setViewName("register");
			}
			if (userbyName != null) {
				mav.addObject(AppConfig.MESSAGE, "Tên này đã được sử dụng !");
				mav.setViewName("register");
			}
			if (userbyEmail == null && userbyName == null) {
				appUserService.createUser(appUser);
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(appUser.getEmail());
				mailMessage.setSubject("Đăng ký hoàn tất!");
				mailMessage.setFrom("PrinceZoZ.inc@gmail.com");
				mailMessage.setText("Để xác nhận tài khoản, vui lòng nhấn vào đường dẫn này : "
						+ "http://localhost:8080/confirm-account?token=" + appUser.getEmailVerifyHash());

				emailSenderService.sendEmail(mailMessage);

				mav.addObject(AppConfig.USER_NAME, appUser.getName());
				mav.addObject(AppConfig.MESSAGE, "Kiểm tra email của bạn và xác thực để tiếp tục");
				mav.setViewName("notify");

			}
		}
		return mav;
	}

	@RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmUserAccount(ModelAndView mav, @RequestParam("token") String token) {
		//kiem tra neu khong co token thi thong bao lỗi
		if (token != null) {
			// tim user theo token
			AppUser user = userDAO.findUserAccountByToken(token);
			//neu ton tai user thi tien hanh xac thuc tai khoan
			if (user != null) {
				appUserService.confimAccount(token);
				mav.addObject(AppConfig.MESSAGE, "Tài khoản của bạn đã xác thực thành công");
			} else {
				mav.addObject(AppConfig.MESSAGE, "Liên kết đã hết hạn hoặc mã xác thực sai");
			}
			mav.setViewName("notify");
		} else {
			mav.addObject(AppConfig.MESSAGE, "Liên kết của bạn không đúng, vui lòng kiểm tra lại !");
			mav.setViewName("notify");
		}

		return mav;
	}

	@RequestMapping(value = "/request-password-reset", method = RequestMethod.GET)
	public String displayForgotPassword(ResetPassword resetPassword) {

		return "forgotPassword";
	}

	@RequestMapping(value = "/request-password-reset", method = RequestMethod.POST)
	public ModelAndView forgotPassword(ModelAndView mav, ResetPassword resetPassword) {
		if (resetPassword != null) {
			// tim user theo email nhap vao
			AppUser appUser = userDAO.findUserAccountByEmail(resetPassword.getEmail());
			// kiem tra tai khoan actived  hoac dang trong tang thai doi mat khau
			if (appUser != null && (AppConfig.ACTIVE == appUser.getStatus()
					|| AppConfig.IN_RESET_PASSWORD == appUser.getStatus())) {
				// change status to IN RESET PASSWORD
				appUserService.inResetPassword(appUser);
				// send mail
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(appUser.getEmail());
				mailMessage.setSubject("Reset password!");
				mailMessage.setFrom("PrinceZoZ.inc@gmail.com");
				mailMessage.setText("Hi, " + appUser.getName() + ", \n\n"
						+ "You are receiving this email because we received a password reset request for your account.Please click the link to reset your password. \n\n"
						+ "http://localhost:8080/reset-password?token=" + appUser.getEmailVerifyHash() + "\n\n"
						+ "If you did not request a password reset, no further action is required.\n\n" + "Thanks, \n"
						+ "The Prince Team");
				emailSenderService.sendEmail(mailMessage);
				// update view
				mav.addObject(AppConfig.MESSAGE, "We just sent a link to your email");
				mav.setViewName("notify");

			} else {
				mav.addObject(AppConfig.MESSAGE,
						"* We couldn't find a Prince account associated with " + resetPassword.getEmail() + "!");
				mav.setViewName("forgotPassword");
			}

		} else {
			mav.addObject(AppConfig.MESSAGE, "Liên kết của bạn không đúng, vui lòng kiểm tra lại !");
			mav.setViewName("notify");
		}

		return mav;
	}

	@RequestMapping(value = "/reset-password", method = RequestMethod.GET)
	public ModelAndView dispalyResetPassword(ModelAndView mav, @RequestParam("token") String token,
			ResetPassword resetPassword) {
		if (token != null) {
			AppUser appUser = userDAO.findUserAccountByToken(token);
			if (appUser != null) {
				mav.addObject("email", appUser.getEmail());
				mav.setViewName("resetPassword");

			} else {
				mav.addObject(AppConfig.MESSAGE, "Liên kết của bạn không đúng, vui lòng kiểm tra lại !");
				mav.setViewName("notify");
			}
		} else {
			mav.addObject(AppConfig.MESSAGE, "Liên kết của bạn không đúng, vui lòng kiểm tra lại !");
			mav.setViewName("notify");
		}

		return mav;
	}

	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public ModelAndView resetPassword(ModelAndView mav, ResetPassword resetPassword,
			@RequestParam("email") String email) {
		AppUser appUser = userDAO.findUserAccountByEmail(email);
		appUserService.resetPassword(appUser, EncrytedPasswordUtils.encrytePassword(resetPassword.getPassword()));
		mav.addObject(AppConfig.MESSAGE, "Password is changed!");
		return new ModelAndView("redirect:/login");
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profilePage(ModelAndView mav, Principal principal) {
		AppUser loginedUser = userDAO.findUserAccountByEmail(principal.getName());
		mav.addObject("user", loginedUser);
		// max score cua user
		mav.addObject("maxs", (int) loginedUser.getScore()/1000);
		// lich su giao dich cua user
		mav.addObject("listTran", tranDAO.findTransactionByUser(loginedUser.getId()));
		mav.setViewName("profile");
		return mav;
	}

	@RequestMapping(value = "/profile/update", method = RequestMethod.POST)
	public ModelAndView updateProfile(ModelAndView mav , AppUser user, HttpServletRequest request,  @RequestParam("avatarimg") MultipartFile avatarimg) {
			AppUser appUser = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
			try {
			// lay duong dan cua hinh anh
			String imageurl = !avatarimg.isEmpty()? ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/download/")
	                .path(storageService.store(avatarimg))
	                .toUriString() : null;
	        // update infomation
			if(imageurl!=null) appUser.setAvatar(imageurl);
			appUser.setDayofbirth(Date.valueOf(user.getDayofbirthstr()));
			appUser.setFirstName(user.getFirstName());
			appUser.setLastName(user.getLastName());
			appUser.setGender(user.getGender());
			userRepository.save(appUser);
			return new ModelAndView("redirect:/profile");
			}catch(StorageException fse) {
				return new ModelAndView("redirect:/profile");
			}
		}
	
	
}
