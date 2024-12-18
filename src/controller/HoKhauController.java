package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import controller.hokhau.ChiTietHoKhauController;
import controller.hokhau.UpdateHoKhau;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.ChuHoModel;
import models.HoKhauModel;
import models.NhanKhauModel;
import models.QuanHeModel;
import services.ChuHoService;
import services.HoKhauService;
import services.NhanKhauService;
import services.QuanHeService;

public class HoKhauController extends HomeController implements Initializable {
	@FXML
	TableColumn<HoKhauModel, String> colMaHoKhau;
	@FXML
	TableColumn<HoKhauModel, String> colMaChuHo;
	@FXML
	TableColumn<HoKhauModel, String> colSoThanhVien;
	@FXML
	TableColumn<HoKhauModel, String> colDiaChi;
	@FXML
	TableView<HoKhauModel> tvHoKhau;
	@FXML
	TextField tfSearch;

	@FXML
	ComboBox<String> cbChooseSearch;

	ObservableList<HoKhauModel> listValueTableView;
	private List<HoKhauModel> listHoKhau;

	// Hien thi thong tin ho khau
	public void showHoKhau() throws ClassNotFoundException, SQLException {
		listHoKhau = new HoKhauService().getListHoKhau();
		listValueTableView = FXCollections.observableArrayList(listHoKhau);
		List<NhanKhauModel> listNhanKhau = new NhanKhauService().getListNhanKhau(null);
		List<ChuHoModel> listChuHo = new ChuHoService().getListChuHo();

		// Khởi tạo map
		Map<Integer, String> mapIdToTen = new HashMap<>();
		if (listNhanKhau != null) {
			listNhanKhau.forEach(nhankhau -> {
				mapIdToTen.put(nhankhau.getId(), nhankhau.getTen());
			});
		} else {
			System.out.println("Danh sách nhân khẩu rỗng hoặc null!");
		}

		Map<Integer, Integer> mapMahoToId = new HashMap<>();
		if (listChuHo != null) {
			listChuHo.forEach(chuho -> {
				mapMahoToId.put(chuho.getMaHo(), chuho.getIdChuHo());
			});
		} else {
			System.out.println("Danh sách chủ hộ rỗng hoặc null!");
		}

		// Đặt giá trị cho các cột
		colMaHoKhau.setCellValueFactory(new PropertyValueFactory<HoKhauModel, String>("maHo"));

		colMaChuHo.setCellValueFactory((CellDataFeatures<HoKhauModel, String> p) -> {
			Integer idChuHo = mapMahoToId.get(p.getValue().getMaHo());
			if (idChuHo == null) {
				System.out.println("Không tìm thấy MaHo: " + p.getValue().getMaHo());
				return new ReadOnlyStringWrapper("Không xác định");
			}

			String tenChuHo = mapIdToTen.get(idChuHo);
			if (tenChuHo == null) {
				System.out.println("Không tìm thấy idChuHo: " + idChuHo);
				return new ReadOnlyStringWrapper("Không xác định");
			}

			return new ReadOnlyStringWrapper(tenChuHo);
		});

		colSoThanhVien.setCellValueFactory(new PropertyValueFactory<HoKhauModel, String>("soThanhvien"));
		colDiaChi.setCellValueFactory(new PropertyValueFactory<HoKhauModel, String>("diaChi"));

		// Cập nhật dữ liệu cho bảng
		tvHoKhau.setItems(listValueTableView);

		// Thiet lap Combo box
		ObservableList<String> listComboBox = FXCollections.observableArrayList("Mã hộ", "Tên chủ hộ", "Địa chỉ");
		cbChooseSearch.setValue("Mã hộ");
		cbChooseSearch.setItems(listComboBox);
	}

	public void viewDetailHoKhau() throws IOException {
		HoKhauModel selectedHoKhau = tvHoKhau.getSelectionModel().getSelectedItem();
		if (selectedHoKhau == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING, "Hãy chọn hộ khẩu để xem chi tiết!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/hokhau/ChiTietHoKhau.fxml"));
		Parent root = loader.load();

