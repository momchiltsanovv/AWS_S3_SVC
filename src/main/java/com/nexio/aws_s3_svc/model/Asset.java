package com.nexio.aws_s3_svc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_on",
            nullable = false,
            updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "aws_s3_path",
            nullable = false,
            length = 1000)
    private String awsS3Path;
}
