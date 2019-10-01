package com.kutseiko.bicycle.entity;

import com.kutseiko.bicycle.annotations.ColumnName;
import com.kutseiko.bicycle.annotations.TableName;
import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
import com.kutseiko.bicycle.core.type.db.tables.AppUserTable;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(name = AppUserTable.TABLE)
public class User {

    @NotNull
    @ColumnName(name = AppUserTable.ID)
    Long id;

    @NotNull
    @ColumnName(name = AppUserTable.USER_TYPE)
    UserType userType;

    @NotNull
    @ColumnName(name = AppUserTable.GENDER)
    Gender gender;

    @ColumnName(name = AppUserTable.EMAIL)
    String email;

    @ColumnName(name = AppUserTable.BIRTHDAY)
    LocalDate dateOfBirth;
}
