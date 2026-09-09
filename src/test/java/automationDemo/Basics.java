package automationDemo;

import files.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static  io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;


public class Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		//		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").
		//		body(Payload.addPlace()).when().post("/maps/api/place/add/json").then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		//		.header("Server", "Apache/2.4.18 (Ubuntu)");


		//add place --> update place -->Verify if the place is added.


		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").body(Payload.addPlace())
				.when().post("/maps/api/place/add/json").then().assertThat().statusCode(200).extract().asString();

		System.out.println(response);

		JsonPath js = new JsonPath(response);
		String placeId = js.getString("place_id");
		System.out.println(placeId);

		String newAddress ="70 Summer walk, USA";
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId).header("Content-Type", "application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}").when().put("/maps/api/place/update/json").then().log().all().assertThat().statusCode(200)
		.body("msg", equalTo("Address successfully updated"));


		String responseFromGet = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
				.when().get("/maps/api/place/get/json").then().log().all().assertThat().statusCode(200).extract().asString();

		JsonPath jsFromGet = ReuseableMethods.rawToJson(responseFromGet);
		String updatedAddress = jsFromGet.getString("address");
		System.out.println(updatedAddress);
		assertEquals(newAddress, updatedAddress);
	}




}
