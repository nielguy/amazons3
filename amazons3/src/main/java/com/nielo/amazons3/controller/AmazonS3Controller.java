package com.nielo.amazons3.controller;

import com.amazonaws.services.xray.model.Http;
import com.nielo.amazons3.dto.RequestDTO;
import com.nielo.amazons3.entity.AmazonS3;
import com.nielo.amazons3.service.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
public class AmazonS3Controller {

    @Autowired
    private AmazonS3Service amazonS3Service;

    @PostMapping("/upload")
    @Transactional
    public ResponseEntity<AmazonS3> uploadFile(@RequestPart("file") MultipartFile file, @RequestPart RequestDTO requestDTO) throws IOException {
        AmazonS3 amazonS3Response = amazonS3Service.uploadFile(file, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(amazonS3Response);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = amazonS3Service.downloadFile(fileName);
        ByteArrayResource byteArrayResource = new ByteArrayResource(data);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; fileName=\""+fileName+"\"")
                .body(byteArrayResource);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(String fileName) {
        return ResponseEntity.status(HttpStatus.OK).body(amazonS3Service.deleteFile(fileName));
    }
}
