package ru.yandex.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserServiceApi {
    private final String URL = "https://stellarburgers.nomoreparties.site";
    private RequestSpecification requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(URL)
            .build();

    @Step("Регистрация пользователя")
    public Response registerUser(UserData user) {
        return given()
                .spec(requestSpec)
                .body(user)
                .post("/api/auth/register");
    }

    @Step("Логин пользователя")
    public Response loginUser(UserData user){
        return given()
                .spec(requestSpec)
                .body(user)
                .post("/api/auth/login");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(requestSpec)
                .delete("/api/auth/user");
    }
    @Step("Обновление данных о пользователе с авторизацией")
    public Response updateUserWithToken(UserData user, String accessToken){
        return given()
                .header("authorization", accessToken)
                .spec(requestSpec)
                .body(user)
                .patch("/api/auth/user");

    }
    @Step("Обновление данных о пользователе без авторизации")
    public Response updateUserWithoutToken(UserData user){
        return given()
                .spec(requestSpec)
                .body(user)
                .patch("/api/auth/user");

    }
}

