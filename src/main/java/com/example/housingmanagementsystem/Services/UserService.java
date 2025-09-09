package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.DTOs.UserUpdateDTO;
import com.example.housingmanagementsystem.Mappers.UserMapper;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.UserRepository;
import com.example.housingmanagementsystem.UtilityClasses.PasswordGenerator;
import com.example.housingmanagementsystem.UtilityClasses.Role;
import com.example.housingmanagementsystem.UtilityClasses.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,UserMapper userMapper){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.userMapper=userMapper;
    }

    private User saveUser(User user){
        String passwordGenerated=PasswordGenerator.generatePassword();
        String hashedPassword=passwordEncoder.encode(passwordGenerated);
        user.setPasswordHash(hashedPassword);
        return userRepository.save(user);
    }

    public UserResponseDTO registerUser(UserRegistrationDTO userDTO){
        User user=userMapper.toEntity(userDTO);
        User savedUser=saveUser(user);
        return userMapper.toDTO(savedUser);
    }

    public UserResponseDTO registerTenant(UserRegistrationDTO tenantDTO){
        User tenant=userMapper.toEntity(tenantDTO);

        //Handle fields not set in the DTO
        tenant.setRole(Role.TENANT);
        tenant.setStatus(UserStatus.ACTIVE);

        User savedTenant=saveUser(tenant);
        return userMapper.toDTO(savedTenant);
    }

    @Transactional
    public UserResponseDTO updateUserDetails(Long id,UserUpdateDTO updateDTO){
        User user=userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));

        //Mapstruct tracks what fields have changed
        userMapper.updateUserfromDTO(updateDTO,user);

        //Save makes/triggers envers to log the changed fields as well as values
        User savedUser=userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    public List<UserResponseDTO> fetchAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public boolean deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
