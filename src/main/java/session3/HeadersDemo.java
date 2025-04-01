package session3;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class HeadersDemo {

    @Test
    public void validateHeaders() {

        given()
                .when()
                .get("https://google.com")
                .then()
                .header("Content-Encoding", "gzip")
                .and()
                .header("Content-Type", "text/html; charset=ISO-8859-1")
                .and()
                .header("Server", "gws");


    }

    @Test(priority = 1)
    public void getHeaders() {

        Response response = given()
                .when().get("https://google.com")
                .then().extract().response();

//        Get Single header
        System.out.println("Extracting single header");
        System.out.println(response.getHeader("Content-Type"));

//        Get Multiple Headers
        System.out.println("Extracting multiple headers");
        Headers headers = response.getHeaders();
        for (Header header : headers) {
            System.out.println(header.getName() + "=" + header.getValue());
        }

//        Get All headers
        System.out.println("*******$$$$$$$$$$$$$$$*******");
        response.then().log().headers();
    }
}
