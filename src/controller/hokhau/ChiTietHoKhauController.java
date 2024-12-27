package controller.hokhau;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.HoKhauModel;
import models.NhanKhauModel;
import services.NhanKhauService;
import services.QuanHeService;

public class ChiTietHoKhauController extends controller.HomeController implements Initializable {

    @FXML
    private TableColumn<NhanKhauModel, Integer> colId;
    @FXML
    private TableColumn<NhanKhauModel, String> colTen;
    @FXML
    private TableColumn<NhanKhauModel, String> colQuanHe; 

    @FXML
    private TableView<NhanKhauModel> tvThongKe;

    private ObservableList<NhanKhauModel> listValueTableView = FXCollections.observableArrayList();
    private HoKhauModel selectedHoKhau;

    private final NhanKhauService nhanKhauService = new NhanKhauService(); 

    public void setHoKhauModel(HoKhauModel hoKhauModel) {
        this.selectedHoKhau = hoKhauModel;
        loadNhanKhauData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
    }

    private void initializeTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTen.setCellValueFactory(new PropertyValueFactory<>("ten"));
        colQuanHe.setCellValueFactory(new PropertyValueFactory<>("quanHeChuHo")); 
    }

    public void loadNhanKhauData() {
        if (selectedHoKhau == null) return;

        try {
            List<NhanKhauModel> nhankhauModel = nhanKhauService.getNhanKhauByHoKhau(selectedHoKhau);
            listValueTableView.setAll(nhankhauModel);
            tvThongKe.setItems(listValueTableView);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showErrorAlert("Không thể tải dữ liệu nhân khẩu.");
        }
    }

    private void showErrorAlert(String message) {
        System.err.println("Error: " + message); 
    }

    // Hàm xử lý khi nhấn nút "Thêm thành viên"
    @FXML
    private void handleThemThanhVien() {
        if (selectedHoKhau == null) {
            showErrorAlert("Vui lòng chọn hộ khẩu để thêm thành viên.");
            return;
        }

        try {
            // Mở cửa sổ ThemThanhVien.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/hokhau/addthanhvien.fxml"));
            Parent root = loader.load();

            // Lấy controller của ThemThanhVien.fxml
            SetThanhVien setThanhVienController = loader.getController();

            // Truyền mã hộ khẩu và controller vào SetThanhVien
            setThanhVienController.setMaHo(selectedHoKhau.getMaHo());
            setThanhVienController.setChiTietHoKhauController(this); // Truyền controller hiện tại vào

            // Mở cửa sổ mới
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Thêm Thành Viên");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Không thể mở cửa sổ thêm thành viên.");
        }
    }

    @FXML
    private void handleXoaThanhVien() {
        // Lấy thành viên được chọn trong TableView
        NhanKhauModel selectedMember = tvThongKe.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
            showErrorAlert("Vui lòng chọn thành viên để xóa.");
            return;
        }

        // Hiển thị hộp thoại xác nhận
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa thành viên này?");

        // Nếu người dùng chọn OK thì tiến hành xóa
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                QuanHeService quanHeService = new QuanHeService();  // Tạo đối tượng QuanHeService

                try {
                    // Gọi phương thức del từ QuanHeService để xóa quan hệ
                    boolean isDeleted = quanHeService.del(selectedHoKhau.getMaHo(), selectedMember.getId());

                    if (isDeleted) {
                        // Nếu xóa thành công, cập nhật lại danh sách thành viên trong TableView
                        listValueTableView.remove(selectedMember);
                        tvThongKe.setItems(listValueTableView);
                        showInfoAlert("Xóa thành viên thành công!");
                    } else {
                        showErrorAlert("Không thể xóa thành viên này. Vui lòng thử lại!");
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    showErrorAlert("Đã xảy ra lỗi khi xóa thành viên.");
                    e.printStackTrace();
                }
            }
        });
    }
    


    // Thông báo thành công
    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
