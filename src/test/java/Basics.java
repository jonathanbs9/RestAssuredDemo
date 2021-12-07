import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    @Test
    public static void main(String[] args) throws IOException {
        /* 1. ADD place
           2. UPDATE place with new address
           3. GET place to validate if new Address is present
         */

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String jsonString = new String(Files.readAllBytes(Paths.get("D:\\Proyectos\\udemy\\RestAssuredDemo\\src\\test\\java\\files\\addPlace.json")));

        String response = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(jsonString)
        .when()
                .post("maps/api/place/add/json")
        .then()
                .assertThat().statusCode(200)
                .body("scope",equalTo("APP"))
                .body("status", equalTo("OK"))
                .header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();

        JsonPath jsonPath = new JsonPath(response);

        // GET placeId
        String placeId = jsonPath.getString("place_id");
        //System.out.println(placeId);

        // UPDATE Place
        String newAddress = "70 winter walk, USA";
        given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
        .when()
                .put("maps/api/place/update/json")
        .then()
                .assertThat().log().all().statusCode(200)
                .body("msg", equalTo("Address successfully updated"));

        // GET Place
        String getPlaceResponse = given().log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
        .when()
                .get("maps/api/place/get/json")
        .then()
                .assertThat().log().all()
                .statusCode(200).extract().response().asString();
        JsonPath js = ReusableMethods.rawToJson(getPlaceResponse);
        //JsonPath js = new JsonPath(getPlaceResponse);
        String actualAddress = js.getString("address");
        //System.out.println(actualAddress);
        Assert.assertEquals(actualAddress, newAddress);


    }

}
