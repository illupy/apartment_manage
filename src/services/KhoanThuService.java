package services;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.KhoanThuModel;
import models.LoaiKhoanThuModel;

public class KhoanThuService {

	public boolean add(KhoanThuModel khoanThuModel) throws ClassNotFoundException, SQLException {
		try (Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(
						"INSERT INTO khoan_thu(MaKhoanThu, SoTien, NgayBatDauThu, NgayKetThucThu, MaHo) "
								+ "VALUES (?, ?, ?, ?, ?);",
						Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setInt(1, khoanThuModel.getMaKhoanThu());
			preparedStatement.setDouble(2, khoanThuModel.getSoTien());
			preparedStatement.setDate(3, (Date) khoanThuModel.getNgayBatDau());
			preparedStatement.setDate(4, (Date) khoanThuModel.getNgayKetThuc());
			preparedStatement.setInt(5, khoanThuModel.getMaHo());

			preparedStatement.executeUpdate();
		}

		return true;
	}

	public boolean update(KhoanThuModel khoanThuModel) throws ClassNotFoundException, SQLException {
		try (Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(
						"UPDATE khoan_thu " + "SET MaKhoanThu=?, SoTien=?, NgayBatDauThu=?, NgayKetThucThu=?, MaHo=? "
								+ "WHERE IDKhoanThu=?;",
						Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setInt(1, khoanThuModel.getMaKhoanThu());
			preparedStatement.setDouble(2, khoanThuModel.getSoTien());
			preparedStatement.setDate(3, (Date) khoanThuModel.getNgayBatDau());
			preparedStatement.setDate(4, (Date) khoanThuModel.getNgayKetThuc());
			preparedStatement.setInt(5, khoanThuModel.getMaHo());
			preparedStatement.setInt(6, khoanThuModel.getIdKhoanThu());

			preparedStatement.executeUpdate();
		}

		return true;
	}

	// checked
	public boolean del(int idKhoanThu) throws ClassNotFoundException, SQLException {
		try (Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement checkStatement = connection
						.prepareStatement("SELECT * FROM nop_tien WHERE IDKhoanThu = ?");
				PreparedStatement deleteStatement = connection
						.prepareStatement("DELETE FROM nop_tien WHERE IDKhoanThu = ?");
				PreparedStatement mainDeleteStatement = connection
						.prepareStatement("DELETE FROM khoan_thu WHERE IDKhoanThu = ?")) {

			checkStatement.setInt(1, idKhoanThu);
			ResultSet rs = checkStatement.executeQuery();

			while (rs.next()) {
				deleteStatement.setInt(1, idKhoanThu);
				deleteStatement.executeUpdate();
			}

			mainDeleteStatement.setInt(1, idKhoanThu);
			mainDeleteStatement.executeUpdate();
		}

		return true;
	}

	// checked
	public List<KhoanThuModel> getListKhoanThu() throws ClassNotFoundException, SQLException {
		List<KhoanThuModel> list = new ArrayList<>();

		Connection connection = MysqlConnection.getMysqlConnection();
		String query = "select kt.IDKhoanThu, lkt.TenKhoanThu, kt.SoTien, kt.NgayBatDauThu, kt.NgayKetThucThu, kt.MaHo, lkt.BatBuoc "
				+ "from khoan_thu kt " + "join loai_khoan_thu lkt on kt.MaKhoanThu = lkt.MaKhoanThu";
		PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			KhoanThuModel khoanThuModel = new KhoanThuModel();
			khoanThuModel.setIDKhoanThu(rs.getInt("IDKhoanThu"));
			khoanThuModel.setLoaiKhoanThu(rs.getInt("BatBuoc"));
			khoanThuModel.setNgayBatDau(rs.getDate("NgayBatDauThu"));
			khoanThuModel.setNgayKetThuc(rs.getDate("NgayKetThucThu"));
			khoanThuModel.setSoTien(rs.getDouble("SoTien"));
			khoanThuModel.setTenKhoanThu(rs.getString("TenKhoanThu"));
			khoanThuModel.setMaHo(rs.getInt("MaHo"));
			list.add(khoanThuModel);
		}
		preparedStatement.close();
		connection.close();
		return list;
	}

	public List<LoaiKhoanThuModel> getLoaiKhoanThu() throws ClassNotFoundException, SQLException {
		List<LoaiKhoanThuModel> list = new ArrayList<>();

		Connection connection = MysqlConnection.getMysqlConnection();
		String query = "select * from loai_khoan_thu;";
		PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query);
		ResultSet rs = preparedStatement.executeQuery();

		while (rs.next()) {
			LoaiKhoanThuModel loaiKhoanThu = new LoaiKhoanThuModel();
			loaiKhoanThu.setMaKhoanThu(rs.getInt("MaKhoanThu"));
			loaiKhoanThu.setTenKhoanThu(rs.getString("TenKhoanThu"));
			list.add(loaiKhoanThu);
		}
		preparedStatement.close();
		connection.close();
		return list;
	}

	public boolean addLoaiKhoanThu(String tenKhoanThu, int BatBuoc) throws ClassNotFoundException, SQLException {
		try (Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(
						"INSERT INTO loai_khoan_thu(TenKhoanThu, BatBuoc) "
								+ "VALUES (?, ?);",
						Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, tenKhoanThu);
			preparedStatement.setInt(2, BatBuoc);

			preparedStatement.executeUpdate();
		}

		return true;
	}

}
