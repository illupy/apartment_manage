package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.QuanHeModel;

public class QuanHeService {

	// checked
    public boolean add(QuanHeModel quanHeModel) throws ClassNotFoundException, SQLException {
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

    public boolean del(int maHo, int idThanhVien) throws ClassNotFoundException, SQLException {
        updateSoThanhVien(maHo, -1);
        return true;
    }
    
    private void updateSoThanhVien(int maHo, int delta) throws ClassNotFoundException, SQLException {
        try (Connection connection = MysqlConnection.getMysqlConnection();
             PreparedStatement updateStatement = connection.prepareStatement(
                     "UPDATE ho_khau SET SoThanhVien = SoThanhVien + ? WHERE maho = ?")) {

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
