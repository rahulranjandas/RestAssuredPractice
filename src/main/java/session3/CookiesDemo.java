package session3;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class CookiesDemo {

    @Test(enabled = false)
    public void validateCookies() {

//        This test case would fail as the cookies have been hardcoded
        given()
                .when()
                .get("https://google.com")
                .then()
                .log().all()
                .cookie("AEC", "AVcja2eoeqclvEASepd2WRnwVnC-XYftzmHivqa-pRzv2cb0d74hajFxZTA");
    }

    @Test
    public void extractCookies() {

        Response response = given()
                .when()
                .get("https://google.com")
                .then()
                .extract()
                .response();

//        Extract Single Cookie
        String AECCookie = response.getCookie("AEC");
        String NIDCookie = response.getCookie("NID");

        System.out.println(AECCookie);
        System.out.println(NIDCookie);
        Assert.assertNotNull(AECCookie, "Value not present");
        Assert.assertNotNull(NIDCookie, "Value not present");

//        Extract multiple cookies - from Response object
        Map<String, String> responseCookies = response.getCookies();
        for (String cookie : responseCookies.keySet()) {
            System.out.println(cookie + "=" + response.getCookie(cookie));
        }

//        Extract multiple cookies - from the Map
        for (String key : responseCookies.keySet()) {
            System.out.println(key + "=" + responseCookies.get(key));
        }
    }
}
