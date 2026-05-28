package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.CountResponseDTO;
import com.example.housingmanagementsystem.DTOs.NoticeFillingDTO;
import com.example.housingmanagementsystem.DTOs.NoticeResponseDTO;
import com.example.housingmanagementsystem.Services.NoticeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/create")
    public ResponseEntity<NoticeResponseDTO> createNotice(@Valid @RequestBody NoticeFillingDTO noticeFillingDTO) {
        NoticeResponseDTO noticeResponseDTO = noticeService.fileNotice(noticeFillingDTO);

        //Returns location of the notice saved
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/notices/{id}").buildAndExpand(noticeResponseDTO.getId()).toUri();

        return ResponseEntity.created(location).body(noticeResponseDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping
    public ResponseEntity<List<NoticeResponseDTO>> fetchAllNotices(){
        return ResponseEntity.ok().body(noticeService.fetchAllNoticesFiled());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/unread")
    public ResponseEntity<List<NoticeResponseDTO>> fetchUnreadNotices(){
        return ResponseEntity.ok().body(noticeService.fetchUnreadNotices());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @GetMapping("/unread/count")
    public CountResponseDTO countUnreadNotices(){
        return new CountResponseDTO(noticeService.countUnreadNotices());
    }

    @PreAuthorize("hasAnyRole('ADMIN','LANDLORD')")
    @PatchMapping("{id}/read")
    public ResponseEntity markNoticeAsRead(@PathVariable Long id){
        noticeService.markNoticeAsRead(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
