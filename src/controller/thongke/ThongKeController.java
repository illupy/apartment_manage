package controller.thongke;

import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import models.KhoanThuModel;
import models.NopTienModel;
import services.KhoanThuService;
import services.NopTienService;
import services.ThongKeService;

public class ThongKeController<ThongKeController> extends controller.HomeController implements Initializable {
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

		colAction.setCellFactory(param -> new TableCell<KhoanThuModel, Void>() {
			private final HBox container = new HBox(8);
			private final Button btn1 = new Button("Các hộ chưa nộp");
			private final Button btn2 = new Button("Các hộ đã nộp");
			{
				btn1.setOnAction(event -> {
					goThongKe2();
				});

				btn2.setOnAction(event -> {
					goThongKe3();
				});

				container.setAlignment(Pos.CENTER);
				container.getChildren().addAll(btn1, btn2);
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(container);
				}
			}
		});

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

	private double calculateTotalAmount(int maKhoanThu) {
		List<NopTienModel> listNopTien;
		try {
			listNopTien = new NopTienService().getListNopTien();
			return listNopTien.stream().filter(nopTien -> nopTien.getMaKhoanThu() == maKhoanThu)
					.mapToDouble(NopTienModel::getSoTien).sum();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return 0.0; // Handle the exception appropriately in your application
		}
	}

	@FXML

	private void goThongKe2() {

		KhoanThuModel selectedKhoanThuModel = tvThongKe.getSelectionModel().getSelectedItem();

		if (selectedKhoanThuModel != null) {
			try {

				ThongKeChuaNopTien thongKeChuaNopTien = new ThongKeChuaNopTien();
				thongKeChuaNopTien.setKhoanThuModel(selectedKhoanThuModel);

				// Load ThongKe2 FXML
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/thongke/ThongKeChuaNopTien.fxml"));
				loader.setController(thongKeChuaNopTien);
				Parent root = loader.load();

				// Show ThongKe2 stage
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void goThongKe3() {

		KhoanThuModel selectedKhoanThuModel = tvThongKe.getSelectionModel().getSelectedItem();

		if (selectedKhoanThuModel != null) {
			try {

				ThongKeDaNopTien thongKeDaNopTien = new ThongKeDaNopTien();
				((ThongKeDaNopTien) thongKeDaNopTien).setkhoanThuModel(selectedKhoanThuModel);

				// Load ThongKe2 FXML
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/thongke/ThongKeNopTien.fxml"));
				loader.setController(thongKeDaNopTien);
				Parent root = loader.load();

				// Show ThongKe2 stage
				Stage stage = new Stage();
				stage.setScene(new Scene(root));
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
