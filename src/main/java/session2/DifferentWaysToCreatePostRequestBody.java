package session2;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

    /*
   Different approaches for creating request body
	- using Hashmap
	- using org.json
	- using POJO class (Plain Old Java Object)
    - using external json file
    */


public class DifferentWaysToCreatePostRequestBody {

    public static String userId;
    public static String URL = "http://localhost:3000/students";
    public boolean flag = false;

    //    Method-1 using Hashmap
    @Test(enabled = false)
    public void usingHashmap() {
        HashMap payload = new HashMap<>();
        payload.put("name", "Scott");
        payload.put("location", "France");
        payload.put("phone", "9873461991");
        String coursesArr[] = {"C", "C++"};
        payload.put("courses", coursesArr);

        Response response = given().contentType("application/json").body(payload)
                .when().post(URL)
                .then().log().all().statusCode(201)
                .body("name", equalTo("Scott"))
                .body("location", equalTo("France"))
                .body("phone", equalTo("9873461991"))
                .body("courses[0]", equalTo("C"))
                .body("courses[1]", equalTo("C++"))
                .header("Content-Type", "application/json")
                .extract().response();

        JsonPath jp = new JsonPath(response.asString());
        userId = jp.getString("id");
        flag = true;
    }

    //    Method - 2 using org.json library
    @Test(enabled = false)
    public void usingOrgJson() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Scott");
        jsonObject.put("location", "France");
        jsonObject.put("phone", "9873461991");
//        String coursesArr[]={"C", "C++"};
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("C");
        jsonArray.put("C++");
        jsonObject.put("courses", jsonArray);

        Response response = given().contentType("application/json").body(jsonObject.toString())
                .when().post(URL)
                .then().log().all().statusCode(201)
                .body("name", equalTo("Scott"))
                .body("location", equalTo("France"))
                .body("phone", equalTo("9873461991"))
                .body("courses[0]", equalTo("C"))
                .body("courses[1]", equalTo("C++"))
                .header("Content-Type", "application/json")
                .extract().response();

        JsonPath jp = new JsonPath(response.asString());
        userId = jp.getString("id");
        flag = true;
    }


    //    Method - 3 Using POJO Classes
    @Test(enabled = false)
    public void usingPOJO() {

        RequestPayloadPOJO payloadPOJO = new RequestPayloadPOJO();
        payloadPOJO.setName("Scott");
        payloadPOJO.setLocation("France");
        payloadPOJO.setPhone("9873461991");
        List<String> courses = new ArrayList<>();
        courses.add("C");
        courses.add("C++");
        payloadPOJO.setCourses(courses);

        Response response = given().contentType("application/json").body(payloadPOJO)
                .when().post(URL)
                .then().log().all().statusCode(201)
                .body("name", equalTo("Scott"))
                .body("location", equalTo("France"))
                .body("phone", equalTo("9873461991"))
                .body("courses[0]", equalTo("C"))
                .body("courses[1]", equalTo("C++"))
                .header("Content-Type", "application/json")
                .extract().response();

        JsonPath jp = new JsonPath(response.asString());
        userId = jp.getString("id");
        flag = true;
    }


    //    Method - 4 Using External JSON files
    @Test(priority = 0)
    public void usingExternalJSONFiles() throws IOException {


        String jsonFilePath = ".\\requestPayload.json";
//        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        String jsonData = new String(Files.readString(Paths.get(jsonFilePath)));

        Response response = given().contentType("application/json").body(jsonData)
                .when().post(URL)
                .then().log().all().statusCode(201)
                .body("name", equalTo("Scott"))
                .body("location", equalTo("France"))
                .body("phone", equalTo("9873461991"))
                .body("courses[0]", equalTo("C"))
                .body("courses[1]", equalTo("C++"))
                .header("Content-Type", "application/json")
                .extract().response();

        JsonPath jp = new JsonPath(response.asString());
        userId = jp.getString("id");
        flag = true;
    }


    @Test(priority = 1)
    public void deleteUser() {

        if (flag == true) {
            given().log().all().baseUri(URL).pathParam("userID", userId)
                    .when().delete("{userID}")
                    .then().log().all()
                    .statusCode(200)
                    .body("name", equalTo("Scott"));
        }
    }
}