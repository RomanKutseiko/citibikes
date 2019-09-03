package com.kutseiko.bicycle.DTO;

import com.kutseiko.bicycle.core.type.Gender;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {

    String email;

    LocalDate dateOfBirth;

    String gender = Gender.ANOTHER.getName();

    @NotNull
    String userType;

}
