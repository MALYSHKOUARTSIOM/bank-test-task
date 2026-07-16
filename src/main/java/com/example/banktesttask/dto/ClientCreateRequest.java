package com.example.banktesttask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCreateRequest {

    @NotBlank(message = "ФИО не должно быть пустым")
    private String fullName;

    @NotBlank(message = "Номер телефона не должен быть пустым")
    private String phoneNumber;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;
}
