package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.TenantRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserRegistrationDTO;
import com.example.housingmanagementsystem.DTOs.UserResponseDTO;
import com.example.housingmanagementsystem.DTOs.UserUpdateDTO;
import com.example.housingmanagementsystem.Mappers.UserMapper;
import com.example.housingmanagementsystem.Models.Property;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.PropertyRepository;
import com.example.housingmanagementsystem.Repositories.UserRepository;
import com.example.housingmanagementsystem.UtilityClasses.PasswordGenerator;
import com.example.housingmanagementsystem.UtilityClasses.Role;
import com.example.housingmanagementsystem.UtilityClasses.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;


   // @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    private User saveUser(User user){
        String passwordGenerated=PasswordGenerator.generatePassword();
        String hashedPassword=passwordEncoder.encode(passwordGenerated);
        user.setStatus(UserStatus.ACTIVE);
        user.setPasswordHash(hashedPassword);
        String to= user.getEmailAddress();
        String subject="Login credentials";
        String body="Hello"+" "+user.getFullName()+"\n" +
                "Please use these credentials when logging in:"+"\n"+
                "Email address:"+" "+to+"\n"+
                "Password:"+" "+passwordGenerated;
        emailService.sendSimpleEmail(to,subject,body);
        return userRepository.save(user);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO registerUser(UserRegistrationDTO userDTO){
        User user=userMapper.toEntity(userDTO);
        User savedUser=saveUser(user);
        return userMapper.toDTO(savedUser);
    }

   // @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    public UserResponseDTO registerTenant(TenantRegistrationDTO tenantDTO){
        User tenant=userMapper.toEntity(tenantDTO);

        //Handle fields not set in the DTO
        tenant.setRole(Role.TENANT);

        User savedTenant=saveUser(tenant);
        return userMapper.toDTO(savedTenant);
    }

    //Load user details by email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user=userRepository.findByEmailAddress(username)
                .orElseThrow(()->new UsernameNotFoundException("User with email"+" "+username+" "+"not found"));

        // Convert your User entity into Spring Security's UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailAddress())
                .password(user.getPasswordHash())
                .authorities(user.getRole().name())
                .build();
    }

    //On user logout

    //@PreAuthorize("hasAnyRole('ADMIN','LANDLORD','USER')")
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

   // @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> fetchAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

//    public List<UserResponseDTO> fetchAllUsers(){
//        return userRepository.findAll()
//                .stream()
//                .map(userMapper::toDTO)
//                .collect(Collectors.groupingBy()
//    }

   // @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    public boolean deleteUser(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User findUSerByEmail(String email){
        User user=userRepository.findByEmailAddress(email)
                .orElseThrow(()->new RuntimeException("User not found"));
        return user;
    }
}
