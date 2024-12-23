package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import models.HoKhauModel;
import models.NhanKhauModel;

public class NhanKhauService {
    
    // Thêm nhân khẩu mới
    public boolean addNhanKhau(NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
        String query = """
            INSERT INTO nhan_khau (CCCD, Ten, GioiTinh, NgaySinh, SDT, QueQuan)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, nhanKhauModel.getCccd());
            preparedStatement.setString(2, nhanKhauModel.getTen());
            preparedStatement.setString(3, nhanKhauModel.getGioiTinh());
            preparedStatement.setDate(4, new java.sql.Date(nhanKhauModel.getNgaySinh().getTime()));
            preparedStatement.setString(5, nhanKhauModel.getSdt());
            preparedStatement.setString(6, nhanKhauModel.getQueQuan());
//            // Xử lý các giá trị có thể null
//            if (nhanKhauModel.getMaHo() != null) {
//                preparedStatement.setInt(6, nhanKhauModel.getMaHo());
//            } else {
//                preparedStatement.setNull(6, Types.INTEGER);
//            }
//            preparedStatement.setString(7, nhanKhauModel.getQuanHeChuHo());

            preparedStatement.executeUpdate();
        }
        return true;
    }

    // Cập nhật nhân khẩu
    public boolean updateNhanKhau(NhanKhauModel nhanKhauModel) throws ClassNotFoundException, SQLException {
        String query = """
            UPDATE nhan_khau
            SET CCCD = ?, Ten = ?, GioiTinh = ?, NgaySinh = ?, SDT = ?, QueQuan = ?
            WHERE ID = ?
        """;

        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, nhanKhauModel.getCccd());
            preparedStatement.setString(2, nhanKhauModel.getTen());
            preparedStatement.setString(3, nhanKhauModel.getGioiTinh());
            preparedStatement.setDate(4, new java.sql.Date(nhanKhauModel.getNgaySinh().getTime()));
            preparedStatement.setString(5, nhanKhauModel.getSdt());
            preparedStatement.setString(6, nhanKhauModel.getQueQuan());
//            // Xử lý các giá trị có thể null
//            if (nhanKhauModel.getMaHo() != null) {
//                preparedStatement.setInt(6, nhanKhauModel.getMaHo());
//            } else {
//                preparedStatement.setNull(6, Types.INTEGER);
//            }
//            preparedStatement.setString(7, nhanKhauModel.getQuanHeChuHo());
            preparedStatement.setInt(7, nhanKhauModel.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Lấy danh sách nhân khẩu theo giới tính (hoặc tất cả nếu không lọc)
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
            	    NhanKhauModel nhanKhauModel = new NhanKhauModel(
            	    	rs.getInt("ID"),
            	        rs.getString("CCCD"),
            	        rs.getString("Ten"),
            	        rs.getString("GioiTinh"),
            	        rs.getDate("NgaySinh"),
            	        rs.getString("SDT"),
            	        rs.getString("QueQuan")
//            	        (Integer) rs.getObject("MaHo"), // Sử dụng rs.getObject để xử lý giá trị null
//            	        rs.getString("QuanHeChuHo") // Quan hệ chủ hộ có thể null
            	    );
            	    list.add(nhanKhauModel);
            	}

            }
        }
        return list;
    }

    // Lấy danh sách nhân khẩu theo mã hộ bằng cách join với bảng quan_he
    public List<NhanKhauModel> getNhanKhauByHoKhau(HoKhauModel hoKhauModel) throws ClassNotFoundException, SQLException {
        List<NhanKhauModel> nhanKhauList = new ArrayList<>();
        String sql = """
            SELECT nk.*
            FROM nhan_khau nk
            INNER JOIN quan_he qh ON nk.ID = qh.IDThanhVien
            WHERE qh.IDHoKhau = ?
        """;

        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, hoKhauModel.getMaHo());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    NhanKhauModel nhanKhauModel = new NhanKhauModel();
                    nhanKhauModel.setId(resultSet.getInt("ID"));
                    nhanKhauModel.setCccd(resultSet.getString("CCCD"));
                    nhanKhauModel.setTen(resultSet.getString("Ten"));
                    nhanKhauModel.setGioiTinh(resultSet.getString("GioiTinh"));
                    nhanKhauModel.setNgaySinh(resultSet.getDate("NgaySinh"));
                    nhanKhauModel.setSdt(resultSet.getString("SDT"));
                    nhanKhauModel.setQueQuan(resultSet.getString("QueQuan"));

                    nhanKhauList.add(nhanKhauModel);
                }
            }
        }
        return nhanKhauList;
    }

    // Xóa nhân khẩu
    public boolean deleteNhanKhau(int ID) throws ClassNotFoundException, SQLException {
        String query = "DELETE FROM nhan_khau WHERE ID = ?";

        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, ID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
