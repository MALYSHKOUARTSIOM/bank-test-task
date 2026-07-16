package com.example.banktesttask.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Integer status;
}
