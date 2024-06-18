package com.baranbasaran.cheaperbook.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
