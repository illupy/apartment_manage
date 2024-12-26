package controller.khoanthu;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import models.KhoanThuModel;
import models.NopTienModel;
import services.KhoanThuService;
import services.NopTienService;

public class KhoanThuController extends controller.HomeController implements Initializable {
	@FXML
	private TableView<KhoanThuModel> tvKhoanPhi;
	@FXML
	private TableColumn<KhoanThuModel, String> colIDKhoanThu;
	@FXML
	private TableColumn<KhoanThuModel, String> colTenKhoanThu;
	@FXML
	private TableColumn<KhoanThuModel, String> colMaHo;
	@FXML
	private TableColumn<KhoanThuModel, String> colSoTien;
	@FXML
	private TableColumn<KhoanThuModel, String> colLoaiKhoanThu;
	@FXML
	private TableColumn<KhoanThuModel, String> colNgayBatDau;
	@FXML
	private TableColumn<KhoanThuModel, String> colNgayKetThuc;
	@FXML
	private TableColumn<KhoanThuModel, Void> colAction;
	@FXML
	private TextField tfSearch;
	@FXML
	private ComboBox<String> cbChooseSearch;
	private List<KhoanThuModel> listKhoanThu;
	private ObservableList<KhoanThuModel> listValueTableView;

	public void showKhoanThu() throws ClassNotFoundException, SQLException {
		listKhoanThu = new KhoanThuService().getListKhoanThu();
		listValueTableView = FXCollections.observableArrayList(listKhoanThu);

		// thiet lap cac cot cho table views
		colIDKhoanThu.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("idKhoanThu"));
		colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("tenKhoanThu"));
		colMaHo.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("maHo"));
		colSoTien.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("soTien"));
		colLoaiKhoanThu.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("loaiKhoanThu"));
		colNgayBatDau.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("ngayBatDau"));
		colNgayKetThuc.setCellValueFactory(new PropertyValueFactory<KhoanThuModel, String>("ngayKetThuc"));

		colAction.setCellFactory(param -> new TableCell<KhoanThuModel, Void>() {
			private final HBox container = new HBox(8);
			private final Button daNopButton = new Button("Đã Nộp");

			{
				daNopButton.setOnAction(event -> {
					// Get the current row item
					KhoanThuModel khoanThu = getTableView().getItems().get(getIndex());

					try {
						// Now you can use the khoanThu object to handle the "Đã Nộp" action
						setDaNop(khoanThu);
					} catch (ClassNotFoundException | SQLException e) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Lỗi");
						alert.setHeaderText(null);
						alert.setContentText("Có lỗi xảy ra: " + e.getMessage());
						alert.showAndWait();
						e.printStackTrace();
					}
				});

				container.setAlignment(Pos.CENTER);
				container.getChildren().addAll(daNopButton);
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

		Map<Integer, String> mapLoaiKhoanThu = new TreeMap<>();
		mapLoaiKhoanThu.put(1, "Bắt buộc");
		mapLoaiKhoanThu.put(0, "Tự nguyện");

		try {
			colLoaiKhoanThu
					.setCellValueFactory((CellDataFeatures<KhoanThuModel, String> p) -> new ReadOnlyStringWrapper(
							mapLoaiKhoanThu.get(p.getValue().getLoaiKhoanThu())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		tvKhoanPhi.setItems(listValueTableView);

		// thiet lap gia tri cho combobox
		ObservableList<String> listComboBox = FXCollections.observableArrayList("Tên khoản thu", "Mã khoản thu",
				"Mã hộ");
		cbChooseSearch.setValue("Tên khoản thu");
		cbChooseSearch.setItems(listComboBox);
	}

	// Tim kiem khoan thu
	public void searchKhoanThu() {
		ObservableList<KhoanThuModel> listValueTableView_tmp = null;
		String keySearch = tfSearch.getText();

		// lay lua chon tim kiem cua khach hang
		SingleSelectionModel<String> typeSearch = cbChooseSearch.getSelectionModel();
		String typeSearchString = typeSearch.getSelectedItem();

		// tim kiem thong tin theo lua chon da lay ra
		switch (typeSearchString) {
		case "Tên khoản thu": {
			// neu khong nhap gi -> thong bao loi
			if (keySearch.length() == 0) {
				tvKhoanPhi.setItems(listValueTableView);
				Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào thông tin cần tìm kiếm!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				break;
			}

			List<KhoanThuModel> listKhoanThuTheoTen = new ArrayList<>();
			for (KhoanThuModel khoanThuModel : listKhoanThu) {
				if (khoanThuModel.getTenKhoanThu().contains(keySearch)) {
					listKhoanThuTheoTen.add(khoanThuModel);
				}
			}
			listValueTableView_tmp = FXCollections.observableArrayList(listKhoanThuTheoTen);
			tvKhoanPhi.setItems(listValueTableView_tmp);

			// neu khong tim thay thong tin can tim kiem -> thong bao toi nguoi dung khong
			if (listKhoanThuTheoTen.isEmpty()) {
				tvKhoanPhi.setItems(listValueTableView); // hien thi toan bo thong tin
				Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
			}
			break;
		}
		case "Mã hộ": {
			if (keySearch.length() == 0) {
				tvKhoanPhi.setItems(listValueTableView);
				Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào mã hộ cần tìm kiếm!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				break;
			}

			try {
				int maHoSearch = Integer.parseInt(keySearch);
				List<KhoanThuModel> listKhoanThuTheoMaHo = new ArrayList<>();

				for (KhoanThuModel khoanThu : listKhoanThu) {
					if (khoanThu.getMaHo() == maHoSearch) {
						listKhoanThuTheoMaHo.add(khoanThu);
					}
				}

				listValueTableView_tmp = FXCollections.observableArrayList(listKhoanThuTheoMaHo);
				tvKhoanPhi.setItems(listValueTableView_tmp);

				if (listKhoanThuTheoMaHo.isEmpty()) {
					tvKhoanPhi.setItems(listValueTableView);
					Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
					alert.setHeaderText(null);
					alert.showAndWait();
				}
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR, "Mã hộ phải là số!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				tvKhoanPhi.setItems(listValueTableView);
			}
			break;
		}
		default: { // truong hop con lai : tim theo ma khoan thu
			// neu khong nhap gi -> thong bao loi
			if (keySearch.length() == 0) {
				tvKhoanPhi.setItems(listValueTableView);
				Alert alert = new Alert(AlertType.INFORMATION, "Bạn cần nhập vào thông tin tìm kiếm!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				break;
			}

			// kiem tra thong tin tim kiem co hop le hay khong
			Pattern pattern = Pattern.compile("\\d{1,}");
			if (!pattern.matcher(keySearch).matches()) {
				Alert alert = new Alert(AlertType.WARNING, "Bạn phải nhập vào 1 số!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				return;
			}

			for (KhoanThuModel khoanThuModel : listKhoanThu) {
				if (khoanThuModel.getIdKhoanThu() == Integer.parseInt(keySearch)) {
					listValueTableView_tmp = FXCollections.observableArrayList(khoanThuModel);
					tvKhoanPhi.setItems(listValueTableView_tmp);
					return;
				}
			}

			// khong tim thay thong tin -> thong bao toi nguoi dung
			tvKhoanPhi.setItems(listValueTableView);
			Alert alert = new Alert(AlertType.WARNING, "Không tìm thấy thông tin!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		}
		}
	}

	@FXML
	void addKhoanThu(ActionEvent event) throws IOException {
		switchScene(event, "/views/khoanthu/AddKhoanThu.fxml");
	}

	public void delKhoanThu() throws ClassNotFoundException, SQLException {
		KhoanThuModel khoanThuModel = tvKhoanPhi.getSelectionModel().getSelectedItem();

		if (khoanThuModel == null) {
			Alert alert = new Alert(AlertType.WARNING, "Chọn khoản thu bạn muốn xóa!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING, "Bạn có chắc chắn muốn xóa khoản thu này!", ButtonType.YES,
					ButtonType.NO);
			alert.setHeaderText(null);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.NO) {
				return;
			} else {
				if (new KhoanThuService().del(khoanThuModel.getIdKhoanThu())) {
					Alert alert_success = new Alert(AlertType.INFORMATION, "Đã xóa khoản thu", ButtonType.OK);
					alert_success.setHeaderText(null);
					alert_success.showAndWait();
				};
			}
		}

		showKhoanThu();
	}

	@FXML
	void updateKhoanThu(ActionEvent event) throws IOException {
		KhoanThuModel khoanThu = tvKhoanPhi.getSelectionModel().getSelectedItem();

		if (khoanThu == null) {
			Alert alert = new Alert(AlertType.WARNING, "Chọn khoản thu muốn sửa!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING, "Bạn có chắc chắn muốn sửa khoản thu này?", ButtonType.OK,
					ButtonType.NO);
			alert.setHeaderText(null);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.NO) {
				return;
			} else {
				switchSceneWithKhoanThuData(event, "/views/khoanthu/UpdateKhoanThu.fxml", khoanThu);
			}

		}
	}

	@FXML
	void nopTien(ActionEvent event) throws IOException {
		switchScene(event, "/views/noptien/AddNopTien.fxml");
	}

	void setDaNop(KhoanThuModel khoanThu) throws ClassNotFoundException, SQLException {
		NopTienModel nopTien = new NopTienModel();
		Date today = new Date();
		nopTien.setSoTien(khoanThu.getSoTien());
		nopTien.setNgayThu(today);
		nopTien.setMaHo(khoanThu.getMaHo());
		nopTien.setIdKhoanThu(khoanThu.getIdKhoanThu());

		if (new NopTienService().add(nopTien)) {
			Alert alert = new Alert(AlertType.INFORMATION, "Đặt khoản thu là đã nộp thành công!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		}
	}

	@FXML
	void addLoaiKhoanThu(ActionEvent event) throws IOException {
		switchScene(event, "/views/khoanthu/AddLoaiKhoanThu.fxml");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			showKhoanThu();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
