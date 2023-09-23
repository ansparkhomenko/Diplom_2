package core;

import java.util.ArrayList;
import java.util.List;

public class IngredientListHelper {
    // Метод для создания дефолтного списка ингредиентов
    public static List<String> createDefaultIngredientList() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa76");
        ingredients.add("61c0c5a71d1f82001bdaaa71");
        return ingredients;
    }

    // Метод для создания списка ингредиентов с неправильными хешами
    public static List<String> createInvalidHashIngredientList() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add("invalid_hash_1");
        ingredients.add("invalid_hash_2");
        return ingredients;
    }

    // Метод для создания пустого списка ингредиентов
    public static List<String> createEmptyIngredientList() {
        return new ArrayList<>();
    }
}
