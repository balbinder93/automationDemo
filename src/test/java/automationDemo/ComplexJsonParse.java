package automationDemo;

import static org.testng.Assert.assertEquals;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	public static void main(String[] args) {
		
		JsonPath js= new JsonPath(Payload.coursePrice());
		//1. Print No of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println(count);
		
		//2.Print Purchase Amount
		
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		//3. Print Title of the first course
		
		String titleOfFirstCourse = js.getString("courses[0].title");
		System.out.println(titleOfFirstCourse);
		
		//Print All course titles and their respective Prices
		
		for(int i=0;i<count;i++) {
			System.out.println(js.getString("courses["+i+"].title"));
			System.out.println(js.getInt("courses["+i+"].price"));
		}
		
		//Print no of copies sold by RPA Course
		
		String copiesOfRpaSold = js.getString("courses[2].copies");
		System.out.println(copiesOfRpaSold);
		
		//Verify if Sum of all Course prices matches with Purchase Amount
		int sum =0;
		for(int i=0;i<count;i++) {
			
			sum +=js.getInt("courses["+i+"].price")*js.getInt("courses["+i+"].copies");
		}
		assertEquals(sum, purchaseAmount);
	}

}
