package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Locale;
import static io.restassured.RestAssured.given;

@Data
@AllArgsConstructor
public class DataGenerator {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    private static void setUpAll(UserData userData) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(userData) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static UserData statusActive() {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().firstName();
        String password = faker.internet().password();
        setUpAll(new UserData(login, password, "active"));
        return new UserData(login, password, "active");
    }

    public static UserData statusBlocked() {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().firstName();
        String password = faker.internet().password();
        setUpAll(new UserData(login, password, "blocked"));
        return new UserData(login, password, "blocked");
    }

    public static UserData invalidLogin() {
        Faker faker = new Faker(new Locale("en"));
        String password = faker.internet().password();
        String status = "active";
        setUpAll(new UserData("Алена", password, status));
        return new UserData("invalidLogin", password, status);
    }

    public static UserData invalidPassword() {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().firstName();
        String status = "active";
        setUpAll(new UserData(login, "пароль", status));
        return new UserData(login, "invalidPassword", status);
    }
}
