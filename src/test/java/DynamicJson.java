import files.Payload;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {

    @Test(dataProvider = "BooksData")
    public void AddBook(String aisle, String isbn)
    {
        RestAssured.baseURI = "http://216.10.245.166";

        String response = given()
                .header("Content-Type", "application/json")
                .body(Payload.AddBook(aisle, isbn))
        .when()
                .post("/Library/Addbook.php")
        .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath jsonPath = ReusableMethods.rawToJson(response);
        String id = jsonPath.get("ID");
        System.out.println(id);

        // DeleteBook

    }
    @DataProvider(name = "BooksData")
    public Object[][] getData(){
        // Collection of elements
        return new Object[][]{
                {"abc1", "1234"},
                {"def","4567"},
                {"ghij","789"}
        };
    }
}
