package com.nexio.aws_s3_svc.service;

import com.nexio.aws_s3_svc.dto.AssetResponse;
import com.nexio.aws_s3_svc.model.Asset;
import com.nexio.aws_s3_svc.repository.AssetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Page<AssetResponse> getAssets(Pageable pageable) {
        return assetRepository.findAll(pageable)
                              .map(this::mapToResponse);
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

