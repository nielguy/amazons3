package com.nielo.amazons3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.nielo.amazons3.dto.RequestDTO;
import com.nielo.amazons3.repository.AmazonS3Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AmazonS3ServiceImpl implements AmazonS3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AmazonS3Repository amazonS3Repository;

    @Value("${application.bucket.name}")
    private String bucketName;

    public com.nielo.amazons3.entity.AmazonS3 uploadFile(MultipartFile file, RequestDTO requestDTO) throws IOException {
        String filename = System.currentTimeMillis()+file.getOriginalFilename().replace(" ", "_");
        uploadFileToS3Bucket(file, filename);
        com.nielo.amazons3.entity.AmazonS3 reponse = uploadDataToGCDaB(requestDTO, file, filename);
        if (reponse == null) {
            throw new RuntimeException("Could'nt upload data to GCPDB but File uploaded successfully in the S3 bucket");
        }
//        return "File: "+file.getOriginalFilename()+" uploaded successfully!";
        return reponse;
    }

    public void createS3Bucket(String bucketName) {
        if(amazonS3.doesBucketExist(bucketName)) {
            log.info("Bucket name already in use. Try another name.");
            return;
        }
        amazonS3.createBucket(bucketName);
    }

    private com.nielo.amazons3.entity.AmazonS3 uploadDataToGCDaB(RequestDTO requestDTO, MultipartFile file, String fileName) throws IOException {
        com.nielo.amazons3.entity.AmazonS3 entityObject = new com.nielo.amazons3.entity.AmazonS3();
        entityObject.setTitle(requestDTO.getTitle());
        entityObject.setApiProducts(requestDTO.getApiProducts());
//        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        entityObject.setFileBody(file.getBytes());
        entityObject.setLineOfBusiness(requestDTO.getLineOfBusiness());
        String partnerNames = requestDTO.getPartnerName().stream().map(String::valueOf).collect(Collectors.joining(", "));
//        String partnerNames = requestDTO.getPartnerName() != null ? requestDTO.getPartnerName().stream()
//                .map(n -> String.valueOf(n))
//                .collect(Collectors.joining("-", "{", "}")) : "";
        entityObject.setPartnerName(partnerNames);
//        String s3Url = "https://"+bucketName+".s3.amazonaws.com/"+fileName;
//        entityObject.setApiFilePath(s3Url);
        entityObject.setApiFilePath(amazonS3.getUrl(bucketName, fileName).toString());
        entityObject.setDocumentType(requestDTO.getDocumentType());
        String fileExtension = getFileExtension(file.getOriginalFilename());
        entityObject.setContentType(fileExtension);
        com.nielo.amazons3.entity.AmazonS3 entityResponse = amazonS3Repository.save(entityObject);
        return entityResponse;
    }

    private void uploadFileToS3Bucket(MultipartFile file, String filename) {
        log.info("Started uploading file {} to Amazon S3 bucket", file.getOriginalFilename());
        File fileObj = convertMultipartFileToFile(file);
//        String filename = System.currentTimeMillis()+file.getOriginalFilename();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, fileObj);
        amazonS3.putObject(putObjectRequest);
        fileObj.delete();
        log.info("Successfully uploaded file {} to Amazon s3 bucket", file.getOriginalFilename());
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to File", e);
        }
        return convertedFile;
    }

    private String getFileExtension(String fileName) {
        String regex = "([^\\s]+(\\.(?i)(\\w+))$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(3);
        }
        return null;
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
        return fileName+" deleted successfully...";
    }
}
