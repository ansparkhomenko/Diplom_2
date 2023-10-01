package ru.yandex.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderServiceApi {
    private static final String URL = "https://stellarburgers.nomoreparties.site";
    private static final String ORDERS_ENDPOINT = "/api/orders";
    public RequestSpecification requestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(URL)
            .build();

    @Step("Создание заказа без авторизации")
    public Response createOrder(OrderData orderData) {
        return given()
                .spec(requestSpec)
                .body(orderData)
                .post(ORDERS_ENDPOINT);
    }

    @Step("Создание заказа c авторизацией")
    public Response createOrderWithAuth(OrderData orderData, String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(requestSpec)
                .body(orderData)
                .post(ORDERS_ENDPOINT);
    }

    @Step("Получение заказа пользователя без авторизации")
    public Response getUserOrder() {
        return given()
                .spec(requestSpec)
                .get(ORDERS_ENDPOINT);
    }

    @Step("Получение заказа пользователя с авторизацией")
    public Response getUserOrderWithAuth(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(requestSpec)
                .get(ORDERS_ENDPOINT);
    }
}
