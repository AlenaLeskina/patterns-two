package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import static io.restassured.RestAssured.given;

@UtilityClass
public class DataGenerator {
    private final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    private void userCreationRequest(UserData userData) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(userData) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public UserData statusUser(String status) {
        Faker faker = new Faker(new Locale("en"));
        String login = faker.name().firstName();
        String password = faker.internet().password();
        userCreationRequest(new UserData(login, password, status));
        return new UserData(login, password, status);
    }

    public UserData invalidLoginOrPassword(String lg, String pwd) {
        Faker faker = new Faker(new Locale("en"));
        if(pwd == null){
            String password = faker.internet().password();
            userCreationRequest(new UserData(lg, password, "active"));
            return new UserData("invalidLogin", password, "active");
        }
        else{
            String login = faker.name().firstName();
            userCreationRequest(new UserData(login, pwd, "active"));
            return new UserData(login, "invalidPassword", "active");
        }
    }
}
