import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderListTest {
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("Успешное получение списка заказов")
    public void getOrdersListSuccessfully() {
        Response response = orderClient.getOrdersList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(List.class))
                .body("orders.size()", greaterThanOrEqualTo(0));
    }
}
