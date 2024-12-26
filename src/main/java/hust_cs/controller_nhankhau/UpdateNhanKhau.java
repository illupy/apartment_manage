package hust_cs.controller_nhankhau;

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
import hust_cs.models.NhanKhauModel;
import hust_cs.services.NhanKhauService;

public class UpdateNhanKhau implements Initializable{
	private int maNhanKhau;

	@FXML
	private TextField tfMaNhanKhau;
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
	private TextField tfNoiThuongTru;
	@FXML
	private TextField tfNgheNghiep;
	@FXML
	private TextField tfQueQuan;

	private NhanKhauModel nhanKhauModel;

	public NhanKhauModel getNhanKhauModel() {
		return nhanKhauModel;
	}

	public void setNhanKhauModel(NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
		this.nhanKhauModel = nhanKhauModel;

		maNhanKhau = nhanKhauModel.getId();
		tfMaNhanKhau.setText(Integer.toString(maNhanKhau));
		tfMaNhanKhau.setEditable(false);

		java.util.Date ngaySinhUtilDate = new java.util.Date(nhanKhauModel.getNgaySinh().getTime());

		LocalDate localDate = ngaySinhUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		dpNgaySinh.setValue(localDate);
		tfTenNhanKhau.setText(nhanKhauModel.getTen());
		tfGioiTinh.setValue(nhanKhauModel.getGioiTinh());
		tfSoDienThoai.setText(nhanKhauModel.getSdt());
		tfSoCCCD.setText(nhanKhauModel.getCccd());
		tfNoiThuongTru.setText(nhanKhauModel.getNoiThuongTru());
		tfNgheNghiep.setText(nhanKhauModel.getNgheNghiep());
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
		String noithuongtruString = tfNoiThuongTru.getText();
		String nghenghiepString = tfNgheNghiep.getText();

		// Update the existing NhanKhau
		NhanKhauModel nhanKhauModel = new NhanKhauModel(maNhanKhau, cccdString, tenString, gioiTinhString, ngaySinhDate, sdtString, nghenghiepString, quequanString, noithuongtruString);
		new NhanKhauService().updateNhanKhau(nhanKhauModel);

		// Display success message
		showAlert("Cập nhật thông tin nhân khẩu thành công!");

		// Close the stage after update
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();
	}

	private boolean validateFields() {
		// Example validation for ID
		Pattern idPattern = Pattern.compile("\\d{1,11}");
		if (!idPattern.matcher(tfMaNhanKhau.getText()).matches()) {
			showAlert("Mã nhân khẩu không hợp lệ!");
			return false;
		}

		// Example validation for name
		if (tfTenNhanKhau.getText().length() >= 50 || tfTenNhanKhau.getText().length() <= 1) {
			showAlert("Tên không hợp lệ!");
			return false;
		}

		// Example validation for CCCD
		Pattern cccdPattern = Pattern.compile("\\d{1,20}");
		if (!cccdPattern.matcher(tfSoCCCD.getText()).matches()) {
			showAlert("CCCD không hợp lệ!");
			return false;
		}

		// Example validation for phone number
		Pattern phonePattern = Pattern.compile("\\d{1,15}");
		if (!phonePattern.matcher(tfSoDienThoai.getText()).matches()) {
			showAlert("Số điện thoại không hợp lệ!");
			return false;
		}

		// Other validations...

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
