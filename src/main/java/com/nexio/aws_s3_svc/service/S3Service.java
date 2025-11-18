package com.nexio.aws_s3_svc.service;

import com.nexio.aws_s3_svc.model.Asset;
import com.nexio.aws_s3_svc.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final AssetRepository assetRepository;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${aws.bucket.folder.profile-pic}")
    private String profilePicFolder;

    @Value("${aws.bucket.folder.item-pic}")
    private String itemFolder;

    public S3Service(S3Client s3Client, AssetRepository assetRepository) {
        this.s3Client = s3Client;
        this.assetRepository = assetRepository;
    }

    @Transactional
    public String upsertProfilePic(UUID userId, MultipartFile file) throws IOException {

        return uploadToS3BucketFolder(userId, file, profilePicFolder, userId);
    }

    @Transactional
    public String upsertItemPic(UUID itemId, UUID userId, MultipartFile file) throws IOException {

        return uploadToS3BucketFolder(itemId, file, itemFolder, userId);
    }

    private String uploadToS3BucketFolder(UUID objectId,
                                          MultipartFile file,
                                          String folderName,
                                          UUID createdBy) throws IOException {

        String ext = file.getOriginalFilename().split("\\.")[1];
        String newFilename = objectId + "." + ext;
        String key = folderName + newFilename;


        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                   .bucket(bucketName)
                                                   .key(key)
                                                   .acl(ObjectCannedACL.PUBLIC_READ)
                                                   .contentType(file.getContentType())
                                                   .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);

        Asset asset = new Asset();
        asset.setCreatedBy(createdBy);
        asset.setAwsS3Path(s3Url);
        assetRepository.save(asset);

        return s3Url;
    }

}
