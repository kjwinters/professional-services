package com.example.gae.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//https://spring.io/guides/gs/securing-web/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests()
		        .antMatchers("/", "/gae/**", "/login/**", "/_ah/**", "/error**").permitAll()
		        .anyRequest().authenticated()
		        .and()
		        	    
	        .exceptionHandling()
	        	.authenticationEntryPoint(new GaeAuthenticationEntryPoint())
	        	.and()
        	
        	.csrf()
	        	.disable();
	        	
    }
    
    @Override
    protected void configure(
      AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new GoogleAccountsAuthenticationProvider());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
