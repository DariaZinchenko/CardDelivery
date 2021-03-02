import com.codeborne.selenide.ElementsCollection;
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

    private void setCityByList(String cityName){
        $("[data-test-id='city'] .input__control").setValue(cityName.substring(0, 2));
        ElementsCollection cityElements = $$("[class='menu-item__control']");
        cityElements.find(text(cityName)).click();
    }

    private void setDateByDatePicker(GregorianCalendar calendar){
        String[] s = $(".calendar__name").getOwnText().split(" ");
        int datePickerMonth = (Month.valueOf(s[0].toUpperCase())).ordinal();
        $("[data-test-id='date'] [type='button']").click();

        while(calendar.get(Calendar.MONTH) != datePickerMonth){
            $("[class='calendar__arrow calendar__arrow_direction_right']").click();
            datePickerMonth ++;
        }
        $(byText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)))).click();
    }

    private String getFormatDate(GregorianCalendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(calendar.getTime());
    }

    @Test
    void setTextSubmitFormTest(){
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

        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));

        String text = $("[data-test-id='notification'] .notification__content").getText().replaceAll("\\s\\s*", " ");
        assertEquals("Встреча успешно забронирована на " + date, text);

    }

    @Test
    void setByControlSubmitFormTest(){
        GregorianCalendar date = new GregorianCalendar();
        date.add(Calendar.DATE, 7);

        open("http://localhost:9999");
        setCityByList("Санкт-Петербург");
        setDateByDatePicker(date);
        $("[data-test-id='name'] .input__control").setValue("Иванов Иван");
        $("[data-test-id='phone'] .input__control").setValue("+79200000000");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[role='button']").submit();

        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));

        String text = $("[data-test-id='notification'] .notification__content").getText().replaceAll("\\s\\s*", " ");
        assertEquals("Встреча успешно забронирована на " + getFormatDate(date), text);
    }


}
