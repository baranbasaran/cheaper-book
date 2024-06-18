package com.baranbasaran.cheaperbook.controller.request.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

        private Long id;

        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        private String name;

        @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
        private String surname;

        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        private String username;

        @Size(min = 2, max = 50, message = "Email must be between 2 and 50 characters")
        @Pattern(regexp = "^(.+)@(.+)$", message = "Email must be valid")
        private String email;

        @Size(min = 2, max = 50, message = "Password must be between 2 and 50 characters")
        private String password;

        private String profilePicture;

        private LocalDate birthDate;

        private String phoneNumber;

        @Valid
        private AddressRequest address;

        public boolean isValid() {
            return this.getUsername() != null && this.getPassword() != null && this.getEmail() != null;
        }

        public boolean isCreateValid() {
            return this.isValid() && this.getName() != null && this.getSurname() != null;
        }

        public boolean isUpdateValid() {
            return this.isValid() && this.getId() != null;
        }

        public boolean isDeleteValid() {
            return this.getId() != null;
        }

        public boolean isGetValid() {
            return this.getId() != null;
        }

        public boolean isGetAllValid() {
            return true;
        }
}
