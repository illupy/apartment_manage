package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ThanhVienService {

    // Phương thức thêm thành viên vào bảng quan_he
    public static boolean addThanhVien(String maHo, String idThanhVien, String quanHe) {
        String query = "INSERT INTO quan_he (MaHo, IDThanhVien, QuanHe) VALUES (?, ?, ?)";
        
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, maHo);
            preparedStatement.setString(2, idThanhVien);
            preparedStatement.setString(3, quanHe);
            
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức xóa thành viên khỏi bảng quan_he
    public static boolean delThanhVien(String maHo, String idThanhVien) {
        String query = "DELETE FROM quan_he WHERE MaHo = ? AND IDThanhVien = ?";
        
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, maHo);
            preparedStatement.setString(2, idThanhVien);
            
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
