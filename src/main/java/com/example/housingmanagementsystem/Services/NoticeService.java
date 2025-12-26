package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.NoticeFillingDTO;
import com.example.housingmanagementsystem.DTOs.NoticeResponseDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.NoticeMapper;
import com.example.housingmanagementsystem.Mappers.PropertyMapper;
import com.example.housingmanagementsystem.Models.Notice;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Models.Property;
import com.example.housingmanagementsystem.Models.User;
import com.example.housingmanagementsystem.Repositories.NoticeRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final UserService userService;
    private final PropertyService propertyService;
    private final PropertyMapper propertyMapper;

    public NoticeResponseDTO fileNotice(NoticeFillingDTO noticeFillingDTO){
        //Find logged in user
        com.example.housingmanagementsystem.Security.CustomUserDetails userDetails =(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //Find logged in user entity
        com.example.housingmanagementsystem.Models.User user =userService.findUSerByEmail(userDetails.getUsername());

        //Find the properties the user has
        List<Occupancy> occupancies=user.getOccupancies();

        List<Property> occupiedProperties=occupancies.stream()
                .filter(o->o.getEndDate()==null)
                .map(Occupancy::getProperty)
                .toList();

        Property selectedProperty;
        if(occupiedProperties.size()==1){
            selectedProperty=occupiedProperties.get(0);
        }else {
            //Lists all properties adn lets the user select from the frontend
            return occupiedProperties.stream()
                    .map(propertyMapper::toDTO);
            selectedProperty=null;
        }

        //Convert to entity
        Notice notice=noticeMapper.toEntity(noticeFillingDTO);

        LocalDateTime date=notice.getDateIntendToLeave();
        Notice no=new Notice(date,user,selectedProperty);
    }

    public List<NoticeResponseDTO> fetchAllNoticesFiled(){
        return noticeRepository.findAll()
                .stream()
                .map(noticeMapper::toDTO)
                .toList();
    }

    public NoticeResponseDTO updateExistingNotice(Long id,NoticeFillingDTO noticeFillingDTO){
        Notice notice=noticeRepository.findById(id)
                        .orElseThrow(()-> new NotFoundException("Notice not found"));

        //Using Mapstruct to track changes
        noticeMapper.updateExistingNotice(noticeFillingDTO,notice);

        //Saving the changes made
        Notice savedNotice=noticeRepository.save(notice);

        //Return the saved notice in DTO form
        return noticeMapper.toDTO(savedNotice);
    }
}
