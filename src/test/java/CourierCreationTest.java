import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class CourierCreationTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;
    private boolean courierCreated = false;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = DataGenerator.getRandomCourier();
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            try {
                courierClient.deleteCourier(courierId);
                System.out.println("Courier deleted with ID: " + courierId);
            } catch (Exception e) {
                System.err.println("Failed to delete courier: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void createCourierSuccessfully() {
        Response response = courierClient.createCourier(courier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));


        Response loginResponse = courierClient.loginCourier(courier);
        loginResponse.then()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = loginResponse.jsonPath().getInt("id");
        courierCreated = true;
    }

    @Test
    @DisplayName("Нельзя создать дубль курьера")
    public void cannotCreateDuplicateCourier() {

        Response firstResponse = courierClient.createCourier(courier);
        firstResponse.then().statusCode(201);

        Response loginResponse = courierClient.loginCourier(courier);
        courierId = loginResponse.jsonPath().getInt("id");
        courierCreated = true;

        Response duplicateResponse = courierClient.createCourier(courier);

        duplicateResponse.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void cannotCreateCourierWithoutLogin() {
        Courier courierWithoutLogin = DataGenerator.getCourierWithoutLogin();

        Response response = courierClient.createCourier(courierWithoutLogin);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));


    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void cannotCreateCourierWithoutPassword() {
        Courier courierWithoutPassword = DataGenerator.getCourierWithoutPassword();

        Response response = courierClient.createCourier(courierWithoutPassword);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));


    }
}
