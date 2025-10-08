package com.example.housingmanagementsystem.Controllers;

import com.example.housingmanagementsystem.DTOs.NoticeFillingDTO;
import com.example.housingmanagementsystem.DTOs.NoticeResponseDTO;
import com.example.housingmanagementsystem.Models.Notice;
import com.example.housingmanagementsystem.Services.NoticeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /*@PostMapping("/create")
    public ResponseEntity<NoticeResponseDTO> createNotice(@Valid @RequestBody NoticeFillingDTO noticeFillingDTO) {
        NoticeResponseDTO noticeResponseDTO = noticeService.saveNotice(noticeFillingDTO);

        //Returns location of the notice saved
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/notices/{id}").buildAndExpand(noticeResponseDTO.getUser()).toUri();

        return ResponseEntity.created(location).body(noticeResponseDTO);
    }*/

    @GetMapping
    public ResponseEntity<List<NoticeResponseDTO>> fetchAllNotices(){
        return ResponseEntity.ok().body(noticeService.fetchAllNoticesFiled());
    }

    @PatchMapping("/update")
    public ResponseEntity<NoticeResponseDTO> updateNotice(@PathVariable Long id,@Valid @RequestBody NoticeFillingDTO noticeFillingDTO){
        return ResponseEntity.ok().body(noticeService.updateExistingNotice(id,noticeFillingDTO));
    }
}
