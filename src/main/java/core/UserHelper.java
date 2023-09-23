package core;

import ru.yandex.praktikum.user.UserData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserHelper {
    public static String generateUniqueEmail() {
        // Генерируем уникальный email на основе текущей даты и времени
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date currentDate = new Date();
        String email = "user_" + dateFormat.format(currentDate) + "@yandex.ru";
        return email;
    }

    public static UserData getDefaultUser() {
        return new UserData("test-data@yandex.ru", "password", "Username");
    }
}
