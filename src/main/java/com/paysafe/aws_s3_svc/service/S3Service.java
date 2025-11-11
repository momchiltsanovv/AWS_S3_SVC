package com.paysafe.aws_s3_svc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${aws.bucket.folder.profile-pic}")
    private String profilePicFolder;

    @Value("${aws.bucket.folder.item-pic}")
    private String itemFolder;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String upsertProfilePic(MultipartFile file) throws IOException {

        return uploadToS3BucketFolder(file, profilePicFolder);
    }

    public String upsertItemPics(MultipartFile file) throws IOException {

        return uploadToS3BucketFolder(file, itemFolder);
    }

    private String uploadToS3BucketFolder(MultipartFile file, String folderName) throws IOException {
        String key = folderName + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                                                   .bucket(bucketName)
                                                   .key(key)
                                                   .acl(ObjectCannedACL.PUBLIC_READ)
                                                   .contentType(file.getContentType())
                                                   .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

}
