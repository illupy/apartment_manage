package controller.thongke;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.HoKhauModel;
import models.KhoanThuModel;
import models.LoaiKhoanThuModel;
import services.ThongKeService;

public class ThongKeController extends controller.HomeController implements Initializable {
	@FXML
	TableColumn<LoaiKhoanThuModel, Integer> colMaKhoanThu;
	@FXML
	TableColumn<LoaiKhoanThuModel, String> colTenKhoanThu;
	@FXML
	TableColumn<LoaiKhoanThuModel, Double> colTongSoTienCanThu;
	@FXML
	TableColumn<LoaiKhoanThuModel, Double> colTongSoTienDaThu;
	@FXML
	TableView<LoaiKhoanThuModel> tvThongKe;
	@FXML
	ComboBox<Integer> cbMonth;
	@FXML
	ComboBox<Integer> cbYear;
	@FXML
	ComboBox<String> cbChooseSearch;
	@FXML
	private TableColumn<KhoanThuModel, Void> colAction;

	private ObservableList<LoaiKhoanThuModel> listValueTableView;
	private List<LoaiKhoanThuModel> listLoaiKhoanThu;
	private List<LoaiKhoanThuModel> listFeeStatsByMonthYear;

	public void showThongKe() throws ClassNotFoundException, SQLException {
		listLoaiKhoanThu = new ThongKeService().getFeeStats();
		listValueTableView = FXCollections.observableArrayList(listLoaiKhoanThu);
		
		if (listValueTableView.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText(null);
			alert.setContentText("Không có dữ liệu thống kê.");
			alert.showAndWait();
			return;
		}

		colMaKhoanThu.setCellValueFactory(new PropertyValueFactory<LoaiKhoanThuModel, Integer>("maKhoanThu"));
		colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<LoaiKhoanThuModel, String>("tenKhoanThu"));
		colTongSoTienCanThu
				.setCellValueFactory(new PropertyValueFactory<LoaiKhoanThuModel, Double>("tongSoTienCanThu"));
		colTongSoTienDaThu.setCellValueFactory(new PropertyValueFactory<LoaiKhoanThuModel, Double>("tongSoTienDaThu"));

		tvThongKe.setItems(listValueTableView);
	}

	public void thongKeTheoThangNam() throws ClassNotFoundException, SQLException {
		if (cbMonth.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Thiếu thông tin");
			alert.setHeaderText(null);
			alert.setContentText("Vui lòng chọn tháng!");
			alert.showAndWait();
			return; 
		}

		// Kiểm tra ComboBox năm
		if (cbYear.getValue() == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Thiếu thông tin");
			alert.setHeaderText(null);
			alert.setContentText("Vui lòng chọn năm!");
			alert.showAndWait();
			return;
		}
		int month = cbMonth.getValue();
		int year = cbYear.getValue();

		ObservableList<LoaiKhoanThuModel> listValueTableView_tmp = null;

		listFeeStatsByMonthYear = new ThongKeService().getFeeStatsByMonthYear(month, year);
		if (listFeeStatsByMonthYear.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText(null);
			alert.setContentText("Không có dữ liệu thống kê.");
			alert.showAndWait();
			return;
		}
		listValueTableView_tmp = FXCollections.observableArrayList(listFeeStatsByMonthYear);

		colMaKhoanThu.setCellValueFactory(new PropertyValueFactory<LoaiKhoanThuModel, Integer>("maKhoanThu"));
		colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<LoaiKhoanThuModel, String>("tenKhoanThu"));
		colTongSoTienCanThu
				.setCellValueFactory(new PropertyValueFactory<LoaiKhoanThuModel, Double>("tongSoTienCanThu"));
		colTongSoTienDaThu.setCellValueFactory(new PropertyValueFactory<LoaiKhoanThuModel, Double>("tongSoTienDaThu"));

		tvThongKe.setItems(listValueTableView_tmp);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			showThongKe();

			// Thêm danh sách tháng (1 đến 12) vào cbMonth
			ObservableList<Integer> months = FXCollections.observableArrayList();
			for (int i = 1; i <= 12; i++) {
				months.add(i);
			}
			cbMonth.setItems(months);

			// Thêm danh sách năm (ví dụ: từ 2000 đến 2030) vào cbYear
			ObservableList<Integer> years = FXCollections.observableArrayList();
			for (int i = 2000; i <= 2030; i++) {
				years.add(i);
			}
			cbYear.setItems(years);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
