package utils.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class mySQLQueryMulti {

    // Method to execute the query and return all fields as a List
    public List<String> queryProfileData(String queryData) {
        Connection conn = DatabaseConnection.createConnection();
        Statement stmt = null;
        ResultSet rs = null;
        List<String> profileData = new ArrayList<>();

        try {
            // Create statement and execute query
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryData);

            // Get the number of columns
            int columnCount = rs.getMetaData().getColumnCount();

            // Process the result set
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String fieldValue = rs.getString(i);
                    profileData.add(fieldValue);  // Add field value to the list
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                // Close all resources
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return profileData;
    }

    public int queryCount(String query, Object... params) {
        Connection conn = DatabaseConnection.createConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            // Prepare the SQL query
            stmt = conn.prepareStatement(query);

            // Set parameters dynamically (to prevent SQL injection)
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]); // Set each parameter dynamically
                }
            }

            // Execute the query and get the result
            rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);  // Get the count value (first column of result set)
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close all resources
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return count;
    }

}
