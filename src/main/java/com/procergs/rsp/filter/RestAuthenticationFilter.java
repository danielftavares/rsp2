package com.procergs.rsp.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.procergs.rsp.user.UserLoginService;

public class RestAuthenticationFilter implements Filter {

	
	public static final String AUTHENTICATION_HEADER = "Authorization";
	
	public static final String URL_LOGIN = "/user/login";
	
	public static final String URL_IMAGE = "/image/";
	
	@Inject
	UserLoginService userLoginService;
	
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);
			
			boolean authenticationStatus = false;
			
			if(httpServletRequest.getRequestURI().endsWith(URL_LOGIN) || httpServletRequest.getRequestURI().contains(URL_IMAGE)){
				authenticationStatus = true;
			} else {
				try{
					authenticationStatus = userLoginService.autenticar(authCredentials, request);
				} catch (Throwable t){
					authenticationStatus = false;
				}
			}
			
			if (authenticationStatus) {
				chain.doFilter(request, response);
			} else {
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}

	}

	public void destroy() {

	}

}