		ChiTietHoKhauController chiTietController = loader.getController();
		chiTietController.setHoKhauModel(selectedHoKhau);

		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.setTitle("Chi Tiết Hộ Khẩu");
		stage.show();
	}

	public void addHoKhau(ActionEvent event) throws IOException {
		switchScene(event, "/views/hokhau/addhokhau.fxml");
	}

	public void delHoKhau() throws ClassNotFoundException, SQLException {
		HoKhauModel hoKhauModel = tvHoKhau.getSelectionModel().getSelectedItem();

		if (hoKhauModel == null) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy chọn hộ khẩu bạn muốn xóa!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING, "Khi xóa hộ khẩu, tất cả thành viên trong hộ đều sẽ bị xóa!",
					ButtonType.YES, ButtonType.NO);
			alert.setHeaderText("Bạn có chắc chắn muốn xóa hộ khẩu này");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.NO) {
				return;
			} else {
				/// tao map anh xa gia tri Id sang maHo
				Map<Integer, Integer> mapIdToMaho = new HashMap<>();
				List<QuanHeModel> listQuanHe = new QuanHeService().getListQuanHe();
				listQuanHe.forEach(quanhe -> {
					mapIdToMaho.put(quanhe.getIdThanhVien(), quanhe.getMaHo());
				});

				// xoa toan bo nhan khau trong ho khau
				int idHoKhauDel = hoKhauModel.getMaHo(); // lay ra ma ho de so sanh
				List<NhanKhauModel> listNhanKhauModels = new NhanKhauService().getListNhanKhau(null);
				listNhanKhauModels.stream().filter(nhankhau -> mapIdToMaho.get(nhankhau.getId()) == idHoKhauDel)
						.forEach(nk -> {
							try {
								new NhanKhauService().delete(nk.getId());
							} catch (ClassNotFoundException | SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});

				// xoa ho khau
				new HoKhauService().delete(idHoKhauDel);
			}
		}

		showHoKhau();
	}

	public void searchHoKhau() throws ClassNotFoundException, SQLException {
		ObservableList<HoKhauModel> listValueTableView_tmp = null;
		String keySearch = tfSearch.getText();

		// lay lua chon tim kiem cua khach hang
		SingleSelectionModel<String> typeSearch = cbChooseSearch.getSelectionModel();
		String typeSearchString = tfSearch.getText();

		// tim kiem thong tin theo lua chon da lay ra
		switch (typeSearchString) {
		case "Ten chu ho": {
			if (keySearch.length() == 0) {
				tvHoKhau.setItems(listValueTableView);
				Alert alert = new Alert(AlertType.WARNING, "Hay nhap thong tin tim kiem", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				break;
			}

			Map<Integer, Integer> mapMahoToId = new HashMap<>();
			List<ChuHoModel> listChuHo = new ChuHoService().getListChuHo();
			listChuHo.stream().forEach(chuho -> {
				mapMahoToId.put(chuho.getMaHo(), chuho.getIdChuHo());
			});
			Map<Integer, String> mapIdToTen = new HashMap<>();
			List<NhanKhauModel> listNhanKhau = new NhanKhauService().getListNhanKhau(null);
			listNhanKhau.stream().forEach(nhankhau -> {
				mapIdToTen.put(nhankhau.getId(), nhankhau.getTen());
			});

			int index = 0;
			List<HoKhauModel> listHoKhauModelsSearch = new ArrayList<>();
			for (HoKhauModel hoKhauModel : listHoKhau) {
				if (mapIdToTen.get(mapMahoToId.get(hoKhauModel.getMaHo())).contains(keySearch)) {
					listHoKhauModelsSearch.add(hoKhauModel);
					index++;
				}
			}
			listValueTableView_tmp = FXCollections.observableArrayList(listHoKhauModelsSearch);
			tvHoKhau.setItems(listValueTableView_tmp);

			// neu khong tim thay thong tin can tim kiem -> thong bao toi nguoi dung khong
			// tim thay
			if (index == 0) {
				tvHoKhau.setItems(listValueTableView); // hien thi toan bo thong tin
				Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
			}
			break;
		}
		case "Địa chỉ": {
			// neu khong nhap gi -> thong bao loi
			if (keySearch.length() == 0) {
				tvHoKhau.setItems(listValueTableView);
				Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào thông tin cần tìm kiếm!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				break;
			}

			int index = 0;
			List<HoKhauModel> listHoKhau_tmp = new ArrayList<>();
			for (HoKhauModel hoKhauModel : listHoKhau) {
				if (hoKhauModel.getDiaChi().contains(keySearch)) {
					listHoKhau_tmp.add(hoKhauModel);
					index++;
				}
			}
			listValueTableView_tmp = FXCollections.observableArrayList(listHoKhau_tmp);
			tvHoKhau.setItems(listValueTableView_tmp);

			// neu khong tim thay thong tin tim kiem -> thong bao toi nguoi dung
			if (index == 0) {
				tvHoKhau.setItems(listValueTableView); // hien thi toan bo thong tin
				Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
			}
			break;
		}
		default: { // truong hop con lai : tim theo id
			// neu khong nhap gi -> thong bao loi
			if (keySearch.length() == 0) {
				tvHoKhau.setItems(listValueTableView);
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

			for (HoKhauModel hoKhauModel : listHoKhau) {
				if (hoKhauModel.getMaHo() == Integer.parseInt(keySearch)) {
					listValueTableView_tmp = FXCollections.observableArrayList(hoKhauModel);
					tvHoKhau.setItems(listValueTableView_tmp);
					return;
				}
			}

			// khong tim thay thong tin -> thong bao toi nguoi dung
			tvHoKhau.setItems(listValueTableView);
			Alert alert = new Alert(AlertType.WARNING, "Không tìm thấy thông tin!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		}
		}
	}

	public void updateHoKhau() throws ClassNotFoundException, SQLException, IOException {
		// lay ra nhan khau can update
		HoKhauModel hoKhauModel = tvHoKhau.getSelectionModel().getSelectedItem();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/hokhau/UpdateHoKhau.fxml"));
		Parent home = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(home, 800, 600));
		UpdateHoKhau updateHoKhau = loader.getController();

		// bat loi truong hop khong hop le
		if (updateHoKhau == null)
			return;
		if (hoKhauModel == null) {
			Alert alert = new Alert(AlertType.WARNING, "Chọn hộ khẩu cần sửa !", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}
		updateHoKhau.setHoKhauModel(hoKhauModel);

		stage.setResizable(false);
		stage.showAndWait();
		showHoKhau();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			showHoKhau();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}