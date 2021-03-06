package com.zensar.olx.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zensar.olx.bean.OlxUser;
import com.zensar.olx.db.OlxUserDAO;

@Service
public class OlxUserDetailsService implements UserDetailsService {

	@Autowired
	private OlxUserDAO repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("In loadUserByUsername ");

		//To do talk to database and fetch user details
		OlxUser foundUser=repository.findByUserName(username);
		if(foundUser==null) {
			throw new UsernameNotFoundException(username);

		}

		System.out.println("In loadUserByUsername ");
		//UserDetails is a interface given by spring security
		//we are free to implement interfaces for simplicity spring security has given class
		//implements UserDetails information
		//Name of the class is User
		//In this method we need to create object of user and return it
	//	if(username.equals("zensar")) {
		String roles=foundUser.getRoles();	
		//BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder(); 

		
		User authenticatedUser=new User(foundUser.getUserName(), foundUser.getPassword() ,
					AuthorityUtils.createAuthorityList(roles));
			return authenticatedUser;
		//}
		//throw new UsernameNotFoundException(username);
	}

}
