package com.kutseiko.bicycle.repository;

import static com.kutseiko.bicycle.utils.DateConverter.convertDateToLocalDate;

import com.kutseiko.bicycle.annotations.ColumnName;
import com.kutseiko.bicycle.annotations.TableName;
import com.kutseiko.bicycle.core.type.Gender;
import com.kutseiko.bicycle.core.type.UserType;
import com.kutseiko.bicycle.exception.CustomSQLException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericDao<E> {
    private Class<E> entityClass;
    private final DataSource dataSource;

    private static String setterPrefix = "set";
    private static String getAllSql;
    private static String getByIdSql;
    private static String updateSql;
    private static String deletedSql;
    private static String addSql;

    /*{
        StringBuilder sb = new StringBuilder();
        getAllSql = sb.append("SELECT * FROM ").append(entityClass.getAnnotation(TableName.class).name()).toString(); //AppUserTable.TABLE
        /*getByIdSql = sb.append(" WHERE ").append(AppUserTable.ID).append("=?").toString();
        updateSql = new StringBuilder().append("UPDATE ").append(AppUserTable.TABLE).append(" SET ").append(AppUserTable.BIRTHDAY)
            .append("=?, ").append(AppUserTable.EMAIL).append("=?, ").append(AppUserTable.GENDER).append("=?::GENDER_ENUM, ")
            .append(AppUserTable.USER_TYPE).append("=?::USER_TYPE_ENUM WHERE ").append(AppUserTable.ID).append("=?").toString();
        deletedSql = new StringBuilder().append("DELETE FROM ").append(AppUserTable.TABLE).append(" WHERE ").append(AppUserTable.ID)
            .append("=?").toString();
        addSql = new StringBuilder().append("INSERT INTO ").append(AppUserTable.TABLE).append("(").append(AppUserTable.BIRTHDAY)
            .append(", ").append(AppUserTable.EMAIL).append(", ").append(AppUserTable.GENDER).append(", ").append(AppUserTable.USER_TYPE)
            .append(") VALUES (?, ?, ?::GENDER_ENUM, ?::USER_TYPE_ENUM)").toString();
    }*/

    public GenericDao(Class<E> entityClass, DataSource dataSource) {
        this.entityClass = entityClass;
        this.dataSource = dataSource;
        StringBuilder sb = new StringBuilder();
        getAllSql = sb.append("SELECT * FROM ").append(entityClass.getAnnotation(TableName.class).name()).toString(); //AppUserTable.TABLE
    }

    public List<E> findAll() {
        log.debug(getAllSql);
        List<E> items = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(getAllSql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(mapItemFromRS(rs));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new CustomSQLException(e);
        }
        return items;
    }

    /*public Optional<E> persist(E toPersist) {
        // ...
    }*/

    private E mapItemFromRS(ResultSet rs) {
        try {
            Annotation[] annotations = entityClass.getAnnotations();
            log.info(annotations.toString());
            E item = entityClass.newInstance();
            Field[] fields = item.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                }
                StringBuilder sb = new StringBuilder();
                Method method = item.getClass()
                    .getMethod(sb.append(setterPrefix).append(field.getName().substring(0, 1).toUpperCase()).append(field.getName().substring(1)).toString(), field.getType());
                //method.invoke(item, field.getType().cast(rs.getObject(field.getAnnotation(ColumnName.class).name())));
                if (field.getName().equalsIgnoreCase("userType")) {
                    method.invoke(item, UserType.valueOf(rs.getString(field.getAnnotation(ColumnName.class).name())));
                } else if (field.getName().equalsIgnoreCase("gender")) {
                    method.invoke(item, Gender.valueOf(rs.getString(field.getAnnotation(ColumnName.class).name())));
                } else if (field.getName().equalsIgnoreCase("dateOfBirth")) {
                    method.invoke(item, convertDateToLocalDate(rs.getDate(field.getAnnotation(ColumnName.class).name())));
                } else {
                    method.invoke(item, field.getType().cast(rs.getObject(field.getAnnotation(ColumnName.class).name())));
                }
            }
            return item;
            /*User().setId(rs.getLong(AppUserTable.ID))
                .setDateOfBirth(convertDateToLocalDate(rs.getDate(AppUserTable.BIRTHDAY)))
                .setEmail(rs.getString(AppUserTable.EMAIL))
                .setGender(Gender.valueOf(rs.getString(AppUserTable.GENDER)))
                .setUserType(UserType.valueOf(rs.getString(AppUserTable.USER_TYPE)));*/

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SQLException | InvocationTargetException e) {
            log.error("Failed to instantiate entity: {}", e.getMessage());
            throw new RuntimeException("Failed to instantiate entity: " + e.getMessage());
        }
    }

}
