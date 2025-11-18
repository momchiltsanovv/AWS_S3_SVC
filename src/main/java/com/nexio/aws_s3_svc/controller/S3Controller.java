package com.nexio.aws_s3_svc.controller;

import com.nexio.aws_s3_svc.dto.AssetResponse;
import com.nexio.aws_s3_svc.dto.S3Response;
import com.nexio.aws_s3_svc.service.AssetService;
import com.nexio.aws_s3_svc.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/aws")
public class S3Controller {


    private final S3Service s3Service;
    private final AssetService assetService;

    public S3Controller(S3Service s3Service, AssetService assetService) {
        this.s3Service = s3Service;
        this.assetService = assetService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<S3Response> sendFile(@RequestParam("user_id") UUID userId,
                                               @RequestPart(value = "file") MultipartFile file) throws IOException {

        String URL = s3Service.upsertProfilePic(userId, file);

        return getResponseEntity(URL);
    }

    @PostMapping("/upload/item")
    public ResponseEntity<S3Response> sendItemFile(@RequestParam("item_id") UUID itemId,
                                                   @RequestParam("user_id") UUID userId,
                                                   @RequestPart(value = "file") MultipartFile file) throws IOException {

        String URL = s3Service.upsertItemPic(itemId, userId, file);

        return getResponseEntity(URL);
    }


    //TODO TEST THIS ENDPOINT for Admin-style to list all uploaded assets.
    @GetMapping("/assets")
    public ResponseEntity<List<AssetResponse>> getAssets() {

        List<AssetResponse> assets = assetService.getAssets();

        return ResponseEntity.ok(assets);
    }

    private static ResponseEntity<S3Response> getResponseEntity(String URL) {

        return ResponseEntity.ok().body(S3Response.builder()
                                                  .URL(URL)
                                                  .build());
    }
}
