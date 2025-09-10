package com.example.housingmanagementsystem.Services;

import com.example.housingmanagementsystem.DTOs.NoticeFillingDTO;
import com.example.housingmanagementsystem.DTOs.NoticeResponseDTO;
import com.example.housingmanagementsystem.Exceptions.NotFoundException;
import com.example.housingmanagementsystem.Mappers.NoticeMapper;
import com.example.housingmanagementsystem.Models.Notice;
import com.example.housingmanagementsystem.Repositories.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeRepository noticeRepository,NoticeMapper noticeMapper){
        this.noticeRepository=noticeRepository;
        this.noticeMapper=noticeMapper;
    }

    public NoticeResponseDTO saveNotice(NoticeFillingDTO noticeFillingDTO){
        //Convert the notice DTO to an entity first
        Notice notice=noticeMapper.toEntity(noticeFillingDTO);

        //Save the entity
        Notice savedNotice=noticeRepository.save(notice);

        //return the saved DTO
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
