package com.twitter.backend.services;

import com.twitter.backend.exceptions.EmailAlreadyTakenException;
import com.twitter.backend.exceptions.IncorrectVerificationCodeException;
import com.twitter.backend.exceptions.UserdoesNotExistException;
import com.twitter.backend.models.ApplicationUser;
import com.twitter.backend.models.RegistrationObject;
import com.twitter.backend.models.Role;
import com.twitter.backend.repositories.RoleRepository;
import com.twitter.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }


    public ApplicationUser registerUser(RegistrationObject ro) {
        ApplicationUser user=new ApplicationUser();

        user.setFirstName(ro.getFirstName());
        user.setLastName(ro.getLastName());
        user.setEmail(ro.getEmail());
        user.setDateOfBirth(ro.getDob());

        String name=user.getFirstName() + user.getLastName();

        boolean nameTaken=true;

        String tempName="";

        while(nameTaken){
            tempName=generateUsername(name);
            if(userRepository.findByUsername(tempName).isEmpty()){
                nameTaken=false;
            }
        }

        user.setUsername(tempName);

        Set<Role> roles=user.getAuthorities();
        roles.add(roleRepository.findByAuthority("USER").get());
        user.setAuthorities(roles);

        try{
            return userRepository.save(user);
        }
        catch (Exception e){
            throw new EmailAlreadyTakenException();
        }

    }

    public void  generateEmailVerification(String username) {
        ApplicationUser user=userRepository.findByUsername(username).orElseThrow(UserdoesNotExistException::new);

        user.setVerification(generateVerificiationNumber());


        mailService.sendEmail(user.getEmail(),"Your verification code","Here is your verification code: "+user.getVerification());

        userRepository.save(user);

    }

    public ApplicationUser verifyEmail(String username, Long code) {
        ApplicationUser user=userRepository.findByUsername(username).orElseThrow(UserdoesNotExistException::new);
        if(code.equals(user.getVerification())){
            user.setEnabled(true);
            user.setVerification(null);
            return userRepository.save(user);
        }
        else{
            throw new IncorrectVerificationCodeException();
        }
    }

    public ApplicationUser setPassword(String username, String password) {
        ApplicationUser user=userRepository.findByUsername(username).orElseThrow(UserdoesNotExistException::new);
        String encodedPassword=passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public ApplicationUser getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserdoesNotExistException::new);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try{
            return userRepository.save(user);
        } catch(Exception e){
            throw new EmailAlreadyTakenException();
        }
    }


    private String generateUsername(String name) {
        long generatedNumber=(long)Math.floor(Math.random()*1000000000);

        return name+generatedNumber;
    }

    private Long generateVerificiationNumber(){
        long generatedNumber=(long)Math.floor(Math.random()*1000000000);
        return generatedNumber;
    }



}
