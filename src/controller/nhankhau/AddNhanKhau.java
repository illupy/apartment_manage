package controller.nhankhau;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.NhanKhauModel;
import services.NhanKhauService;

public class AddNhanKhau extends controller.HomeController implements Initializable {
    @FXML
    private TextField tfTen;
    @FXML
    private DatePicker dpNgaySinh;
    @FXML
    private ComboBox<String> tfGioiTinh;
    @FXML
    private TextField tfCccd;
    @FXML
    private TextField tfSdt;
    @FXML
    private TextField tfQueQuan;

    @FXML
    public void addNhanKhau(ActionEvent event) throws ClassNotFoundException, SQLException {
            if (!validateFields()) {
                return;
            }

            // Lấy dữ liệu từ các trường nhập liệu
            String tenString = tfTen.getText();
            String gioiTinhString = tfGioiTinh.getSelectionModel().getSelectedItem().trim();
            String cccdString = tfCccd.getText();
            Date ngaySinhDate = java.sql.Date.valueOf(dpNgaySinh.getValue());
            String sdtString = tfSdt.getText();
            String quequanString = tfQueQuan.getText();

            // Tạo đối tượng NhanKhauModel
            NhanKhauModel nhanKhauModel = new NhanKhauModel(cccdString, tenString, gioiTinhString, ngaySinhDate, sdtString, quequanString);

            // Gọi service để thêm nhân khẩu
            new NhanKhauService().addNhanKhau(nhanKhauModel);

            // Hiển thị thông báo thành công
            showAlert("Thêm thông tin nhân khẩu thành công!");

            // Đóng cửa sổ sau khi thêm
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    		stage.close();
 
    }
    
    

    private boolean validateFields() {
        return validateName() && validateCCCD() && validatePhoneNumber() && validateDateOfBirth() && validateQueQuan();
    }

    private boolean validateName() {
        if (tfTen.getText().length() < 2 || tfTen.getText().length() > 50) {
            showAlert("Tên không hợp lệ! Phải có độ dài từ 2 đến 50 ký tự.");
            return false;
        }
        return true;
    }

    private boolean validateCCCD() {
        Pattern cccdPattern = Pattern.compile("\\d{9,12}");
        if (!cccdPattern.matcher(tfCccd.getText()).matches()) {
            showAlert("CCCD không hợp lệ! Phải là số từ 9 đến 12 chữ số.");
            return false;
        }

        try {
            List<NhanKhauModel> listNhanKhauModels = new NhanKhauService().getListNhanKhau(null);
            for (NhanKhauModel nhanKhau : listNhanKhauModels) {
                if (nhanKhau.getCccd().equals(tfCccd.getText())) {
                    showAlert("CCCD đã tồn tại!");
                    return false;
                }
            }
        } catch (Exception e) {
            showAlert( "Lỗi" ,"Lỗi kiểm tra CCCD!", Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean validatePhoneNumber() {
        if (!tfSdt.getText().isEmpty()) {
            Pattern phonePattern = Pattern.compile("\\d{10,11}");
            if (!phonePattern.matcher(tfSdt.getText()).matches()) {
                showAlert("Số điện thoại không hợp lệ! Phải là số từ 10 đến 11 chữ số.");
                return false;
            }
        }
        return true;
    }

	private boolean validateDateOfBirth() {
	    if (dpNgaySinh.getValue() == null) {
	        showAlert("Hãy chọn ngày sinh hợp lệ!");
	        return false;
	    }
	    return true;
	}

    private boolean validateQueQuan() {
        if (tfQueQuan.getText().isEmpty() || tfQueQuan.getText().length() > 100) {
            showAlert("Quê quán không hợp lệ! Độ dài tối đa là 100 ký tự.");
            return false;
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
    }
    
}
