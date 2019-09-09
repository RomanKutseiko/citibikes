package com.kutseiko.bicycle.controller.handler;

import com.kutseiko.bicycle.exception.CustomSQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @ResponseBody
    @ExceptionHandler(CustomSQLException.class)
    public ResponseEntity<CustomResponceBody> handleCustomException(CustomSQLException e) {
        log.error(e.getSqlException().getSQLState(), e.getSqlException().getErrorCode(), e.getSqlException().getMessage());
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new CustomResponceBody("Unfortunately, something went wrong with database!",
                e.getSqlException().getErrorCode()));
    }

}
