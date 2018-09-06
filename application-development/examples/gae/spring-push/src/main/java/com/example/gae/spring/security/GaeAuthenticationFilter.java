/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.gae.spring.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author Luke Taylor
 * https://github.com/spring-projects/spring-security/blob/master/samples/xml/gae/src/main/java/samples/gae/security/GaeAuthenticationFilter.java
 */
@Component
public class GaeAuthenticationFilter extends GenericFilterBean {

	private final Logger logger = Logger.getLogger(getClass().getName());

	private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> ads = new WebAuthenticationDetailsSource();
	
	@Autowired
	private AuthenticationManager authenticationManager;
	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		User googleUser = UserServiceFactory.getUserService().getCurrentUser();

		if (authentication != null
				&& !loggedInUserMatchesGaeUser(authentication, googleUser)) {
			SecurityContextHolder.clearContext();
			authentication = null;
			((HttpServletRequest) request).getSession().invalidate();
		}

		if (authentication == null) {
			if (googleUser != null) {
				logger.info("Currently logged on to GAE as user " + googleUser);
				logger.info("Authenticating to Spring Security");
				// User has returned after authenticating via GAE. Need to authenticate
				// through Spring Security.
				PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
						googleUser, null);
				token.setDetails(ads.buildDetails((HttpServletRequest) request));

				try {
					authentication = authenticationManager.authenticate(token);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
				catch (AuthenticationException e) {
					failureHandler.onAuthenticationFailure((HttpServletRequest) request,
							(HttpServletResponse) response, e);

					return;
				}
			}
		}

		chain.doFilter(request, response);
	}

	public static <T> T as(Class<T> clazz, Object o){
	    if(clazz.isInstance(o)){
	        return clazz.cast(o);
	    }
	    return null;
	}
	
	private boolean loggedInUserMatchesGaeUser(Authentication authentication,
			User googleUser) {
		assert authentication != null;

		if (googleUser == null) {
			// User has logged out of GAE but is still logged into application
			return false;
		}

		User gaeUser = as(User.class, authentication.getPrincipal());

		if (gaeUser == null || !gaeUser.getEmail().equals(googleUser.getEmail())) {
			return false;
		}

		return true;

	}

	@Override
	public void afterPropertiesSet() throws ServletException {
		Assert.notNull(authenticationManager, "AuthenticationManager must be set");
	}

}
