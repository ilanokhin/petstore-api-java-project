package org.myproject.api.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

public record UserModel(
        @Positive(message = "ID пользователя должен быть положительным")
        Long id,
        String username,
        String firstName,
        String lastName,
        @Email(message = "Email должен иметь корректный формат")
        String email,
        String password,
        String phone,
        Integer userStatus
) {}
