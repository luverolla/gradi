package io.luverolla.gradi.rest;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * An object that wraps username and password for authentication
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest
{
    private String username;
    private String password;
}
