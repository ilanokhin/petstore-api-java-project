package org.myproject.api.payloads;

import com.github.javafaker.Faker;
import org.myproject.api.models.CategoryModel;
import org.myproject.api.models.PetModel;
import org.myproject.api.models.TagModel;

import java.util.Arrays;
import java.util.List;

public class PetPayload {
    private static final Faker fake = new Faker();

    public static PetModel getOne(String... excludeKeys) {
        int urlSize = fake.number().numberBetween(1, 5);
        Long id = null;
        CategoryModel category = null;
        String name, status = null;
        String[] photoUrls;
        TagModel[] tags = null;
        List<String> excludeList = Arrays.asList(excludeKeys);

        name = fake.name().firstName();

        photoUrls = new String[urlSize];

        for (int i = 0; i < urlSize; i++) {
            photoUrls[i] = fake.internet().url();
        }

        if (!excludeList.contains("id")) id = (long) fake.number().numberBetween(1, 9999999);

        if (!excludeList.contains("category")) {
            category = new CategoryModel((long) fake.number().numberBetween(1, 9999999), fake.lorem().word());
        }

        if (!excludeList.contains("tags")) {
            int size = fake.number().numberBetween(1, 5);
            tags = new TagModel[size];

            for (int i = 0; i < size; i++) {
                tags[i] = new TagModel((long) fake.number().numberBetween(1, 9999999), fake.lorem().word());
            }
        }

        if (!excludeList.contains("status")) status = fake.options().option("available", "pending", "sold");

        return new PetModel(id, category, name, photoUrls, tags, status);
    }

    public static PetModel[] getArray(int number, String... excludeKeys) {
        PetModel[] result = new PetModel[number];

        for (int i = 0; i < number; i++) {
            result[i] = getOne(excludeKeys);
        }

        return result;
    }
}
