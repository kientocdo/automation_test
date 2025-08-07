package utils.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class mySQLQuerySingle {
    public String querySingleField(String queryData){
        Connection conn = DatabaseConnection.createConnection();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Tạo statement
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryData);

            // Lấy thông tin về số cột và tên cột từ kết quả
            int columnCount = rs.getMetaData().getColumnCount();

            // In ra tất cả các hàng của bảng
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String storeField = rs.getString(i) + "\t";
                    return storeField;
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
