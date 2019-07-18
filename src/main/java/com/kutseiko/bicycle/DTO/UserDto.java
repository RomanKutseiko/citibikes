package com.kutseiko.bicycle.DTO;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {

    String email;

    LocalDate dateOfBirth;

    @NotNull
    int gender = 0;

    @NotNull
    String userType;

}
