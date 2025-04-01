package session3;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LoggingDemo {

    @Test
    public void validateLogs() {

        given()
                .pathParam("pathParam", "users")
                .queryParam("page", "2")
                .and()
                .queryParam("id", 5)
                .when()
                .get("https://reqres.in/api/{pathParam}")
                .then()
                .log()
                .headers();
//                .body();
//                .cookies();
    }
}
