package org.myproject.api.tests;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.myproject.api.models.OrderModel;
import org.myproject.api.models.PetModel;
import org.myproject.api.models.UserModel;
import org.myproject.api.payloads.PetPayload;
import org.myproject.api.payloads.StorePayload;
import org.myproject.api.payloads.UserPayload;
import org.myproject.api.utils.PayloadHolder;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тестирование Petstore API")
@Tag("petstore_api_suite")
public class TestPetstoreApi extends BaseTest {
    @Test
    @DisplayName("Полный цикл работы с питомцем")
    @Tag("pet_group")
    public void testPetGroup() {
        var holder = new PayloadHolder<>(PetPayload.getOne("category", "tags"));

        pet.addNewPet(holder.payload, 200);
        pet.uploadPetImage(holder.payload.id(), "src/test/resources/images/dog1.jpg", null, 200);

        Allure.step("Получить данные питомца по ID, проверить, что они совпадают с ранее отправленными", () -> {
            Response response = pet.getPetById(holder.payload.id(), 200);
            PetModel responsePet = response.as(PetModel.class);
            boolean check = true;

            if (!holder.payload.id().equals(responsePet.id())) check = false;
            if (!holder.payload.name().equals(responsePet.name())) check = false;
            if (!holder.payload.status().equals(responsePet.status())) check = false;
            if (!Arrays.equals(holder.payload.photoUrls(), responsePet.photoUrls())) check = false;

            assertTrue(check, "Полученные данные не совпадают с отправленными");
        });

        holder.payload = Allure.step("Обновить статус питомца", () -> {
            String newStatus = fake.options().option("available", "pending", "sold");

            while (holder.payload.status().equals(newStatus)) {
                newStatus = fake.options().option("available", "pending", "sold");
            }

            PetModel updatedPayload = new PetModel(holder.payload.id(), holder.payload.category(),
                    holder.payload.name(), holder.payload.photoUrls(), holder.payload.tags(), newStatus);

            pet.updatePet(updatedPayload, 200);
            return updatedPayload;
        });

        holder.payload = Allure.step("Обновить имя питомца через форму", () -> {
            String newName = fake.name().firstName();

            pet.updatePetWithForm(holder.payload.id(), newName, null, 200);

            return new PetModel(holder.payload.id(), holder.payload.category(), newName, holder.payload.photoUrls(),
                    holder.payload.tags(), holder.payload.status());
        });

        Allure.step("Получить имя и статус питомца по ID, проверить, что они обновились", () -> {
            Response response = pet.getPetById(holder.payload.id(), 200);
            PetModel responsePet = response.as(PetModel.class);

            assertEquals(holder.payload.name(), responsePet.name(), "Имя питомца не обновлено");
            assertEquals(holder.payload.status(), responsePet.status(), "Статус питомца не обновлён");
        });

        pet.deletePet(holder.payload.id(), 200);
        pet.getPetById(holder.payload.id(), 404);
        pet.findPetsByStatus(fake.options().option("available", "pending", "sold"), 200);
    }

    @Test
    @DisplayName("Управление заказами и инвентарём")
    @Tag("store_group")
    public void testStoreGroup() {
        var holder = new PayloadHolder<>(StorePayload.getOne("shipDate"));

        Allure.step("Получить инвентарь, проверить, что он содержит статусы 'available', 'pending' и 'sold'", () -> {
            Response response = store.getInventory(200);
            var inventory = response.as(Map.class);

            assertTrue(inventory.containsKey("available"), "Инвентарь не содержит статус 'available'");
            assertTrue(inventory.containsKey("pending"), "Инвентарь не содержит статус 'pending'");
            assertTrue(inventory.containsKey("sold"), "Инвентарь не содержит статус 'sold'");
        });

        store.placeOrder(holder.payload, 200);

        Allure.step("Получить данные заказа по ID, проверить, что они совпадают с ранее отправленными", () -> {
            Response response = store.getOrderById(holder.payload.id(), 200);
            OrderModel responseOrder = response.as(OrderModel.class);
            boolean check = true;

            if (!holder.payload.id().equals(responseOrder.id())) check = false;
            if (!holder.payload.petId().equals(responseOrder.petId())) check = false;
            if (!holder.payload.quantity().equals(responseOrder.quantity())) check = false;
            if (!holder.payload.status().equals(responseOrder.status())) check = false;
            if (!holder.payload.complete().equals(responseOrder.complete())) check = false;

            assertTrue(check, "Полученные данные не совпадают с отправленными");
        });

        store.deleteOrder(holder.payload.id(), 200);
        store.getOrderById(holder.payload.id(), 404);
    }

