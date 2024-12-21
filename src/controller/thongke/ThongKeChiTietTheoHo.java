package controller.thongke;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.HoKhauModel;
import models.KhoanThuModel;
import services.ThongKeService;

public class ThongKeChiTietTheoHo extends controller.HomeController implements Initializable {

	@FXML
	private TableColumn<HoKhauModel, Integer> colMaHo;
	@FXML
	private TableColumn<HoKhauModel, String> colTenChuHo;
	@FXML
	private TableColumn<HoKhauModel, Integer> colSoThanhVien;
	@FXML
	private TableColumn<HoKhauModel, String> colDiaChi;
	@FXML
	private TableView<HoKhauModel> tvThongKe;
	@FXML
	ComboBox<String> cbMonth;
	@FXML
	ComboBox<String> cbYear;

	private ObservableList<HoKhauModel> listValueTableView;

	private KhoanThuModel selectedKhoanThu;

	// Default constructor
	public ThongKeChiTietTheoHo() {
	}

	// Method to set the selected KhoanThuModel
	public void setKhoanThuModel(KhoanThuModel khoanThuModel) {
		this.selectedKhoanThu = khoanThuModel;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
	}

}
