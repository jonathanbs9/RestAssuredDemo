import files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ComplexJsonParse {
    public  void  TestCase1(){
        JsonPath jsonPath = new JsonPath(Payload.CoursePrice());

    // Print the number of courses
        int count = jsonPath.getInt("courses.size()");
        System.out.println(count);
        System.out.println(jsonPath.getString(""));


    // Print Purchase amount
        //System.out.println(jsonPath.getString("dashboard.purchaseAmount"));
        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        System.out.println(purchaseAmount);

    // Print Title of the first course
        //System.out.println(jsonPath.getString("courses[0].title"));
        String firstCourseTitle = jsonPath.get("courses[0].title");
        System.out.println(firstCourseTitle);

    // Print All course title and their respective prices
        for (int i=0; i<count; i++){
            String courseTitle = jsonPath.getString("courses["+i+"].title");
            String coursePrice = jsonPath.getString("courses["+i+"].price");
            System.out.print(courseTitle+" ");
            System.out.println(coursePrice);
        }

    // Print number of copies sold by RPA Course
        String courseCopies = "";
        for (int i=0; i<count; i++){
            String courseTitle = jsonPath.getString("courses["+i+"].title");
            if (courseTitle.equals("RPA")){
                courseCopies = jsonPath.getString("courses["+i+"].copies");
                break;
            }

        }
        System.out.println(courseCopies);

    // Verify if sum of all course prices matches with purchase
        int purchaseAmountPrice = jsonPath.getInt("dashboard.purchaseAmount");
        int totalAmout = 0;
        for (int i=0; i<count; i++) {
            int coursePrice = jsonPath.getInt("courses["+i+"].price");
            int courseCopy = jsonPath.getInt("courses["+i+"].copies");
            totalAmout += courseCopy * coursePrice;
        }
        System.out.println(totalAmout+" == "+ purchaseAmountPrice);

        Assert.assertEquals(totalAmout,purchaseAmountPrice );

    }

}
