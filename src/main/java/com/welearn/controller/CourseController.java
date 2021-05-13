package com.welearn.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.datastore.Entity;
import com.welearn.dao.CourseInterface;
import com.welearn.dao.UserDao;
import com.welearn.dao.UserInterface;
import com.welearn.model.Course;
import com.welearn.model.Login;
import com.welearn.model.User;

@Controller
public class CourseController {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Autowired
	private CourseInterface courseInterface;
	
	@GetMapping("/user/enrollcourse")
	public ModelAndView enrollcourse(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		String email = (String) session.getAttribute("email");
		ModelAndView mv = new ModelAndView();
		mv.addObject("courses", courseInterface.getAllAvailableCourses(email));
		mv.setViewName("/user/enrollcourse");
		logger.info("Displayed Enroll-Course Page");
		return mv;
	}
	
	
	@GetMapping("/user/add-course")
	public ModelAndView showAddCoursePage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/addCourse");
		return mv;
	}
	
	@PostMapping("/user/add-course")
	public ModelAndView addCourse(HttpServletRequest req,HttpServletResponse res) throws IOException {
		
		
		try {
			HttpSession session = req.getSession(false);
			String name = req.getParameter("courseName");
			String price = req.getParameter("price");
			String chapters = req.getParameter("chapters");
			String description = req.getParameter("courseDescription");		
			String email = (String) session.getAttribute("email");
			boolean isNameAvailable = courseInterface.checkCourseName(name);
			if(isNameAvailable==true) {
				boolean isAdded = courseInterface.addCourse(name, price, description, chapters, email);
				if(isAdded) {
					
					ModelAndView mv = new ModelAndView("redirect:/user/add-course-success");
					logger.info("New Course Added");
					mv.addObject("courseTitle",name);
					return mv;

				}
				else {
					logger.info("New Course Not Added");
					return new ModelAndView("redirect:/user/add-course");
				}

			}
			else {
				ModelAndView mv = new ModelAndView();
				mv.setViewName("/user/addCourse");
				logger.info("course name not available error");
				mv.addObject("courseNameError","Course Name not available, Try some unique name");
				return mv;
				
			}
						
		} catch (Exception e) {
			logger.warning(e.getMessage());
			return null;
		}
		
		
		
	}
	
	@GetMapping ("/user/add-course-success")
	public ModelAndView showAddCourseConfirmPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/addCourseSuccess");
		logger.info("Displayed Course Added Sucess page");
		return mv;
	}
	
	@GetMapping("/user/{courseId}")
	public ModelAndView showCourseDetails(@PathVariable int courseId) {
		System.out.println(courseId);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/courseDetails");
		mv.addObject("course", courseInterface.getCourseDetails(courseId));
		
		return mv;
	}
	
	@GetMapping("/user/view-course-created-by-you")
	public ModelAndView showCoursesCreatedByUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		String email = (String) session.getAttribute("email");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/courseCreatedByUser");
		mv.addObject("courses",courseInterface.getCourseCreatedByUser(email));
		return mv;
	}
	
	@PostMapping("/user/course-enroll")
	public ModelAndView enrollCourse(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		String email = (String) session.getAttribute("email");
		long courseId = Long.parseLong(req.getParameter("selectedCourse"));
		ModelAndView mv = new ModelAndView();
		Entity course = courseInterface.enrollCourse(email, courseId);
		if(course!=null) {
			mv.addObject("course",course);
			mv.setViewName("/user/enrollCourseSuccess");
		}
		else {
			mv.setViewName("/user/enrollCourse");
		}
		return mv;
	}
	
	@GetMapping("/user/enrolled-courses")
	public ModelAndView showEnrolledCourses(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		ModelAndView mv = new ModelAndView();
		String email = (String) session.getAttribute("email");
		mv.setViewName("/user/enrolledCourses");
		mv.addObject("courses",courseInterface.getAllEnrolledCourses(email));
		return mv;
		
	}
	
	@PostMapping("/user/course-uneroll")
	public ModelAndView unerollCourse(HttpServletRequest req) {
		String email = (String) req.getSession(false).getAttribute("email");
		long courseId = Long.parseLong(req.getParameter("selectedCourse"));
		Entity course = courseInterface.unenrollCourse(email, courseId);
		if (course!=null) {
			ModelAndView mv = new ModelAndView("redirect:/user/course-unenrolled-success/"+course.getProperty("courseName"));
			mv.addObject("course",course);
			System.out.println(course);
			return mv;
		}
		else {
			ModelAndView mv = new ModelAndView("redirect:/user/enrolled-courses");
			mv.addObject("Error","Error Unenrolling,Try again some time!");
			return mv;
			
		}
	}
	
	@GetMapping("/user/course-unenrolled-success/{name}")
	public ModelAndView showUnenrolledCourse(@PathVariable String name) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("name",name);
		mv.setViewName("/user/unenrollCourseSuccess");
		return mv;
	}
	
	@GetMapping("/user/update-course")
	public ModelAndView  showUpdateCourse(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		long courseId = Long.parseLong(req.getParameter("selectedCourse"));
		mv.setViewName("/user/courseUpdate");
		mv.addObject("course", courseInterface.getCourseDetails(courseId));		
		return mv;
	}
	
	@PostMapping("/user/update-course")
	public ModelAndView updateCourse(HttpServletRequest req) {
		String name = req.getParameter("courseName");
		String price = req.getParameter("price");
		String chapters = req.getParameter("chapters");
		String description = req.getParameter("courseDescription");
		long courseId = Long.parseLong(req.getParameter("courseId"));
		HttpSession session  = req.getSession(false);
		String email = (String)session.getAttribute("email");
		if(courseInterface.checkCourseName(name)) {
			boolean isUpdated = courseInterface.updateCourse(name, price, description, chapters, courseId,email);
			if (isUpdated==true) {
				ModelAndView mv = new ModelAndView();
				mv.setViewName("/user/courseUpdateSuccess");
				mv.addObject("name",name);
				return mv;
			}
			
		}
		else {
			ModelAndView mv = new ModelAndView();
			mv.setViewName("/user/view-course-created-by-you");
			return mv;
			
		}
		return null;
		
	}
}
