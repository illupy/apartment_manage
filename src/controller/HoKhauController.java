package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import controller.hokhau.AddHoKhau;
import controller.hokhau.ChiTietHoKhauController;
import controller.hokhau.UpdateHoKhau;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import services.ChuHoService;
import services.HoKhauService;
import services.MysqlConnection;
import services.NhanKhauService;

public class HoKhauController extends HomeController implements Initializable{
	@FXML
	TableColumn<HoKhauModel, String> colMaHoKhau;
	@FXML
	TableColumn<HoKhauModel, String> colMaChuHo;
	@FXML
	TableColumn<HoKhauModel, String> colSoThanhVien;
	@FXML
	TableColumn<HoKhauModel, String> colSoPhong;
	@FXML
	TableColumn<HoKhauModel, String> colSoOto;
	@FXML
	TableColumn<HoKhauModel, String> colSoXeMay;
	@FXML
	TableView<HoKhauModel> tvHoKhau;
	@FXML
	TextField tfSearch;

	@FXML
	ComboBox<String> cbChooseSearch;

	ObservableList<HoKhauModel> listValueTableView;
	private List<HoKhauModel> listHoKhau;

	// Hiển thị thông tin hộ khẩu
	public void showHoKhau() throws ClassNotFoundException, SQLException {
		listHoKhau = new HoKhauService().getListHoKhau();
		listValueTableView = FXCollections.observableArrayList(listHoKhau);
		List<NhanKhauModel> listNhanKhau = new NhanKhauService().getListNhanKhau(null);
		List<ChuHoModel> listChuHo = new ChuHoService().getListChuHo();

		Map<Integer, String> mapIdToTen = new HashMap<>();
		listNhanKhau.forEach(nhankhau -> {
			mapIdToTen.put(nhankhau.getId(), nhankhau.getTen());
		}); 

		Map<Integer, Integer> mapMahoToId = new HashMap<>();
		listChuHo.forEach(chuho -> {
			mapMahoToId.put(chuho.getMaHo(), chuho.getIdChuHo());
		});

		colMaHoKhau.setCellValueFactory(new PropertyValueFactory<HoKhauModel, String>("maHo"));
		colMaChuHo.setCellValueFactory((CellDataFeatures<HoKhauModel, String> p) -> {
			Integer idChuHo = mapMahoToId.get(p.getValue().getMaHo());
			String tenChuHo = idChuHo != null ? mapIdToTen.get(idChuHo) : "Không xác định";
			return new ReadOnlyStringWrapper(tenChuHo != null ? tenChuHo : "Không xác định");
		});
		colSoThanhVien.setCellValueFactory(new PropertyValueFactory<HoKhauModel, String>("soThanhvien"));
		colSoPhong.setCellValueFactory(new PropertyValueFactory<HoKhauModel, String>("soPhong"));
		colSoOto.setCellValueFactory((CellDataFeatures<HoKhauModel, String> p) -> {
			Integer soOto = p.getValue().getSoOto();
			return new ReadOnlyStringWrapper(soOto != null ? soOto.toString() : "0");
		});
		colSoXeMay.setCellValueFactory((CellDataFeatures<HoKhauModel, String> p) -> {
			Integer soXeMay = p.getValue().getSoXeMay();
			return new ReadOnlyStringWrapper(soXeMay != null ? soXeMay.toString() : "0");
		});

		tvHoKhau.setItems(listValueTableView);

		// Thiết lập Combo box
		ObservableList<String> listComboBox = FXCollections.observableArrayList("Mã hộ", "Tên chủ hộ", "Số phòng");
		cbChooseSearch.setValue("Mã hộ");
		cbChooseSearch.setItems(listComboBox);
	}

