package session3;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PathAndQueryParameters {

//    Sample URL - https://reqres.in/api/users?page=2&id=5

    @Test
    public void testPathAndQueryParams() {

        Response response = given()
                .pathParam("pathParam", "users")
                .queryParam("page", 2)
                .queryParam("id", 5)
                .when()
                .get("https://reqres.in/api/{pathParam}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(jsonPath.getString("data.first_name"), "Charles");
        Assert.assertEquals(jsonPath.getString("data.last_name"), "Morris");
    }
}
