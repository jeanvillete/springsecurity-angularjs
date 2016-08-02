package com.example;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UiApplication {

	/* This is a useful trick in a Spring Security application. 
	 * If the "/user" resource is reachable then it will return the currently authenticated 
	 * user (an Authentication), and otherwise Spring Security will intercept the request 
	 * and send a 401 response through an AuthenticationEntryPoint. */
	@RequestMapping( "/user" )
	public Principal user( Principal user ) {
		return user;
	}
	
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			String[] publicResources = new String[]{
				"/",
				"/index.html",
				"/home.html",
				"/login.html",
				"/css/*",
				"/js/*",
			};
			http
				.httpBasic()
			.and()
				.authorizeRequests()
					.antMatchers( publicResources ).permitAll()
					.anyRequest().authenticated()
			/*
			 * If you start with a clean browser (e.g. incognito in Chrome), the very first 
			 * request has no cookies going off to the server, but the server sends back 
			 * "Set-Cookie" for "JSESSIONID" (the regular HttpSession) and "X-XSRF-TOKEN" 
			 * (the CRSF cookie that we set up above). Subsequent requests all have those cookies
			 * , and they are important: the application doesn’t work without them, and they 
			 * are providing some really basic security features (authentication and CSRF protection). 
			 * The values of the cookies change when the user authenticates (after the POST) and 
			 * this is another important security feature (preventing session fixation attacks).
			 */
			.and()
				.csrf()
					.csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse() );
		}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(UiApplication.class, args);
	}
}
