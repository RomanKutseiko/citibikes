package com.kutseiko.bicycle.controller.handler;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class CustomResponceBody {
    private final String errorMessage;
    private final Integer errorCode;
}
