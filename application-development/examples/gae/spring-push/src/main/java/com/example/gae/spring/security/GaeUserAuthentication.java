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

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Authentication object representing a fully-authenticated user.
 * 
 * @author Luke Taylor
 * 
 * https://raw.githubusercontent.com/spring-projects/spring-security/master/samples/xml/gae/src/main/java/samples/gae/security/GaeUserAuthentication.java
 */
public class GaeUserAuthentication implements Authentication {
	private final User principal;
	private final Object details;
	private boolean authenticated;

	public GaeUserAuthentication(User principal, Object details) {
		this.principal = principal;
		this.details = details;
		authenticated = true;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		UserService userService = UserServiceFactory.getUserService();
		return new HashSet<>(EnumSet.of(userService.isUserAdmin() ? AppRole.ADMIN : AppRole.USER));
	}

	public Object getCredentials() {
		throw new UnsupportedOperationException();
	}

	public Object getDetails() {
		return null;
	}

	public Object getPrincipal() {
		return principal;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		authenticated = isAuthenticated;
	}

	public String getName() {
		String nickname = principal.getNickname();
		if (nickname == null || nickname == "") {
			return principal.getEmail();
		} else {
			return nickname;
		}
	}

	@Override
	public String toString() {
		return "GaeUserAuthentication{" + "principal=" + principal + ", details="
				+ details + ", authenticated=" + authenticated + '}';
	}
}