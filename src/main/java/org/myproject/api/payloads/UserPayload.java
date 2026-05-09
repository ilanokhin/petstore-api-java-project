package org.myproject.api.payloads;

import com.github.javafaker.Faker;
import org.myproject.api.models.UserModel;

import java.util.Arrays;
import java.util.List;

public class UserPayload {
    private static final Faker fake = new Faker();

    public static UserModel getOne(String... excludeKeys) {
        Long id = null;
        String username = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        String password = null;
        String phone = null;
        Integer userStatus = null;
        List<String> excludeList = Arrays.asList(excludeKeys);

        if (!excludeList.contains("id")) id = (long) fake.number().numberBetween(1, 9999999);
        if (!excludeList.contains("username")) username = fake.name().username();
        if (!excludeList.contains("firstName")) firstName = fake.name().firstName();
        if (!excludeList.contains("lastName")) lastName = fake.name().lastName();
        if (!excludeList.contains("email")) email = fake.internet().emailAddress();
        if (!excludeList.contains("password")) password = fake.internet().password();
        if (!excludeList.contains("phone")) phone = fake.phoneNumber().cellPhone();
        if (!excludeList.contains("userStatus")) userStatus = fake.number().numberBetween(0, 1);

        return new UserModel(id, username, firstName, lastName, email, password, phone, userStatus);
    }

    public static UserModel[] getArray(int number, String... excludeKeys) {
        UserModel[] result = new UserModel[number];

        for (int i = 0; i < number; i++) {
            result[i] = getOne(excludeKeys);
        }

        return result;
    }
}
