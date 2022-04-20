package com.cerner.patienttransfer.repository;

import com.cerner.patienttransfer.models.AppUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AppUserRepoTest {

    @Autowired
    private AppUserRepo underTest;

    @Test
    void findByUsernameTest() {
        AppUser appUser = new AppUser(null, "John Doe","john", "john@example.com", "1234");
        underTest.save(appUser);

        AppUser user = underTest.findByUsername("john");
        Assertions.assertEquals(appUser, user);
    }
}
