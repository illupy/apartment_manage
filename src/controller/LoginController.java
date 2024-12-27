package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.UserSession;
import services.MysqlConnection;

public class LoginController {
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lbLoginFail;

    public void Login(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        String username = tfUsername.getText();
        String pass = tfPassword.getText();
        Platform.runLater(() -> lbLoginFail.setVisible(false)); // Chưa hiển thị lbLoginFail

        try (Connection connection = MysqlConnection.getMysqlConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND passwd = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, pass);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        Platform.runLater(() -> lbLoginFail.setVisible(true)); // Hiển thị label lbLoginFail
                        return;
                    }

                    // Đăng nhập thành công, lưu username vào UserSession
                    UserSession.setUsername(username);

                    // Chuyển đến màn hình Home
                    Parent home = FXMLLoader.load(getClass().getResource("/views/Home3.fxml"));
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(home));
                    stage.setResizable(false);
                    stage.show();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
