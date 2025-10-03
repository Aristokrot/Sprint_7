import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.*;

public class CourierLoginTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = DataGenerator.getRandomCourier();

        Response createResponse = courierClient.createCourier(courier);
        if (createResponse.statusCode() != 201) {
            throw new RuntimeException("Failed to create courier in setup");
        }

        Response loginResponse = courierClient.loginCourier(courier);
        if (loginResponse.statusCode() != 200) {
            throw new RuntimeException("Failed to login courier in setup");
        }
        courierId = loginResponse.jsonPath().getInt("id");
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            try {
                Response deleteResponse = courierClient.deleteCourier(courierId);
                if (deleteResponse.statusCode() == 200) {
                    System.out.println("Courier successfully deleted in tearDown");
                } else {
                    System.err.println("Failed to delete courier, status: " + deleteResponse.statusCode());
                }
            } catch (Exception e) {
                System.err.println("Exception during courier deletion: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Курьер успешно авторизовался")
    public void loginCourierSuccessfully() {
        Response response = courierClient.loginCourier(courier);

        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", equalTo(courierId)); // Дополнительная проверка что ID совпадает
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неверным паролем")
    public void cannotLoginWithWrongPassword() {
        Courier wrongPasswordCourier = new Courier(courier.getLogin(), "wrong_password_123", null);

        Response response = courierClient.loginCourier(wrongPasswordCourier);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неверным логином")
    public void cannotLoginWithWrongLogin() {
        Courier wrongLoginCourier = new Courier("nonexistent_login_123", courier.getPassword(), null);

        Response response = courierClient.loginCourier(wrongLoginCourier);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без логина")
    public void cannotLoginWithoutLogin() {
        Courier withoutLoginCourier = new Courier(null, courier.getPassword(), null);

        Response response = courierClient.loginCourier(withoutLoginCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без пароля")
    public void cannotLoginWithoutPassword() {
        Courier withoutPasswordCourier = new Courier(courier.getLogin(), null, null);

        Response response = courierClient.loginCourier(withoutPasswordCourier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
