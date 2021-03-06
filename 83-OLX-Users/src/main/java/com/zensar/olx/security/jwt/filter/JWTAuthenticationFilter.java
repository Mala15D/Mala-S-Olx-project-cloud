package com.zensar.olx.security.jwt.filter;

//
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.zensar.olx.db.TokenStorage;


//This is custom filter
//You need to add this filer in spring security Filter chain otherwise it is not executed

public class JWTAuthenticationFilter  extends BasicAuthenticationFilter{

	private String authorizationHeader="Authorization";
	private final String BEARER="Bearer ";
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		com.zensar.olx.security.jwt.util.JwtUtil jwtUtil=new com.zensar.olx.security.jwt.util.JwtUtil();

		System.out.println("IN doFilterInternal");
		//1. check if user has passed token,we do that by fetching value from Authorization header
		String authorizationHeaderValue=request.getHeader(authorizationHeader);

		//if token is not passed OR if it does not start with Bearer
		//Don't do anything proceed o next filter in chain

		if(authorizationHeaderValue==null || !authorizationHeaderValue.startsWith(BEARER)) {
			chain.doFilter(request, response); //invoke next security filter in chain
			return;
		}
		if(authorizationHeaderValue!=null && authorizationHeaderValue.startsWith(BEARER))
		{
			String token=authorizationHeaderValue.substring(7);
			if(token!=null) {
				//Authorization Bearer token
				System.out.println("Authorization Value "+authorizationHeaderValue);
				System.out.println("Token Value------>"+token);
			
				//check if this token exists in cache
				String tokenExists=TokenStorage.getToken(token);

				//if token is null means user has loggedout
				if(tokenExists==null) {
					chain.doFilter(request, response); //invoke next security filter in chain
					return;
				}
				//validate the token
				try {
					String encodedPayload=jwtUtil.validateToken(token);
					//if token is valid
					String payload=new String(Base64.getDecoder().decode(encodedPayload));
					//From this payload we need to fetch username
					JsonParser jsonParser=JsonParserFactory.getJsonParser();
					Map<String,Object>parseMap=jsonParser.parseMap(payload);
					String username=(String) parseMap.get("username");
					//create UsernamePasswordAuthenticationToken
					UsernamePasswordAuthenticationToken authenticationToken;
					authenticationToken=new UsernamePasswordAuthenticationToken(username,null,
							AuthorityUtils.createAuthorityList("ROLE_USER"));

					//Authenticate user
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
				catch(Exception e) {

					//if token is not valid
					e.printStackTrace();
				}
				System.out.println("Authorization value"+authorizationHeaderValue);
				//2 if token not present ask user to login

				//3 If token present fetch it and validates it
			}
		}
		chain.doFilter(request, response);
	}
}