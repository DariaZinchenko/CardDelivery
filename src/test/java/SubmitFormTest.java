import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.ElementsContainer;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubmitFormTest {

    private void setCityByList(String city){
        $("[data-test-id='city'] .input__control").setValue(city.substring(0, 2));
        SelenideElement parentDiv = $("[class='popup popup_direction_bottom-left popup_target_anchor popup_size_m popup_visible popup_height_adaptive popup_theme_alfa-on-white input__popup']");
        parentDiv.find(withText(city)).parent().click();
    }

    private void setDateByDatePicker(GregorianCalendar calendar){
        GregorianCalendar now = new GregorianCalendar();

        $("[data-test-id='date'] [type='button']").click();
        if(calendar.get(Calendar.MONTH) != now.get(Calendar.MONTH)){
            $("[class='calendar__arrow calendar__arrow_direction_right']").click();
        }
        $(byText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)))).click();
    }

    private String getFormatDate(GregorianCalendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(calendar.getTime());
    }

    @Test
    void SetTextSubmitFormTest(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 3);
        String date = getFormatDate(calendar);

        open("http://localhost:9999");
        $("[data-test-id='city'] .input__control").setValue("Санкт-Петербург");
        $("[data-test-id='date'] .input__control").setValue(date);
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79200000000");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[role='button']").submit();
        SelenideElement element = $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));

        String text = element.getText().replaceAll("\\s\\s*", " ");
        assertEquals("Успешно! Встреча успешно забронирована на " + date, text);

    }

    @Test
    void SetByControlSubmitFormTest(){
        GregorianCalendar date = new GregorianCalendar();
        date.add(Calendar.DATE, 7);

        open("http://localhost:9999");
        setCityByList("Санкт-Петербург");
        setDateByDatePicker(date);
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79200000000");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[role='button']").submit();
        SelenideElement element1 = $("[data-test-id='notification']") .shouldBe(visible, Duration.ofSeconds(15));

        String text = element1.getText().replaceAll("\\s\\s*", " ");
        assertEquals("Успешно! Встреча успешно забронирована на " + getFormatDate(date), text);
    }


}