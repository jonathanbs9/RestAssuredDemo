package files;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

@Test
public class ReusableMethods {

    public static JsonPath rawToJson(String response){
        JsonPath js = new JsonPath(response);
        return js;
    }
}
