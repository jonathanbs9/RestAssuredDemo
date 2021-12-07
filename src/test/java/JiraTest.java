import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class JiraTest {
    @Test
    public void LoginCreateACommentAndUploadAttachment(){
        baseURI = "http://localhost:8080/";

        /*** Login (session) ***/
        SessionFilter session = new SessionFilter();
        String response = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\" : \"jonathan.brull.schroeder\",\n" +
                        "    \"password\" : \"rivercapo9\"\n" +
                        "}").log().all()
                .filter(session)
        .when()
                .post("/rest/auth/1/session")
        .then().log().all()
                .extract().response().asString();

        /*** Post Comment ***/
        String expectedMessage = "Comment with automation.";
        String addCommentResponse = given()
                .pathParam("issueId", 10000).log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \""+expectedMessage+"\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}")
                .filter(session)
        .when()
                .post("/rest/api/2/issue/{issueId}/comment")
        .then().log().all()
                .assertThat().statusCode(201).extract().response().asString();

        JsonPath jsComment = new JsonPath(addCommentResponse);
        String commentId = jsComment.getString("id");

        /*** Add attachment ***/
        given()
                .header("X-Atlassian-Token", "no-check")
                .header("Content-Type", "multipart/form-data")
                .filter(session)
                .pathParam("issueId", 10000).log().all()
                .multiPart("file", new File("src/test/java/files/attachment_test.txt"))
        .when()
                .post("/rest/api/2/issue/{issueId}/attachments")
        .then().log().all()
                .assertThat().statusCode(200);

        /*** Get issue ***/
        String issueDetails = given()
                .filter(session)
                .pathParam("issueId", 10000)
                .queryParam("fields","comment")
                .log().all()
        .when()
                .get("/rest/api/2/issue/{issueId}")
        .then().log().all()
                .extract().response().asString();

        System.out.println(issueDetails);

        JsonPath jsonPath = new JsonPath(issueDetails);
        int commentsCount = jsonPath.getInt("fields.comment.comments.size()");
        for  (int i=0; i < commentsCount; i++){
            String commentIssueId = jsonPath.getString("fields.comment.comments["+i+"].id");
            if (commentIssueId.equalsIgnoreCase(commentId)){
                String message = jsonPath.getString("fields.comment.comments["+i+"].body");
                Assert.assertEquals(message,expectedMessage);
                break;
            }
        }

    }
}
