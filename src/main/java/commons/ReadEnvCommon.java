package commons;

import constants.Constant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ReadEnvCommon {
    public static String loadConfigDataOfEvn(String key) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(loadDataCommon(Constant.FOLDER_DATA + Constant.FILE_EVN,"EVN"));
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static String loadDataCommon(String path, String key) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(path);
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void writeConfig(String file, String key, String value) {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(Constant.FOLDER_DATA + file);
            properties.load(input);
            input.close();
            properties.setProperty(key, value);
            OutputStream outputStream = new FileOutputStream(Constant.FOLDER_DATA + file);
            properties.save(outputStream, null);
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String accessUrlContainBasicAuthen(String key_url, String key_userName, String key_password){
        String basicUrl = loadConfigDataOfEvn(key_url);
        String basicUserName = loadConfigDataOfEvn(key_userName);
        String basicPassword = loadConfigDataOfEvn(key_password);
        String endCodeBasicPassword = encodeValue(basicPassword);
        return "https://" + basicUserName + ":" + endCodeBasicPassword + "@" +  basicUrl;
    }

    public static String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