	public void chiTietHoKhau() throws IOException {
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

	
	public void addHoKhau() throws IOException, ClassNotFoundException, SQLException {
	    // Load giao diện addnhankhau.fxml
	    FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(getClass().getResource("/views/hokhau/addhokhau.fxml"));
	    Parent home = loader.load();

	    // Tạo stage để hiển thị cửa sổ thêm nhân khẩu
	    Stage stage = new Stage();
	    stage.setScene(new Scene(home, 800, 600));

	    // Lấy controller của giao diện thêm nhân khẩu
	    AddHoKhau addHoKhau = loader.getController();

	    // Kiểm tra nếu controller null
	    if (addHoKhau == null) return;

	    stage.setResizable(false);
	    stage.showAndWait();

	    // Sau khi thêm, cập nhật lại danh sách nhân khẩu
	    showHoKhau();
	}


	public void delHoKhau() throws ClassNotFoundException, SQLException {
	    HoKhauModel hoKhauModel = tvHoKhau.getSelectionModel().getSelectedItem();

	    if (hoKhauModel == null) {
	        Alert alert = new Alert(AlertType.WARNING, "Hãy chọn hộ khẩu bạn muốn xóa!", ButtonType.OK);
	        alert.setHeaderText(null);
	        alert.showAndWait();
	    } else {
	        Alert alert = new Alert(AlertType.WARNING, "Khi xóa hộ khẩu, tất cả quan hệ sẽ bị xóa nhưng thành viên không bị xóa!",
	                ButtonType.YES, ButtonType.NO);
	        alert.setHeaderText("Bạn có chắc chắn muốn xóa hộ khẩu này?");
	        Optional<ButtonType> result = alert.showAndWait();

	        if (result.get() == ButtonType.NO) {
	            return;
	        } else {
	            // Lấy mã hộ khẩu cần xóa
	            int idHoKhauDel = hoKhauModel.getMaHo();

	            try (Connection connection = MysqlConnection.getMysqlConnection()) {
	                // Bước 1: Xóa quan hệ trong bảng quan_he
	                String deleteQuanHeQuery = "DELETE FROM quan_he WHERE MaHo = ?";
	                try (PreparedStatement deleteQuanHeStmt = connection.prepareStatement(deleteQuanHeQuery)) {
	                    deleteQuanHeStmt.setInt(1, idHoKhauDel);
	                    deleteQuanHeStmt.executeUpdate();
	                }

	                // Bước 2: Xóa thông tin chủ hộ trong bảng chu_ho
	                String deleteChuHoQuery = "DELETE FROM chu_ho WHERE MaHo = ?";
	                try (PreparedStatement deleteChuHoStmt = connection.prepareStatement(deleteChuHoQuery)) {
	                    deleteChuHoStmt.setInt(1, idHoKhauDel);
	                    deleteChuHoStmt.executeUpdate();
	                }

	                // Bước 3: Xóa hộ khẩu khỏi bảng ho_khau
	                String deleteHoKhauQuery = "DELETE FROM ho_khau WHERE MaHo = ?";
	                try (PreparedStatement deleteHoKhauStmt = connection.prepareStatement(deleteHoKhauQuery)) {
	                    deleteHoKhauStmt.setInt(1, idHoKhauDel);
	                    deleteHoKhauStmt.executeUpdate();
	                }

	                // Sau khi xóa, cập nhật lại giao diện hiển thị hộ khẩu
	                showHoKhau();
	            } catch (SQLException e) {
	                System.err.println("SQL Error: " + e.getMessage());
	            } catch (ClassNotFoundException e) {
	                System.err.println("Connection Error: " + e.getMessage());
	            }
	        }
	    }
	}


	public void searchHoKhau() throws ClassNotFoundException, SQLException{
		ObservableList<HoKhauModel> listValueTableView_tmp = null;
		String keySearch = tfSearch.getText();

		// lay lua chon tim kiem cua khach hang
		SingleSelectionModel<String> typeSearch = cbChooseSearch.getSelectionModel();
		 String typeSearchString = typeSearch.getSelectedItem();

		// tim kiem thong tin theo lua chon da lay ra
		switch (typeSearchString) {
		case "Tên chủ hộ":{
			if(keySearch.length()==0) {
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
			for(HoKhauModel hoKhauModel : listHoKhau) {
				if (mapIdToTen.get(mapMahoToId.get(hoKhauModel.getMaHo()))
					    .toLowerCase().contains(keySearch.toLowerCase())) {
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
		case "Số phòng": {
			if (keySearch.length() == 0) {
				tvHoKhau.setItems(listValueTableView);
				Alert alert = new Alert(AlertType.WARNING, "Hãy nhập thông tin tìm kiếm!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.showAndWait();
				break;
			}

			int index = 0;
			List<HoKhauModel> listHoKhau_tmp = new ArrayList<>();
			for (HoKhauModel hoKhauModel : listHoKhau) {
				if (hoKhauModel.getSoPhong() != null && hoKhauModel.getSoPhong().contains(keySearch)) {
					listHoKhau_tmp.add(hoKhauModel);
					index++;
				}
			}
			listValueTableView_tmp = FXCollections.observableArrayList(listHoKhau_tmp);
			tvHoKhau.setItems(listValueTableView_tmp);

			if (index == 0) {
				tvHoKhau.setItems(listValueTableView);
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
		loader.setLocation(getClass().getResource("/views/hokhau/updatehokhau.fxml"));
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