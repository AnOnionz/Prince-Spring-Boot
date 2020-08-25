package com.prince.project.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import com.prince.project.DAO.PostDAO;
import com.prince.project.DAO.UserDAO;
import com.prince.project.DAO.WatchDAO;
import com.prince.project.config.AppConfig;
import com.prince.project.model.AppUser;
import com.prince.project.model.Category;
import com.prince.project.model.Post;
import com.prince.project.model.WatchHistory;
import com.prince.project.repository.CategoryRepository;
import com.prince.project.repository.PostRepository;
import com.prince.project.repository.UserRepository;
import com.prince.project.repository.WatchHistoryRepository;
import com.prince.project.service.PostService;
import com.prince.project.service.StorageException;
import com.prince.project.service.StorageService;
import com.prince.project.utils.WebUtils;

@Controller
public class MainController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private WatchHistoryRepository watchHistoryRepository;
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private WatchDAO watchDAO;
	
	@Autowired
    StorageService storageService;
	
	@Autowired
    PostService postService;
	
	@RequestMapping(value = { "/"}, method = RequestMethod.GET)
	public String landingPage() {
		
		return "redirect:/home";
	}
	
	@RequestMapping(value = { "/home"}, method = RequestMethod.GET)
	public ModelAndView HomePage(ModelAndView mav, HttpServletRequest request, HttpSession session) {
		AppUser appUser = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
		
		// lay quang cao co the xem
		List<Post> recentPosts = postDAO.findPostRecent(appUser.getId());
		mav.addObject("recentPosts", recentPosts);
		mav.setViewName("home");
		return mav;
	}
	@RequestMapping(value = { "/post/step1" }, method = RequestMethod.GET)
	public ModelAndView displayUploadPost(ModelAndView mav, HttpServletRequest request, Post post) {
		//lay danh sach the loai de hien thi
		List<Category> categories = categoryRepository.findAll();
		mav.addObject("categories", categories);
		mav.setViewName("step1");
		return mav;
	}
	@RequestMapping(value = "/post/step2", method = RequestMethod.POST)
	public ModelAndView step1(ModelAndView mav,Post post, BindingResult result, HttpServletRequest request) {
		// kiem tra rang buoc giua form voi data bean
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return new ModelAndView("redirect:/post/step1");
		} 
			// update post
			AppUser appUser = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
			post.setAuthor(appUser);
			post.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			post.setCategory(categoryRepository.getOne(post.getSelectedCat()));
			//luu post vao session
			postService.setPresentPost(request, post);
			mav.addObject("categoryName", post.getCategory().getName());
			mav.addObject("username",appUser.getName());
			mav.setViewName("step2");
			System.out.println(post.toString());
		
		return mav;
	}
	@RequestMapping(value = "/post/step3", method = RequestMethod.POST, consumes = {"multipart/form-data"})
	public ModelAndView step2(ModelAndView mav, Post postObj, BindingResult result, HttpServletRequest request, @RequestParam("file-input1") MultipartFile image,
			@RequestParam("file-input2") MultipartFile image1, @RequestParam("file-input3") MultipartFile image2) {
		// kiem tra data tu form
		if (result.hasErrors() || postObj == null) {
			System.out.println(result.getAllErrors());
			return new ModelAndView("redirect:/post/step2");
		} else {
			try {
			// tao duong dan tuyet doi cho hinh anh
			String imageurl = !image.isEmpty()? ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/download/")
	                .path(storageService.store(image))
	                .toUriString() : null;
	        String imageurl1 = !image1.isEmpty()? ServletUriComponentsBuilder.fromCurrentContextPath()
	    	        .path("/download/")
	    	        .path(storageService.store(image1))
	    	        .toUriString() : null;
	    	String imageurl2 = !image2.isEmpty()? ServletUriComponentsBuilder.fromCurrentContextPath()
	    	    	.path("/download/")
	    	    	.path(storageService.store(image2))
	    	    	.toUriString() : null;
	    	    	
	    	// lay post da luu trong session
			Post post = postService.getPresentPost(request);
			// update post
			post.setTitle(postObj.getTitle().trim());
			post.setSubTitle1(postObj.getSubTitle1().trim());
			post.setSubTitle2(postObj.getSubTitle2().trim());
			post.setContent1(postObj.getContent1().trim());
			post.setContent2(postObj.getContent2().trim());
			post.setFigure1(postObj.getFigure1().trim());
			post.setFigure2(postObj.getFigure2().trim());
			post.setImage(imageurl);
			post.setImage1(imageurl1);
			post.setImage2(imageurl2);
			post.setClick(0);
			post.setExtend(0);
			post.setScore(post.costPerClick());
			post.setStatus(AppConfig.NEW);
			post.setVisiter(0);
			// them post vao danh sach thanh toan
			postService.updateListPayment(request, post);
			//chuyen den trang thanh toan
			return new ModelAndView("redirect:/payment");
			}catch(StorageException fse) {
				mav.setViewName("step2");
			}
		}
		return mav;
	}
	@RequestMapping(value = "/post/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(ModelAndView mav, HttpServletRequest request) {
		//xoa post khoi session
		//tro ve man hinh home
		postService.deletePresentPost(request);
		return new ModelAndView("redirect:/home");
	}
	@RequestMapping(value = "/post/list", method = RequestMethod.GET)
	public ModelAndView displayListPost(ModelAndView mav, HttpServletRequest request) {
		AppUser appUser = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
		//lay danh sach quang cao user đã đăng 
		List<Post> listPost = postDAO.findPostByUser(appUser.getId()); 
		mav.addObject("listPayment", postService.listPayment);
		mav.addObject("listPost", listPost);
		mav.addObject("user",appUser);
		mav.setViewName("listPost");
		return mav;
	}
	@RequestMapping(value = "/post/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteMyPost(ModelAndView mav,@PathVariable int id, HttpServletRequest request) {
		AppUser user = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
		// lay post theo id
		Post post = postService.getPost(id);
		// kiem tra neu khong phai tac gia thi thong bao loi
		if(post.getAuthor() != user) {
			mav.addObject(AppConfig.MESSAGE, "You haven't permission");
			mav.setViewName("notify");
			return mav;
		}
		// doi status cua post ve deleted
		post.setStatus(AppConfig.DELETED);
		postRepository.save(post);
		return new ModelAndView("redirect:/post/list");
	}
	@RequestMapping(value = "/post/view/{id}", method = RequestMethod.GET)
	public ModelAndView viewPost(ModelAndView mav,@PathVariable int id, HttpServletRequest request) {
		AppUser appUser = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
		try {
		// lay post theo id
		Post post = postService.getPost(id);
		mav.addObject("view", post);
		// kiem tra nguoi da xem quang cao nay chua
		WatchHistory history = watchDAO.getWatchHistory(appUser.getId(), post.getId()) ;
		// neu khong phai tac gia va chua xem thi them vao lich su xem
		if(post.getAuthor() != appUser && history == null) {
			WatchHistory w = new WatchHistory();
			w.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			w.setUser(appUser);
			w.setPost(post);
			w.setIsClick(false);
			watchHistoryRepository.save(w);
		}
		// them 1 luot xem cho quang cao
		postService.PostVisited(post);
		// nếu đã click nhận điểm thì không hiển thị button nhận điểm nữa 
		mav.addObject("clicked", history!=null && history.getIsClick() == true ? "true" : "false");
		} catch (EntityNotFoundException e) {
			mav.addObject("view", "NoData");
		}
		mav.addObject("userId",appUser.getId());
		mav.addObject("username",appUser.getName());
		mav.setViewName("post");
		return mav;
	}
	@RequestMapping(value = "/view/watched", method = RequestMethod.POST)
	public @ResponseBody String watchedPost(@RequestParam Integer id, HttpServletRequest request) {
		int postId = id;
		AppUser appUser = userDAO.findUserAccountByEmail(request.getUserPrincipal().getName());
		// lay post theo id
		try {
			Post post = postService.getPost(postId);
			// kiem tra lich su xem
			WatchHistory history = watchDAO.getWatchHistory(appUser.getId(), post.getId()) ;
			// kiem tra neu nguoi xem khong phai tác giả thi cộng điểm vào tài khoản 
			if(post.getAuthor() != appUser) {
				// update so click cua post
				postService.PostWatched(post);
				appUser.setScore(appUser.getScore() + post.getScore());
				userRepository.save(appUser);
			}
			// cap nhat là đã click
			if(history!=null) {
				history.setIsClick(true);
				watchHistoryRepository.save(history);
			}
			
		}catch (EntityNotFoundException e) {
			return "false";
		}
		
		return "true";
	}
	@RequestMapping(value = "/post/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editOrder(ModelAndView mav, HttpServletRequest request,@PathVariable int id) {
		//	dua post vao seesion
		postService.getOrder(request, id);
		// xoa post cu
		postService.deleteOrder(id);
		return new ModelAndView("redirect:/post/step1");
	}	

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();

			String userInfo = WebUtils.toString(loginedUser);

			model.addAttribute("userInfo", userInfo);

			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);

		}

		return "403Page";
	}
	// trang hiển thi file
	@GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	


}
