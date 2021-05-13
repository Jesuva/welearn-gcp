package com.welearn.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebFilter("/*")
public class DisableCacheFilter implements Filter {

   
	public void destroy() {
	}

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
			HttpServletResponse res = (HttpServletResponse) response;
			res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
	        res.setHeader("Pragma", "no-cache");
			chain.doFilter(request, response);
        
		}

	
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
