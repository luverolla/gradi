package io.luverolla.gradi.security;

import java.util.Collections;
import java.util.List;

import io.luverolla.gradi.entities.User;
import io.luverolla.gradi.repositories.UserRepository;
import io.luverolla.gradi.rest.AuthenticationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link UserDetailsService}.
 * 
 * <p>This contains method useful for retrieving user authentication info.</p>
 */
@Component
public class JwtUserService implements UserDetailsService
{
    @Autowired
    private UserRepository repo;
    
    @Autowired
    private JwtTokenUtil ju;
    
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        User us = repo.findByEmail(s).orElseThrow(() -> new UsernameNotFoundException(""));
        
        System.out.println(us.getRole().toString());
        GrantedAuthority au = new SimpleGrantedAuthority("ROLE_" + us.getRole().toString());
        List<GrantedAuthority> authorities = Collections.singletonList(au);
        
        return new JwtUser(us, true, null, authorities);
    }
    
    public String authenticate(AuthenticationRequest req)
    {
    	return ju.generateToken(loadUserByUsername(req.getUsername()));
    }
}
