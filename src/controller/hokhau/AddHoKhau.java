package controller.hokhau;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ChuHoService;

public class AddHoKhau extends controller.HomeController implements Initializable{ 
	@FXML
	private TextField tfSoPhong;
	@FXML
	private TextField tfIDChuHo;

	private Stage stage;
	private Scene scene;

	@FXML
	public void addHoKhau(ActionEvent event) {
	    if (validateInput()) {
	        String soPhong = tfSoPhong.getText();
	        int idChuHo = Integer.parseInt(tfIDChuHo.getText());

	        try {
	            // Gọi phương thức add và kiểm tra kết quả trả về
	            boolean isAdded = new ChuHoService().add(soPhong, idChuHo);

	            if (isAdded) {
	                // Nếu thêm thành công, hiển thị thông báo thành công
	                showAlert("Thêm thông tin hộ khẩu thành công!");
	                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/hokhau/hokhau.fxml"));
	        		Parent root = loader.load();
	                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	                scene = new Scene(root);
	        		stage.setScene(scene);
	        		stage.show();
	            }
	        } catch (Exception e) {
	            // Nếu có ngoại lệ xảy ra, hiển thị thông báo lỗi cho người dùng
	            showAlert("Lỗi: " + e.getMessage());  // Hiển thị thông báo lỗi lên giao diện
	        }

	        // Đóng cửa sổ sau khi thêm (nếu thành công)
	        
	    }
	}


	private boolean validateInput() {
		if (tfSoPhong.getText().isEmpty() || tfSoPhong.getText().length() > 50) {
			showAlert("Hãy nhập số phòng hợp lệ!");
			return false;
		}

		if (!tfIDChuHo.getText().matches("\\d+")) {
			showAlert("Hãy nhập vào ID hợp lệ (là một số nguyên)!");
			return false;
		}

		return true;
	}


	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
		alert.setHeaderText(null);
		alert.showAndWait();
	}


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// Không cần khởi tạo đặc biệt
	}
	
}