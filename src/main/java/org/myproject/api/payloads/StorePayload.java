package org.myproject.api.payloads;

import com.github.javafaker.Faker;
import org.myproject.api.models.OrderModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class StorePayload {
    private static final Faker fake = new Faker();

    public static OrderModel getOne(String... excludeKeys) {
        Long id = null;
        Long petId = null;
        Integer quantity = null;
        String shipDate = null;
        String status = null;
        Boolean complete = null;
        List<String> excludeList = Arrays.asList(excludeKeys);

        if (!excludeList.contains("id")) id = (long) fake.number().numberBetween(1, 9999999);
        if (!excludeList.contains("petId")) petId = (long) fake.number().numberBetween(1, 9999999);
        if (!excludeList.contains("quantity")) quantity = 1;

        if (!excludeList.contains("shipDate")) {
            LocalDateTime now = LocalDateTime.now();
            shipDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        if (!excludeList.contains("status")) status = fake.options().option("placed", "approved", "delivered");
        if (!excludeList.contains("complete")) complete = fake.bool().bool();

        return new OrderModel(id, petId, quantity, shipDate, status, complete);
    }

    public static OrderModel[] getArray(int number, String... excludeKeys) {
        OrderModel[] result = new OrderModel[number];

        for (int i = 0; i < number; i++) {
            result[i] = getOne(excludeKeys);
        }

        return result;
    }
}