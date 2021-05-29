package automationDemo;

import static io.restassured.RestAssured.*;

import java.sql.DriverAction;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.API;
import pojo.GetCourse;
import pojo.WebAutomation;

public class Oauth {

	public static void main(String[] args) {
		
		System.setProperty("webdriver.chrome.driver", "F:\\chromedriver.exe");
		WebDriver driver =new ChromeDriver();
		driver.get("");
		
		String tokenResponse = given().queryParams("code", "").urlEncodingEnabled(false)
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("clent_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js = new JsonPath(tokenResponse);
		String accessToken = js.getString("access_token");
		
		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		
		//System.out.println(gc.getLinkedin());
		//System.out.println(gc.getInstructor());
		
		//List<API> apiCourses = gc.getCourses().getApi();
//		for (API api : apiCourses) {
//			
//			if(api.getCourseTitle().equalsIgnoreCase("SoapUI Webservices Testing")) {
//				api.getPrice();
//			}
//			
//		}
//		List<WebAutomation> webCourses = gc.getCourses().getWebAutomation();http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=2427135
//		for (WebAutomation webAutomation : webCourses) {
//			
//			System.out.println(webAutomation.getCourseTitle());
//			
//		}
	}

}
