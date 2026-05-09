package org.myproject.api.models;

public record ApiResponseModel(
        Integer code,
        String type,
        String message
) {}
