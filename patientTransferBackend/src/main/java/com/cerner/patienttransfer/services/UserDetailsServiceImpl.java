package com.cerner.patienttransfer.services;

import com.cerner.patienttransfer.models.AppUser;
import com.cerner.patienttransfer.repository.AppUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


/**
 * Implementation class for UserDetailsService
 */
@Service
@Transactional
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepo appUserRepo;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder){
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Finds user using the username
     * @param username
     * @return user with details if user exists else throws error
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByUsername(username);

        if (appUser == null){
            throw new UsernameNotFoundException("User not found in database!");
        } else{
            log.info("User found in database: {}", username);
        }

        return new User(appUser.getUsername(), appUser.getPassword(), new ArrayList<>());
    }

    /**
     * Saves a user
     * @param appUser Represents the user and its details
     * @return The details of user that is saved
     */
    public AppUser saveAppUser(AppUser appUser){
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepo.save(appUser);
    }

    /**
     * Return a list of existing users
     * @return List - list of all users
     */
    public List<AppUser> getAppUsers(){
        return appUserRepo.findAll();
    }
}
