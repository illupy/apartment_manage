package hust_cs.controller_nhankhau;

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
import hust_cs.models.HoKhauModel;
import hust_cs.models.NhanKhauModel;
import hust_cs.models.QuanHeModel;
import hust_cs.services.HoKhauService;
import hust_cs.services.NhanKhauService;
import hust_cs.services.QuanHeService;

public class AddNhanKhau implements Initializable {
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
	private TextField tfMaHoKhau;
	@FXML
	private TextField tfQuanHe;
	@FXML
	private TextField tfQueQuan;
	@FXML
	private TextField tfNoiThuongTru;
	@FXML
	private TextField tfNgheNghiep;

	 public void addNhanKhau(ActionEvent event) {
	        try {
	            if(validateInput()) {
	            	performDatabaseOperations();            	
	            	closeStage(event);
	            }
	        } catch (Exception e) {
	            showAlert("Error", "Không thể thực hiện", AlertType.ERROR);
	            e.printStackTrace(); // Handle or log the exception appropriately
	        }
	    }
	 
	 private boolean validateInput() throws ClassNotFoundException, SQLException {
	        return validateID() && validateName() && validateCCCD() && validateMaHo() ;
	    }

	    //validate ID
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
	    
	    //validate CCCD
	    private boolean validateCCCD() throws ClassNotFoundException, SQLException {
	        Pattern pattern = Pattern.compile("\\d{1,11}");
	        if (!pattern.matcher(tfCccd.getText()).matches()) {
	            showAlert("Hãy nhập vào CCCD hợp lệ!");
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
	    
	    //validate Mahokhau
	    private boolean validateMaHo() throws ClassNotFoundException, SQLException {
	        Pattern pattern = Pattern.compile("\\d{1,11}");
	        if (!pattern.matcher(tfMaHoKhau.getText()).matches()) {
	            showAlert("Hãy nhập vào mã hộ khẩu hợp lệ!");
	            return false;
	        }
	        
	        boolean check = false;
	        
	        List<HoKhauModel> listHoKhauModels = new HoKhauService().getListHoKhau();
	        for (HoKhauModel hokhau : listHoKhauModels) {
	            if (hokhau.getMaHo() == Integer.parseInt(tfMaHoKhau.getText())) {
	                check = true;
	            }
	        }
	        if(!check) {
	        	showAlert("Mã hộ khẩu không tồn tại!");
	        	return false;
	        }
	        return true;
	    }

	    //validate Name
	    private boolean validateName() {
	        if (tfTen.getText().length() >= 50 || tfTen.getText().length() <= 1) {
	            showAlert("Hãy nhập vào tên hợp lệ!");
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
	        int mahokhauInt = Integer.parseInt(tfMaHoKhau.getText());
	        String quanheString = tfQuanHe.getText();
	        String quequanString = tfQueQuan.getText();
	        String noithuongtruString = tfNoiThuongTru.getText();
	        String nghenghiepString = tfNgheNghiep.getText();

	        NhanKhauService nhanKhauService = new NhanKhauService();
	        QuanHeService quanHeService = new QuanHeService();

	        NhanKhauModel nhanKhauModel = new NhanKhauModel(idInt, cccdString, tenString, gioiTinhString, ngaySinhDate, sdtString, nghenghiepString, quequanString, noithuongtruString);
	        QuanHeModel quanHeModel = new QuanHeModel(mahokhauInt, idInt, quanheString);

	        nhanKhauService.add(nhanKhauModel);
	        quanHeService.add(quanHeModel);
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
			// TODO Auto-generated method stub
			tfGioiTinh.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
		}
}