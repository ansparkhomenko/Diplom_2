package core;

import ru.yandex.praktikum.order.OrderData;

import java.util.ArrayList;
import java.util.List;

public class OrderHelper {
    public static OrderData createOrderWithIngredients(List<String> ingredients) {
        return new OrderData(ingredients);
    }

    public static OrderData createOrderWithoutIngredients() {
        return new OrderData(new ArrayList<>());
    }

    /**
     * Метод добавляет в список хеши ингредиентов
     *
     * @param ingredientsHash  список хешей
     * @param numOfIngredients кол-во ингредиентов
     * @return список ингредиентов
     */
    public static List<String> addIngredientsToList(List<String> ingredientsHash, int numOfIngredients) {
        List<String> ingredients = new ArrayList<>();

        for (int i = 0; i < numOfIngredients; i++) {
            if (i < ingredientsHash.size()) {
                ingredients.add(ingredientsHash.get(i));
            } else {
                break;
            }
        }
        return ingredients;
    }
}
