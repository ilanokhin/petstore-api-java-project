package org.myproject.api.validator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;

import java.util.ArrayList;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

public class Validator {
    public static String[] validate(Object payloadOrResponseModel) throws JsonProcessingException {
        String[] errors;
        ObjectMapper mapper = new ObjectMapper();
        var validator = buildDefaultValidatorFactory().getValidator();
        var violations = validator.validate(payloadOrResponseModel);
        var violationList = new ArrayList<>(violations);
        String json;

        errors = new String[violationList.size() + 1];
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        errors[0] = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payloadOrResponseModel);

        for (int i = 1; i < errors.length; i++) {
            String message = violationList.get(i - 1).getMessage();
            var field = violationList.get(i - 1).getPropertyPath();

            errors[i] = String.format("Ошибка валидации #%d:\n* Она связана с полем: %s\n* Суть ошибки: %s",
                    i, field, message);
        }

        return errors;
    }

    public static void attachFiles(String[] errors, String objectName) {
        StringBuilder errorsSB = new StringBuilder();

        if (errors.length > 1) {
            for (int i = 1; i < errors.length; i++) {
                errorsSB.append(errors[i]).append("\n\n");
            }

            Allure.addAttachment(String.format("%sСписок ошибок (%d)", objectName, errors.length - 1), "text/plain",
                    errorsSB.toString(), ".txt");

            if (!objectName.isEmpty()) {
                Allure.addAttachment(String.format("%sОтвет с ошибками", objectName), "text/plain", errors[0], ".txt");
            }
        }
    }

    public static void attachFiles(String[] errors) {
        attachFiles(errors, "");
    }
}
