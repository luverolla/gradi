package io.luverolla.gradi.rest;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * An object that contains a JWT and some other information, such as
 *
 * <ul>
 *     <li>authentication's request header name</li>
 *     <li>token: the encoded JWT token</li>
 *     <li>expiration: the token's time-to-live, expressed in seconds</li>
 * </ul>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse
{
    private String header;
    private String token;
    private int expiration;
}