package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import models.UserSession;
import services.MysqlConnection;

public class UserController extends HomeController {

    @FXML private PasswordField pfCurrentPassword;
    @FXML private PasswordField pfNewPassword;
    @FXML private PasswordField pfConfirmNewPassword;

    @FXML
    public void handleChangePassword(ActionEvent event) {
        // Lấy username từ UserSession thay vì nhập thủ công
        String username = UserSession.getUsername();
        
        if (username == null || username.isEmpty()) {
            showAlert("Bạn chưa đăng nhập. Vui lòng đăng nhập lại.");
            return;
        }

        String currentPassword = pfCurrentPassword.getText().trim();
        String newPassword = pfNewPassword.getText().trim();
        String confirmNewPassword = pfConfirmNewPassword.getText().trim();

        // Kiểm tra thông tin nhập vào
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu mới có khớp không
        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("Mật khẩu mới và xác nhận mật khẩu không khớp.");
            return;
        }

        // Kiểm tra mật khẩu hiện tại có đúng không
        try {
            if (!checkCurrentPassword(username, currentPassword)) {
                showAlert("Mật khẩu hiện tại không đúng.");
                return;
            }

            // Cập nhật mật khẩu mới
            if (updatePassword(username, newPassword)) {
                showAlert("Mật khẩu đã được thay đổi thành công!", Alert.AlertType.INFORMATION);

                // Reset các ô nhập liệu sau khi thay đổi mật khẩu thành công
                pfCurrentPassword.clear();
                pfNewPassword.clear();
                pfConfirmNewPassword.clear();

            } else {
                showAlert("Có lỗi xảy ra khi cập nhật mật khẩu.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Lỗi: " + e.getMessage());
        }
    }


    // Kiểm tra mật khẩu hiện tại
    private boolean checkCurrentPassword(String username, String currentPassword) throws SQLException, ClassNotFoundException {
        String query = "SELECT passwd FROM users WHERE username = ?";
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("passwd");
                // So sánh mật khẩu hiện tại (nếu mật khẩu chưa mã hóa, so sánh thẳng)
                return currentPassword.equals(storedPassword);
            }
        }
        return false;
    }

    // Cập nhật mật khẩu mới
    private boolean updatePassword(String username, String newPassword) throws SQLException, ClassNotFoundException {
        String updateQuery = "UPDATE users SET passwd = ? WHERE username = ?";
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement ps = connection.prepareStatement(updateQuery)) {

            ps.setString(1, newPassword);
            ps.setString(2, username);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    // Hiển thị thông báo lỗi
    private void showAlert(String message) {
        showAlert(message, Alert.AlertType.WARNING);
    }

    // Hiển thị thông báo với tùy chọn loại cảnh báo
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setHeaderText(null);  // Không cần tiêu đề
        alert.showAndWait();  // Hiển thị và đợi người dùng đóng thông báo
    }
}
