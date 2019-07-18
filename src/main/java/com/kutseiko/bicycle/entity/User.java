package com.kutseiko.bicycle.entity;

import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {

    @NotNull
    Long id;

    @NotNull
    UserType userType;

    @NotNull
    Gender gender;

    String email;

    LocalDate dateOfBirth;
}
