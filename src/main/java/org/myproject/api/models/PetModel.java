package org.myproject.api.models;

import jakarta.validation.constraints.*;

public record PetModel(
        @Positive(message = "ID питомца должен быть положительным")
        Long id,
        CategoryModel category,
        @NotBlank(message = "Имя питомца не должно отсутствовать или быть пустым")
        String name,
        @NotNull(message = "Список ссылок на фотографии отсутствует")
        String[] photoUrls,
        TagModel[] tags,
        @Pattern(regexp = "available|pending|sold", message = "Статус должен быть 'available', 'pending' или 'sold'")
        String status
) {}
