package org.myproject.api.models;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderModel(
        @Positive(message = "ID заказа должен быть положительным")
        Long id,
        @Positive(message = "ID питомца должен быть положительным")
        Long petId,
        @PositiveOrZero(message = "Количество не должно быть отрицательным")
        Integer quantity,
        String shipDate,
        @Pattern(regexp = "placed|approved|delivered",
                message = "Статус должен быть 'placed', 'approved' или 'delivered'")
        String status,
        Boolean complete
) {}
