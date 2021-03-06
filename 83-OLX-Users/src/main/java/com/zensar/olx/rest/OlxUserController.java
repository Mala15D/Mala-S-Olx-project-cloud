package com.zensar.olx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.olx.bean.LoginResponse;
import com.zensar.olx.bean.LoginUser;
import com.zensar.olx.bean.OlxUser;
import com.zensar.olx.db.TokenStorage;
import com.zensar.olx.security.jwt.util.JwtUtil;
import com.zensar.olx.service.OlxUserService;

@RestController
public class OlxUserController {

	@Autowired
	OlxUserService service;

	@Autowired
	private AuthenticationManager manager; //it is predefined by 

	@Autowired
	private JwtUtil util;

	/*
	 * This is the rest specification for authenticating token for user details
	 * 
	 * 
	 * */

	@PostMapping("/user/authenticate")
	public LoginResponse login(@RequestBody LoginUser loginUser) {

		UsernamePasswordAuthenticationToken authenticationToken;
		authenticationToken =new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword());

		try {
			manager.authenticate(authenticationToken);  //this method is built in method..may be its throw an exception  we have to provide try catch block

			//2 If user is authenticated generate token and return it
			String token=util.generateToken(loginUser.getUserName());
			 TokenStorage.storeToken(token, token);
			
			LoginResponse loginResponse=new LoginResponse();
			loginResponse.setJwt(token);
			return loginResponse;
		}
		catch (Exception e) {

			// 3 if user is not athenticated throw exception 
			e.printStackTrace();
			throw e;
		}

	}




	@PostMapping("/user")
	public OlxUser addOlxUser(@RequestBody OlxUser olxUser) {
		return this.service.addOlxUser(olxUser);
	}

	@GetMapping("/user/{uid}")
	public OlxUser findOlxUserById(@PathVariable(name ="uid")int  id) {
		return this.service.findOlxUser(id);
	}

	@GetMapping("/user/find/{userName}")
	public OlxUser findOlxUserByName(@PathVariable(name = "userName") String name) {
		return this.service.findOlxUserByName(name);

	}

	@GetMapping("/token/validate")
	public ResponseEntity<Boolean> isValidateUser(@RequestHeader("Authorization") String authToken){
		try {
			String validateToken=util.validateToken(authToken.substring(7));
			return new ResponseEntity<Boolean>(true,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/user/logout")
	public ResponseEntity<Boolean> logout(@RequestHeader("Authorization") String authToken){
			String token=authToken.substring(7);
			try {
			//Delete the token from the cache
				TokenStorage.removeToken(token);
				ResponseEntity<Boolean> responseEntity=new ResponseEntity<Boolean>(true,HttpStatus.OK);
				return responseEntity;
			}catch (Exception e) {
				ResponseEntity<Boolean> responseEntity=new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
				return responseEntity;
				// TODO: handle exception
			}
	}
}


