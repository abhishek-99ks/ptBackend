package com.cerner.patienttransfer.services;

import com.cerner.patienttransfer.models.AppUser;
import com.cerner.patienttransfer.repository.AppUserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UserDetailsServiceImplTest {
    @Mock
    private AppUserRepo appUserRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private AppUser appUser1 = new AppUser(null, "John Doe","john", "john@example.com", "1234");

    private List<AppUser> userList = new ArrayList<AppUser>(
        Arrays.asList(appUser1,appUser1,appUser1)
    );

    @Test
    public void saveUserSuccessTest(){
        Mockito.when(appUserRepo.save(Mockito.any(AppUser.class))).thenReturn(appUser1);
        Assertions.assertEquals(appUser1, userDetailsService.saveAppUser(appUser1));
    }

    @Test
    public void getAllUsersTest(){
        Mockito.when(appUserRepo.findAll()).thenReturn(userList);
        Assertions.assertEquals(userList, userDetailsService.getAppUsers());
    }
}
