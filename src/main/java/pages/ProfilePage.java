package pages;

import constants.ConstantApi;
import constants.ConstantQuery;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.api.ApiHelper;
import utils.api.HttpMethod;
import utils.connection.mySQLQueryMulti;

import java.util.HashMap;
import java.util.List;

public class ProfilePage {
    private static final Logger logger = LoggerFactory.getLogger(ProfilePage.class);

    private final ApiHelper apiHelper;
    private final RequestSpecification request;
    private final HashMap<String, String> headers = new HashMap<>();

    public ProfilePage(RequestSpecification request) {
        this.apiHelper = new ApiHelper();
        this.request = request;
        headers.put("Content-Type", "application/json");
    }

    public Response createProfileValidate(String username, String dateOfBirth, String gender, boolean subscribedMarketing) {
        String jsonBody = createProfileJsonBody(username, dateOfBirth, gender, subscribedMarketing);
        Response response = apiHelper.makeRequest(request, HttpMethod.POST, ConstantApi.API_CREATE_PROFILE, headers, jsonBody, null);
        if (response.getStatusCode() != 200) {
            logger.error("Error creating profile: " + response.getBody().asString());
        }
        return response;
    }

    public Response createProfile(String username, String dateOfBirth, String gender, boolean subscribedMarketing) {
        String jsonBody = createProfileJsonBody(username, dateOfBirth, gender, subscribedMarketing);
        Response response = apiHelper.makeRequest(request, HttpMethod.POST, ConstantApi.API_CREATE_PROFILE, headers, jsonBody, null);

        // Nếu trùng username, thử lại với username khác
        if (isUsernameConflict(response)) {
            logger.warn("Username '{}' already exists. Retrying with new username.", username);
            return retryWithUniqueUsername(username, dateOfBirth, gender, subscribedMarketing);
        }

        return response;
    }

    // Phương thức này xử lý trường hợp trùng username
    private Response retryWithUniqueUsername(String baseUsername, String dateOfBirth, String gender, boolean subscribedMarketing) {
        String newUsername = generateUniqueUsername(baseUsername);
        String jsonBody = createProfileJsonBody(newUsername, dateOfBirth, gender, subscribedMarketing);
        return apiHelper.makeRequest(request, HttpMethod.POST, ConstantApi.API_CREATE_PROFILE, headers, jsonBody, null);
    }


    // Create request body
    private String createProfileJsonBody(String username, String dateOfBirth, String gender, boolean subscribedMarketing) {
        return String.format(
                "{\"username\": \"%s\", \"dateOfBirth\": \"%s\", \"gender\": \"%s\", \"subscribedMarketing\": %b}",
                username, dateOfBirth, gender, subscribedMarketing
        );
    }

    // Check for conflict
    private boolean isUsernameConflict(Response response) {
        return response.getStatusCode() == 400 &&
                response.getBody().asString().contains("Username already exists");
    }

    // Retry with timestamp
    private String generateUniqueUsername(String baseUsername) {
        return baseUsername + "_" + System.currentTimeMillis();
    }

    // GET profile
    public Response getProfile(int userId) {
        String url = ConstantApi.API_GET_PROFILE.replace("{userId}", String.valueOf(userId));
        return apiHelper.makeRequest(request, HttpMethod.GET, url, headers, null, null);
    }

    // Verify DB
    public void verifyProfileInDatabase(int userId, String expectedUsername, String expectedDateOfBirth,
                                        String expectedGender, boolean expectedSubscribedMarketing) {
        try {
            String query = "SELECT username, dateOfBirth, gender, subscribedMarketing FROM profiles WHERE userId = " + userId;

            mySQLQueryMulti queryExecutor = new mySQLQueryMulti();
            List<String> profileData = queryExecutor.queryProfileData(query);

            // Kiểm tra tính hợp lệ của dữ liệu trả về
            if (profileData == null || profileData.size() != 4) {
                throw new RuntimeException("Profile data mismatch or incomplete: " + profileData);
            }

            assertUsername(profileData.get(0), expectedUsername);
            assertDateOfBirth(profileData.get(1), expectedDateOfBirth);
            assertGender(profileData.get(2), expectedGender);
            assertSubscribedMarketing(profileData.get(3), expectedSubscribedMarketing);

        } catch (Exception e) {
            logger.error("❌ DB Verification failed: ", e);
            throw new RuntimeException("Error verifying profile in database", e);
        }
    }

    private void assertUsername(String actualUsername, String expectedUsername) {
        assert actualUsername.equals(expectedUsername) : "Username mismatch!";
    }

    private void assertDateOfBirth(String actualDateOfBirth, String expectedDateOfBirth) {
        assert actualDateOfBirth.split(" ")[0].equals(expectedDateOfBirth) : "Date of birth mismatch!";
    }

    private void assertGender(String actualGender, String expectedGender) {
        assert actualGender.equals(expectedGender) : "Gender mismatch!";
    }

    private void assertSubscribedMarketing(String actualSubscribedMarketing, boolean expectedSubscribedMarketing) {
        String expectedSubscribedMarketingStr = expectedSubscribedMarketing ? "1" : "0";
        assert actualSubscribedMarketing.equals(expectedSubscribedMarketingStr) : "SubscribedMarketing mismatch!";
    }

    // Phương thức kiểm tra tính duy nhất của profile trong DB
    public boolean isProfileUnique(String username) {
        String query = "SELECT COUNT(*) FROM profiles WHERE username = ?";
        mySQLQueryMulti queryExecutor = new mySQLQueryMulti();
        int count = queryExecutor.queryCount(query, username);
        return count == 1;
    }
}
