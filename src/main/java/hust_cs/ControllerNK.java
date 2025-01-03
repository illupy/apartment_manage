package hust_cs;

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

import hust_cs.controller_nhankhau.UpdateNhanKhau;
import hust_cs.models.ChuHoModel;
import hust_cs.models.NhanKhauModel;
import hust_cs.models.QuanHeModel;
import hust_cs.services.ChuHoService;
import hust_cs.services.NhanKhauService;
import hust_cs.services.QuanHeService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class ControllerNK implements Initializable {
    
    private Stage stage;
    private Scene scene;

    @SuppressWarnings("exports")
    public void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile + ".fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root); 
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void TKho(ActionEvent event) throws IOException {
        switchScene(event, "/thongke/home_statistic");
    }

    @FXML
    void TKkhoanphi(ActionEvent event) throws IOException {
        switchScene(event, "/thongke/fee_statistic");
    }

    @FXML
    void addnhankhau(ActionEvent event) throws IOException {
        switchScene(event, "/nhankhau/addnhankhau");
    }

    @FXML
    void hokhau(ActionEvent event) throws IOException {
        switchScene(event, "/hokhau/hokhau");
    }

    @FXML
    void khoanphi(ActionEvent event) throws IOException {
        switchScene(event, "/khoanphi/khoanphi");

    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        switchScene(event, "/hust_cs/login");
    }

    @FXML
    void nhankhau(ActionEvent event) throws IOException {
        switchScene(event, "/nhankhau/nhankhau");
    }

    @FXML
    void update(ActionEvent event) throws IOException {
        switchScene(event, "/updateinfo/updateinfo");
    }

    @FXML
	private TableColumn<NhanKhauModel, String> colMaNhanKhau;
	@FXML
	private TableColumn<NhanKhauModel, String> colTen;
	@FXML
	private TableColumn<NhanKhauModel, String> colCCCD;
	@FXML 
	private TableColumn<NhanKhauModel, String> colSDT;
	@FXML
	private TableColumn<NhanKhauModel, String> colMaHo;
	@FXML
	private TableColumn<NhanKhauModel, String> colGioiTinh;
	@FXML
	private TableColumn<NhanKhauModel, String> colNgaySinh;
	@FXML
	private TableColumn<NhanKhauModel, String> colQuanHeChuHo;
	@FXML
	private TableColumn<NhanKhauModel, String> colNgheNghiep;
	@FXML
	private TableColumn<NhanKhauModel, String> colQueQuan;
	@FXML
	private TableColumn<NhanKhauModel, String> colNoiThuongTru;

	@FXML
	private TableView<NhanKhauModel> tvNhanKhau;
	@FXML
	private TextField tfSearch;
	@FXML
	private ComboBox<String> cbChooseSearch;
	
	private ObservableList<NhanKhauModel> listValueTableView;
	private List<NhanKhauModel> listNhanKhau;
	

    public TableView<NhanKhauModel> getTvNhanKhau(){
		return tvNhanKhau;
	}
	
	public void setTvNhanKhau(TableView<NhanKhauModel> tvNhanKhau) {
		this.tvNhanKhau = tvNhanKhau;
	}
	
	// hien thi thong tin nhan khau
	public void showNhanKhau() throws ClassNotFoundException, SQLException {
		listNhanKhau = new NhanKhauService().getListNhanKhau(null);
		listValueTableView = FXCollections.observableArrayList(listNhanKhau);
		
		// tao map anh xa gia tri Id sang maHo
		Map<Integer, Integer> mapIdToMaho = new HashMap<>();
		List<QuanHeModel> listQuanHe = new QuanHeService().getListQuanHe();
		listQuanHe.forEach(quanhe -> {
			mapIdToMaho.put(quanhe.getIdThanhVien(), quanhe.getMaHo());
		});
		
		// thiet lap cac cot cho tableviews
		colMaNhanKhau.setCellValueFactory(new PropertyValueFactory<NhanKhauModel, String>("id"));
		colTen.setCellValueFactory(new PropertyValueFactory<NhanKhauModel, String>("ten"));
		colNgaySinh.setCellValueFactory(
			    (CellDataFeatures<NhanKhauModel, String> p) -> new ReadOnlyStringWrapper(
			        p.getValue().getNgaySinh() != null ? p.getValue().getNgaySinh().toString() : "Không xác định"
			    )
			);
		colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
		colQuanHeChuHo.setCellValueFactory(new PropertyValueFactory<>("quanHeChuHo"));
		colNgheNghiep.setCellValueFactory(new PropertyValueFactory<>("ngheNghiep"));
		colQueQuan.setCellValueFactory(new PropertyValueFactory<>("queQuan"));
		colNoiThuongTru.setCellValueFactory(new PropertyValueFactory<>("noiThuongTru"));
		colCCCD.setCellValueFactory(new PropertyValueFactory<NhanKhauModel, String>("cccd"));
		colSDT.setCellValueFactory(new PropertyValueFactory<NhanKhauModel, String>("sdt"));
		try {
			colMaHo.setCellValueFactory(
					(CellDataFeatures<NhanKhauModel, String> p) -> new ReadOnlyStringWrapper(mapIdToMaho.get(p.getValue().getId()).toString())
			);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		tvNhanKhau.setItems(listValueTableView);

		// thiet lap gia tri cho combobox
		ObservableList<String> listComboBox = FXCollections.observableArrayList("Tên", "Tuổi", "id");
		cbChooseSearch.setValue("Tên");
		cbChooseSearch.setItems(listComboBox);
	}

	// tim kiem nhan khau theo ten, tuoi, id
	public void searchNhanKhau() {
	    ObservableList<NhanKhauModel> listValueTableView_tmp = null;
	    String keySearch = tfSearch.getText();

	    // lấy lựa chọn tìm kiếm của người dùng
	    SingleSelectionModel<String> typeSearch = cbChooseSearch.getSelectionModel();
	    String typeSearchString = typeSearch.getSelectedItem();

	    switch (typeSearchString) {
	        case "Tên": {
	            if (keySearch.length() == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào thông tin cần tìm kiếm!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	                break;
	            }

	            int index = 0;
	            List<NhanKhauModel> listNhanKhauModelsSearch = new ArrayList<>();
	            for (NhanKhauModel nhanKhauModel : listNhanKhau) {
	                if (nhanKhauModel.getTen().contains(keySearch)) {
	                    listNhanKhauModelsSearch.add(nhanKhauModel);
	                    index++;
	                }
	            }
	            listValueTableView_tmp = FXCollections.observableArrayList(listNhanKhauModelsSearch);
	            tvNhanKhau.setItems(listValueTableView_tmp);
	            
	            if (index == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	            }
	            break;
	        }
	        case "Tuổi": {
	            if (keySearch.length() == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào thông tin cần tìm kiếm!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	                break;
	            }

	            Pattern pattern = Pattern.compile("\\d{1,}");
	            if (!pattern.matcher(keySearch).matches()) {
	                Alert alert = new Alert(AlertType.WARNING, "Tuổi phải là một số!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	                break;
	            }

	            int index = 0;
	            List<NhanKhauModel> listNhanKhau_tmp = new ArrayList<>();
	            for (NhanKhauModel nhanKhauModel : listNhanKhau) {
	                if (nhanKhauModel.getTuoi() == Integer.parseInt(keySearch)) {
	                    listNhanKhau_tmp.add(nhanKhauModel);
	                    index++;
	                }
	            }
	            listValueTableView_tmp = FXCollections.observableArrayList(listNhanKhau_tmp);
	            tvNhanKhau.setItems(listValueTableView_tmp);
	            
	            if (index == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	            }
	            break;
	        }
	        case "SĐT": {
	            if (keySearch.length() == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào số điện thoại cần tìm kiếm!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	                break;
	            }

	            int index = 0;
	            List<NhanKhauModel> listNhanKhauModelsSearch = new ArrayList<>();
	            for (NhanKhauModel nhanKhauModel : listNhanKhau) {
	                if (nhanKhauModel.getSdt() != null && nhanKhauModel.getSdt().contains(keySearch)) {
	                    listNhanKhauModelsSearch.add(nhanKhauModel);
	                    index++;
	                }
	            }
	            listValueTableView_tmp = FXCollections.observableArrayList(listNhanKhauModelsSearch);
	            tvNhanKhau.setItems(listValueTableView_tmp);
	            
	            if (index == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	            }
	            break;
	        }
	        case "cccd": {
	            if (keySearch.length() == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào CCCD cần tìm kiếm!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	                break;
	            }

	            int index = 0;
	            List<NhanKhauModel> listNhanKhauModelsSearch = new ArrayList<>();
	            for (NhanKhauModel nhanKhauModel : listNhanKhau) {
	                if (nhanKhauModel.getCccd() != null && nhanKhauModel.getCccd().contains(keySearch)) {
	                    listNhanKhauModelsSearch.add(nhanKhauModel);
	                    index++;
	                }
	            }
	            listValueTableView_tmp = FXCollections.observableArrayList(listNhanKhauModelsSearch);
	            tvNhanKhau.setItems(listValueTableView_tmp);
	            
	            if (index == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	            }
	            break;
	        }
	        case "Ngày sinh": {
	            if (keySearch.length() == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào ngày sinh cần tìm kiếm!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	                break;
	            }

	            int index = 0;
	            List<NhanKhauModel> listNhanKhauModelsSearch = new ArrayList<>();
	            for (NhanKhauModel nhanKhauModel : listNhanKhau) {
	                // Lấy ngày sinh của người dùng
	                java.util.Date ngaySinh = nhanKhauModel.getNgaySinh();
	                if (ngaySinh != null && ngaySinh.toString().contains(keySearch)) {
	                    listNhanKhauModelsSearch.add(nhanKhauModel);
	                    index++;
	                }
	            }
	            listValueTableView_tmp = FXCollections.observableArrayList(listNhanKhauModelsSearch);
	            tvNhanKhau.setItems(listValueTableView_tmp);
	            
	            if (index == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.INFORMATION, "Không tìm thấy thông tin!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	            }
	            break;
	        }
	        default: {
	            if (keySearch.length() == 0) {
	                tvNhanKhau.setItems(listValueTableView);
	                Alert alert = new Alert(AlertType.INFORMATION, "Bạn cần nhập vào thông tin tìm kiếm!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	                break;
	            }

	            Pattern pattern = Pattern.compile("\\d{1,}");
	            if (!pattern.matcher(keySearch).matches()) {
	                Alert alert = new Alert(AlertType.WARNING, "Bạn phải nhập vào một số!", ButtonType.OK);
	                alert.setHeaderText(null);
	                alert.showAndWait();
	                break;
	            }

	            for (NhanKhauModel nhanKhauModel : listNhanKhau) {
	                if (nhanKhauModel.getId() == Integer.parseInt(keySearch)) {
	                    listValueTableView_tmp = FXCollections.observableArrayList(nhanKhauModel);
	                    tvNhanKhau.setItems(listValueTableView_tmp);
	                    return;
	                }
	            }
	            tvNhanKhau.setItems(listValueTableView);
	            Alert alert = new Alert(AlertType.WARNING, "Không tìm thấy thông tin!", ButtonType.OK);
	            alert.setHeaderText(null);
	            alert.showAndWait();
	        }
	    }
	}


	public void addNhanKhau(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
		Parent home = FXMLLoader.load(getClass().getResource("/views/nhankhau/AddNhanKhau.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(home,800,600));
        stage.setResizable(false);
        stage.showAndWait();
        showNhanKhau();
	}
	
	// con truong hop neu xoa chu ho chua xet
	public void delNhanKhau() throws IOException, ClassNotFoundException, SQLException {
		NhanKhauModel nhanKhauModel = tvNhanKhau.getSelectionModel().getSelectedItem();
		int maho = 0;
		
		if(nhanKhauModel == null) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy chọn nhân khẩu bạn muốn xóa!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
		} else {
			// kiem tra dieu kien chu ho
			List<ChuHoModel> listChuHo = new ChuHoService().getListChuHo();
			for(ChuHoModel chuho : listChuHo) {
				if(chuho.getIdChuHo() == nhanKhauModel.getId()) {
					Alert alert = new Alert(AlertType.WARNING, "Bạn không thể xóa chủ hộ tại đây, hãy xóa chủ hộ tại mục hộ khẩu!", ButtonType.OK);
					alert.setHeaderText("Nhân khẩu này là 1 chủ hộ!");
					alert.showAndWait();
					return;
				}
			}
			
			Alert alert = new Alert(AlertType.WARNING, "Bạn có chắc chắn muốn xóa nhân khẩu này!", ButtonType.YES, ButtonType.NO);
			alert.setHeaderText(null);
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == ButtonType.NO) {
				return;
			} else {
				List<QuanHeModel> listQuanHe = new QuanHeService().getListQuanHe();
				for(QuanHeModel quanHeModel : listQuanHe) {
					if(quanHeModel.getIdThanhVien() == nhanKhauModel.getId()) {
						maho = quanHeModel.getMaHo();
						break;
					}
				}
				
				new NhanKhauService().delete(nhanKhauModel.getId());
				new QuanHeService().del(maho, nhanKhauModel.getId());
			}
		}
		
		showNhanKhau();
	}
	
	public void updateNhanKhau() throws IOException, ClassNotFoundException, SQLException {
		// lay ra nhan khau can update
		NhanKhauModel nhanKhauModel = tvNhanKhau.getSelectionModel().getSelectedItem();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/nhankhau/UpdateNhanKhau.fxml"));
		Parent home = loader.load(); 
        Stage stage = new Stage();
        stage.setScene(new Scene(home,800,600));
        UpdateNhanKhau updateNhanKhau = loader.getController();
        
        // bat loi truong hop khong hop le
        if(updateNhanKhau == null) return;
        if(nhanKhauModel == null) {
			Alert alert = new Alert(AlertType.WARNING, "Chọn nhân khẩu cần update !", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}
        updateNhanKhau.setNhanKhauModel(nhanKhauModel);
        
        stage.setResizable(false);
        stage.showAndWait();
        showNhanKhau();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			showNhanKhau();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
