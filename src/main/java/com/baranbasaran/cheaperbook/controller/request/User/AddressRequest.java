package com.baranbasaran.cheaperbook.controller.request.User;

import com.baranbasaran.cheaperbook.model.Address;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters")
    private String street;

    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    private String country;

    @Size(min = 2, max = 20, message = "Postal code must be between 2 and 20 characters")
    private String postalCode;

    public Address to() {
        return Address.builder()
                .street(this.street)
                .city(this.city)
                .country(this.country)
                .postalCode(this.postalCode)
                .build();
    }
}