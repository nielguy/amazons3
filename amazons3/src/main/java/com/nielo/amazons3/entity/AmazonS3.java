package com.nielo.amazons3.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GCP_API_DOCS")
@EntityListeners(AuditingEntityListener.class)
public class AmazonS3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String apiProducts;
    @Column(name = "body")
    @Lob
    private byte[] fileBody;
    @Column(name = "line_of_business")
    private String lineOfBusiness;
    @Column(name = "partner_name")
    private String partnerName;
    @Column(name = "API_File_Path")
    private String apiFilePath;
    private String documentType;
    private String contentType;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date publisDate;

//    @CreationTimestamp
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    private LocalDateTime updatedDate;
//
//    @CreationTimestamp
//    private LocalDateTime publishDate;

}
