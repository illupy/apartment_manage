package controller.hokhau;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
import models.ChuHoModel;
import models.HoKhauModel;
import models.NhanKhauModel;
import models.QuanHeModel;
import services.ChuHoService;
import services.HoKhauService;
import services.NhanKhauService;
import services.QuanHeService;

public class AddHoKhau extends controller.HomeController implements Initializable{
	@FXML
	private TextField tfMaHoKhau;
	@FXML
	private TextField tfSoPhong;
	@FXML
	private TextField tfSoOto;
	@FXML
	private TextField tfSoXeMay;
	@FXML
	private TextField tfMaChuHo;
	@FXML
	private TextField tfTenChuHo;
	@FXML
	private DatePicker dpNgaySinh;
	@FXML
	private TextField tfCCCD;
	@FXML
	private TextField tfSoDienThoai;
	@FXML
	private ComboBox<String> tfGioiTinhChuHo;
	
	@FXML
	public void addHoKhau(ActionEvent event) throws ClassNotFoundException, SQLException{
		if(validateInput()) {
			int maHo = Integer.parseInt(tfMaHoKhau.getText());
			String soPhong = tfSoPhong.getText();
			
			
			Integer soOto = tfSoOto.getText().isEmpty() ? null : Integer.parseInt(tfSoOto.getText());
			Integer soXeMay = tfSoXeMay.getText().isEmpty() ? null : Integer.parseInt(tfSoXeMay.getText());
			
			int maChuHo = Integer.parseInt(tfMaChuHo.getText());
			String tenChuHo = tfTenChuHo.getText();
			String gioiTinhChuHo = tfGioiTinhChuHo.getSelectionModel().getSelectedItem().trim();
			Date ngaySinhDate = java.sql.Date.valueOf(dpNgaySinh.getValue());
			String cccdChuHo = tfCCCD.getText();
			String sdtChuHo = tfSoDienThoai.getText();
			
			HoKhauModel hoKhauModel = new HoKhauModel(maHo, soPhong, soOto, soXeMay);
			NhanKhauModel nhanKhauModel = new NhanKhauModel(maChuHo, cccdChuHo, tenChuHo, gioiTinhChuHo, ngaySinhDate, sdtChuHo);
			
			addHoKhauToDatabase(hoKhauModel, nhanKhauModel);
			closeStage(event);
		}
	}
	
	private boolean validateInput() throws ClassNotFoundException, SQLException {
        return validateMaHo() && validateSoPhong() && validateSoOtoXeMay() && validateMaChuHo() && validateTenChuHo() && validateCCCD() && validateSDT();
    }

    private boolean validateMaHo() throws ClassNotFoundException, SQLException {
        Pattern pattern = Pattern.compile("\\d{1,11}");
        if (!pattern.matcher(tfMaHoKhau.getText()).matches()) {
            showAlert("Hãy nhập mã hộ khẩu hợp lệ!");
            return false;
        }

        List<HoKhauModel> listHoKhauModels = new HoKhauService().getListHoKhau();
        for (HoKhauModel hokhau : listHoKhauModels) {
            if (hokhau.getMaHo() == Integer.parseInt(tfMaHoKhau.getText())) {
                showAlert("Mã hộ khẩu bị trùng!");
                return false;
            }
        }
        return true;
    }

    private boolean validateSoPhong() throws SQLException {
        if (tfSoPhong.getText().isEmpty() || tfSoPhong.getText().length() > 50) {
            showAlert("Hãy nhập số phòng hợp lệ!");
            return false;
        }

        // Kiểm tra số phòng trong bảng Phòng của database
//        String query = "SELECT COUNT(*) FROM Phong WHERE soPhong = ?";
//        try (var connection = DatabaseUtils.getConnection();
//             var preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1, tfSoPhong.getText());
//            var resultSet = preparedStatement.executeQuery();
//            if (resultSet.next() && resultSet.getInt(1) == 0) {
//                showAlert("Số phòng không tồn tại trong hệ thống!");
//                return false;
//            }
//        }
        return true;
    }

    private boolean validateSoOtoXeMay() {
        if (!tfSoOto.getText().isEmpty()) {
            try {
                Integer.parseInt(tfSoOto.getText());
            } catch (NumberFormatException e) {
                showAlert("Số ô tô phải là một số nguyên hợp lệ hoặc để trống!");
                return false;
            }
        }

        if (!tfSoXeMay.getText().isEmpty()) {
            try {
                Integer.parseInt(tfSoXeMay.getText());
            } catch (NumberFormatException e) {
                showAlert("Số xe máy phải là một số nguyên hợp lệ hoặc để trống!");
                return false;
            }
        }
        return true;
    }

    private boolean validateMaChuHo() throws ClassNotFoundException, SQLException {
        Pattern pattern = Pattern.compile("\\d{1,11}");
        if (!pattern.matcher(tfMaChuHo.getText()).matches()) {
            showAlert("Hãy nhập mã nhân khẩu hợp lệ!");
            return false;
        }

        List<NhanKhauModel> listNhanKhauModels = new NhanKhauService().getListNhanKhau(null);
        for (NhanKhauModel nhankhau : listNhanKhauModels) {
            if (nhankhau.getId() == Integer.parseInt(tfMaChuHo.getText())) {
                showAlert("Mã nhân khẩu bị trùng!");
                return false;
            }
        }
        return true;
    }

    private boolean validateTenChuHo() {
        if (tfTenChuHo.getText().length() >= 50 || tfTenChuHo.getText().length() <= 1) {
            showAlert("Hãy nhập vào tên hợp lệ!");
            return false;
        }
        return true;
    }

    private boolean validateCCCD() {
        Pattern pattern = Pattern.compile("\\d{1,20}");
        if (!pattern.matcher(tfCCCD.getText()).matches()) {
            showAlert("Hãy nhập vào CCCD hợp lệ!");
            return false;
        }
        return true;
    }

    private boolean validateSDT() {
        Pattern pattern = Pattern.compile("^\\d{10}$");
        if (!pattern.matcher(tfSoDienThoai.getText()).matches()) {
            showAlert("Hãy nhập vào số điện thoại hợp lệ!");
            return false;
        }
        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void addHoKhauToDatabase(HoKhauModel hoKhauModel, NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
        new HoKhauService().add(hoKhauModel);
        new NhanKhauService().addNhanKhau(nhanKhauModel);
        new QuanHeService().add(new QuanHeModel(hoKhauModel.getMaHo(), nhanKhauModel.getId(), "Là chủ hộ"));
        new ChuHoService().add(new ChuHoModel(hoKhauModel.getMaHo(), nhanKhauModel.getId()));
    }

    private void closeStage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // tfGioiTinhChuHo.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
    }
}
