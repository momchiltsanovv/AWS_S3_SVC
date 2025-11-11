package com.paysafe.aws_s3_svc.controller;

import com.paysafe.aws_s3_svc.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class S3Controller {


    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = s3Service.upsertProfilePic(file);

        return ResponseEntity.ok(fileUrl);
    }

//    @GetMapping("/download/{filename}")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
//        byte[] data = s3Service.downloadFile(filename);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename)
//                .body(data);
//    }


}
