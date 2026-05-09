package org.myproject.api.endpoints;

import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.myproject.api.models.ApiResponseModel;
import org.myproject.api.models.OrderModel;

import static io.restassured.RestAssured.given;
import static org.myproject.api.config.Config.*;

public class Store extends BaseApi {
    public Response getInventory(int expectedStatusCode) {
        return Allure.step("Получение инвентаря: GET /store/inventory", step -> {
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .when().get(API_BASE_URL + "/store/inventory");

            standardResponseValidation(response, 200, null);
            return response;
        });
    }

    public Response placeOrder(OrderModel payload, int expectedStatusCode) {
        return Allure.step("Создание заказа: POST /store/order", step -> {
            step.parameter("expectedStatusCode", expectedStatusCode);

            super.standardPayloadValidation(payload, OrderModel.class);

            Response response = given().contentType(ContentType.JSON).body(payload)
                    .when().post(API_BASE_URL + "/store/order");

            super.standardResponseValidation(response, expectedStatusCode, OrderModel.class);
            return response;
        });
    }

    public Response getOrderById(long orderId, int expectedStatusCode) {
        return Allure.step("Получение заказа по ID: GET /store/order/{orderId}", step -> {
            step.parameter("orderId", orderId);
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .when().get(API_BASE_URL + "/store/order/" + orderId);

            super.standardResponseValidation(response, expectedStatusCode, OrderModel.class);
            return response;
        });
    }

    public Response deleteOrder(long orderId, int expectedStatusCode) {
        return Allure.step("Удаление заказа: DELETE /store/order/{orderId}", step -> {
            step.parameter("orderId", orderId);
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .when().delete(API_BASE_URL + "/store/order/" + orderId);

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }
}