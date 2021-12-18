package io.luverolla.gradi.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.luverolla.gradi.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Bean for JWT token processing
 */
@Component
public class JwtTokenUtil implements Serializable
{
    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "iat";
    static final String CLAIM_KEY_AUTHORITIES = "roles";
    static final String CLAIM_KEY_IS_ENABLED = "isEnabled";

    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    ObjectMapper objectMapper;

    public String getUsernameFromToken(String token)
    {
        return getClaimsFromToken(token).getSubject();
    }

    public JwtUser getUserDetails(String token)
    {
        if(token == null)
            return null;
        
        final Claims claims = getClaimsFromToken(token);
        List<SimpleGrantedAuthority> authorities = null;

        if (claims.get(CLAIM_KEY_AUTHORITIES) != null)
            authorities = ((List<String>) claims.get(CLAIM_KEY_AUTHORITIES)).stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        User u = new User();
        u.setEmail(claims.getSubject());
        return new JwtUser(u, (boolean)claims.get(CLAIM_KEY_IS_ENABLED), null, authorities);
    }

    public Date getCreatedDateFromToken(String token)
    {
        final Claims claims = getClaimsFromToken(token);
        return new Date((Long)claims.get(CLAIM_KEY_CREATED));
    }

    public Date getExpirationDateFromToken(String token)
    {
        final Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public String getAudienceFromToken(String token)
    {
        final Claims claims = getClaimsFromToken(token);
        return (String)claims.get(CLAIM_KEY_AUDIENCE);
    }

    private Claims getClaimsFromToken(String token)
    {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Date generateExpirationDate()
    {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token)
    {
        return getExpirationDateFromToken(token).before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token)
    {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }

    public String generateToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        
        List<String> auth = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        claims.put(CLAIM_KEY_AUTHORITIES, auth);
        claims.put(CLAIM_KEY_IS_ENABLED,userDetails.isEnabled());

        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims)
    {
        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(generateExpirationDate())
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    public Boolean canTokenBeRefreshed(String token)
    {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token)
    {
        final Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    public Boolean validateToken(String token, UserDetails userDetails)
    {
        JwtUser user = (JwtUser) userDetails;
        final String username = getUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }
}