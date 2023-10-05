package ru.yandex.praktikum.order;

import core.OrderHelper;
import core.UserHelper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.user.UserData;
import ru.yandex.praktikum.user.UserServiceApi;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Response response;

    private static final String INVALID_HASH = "invalidHash";
    private static final int NUM_OF_INGREDIENTS_IN_LIST = 4;


    @Before
    public void init() {
        user = UserHelper.getUniqueUser();
        userServiceApi.registerUser(user);
        accessToken = userServiceApi.loginUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и корректными хешами ингредиентов")
    @Description("Успешное создание заказа")
    public void createOrderWithValidIngredients() {
        response = orderServiceApi.getIngredients().then().extract().response();
        JsonPath jsonPath = response.jsonPath();
        List<String> ingredientsHash = jsonPath.get("data._id");

        ingredients = OrderHelper.addIngredientsToList(ingredientsHash, NUM_OF_INGREDIENTS_IN_LIST);
        orderData = OrderHelper.createOrderWithIngredients(ingredients);

        response = orderServiceApi.createOrderWithAuth(orderData, accessToken);
        response.then()
                .log().all()
                .statusCode(200)
                .body("order.number", greaterThan(0))
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с невалидными хешами ингредиентов")
    @Description("Неуспешное создание заказа")
    public void createOrderWithInvalidIngredients() {
        ingredients = new ArrayList<>(Arrays.asList(INVALID_HASH, INVALID_HASH));
        orderData = OrderHelper.createOrderWithIngredients(ingredients);
        response = orderServiceApi.createOrderWithAuth(orderData, accessToken);
        response.then()
                .log().all()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с пустым списком ингредиентов")
    @Description("Неуспешное создание заказа")
    public void createOrderWithNoIngredients() {
        orderData = OrderHelper.createOrderWithoutIngredients();
        response = orderServiceApi.createOrderWithAuth(orderData, accessToken);
        response.then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @DisplayName("Создание заказа без авторизаци и корректными хешами ингредиентов")
    @Description("Успешное создание заказа")
    public void createOrderNoAuthWithValidIngredients() {
        response = orderServiceApi.getIngredients().then().extract().response();
        JsonPath jsonPath = response.jsonPath();
        List<String> ingredientsHash = jsonPath.get("data._id");

        ingredients = OrderHelper.addIngredientsToList(ingredientsHash, NUM_OF_INGREDIENTS_IN_LIST);
        orderData = OrderHelper.createOrderWithIngredients(ingredients);

        response = orderServiceApi.createOrder(orderData);
        response.then()
                .log().all()
                .statusCode(200)
                .body("order.number", greaterThan(0))
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с невалидными хешами ингредиентов")
    @Description("Неуспешное создание заказа")
    public void createOrderNoWithInvalidIngredients() {
        ingredients = new ArrayList<>(Arrays.asList(INVALID_HASH, INVALID_HASH));
        orderData = OrderHelper.createOrderWithIngredients(ingredients);
        response = orderServiceApi.createOrder(orderData);
        response.then()
                .log().all()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и с пустым списком ингредиентов")
    @Description("Неуспешное создание заказа")
    public void createOrderNoAuthWithNoIngredients() {
        orderData = OrderHelper.createOrderWithoutIngredients();
        response = orderServiceApi.createOrder(orderData);
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
