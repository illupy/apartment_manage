package hust_cs;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerKP {

    private Stage stage;
    private Scene scene;

    public void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile + ".fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root); 
        stage.setScene(scene);
        stage.show();
    }
  
    @FXML
    void TKho(ActionEvent event) throws IOException {
        switchScene(event, "/thongke/home_statistic");
    }

    @FXML
    void TKkhoanphi(ActionEvent event) throws IOException {
        switchScene(event, "/thongke/fee_statistic");
    }

    @FXML
    void addKhoanPhi(ActionEvent event) {

    }

    @FXML
    void hokhau(ActionEvent event) throws IOException {
        switchScene(event, "/hokhau/hokhau");
    }

    @FXML
    void khoanphi(ActionEvent event) throws IOException {
        switchScene(event, "/khoanphi/khoanphi");

    }

    @FXML
    void logout(ActionEvent event) {
    
    }

    @FXML
    void nhankhau(ActionEvent event) throws IOException {
        switchScene(event, "/nhankhau/nhankhau");
    }

    @FXML
    void update(ActionEvent event) throws IOException {
        switchScene(event, "/updateinfo/updateinfo");
    }

}
