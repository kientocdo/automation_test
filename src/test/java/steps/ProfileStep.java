package steps;

import constants.ConstantApi;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import pages.ProfilePage;
import io.restassured.path.json.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.connection.mySQLQueryMulti;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ProfileStep {
    private static final Logger logger = LoggerFactory.getLogger(ProfileStep.class);
    private ProfilePage profilePage;
    private Response response;
    private RequestSpecification request;
    private int userId;

    private String expectedUsername;
    private String expectedDateOfBirth;
    private String expectedGender;
    private boolean expectedSubscribedMarketing;
    private String generatedUsername;

    @Given("the user sends a invalid POST request to create a profile from feature")
    public void the_user_sends_a_invalid_post_request_to_create_a_profile_from_feature(String jsonData) {
        try {
            RestAssured.baseURI = ConstantApi.BASE_URL;
            request = RestAssured.given();
            profilePage = new ProfilePage(request);

            JsonPath jsonPath = new JsonPath(jsonData);
            expectedUsername = jsonPath.getString("username");
            expectedDateOfBirth = jsonPath.getString("dateOfBirth");
            expectedGender = jsonPath.getString("gender");
            expectedSubscribedMarketing = jsonPath.getBoolean("subscribedMarketing");

            response = profilePage.createProfileValidate(expectedUsername, expectedDateOfBirth, expectedGender, expectedSubscribedMarketing);
        } catch (Exception e) {
            logger.error("Error during profile creation", e);
            throw new RuntimeException(e);
        }
    }

    @Given("the user sends a POST request to create a profile from feature")
    public void the_user_sends_a_post_request_to_create_a_profile_from_feature(String jsonData) {
        try {
            RestAssured.baseURI = ConstantApi.BASE_URL;
            request = RestAssured.given();
            profilePage = new ProfilePage(request);

            JsonPath jsonPath = new JsonPath(jsonData);
            expectedUsername = jsonPath.getString("username");
            expectedDateOfBirth = jsonPath.getString("dateOfBirth");
            expectedGender = jsonPath.getString("gender");
            expectedSubscribedMarketing = jsonPath.getBoolean("subscribedMarketing");

            generatedUsername = expectedUsername + "_" + System.currentTimeMillis();
            response = profilePage.createProfile(generatedUsername, expectedDateOfBirth, expectedGender, expectedSubscribedMarketing);
            userId = response.jsonPath().getInt("userId");
        } catch (Exception e) {
            logger.error("Error during profile creation", e);
            throw new RuntimeException(e);
        }
    }

    @When("the user should receive a successful response with status {string} and message {string}")
    public void theUserShouldReceiveASuccessfulResponseWithStatusAndMessage(String status, String errorMessage) {
        try {
            int expectedStatus = Integer.parseInt(status);
            int actualCode = response.getStatusCode();
            assertEquals("Status code mismatch", expectedStatus, actualCode);

            List<Map<String, String>> errors = response.jsonPath().getList("errors");
            if (errors != null && !errors.isEmpty()) {
                String actualErrorMessage = errors.get(0).get("msg");  // Lấy msg từ lỗi đầu tiên
                assertEquals("Error message mismatch", errorMessage, actualErrorMessage);
            } else {
                assertTrue("No error message found", errorMessage.isEmpty());
            }

        } catch (Exception e) {
            logger.error("Error during response validation", e);
            throw new RuntimeException("Error during response validation", e);
        }
    }

    @When("the user should receive a successful response with status {string}")
    public void the_user_should_receive_a_successful_response_with_status_201(String expectedStatusCode) {
        try {
            int actualCode = response.getStatusCode();
            int expectedCode = Integer.parseInt(expectedStatusCode);
            assertEquals("Status code mismatch", expectedCode, actualCode);
        } catch (Exception e) {
            logger.error("Status code validation failed", e);
            throw new RuntimeException(e);
        }
    }

    @Then("the user should receive a valid userId")
    public void the_user_should_receive_a_valid_userId() {
        try {
            assert userId > 0 : "Invalid userId received";
        } catch (AssertionError e) {
            logger.error("UserId validation failed", e);
            throw e;
        }
    }

    @When("the user sends a valid GET request to fetch the profile with the created userId")
    public void the_user_sends_a_valid_get_request_to_fetch_the_profile_with_the_created_userId() {
        try {
            response = profilePage.getProfile(userId);
        } catch (Exception e) {
            logger.error("Error fetching profile", e);
            throw new RuntimeException(e);
        }
    }

    @Then("the profile should contain the created userId")
    public void the_profile_should_contain_the_created_userId() {
        verifyFieldEquals("userId", userId, response.jsonPath().getInt("userId"));
    }

    @Then("the profile should contain username correctly")
    public void the_profile_should_contain_username() {
        verifyFieldEquals("username", generatedUsername, response.jsonPath().getString("username"));
    }

    @Then("the profile should contain dateOfBirth correctly")
    public void the_profile_should_contain_dateOfBirth() {
        String actualDate = response.jsonPath().getString("dateOfBirth").split("T")[0];
        verifyFieldEquals("dateOfBirth", expectedDateOfBirth, actualDate);
    }

    @Then("the profile should contain gender correctly")
    public void the_profile_should_contain_gender() {
        verifyFieldEquals("gender", expectedGender, response.jsonPath().getString("gender"));
    }

    @Then("the profile should contain subscribedMarketing correctly")
    public void the_profile_should_contain_subscribedMarketing() {
        int expected = expectedSubscribedMarketing ? 1 : 0;
        int actual = response.jsonPath().getInt("subscribedMarketing");
        verifyFieldEquals("subscribedMarketing", expected, actual);
    }


    private void verifyFieldEquals(String field, Object expected, Object actual) {
        try {
            assertEquals("Mismatch in field: " + field, expected, actual);
        } catch (AssertionError e) {
            logger.error("Validation failed for {}. Expected: {}, but was: {}", field, expected, actual, e);
            throw e;
        }
    }

    @When("I connect database and I query data inserted correctly")
    public void iConnectDatabaseAndIQueryDataInsertedCorrectly() {
        try {
            profilePage.verifyProfileInDatabase(
                    userId,
                    generatedUsername,
                    expectedDateOfBirth,
                    expectedGender,
                    expectedSubscribedMarketing
            );
        } catch (Exception e) {
            logger.error("Error during database verification", e);
            throw new RuntimeException("Error during database verification", e);
        }
    }

    @Then("I verify new profile is unique")
    public void iVerifyNewProfileIsUnique() {
        String username = response.jsonPath().getString("username");
        boolean isUnique = profilePage.isProfileUnique(username);
        assert isUnique : "Profile with username '" + username + "' already exists in the database!";
    }

}
