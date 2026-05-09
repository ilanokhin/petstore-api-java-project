package org.myproject.api.tests;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.myproject.api.endpoints.Pet;
import org.myproject.api.endpoints.Store;
import org.myproject.api.endpoints.User;

public class BaseTest {
    protected static Faker fake;
    protected static Pet pet;
    protected static Store store;
    protected static User user;

    @BeforeAll
    public static void setUp() {
        fake = new Faker();
        pet = new Pet();
        store = new Store();
        user = new User();
    }
}
