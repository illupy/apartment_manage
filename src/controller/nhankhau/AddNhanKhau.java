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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.NhanKhauModel;
import services.NhanKhauService;

public class AddNhanKhau extends controller.HomeController implements Initializable {
	@FXML
	private TextField tfId;
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
	
	

	
	public void addNhanKhau(ActionEvent event) {
	    try {
	        if (validateInput()) {
	            performDatabaseOperations();
	            closeStage(event);
	        }
	    } catch (Exception e) {
	        showAlert("Error", "Không thể thực hiện", AlertType.ERROR);
	        e.printStackTrace();
	    }
	}

	private boolean validateInput() throws ClassNotFoundException, SQLException {
	    return validateID() && 
	    		validateName() && validateCCCD() && validatePhoneNumber() && validateDateOfBirth();
	}

	// Validate ID
	private boolean validateID() throws ClassNotFoundException, SQLException {
	    Pattern pattern = Pattern.compile("\\d{1,11}");
	    if (!pattern.matcher(tfId.getText()).matches()) {
	        showAlert("Hãy nhập vào ID hợp lệ!");
			return false;
	    }

	    List<NhanKhauModel> listNhanKhauModels = new NhanKhauService().getListNhanKhau(null);
	    for (NhanKhauModel nhanKhau : listNhanKhauModels) {
	        if (nhanKhau.getId() == Integer.parseInt(tfId.getText())) {
	            showAlert("ID nhân khẩu bị trùng!");
	            return false;
	        }
	    }
	    return true;
	}

	// Validate CCCD
	private boolean validateCCCD() throws ClassNotFoundException, SQLException {
	    Pattern pattern = Pattern.compile("\\d{9,12}");
	    if (!pattern.matcher(tfCccd.getText()).matches()) {
	        showAlert("Hãy nhập vào CCCD hợp lệ (9-12 chữ số)!");
	        return false;
	    }

	    List<NhanKhauModel> listNhanKhauModels = new NhanKhauService().getListNhanKhau(null);
	    for (NhanKhauModel nhanKhau : listNhanKhauModels) {
	        if (nhanKhau.getCccd().equals(tfCccd.getText())) {
	            showAlert("CCCD bị trùng!");
	            return false;
	        }
	    }
	    return true;
	}

	// Validate Name
	private boolean validateName() {
	    if (tfTen.getText().isEmpty() || tfTen.getText().length() > 50) {
	        showAlert("Hãy nhập vào tên hợp lệ (tối đa 50 ký tự)!");
	        return false;
	    }
	    return true;
	}

	// Validate Phone Number
	private boolean validatePhoneNumber() {
	    if (!tfSdt.getText().isEmpty()) {
	        Pattern pattern = Pattern.compile("\\d{10,11}");
	        if (!pattern.matcher(tfSdt.getText()).matches()) {
	            showAlert("Số điện thoại không hợp lệ (10-11 chữ số)!");
	            return false;
	        }
	    }
	    return true;
	}

	// Validate Date of Birth
	private boolean validateDateOfBirth() {
	    if (dpNgaySinh.getValue() == null) {
	        showAlert("Hãy chọn ngày sinh hợp lệ!");
	        return false;
	    }
	    return true;
	}

	private void performDatabaseOperations() throws ClassNotFoundException, SQLException {
	    int idInt = Integer.parseInt(tfId.getText());
	    String tenString = tfTen.getText();
	    String gioiTinhString = tfGioiTinh.getSelectionModel().getSelectedItem().trim();
	    String cccdString = tfCccd.getText();
	    Date ngaySinhDate = java.sql.Date.valueOf(dpNgaySinh.getValue());
	    String sdtString = tfSdt.getText();
	    String quequanString = tfQueQuan.getText();

	    NhanKhauService nhanKhauService = new NhanKhauService();
	    NhanKhauModel nhanKhauModel = new NhanKhauModel(idInt, 
	    		cccdString, tenString, gioiTinhString, ngaySinhDate, sdtString, quequanString);

	    nhanKhauService.addNhanKhau(nhanKhauModel);
	}

	private void closeStage(ActionEvent event) {
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.close();
	}

	private void showAlert(String message) {
	    Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
	    alert.setHeaderText(null);
	    alert.showAndWait();
	}

	private void showAlert(String title, String content, AlertType alertType) {
	    Alert alert = new Alert(alertType, content, ButtonType.OK);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.showAndWait();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    tfGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
	}
}
