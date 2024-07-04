package com.nielo.amazons3.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestDTO {

    private String title;
    private String apiProducts;
    private String lineOfBusiness;
    private List<String> partnerName;
    private String documentType;
}
