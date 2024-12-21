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
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.KhoanThuModel;
import services.ThongKeService;

public class ThongKeController extends controller.HomeController implements Initializable {
	@FXML
	TableColumn<KhoanThuModel, String> colTenPhi;
	@FXML
	TableColumn<KhoanThuModel, String> colTongSoTien;
	@FXML
	TableView<KhoanThuModel> tvThongKe;
	@FXML
	ComboBox<String> cbChooseSearch;
	@FXML
	private TableColumn<KhoanThuModel, Void> colAction;

	private ObservableList<KhoanThuModel> listValueTableView;
	private List<KhoanThuModel> listKhoanThu;

	public void showThongKe() throws ClassNotFoundException, SQLException {
		listKhoanThu = new ThongKeService().getFeeStats();
		listValueTableView = FXCollections.observableArrayList(listKhoanThu);

		colTenPhi.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("tenKhoanThu"));
		colTongSoTien.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("soTien"));

		tvThongKe.setItems(listValueTableView);
		// thiet lap gia tri cho combobox
		ObservableList<String> listComboBox = FXCollections.observableArrayList("Bắt buộc đóng", "Ủng hộ");
		cbChooseSearch.setValue("Thống kê theo");
		cbChooseSearch.setItems(listComboBox);
	}

	public void loc() {
		ObservableList<KhoanThuModel> listValueTableView_tmp = null;

		List<KhoanThuModel> listKhoanThuBatBuoc = new ArrayList<>();
		List<KhoanThuModel> listKhoanThuTuNguyen = new ArrayList<>();
		for (KhoanThuModel khoanThuModel : listKhoanThu) {
			if (khoanThuModel.getLoaiKhoanThu() == 0) {
				listKhoanThuTuNguyen.add(khoanThuModel);
			} else {
				listKhoanThuBatBuoc.add(khoanThuModel);
			}
		}

		// lay lua chon tim kiem cua khach hang
		SingleSelectionModel<String> typeSearch = cbChooseSearch.getSelectionModel();
		String typeSearchString = typeSearch.getSelectedItem();

		switch (typeSearchString) {
		case "Tất cả":
			tvThongKe.setItems(listValueTableView);
			break;
		case "Bắt buộc đóng":
			listValueTableView_tmp = FXCollections.observableArrayList(listKhoanThuBatBuoc);
			tvThongKe.setItems(listValueTableView_tmp);
			break;
		case "Ủng hộ":
			listValueTableView_tmp = FXCollections.observableArrayList(listKhoanThuTuNguyen);
			tvThongKe.setItems(listValueTableView_tmp);
			break;
		default:
			break;
		}
	}

	public KhoanThuModel thongkeKhoanThu() throws IOException, ClassNotFoundException, SQLException {
		KhoanThuModel khoanThuModel = tvThongKe.getSelectionModel().getSelectedItem();
		return khoanThuModel;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			showThongKe();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
