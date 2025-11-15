package com.nexio.aws_s3_svc.controller;

import com.nexio.aws_s3_svc.service.S3Service;
import com.nexio.aws_s3_svc.dto.S3Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/aws")
public class S3Controller {


    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<S3Response> sendFile(@RequestParam("user_id") UUID id,
                                               @RequestPart(value = "file") MultipartFile file) throws IOException {

        String URL = s3Service.upsertProfilePic(id, file);

        S3Response response = S3Response.builder()
                                        .URL(URL)
                                        .build();

        return ResponseEntity.ok()
                             .body(response);
    }


}
