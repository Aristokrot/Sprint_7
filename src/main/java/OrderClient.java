import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDER_PATH = "/api/v1/orders";
    private static final String ORDERS_LIST_PATH = "/api/v1/orders";

    @Step("Create order")
    public Response createOrder(Order order) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_PATH);
    }


    @Step("Get orders list")
    public Response getOrdersList() {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .when()
                .get(ORDERS_LIST_PATH);
    }


    @Step("Cancel order")
    public Response cancelOrder(int trackId) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body("{\"track\": " + trackId + "}")
                .when()
                .put(ORDER_PATH + "/cancel");
    }
}
