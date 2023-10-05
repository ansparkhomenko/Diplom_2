package ru.yandex.praktikum.user;

import core.UserHelper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class RegisterUserTest {

    private UserData user;
    private UserServiceApi userServiceApi = new UserServiceApi();
    private Response response;

    @Test
    @DisplayName("Регистрация нового пользователя")
    @Description("Успешная регистрация")
    public void registerUser() {
        user = UserHelper.getUniqueUser();
        response = userServiceApi.registerUser(user);
        response.then()
                .log().all()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Регистрация уже зарегистрированного пользователя")
    @Description("Неуспешная регистрация")
    public void registerExistingUser() {
        user = UserHelper.getUniqueUser();
        userServiceApi.registerUser(user);
        response = userServiceApi.registerUser(user);
        response.then()
                .log().all()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Регистрация пользователя без обязательных полей")
    @Description("Неуспешная регистрация")
    public void registerUserWithoutRequiredFields() {
        user = new UserData("", "", "");
        response = userServiceApi.registerUser(user);
        response.then()
                .log().all()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
