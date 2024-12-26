package hust_cs.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hust_cs.models.HoKhauModel;

public class HoKhauService{
	public boolean add(HoKhauModel hoKhauModel) {
	    String query = "INSERT INTO ho_khau(MaHo, SoThanhVien, DiaChi) VALUES (?, ?, ?)";
	    try (Connection connection = MysqlConnection.getMysqlConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setInt(1, hoKhauModel.getMaHo());
	        preparedStatement.setInt(2, hoKhauModel.getSoThanhvien());
	        preparedStatement.setString(3, hoKhauModel.getDiaChi());
	        preparedStatement.executeUpdate();
	        return true;

	    } catch (ClassNotFoundException e) {
	        System.err.println("Database connection error: " + e.getMessage());
	    } catch (SQLException e) {
	        System.err.println("SQL execution error: " + e.getMessage());
	    }
	    return false;
	}

	
	public boolean delete(int maHo) throws ClassNotFoundException, SQLException{
		try(Connection connection = MysqlConnection.getMysqlConnection()){
			// xoa tai bang chu ho
			deleteFromTable(connection, "chu_ho", "MaHo", maHo);
			
			// xoa tai bang quan_he
			deleteFromTable(connection, "quan_he", "MaHo", maHo);
			
			// xoa tai bang ho_khau
			deleteFromTable(connection, "ho_khau", "MaHo", maHo);
		}
		return true;
	}
	
	private void deleteFromTable(Connection connection, String tableName, String columnName, int value) throws SQLException{
		String query = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";
 
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setInt(1, value);
	            preparedStatement.executeUpdate();
	        }
	}
	
    public boolean update(int maHo, String diaChi, String sdt) throws ClassNotFoundException, SQLException {
        try (Connection connection = MysqlConnection.getMysqlConnection()) {
            String query = "UPDATE ho_khau " +
                           "INNER JOIN chu_ho ON ho_khau.MaHo = chu_ho.MaHo " +
                           "INNER JOIN nhan_khau ON chu_ho.IDChuHo = nhan_khau.ID " +
                           "SET ho_khau.DiaChi = ?, nhan_khau.SDT = ? " +
                           "WHERE ho_khau.MaHo = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, diaChi);
                preparedStatement.setString(2, sdt);
                preparedStatement.setInt(3, maHo);
                preparedStatement.executeUpdate();
            }
        }
        return true;
    }

	// checked
    public List<HoKhauModel> getListHoKhau() throws ClassNotFoundException, SQLException {
        List<HoKhauModel> list = new ArrayList<>();
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ho_khau");
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                HoKhauModel hoKhauModel = new HoKhauModel(rs.getInt("MaHo"), rs.getInt("SoThanhVien"),
                        rs.getString("DiaChi"));
                list.add(hoKhauModel);
            }
        }
        return list;
    }
    
    public HoKhauModel getHoKhaubyIdNhanKhau(int IdNhanKhau) throws ClassNotFoundException, SQLException {
        HoKhauModel hoKhau = null;
        try (Connection connection = MysqlConnection.getMysqlConnection()) {
            String query = "SELECT hk.MaHo, hk.SoThanhVien, hk.DiaChi " +
                           "FROM ho_khau hk " +
                           "JOIN quan_he qh ON hk.MaHo = qh.MaHo " +
                           "JOIN nhan_khau nk ON qh.IDThanhVien = nk.ID " +
                           "WHERE nk.ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, IdNhanKhau);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hoKhau = new HoKhauModel(
                            resultSet.getInt("MaHo"),
                            resultSet.getInt("SoThanhVien"),
                            resultSet.getString("DiaChi")
                        );
                    }
                }
            }
        }
        return hoKhau;
    }


}