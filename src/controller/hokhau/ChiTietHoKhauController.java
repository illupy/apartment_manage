package controller.hokhau;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.HoKhauModel;
import models.NhanKhauModel;
import services.NhanKhauService;

public class ChiTietHoKhauController implements Initializable {

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

    private void loadNhanKhauData() {
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
}

