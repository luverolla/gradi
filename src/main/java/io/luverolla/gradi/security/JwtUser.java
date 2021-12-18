package io.luverolla.gradi.security;

import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import io.luverolla.gradi.entities.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementation of {@link UserDetails}.
 * 
 * This will contains user authentication details
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser implements UserDetails
{
	private static final long serialVersionUID = 1L;
	
	private User user;
    private boolean enabled;
    private Date lastPasswordResetDate;
    private Collection<? extends GrantedAuthority> authorities;
    
    @Override
    public String getUsername()
    {
        return user.getEmail();
    }
    
    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }
}
