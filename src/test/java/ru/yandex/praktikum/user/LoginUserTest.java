package ru.yandex.praktikum.user;

import core.UserHelper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
    private String accessToken;
    private UserData user;
    private UserServiceApi userServiceApi = new UserServiceApi();
    private Response response;


    @Test
    @DisplayName("Авторизация зарегистрированного пользователя")
    @Description("Успешная авторизация пользователя")
    public void authenticateUser() {
        user = UserHelper.getUniqueUser();
        response = userServiceApi.registerUser(user);
        response = userServiceApi.loginUser(user);
        response.then()
                .log().all()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация не зарегистрированного пользователя")
    @Description("Не успешная авторизация")
    public void authenticateNotExistentUser() {
        user = UserHelper.getUniqueUser();
        response = userServiceApi.loginUser(user);
        response.then()
                .log().all()
                .statusCode(401)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        accessToken = userServiceApi.loginUser(user)
                .then()
                .extract()
                .path("accessToken");
        if (accessToken != null) {
            userServiceApi.deleteUser(accessToken);
        }
    }
}
