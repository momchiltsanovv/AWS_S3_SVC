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
    public ResponseEntity<S3Response> sendFile(@RequestParam("userId") UUID userId,
                                               @RequestPart(
                                                       value = "file") MultipartFile file) throws IOException {

        String URL = s3Service.upsertProfilePic(userId, file);

        return getResponseEntity(URL);
    }

    @PostMapping("/upload/item")
    public ResponseEntity<S3Response> sendItemFile(@RequestParam("item_id") UUID itemId,
                                                   @RequestPart(
                                                           value = "file") MultipartFile file) throws IOException {

        String URL = s3Service.upsertItemPic(itemId, file);

        return getResponseEntity(URL);
    }

    private static ResponseEntity<S3Response> getResponseEntity(String URL) {

        return ResponseEntity.ok().body(S3Response.builder()
                                                  .URL(URL)
                                                  .build());
    }
}
