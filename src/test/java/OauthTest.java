import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.GetCourse;
import pojo.WebAutomation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;


public class OauthTest {
    @Test
    public void TestCase01() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver96.exe");
        ChromeOptions options = new ChromeOptions();

        WebDriver driver = new ChromeDriver();

        driver.get("https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&auth_url=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fv2%2Fauth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https%3A%2F%2Frahulshettyacademy.com%2FgetCourse.php&flowName=GeneralOAuthFlow");
        driver.findElement(new By.ByCssSelector("input[type='email']")).sendKeys("automation.testing1986@gmail.com");
        driver.findElement(new By.ByCssSelector("input[type='email']")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        driver.findElement(new By.ByCssSelector("input[type='password']")).sendKeys("automationsecret");
        driver.findElement(new By.ByCssSelector("input[type='password']")).sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        String url = driver.getCurrentUrl();
        String partialCode = url.split("code=")[1];
        String code = partialCode.split("&scope")[0];
        System.out.println("CODE => " +code);


        String accessTokenResponse = given().urlEncodingEnabled(false)
                .queryParam("code", code)
                .queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParam("grant_type", "authorization_code")
        .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath jsResponse = new JsonPath(accessTokenResponse);
        System.out.println("ACCESS TOKEN RESPONSE => "+ accessTokenResponse);
        String accessToken = jsResponse.getString("access_token");
        System.out.println(jsResponse.getString("access_token"));
        System.out.println("access_token => "+jsResponse.getString("access_token"));


        GetCourse response = given()
                .queryParam("access_token", accessToken)
                .expect().defaultParser(Parser.JSON)
        .when()
                //.get("https://rahulshettyacademy.com/getCourse.php").asString();
                .get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

        //System.out.println(response);

        /*** Get Linkedln item ***/
        System.out.println(response.getLinkedIn());

        /*** Get Instructor item ***/
        System.out.println(response.getInstructor());

        System.out.println(response.getUrl());
        System.out.println(response.getCourses());
        System.out.println(response.getExpertise());
        System.out.println(response.getService());

        /*** Find determined APÏ course and get the price ***/
        String courseTitleToFind = "SoapUI Webservices testing";
        for (int i = 0; i< response.getCourses().getApi().size() ; i++){
            //System.out.println(response.getCourses().getApi().get(i).getCourseTitle());
            if (response.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase(courseTitleToFind)) {
                String priceCourse = response.getCourses().getApi().get(i).getPrice();
                System.out.println(priceCourse);
                break;
            }
        }

        String[] courseTitles = {
                "Selenium Webdriver Java",
                "Cypress",
                "Protractor"
        };

        /*** Get the course name of Web Automation***/
        ArrayList<String> a = new ArrayList<String>();
        List<WebAutomation> webAutomationList = response.getCourses().getWebAutomation();
        for (int i = 0; i < webAutomationList.size() ; i++ ) {
            a.add(webAutomationList.get(i).getCourseTitle());
        }
        List<String> expectedList = Arrays.asList(courseTitles);

        Assert.assertTrue(a.equals(expectedList));

    }
}
