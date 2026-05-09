package org.myproject.api.endpoints;

import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.myproject.api.models.ApiResponseModel;
import org.myproject.api.models.PetModel;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.myproject.api.config.Config.*;

public class Pet extends BaseApi {
    public Response uploadPetImage(long petId, String filePath, String additionalMetadata, int expectedStatusCode) {
        return Allure.step("Загрузка изображения питомца: POST /pet/{petId}/uploadImage", step -> {
            step.parameter("petId", petId);
            step.parameter("filePath", filePath);
            step.parameter("additionalMetadata", additionalMetadata);
            step.parameter("expectedStatusCode", expectedStatusCode);

            var request = given().contentType(ContentType.MULTIPART).multiPart("file", new File(filePath));
            if (additionalMetadata != null) request.formParam("additionalMetadata", additionalMetadata);

            Response response = request.when().post(API_BASE_URL + "/pet/" + petId + "/uploadImage");

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }

    public Response addNewPet(PetModel payload, int expectedStatusCode) {
        return Allure.step("Добавление нового питомца: POST /pet", step -> {
            step.parameter("expectedStatusCode", expectedStatusCode);

            super.standardPayloadValidation(payload, PetModel.class);

            Response response = given().contentType(ContentType.JSON).body(payload)
                    .when().post(API_BASE_URL + "/pet");

            super.standardResponseValidation(response, expectedStatusCode, PetModel.class);
            return response;
        });
    }

    public Response updatePet(PetModel payload, int expectedStatusCode) {
        return Allure.step("Обновление существующего питомца: PUT /pet", step -> {
            step.parameter("expectedStatusCode", expectedStatusCode);

            super.standardPayloadValidation(payload, PetModel.class);

            Response response = given().contentType(ContentType.JSON).body(payload)
                    .when().put(API_BASE_URL + "/pet");

            super.standardResponseValidation(response, expectedStatusCode, PetModel.class);
            return response;
        });
    }

    public Response findPetsByStatus(String status, int expectedStatusCode) {
        return Allure.step("Поиск питомцев по статусу: GET /pet/findByStatus", step -> {
            step.parameter("status", status);
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON).queryParam("status", status)
                    .when().get(API_BASE_URL + "/pet/findByStatus");

            super.arrayResponseValidation(response, expectedStatusCode, PetModel[].class, "Питомец");
            return response;
        });
    }

    public Response getPetById(long petId, int expectedStatusCode) {
        return Allure.step("Получение питомца по ID: GET /pet/{petId}", step -> {
            step.parameter("petId", petId);
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .when().get(API_BASE_URL + "/pet/" + petId);

            super.standardResponseValidation(response, expectedStatusCode, PetModel.class);
            return response;
        });
    }

    public Response updatePetWithForm(long petId, String name, String status, int expectedStatusCode) {
        return Allure.step("Обновление питомца через форму: POST /pet/{petId}", step -> {
            step.parameter("petId", petId);
            step.parameter("name", name);
            step.parameter("status", status);
            step.parameter("expectedStatusCode", expectedStatusCode);

            var request = given().contentType(ContentType.URLENC);
            if (name != null) request.formParam("name", name);
            if (status != null) request.formParam("status", status);

            Response response = request.when().post(API_BASE_URL + "/pet/" + petId);
            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }

    public Response deletePet(long petId, int expectedStatusCode) {
        return Allure.step("Удаление питомца: DELETE /pet/{petId}", step -> {
            step.parameter("petId", petId);
            step.parameter("expectedStatusCode", expectedStatusCode);

            Response response = given().contentType(ContentType.JSON)
                    .when().delete(API_BASE_URL + "/pet/" + petId);

            super.standardResponseValidation(response, expectedStatusCode, ApiResponseModel.class);
            return response;
        });
    }
}
