package org.example;


import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

public class CourierClient {

    @Step("Вход курьера")
    public ValidatableResponse loginCourier(CourierEntry creds) {
        return BaseClient.getRequestSpecification()
                .body(creds)
                .when().log().all()
                .post(BaseClient.COURIER_LOGIN)
                .then().log().all();
    }

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return BaseClient.getRequestSpecification()
                .and()
                .body(courier)
                .when().log().all()
                .post(BaseClient.COURIER)
                .then().log().all();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return BaseClient.getRequestSpecification()
                .body(Map.of("id", id))
                .when().log().all()
                .delete(BaseClient.COURIER + "/" + id)
                .then().log().all();
    }
}
