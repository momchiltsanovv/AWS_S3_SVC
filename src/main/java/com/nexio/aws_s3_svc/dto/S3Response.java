package com.nexio.aws_s3_svc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record S3Response(
        @JsonProperty("URL") String URL
){
}
