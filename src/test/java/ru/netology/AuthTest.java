package ru.netology;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.UserData;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    private final SelenideElement login = $("[data-test-id=login] input");
    private final SelenideElement loginOrPasswordInvalid = $(withText("Неверно указан логин или пароль"));
    private final SelenideElement password = $("[data-test-id=password] input");
    private final SelenideElement userBlock = $(withText("Пользователь заблокирован"));
    private final SelenideElement actionButton = $("[data-test-id=action-login]");
    private final ElementsCollection heading = $$(".heading");

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        clearBrowserCookies();
    }

    @Test
    void shouldTestWithStatusActive() {
        UserData userData = DataGenerator.statusActive();
        login.setValue(userData.getLogin());
        password.setValue(userData.getPassword());
        actionButton.click();
        heading.find(text("Личный кабинет")).shouldBe(exist);
    }

    @Test
    void shouldTestWithStatusBlocked() {
        UserData userData = DataGenerator.statusBlocked();
        login.setValue(userData.getLogin());
        password.setValue(userData.getPassword());
        actionButton.click();
        userBlock.shouldBe(visible, Duration.ofMillis(15000));
    }

    @Test
    void shouldTestWithInvalidLogin() {
        UserData userData = DataGenerator.invalidLogin();
        login.setValue(userData.getLogin());
        password.setValue(userData.getPassword());
        actionButton.click();
        loginOrPasswordInvalid.shouldBe(visible, Duration.ofMillis(15000));
    }

    @Test
    void shouldTestWithInvalidPassword() {
        UserData userData = DataGenerator.invalidPassword();
        login.setValue(userData.getLogin());
        password.setValue(userData.getPassword());
        actionButton.click();
        loginOrPasswordInvalid.shouldBe(visible, Duration.ofMillis(15000));
    }
}