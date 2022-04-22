package com.cerner.patienttransfer;

import com.cerner.patienttransfer.models.AppUser;
import com.cerner.patienttransfer.services.UserDetailsServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PatienttransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatienttransferApplication.class, args);
	}

	/**
	 * Performs encryption on password
	 * @return A passwordEncoder object with encryption functionality
	 */
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}



// addd more and more lines
// addd more and more lines
// addd more and more lines
// addd more and more lines
// addd more and more lines
// addd more and more lines
// addd more and more lines
// addd more and more lines
	/**
	 * Performs addition of dummy user data upon running the application
	 * @param appUserService Represents the object of UserDetailsServiceImpl. Used to save the user
	 * @return
	 */
	@Bean
	CommandLineRunner run(UserDetailsServiceImpl appUserService){
		return args -> {
			appUserService.saveAppUser(new AppUser(null, "John Doe","john", "john@example.com", "1234"));
			appUserService.saveAppUser(new AppUser(null, "Jane Doo","jane", "jane@example.com", "1234"));
			appUserService.saveAppUser(new AppUser(null, "Jamie Foxx","jamie", "jamie@example.com", "1234"));
			appUserService.saveAppUser(new AppUser(null, "Jack Paul","jack", "jack@example.com", "1234"));
		};
	}
}
