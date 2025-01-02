package controller.nhankhau;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.NhanKhauModel;
import services.NhanKhauService;

public class UpdateNhanKhau extends controller.HomeController implements Initializable{
	private int maNhanKhau;

	@FXML
	private DatePicker dpNgaySinh;
	@FXML
	private TextField tfTenNhanKhau;
	@FXML
	private ComboBox<String> tfGioiTinh;
	@FXML
	private TextField tfSoDienThoai;
	@FXML
	private TextField tfSoCCCD;
	@FXML
	private TextField tfQueQuan;

	private NhanKhauModel nhanKhauModel;

	public NhanKhauModel getNhanKhauModel() {
		return nhanKhauModel;
	}

	public void setNhanKhauModel(NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
		this.nhanKhauModel = nhanKhauModel;

		maNhanKhau = nhanKhauModel.getId();

		java.util.Date ngaySinhUtilDate = new java.util.Date(nhanKhauModel.getNgaySinh().getTime());

		LocalDate localDate = ngaySinhUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		dpNgaySinh.setValue(localDate);
		tfTenNhanKhau.setText(nhanKhauModel.getTen());
		tfGioiTinh.setValue(nhanKhauModel.getGioiTinh());
		tfSoDienThoai.setText(nhanKhauModel.getSdt());
		tfSoCCCD.setText(nhanKhauModel.getCccd());
		tfQueQuan.setText(nhanKhauModel.getQueQuan());

	}
	
	@FXML
	public void updateNhanKhau(ActionEvent event) throws ClassNotFoundException, SQLException {
		if (!validateFields()) {
			return; 
		}

		Date ngaySinhDate = java.sql.Date.valueOf(dpNgaySinh.getValue());

		// Get values from form fields
		String tenString = tfTenNhanKhau.getText();
		String cccdString = tfSoCCCD.getText();
		String gioiTinhString = tfGioiTinh.getSelectionModel().getSelectedItem().trim();
		String sdtString = tfSoDienThoai.getText();
		String quequanString = tfQueQuan.getText();

		// Update the existing NhanKhau
		NhanKhauModel nhanKhauModel = new NhanKhauModel(maNhanKhau, cccdString, tenString, gioiTinhString, ngaySinhDate, sdtString, quequanString);
		new NhanKhauService().updateNhanKhau(nhanKhauModel);

		// Cập nhật lại giao diện
        Alert alert = new Alert(AlertType.INFORMATION, "Cập nhật nhân khẩu thành công!", ButtonType.OK);
        alert.setHeaderText(null);
        
        // Đợi người dùng bấm OK trên alert rồi mới chuyển scene
        alert.showAndWait().ifPresent(response -> {
            try {
                switchScene(event, "/views/nhankhau/nhankhau.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(AlertType.ERROR, "Có lỗi xảy ra: " + e.getMessage(), ButtonType.OK);
                errorAlert.setHeaderText(null);
                errorAlert.showAndWait();
            }
        });
	}

	private boolean validateFields() {
		return  validateTenNhanKhau() && validateCCCD() && validateSoDienThoai() && validateNgaySinh() && validateQueQuan();
	}

	private boolean validateTenNhanKhau() {
		if (tfTenNhanKhau.getText().length() >= 50 || tfTenNhanKhau.getText().length() <= 1) {
			showAlert("Tên không hợp lệ! Phải có độ dài từ 2 đến 50 ký tự.");
			return false;
		}
		return true;
	}

	private boolean validateCCCD() {
		Pattern cccdPattern = Pattern.compile("\\d{1,20}");
		if (!cccdPattern.matcher(tfSoCCCD.getText()).matches()) {
			showAlert("CCCD không hợp lệ! Phải là số nguyên từ 1 đến 20 chữ số.");
			return false;
		}
		return true;
	}

	private boolean validateSoDienThoai() {
		Pattern phonePattern = Pattern.compile("\\d{1,15}");
		if (!phonePattern.matcher(tfSoDienThoai.getText()).matches()) {
			showAlert("Số điện thoại không hợp lệ! Phải là số nguyên từ 1 đến 15 chữ số.");
			return false;
		}
		return true;
	}

	private boolean validateNgaySinh() {
		if (dpNgaySinh.getValue() == null) {
			showAlert("Ngày sinh không được để trống!");
			return false;
		}
		LocalDate ngaySinh = dpNgaySinh.getValue();
		if (ngaySinh.isAfter(LocalDate.now())) {
			showAlert("Ngày sinh không được lớn hơn ngày hiện tại!");
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
		// Display alert messages
		Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		tfGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
	}

}

