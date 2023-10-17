package ru.yandex.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserServiceApi {
    private static final String URL = "https://stellarburgers.nomoreparties.site";
    private static final String REGISTER_USER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_USER_ENDPOINT = "/api/auth/login";
    private static final String DELETE_AND_UPDATE_USER_ENDPOINT = "/api/auth/user";
    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(URL)
            .build();

    @Step("Регистрация пользователя")
    public Response registerUser(UserData user) {
        return given()
                .spec(requestSpec)
                .body(user)
                .post(REGISTER_USER_ENDPOINT);
    }

    @Step("Логин пользователя")
    public Response loginUser(UserData user){
        return given()
                .spec(requestSpec)
                .body(user)
                .post(LOGIN_USER_ENDPOINT);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(requestSpec)
                .delete(DELETE_AND_UPDATE_USER_ENDPOINT);
    }
    @Step("Обновление данных о пользователе с авторизацией")
    public Response updateUserWithToken(UserData user, String accessToken){
        return given()
                .header("authorization", accessToken)
                .spec(requestSpec)
                .body(user)
                .patch(DELETE_AND_UPDATE_USER_ENDPOINT);

    }
    @Step("Обновление данных о пользователе без авторизации")
    public Response updateUserWithoutToken(UserData user){
        return given()
                .spec(requestSpec)
                .body(user)
                .patch(DELETE_AND_UPDATE_USER_ENDPOINT);

    }
}

