import net.datafaker.Faker;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));

    public static Courier getRandomCourier() {
        String login = faker.name().username() + "_" + faker.random().hex(8);
        String password = faker.internet().password(8, 16, true, true, true);
        String firstName = faker.name().firstName();

        return new Courier(login, password, firstName);
    }

    public static Courier getCourierWithoutLogin() {
        String password = faker.internet().password(8, 16, true, true, true);
        String firstName = faker.name().firstName();
        return new Courier(null, password, firstName);
    }

    public static Courier getCourierWithoutPassword() {
        String login = faker.name().username() + "_" + faker.random().hex(8);
        String firstName = faker.name().firstName();
        return new Courier(login, null, firstName);
    }

    public static String getRandomFirstName() {
        return faker.name().firstName();
    }

    public static String getRandomLastName() {
        return faker.name().lastName();
    }

    public static String getRandomAddress() {
        return faker.address().fullAddress();
    }

    public static String getRandomPhone() {
        return faker.phoneNumber().phoneNumber();
    }

    public static String getFutureDate() {
        return faker.date().future(30, TimeUnit.DAYS).toString();
    }

    public static String getRandomComment() {
        return faker.lorem().sentence(5);
    }
}
