package com.cerner.patienttransfer.jwt;

/**
 * Class that represents JWT token returned after user login
 */
public class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}