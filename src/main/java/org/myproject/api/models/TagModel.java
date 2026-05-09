package org.myproject.api.models;

import jakarta.validation.constraints.*;

public record TagModel(
        @Positive(message = "ID тега должен быть положительным")
        Long id,
        String name
) {}
