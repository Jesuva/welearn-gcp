package com.welearn;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.welearn.controller.AuthenticationController;
import com.welearn.dao.UserDao;
import com.welearn.dao.UserInterface;

public class UserTest {
	
	HttpSession session = PowerMockito.mock(HttpSession.class);
	HttpServletRequest req = PowerMockito.mock(HttpServletRequest.class);
	HttpServletResponse res = PowerMockito.mock(HttpServletResponse.class);

	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Mock
	UserInterface userInterface;
	
	@InjectMocks
	AuthenticationController authenticationController = null;


	@Rule public MockitoRule rule = MockitoJUnit.rule();
	
	@Before
	public void setUp() {
		authenticationController = new AuthenticationController(userInterface);

	}
	
	public void setMockSession() {
		try {
			when(req.getSession()).thenReturn(session);
			session.setAttribute("name", "jesuva");
			session.setAttribute("email", "test@testing.com");
		}
		catch(NullPointerException npe) {
			logger.severe("Session object returns Null value, Check the Session Object");
		}
		catch(Exception e) {
			logger.severe(e.getMessage());
		}
	}
	
	@Test
	public void showIndexPageMethodTest() {
		assertEquals("index",authenticationController.home().getViewName());
	}
	
	@Test
	public void fail_loginWithAllNullValuesTest() {
		when(userInterface.findUser(null, null)).thenReturn(false);
		assertEquals("index",authenticationController.login(null, null, null).getViewName());
	}
	
	@Test
	public void success_loginTest() {
		try {
			setMockSession();
			when(userInterface.findUser("test", "testing")).thenReturn(true);
			assertEquals("redirect:/user/enrollcourse",authenticationController.login("test", "testing", req).getViewName());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void success_signupTest() {
		try {			
			setMockSession();
			PowerMockito.when(req.getParameter("name")).thenReturn("jesuva");
			when(req.getParameter("email")).thenReturn("test@testing.com");
			when(req.getParameter("password")).thenReturn("asDF!@");
			when(userInterface.checkUserMail("test@testing.com")).thenReturn(true);
			assertEquals("redirect:/user/enrollcourse",authenticationController.signup(req, null, null).getViewName());
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	@Test 
	public void success_logout() {
		try {
			setMockSession();
			session.invalidate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

//static method over instance method
//protocols 
//try catch block