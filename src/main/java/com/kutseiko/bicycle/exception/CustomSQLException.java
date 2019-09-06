package com.kutseiko.bicycle.exception;

import java.sql.SQLException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class CustomSQLException extends RuntimeException {
    private SQLException sqlException;
    public CustomSQLException(SQLException e) {
        super(e);
        sqlException = e;
    }

}