    @Test
    @DisplayName("Полный цикл управления пользователями")
    @Tag("user_group")
    public void testUserGroup() {
        UserModel[] userList = UserPayload.getArray(5);
        var holder = new PayloadHolder<>(userList[1]);

        user.createUsersList(userList, 200);

        Allure.step("Получить второго пользователя по username, проверить, что данные соответствуют отправленным", () -> {
            Response response = user.getUserByUsername(holder.payload.username(), 200);
            UserModel responseUser = response.as(UserModel.class);
            boolean check = true;

            if (!holder.payload.id().equals(responseUser.id())) check = false;
            if (!holder.payload.username().equals(responseUser.username())) check = false;
            if (!holder.payload.firstName().equals(responseUser.firstName())) check = false;
            if (!holder.payload.lastName().equals(responseUser.lastName())) check = false;
            if (!holder.payload.email().equals(responseUser.email())) check = false;
            if (!holder.payload.password().equals(responseUser.password())) check = false;
            if (!holder.payload.phone().equals(responseUser.phone())) check = false;
            if (!holder.payload.userStatus().equals(responseUser.userStatus())) check = false;

            assertTrue(check, "Полученные данные не соответствуют отправленным");
        });

        holder.payload = UserPayload.getOne();
        user.createUser(holder.payload, 200);

        Allure.step("Получить пользователя по username, проверить, что данные соответствуют отправленным", () -> {
            Response response = user.getUserByUsername(holder.payload.username(), 200);
            UserModel responseUser = response.as(UserModel.class);
            boolean check = true;

            if (!holder.payload.id().equals(responseUser.id())) check = false;
            if (!holder.payload.username().equals(responseUser.username())) check = false;
            if (!holder.payload.firstName().equals(responseUser.firstName())) check = false;
            if (!holder.payload.lastName().equals(responseUser.lastName())) check = false;
            if (!holder.payload.email().equals(responseUser.email())) check = false;
            if (!holder.payload.password().equals(responseUser.password())) check = false;
            if (!holder.payload.phone().equals(responseUser.phone())) check = false;
            if (!holder.payload.userStatus().equals(responseUser.userStatus())) check = false;

            assertTrue(check, "Полученные данные не соответствуют отправленным");
        });

        holder.payload = Allure.step("Обновить телефон и email пользователя", () -> {
            String newPhone = fake.phoneNumber().cellPhone();
            String newEmail = fake.internet().emailAddress();

            UserModel updatedPayload = new UserModel(
                    holder.payload.id(),
                    holder.payload.username(),
                    holder.payload.firstName(),
                    holder.payload.lastName(),
                    newEmail,
                    holder.payload.password(),
                    newPhone,
                    holder.payload.userStatus()
            );

            user.updateUser(holder.payload.username(), updatedPayload, 200);
            return updatedPayload;
        });

        Allure.step("Получить телефон и email пользователя по username, проверить, что они обновлены", () -> {
            Response response = user.getUserByUsername(holder.payload.username(), 200);
            UserModel responseUser = response.as(UserModel.class);

            assertEquals(holder.payload.phone(), responseUser.phone(), "Телефон пользователя не обновлён");
            assertEquals(holder.payload.email(), responseUser.email(), "Email пользователя не обновлён");
        });

        user.loginUser(holder.payload.username(), holder.payload.password(), 200);
        user.logoutUser(200);
        user.deleteUser(holder.payload.username(), 200);
        user.getUserByUsername(holder.payload.username(), 404);
    }
}
