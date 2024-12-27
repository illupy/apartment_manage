package controller;

import java.io.IOException;
import java.util.Optional;

import controller.khoanthu.UpdateKhoanThu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import models.KhoanThuModel;
import models.LoaiKhoanThuModel;

public class HomeController {
	@FXML

	private Stage stage;
	private Scene scene;

	public void switchScene(ActionEvent event, String fxmlFile) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchSceneWithKhoanThuData(ActionEvent event, String fxmlFile, KhoanThuModel khoanThu)
		    throws IOException {
		    // Tải FXML
		    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
		    Parent root = loader.load();

		    // Lấy controller của màn hình đích
		    UpdateKhoanThu controller = loader.getController();
		    LoaiKhoanThuModel loaiKhoanThu = new LoaiKhoanThuModel(khoanThu.getMaKhoanThu(), khoanThu.getTenKhoanThu());

		    // Thiết lập dữ liệu cho controller
		    if (khoanThu != null) {
		        // Điền thông tin vào các trường của controller
		        controller.getTfSoTien().setText(String.valueOf(khoanThu.getSoTien()));
		        controller.getTfMaHo().setText(String.valueOf(khoanThu.getMaHo()));
		        controller.setIdKhoanThu(khoanThu.getIdKhoanThu());
		        System.out.println(String.valueOf(khoanThu.getMaHo()));
		  
		        
		        // Chuyển đổi java.sql.Date sang LocalDate
		        if (khoanThu.getNgayBatDau() != null) {
		            controller.getDpNgayBatDau().setValue(
		            		((java.sql.Date) khoanThu.getNgayBatDau()).toLocalDate()
		            );
		        }
		        
		        if (khoanThu.getNgayKetThuc() != null) {
		            controller.getDpNgayKetThuc().setValue(
		            		((java.sql.Date) khoanThu.getNgayKetThuc()).toLocalDate()
		            );
		        }
		    }

		    // Chuyển cảnh
		    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		    stage.setScene(new Scene(root));
		    stage.show();
		}

	@FXML
	public void setNhanKhau(ActionEvent event) throws IOException {
		switchScene(event, "/views/nhankhau/nhankhau.fxml");
	}

	@FXML
	public void setHoKhau(ActionEvent event) throws IOException {
		switchScene(event, "/views/hokhau/hoKhau.fxml");
	}

	@FXML
	public void setKhoanPhi(ActionEvent event) throws IOException {
		switchScene(event, "/views/KhoanThu.fxml");
	}

	@FXML
	public void setTKho(ActionEvent event) throws IOException {
		switchScene(event, "/views/thongke/ThongKeTheoHo.fxml");
	}

	@FXML
	public void setTKkhoanphi(ActionEvent event) throws IOException {
		switchScene(event, "/views/thongke/ThongKeKhoanPhi.fxml");
	}

	@FXML
	public void update(ActionEvent event) throws IOException {
		switchScene(event, "/views/updateinfo.fxml");
	}
	


	@FXML
	public void logout(ActionEvent event) throws IOException {
		// Tạo một Alert với loại CONFIRMATION
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Xác nhận đăng xuất");
		alert.setHeaderText(null);
		alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

		// Hiển thị Alert và chờ người dùng phản hồi
		Optional<ButtonType> result = alert.showAndWait();

		// Kiểm tra xem người dùng đã chọn OK hoặc Cancel
		if (result.isPresent() && result.get() == ButtonType.OK) {
			// Nếu chọn OK, thực hiện đăng xuất
			switchScene(event, "/views/Login.fxml");
		} else {
			// Người dùng chọn Cancel hoặc đóng cửa sổ Alert, không làm gì cả
		}
	}
}