package automationDemo;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReuseableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
public class DynamicJson {

		
		
		@Test(dataProvider="BooksData")
		public void addBook(String isbn, String aisle) {
			
			//add book
			RestAssured.baseURI = "http://216.10.245.166";
			String response = given().log().all().header("Content-Type", "application/json").body(Payload.addBook(isbn,aisle)).when().post("/Library/Addbook.php")
			.then().assertThat().statusCode(200).extract().response().asString();
			
			JsonPath js = ReuseableMethods.rawToJson(response);
			String id = js.getString("ID");
			System.out.println(id);
			
			//delete book;
			
			given().log().all().header("Content-Type", "application/json").body(Payload.deleteBook(id))
			.when().post("/Library/DeleteBook.php")
			.then().assertThat().statusCode(200);
	}
		
		@DataProvider(name="BooksData")
		public Object[][] getData() {
			return new Object[][] {{"AFAF","14464"},{"AFASE","46461"},{"ASFSDTG","46464"}};
		}
}
