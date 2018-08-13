package com.ikubinfo.certification.filter;

import java.io.IOException;
import java.security.KeyStore.PrivateKeyEntry;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ikubinfo.certification.bean.UserBean;

@WebFilter(filterName = "/AuthorizationFilter", urlPatterns = { "/*" })
public class AuthorizationFilter implements Filter {
		
		@Override
		public void destroy() {}

		@Override
		public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
				throws IOException, ServletException {
			
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			HttpServletResponse  httpServletResponse = (HttpServletResponse) servletResponse;
			HttpSession session  = ((HttpServletRequest) servletRequest).getSession(false);
		
			
			UserBean userBean = (session != null) ? (UserBean) session.getAttribute("userBean") : null;
			
			
			//Redirect if user is logged in and goes to login page
			String uri = httpServletRequest.getRequestURI();
			
			if(uri.contains("javax.faces.resource")) {
				chain.doFilter(servletRequest, servletResponse);
			}
			else if (uri.contains("resources")){
				chain.doFilter(servletRequest, servletResponse);
			}
			else if(uri.contains("login")) {
				if(userBean!=null) {
					if(userBean.getUser()!=null) {
						if(userBean.getUser().getRole().getTitle().equals("Manager")) {
							httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/administration/index.xhtml");
						}
						else if(userBean.getUser().getRole().getTitle().equals("Employee")) {
							httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/employee/index.xhtml");
						}
					}else {
						chain.doFilter(servletRequest, servletResponse);
					}
				}else {
					chain.doFilter(servletRequest, servletResponse);
				}
				
				
			}else if(uri.contains("administration")) {
				if(userBean!=null) {
					if(userBean.getUser()!=null && userBean.getUser().getRole().getTitle().equals("Manager")) {
						chain.doFilter(servletRequest, servletResponse);
					}
					else if(userBean.getUser()!=null && userBean.getUser().getRole().getTitle().equals("Employee")) {
						httpServletResponse.sendError(403,"Employee is not authorized to access these resources!");
					}
					else {
						httpServletResponse.sendError(401,"Unauthorized Access!Please log in to access this resource.");
					}
				}else {
					//chain.doFilter(servletRequest, servletResponse);
					System.out.println("User Bean is null while trying to accesss administration resources");
					httpServletResponse.sendError(401,"Unauthorized Access!Please log in to access this resource.");
				}
			}else if(uri.contains("employee")) {
				if(userBean!=null) {
					if(userBean.getUser()!=null && userBean.getUser().getRole().getTitle().equals("Manager")) {
						httpServletResponse.sendError(403,"Manager is not authorized to access these resources!");
					}
					else if(userBean.getUser()!=null && userBean.getUser().getRole().getTitle().equals("Employee")) {
						chain.doFilter(servletRequest, servletResponse);
					}
					else {
						httpServletResponse.sendError(401,"Unauthorized Access!Please log in to access this resource.");
					}
				}else {
					httpServletResponse.sendError(401,"Unauthorized Access!Please log in to access this resource.");
				}
			}else {
				if(uri.contains("error")) {
					chain.doFilter(servletRequest, servletResponse);
				}
			}
		}

		@Override
		public void init(FilterConfig config) throws ServletException {}
		
		
}
