package io.luverolla.gradi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.luverolla.gradi.rest.AuthenticationRequest;
import io.luverolla.gradi.rest.AuthenticationResponse;
import io.luverolla.gradi.security.JwtUserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController
{
    @Value("${jwt.header}")
    private String tokenHeader;
    
    @Value("${jwt.expiration}")
    private int expiration;
    
    @Autowired
    private AuthenticationManager am;
    
    @Autowired
    private JwtUserService as;
    
    /**
     * Performs authentication and attempt to get a JWT token
     *
     * @param req object that wraps username and password for authentication
     *
     * @return a {@link JwtAuthResponse} object in case of success
     *
     * @throws JsonProcessingException thrown if errors in JSON format are found
     * @throws AuthenticationException thrown if credentials are missing or wrong
     */
    @PostMapping(value = "/api/public/auth")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest req)
    { 
    	String token;
    	Authentication auth;
    	
        try {
        	auth = am.authenticate(
    			new UsernamePasswordAuthenticationToken(
    					req.getUsername(), req.getPassword()
                )
            );  
        }
        catch(AuthenticationException e) {
        	return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        SecurityContextHolder.getContext().setAuthentication(auth);
        token = as.authenticate(req);
        
        AuthenticationResponse res = new AuthenticationResponse(tokenHeader, token, expiration);
        return ResponseEntity.ok(res);
    }
    
    @GetMapping("/api/public/test")
    public ResponseEntity<?> __selftest()
    {
    	return ResponseEntity.ok(Map.of("result", "success"));
    }
}