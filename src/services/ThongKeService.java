package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import models.HoKhauModel;
import models.KhoanThuModel;

public class ThongKeService {

	public List<HoKhauModel> getHouseholdsStats() throws ClassNotFoundException, SQLException {
		List<HoKhauModel> list = new ArrayList<>();
		String query = "select hk.MaHo, nk.Ten, " + "COALESCE(sum(kt.SoTien), 0) as 'Tiền cần đóng', "
				+ "COALESCE(sum(nt.SoTien), 0) as 'Tiền đã đóng' " + "from ho_khau hk "
				+ "inner join chu_ho ch on ch.MaHo = hk.MaHo " + "inner join nhan_khau nk on ch.IDChuHo = nk.ID "
				+ "left join khoan_thu kt on kt.MaHo = hk.MaHo " + "left join nop_tien nt on nt.IDKhoanThu = kt.IDKhoanThu "
				+ "group by hk.MaHo, nk.Ten";

		try (Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery()) {

			while (rs.next()) {
				HoKhauModel hoKhau = new HoKhauModel();
				hoKhau.setMaHo(rs.getInt("MaHo"));
				hoKhau.setTenChuHo(rs.getString("Ten"));
				hoKhau.setTienCanDong(rs.getDouble("Tiền cần đóng"));
				hoKhau.setTienDaDong(rs.getDouble("Tiền đã đóng"));
				list.add(hoKhau);
			}
		} catch (SQLException e) {
			// Ghi log lỗi hoặc xử lý ngoại lệ
			throw e;
		}
		return list;
	}

	public List<HoKhauModel> getHouseholdsStatsByMonthYear(int month, int year)
			throws ClassNotFoundException, SQLException {
		List<HoKhauModel> list = new ArrayList<>();

		String query = "select hk.MaHo, nk.Ten, COALESCE(sum(kt.SoTien), 0) as 'Tiền cần đóng', COALESCE(sum(nt.SoTien), 0) as 'Tiền đã đóng' "
				+ "from ho_khau hk " + "inner join chu_ho ch on ch.MaHo = hk.MaHo "
				+ "inner join nhan_khau nk on ch.IDChuHo = nk.ID " + "left join khoan_thu kt on kt.MaHo = hk.MaHo "
				+ "left join nop_tien nt on nt.IDKhoanThu = kt.IDKhoanThu "
				+ "where month(kt.NgayBatDauThu) = ? and year(kt.NgayKetThucThu) = ? "
				+ "group by hk.MaHo, nk.Ten";

		try (Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setInt(1, month);
			preparedStatement.setInt(2, year);

			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					HoKhauModel hoKhau = new HoKhauModel();
					hoKhau.setMaHo(rs.getInt("MaHo"));
					hoKhau.setTenChuHo(rs.getString("Ten"));
					hoKhau.setTienCanDong(rs.getDouble("Tiền cần đóng"));
					hoKhau.setTienDaDong(rs.getDouble("Tiền đã đóng"));
					list.add(hoKhau);
				}
			}
		} catch (SQLException e) {
			// Ghi log lỗi hoặc xử lý ngoại lệ
			throw e;
		}
		return list;
	}
	
	public List<KhoanThuModel> getHouseholdDetailStats(int maHo) throws ClassNotFoundException, SQLException {
		List<KhoanThuModel> list = new ArrayList<>();
		
		String query = "select lkt.MaKhoanThu, lkt.TenKhoanThu, sum(kt.SoTien) as TongTien "
				+ "from khoan_thu kt "
				+ "inner join loai_khoan_thu lkt on kt.MaKhoanThu = lkt.MaKhoanThu "
				+ "where kt.MaHo = ? "
				+ "group by lkt.TenKhoanThu, lkt.MaKhoanThu;";

		try (Connection connection = MysqlConnection.getMysqlConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			
			preparedStatement.setInt(1, maHo);

			try (ResultSet rs = preparedStatement.executeQuery()) {
				while (rs.next()) {
					KhoanThuModel khoanThu = new KhoanThuModel();
					khoanThu.setMaKhoanThu(rs.getInt("MaKhoanThu"));
					khoanThu.setTenKhoanThu(rs.getString("TenKhoanThu"));
					khoanThu.setSoTien(rs.getDouble("TongTien"));
					list.add(khoanThu);
				}
			}
		} catch (SQLException e) {
			// Ghi log lỗi hoặc xử lý ngoại lệ
			throw e;
		}
		return list;
	}

	public List<KhoanThuModel> getFeeStats() throws ClassNotFoundException, SQLException {
		List<KhoanThuModel> list = new ArrayList<>();
		try (Connection connection = MysqlConnection.getMysqlConnection()) {
			String query = "select lkt.TenKhoanThu, sum(kt.SoTien) as 'Tổng số tiền' " + "from khoan_thu kt "
					+ "join loai_khoan_thu lkt on lkt.MaKhoanThu = kt.MaKhoanThu " + "group by kt.MaKhoanThu;";

			PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				KhoanThuModel khoanthu = new KhoanThuModel();
				khoanthu.setTenKhoanThu(rs.getString("TenKhoanThu"));
				khoanthu.setSoTien(rs.getDouble("Tổng số tiền"));
				list.add(khoanthu);
			}
		}
		return list;
	}
}