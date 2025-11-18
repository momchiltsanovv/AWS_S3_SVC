package com.nexio.aws_s3_svc.service;

import com.nexio.aws_s3_svc.dto.AssetResponse;
import com.nexio.aws_s3_svc.model.Asset;
import com.nexio.aws_s3_svc.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<AssetResponse> getAssets() {
        return assetRepository.findAll()
                              .stream()
                              .map(this::mapToResponse)
                              .collect(Collectors.toList());
    }

    private AssetResponse mapToResponse(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getCreatedBy(),
                asset.getCreatedOn(),
                asset.getAwsS3Path()
        );
    }
}

