package com.example.gae.spring.push;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@WebServlet(name = "Secure HelloAppEngine", value = "/gae/secure")
@ServletSecurity(@HttpConstraint(rolesAllowed= {"admin"}))
public class HelloSecureServlet extends HttpServlet {
	
	  @Override
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws IOException {
	    UserService userService = UserServiceFactory.getUserService();
	    response.setContentType("text/plain");
	    response.getWriter().println("Hello " + userService.getCurrentUser().getEmail());
	  }
}
