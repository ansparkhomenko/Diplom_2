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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class OrderListTest {
    private UserData user;
    private UserServiceApi userServiceApi = new UserServiceApi();
    private OrderServiceApi orderServiceApi = new OrderServiceApi();
    private String accessToken;
    private OrderData orderData;

    @Before
    public void init() {
        user = UserHelper.getDefaultUser();
        userServiceApi.registerUser(user);
        accessToken = userServiceApi.loginUser(user).then().extract().path("accessToken");
        List<String> ingredients = IngredientListHelper.createDefaultIngredientList();
        orderData = new OrderData(ingredients);
        orderServiceApi.createOrderWithAuth(orderData, accessToken);
    }

    @Test
    @DisplayName("Получение заказов не авторизованным пользователем")
    @Description("Неуспешное получение заказов")
    public void getOrderListNoAuth() {
        Response response = orderServiceApi.getUserOrder();
        response.then()
                .log().all()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Получение заказов авторизованным пользователем")
    @Description("Успешное получение заказов")
    public void getOrderListWithAuth() {
        Response response = orderServiceApi.getUserOrderWithAuth(accessToken);
        response.then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userServiceApi.deleteUser(accessToken);
        }
    }
}