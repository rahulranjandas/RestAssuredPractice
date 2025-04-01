package session1;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HTTPRequests {

    public static int userId;
    public static String URL = "https://reqres.in/api/users/";

    @Test(priority = 1, enabled = true)
    public void getUsers() {

        Integer pageNo = 2;
        given().baseUri(URL)
                .queryParam("page", 2)
                .when().get()
                .then().statusCode(200).body("page", equalTo(2)).log().all();
    }

    @Test(priority = 2)
    public void createUser() {

        HashMap<String, String> payload = new HashMap<>();
        payload.put("name", "rahul ranjan das");
        payload.put("job", "qa");

        Response response = given().baseUri(URL).contentType("application/json").body(payload)
                .when().post()
//                .jsonPath().getInt("id")
                .then().log().all().statusCode(201)
                .body("name", equalTo(payload.get("name")))
                .body("job", equalTo(payload.get("job")))
                .extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        userId = jsonPath.getInt("id");
        System.out.println(userId);
    }

    @Test(priority = 3, dependsOnMethods = {"createUser"})
    public void updateUser() {

        HashMap<String, String> payload = new HashMap<>();
        payload.put("name", "rahul ranjan das");
        payload.put("job", "test architect");

        given().baseUri(URL).contentType("application/json").body(payload)
                .pathParam("userId", userId)
                .when().put("{userId}")
                .then().statusCode(200)
                .body("name", equalTo(payload.get("name")))
                .body("job", equalTo(payload.get("job")))
                .log().all();
    }

    @Test(priority = 4, dependsOnMethods = {"createUser"})
    public void deleteUser() {

        given().baseUri(URL).pathParam("userId", userId)
                .when().delete("{userId}")
                .then().statusCode(204)
                .log().all();
    }

}
