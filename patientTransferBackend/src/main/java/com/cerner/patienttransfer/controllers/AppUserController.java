package com.cerner.patienttransfer.controllers;

import com.cerner.patienttransfer.jwt.AuthenticationRequest;
import com.cerner.patienttransfer.jwt.AuthenticationResponse;
import com.cerner.patienttransfer.jwt.JwtRequestFilter;
import com.cerner.patienttransfer.jwt.JwtUtils;
import com.cerner.patienttransfer.models.AppUser;
import com.cerner.patienttransfer.payloads.ApiResponse;
import com.cerner.patienttransfer.services.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that helps with user data
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class AppUserController {

        private AuthenticationManager authenticationManager;
        private UserDetailsServiceImpl userDetailsServiceImpl;
        private JwtUtils jwtUtils;
        private JwtRequestFilter jwtRequestFilter;

        /**
         * This constructor initializes dependencies needed
         * @param authenticationManager Authentication manager object that authenticates the user using login credentials
         * @param userDetailsServiceImpl Implementation of UserDetailsService
         * @param jwtUtils Object of JWT Utility class
         * @param jwtRequestFilter Filter that checks every request being made
         */
        public AppUserController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl, JwtUtils jwtUtils, JwtRequestFilter jwtRequestFilter){
            this.authenticationManager = authenticationManager;
            this.userDetailsServiceImpl = userDetailsServiceImpl;
            this.jwtUtils = jwtUtils;
            this.jwtRequestFilter = jwtRequestFilter;
        }

        /**
         * Function mapped to /api/users/signin for user login and JWT token creation
         * @param authenticationRequest Represents the JSON data (username, password) sent from the client via POST request
         * @return A JWT token else unauthorized(401) error
         * @throws Exception
         */
        @RequestMapping(value="/signin", method = RequestMethod.POST)
        public ResponseEntity<?> loginUserAndGenerateJWT(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
            Authentication authentication;
            try {
                authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
                );
            } catch (BadCredentialsException e) {
                return new ResponseEntity<ApiResponse>(new ApiResponse("Wrong Credentials"), HttpStatus.UNAUTHORIZED);
            }
            final UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(authenticationRequest.getUsername());
            final String jwt = this.jwtUtils.generateJwtToken(userDetails);
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }

        /**
         * Function mapped to /api/users/ for retrieving the list of users
         * @return List - list of users with their details
         */
        @RequestMapping("/")
        public ResponseEntity<List<AppUser>> getAppUsers(){
            return ResponseEntity.ok().body(userDetailsServiceImpl.getAppUsers());
        }
}
