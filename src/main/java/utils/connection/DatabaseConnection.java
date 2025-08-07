package utils.connection;

import commons.ReadEnvCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

    // Lấy thông tin cấu hình từ tệp Evn.properties
    private static final String MySqlDriver = ReadEnvCommon.loadConfigDataOfEvn("MYSQL_URL");
    private static final String MySqlUsername = ReadEnvCommon.loadConfigDataOfEvn("MYSQL_USERNAME");
    private static final String MySqlUserPassword = ReadEnvCommon.loadConfigDataOfEvn("MYSQL_PASSWORD");

    /**
     * Tạo kết nối đến MySQL database và trả về đối tượng Connection.
     *
     * @return Connection nếu kết nối thành công, null nếu không.
     */
    public static Connection createConnection() {
        Connection conn = null;
        try {
            // Đăng ký driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo kết nối với cơ sở dữ liệu MySQL
            conn = DriverManager.getConnection(MySqlDriver, MySqlUsername, MySqlUserPassword);

            // Log thông báo kết nối thành công
            if (conn != null) {
                logger.info("Successfully connected to the MySQL database.");
            } else {
                logger.error("Failed to connect to the MySQL database. Connection is null.");
            }
        } catch (SQLException e) {
            // Log chi tiết lỗi SQLException
            logger.error("SQLException occurred while trying to connect to MySQL. Error: ", e);
        } catch (ClassNotFoundException e) {
            // Log chi tiết lỗi ClassNotFoundException nếu driver không tìm thấy
            logger.error("MySQL JDBC Driver not found. Error: ", e);
        } catch (Exception e) {
            // Log chi tiết lỗi khác
            logger.error("Failed to connect to MySQL database. Error: ", e);
        }
        return conn;
    }

    /**
     * Kiểm tra trạng thái kết nối và đóng kết nối nếu cần
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                logger.info("Connection closed successfully.");
            } else {
                logger.warn("Connection is already closed or null.");
            }
        } catch (SQLException e) {
            logger.error("Error occurred while closing the connection. Error: ", e);
        }
    }
}
