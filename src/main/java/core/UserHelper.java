package core;

import com.github.javafaker.Faker;
import ru.yandex.praktikum.user.UserData;

public class UserHelper {

    private static Faker faker = new Faker();
    private static String email = faker.internet().emailAddress();
    private static String name = faker.name().firstName();
    private static String password = faker.internet().password();


    public static UserData getUniqueUser() {
        return new UserData(email, password, name);
    }
}
