package controller.thongke;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import models.HoKhauModel;
import models.KhoanThuModel;
import services.ThongKeService;
import application.FormatMoneyNumber;

public class ThongKeChiTietTheoHo extends controller.HomeController implements Initializable {

	@FXML
	private Text title;
	@FXML
	private TableColumn<KhoanThuModel, Integer> colMaKhoanThu;
	@FXML
	private TableColumn<KhoanThuModel, String> colTenKhoanThu;
	@FXML
	private TableColumn<KhoanThuModel, Double> colTongTien;
	@FXML
	private TableColumn<KhoanThuModel, Double> colSoTienDaThu;
	@FXML
	private TableView<KhoanThuModel> tvThongKeChiTietTheoHo;
	@FXML
	ComboBox<String> cbMonth;
	@FXML
	ComboBox<String> cbYear;
	
	private List<KhoanThuModel> listThongKeChiTietTheoHo;
	private ObservableList<KhoanThuModel> listValueTableView;

	private HoKhauModel hoKhauSelected = new HoKhauModel();	
	
	public HoKhauModel getHoKhauSelected() {
		return hoKhauSelected;
	}

	public void setHoKhauSelected(HoKhauModel hoKhau) {
		this.hoKhauSelected = hoKhau;
		title.setText("CHI TIẾT HỘ GIA ĐÌNH " + hoKhauSelected.getTenChuHo());
	}

	public Text getTitle() {
		return title;
	}

	public void setTitle(Text title) {
		this.title = title;
	}
	
	public void importTableData() throws ClassNotFoundException, SQLException {
		listThongKeChiTietTheoHo = new ThongKeService().getHouseholdDetailStats(hoKhauSelected.getMaHo());
		listValueTableView = FXCollections.observableArrayList(listThongKeChiTietTheoHo);
		
		if (hoKhauSelected == null || hoKhauSelected.getMaHo() == 0) {
	        return; // Không làm gì nếu hoKhauSelected chưa được thiết lập
	    }
		
		if (listThongKeChiTietTheoHo.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Thông báo");
			alert.setHeaderText(null);
			alert.setContentText("Không có dữ liệu thống kê.");
			alert.showAndWait();
			return;
		}
		
		colMaKhoanThu.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, Integer>("maKhoanThu"));
		colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("tenKhoanThu"));
		colTongTien.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, Double>("soTien"));
		colSoTienDaThu.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, Double>("SoTienDaThu"));
		
		FormatMoneyNumber.applyCurrencyFormat(colTongTien);
		FormatMoneyNumber.applyCurrencyFormat(colSoTienDaThu);
		tvThongKeChiTietTheoHo.setItems(listValueTableView);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Thêm danh sách tháng (1 đến 12) vào cbMonth
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
