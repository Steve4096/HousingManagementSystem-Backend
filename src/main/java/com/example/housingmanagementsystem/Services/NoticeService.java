package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.NoticeFillingDTO;
import com.example.housingmanagementsystem.DTOs.NoticeResponseDTO;
import com.example.housingmanagementsystem.Exceptions.AccessDeniedException;
import com.example.housingmanagementsystem.Exceptions.DuplicateException;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.NoticeMapper;
import com.example.housingmanagementsystem.Models.Notice;
import com.example.housingmanagementsystem.Models.Occupancy;
import com.example.housingmanagementsystem.Repositories.NoticeRepository;
import com.example.housingmanagementsystem.Security.CustomUserDetails;
import com.example.housingmanagementsystem.UtilityClasses.NoticeStatus;
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
    private final OccupancyService occupancyService;

    public NoticeResponseDTO fileNotice(NoticeFillingDTO noticeFillingDTO){
        //Find logged-in user
        com.example.housingmanagementsystem.Security.CustomUserDetails userDetails =(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //Find logged in user entity
        com.example.housingmanagementsystem.Models.User user =userService.findUSerByEmail(userDetails.getUsername());

        //Verify that the occupancy exists
        Occupancy occupancy=occupancyService.findOccupancy(noticeFillingDTO.getOccupancyId())
                .orElseThrow(()->new NotFoundException("Occupancy not found"));

        //Verify that no notice has been filed before
        if(noticeRepository.existsByOccupancyId(noticeFillingDTO.getOccupancyId())){
            throw new DuplicateException("Only one notice can be filed per occupancy");
        }

        //Verify that the occupancy belongs to the user
        if(!occupancy.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("You can only file a notice for properties belonging to you");
        }

        //Convert to entity
        Notice notice=noticeMapper.toEntity(noticeFillingDTO);
        notice.setStatus(NoticeStatus.UNREAD);
        notice.setOccupancy(occupancy);

        Notice savedNotice=noticeRepository.save(notice);

        return noticeMapper.toDTO(savedNotice);
    }

    public List<NoticeResponseDTO> fetchAllNoticesFiled(){
        return noticeRepository.findAll()
                .stream()
                .map(noticeMapper::toDTO)
                .toList();
    }

    public List<NoticeResponseDTO> fetchUnreadNotices(){
        return noticeRepository.findByStatus(NoticeStatus.UNREAD)
                .stream()
                .map(noticeMapper::toDTO)
                .toList();
    }

    public long countUnreadNotices(){
        return noticeRepository.countByStatus(NoticeStatus.UNREAD);
    }

    public void markNoticeAsRead(Long id){
        Notice notice=noticeRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Notice not found"));

        notice.setStatus(NoticeStatus.READ);
        noticeRepository.save(notice);
    }
}
