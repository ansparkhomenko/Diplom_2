package ru.yandex.praktikum.order;


import java.util.List;

public class OrderData {
    private List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderData(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderData() {
    }
}

