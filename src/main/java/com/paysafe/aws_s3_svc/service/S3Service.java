package com.paysafe.aws_s3_svc.service;

import com.paysafe.aws_s3_svc.web.dto.AwsFileRequest;
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

    public String upsertProfilePic(MultipartFile file, AwsFileRequest request) throws IOException {

        return uploadToS3BucketFolder(request.getFile(),request, profilePicFolder);
    }

    public String upsertItemPics(MultipartFile file, AwsFileRequest request) throws IOException {

        return uploadToS3BucketFolder(request.getFile(),request, itemFolder);
    }

    private String uploadToS3BucketFolder(MultipartFile file,AwsFileRequest request, String folderName) throws IOException {

        String ext = file.getOriginalFilename().split("\\.")[1];
        String newFilename = request.getUserId() + "." + ext;
        String key = folderName + newFilename;


        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                   .bucket(bucketName)
                                                   .key(key)
                                                   .acl(ObjectCannedACL.PUBLIC_READ)
                                                   .contentType(file.getContentType())
                                                   .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

}
