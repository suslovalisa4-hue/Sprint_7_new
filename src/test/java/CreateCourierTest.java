import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.CourierCheck;
import org.example.CourierClient;
import org.example.CourierEntry;
import org.example.Courier;
import org.junit.After;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertNotEquals;

public class CreateCourierTest {
    private final CourierClient client = new CourierClient();
    private final CourierCheck check = new CourierCheck();
    int courierId;

    @After
    public void deleteCourier() {
        if (courierId != 0) {
            ValidatableResponse response = client.deleteCourier(courierId);
            check.deletedSuccesfully(response);
        }
    }


    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Тест на успешное создание курьера")
    public void testCreateCourierSuccess() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);
        var creds = CourierEntry.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);

        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Обязательность полей: создание курьера без логина")
    @Description("Тест на создание курьера без указания логина")
    public void testCreateCourierNoLogin() {
        Courier requestBody = new Courier();
        requestBody.setPassword("1234");
        requestBody.setFirstName("Test");
        ValidatableResponse createResponse = client.createCourier(requestBody);
        CourierCheck.badRequestCheck(createResponse);
    }

    @Test
    @DisplayName("Обязательность полей: создание курьера без пароля")
    @Description("Тест на создание курьера без указания пароля")
    public void testCreateCourierNoPassword() {
        Courier requestBody = new Courier();
        requestBody.setLogin("ninaaa" + new Random().nextInt(100));
        requestBody.setFirstName("Test");

        ValidatableResponse createResponse = client.createCourier(requestBody);
        CourierCheck.badRequestCheck(createResponse);
    }

    @Test
    @DisplayName("Невозможность создания дубликата курьера")
    @Description("Тест на создание курьера с дублирующимися учетными данными")
    public void testCreateCourierDuplicate() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        ValidatableResponse createResponseDuplicate = client.createCourier(courier);
        check.conflictCheck(createResponseDuplicate);

        var creds = CourierEntry.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);
    }

    @Test
    @DisplayName("Невозможность создание курьера с дублирующимся логином /courier")
    @Description("Тест на создание курьера с дублирующимся логином")
    public void testCreateCourierDuplicateLogin() {
        var courier = Courier.random();
        ValidatableResponse createResponse = client.createCourier(courier);
        check.createdSuccessfully(createResponse);

        courier.setFirstName("Test");
        ValidatableResponse createDuplicateResponse = client.createCourier(courier);
        check.conflictCheck(createDuplicateResponse);

        var creds = CourierEntry.from(courier);
        ValidatableResponse loginResponse = client.loginCourier(creds);
        courierId = check.loggedInSuccessfully(loginResponse);
    }
}
