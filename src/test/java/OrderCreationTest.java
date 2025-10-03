import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final OrderClient orderClient = new OrderClient();
    private final List<String> color;
    private Integer trackId = null;

    public OrderCreationTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        };
    }

    @Test
    @DisplayName("Создание заказа с различными цветовыми вариантами")
    public void createOrderWithDifferentColors() {
        Order order = new Order(
                DataGenerator.getRandomFirstName(),
                DataGenerator.getRandomLastName(),
                DataGenerator.getRandomAddress(),
                "4",
                DataGenerator.getRandomPhone(),
                3,
                "2024-12-31",
                DataGenerator.getRandomComment(),
                color
        );

        Response response = orderClient.createOrder(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());

        trackId = response.jsonPath().getInt("track");
    }

    @After
    public void tearDown() {
        if (trackId != null) {
            try {
                Response cancelResponse = orderClient.cancelOrder(trackId);
                if (cancelResponse.statusCode() == 200) {
                    System.out.println("Order successfully canceled: " + trackId);
                } else {
                    System.err.println("Failed to cancel order, status: " + cancelResponse.statusCode());
                }
            } catch (Exception e) {
                System.err.println("Exception during order cancellation: " + e.getMessage());
            }
        }
    }
}
