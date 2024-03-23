package com.twitter.backend.services;

import com.twitter.backend.models.ApplicationUser;
import com.twitter.backend.models.Role;
import com.twitter.backend.repositories.RoleRepository;
import com.twitter.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    public ApplicationUser registerUser(ApplicationUser user) {
        Set<Role> roles=user.getAuthorities();
        roles.add(roleRepository.findByAuthority("USER").get());
        user.setAuthorities(roles);

        return userRepository.save(user);

    }
}
