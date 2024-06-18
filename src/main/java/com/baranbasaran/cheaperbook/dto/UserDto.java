package com.baranbasaran.cheaperbook.dto;

import com.baranbasaran.cheaperbook.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String phoneNumber;
    private LocalDate birthDate;
    private AddressDto address;

    @JsonIgnore
    private String password;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .address(AddressDto.builder()
                        .street(user.getAddress().getStreet())
                        .city(user.getAddress().getCity())
                        .country(user.getAddress().getCountry())
                        .postalCode(user.getAddress().getPostalCode())
                        .build())
                .build();
    }

}
