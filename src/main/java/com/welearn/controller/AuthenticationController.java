package com.welearn.controller;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.ModelAndView;
import com.welearn.dao.UserInterface;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.welearn.AppLogger;
import com.welearn.model.Login;
import com.welearn.model.User;

import jakarta.validation.Valid;
@Controller
public class AuthenticationController {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public AuthenticationController(UserInterface userService) {
		this.userInterface = userService;
	}
	
	AppLogger applog = new AppLogger(); 
	
	@Autowired
	private UserInterface userInterface;
	
	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");	    
		return mv;
	}
	
	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("index");
		return mv;
	}
	
	@PostMapping("/login")
	public ModelAndView login(@RequestParam("userMail") String email,@RequestParam("userPassword") String password,HttpServletRequest request ) {
		ModelAndView mv = new ModelAndView();
		try {
			boolean user = userInterface.findUser(email, password);
			mv.clear();
			if(user) {
				HttpSession session =request.getSession();
				session.setAttribute("name","test");
				session.setAttribute("email", email);
				logger.info("User Logged In");
				return new ModelAndView("redirect:/user/enrollcourse");
				
			}
			else {
				mv.setViewName("index");
				mv.addObject("loginError","Invalid Credentials!");
				logger.warning("Invalid User Credentials");
				return mv;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
		}
		return mv;
		
	}
	
	@GetMapping("/signup")
	public ModelAndView signup(Model model) {
		model.addAttribute("user", new User());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("signup");
		logger.info("Display Signup Page");
		return mv;
	}
	
	@PostMapping("/signup")
	public ModelAndView signup(HttpServletRequest req,@Valid User user, BindingResult br) {
		ModelAndView mv = new ModelAndView();
		try {
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			String password = req.getParameter("password");
			boolean emailCheck = Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",email);
			boolean passwordCheck = Pattern.matches("^(?=.*[a-z]{2})(?=.*[A-Z]{2})(?=.*[!@#&()�[{}]:;',?/*~$^+=<>]{2}).{6,20}$", password);
			mv.clear();
			if(emailCheck==false) {
				mv.addObject("emailError","Please Enter Valid Email");
				logger.info("Invaild Email Error");
			}
			if(passwordCheck==false) {
				mv.addObject("passwordError","Please Use Strong Password");
				logger.info("Invalid Password Error");
			}
			
			if(userInterface.checkUserMail(email)==true && emailCheck==true && passwordCheck==true) {
				HttpSession session = req.getSession();
				session.setAttribute("name", name);
				session.setAttribute("email", email);
				userInterface.addUser(name,email,password);
				logger.info("User Signed Up succesfull");
				return new ModelAndView("redirect:/user/enrollcourse");
			}
			if(userInterface.checkUserMail(email)==false) {
				mv.addObject("emailError","Email Already Exists, Try Login!");
				logger.info("Email Already Exists Error");

			}
			
		}
		catch(Exception e) {
			mv.setViewName("index");
			mv.addObject("signupError", "Invalid Credentials");
			e.printStackTrace();
		}
		
		return mv;
	}
	
	@GetMapping("/logout")
	public void logout(HttpServletRequest request,HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(false);
			session.invalidate();
			logger.info("User logged out");
			response.sendRedirect("/login");
		} 
		catch (IOException e) {
			logger.warning(e.getMessage());
		}
	}
}
