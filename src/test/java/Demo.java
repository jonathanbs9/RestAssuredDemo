import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Demo {
    @Test
    public void testGetUsers(){
       baseURI = "https://reqres.in/api";
       String body = given()
               .when()
                    .get("/users")
               .then()
                    .log().all()
                    .statusCode(200)
                    .extract().body().asString();

       System.out.println("TestGetUsers");
    }

    @Test
    public void validateUser(){
        baseURI = "https://reqres.in/api";

        String body = given()
                .when()
                .get("/users")
                .then()
                    .log().all()
                    .statusCode(200)
                .body("data[1].first_name", equalTo("Janet"))
                        .extract().body().asString();

        System.out.println("validateUser");
    }

    @Test
    public void testPOSTUser(){
        baseURI = "https://reqres.in/api";

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Jonathan");
        map.put("job", "Developer");



        given()
                .log().all()
                .body(map.toString())
                .header("Accept","application/json")
                .when()
                .post("/users")
                .then()
                //.log().all()
                .statusCode(201);

    }

}