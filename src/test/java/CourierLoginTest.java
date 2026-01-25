import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.example.CourierCheck;
import org.example.CourierClient;
import org.example.CourierEntry;
import org.junit.After;
import org.junit.Test;

public class CourierLoginTest {


    private final CourierCheck check = new CourierCheck();
    private final CourierClient client = new CourierClient();
    int courierId;

    @After
    public void deleteCourier() {
        if (courierId != 0) {
            ValidatableResponse response = client.deleteCourier(courierId);
            check.deletedSuccesfully(response);
        }
    }


    @Test
    @DisplayName("Авторизация с валидными данными")
    @Description("Проверка успешного входа курьера с правильными данными")

    public void testCourierLoginSuccess() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        var creds = CourierEntry.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем ")
    @Description("Проверка сообщения об ошибке при входе с неверным паролем")
    public void testCourierWrongPassword() {

        var creds = new CourierEntry("test", "12345");
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.notFoundLogin(loginResponse);
    }

    @Test
    @DisplayName("Авторизовация под несуществующим пользователем")
    @Description("Проверка сообщения об ошибке при входе с несуществующим ак-ом")

    public void testCourierLoginAccountNotFound() {
        var creds = new CourierEntry("12345");
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.badRequestLogin(loginResponse);
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка сообщения об ошибке при входе без логина")
    public void testCourierLoginNoLogin() {
        var courier = Courier.random();
        var creds = CourierEntry.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        check.notFoundLogin(loginResponse);
    }
}
