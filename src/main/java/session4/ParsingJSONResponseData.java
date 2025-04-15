package session4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ParsingJSONResponseData {

    @Test(enabled = true, priority = 1)
    void validateJSONResponse() {

//        Approach - 1 for smaller JSON objects in response; without capturing the response
        given()
                .contentType(ContentType.JSON)
                .pathParam("path", "books")
                .when()
                .get("http://localhost:3000/{path}")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("[3].title", equalTo("The Lord of the Rings"));


//        Approach - 2 using Response interface; by capturing the response and converting to String
        Response response = given()
                .contentType(ContentType.JSON)
                .pathParam("path", "books")
                .when()
                .get("http://localhost:3000/{path}")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(jsonPath.getString("[3].title"), "The Lord of the Rings");

    }

    @Test(enabled = true, priority = 2)
    void validateJSONResponseData() {

//        Approach - 3 Using JSON Object Class
        Response response = given()
                .contentType(ContentType.JSON)
                .pathParam("path", "books")
                .when()
                .get("http://localhost:3000/{path}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        JsonPath jsonPath = new JsonPath(response.asString());
        List<String> titles = jsonPath.getList("title");

//        Approach-1 for filtering the titles and matching the pattern; using lambda functions
        AtomicBoolean flag = new AtomicBoolean(false);
        titles.forEach(title -> {
            if (title.equalsIgnoreCase("The Lord of the Rings")) {
                System.out.println("Title Found:- " + title);
                flag.set(true);
            }
        });

//        Approach-2 for filtering the titles and matching the pattern using foreach loop
        for (String title : titles) {
            if (title.equalsIgnoreCase("The Lord of the Rings")) {
                System.out.println("Title found: " + title);
                break;
            }
        }

//        Approach - 3 using streams
        titles.stream()
                .filter(title -> title.equalsIgnoreCase("The Lord of the Rings"))
                .findFirst()
                .ifPresent(title -> System.out.println("Title Found: " + title));


//        Approach - 4 using JSONArray
        boolean status = false;
        JSONArray jsonArray = new JSONArray(response.asString());
        for (int i = 0; i < jsonArray.length(); i++) {
            String bookTitle = jsonArray.getJSONObject(i).getString("title").toString();
            if (bookTitle.equalsIgnoreCase("The Lord of the Rings")) {
                status = true;
                break;
            }
        }
        Assert.assertTrue(status);


//        2nd validation - Find the total price of all the books
//        Approach-1
        List<Object> priceList = jsonPath.getList("price");
        Float totalPrice = 0F;
        for (Object itemPrice : priceList) {
            totalPrice += Float.parseFloat(itemPrice.toString());
        }

        Assert.assertEquals(totalPrice, 526F);


//        Approach-2
        totalPrice = 0F;
        for (int i = 0; i < jsonArray.length(); i++) {
            Float price = jsonArray.getJSONObject(i).getFloat("price");
            totalPrice += price;
        }

        Assert.assertEquals(totalPrice, 526F);
    }

    @Test(priority = 3)
    public void validateResponseJackson() throws JsonProcessingException {
        //        Approach - 3 Using Jackson library to validate response and find title
        Response response = given()
                .contentType(ContentType.JSON)
                .pathParam("path", "books")
                .when()
                .get("http://localhost:3000/{path}")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = response.asString();
        List<Books> responsePayloadPOJOS = mapper.readValue(jsonResponse, new TypeReference<List<Books>>() {
        });

//        Find the title - Approach 1
        responsePayloadPOJOS.forEach(books -> {
            if (books.getTitle().equalsIgnoreCase("The Lord of the Rings")) {
                System.out.println("Title found=" + books.getTitle());
            }
        });

//        Find the title - Approach 2
        for (Books responsePayloadPOJO : responsePayloadPOJOS) {
            if (responsePayloadPOJO.getTitle().equalsIgnoreCase("The Lord of the Rings")) {
                System.out.println("Title found=" + responsePayloadPOJO.getTitle());
                break;
            }
        }

//        Find the total price of all the books
        double totalPrice= responsePayloadPOJOS.stream().mapToDouble(Books::getPrice).sum();
        Assert.assertEquals(totalPrice,526F);

    }
}
