package org.myproject.api.endpoints;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.myproject.api.models.ApiResponseModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.myproject.api.validator.Validator.attachFiles;
import static org.myproject.api.validator.Validator.validate;

public class BaseApi {
    protected void standardResponseValidation(Response response, int expectedStatusCode, Class<?> modelClass) {
        Allure.step("Валидация ответа", () -> {
            Allure.addAttachment("Статус код", "text/plain", String.valueOf(response.getStatusCode()), ".txt");
            Allure.addAttachment("Ответ", "application/json", response.asPrettyString(), ".json");

            assertEquals(expectedStatusCode, response.getStatusCode(),
                    String.format("Ожидался статус код %d, получен %d", expectedStatusCode, response.getStatusCode()));

            if (modelClass == null) return;

            var responseModel = expectedStatusCode >= 400
                    ? response.then().extract().as(ApiResponseModel.class)
                    : response.then().extract().as(modelClass);

            String message = "";
            String[] errors = validate(responseModel);

            attachFiles(errors);
            if (errors.length > 1) message = errors[1];

            assertTrue(errors.length < 2,
                    String.format("%s\n\nДополнительная информация в приложенных файлах.\n", message));
        });
    }

    protected <T> void arrayResponseValidation(Response response, int expectedStatusCode, Class<T[]> modelClass,
                                               String unitName) {
        Allure.step("Валидация ответа", () -> {
            int size = response.then().extract().body().jsonPath().getList("").size();

            Allure.addAttachment("Статус код", "text/plain", String.valueOf(response.getStatusCode()), ".txt");
            Allure.addAttachment(String.format("Ответ (%d)", size), "application/json",
                    response.asPrettyString(), ".json");

            assertEquals(expectedStatusCode, response.getStatusCode(),
                    String.format("Ожидался статус код %d, получен %d", expectedStatusCode, response.getStatusCode()));

            if (expectedStatusCode >= 400) {
                var responseModel = response.then().extract().as(ApiResponseModel.class);
                String message = "";
                String[] errors = validate(responseModel);

                attachFiles(errors);
                if (errors.length > 1) message = errors[1];

                assertTrue(errors.length < 2,
                        String.format("%s\n\nДополнительная информация в приложенных файлах.\n", message));
            } else {
                Object[] responseModels = response.then().extract().as(modelClass);
                String firstError = "";

                for (int i = 0; i < responseModels.length; i++) {
                    String[] errors = validate(responseModels[i]);
                    String unit = String.format("[%s #%d] ", unitName, i + 1);
                    attachFiles(errors, unit);

                    if (firstError.isEmpty() && errors.length > 1) firstError = unit + "\n" + errors[1];
                }

                assertTrue(firstError.isEmpty(),
                        String.format("%s\n\nДополнительная информация в приложенных файлах.\n", firstError));
            }
        });
    }

    protected void standardPayloadValidation(Object payload, Class<?> modelClass) {
        Allure.step("Валидация запроса", () -> {
            ObjectMapper mapper = new ObjectMapper();
            String json, message = "";

            mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            Allure.addAttachment("Тело запроса", "text/plain", json, ".txt");

            String[] errors = validate(payload);
            attachFiles(errors);

            if (errors.length > 1) message = errors[1];

            assertTrue(errors.length < 2,
                    String.format("%s\n\nДополнительная информация в приложенных файлах.\n", message));
        });
    }

    protected void arrayPayloadValidation(Object[] payload, Class<?> modelClass, String unitName) {
        Allure.step("Валидация запроса", () -> {
            ObjectMapper mapper = new ObjectMapper();
            String json;

            mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            Allure.addAttachment("Тело запроса", "text/plain", json, ".txt");

            String firstError = "";

            for (int i = 0; i < payload.length; i++) {
                String[] errors = validate(payload[i]);
                String unit = String.format("[%s #%d] ", unitName, i + 1);
                attachFiles(errors, unit);

                if (firstError.isEmpty() && errors.length > 1) firstError = unit + "\n" + errors[1];
            }

            assertTrue(firstError.isEmpty(),
                    String.format("%s\n\nДополнительная информация в приложенных файлах.\n", firstError));
        });
    }
}
