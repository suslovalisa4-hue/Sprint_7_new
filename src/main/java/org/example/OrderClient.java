package org.example;


import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;


public class OrderClient {

    @Step("Разместить заказ")
    public static Response postOrder(Order order) {
        return given(BaseClient.getRequestSpecification())
                .contentType(ContentType.JSON)
                .and()
                .body(order)
                .when()
                .post(BaseClient.ORDERS);
    }

    @Step("Проверить статус ответа")
    public static void validateResponseStatus(Response response, int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Step("Получить доступные заказы")
    public static Response getAvailableOrders(int limit, int page) {
        return given(BaseClient.getRequestSpecification())
                .contentType(ContentType.JSON)
                .queryParam("limit", limit)
                .queryParam("page", page)
                .when()
                .get(BaseClient.ORDERS);

    }

    @Step("Проверить, список заказов пришел, если не передали id курьера")
    public static Response getOrdersWithoutCourierId() {
        return given(BaseClient.getRequestSpecification())
                .contentType(ContentType.JSON)
                .when()
                .get(BaseClient.ORDERS);
    }

    @Step("Проверить, что заказы вернулись")
    public static void responseIsSuccess(Response response) {
        response.then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("size()", greaterThan(0));
    }

    @Step("Получить ID заказа")
    public static int getOrderId(Response response) {
        return response
                .then().log().all()
                .extract()
                .path("track");
    }

    @Step("Проверить, что вернулось менее 10 заказов или 10")
    public static void bodySizeIsMoreThen10(Response response) {
        response.then()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("size()", lessThanOrEqualTo(10));
    }

}