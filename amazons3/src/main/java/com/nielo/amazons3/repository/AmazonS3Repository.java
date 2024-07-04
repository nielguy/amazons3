package com.nielo.amazons3.repository;

import com.nielo.amazons3.entity.AmazonS3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmazonS3Repository extends JpaRepository<AmazonS3, Long> {
}
