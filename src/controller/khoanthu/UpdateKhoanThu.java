package controller.khoanthu;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import models.KhoanThuModel;
import models.LoaiKhoanThuModel;
import services.KhoanThuService;

public class UpdateKhoanThu extends controller.HomeController implements Initializable {
	@FXML
	private ComboBox<LoaiKhoanThuModel> cbMaKhoanThu;
	@FXML
	private TextField tfSoTien;
	@FXML
	private DatePicker dpNgayBatDau;
	@FXML
	private DatePicker dpNgayKetThuc;
	@FXML
	private TextField tfMaHo;
	
	private int idKhoanThu;
	
	public int getIdKhoanThu() {
		return idKhoanThu;
	}

	public void setIdKhoanThu(int idKhoanThu) {
		this.idKhoanThu = idKhoanThu;
	}

	public void updateKhoanThu(ActionEvent event) throws ClassNotFoundException, SQLException {
		Pattern pattern;

		// kiem tra soTien nhap vao
		// so tien nhap vao phai la so va nho hon 11 chu so
		pattern = Pattern.compile("^[1-9]\\d*(\\.\\d+)?$");
		if (!pattern.matcher(getTfSoTien().getText()).matches()) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào số tiền hợp lệ!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}

		// Kiểm tra xem đã chọn khoản thu chưa
        LoaiKhoanThuModel selectedKhoanThu = getCbMaKhoanThu().getValue();
        if (selectedKhoanThu == null) {
            Alert alert = new Alert(AlertType.WARNING, "Vui lòng chọn khoản thu!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        
     // Kiểm tra ngày bắt đầu
        if (getDpNgayBatDau().getValue() == null) {
            Alert alert = new Alert(AlertType.WARNING, "Vui lòng chọn ngày bắt đầu!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Kiểm tra ngày kết thúc
        if (getDpNgayKetThuc().getValue() == null) {
            Alert alert = new Alert(AlertType.WARNING, "Vui lòng chọn ngày kết thúc!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (getDpNgayKetThuc().getValue().isBefore(getDpNgayBatDau().getValue())) {
            Alert alert = new Alert(AlertType.WARNING, "Ngày kết thúc phải sau ngày bắt đầu!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Lấy thông tin từ item đã chọn
        int maKhoanThu = selectedKhoanThu.getMaKhoanThu();
        double soTienDouble = Double.parseDouble(getTfSoTien().getText());
        int maHo = Integer.parseInt(getTfMaHo().getText());
        
        KhoanThuModel newKhoanThu = new KhoanThuModel();
        newKhoanThu.setIDKhoanThu(idKhoanThu);
        newKhoanThu.setMaKhoanThu(maKhoanThu);
        newKhoanThu.setSoTien(soTienDouble);
        newKhoanThu.setNgayBatDau(java.sql.Date.valueOf(getDpNgayBatDau().getValue()));
        newKhoanThu.setNgayKetThuc(java.sql.Date.valueOf(getDpNgayKetThuc().getValue()));
        newKhoanThu.setMaHo(maHo);

        if (new KhoanThuService().update(newKhoanThu)) {
            Alert alert = new Alert(AlertType.INFORMATION, "Sửa khoản thu thành công!", ButtonType.OK);
            alert.setHeaderText(null);
            
            // Đợi người dùng bấm OK trên alert rồi mới chuyển scene
            alert.showAndWait().ifPresent(response -> {
                try {
                    switchScene(event, "/views/KhoanThu.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(AlertType.ERROR, "Có lỗi xảy ra: " + e.getMessage(), ButtonType.OK);
                    errorAlert.setHeaderText(null);
                    errorAlert.showAndWait();
                }
            });
        }
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Thiết lập giới hạn ngày cho DatePicker
	    getDpNgayBatDau().setPromptText("Chọn ngày bắt đầu");
	    getDpNgayKetThuc().setPromptText("Chọn ngày kết thúc");
	    
	    // Giới hạn ngày kết thúc không được trước ngày bắt đầu
	    getDpNgayBatDau().valueProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            getDpNgayKetThuc().setDayCellFactory(picker -> new DateCell() {
	                @Override
	                public void updateItem(LocalDate date, boolean empty) {
	                    super.updateItem(date, empty);
	                    setDisable(empty || date.isBefore(newValue));
	                }
	            });
	        }
	    });

		// thiet lap gia tri cho loai khoan thu
		List<LoaiKhoanThuModel> listLoaiKhoanThu = null;
		
		try {
			listLoaiKhoanThu = new KhoanThuService().getLoaiKhoanThu();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObservableList<LoaiKhoanThuModel> items = FXCollections.observableArrayList();
		
		for (LoaiKhoanThuModel loaiKhoanThu : listLoaiKhoanThu) {
            items.add(loaiKhoanThu);
        }
		
		getCbMaKhoanThu().setItems(items);
	}

	public DatePicker getDpNgayBatDau() {
		return dpNgayBatDau;
	}

	public void setDpNgayBatDau(DatePicker dpNgayBatDau) {
		this.dpNgayBatDau = dpNgayBatDau;
	}

	public DatePicker getDpNgayKetThuc() {
		return dpNgayKetThuc;
	}

	public void setDpNgayKetThuc(DatePicker dpNgayKetThuc) {
		this.dpNgayKetThuc = dpNgayKetThuc;
	}

	public TextField getTfSoTien() {
		return tfSoTien;
	}

	public void setTfSoTien(TextField tfSoTien) {
		this.tfSoTien = tfSoTien;
	}

	public TextField getTfMaHo() {
		return tfMaHo;
	}

	public void setTfMaHo(TextField tfMaHo) {
		this.tfMaHo = tfMaHo;
	}

	public ComboBox<LoaiKhoanThuModel> getCbMaKhoanThu() {
		return cbMaKhoanThu;
	}

	public void setCbMaKhoanThu(ComboBox<LoaiKhoanThuModel> cbMaKhoanThu) {
		this.cbMaKhoanThu = cbMaKhoanThu;
	}
}
