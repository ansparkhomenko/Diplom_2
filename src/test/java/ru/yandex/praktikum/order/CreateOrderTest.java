package ru.yandex.praktikum.order;

import core.IngredientListHelper;
import core.UserHelper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.user.UserData;
import ru.yandex.praktikum.user.UserServiceApi;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {
    private OrderServiceApi orderServiceApi = new OrderServiceApi();
    private UserServiceApi userServiceApi = new UserServiceApi();
    private OrderData orderData;
    private List<String> ingredients;
    private UserData user;

    private String accessToken;

    @Before
    public void init() {
        user = UserHelper.getDefaultUser();
        userServiceApi.registerUser(user);
        accessToken = userServiceApi.loginUser(user).then().extract().path("accessToken");
    }


    @Test
    @DisplayName("Создание заказа с авторизацией и корректными хешами ингредиентов")
    @Description("Успешное создание заказа")
    public void createOrderWithValidIngredients() {
        ingredients = IngredientListHelper.createDefaultIngredientList();
        orderData = new OrderData(ingredients);
        Response response = orderServiceApi.createOrderWithAuth(orderData, accessToken);
        response.then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo("Био-марсианский минеральный бургер"))
                .body("order.number", greaterThan(0))
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с невалидными хешами ингредиентов")
    @Description("Неуспешное создание заказа")
    public void createOrderWithInvalidIngredients() {
        ingredients = IngredientListHelper.createInvalidHashIngredientList();
        orderData = new OrderData(ingredients);
        Response response = orderServiceApi.createOrderWithAuth(orderData, accessToken);
        response.then()
                .log().all()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с пустым списком ингредиентов")
    @Description("Неуспешное создание заказа")
    public void createOrderWithNoIngredients() {
        ingredients = IngredientListHelper.createEmptyIngredientList();
        orderData = new OrderData(ingredients);
        Response response = orderServiceApi.createOrderWithAuth(orderData, accessToken);
        response.then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @DisplayName("Создание заказа без авторизаци и корректными хешами ингредиентов")
    @Description("Успешное создание заказа")
    public void createOrderNoAuthWithValidIngredients() {
        ingredients = IngredientListHelper.createDefaultIngredientList();
        orderData = new OrderData(ingredients);
        Response response = orderServiceApi.createOrder(orderData);
        response.then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo("Био-марсианский минеральный бургер"))
                .body("order.number", greaterThan(0))
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с невалидными хешами ингредиентов")
    @Description("Неуспешное создание заказа")
    public void createOrderNoWithInvalidIngredients() {
        ingredients = IngredientListHelper.createInvalidHashIngredientList();
        orderData = new OrderData(ingredients);
        Response response = orderServiceApi.createOrder(orderData);
        response.then()
                .log().all()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с пустым списком ингредиентов")
    @Description("Неуспешное создание заказа")
    public void createOrderNoAuthWithNoIngredients() {
        ingredients = IngredientListHelper.createEmptyIngredientList();
        orderData = new OrderData(ingredients);
        Response response = orderServiceApi.createOrder(orderData);
        response.then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @After
    public void tearDown() {
        if (accessToken != null) {
            userServiceApi.deleteUser(accessToken);
        }
    }
}
