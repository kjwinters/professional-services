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

import com.google.appengine.api.users.User;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * A simple authentication provider which interacts with {@code User} returned by the GAE
 * {@code UserService}, and also the local persistent {@code UserRegistry} to build an
 * application user principal.
 * <p>
 * If the user has been authenticated through google accounts, it will check if they are
 * already registered and either load the existing user information or assign them a
 * temporary identity with limited access until they have registered.
 * <p>
 * If the account has been disabled, a {@code DisabledException} will be raised.
 *
 * @author Luke Taylor
 * 
 * https://raw.githubusercontent.com/spring-projects/spring-security/master/samples/xml/gae/src/main/java/samples/gae/security/GoogleAccountsAuthenticationProvider.java
 */
public class GoogleAccountsAuthenticationProvider implements AuthenticationProvider {

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		User googleUser = (User) authentication.getPrincipal();

		return new GaeUserAuthentication(googleUser, authentication.getDetails());
	}

	/**
	 * Indicate that this provider only supports PreAuthenticatedAuthenticationToken
	 * (sub)classes.
	 */
	public final boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
	}
}