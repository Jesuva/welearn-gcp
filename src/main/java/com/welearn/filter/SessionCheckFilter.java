package com.welearn.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns="/user/*")
public class SessionCheckFilter implements Filter {

    
    
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		try {
		HttpSession session = req.getSession(false);	
		if(session!=null) {
			chain.doFilter(request, response);
		}
		else {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<meta http-equiv='refresh' content='2;URL=../login'>");
			out.println("<h3 style='color:red;text-align:center;margin-top:15%'>User Session Expired, Please Login!</h3>");

		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public void init(FilterConfig fConfig) throws ServletException {
	}

}


