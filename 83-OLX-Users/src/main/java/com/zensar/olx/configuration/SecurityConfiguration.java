package com.zensar.olx.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zensar.olx.security.jwt.filter.JWTAuthenticationFilter;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	//UserDetailsService is an interface given by spring security
	//this interface has only one method loadUserByUserName(String userName )
	//this method is responsible for loading the user object rom database
	//if user object couldn't found in database this method should throw userName not foud exception
	//it is responsible for give implementation for this interface
	@Autowired
	private UserDetailsService userDetailsService;

	//HttpStatus code 401 which specify that user is not passing right username and password
	//authentication
	
	//following Bean is used for password encoding
		@Bean
		public PasswordEncoder getPasswordEncoder() {
			BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
			return passwordEncoder;
		}
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			//.inMemoryAuthentication() //we are storing credintials in memory
			//.withUser("zensar")  //user name
			//.password("$2a$10$vT9wd8wsIpy2Q5MWNyTX.eWg6z/Ti0E8wgxmZShF2CmSHlIpGnysq") //password stored here MUST be in encoded form
			 //this is bad to stored password in plain text
			//we must store password in encoded form
			//BCryptPasswordEncoder is recommended for password encodin
			//.roles("USER") //role
		//.and()
			.userDetailsService(userDetailsService)
			
			.passwordEncoder(getPasswordEncoder()) ;//this line tells spring security to use BCryptPasswordEncoder;
	}
	
	//Authorization-specifying access rights to a resource
	//Access based on Roles
	//what are you allowed to?
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/user/authenticate","/token/validate").permitAll()			
			.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.addFilter(new JWTAuthenticationFilter(authenticationManager()))//this ask  to user  to create enter for username and password
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS); //this is must for REST webservice 
																															//Rest should be stateless				
	}
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
}
