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
		
		// Kiểm tra tên khoản thu
        if (tenKhoanThu == null || tenKhoanThu.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào tên khoản thu hợp lệ!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        
        // Kiểm tra giá trị ComboBox
        if (batBuocString == null || batBuocString.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING, "Hãy lựa chọn một giá trị trong danh sách!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Xác định giá trị bắt buộc
        switch (batBuocString) {
            case "Tự nguyện":
                batBuocInt = 0;
                break;
            case "Bắt buộc":
                batBuocInt = 1;
                break;
            default:
                Alert alert = new Alert(AlertType.WARNING, "Giá trị không hợp lệ! Hãy chọn lại.", ButtonType.OK);
                alert.setHeaderText(null);
                alert.showAndWait();
                return;
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
