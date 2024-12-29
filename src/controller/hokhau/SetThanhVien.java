package controller.hokhau;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.QuanHeModel;
import services.QuanHeService;



public class SetThanhVien  extends controller.HomeController  {

    @FXML
    private TextField idField;

    @FXML
    private TextField relationField;

    private int maHo; // Lấy giá trị từ màn hình chi tiết hộ khẩu

    private ChiTietHoKhauController chiTietHoKhauController;
    public void setChiTietHoKhauController(ChiTietHoKhauController controller) {
        this.chiTietHoKhauController = controller;
    }
    
    
    private final QuanHeService quanHeService = new QuanHeService();

    // Phương thức này được gọi khi nút "Thêm thành viên" được nhấn
    @FXML
    private void addThanhVien(ActionEvent event) {
        String idThanhVienStr = idField.getText().trim();
        String quanHe = relationField.getText().trim();

        if (idThanhVienStr.isEmpty() || quanHe.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        try {
            int idThanhVien = Integer.parseInt(idThanhVienStr);
            QuanHeModel quanHeModel = new QuanHeModel(maHo, idThanhVien, quanHe);

            if (quanHeService.add(quanHeModel)) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Thêm thành viên thành công!");
                idField.clear();
                relationField.clear();
                
                // Làm mới dữ liệu trong TableView của ChiTietHoKhauController
                if (chiTietHoKhauController != null) {
                    chiTietHoKhauController.loadNhanKhauData();
                }
                
                // Đóng cửa sổ thêm thành viên
                Stage stage = (Stage) idField.getScene().getWindow();  // Lấy cửa sổ hiện tại
                stage.close();
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("ID thành viên không tồn tại")) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "ID thành viên không tồn tại!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "ID thành viên phải là số nguyên!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }


    // Phương thức này được gọi khi nút "Xóa thành viên" được nhấn
    @FXML
    private void deleteThanhVien(ActionEvent event) {
        String idThanhVienStr = idField.getText().trim();

        if (idThanhVienStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập ID thành viên để xóa!");
            return;
        }

        try {
            int idThanhVien = Integer.parseInt(idThanhVienStr);

            if (quanHeService.del(maHo, idThanhVien)) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Xóa thành viên thành công!");
                idField.clear();
            } else {
                showAlert(Alert.AlertType.ERROR, "Thất bại", "Không thể xóa thành viên. Vui lòng thử lại!");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "ID thành viên phải là số nguyên!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    // Nhận mã hộ từ màn hình chi tiết hộ khẩu
    public void setMaHo(int maHo) {
        this.maHo = maHo;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
