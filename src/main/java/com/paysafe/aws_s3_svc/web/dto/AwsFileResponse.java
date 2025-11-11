package com.paysafe.aws_s3_svc.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AwsFileResponse {

    private String URL;

}
