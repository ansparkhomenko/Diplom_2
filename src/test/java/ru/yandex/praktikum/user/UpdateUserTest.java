package ru.yandex.praktikum.user;

import core.UserHelper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserTest {
    private UserData user;
    private Response response;
    private UserServiceApi userServiceApi = new UserServiceApi();
    private String accessToken;
    private Response createdUser;

    @Before
    public void init() {
        user = UserHelper.getUniqueUser();
        createdUser = userServiceApi.registerUser(user);
        accessToken = userServiceApi.loginUser(user)
                .then()
                .log().all()
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Обновление данных не авторизованного пользователя")
    @Description("Не успешное обновление данных")
    public void updateUserDataNoAuth() {
        response = userServiceApi.updateUserWithoutToken(user);
        response.then()
                .log().all()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Обновление Email авторизованного пользователя")
    @Description("Успешное обновление данных")
    public void updateUserEmailWithAuth() {
        user.setEmail("222newemail@yandex.ru");
        response = userServiceApi.updateUserWithToken(user, accessToken)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals("222newemail@yandex.ru", jsonPath.getString("user.email"));
    }

    @Test
    @DisplayName("Обновление имени авторизованного пользователя")
    @Description("Успешное обновление данных")
    public void updateUserNamedWithAuth() {
        user.setName("UpdatedName");
        response = userServiceApi.updateUserWithToken(user, accessToken)
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals("UpdatedName", jsonPath.getString("user.name"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userServiceApi.deleteUser(accessToken);
        }
    }
}
