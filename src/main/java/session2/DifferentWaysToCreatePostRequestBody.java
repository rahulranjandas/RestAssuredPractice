package session2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
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
    - using Jackson library with an external json file
    */


public class DifferentWaysToCreatePostRequestBody {

    public static String userId;
    public static String location;
    public static String URL = "http://localhost:3000/students";
    public boolean flag = false;

    //    Method-1 using Hashmap
    @Test(enabled = false)
    public void usingHashmap() {
        HashMap<Object, Object> payload = new HashMap<>();
        payload.put("name", "Scott");
        payload.put("location", "France");
        payload.put("phone", "9873461991");
        String[] coursesArr = {"C", "C++"};
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
    @Test(priority = 0, enabled = false)
    public void usingExternalJSONFiles() throws IOException {


        String jsonFilePath = ".\\requestPayload.json";
//        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        String jsonData = Files.readString(Paths.get(jsonFilePath));

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

    //    Method - 5 Using Jackson library and external JSON file without using POJO
    @Test(enabled = false)
    public void readUsingJackson() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("C:\\Users\\RAHUL RANJAN DAS\\OFSA-DIH\\RestAssuredPractice\\requestPayload.json"));
        Response response = given().contentType(ContentType.JSON).body(root)
                .when().post(URL).then().extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        userId = jsonPath.getString("id");
        location = jsonPath.getString("location");
        System.out.println("User Id: " + userId);
        System.out.println("User location: " + location);
        System.out.println("First Course: " + jsonPath.getString("courses[0]"));
        flag = true;
    }

    //    Method - 6 Using Jackson library and external JSON file using POJO
    @Test
    public void readUsingJacksonLib() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        RequestPayloadPOJO jsonData = objectMapper.readValue(new File("C:\\Users\\RAHUL RANJAN DAS\\OFSA-DIH\\RestAssuredPractice\\requestPayload.json"), RequestPayloadPOJO.class);
        Response response = given().contentType(ContentType.JSON).body(jsonData)
                .when().post(URL).then().extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        System.out.println(response.asString());
        userId = jsonPath.getString("id");
        location = jsonPath.getString("location");
        System.out.println("User Id: " + userId);
        System.out.println("User location: " + location);
        System.out.println("First Course: " + jsonPath.getString("courses[0]"));
        System.out.println("Second Course: " + jsonPath.getString("courses[1]"));
        flag = true;
    }

    @Test(priority = 1)
    public void deleteUser() {

        if (flag) {
            given().log().all().baseUri(URL).pathParam("userID", userId)
                    .when().delete("{userID}")
                    .then().log().all()
                    .statusCode(200)
                    .body("name", equalTo("Scott"));
        }
    }
}