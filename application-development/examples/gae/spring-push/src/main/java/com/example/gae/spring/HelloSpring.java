package com.example.gae.spring;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSpring {	
	
	@GetMapping("/spring/hello")
	public void hello(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if ( request.isUserInRole("ROLE_ADMIN") ) {
			response.sendRedirect("/spring/admin");
		} else {
			response.getWriter().write("Hello, from Spring Boot!");
		}
	}
	

	@GetMapping("/spring/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String secure() {
		return "Hello Admin, from Spring Boot!";
	}
	  
}
