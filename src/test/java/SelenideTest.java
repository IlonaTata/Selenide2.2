import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.selector.ByText;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class SelenideTest {
    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }
    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldDeliveryCard() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date=generateDate(3,"dd.MM.yyyy");
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79023216547");
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);

    }

    @Test
    void shouldDeliveryCardWithNotExistCity() {
        $("[data-test-id='city'] input").setValue("Молоко");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date = generateDate(3,"dd.MM.yyyy");
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79023216547");
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id='city'].input_invalid").shouldHave(Condition.text("Доставка в выбранный город недоступна"), Duration.ofSeconds(15));

    }

    @Test
    void shouldDeliveryCardWithWrongDate() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue("01.01.2000");
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79023216547");
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id='date'] .input_invalid").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));

    }

    @Test
    void shouldDeliveryCardWithWrongName() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date = generateDate(3,"dd.MM.yyyy");
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Ivanov Ivan");
        $("[data-test-id='phone'] input").setValue("+79023216547");
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id='name'].input_invalid").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));

    }

    @Test
    void shouldDeliveryCardWhereNameWithOneLetter() {
        $("[data-test-id='city'] input").setValue("Казань");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date = generateDate(3,"dd.MM.yyyy");
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("И");
        $("[data-test-id='phone'] input").setValue("+79023216547");
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
      //  $("[data-test-id='notification']").should(Condition.visible, Duration.ofSeconds(15));
      //  $(byText("Встреча успешно забронирована на" + date));
    }

    @Test
    void shouldDeliveryCardWithWrongPhone() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date = generateDate(3,"dd.MM.yyyy");
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("89023216547");
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
        $("[data-test-id='phone'].input_invalid").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), Duration.ofSeconds(15));

    }

    @Test
    void shouldDeliveryCardWithChoiceCity() {
        $("[data-test-id='city'] input").setValue("Мо");
        $$(".menu-item__control").find(exactText("Кемерово")).click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        String date = generateDate(3,"dd.MM.yyyy");
        $("[data-test-id='date'] input").setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79023216547");
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
      //  $("[data-test-id='notification']").should(Condition.visible, Duration.ofSeconds(15));
      //  $(byText("Встреча успешно забронирована на" + date));
    }

    @Test
    void shouldDeliveryCardWithChoiceDate() {
        $("[data-test-id='city'] input").setValue("Мо");
        $$(".menu-item__control").find(exactText("Кемерово")).click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfDelivery = LocalDate.now().plusDays(7);
        String day = dateOfDelivery.format(DateTimeFormatter.ofPattern("d"));
        $(".input__icon").click();
        if (dateOfDelivery.getMonthValue() - currentDate.getMonthValue() == 1) {
            $("[data-step='1']").click();
       }
        $$("td.calendar__day").find(exactText(day)).click();

        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79023216547");
        $("[data-test-id='agreement']").click();
        $x("//*[@class='button__content']").click();
       $("[data-test-id='notification']").should(Condition.visible, Duration.ofSeconds(15));
       $(byText("Встреча успешно забронирована на" + dateOfDelivery));
    }
}
