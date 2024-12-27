package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.QuanHeModel;

public class QuanHeService {

    // Phương thức thêm quan hệ vào bảng quan_he
    public boolean add(QuanHeModel quanHeModel) throws ClassNotFoundException, SQLException {
        // Kiểm tra xem ID thành viên có tồn tại trong bảng nhan_khau không
        if (!isValidNhanKhauId(quanHeModel.getIdThanhVien())) {
            throw new SQLException("ID thành viên không tồn tại!");
        }

        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement insertStatement = connection.prepareStatement(
                     "INSERT INTO quan_he (MaHo, IDThanhVien, QuanHe) VALUES (?, ?, ?)")) {

            insertStatement.setInt(1, quanHeModel.getMaHo());
            insertStatement.setInt(2, quanHeModel.getIdThanhVien());
            insertStatement.setString(3, quanHeModel.getQuanHe());
            insertStatement.executeUpdate();
        }

        updateSoThanhVien(quanHeModel.getMaHo(), 1);
        return true;
    }

    // Phương thức kiểm tra ID thành viên
    private boolean isValidNhanKhauId(int idThanhVien) throws ClassNotFoundException, SQLException {
        String query = "SELECT COUNT(*) FROM nhan_khau WHERE ID = ?";
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement checkStatement = connection.prepareStatement(query)) {

            checkStatement.setInt(1, idThanhVien);
            try (ResultSet rs = checkStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Trả về true nếu tìm thấy ID
                }
            }
        }
        return false; // Trả về false nếu không tìm thấy
    }

    // Phương thức xóa quan hệ khỏi bảng quan_he
    public boolean del(int maHo, int idThanhVien) throws ClassNotFoundException, SQLException {
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(
                     "DELETE FROM quan_he WHERE MaHo = ? AND IDThanhVien = ?")) {

            deleteStatement.setInt(1, maHo);
            deleteStatement.setInt(2, idThanhVien);
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                updateSoThanhVien(maHo, -1);
                return true;
            }
        }
        return false;
    }

    // Cập nhật số thành viên trong bảng ho_khau
    private void updateSoThanhVien(int maHo, int delta) throws ClassNotFoundException, SQLException {
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement updateStatement = connection.prepareStatement(
                     "UPDATE ho_khau SET SoThanhVien = SoThanhVien + ? WHERE MaHo = ?")) {

            updateStatement.setInt(1, delta);
            updateStatement.setInt(2, maHo);
            updateStatement.executeUpdate();
        }
    }
    
    public List<QuanHeModel> getListQuanHe() throws ClassNotFoundException, SQLException {
        List<QuanHeModel> list = new ArrayList<>();

        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM quan_he");
             ResultSet rs = selectStatement.executeQuery()) {

            while (rs.next()) {
                QuanHeModel quanHeModel = new QuanHeModel();
                quanHeModel.setMaHo(rs.getInt("MaHo"));
                quanHeModel.setIdThanhVien(rs.getInt("IDThanhVien"));
                quanHeModel.setQuanHe(rs.getString("QuanHe"));
                list.add(quanHeModel);
            }
        }

        return list;
    }
    
    // Phương thức cập nhật chủ hộ trong bảng quan_he
    public void updateChuHoInQuanHe(int maHo, int idChuHo) throws SQLException, ClassNotFoundException {
        String query = "UPDATE quan_he SET IDThanhVien = ? WHERE MaHo = ? AND QuanHe = 'chủ hộ'";

        try (Connection connection = MysqlConnection.getMysqlConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, idChuHo);
                stmt.setInt(2, maHo);
                stmt.executeUpdate();
            }
        }
    }
    
    public boolean changeRelationship(int currentMaHo, int newMaHo, int idThanhVien, String newQuanHe) throws ClassNotFoundException, SQLException {
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement updateStatement = connection.prepareStatement(
                     "UPDATE quan_he SET MaHo = ?, QuanHe = ? WHERE MaHo = ? AND IDThanhVien = ?")) {

            updateStatement.setInt(1, newMaHo);
            updateStatement.setString(2, newQuanHe);
            updateStatement.setInt(3, currentMaHo);
            updateStatement.setInt(4, idThanhVien);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                updateSoThanhVien(currentMaHo, -1);
                updateSoThanhVien(newMaHo, 1);
                return true; 
            }
        }
        return false; 
    }


}
