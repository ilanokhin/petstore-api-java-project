package org.myproject.api.models;

import jakarta.validation.constraints.*;

public record CategoryModel(
        @Positive(message = "ID категории должен быть положительным")
        Long id,
        String name
) {}
