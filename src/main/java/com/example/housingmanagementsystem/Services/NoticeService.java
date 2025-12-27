package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.NoticeFillingDTO;
import com.example.housingmanagementsystem.DTOs.NoticeResponseDTO;
import com.example.housingmanagementsystem.DTOs.OccupancyResponseDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.NoticeMapper;
import com.example.housingmanagementsystem.Mappers.OccupancyMapper;
import com.example.housingmanagementsystem.Mappers.PropertyMapper;
import com.example.housingmanagementsystem.Models.Notice;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Repositories.NoticeRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final UserService userService;
    private final PropertyService propertyService;
    private final PropertyMapper propertyMapper;
    private final OccupancyService occupancyService;
    private final OccupancyMapper occupancyMapper;

    public Object fileNotice(NoticeFillingDTO noticeFillingDTO){
        //Find logged-in user
        com.example.housingmanagementsystem.Security.CustomUserDetails userDetails =(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //Find logged in user entity
        com.example.housingmanagementsystem.Models.User user =userService.findUSerByEmail(userDetails.getUsername());

        List<Occupancy> activeOccupancies=user.getOccupancies().stream()
                .filter(o->o.getEndDate()==null)
                //.map(occupancyMapper::toDTO)
                .toList();

        Long occupancyId;
        if(activeOccupancies.isEmpty()){
            throw new NotFoundException("No Active occupancies found for the user");
        } else if (activeOccupancies.size()>1) {
            List<OccupancyResponseDTO> selectionList=activeOccupancies.stream()
                    .map(occupancyMapper::toDTO)
                    .toList();
            return selectionList;
        }else {
            noticeFillingDTO.setOccupancyId(activeOccupancies.get(0).getId());
        }

        //Convert to entity
        Notice notice=noticeMapper.toEntity(noticeFillingDTO);

        Notice savedNotice=noticeRepository.save(notice);

        return noticeMapper.toDTO(savedNotice);
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
