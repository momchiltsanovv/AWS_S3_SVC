package com.nexio.aws_s3_svc.web.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.annotations.NotNull;

import java.util.UUID;

@Data
@Builder
public class AwsFileRequest {

    @NotNull
    private UUID userId;

    private MultipartFile file;


}
