package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.UserRepository;
import com.example.housingmanagementsystem.UtilityClasses.PasswordGenerator;
import com.example.housingmanagementsystem.UtilityClasses.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User saveUser(User user){
        String passwordGenerated=PasswordGenerator.generatePassword();
        String hashedPassword=passwordEncoder.encode(passwordGenerated);
        user.setPasswordHash(hashedPassword);
        return userRepository.save(user);
    }

    public User mapToUser(UserRegistrationDTO registrationDTO){
        User newUser=new User();
        newUser.setIdNumber(registrationDTO.getIdNumber());
        newUser.setFullName(registrationDTO.getFullName());
        newUser.setUserName(registrationDTO.getUserName());
        newUser.setEmailAddress(registrationDTO.getEmailAddress());
        newUser.setPhoneNumber(registrationDTO.getPhoneNumber());
        return newUser;
    }

    public User registerUser(UserRegistrationDTO userDTO){
        User newUser=new User();
        newUser=mapToUser(userDTO);
        newUser.setRole(userDTO.getRole());
        return saveUser(newUser);
    }

    public User registerTenant(UserRegistrationDTO tenantDTO){
        User newTenant=new User();
        newTenant=mapToUser(tenantDTO);
        newTenant.setRole(Role.TENANT);
        return saveUser(newTenant);
    }

    public List<User> fetchAllUsers(){
       return userRepository.findAll();
    }

    public boolean deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
