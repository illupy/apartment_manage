package controller.khoanthu;

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

public class AddKhoanThu extends controller.HomeController implements Initializable {
	@FXML
	private ComboBox<KhoanThuItem> cbMaKhoanThu;
	@FXML
	private TextField tfSoTien;
	@FXML
	private DatePicker dpNgayBatDau;
	@FXML
	private DatePicker dpNgayKetThuc;
	@FXML
	private TextField tfMaHo;
	
	public class KhoanThuItem {
        private int maKhoanThu;
        private String tenKhoanThu;

        public KhoanThuItem(int maKhoanThu, String tenKhoanThu) {
            this.maKhoanThu = maKhoanThu;
            this.tenKhoanThu = tenKhoanThu;
        }

        public int getMaKhoanThu() {
            return maKhoanThu;
        }

        public String getTenKhoanThu() {
            return tenKhoanThu;
        }

        @Override
        public String toString() {
            return maKhoanThu + " - " + tenKhoanThu;
        }
    }

	public void addKhoanThu(ActionEvent event) throws ClassNotFoundException, SQLException {
		Pattern pattern;

		// kiem tra soTien nhap vao
		// so tien nhap vao phai la so va nho hon 11 chu so
		pattern = Pattern.compile("^[1-9]\\d*(\\.\\d+)?$");
		if (!pattern.matcher(tfSoTien.getText()).matches()) {
			Alert alert = new Alert(AlertType.WARNING, "Hãy nhập vào số tiền hợp lệ!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}

		// Kiểm tra xem đã chọn khoản thu chưa
        KhoanThuItem selectedKhoanThu = cbMaKhoanThu.getValue();
        if (selectedKhoanThu == null) {
            Alert alert = new Alert(AlertType.WARNING, "Vui lòng chọn khoản thu!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        
     // Kiểm tra ngày bắt đầu
        if (dpNgayBatDau.getValue() == null) {
            Alert alert = new Alert(AlertType.WARNING, "Vui lòng chọn ngày bắt đầu!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Kiểm tra ngày kết thúc
        if (dpNgayKetThuc.getValue() == null) {
            Alert alert = new Alert(AlertType.WARNING, "Vui lòng chọn ngày kết thúc!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (dpNgayKetThuc.getValue().isBefore(dpNgayBatDau.getValue())) {
            Alert alert = new Alert(AlertType.WARNING, "Ngày kết thúc phải sau ngày bắt đầu!", ButtonType.OK);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Lấy thông tin từ item đã chọn
        int maKhoanThu = selectedKhoanThu.getMaKhoanThu();
        double soTienDouble = Double.parseDouble(tfSoTien.getText());
        int maHo = Integer.parseInt(tfMaHo.getText());
        
        KhoanThuModel newKhoanThu = new KhoanThuModel();
        newKhoanThu.setMaKhoanThu(maKhoanThu);
        newKhoanThu.setSoTien(soTienDouble);
        newKhoanThu.setNgayBatDau(java.sql.Date.valueOf(dpNgayBatDau.getValue()));
        newKhoanThu.setNgayKetThuc(java.sql.Date.valueOf(dpNgayKetThuc.getValue()));
        newKhoanThu.setMaHo(maHo);

		new KhoanThuService().add(newKhoanThu);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Thiết lập giới hạn ngày cho DatePicker
	    dpNgayBatDau.setPromptText("Chọn ngày bắt đầu");
	    dpNgayKetThuc.setPromptText("Chọn ngày kết thúc");
	    
	    // Giới hạn ngày kết thúc không được trước ngày bắt đầu
	    dpNgayBatDau.valueProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            dpNgayKetThuc.setDayCellFactory(picker -> new DateCell() {
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
		ObservableList<KhoanThuItem> items = FXCollections.observableArrayList();
		
		for (LoaiKhoanThuModel loaiKhoanThu : listLoaiKhoanThu) {
            items.add(new KhoanThuItem(loaiKhoanThu.getMaKhoanThu(), loaiKhoanThu.getTenKhoanThu()));
        }
		
		cbMaKhoanThu.setItems(items);
	}
}
