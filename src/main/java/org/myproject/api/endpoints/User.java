package org.myproject.api.endpoints;

import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.myproject.api.models.ApiResponseModel;
import org.myproject.api.models.UserModel;

import static io.restassured.RestAssured.given;
import static org.myproject.api.config.Config.*;

public class User extends BaseApi {
    public Response createUser(UserModel payload, int expectedStatusCode) {
        return Allure.step("Создание пользователя: POST /user", step -> {
            step.parameter("expectedStatusCode", expectedStatusCode);

            super.standardPayloadValidation(payload, UserModel.class);

            Response response = given().contentType(ContentType.JSON).body(payload)
                    .when().post(API_BASE_URL + "/user");

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }

    public Response createUsersList(UserModel[] payloadList, int expectedStatusCode) {
        return Allure.step("Создание пользователей из списка: POST /user/createWithList", step -> {
            step.parameter("expectedStatusCode", expectedStatusCode);

            super.arrayPayloadValidation(payloadList, UserModel.class, "Пользователь");

            Response response = given().contentType(ContentType.JSON).body(payloadList)
                    .when().post(API_BASE_URL + "/user/createWithList");

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }

    public Response loginUser(String username, String password, int expectedStatusCode) {
        return Allure.step("Вход пользователя: GET /user/login", step -> {
            step.parameter("username", username);
            step.parameter("password", password);
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .queryParam("username", username).queryParam("password", password)
                    .when().get(API_BASE_URL + "/user/login");

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }

    public Response logoutUser(int expectedStatusCode) {
        return Allure.step("Выход пользователя: GET /user/logout", step -> {
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .when().get(API_BASE_URL + "/user/logout");

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }

    public Response getUserByUsername(String username, int expectedStatusCode) {
        return Allure.step("Получение пользователя по username: GET /user/{username}", step -> {
            step.parameter("username", username);
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .when().get(API_BASE_URL + "/user/" + username);

            super.standardResponseValidation(response, expectedStatusCode, UserModel.class);
            return response;
        });
    }

    public Response updateUser(String username, UserModel payload, int expectedStatusCode) {
        return Allure.step("Обновление пользователя: PUT /user/{username}", step -> {
            step.parameter("username", username);
            step.parameter("expectedStatusCode", expectedStatusCode);

            super.standardPayloadValidation(payload, UserModel.class);

            Response response = given().contentType(ContentType.JSON).body(payload)
                    .when().put(API_BASE_URL + "/user/" + username);

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }

    public Response deleteUser(String username, int expectedStatusCode) {
        return Allure.step("Удаление пользователя: DELETE /user/{username}", step -> {
            step.parameter("username", username);
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .when().delete(API_BASE_URL + "/user/" + username);

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }
}