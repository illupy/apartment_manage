package hust_cs.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hust_cs.models.HoKhauModel;
import hust_cs.models.NhanKhauModel;

public class NhanKhauService{
	public boolean add(NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
	    try (Connection connection = MysqlConnection.getMysqlConnection()) {
	        String query = "INSERT INTO nhan_khau(ID, CCCD, Ten, GioiTinh, NgaySinh, SDT, NoiThuongTru, NgheNghiep, QueQuan) " +
	                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	            preparedStatement.setInt(1, nhanKhauModel.getId());
	            preparedStatement.setString(2, nhanKhauModel.getCccd());
	            preparedStatement.setString(3, nhanKhauModel.getTen());
	            preparedStatement.setString(4, nhanKhauModel.getGioiTinh());
	            preparedStatement.setDate(5, new java.sql.Date(nhanKhauModel.getNgaySinh().getTime()));
	            preparedStatement.setString(6, nhanKhauModel.getSdt());	            
	            preparedStatement.setString(7, nhanKhauModel.getNoiThuongTru());
	            preparedStatement.setString(8, nhanKhauModel.getNgheNghiep());
	            preparedStatement.setString(9, nhanKhauModel.getQueQuan());
	            preparedStatement.executeUpdate();
	        }
	    }
	    return true;
	}
	
	public boolean delete(int ID) throws ClassNotFoundException, SQLException {
	    try (Connection connection = MysqlConnection.getMysqlConnection()) {
	        // Xóa dữ liệu từ các bảng theo thứ tự
	        deleteFromTable(connection, "nop_tien", "IDNopTien", ID);
	        deleteFromTable(connection, "chu_ho", "IDChuHo", ID);
	        deleteFromTable(connection, "quan_he", "IDThanhVien", ID);
	        deleteFromTable(connection, "nhan_khau", "ID", ID);
	    }
	    return true;
	}

	// Hàm xóa dữ liệu từ bảng
	private void deleteFromTable(Connection connection, String tableName, String columnName, int value)
	        throws SQLException {
	    String query = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, value);
	        preparedStatement.executeUpdate();
	    }
	}

	
	public boolean updateNhanKhau(NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
	    String query = "UPDATE nhan_khau SET CCCD = ?, Ten = ?, GioiTinh = ?, NgaySinh = ?, SDT = ?,  NoiThuongTru = ?, NgheNghiep = ?, QueQuan = ?WHERE ID = ?";

	    try (Connection connection = MysqlConnection.getMysqlConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, nhanKhauModel.getCccd());
	        preparedStatement.setString(2, nhanKhauModel.getTen());
	        preparedStatement.setString(3, nhanKhauModel.getGioiTinh());
	        preparedStatement.setDate(4, new java.sql.Date(nhanKhauModel.getNgaySinh().getTime()));
	        preparedStatement.setString(5, nhanKhauModel.getSdt());
	        preparedStatement.setString(6, nhanKhauModel.getNoiThuongTru());
	        preparedStatement.setString(7, nhanKhauModel.getNgheNghiep());
	        preparedStatement.setString(8, nhanKhauModel.getQueQuan());
	        preparedStatement.setInt(9, nhanKhauModel.getId());

	        int rowsAffected = preparedStatement.executeUpdate();
	        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
	    }
	}

	public List<NhanKhauModel> getListNhanKhau(String gioiTinh) throws ClassNotFoundException, SQLException {
	    List<NhanKhauModel> list = new ArrayList<>();

	    String query = gioiTinh == null ? "SELECT * FROM nhan_khau" 
	                                    : "SELECT * FROM nhan_khau WHERE GioiTinh = ?";
	    try (Connection connection = MysqlConnection.getMysqlConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        if (gioiTinh != null) {
	            preparedStatement.setString(1, gioiTinh);
	        }

	        try (ResultSet rs = preparedStatement.executeQuery()) {
	            while (rs.next()) {
	                NhanKhauModel nhanKhauModel = new NhanKhauModel();
	                nhanKhauModel.setId(rs.getInt("ID"));
	                nhanKhauModel.setCccd(rs.getString("CCCD"));
	                nhanKhauModel.setTen(rs.getString("Ten"));
	                nhanKhauModel.setGioiTinh(rs.getString("GioiTinh"));
	                nhanKhauModel.setNgaySinh(rs.getDate("NgaySinh"));
	                nhanKhauModel.setSdt(rs.getString("SDT"));
	                nhanKhauModel.setNoiThuongTru(rs.getString("NoiThuongTru"));
	                nhanKhauModel.setNgheNghiep(rs.getString("NgheNghiep"));
	                nhanKhauModel.setQueQuan(rs.getString("QueQuan"));

	                list.add(nhanKhauModel);
	            }
	        }
	    }
	    return list;
	}
	
	public boolean addnk(NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
	    String checkQuery = "SELECT COUNT(*) FROM nhan_khau WHERE ID = ?";
	    String insertQuery = "INSERT INTO nhan_khau(ID, CCCD, Ten, GioiTinh, NgaySinh, SDT) VALUES (?, ?, ?, ?, ?, ?)";

	    try (Connection connection = MysqlConnection.getMysqlConnection();
	         PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {

	        checkStatement.setInt(1, nhanKhauModel.getId());
	        try (ResultSet rs = checkStatement.executeQuery()) {
	            if (rs.next() && rs.getInt(1) > 0) {
	                return false; // Nhân khẩu đã tồn tại
	            }
	        }

	        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
	            insertStatement.setInt(1, nhanKhauModel.getId());
	            insertStatement.setString(2, nhanKhauModel.getCccd());
	            insertStatement.setString(3, nhanKhauModel.getTen());
	            insertStatement.setString(4, nhanKhauModel.getGioiTinh());
	            insertStatement.setDate(5, new java.sql.Date(nhanKhauModel.getNgaySinh().getTime()));
	            insertStatement.setString(6, nhanKhauModel.getSdt());
	            insertStatement.executeUpdate();
	        }
	    }
	    return true;
	}

	public List<NhanKhauModel> getNhanKhauByHoKhau(HoKhauModel hoKhauModel) throws ClassNotFoundException, SQLException {
	    List<NhanKhauModel> nhanKhauList = new ArrayList<>();
	    String sql = "SELECT nk.ID, nk.Ten, qh.QuanHe FROM nhan_khau nk JOIN quan_he qh ON nk.ID = qh.IDThanhVien WHERE qh.MaHo = ?";

	    try (Connection connection = MysqlConnection.getMysqlConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

	        preparedStatement.setInt(1, hoKhauModel.getMaHo());

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                NhanKhauModel nhanKhauModel = new NhanKhauModel();
	                nhanKhauModel.setId(resultSet.getInt("ID"));
	                nhanKhauModel.setTen(resultSet.getString("Ten"));
	                nhanKhauModel.setQuanHeChuHo(resultSet.getString("QuanHe"));

	                nhanKhauList.add(nhanKhauModel);
	            }
	        }
	    }
	    return nhanKhauList;
	}

	

}
