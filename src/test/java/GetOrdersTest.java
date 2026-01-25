import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.example.*;
import org.junit.After;
import org.junit.Test;

public class GetOrdersTest {
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
    @DisplayName("Получение списка заказов /orders")
    @Description("Позитивный тест получения заказов /orders")
    public void testGetOrders() {
        Response response = OrderClient.getOrdersWithoutCourierId();
        OrderClient.responseIsSuccess(response);
        System.out.println(response.body().asString());
    }

    @Test
    @DisplayName("Получение доступных заказов")
    @Description("Позитвный тест получения доступных заказов")
    public void testGetAvailableOrders() {
        Response response = OrderClient.getAvailableOrders(10, 0);
        OrderClient.bodySizeIsMoreThen10(response);
        System.out.println(response.body().asString());
    }

}