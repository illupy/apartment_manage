package controller.khoanthu;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import services.KhoanThuService;

public class AddLoaiKhoanThu extends controller.HomeController implements Initializable {
	@FXML
	private TextField tfTenKhoanThu;
	@FXML
	private ComboBox<String> cbBatBuoc;

	public void addLoaiKhoanThu(ActionEvent event) throws ClassNotFoundException, SQLException {
		String tenKhoanThu = tfTenKhoanThu.getText();
		SingleSelectionModel<String> batBuoc = cbBatBuoc.getSelectionModel();
		String batBuocString = batBuoc.getSelectedItem();
		int batBuocInt = 1;
		
		if (tenKhoanThu == null) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào tên khoản thu!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}
		
		if (batBuocString == null) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy lựa chọn 1 trong 2 giá trị!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		} else if (batBuocString.equals("Tự nguyện")) {
			batBuocInt = 0;
		}
		
		if (new KhoanThuService().addLoaiKhoanThu(tenKhoanThu, batBuocInt)) {
	        Alert alert = new Alert(AlertType.INFORMATION, "Thêm loại khoản thu thành công!", ButtonType.OK);
	        alert.setHeaderText(null);
	        alert.showAndWait();
	        
	        // Clear form sau khi thêm thành công
	        tfTenKhoanThu.clear();
	        cbBatBuoc.getSelectionModel().clearSelection();
	    }
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cbBatBuoc.getItems().addAll("Bắt buộc", "Tự nguyện");
	}

}
