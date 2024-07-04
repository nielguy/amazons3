package com.nielo.amazons3.service;

import com.nielo.amazons3.dto.RequestDTO;
import com.nielo.amazons3.entity.AmazonS3;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AmazonS3Service {

    AmazonS3 uploadFile(MultipartFile file, RequestDTO requestDTO) throws IOException;

    byte[] downloadFile(String fileName);

    String deleteFile(String fileName);
}
