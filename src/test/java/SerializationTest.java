import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import pojo.AddPlace;
import pojo.Location;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializationTest {
    @Test
    public void TestCaseSerialization1(){
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        AddPlace addPlace = new AddPlace();
        addPlace.setAccuracy(50);
        addPlace.setAddress("Calle Falsa 123");
        addPlace.setLanguage("AR-Spanish");
        addPlace.setPhoneNumber("+54 9 223 4885565");
        addPlace.setWebsite("http://www.avalith.net");
        addPlace.setName("Front Line Department");

        ArrayList<String> myList = new ArrayList<String>();
        myList.add("Shoe Park"); myList.add("Shopping");

        addPlace.setTypes(myList);

        Location location = new Location();
        location.setLat(-37.0600194);
        location.setLng(-81.6070372);

        addPlace.setLocation(location);

        RequestSpecification request = new RequestSpecBuilder()
                                        .setBaseUri("https://rahulshettyacademy.com")
                                        .addQueryParam("key", "qaclick123")
                                        .setContentType(ContentType.JSON).build();

        ResponseSpecification responseSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
        RequestSpecification res  =
                given().spec(request)
                        .body(addPlace);

        Response response = res.when()
                .post("/maps/api/place/add/json")
        .then()
                .assertThat().statusCode(200).extract().response();

        String responseString = response.asString();
        System.out.println(responseString);
    }
}
