package controller.hokhau;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import services.QuanHeService;

public class UpdateHoKhau extends controller.HomeController implements Initializable {

    @FXML
    private TextField tfSoPhong;
    @FXML
    private TextField tfSoOto;
    @FXML
    private TextField tfSoXeMay;
    @FXML
    private TextField tfIdChuHo; // ID Chủ Hộ sẽ nhập vào đây

    private HoKhauModel hoKhauModel;

    public void setHoKhauModel(HoKhauModel hoKhauModel) throws ClassNotFoundException, SQLException {
        this.hoKhauModel = hoKhauModel;
        populateFields();
    }

    private void populateFields() {
        tfSoPhong.setText(hoKhauModel.getSoPhong());
        tfSoOto.setText(String.valueOf(hoKhauModel.getSoOto() != null ? hoKhauModel.getSoOto() : 0));
        tfSoXeMay.setText(String.valueOf(hoKhauModel.getSoXeMay() != null ? hoKhauModel.getSoXeMay() : 0));
        
        int idChuHo = getIdChuHoByMaHo(hoKhauModel.getMaHo());
        tfIdChuHo.setText(String.valueOf(idChuHo));
    }

    private int getIdChuHoByMaHo(int maHo) {
        // Lấy ID Chủ Hộ từ mã hộ
        for (ChuHoModel chuHo : new ChuHoService().getListChuHo()) {
            if (chuHo.getMaHo() == maHo) {
                return chuHo.getIdChuHo();
            }
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }

    @FXML
    public void updateHoKhau(ActionEvent event) {
        try {
            // Kiểm tra tính hợp lệ của số phòng và chủ hộ
            if (!isRoomNumberValid() || !isChuHoValid()) {
                return;
            }

            String soPhong = tfSoPhong.getText();
            int soOto = tfSoOto.getText().isEmpty() ? 0 : Integer.parseInt(tfSoOto.getText());
            int soXeMay = tfSoXeMay.getText().isEmpty() ? 0 : Integer.parseInt(tfSoXeMay.getText());
            int idChuHo = Integer.parseInt(tfIdChuHo.getText());  // Lấy ID của chủ hộ từ trường tfIdChuHo

            // Kiểm tra SoPhong trong bảng phong
            if (!isSoPhongExist(soPhong)) {
                showAlert("Số phòng không tồn tại.");
                return;
            }

            // Kiểm tra IDChuHo trong bảng nhan_khau
            if (!isChuHoExist(idChuHo)) {
                showAlert("ID chủ hộ không tồn tại.");
                return;
            }

            // Cập nhật thông tin trong bảng ho_khau
            new HoKhauService().update(hoKhauModel.getMaHo(), soPhong, soOto, soXeMay);

            // Cập nhật thông tin chủ hộ trong bảng chu_ho
            new ChuHoService().updateChuHo(hoKhauModel.getMaHo(), idChuHo);

            // Cập nhật quan hệ trong bảng quan_he
            new QuanHeService().updateChuHoInQuanHe(hoKhauModel.getMaHo(), idChuHo);

            // Cập nhật lại giao diện
            showAlert("Cập nhật hộ khẩu thành công!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

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

    private boolean isChuHoValid() {
        if (tfIdChuHo.getText().isEmpty()) {
            showAlert("ID Chủ Hộ không được để trống!");
            return false;
        }
        return true;
    }

    private boolean isSoPhongExist(String soPhong) throws ClassNotFoundException, SQLException {
        // Kiểm tra số phòng trong bảng phong
        List<String> listSoPhong = new HoKhauService().getAllSoPhong();
        return listSoPhong.contains(soPhong);
    }

    private boolean isChuHoExist(int idChuHo) throws ClassNotFoundException, SQLException {
        // Kiểm tra ID Chủ Hộ có tồn tại trong bảng nhan_khau
        List<NhanKhauModel> listNhanKhau = new NhanKhauService().getListNhanKhau(null);
        return listNhanKhau.stream().anyMatch(nhankhau -> nhankhau.getId() == idChuHo);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Initialize if necessary (e.g., load dropdowns, initial setup)
    }
}
