package constants;

public class ConstantQuery {
    // Verify Profile
    public static final String queryVerifyProfile = "SELECT username, dateOfBirth, gender, subscribedMarketing FROM profiles WHERE userId = {userId}";
    public static final String queryUnique = "SELECT COUNT(*) FROM profiles WHERE username = {username}";
}
