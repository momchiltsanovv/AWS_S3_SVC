package com.paysafe.aws_s3_svc.web;

import com.paysafe.aws_s3_svc.service.S3Service;
import com.paysafe.aws_s3_svc.web.dto.S3Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/aws")
public class AwsController {


    private final S3Service s3Service;

    public AwsController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<S3Response> sendFile(@RequestParam("user_id") UUID id,
                                               @RequestPart(value = "file") MultipartFile file) throws IOException {

        System.out.println();
        String URL = s3Service.upsertProfilePic(id, file);

        S3Response response = S3Response.builder()
                                                  .URL(URL)
                                                  .build();

        return ResponseEntity.ok()
                             .body(response);
    }


}
