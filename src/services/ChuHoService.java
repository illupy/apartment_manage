package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.ChuHoModel;

public class ChuHoService{
	public boolean add(String soPhong, int idChuHo) throws Exception {
	    // Các câu lệnh SQL
	    String checkPhongQuery = "SELECT COUNT(*) FROM phong WHERE soPhong = ?";
	    String checkNhanKhauQuery = "SELECT COUNT(*) FROM nhan_khau WHERE ID = ?";
	    String insertHoKhauQuery = "INSERT INTO ho_khau (soPhong) VALUES (?)";
	    String insertChuHoQuery = "INSERT INTO chu_ho (MaHo, IDChuHo) VALUES ((SELECT MaHo FROM ho_khau WHERE soPhong = ?), ?)";
	    String insertQuanHeQuery = "INSERT INTO quan_he (MaHo, IDThanhVien, QuanHe) VALUES ((SELECT MaHo FROM ho_khau WHERE soPhong = ?), ?, 'chủ hộ')";

	    try (Connection connection = MysqlConnection.getMysqlConnection()) {
	        // Kiểm tra xem soPhong có tồn tại trong bảng phong không
	        try (PreparedStatement checkPhongStmt = connection.prepareStatement(checkPhongQuery)) {
	            checkPhongStmt.setString(1, soPhong);
	            try (ResultSet rs = checkPhongStmt.executeQuery()) {
	                if (rs.next() && rs.getInt(1) == 0) {
	                    throw new Exception("Số phòng không tồn tại trong bảng phong.");
	                }
	            }
	        }

	        // Kiểm tra xem IDChuHo có tồn tại trong bảng nhan_khau không
	        try (PreparedStatement checkNhanKhauStmt = connection.prepareStatement(checkNhanKhauQuery)) {
	            checkNhanKhauStmt.setInt(1, idChuHo);
	            try (ResultSet rs = checkNhanKhauStmt.executeQuery()) {
	                if (rs.next() && rs.getInt(1) == 0) {
	                    throw new Exception("IDChuHo không tồn tại trong bảng nhan_khau.");
	                }
	            }
	        }

	        // Thêm vào bảng ho_khau
	        try (PreparedStatement insertHoKhauStmt = connection.prepareStatement(insertHoKhauQuery)) {
	            insertHoKhauStmt.setString(1, soPhong);
	            insertHoKhauStmt.executeUpdate();
	        }

	        // Thêm vào bảng chu_ho với MaHo tự động lấy từ bảng ho_khau
	        try (PreparedStatement insertChuHoStmt = connection.prepareStatement(insertChuHoQuery)) {
	            insertChuHoStmt.setString(1, soPhong);
	            insertChuHoStmt.setInt(2, idChuHo);
	            insertChuHoStmt.executeUpdate();
	        }

	        // Thêm vào bảng quan_he
	        try (PreparedStatement insertQuanHeStmt = connection.prepareStatement(insertQuanHeQuery)) {
	            insertQuanHeStmt.setString(1, soPhong);
	            insertQuanHeStmt.setInt(2, idChuHo);
	            insertQuanHeStmt.executeUpdate();
	        }

	        return true;  // Trả về true khi mọi thao tác thành công

	    } catch (SQLException e) {
	        throw new Exception("SQL Error: " + e.getMessage());  // Ném ra ngoại lệ với chi tiết lỗi
	    } catch (ClassNotFoundException e) {
	        throw new Exception("Connection Error: " + e.getMessage());  // Ném ra ngoại lệ với chi tiết lỗi
	    }
	}


	public void updateChuHo(int maHo, int idChuHo) throws SQLException, ClassNotFoundException {
		String query = "UPDATE chu_ho SET IDChuHo = ? WHERE MaHo = ?";

		try (Connection connection = MysqlConnection.getMysqlConnection()) {
			try (PreparedStatement stmt = connection.prepareStatement(query)) {
				stmt.setInt(1, idChuHo);
				stmt.setInt(2, maHo);
				stmt.executeUpdate();
			}
		}
	}


	// xoa gia tri
	public boolean delete(int maHo, int idChuHo ) {
		String sql = "DELETE FROM chu_ho WHERE  MaHo= ? AND IDChuHo = ?";  
		try(Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)){

			preparedStatement.setInt(1, maHo);
			preparedStatement.setInt(2, idChuHo);
			preparedStatement.executeUpdate();

			return true;
		} catch (SQLException e) {
			System.err.println("SQL Error: " + e.getMessage());
			return false;
		} catch (ClassNotFoundException e) {
			System.err.println("Connection Error: " + e.getMessage());
			return false;
		}          
	}

	// lay danh sach tat ca chu ho
	public List<ChuHoModel> getListChuHo() {
		List<ChuHoModel> list = new ArrayList<>();
		String query = "SELECT * FROM chu_ho";

		try (Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery()) {

			while (rs.next()) {
				ChuHoModel chuHoModel = new ChuHoModel();
				chuHoModel.setMaHo(rs.getInt("MaHo"));
				chuHoModel.setIdChuHo(rs.getInt("IDChuHo"));
				list.add(chuHoModel);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace(); // Log or handle the exception appropriately
		}

		return list;
	}
}