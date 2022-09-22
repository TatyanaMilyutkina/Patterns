package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.*;

class CardTest {

    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);


        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id = 'date'] input").setValue(firstMeetingDate);
        $x("//input[@name='name']").setValue(validUser.getName());
        $x("//input[@name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(),'Успешно!')]").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id= 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(secondMeetingDate);
        $x("//span[@class='button__text']").click();
        $x("//span[@class='button__text']").click();
        $x("//*[contains(text(),'У вас уже запланирована встреча на другую дату. Перепланировать?')]").shouldBe(Condition.visible);
        $x("//button[@type='button']/span[.='Перепланировать']").click();
        $("[class='notification__content']").shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));

    }
}
