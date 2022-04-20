package com.cerner.patienttransfer.controller;

import com.cerner.patienttransfer.controllers.AppUserController;
import com.cerner.patienttransfer.jwt.AuthenticationRequest;
import com.cerner.patienttransfer.jwt.AuthenticationResponse;
import com.cerner.patienttransfer.jwt.JwtUtils;
import com.cerner.patienttransfer.models.AppUser;
import com.cerner.patienttransfer.payloads.ApiResponse;
import com.cerner.patienttransfer.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AppUserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @InjectMocks
    private AppUserController appUserController;

    @Autowired
    private MockMvc mockMvc;

    private AppUser appUser;

    private AuthenticationRequest authenticationRequest;

    @Mock
    private AuthenticationManager authenticationManager;

//    private static UserDetails dummy;
    private static UserDetails dummy;
    private static String jwtToken;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(appUserController).build();

        // User is from UserDetails class
        dummy = new User("username", "1234", new ArrayList<>());
        jwtToken = jwtUtils.generateJwtToken(dummy);

    }

    /**
     * Test user login success with a response of JWT and 200 status code
     */
    @Test
    public void loginUserAndGenerateJWTSuccessTest() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "1234");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("anyjwt");

        String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);
        String jsonResponse = objectMapper.writeValueAsString(authenticationResponse);

        RequestBuilder request = MockMvcRequestBuilders.post("/api/users/signin")
            .content(jsonRequest)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        Mockito.when(jwtUtils.generateJwtToken(dummy)).thenReturn("anyjwt");
        Mockito.when(userDetailsServiceImpl.loadUserByUsername("username")).thenReturn(dummy);

        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
        Assertions.assertEquals(jsonResponse, mvcResult.getResponse().getContentAsString());
    }

    /**
     * Test user login failure with a response message and 401 status code
     */
    @Test
    public void loginUserAndGenerateJWTFailureTest() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "");
        ApiResponse apiResponse = new ApiResponse("Wrong Credentials");

        String jsonRequest = objectMapper.writeValueAsString(authenticationRequest);
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        log.info("this is json response: {}", jsonResponse);

        RequestBuilder request = MockMvcRequestBuilders.post("/api/users/signin")
            .content(jsonRequest)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("username", ""))).thenThrow(new BadCredentialsException("Wrong Credentials"));
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        Assertions.assertEquals(401, mvcResult.getResponse().getStatus());
        Assertions.assertEquals(jsonResponse, mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void getAllUsersSuccessTest() throws Exception {
        AppUser appUser1 = new AppUser(null, "John Doe","john", "john@example.com", "1234");
        List<AppUser> userList = new ArrayList<AppUser>(
            Arrays.asList(appUser1,appUser1,appUser1)
        );
        String jsonResponse = objectMapper.writeValueAsString(userList);

        RequestBuilder request = MockMvcRequestBuilders.post("/api/users/")
            .header("Authorization", "Bearer " + jwtToken)
            .accept(MediaType.APPLICATION_JSON);

        Mockito.when(userDetailsServiceImpl.getAppUsers()).thenReturn(userList);
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        Assertions.assertEquals(jsonResponse, mvcResult.getResponse().getContentAsString());
    }
}