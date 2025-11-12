package com.nexio.aws_s3_svc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3Response {

    @JsonProperty("URL")
    private String URL;

}
