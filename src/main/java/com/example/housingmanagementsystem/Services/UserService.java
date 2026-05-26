package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.*;
import com.example.housingmanagementsystem.Exceptions.AccessDeniedException;
import com.example.housingmanagementsystem.Exceptions.DuplicateException;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.UserMapper;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.Property;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.OccupancyRepository;
import com.example.housingmanagementsystem.Repositories.UserRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import com.example.housingmanagementsystem.UtilityClasses.PasswordGenerator;
import com.example.housingmanagementsystem.UtilityClasses.Role;
import com.example.housingmanagementsystem.UtilityClasses.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final OccupancyRepository occupancyRepository;
    private final PropertyService propertyService;


    // @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    private User saveUser(User user){
        return userRepository.save(user);
    }
//    private User saveUser(User user){
//        String passwordGenerated=PasswordGenerator.generatePassword();
//        String hashedPassword=passwordEncoder.encode(passwordGenerated);
//        user.setStatus(UserStatus.ACTIVE);
//        user.setPasswordHash(hashedPassword);
//        String to= user.getEmailAddress();
//        String subject="Login credentials";
//        String body="Hello"+" "+user.getFullName()+"\n" +
//                "Please use these credentials when logging in:"+"\n"+
//                "Email address:"+" "+to+"\n"+
//                "Password:"+" "+passwordGenerated;
//        //emailService.sendSimpleEmail(to,subject,body);
//        return userRepository.save(user);
//    }

    private String generateAndSetPassword(User user){
        String rawPassword = PasswordGenerator.generatePassword();
        String hashed = passwordEncoder.encode(rawPassword);

        user.setPasswordHash(hashed);
        return rawPassword; // return raw so you can email it
    }



    //@PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO registerUser(UserRegistrationDTO userDTO){

        User user = userMapper.toEntity(userDTO);
        user.setStatus(UserStatus.ACTIVE);

        String rawPassword = generateAndSetPassword(user);

        User saved = saveUser(user);

        emailService.sendSimpleEmail(
                saved.getEmailAddress(),
                "Login credentials",
                "Your password is: " + rawPassword
        );

        return userMapper.toDTO(saved);
    }

//    public UserResponseDTO registerUser(UserRegistrationDTO userDTO){
//        User user=userMapper.toEntity(userDTO);
//        User savedUser=saveUser(user);
//        return userMapper.toDTO(savedUser);
//    }

    private User getOrCreateTenant(TenantRegistrationDTO dto) {

        return userRepository.findByEmailAddress(dto.getEmailAddress())
                .map(user -> {
                    if (user.getStatus() == UserStatus.INACTIVE) {
                        user.setStatus(UserStatus.ACTIVE);
                        return saveUser(user);
                    }
                    return user;
                })
                .orElseGet(() -> {
                    User newUser = userMapper.toEntity(dto);
                    newUser.setRole(Role.TENANT);
                    newUser.setStatus(UserStatus.ACTIVE);

                    generateAndSetPassword(newUser);

                    return saveUser(newUser);
                });
    }

//    private User getOrCreateTenant(TenantRegistrationDTO dto) {
//        return userRepository.findByEmailAddress(dto.getEmailAddress())
//                .orElseGet(() -> {
//                    User newUser = userMapper.toEntity(dto);
//                    newUser.setRole(Role.TENANT);
//                    return saveUser(newUser);
//                });
//    }

    @Transactional
    public UserResponseDTO registerTenant(TenantRegistrationDTO dto) {

        User tenant = getOrCreateTenant(dto);

        Property property = propertyService.findById(dto.getPropertyId());

        if (occupancyRepository.existsByPropertyAndEndDateIsNull(property)) {
            throw new DuplicateException("Property is already occupied");
        }

        Occupancy occupancy = new Occupancy();
        occupancy.setUser(tenant);
        occupancy.setProperty(property);

        occupancyRepository.save(occupancy);

        return userMapper.toDTO(tenant);
    }

//    @Transactional
//    public UserResponseDTO registerTenant(TenantRegistrationDTO dto) {
//
//        User tenant = getOrCreateTenant(dto);
//
//        Property property = propertyService.findById(dto.getPropertyId());
//
//        if (occupancyRepository.existsByPropertyAndEndDateIsNull(property)) {
//            throw new DuplicateException("Property is already occupied");
//        }
//
//        Occupancy occupancy = new Occupancy();
//        occupancy.setUser(tenant);
//        occupancy.setProperty(property);
//
//        occupancyRepository.save(occupancy);
//
//        return userMapper.toDTO(tenant);
//    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user=userRepository.findByEmailAddress(username)
                .orElseThrow(()->new UsernameNotFoundException("User with email"+" "+username+"not found"));

        //Convert the entity into spring security
        return new CustomUserDetails(user);
    }

    //On user logout

    //@PreAuthorize("hasAnyRole('ADMIN','LANDLORD','USER')")
    @PreAuthorize("#id==authentication.principal.id or hasRole('ADMIN')")
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

   // @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    public boolean deleteUser(Long id){
        User toBeDeleted=userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));

            String currentUserRole= SecurityContextHolder.getContext().getAuthentication()
                    .getAuthorities().iterator().next().getAuthority();
            if(currentUserRole.equals("ROLE_LANDLORD") && !toBeDeleted.getRole().equals("TENANT")){
                throw new AccessDeniedException("Landlord can only delete tenant");

            }

            //Enabled soft delete
        toBeDeleted.setStatus(UserStatus.INACTIVE);
            userRepository.save(toBeDeleted);
        return true;
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User not found"));
    }

    public User findUSerByEmail(String email){
        User user=userRepository.findByEmailAddress(email)
                .orElseThrow(()->new RuntimeException("User not found"));
        return user;
    }
}
