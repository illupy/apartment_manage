package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController{
	@FXML
	private TextField tfUsername;
	@FXML PasswordField tfPassword;
	
	public void Login(ActionEvent event) throws IOException{
		String name = tfUsername.getText();
		String pass = tfPassword.getText();
		
		if(!name.equals("root") || !pass.equals("hshdhu123")) {
			Alert alert = new Alert(AlertType.WARNING, "wrong pass", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}
		
		// Tai giao dien 
		Parent home = FXMLLoader.load(getClass().getResource("/views/Home3.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); 
        stage.setScene(new Scene(home,800,600));
        stage.setResizable(false); // khong thay doi kich thuoc cua so
        stage.show();		
	}
}