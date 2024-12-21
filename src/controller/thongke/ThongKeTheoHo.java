package controller.thongke;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.HoKhauModel;

import services.ThongKeService;

public class ThongKeTheoHo extends controller.HomeController implements Initializable {
	@FXML
	TableColumn<HoKhauModel, Integer> colMaHo;
	@FXML
	TableColumn<HoKhauModel, String> colTenChuHo;
	@FXML
	TableColumn<HoKhauModel, Double> colTienCanDong;
	@FXML
	TableColumn<HoKhauModel, Double> colTienDaDong;
	@FXML
	TableView<HoKhauModel> tvThongKeTheoHo;
	@FXML
	ComboBox<String> cbMonth;
	@FXML
	ComboBox<String> cbYear;

	private List<HoKhauModel> listThongKeTheoHo;
	private ObservableList<HoKhauModel> listValueTableView;
	private List<HoKhauModel> listHomeStatsByMonthYear;

	public void importTableData() throws ClassNotFoundException, SQLException {
		listThongKeTheoHo = new ThongKeService().getHouseholdsStats();
		listValueTableView = FXCollections.observableArrayList(listThongKeTheoHo);

		if (listThongKeTheoHo.isEmpty()) {
			// Hiển thị thông báo khi không có dữ liệu
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText(null);
			alert.setContentText("Không có dữ liệu thống kê.");
			alert.showAndWait();
			return;
		}

		// Thiet lap cot
		colMaHo.setCellValueFactory(new PropertyValueFactory<HoKhauModel, Integer>("maHo"));
		colTenChuHo.setCellValueFactory(new PropertyValueFactory<HoKhauModel, String>("tenChuHo"));
		colTienCanDong.setCellValueFactory(new PropertyValueFactory<HoKhauModel, Double>("tienCanDong"));
		colTienDaDong.setCellValueFactory(new PropertyValueFactory<HoKhauModel, Double>("tienDaDong"));

		tvThongKeTheoHo.setItems(listValueTableView);
	}

	public void thongKeTheoThangNam() throws ClassNotFoundException, SQLException {
		if (cbMonth.getValue() == null) {
			// Hiển thị cảnh báo cho ComboBox tháng
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Thiếu thông tin");
			alert.setHeaderText(null);
			alert.setContentText("Vui lòng chọn tháng!");
			alert.showAndWait();
			return; // Dừng việc xử lý tiếp theo
		}

		// Kiểm tra ComboBox năm
		if (cbYear.getValue() == null) {
			// Hiển thị cảnh báo cho ComboBox năm
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Thiếu thông tin");
			alert.setHeaderText(null);
			alert.setContentText("Vui lòng chọn năm!");
			alert.showAndWait();
			return; // Dừng việc xử lý tiếp theo
		}
		int month = Integer.parseInt(cbMonth.getValue());
		int year = Integer.parseInt(cbYear.getValue());

		ObservableList<HoKhauModel> listValueTableView_tmp = null;

		listHomeStatsByMonthYear = new ThongKeService().getHouseholdsStatsByMonthYear(month, year);
		if (listHomeStatsByMonthYear.isEmpty()) {
			// Hiển thị thông báo khi không có dữ liệu
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText(null);
			alert.setContentText("Không có dữ liệu thống kê.");
			alert.showAndWait();
			return;
		}
		listValueTableView_tmp = FXCollections.observableArrayList(listHomeStatsByMonthYear);

		// Thiet lap cot
		colMaHo.setCellValueFactory(new PropertyValueFactory<HoKhauModel, Integer>("maHo"));
		colTenChuHo.setCellValueFactory(new PropertyValueFactory<HoKhauModel, String>("tenChuHo"));
		colTienCanDong.setCellValueFactory(new PropertyValueFactory<HoKhauModel, Double>("tienCanDong"));
		colTienDaDong.setCellValueFactory(new PropertyValueFactory<HoKhauModel, Double>("tienDaDong"));

		tvThongKeTheoHo.setItems(listValueTableView_tmp);
	}
	
	@FXML
	void xemChiTietTheoHo(ActionEvent event) throws IOException {
		HoKhauModel hoKhau = tvThongKeTheoHo.getSelectionModel().getSelectedItem();
		
		if (hoKhau == null) {
			Alert alert = new Alert(AlertType.WARNING, "Chọn hộ mmuốn xem thống kê chi tiết", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		} else {
			switchScene(event, "/views/thongke/ThongKeChiTietTheoHo.fxml");
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			importTableData();

			// Thêm danh sách tháng (1 đến 12) vào cbMonth
			ObservableList<String> months = FXCollections.observableArrayList();
			for (int i = 1; i <= 12; i++) {
				months.add(String.valueOf(i));
			}
			cbMonth.setItems(months);

			// Thêm danh sách năm (ví dụ: từ 2000 đến 2030) vào cbYear
			ObservableList<String> years = FXCollections.observableArrayList();
			for (int i = 2000; i <= 2030; i++) {
				years.add(String.valueOf(i));
			}
			cbYear.setItems(years);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
