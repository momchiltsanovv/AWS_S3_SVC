package com.paysafe.aws_s3_svc.web;

import com.paysafe.aws_s3_svc.service.S3Service;
import com.paysafe.aws_s3_svc.web.dto.AwsFileRequest;
import com.paysafe.aws_s3_svc.web.dto.AwsFileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/aws")
public class AwsController {


    private final S3Service s3Service;

    public AwsController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<AwsFileResponse> sendFile(@RequestParam String id,
                                                    @RequestPart(value = "file") MultipartFile file) throws IOException {

        System.out.println();
//        String URL = s3Service.upsertProfilePic(request.getFile(), request);

        AwsFileResponse response = AwsFileResponse.builder()
//                                                  .URL(URL)
                                                  .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


}
