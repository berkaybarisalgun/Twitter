package com.twitter.backend;

import com.twitter.backend.models.ApplicationUser;
import com.twitter.backend.models.Role;
import com.twitter.backend.repositories.RoleRepository;
import com.twitter.backend.repositories.UserRepository;
import com.twitter.backend.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@SpringBootApplication
public class TwitterBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepo, UserService userService){
		return args -> {
			roleRepo.save(new Role(1, "USER"));
			ApplicationUser u = new ApplicationUser();
			u.setFirstName("Baris");
			u.setLastname("aLG");

			userService.registerUser(u);
		};
	}

	}
