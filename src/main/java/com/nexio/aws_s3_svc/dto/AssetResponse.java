package com.nexio.aws_s3_svc.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AssetResponse(
        UUID id,
        UUID createdBy,
        LocalDateTime createdOn,
        String awsS3Path
) {
}

