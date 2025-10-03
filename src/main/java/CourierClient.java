import io.qameta.allure.Step;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";


    @Step("Create courier")
    public Response createCourier(Courier courier) {
        return given().log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }


    @Step("Login courier")
    public Response loginCourier(Courier courier) {
        return given().log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(LOGIN_PATH);
    }


    @Step("Delete courier")
    public Response deleteCourier(int courierId) {
        return given().log().all()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }
}
