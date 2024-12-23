package controller.hokhau;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.ChuHoModel;
import models.HoKhauModel;
import models.NhanKhauModel;
import services.ChuHoService;
import services.HoKhauService;
import services.NhanKhauService;

public class UpdateHoKhau{
	@FXML
	private TextField tfSoThanhVien;
	@FXML
	private TextField tfTenChuHo;
	@FXML
	private TextField tfSoPhong;
	@FXML
	private TextField tfSoOto;
	@FXML
	private TextField tfSoXeMay;
	@FXML
	private TextField tfMaHo;

	private HoKhauModel hoKhauModel;

	private Map<Integer, String> mapIDToTenNhanKhau = new TreeMap<>();

	public void setHoKhauModel(HoKhauModel hoKhauModel) throws ClassNotFoundException, SQLException {
		this.hoKhauModel = hoKhauModel;
		initializeDropdownData();
		populateFields();
	}

	private void initializeDropdownData() throws ClassNotFoundException, SQLException {
		Map<Integer, Integer> mapMahoToID = new TreeMap<>();
		List<ChuHoModel> listChuHo = new ChuHoService().getListChuHo();
		listChuHo.forEach(chuho -> mapMahoToID.put(chuho.getMaHo(), chuho.getIdChuHo()));

		List<NhanKhauModel> listNhanKhau = new NhanKhauService().getListNhanKhau(null);
		listNhanKhau.forEach(nhankhau -> {
			mapIDToTenNhanKhau.put(nhankhau.getId(), nhankhau.getTen());
		});
	}

	private void populateFields() {
		tfMaHo.setText(String.valueOf(hoKhauModel.getMaHo()));
		tfSoThanhVien.setText(String.valueOf(hoKhauModel.getSoThanhvien()));
		tfSoPhong.setText(hoKhauModel.getSoPhong());
		tfSoOto.setText(String.valueOf(hoKhauModel.getSoOto() != null ? hoKhauModel.getSoOto() : 0));
	    tfSoXeMay.setText(String.valueOf(hoKhauModel.getSoXeMay() != null ? hoKhauModel.getSoXeMay() : 0));
	    
	    int idChuHo = getIdChuHoByMaHo(hoKhauModel.getMaHo());
	    tfTenChuHo.setText(mapIDToTenNhanKhau.getOrDefault(idChuHo, "Không xác định"));
	}

	private int getIdChuHoByMaHo(int maHo) {
		for (ChuHoModel chuHo : new ChuHoService().getListChuHo()) {
			if (chuHo.getMaHo() == maHo) {
				return chuHo.getIdChuHo();
			}
		}
		return -1; // Handle not found case
	}

	@FXML
	public void updateHoKhau(ActionEvent event) {
	    try {
	        if (!isRoomNumberValid()) {
	            return;
	        }

	        String soPhong = tfSoPhong.getText();
	        int soOto = tfSoOto.getText().isEmpty() ? 0 : Integer.parseInt(tfSoOto.getText());
	        int soXeMay = tfSoXeMay.getText().isEmpty() ? 0 : Integer.parseInt(tfSoXeMay.getText());

	        new HoKhauService().update(hoKhauModel.getMaHo(), soPhong, soOto, soXeMay);
	        closeCurrentStage(event);

	    } catch (ClassNotFoundException | SQLException ex) {
	        showAlert("Đã xảy ra lỗi khi cập nhật hộ khẩu: " + ex.getMessage());
	    } catch (NumberFormatException ex) {
	        showAlert("Số ô tô và số xe máy phải là số nguyên hợp lệ!");
	    }
	}

	private boolean isRoomNumberValid() {
	    if (tfSoPhong.getText().isEmpty() || tfSoPhong.getText().length() > 10) {
	        showAlert("Hãy nhập vào số phòng hợp lệ (không được rỗng và tối đa 10 ký tự)!");
	        return false;
	    }
	    return true;
	}

	private void showAlert(String message) {
	    Alert alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
	    alert.setHeaderText(null);
	    alert.showAndWait();
	}

	private void closeCurrentStage(ActionEvent event) {
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.close();
	}
}