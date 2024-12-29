package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.HoKhauModel;

public class HoKhauService {

    // Phương thức thêm mới hộ khẩu
    public boolean add(HoKhauModel hoKhauModel) {
        String query = "INSERT INTO ho_khau(MaHo, SoThanhVien, SoPhong, SoOto, SoXeMay) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, hoKhauModel.getMaHo());
            preparedStatement.setInt(2, hoKhauModel.getSoThanhvien());
            preparedStatement.setString(3, hoKhauModel.getSoPhong());
            preparedStatement.setObject(4, hoKhauModel.getSoOto()); // Xử lý null
            preparedStatement.setObject(5, hoKhauModel.getSoXeMay()); // Xử lý null
            preparedStatement.executeUpdate();
            return true;

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Lỗi khi thêm hộ khẩu: " + e.getMessage());
        }
        return false;
    }
    
    public List<String> getAllSoPhong() throws SQLException, ClassNotFoundException {
        List<String> listSoPhong = new ArrayList<>();
        String query = "SELECT SoPhong FROM phong";

        try (Connection connection = MysqlConnection.getMysqlConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    listSoPhong.add(rs.getString("SoPhong"));
                }
            }
        }
        return listSoPhong;
    }
 
    // Phương thức xóa hộ khẩu
    public boolean delete(int maHo) throws ClassNotFoundException, SQLException {
        try (Connection connection = MysqlConnection.getMysqlConnection()) {
            // Xóa trong bảng chu_ho
            deleteFromTable(connection, "chu_ho", "MaHo", maHo);
            
            // Xóa trong bảng quan_he
            deleteFromTable(connection, "quan_he", "MaHo", maHo);

            // Xóa trong bảng ho_khau
            deleteFromTable(connection, "ho_khau", "MaHo", maHo);
        }
        return true;
    }

    private void deleteFromTable(Connection connection, String tableName, String columnName, int value) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, value);
            preparedStatement.executeUpdate();
        }
    }

    // Phương thức cập nhật hộ khẩu
    public boolean update(int maHo, String soPhong, Integer soOto, Integer soXeMay) throws ClassNotFoundException, SQLException {
        String query = "UPDATE ho_khau SET SoPhong = ?, SoOto = ?, SoXeMay = ? WHERE MaHo = ?";
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, soPhong);
            preparedStatement.setObject(2, soOto); // Xử lý null
            preparedStatement.setObject(3, soXeMay); // Xử lý null
            preparedStatement.setInt(4, maHo);
            preparedStatement.executeUpdate();
            return true;
        }
    }

    // Phương thức lấy danh sách hộ khẩu
    public List<HoKhauModel> getListHoKhau() throws ClassNotFoundException, SQLException {
        List<HoKhauModel> list = new ArrayList<>();
        String query = "SELECT * FROM ho_khau";
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                HoKhauModel hoKhauModel = new HoKhauModel(
                    rs.getInt("MaHo"),
                    rs.getInt("SoThanhVien"),
                    rs.getString("SoPhong"),
                    rs.getObject("SoOto", Integer.class), // Xử lý null
                    rs.getObject("SoXeMay", Integer.class) // Xử lý null
                );
                list.add(hoKhauModel);
            }
        }
        return list;
    }

    // Phương thức lấy hộ khẩu theo ID nhân khẩu
    public HoKhauModel getHoKhaubyIdNhanKhau(int idNhanKhau) throws ClassNotFoundException, SQLException {
        HoKhauModel hoKhau = null;
        String query = "SELECT hk.MaHo, hk.SoThanhVien, hk.SoPhong, hk.SoOto, hk.SoXeMay " +
                       "FROM ho_khau hk " +
                       "JOIN quan_he qh ON hk.MaHo = qh.MaHo " +
                       "JOIN nhan_khau nk ON qh.IDThanhVien = nk.ID " +
                       "WHERE nk.ID = ?";
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, idNhanKhau);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    hoKhau = new HoKhauModel(
                        resultSet.getInt("MaHo"),
                        resultSet.getInt("SoThanhVien"),
                        resultSet.getString("SoPhong"),
                        resultSet.getObject("SoOto", Integer.class), // Xử lý null
                        resultSet.getObject("SoXeMay", Integer.class) // Xử lý null
                    );
                }
            }
        }
        return hoKhau;
    }
}