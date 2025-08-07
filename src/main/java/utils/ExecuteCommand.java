package utils;

import commons.ReadEnvCommon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ExecuteCommand {

    // SSH params
    private static String sshName = ReadEnvCommon.loadConfigDataOfEvn("SSH_NAME");
    private static String sshIP = ReadEnvCommon.loadConfigDataOfEvn("SSH_IP");
    private static String sshTunnelRDS = ReadEnvCommon.loadConfigDataOfEvn("SSH_TUNNEL_RDS");
    private static String sshTunnel = ReadEnvCommon.loadConfigDataOfEvn("SSH_TUNNEL");
    private static String os = System.getProperty("os.name").toLowerCase();

    // Execute Command
    public static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder();

        if (os.contains("win")) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("bash", "-c", command);
        }

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Error executing command: ").append(exitCode);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            output.append("Exception: ").append(e.getMessage());
        }

        return output.toString();
    }

    public static void sshToRDS() {
        try {
            disconnectSSH();

            // Get the user's home directory
            String homeDir = System.getProperty("user.home");
            String sshKeyPath = homeDir + "\\.ssh\\" + sshName; // Construct the path to the SSH key

            // Construct the SSH command
            String sshCommandWindow = "ssh -f -N -L " + sshTunnelRDS + " " + sshName + "@" + sshIP + " -i \"" + sshKeyPath + "\"";
            String sshCommand = "ssh -f -N -L " + sshTunnelRDS + " " + sshName + "@" + sshIP + " -i " + "~/.ssh/" + sshName;

            if (os.contains("win")) {
                executeCommand(sshCommandWindow);
            } else {
                executeCommand(sshCommand);
            }

            // Kiểm tra output hoặc mã thoát để xác nhận kết nối thành công
            if (isPortOpen("localhost", 3306)) {
                System.out.println("SSH connection established. Running testcase...");
            } else {
                System.out.println("SSH connection failed. Testcase skipped.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sshToRedshift() {
        try {
            disconnectSSH();

            // Get the user's home directory
            String homeDir = System.getProperty("user.home");
            String sshKeyPath = homeDir + "\\.ssh\\" + sshName; // Construct the path to the SSH key

            // Construct the SSH command
            String sshCommandWindow = "ssh -f -N -L " + sshTunnel + " " + sshName + "@" + sshIP + " -i \"" + sshKeyPath + "\"";
            String sshCommand = "ssh -f -N -L " + sshTunnel + " " + sshName + "@" + sshIP + " -i " + "~/.ssh/" + sshName;

            if (os.contains("win")) {
                executeCommand(sshCommandWindow);
            } else {
                executeCommand(sshCommand);
            }

            // Kiểm tra output hoặc mã thoát để xác nhận kết nối thành công
            if (isPortOpen("localhost", 5439)) {
                System.out.println("SSH connection established. Running testcase...");
            } else {
                System.out.println("SSH connection failed. Testcase skipped.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPortOpen(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);  // Thời gian chờ 2 giây
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void disconnectSSH() {
        if (os.contains("win")) {
            executeCommand("for /F \"tokens=5\" %P in ('netstat -aon ^| findstr :22') do taskkill /F /PID %P");
            System.out.println("Command Output: " + "Disconnected SSH running successfully!");
        } else {
            executeCommand("kill -9 $(ps aux | grep '[s]sh' | awk '{print $2}')");
            System.out.println("Command Output: " + "Disconnected SSH running successfully!");
        }
    }

    public static void clearAllureReport() throws InterruptedException {
        if (os.contains("win")) {
            executeCommand("rmdir /s /q allure-results");
            executeCommand("rmdir /s /q allure-report-all");
        } else {
            executeCommand("rm -rf allure-results");
            Thread.sleep(2000);
            executeCommand("rm -rf allure-report-all");
            Thread.sleep(2000);
            System.out.println("Command Output: " + "Clear previous report successfully!");
        }
    }
}
