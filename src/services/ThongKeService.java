package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.HoKhauModel;
import models.KhoanThuModel;
import models.LoaiKhoanThuModel;

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

	public List<LoaiKhoanThuModel> getFeeStats() throws ClassNotFoundException, SQLException {
		List<LoaiKhoanThuModel> list = new ArrayList<>();
		try (Connection connection = MysqlConnection.getMysqlConnection()) {
			String query = "select lkt.MaKhoanThu, lkt.TenKhoanThu, COALESCE(sum(kt.SoTien), 0) as TongSoTienCanThu, COALESCE(sum(nt.SoTien), 0) as TongSoTienDaThu "
					+ "from loai_khoan_thu lkt "
					+ "inner join khoan_thu kt on kt.MaKhoanThu = lkt.MaKhoanThu "
					+ "left join nop_tien nt on nt.IDKhoanThu = kt.IDKhoanThu "
					+ "group by lkt.MaKhoanThu, lkt.TenKhoanThu;";

			PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				LoaiKhoanThuModel loaiKhoanThu = new LoaiKhoanThuModel();
				loaiKhoanThu.setMaKhoanThu(rs.getInt("MaKhoanThu"));
				loaiKhoanThu.setTenKhoanThu(rs.getString("TenKhoanThu"));
				loaiKhoanThu.setTongSoTienCanThu(rs.getDouble("TongSoTienCanThu"));
				loaiKhoanThu.setTongSoTienDaThu(rs.getDouble("TongSoTienDaThu"));
				list.add(loaiKhoanThu);
			}
		}
		return list;
	}
	
	public List<LoaiKhoanThuModel> getFeeStatsByMonthYear(int month, int year) throws ClassNotFoundException, SQLException {
		List<LoaiKhoanThuModel> list = new ArrayList<>();
		try (Connection connection = MysqlConnection.getMysqlConnection()) {
			String query = "select lkt.MaKhoanThu, lkt.TenKhoanThu, COALESCE(sum(kt.SoTien), 0) as TongSoTienCanThu, COALESCE(sum(nt.SoTien), 0) as TongSoTienDaThu "
					+ "from loai_khoan_thu lkt "
					+ "inner join khoan_thu kt on kt.MaKhoanThu = lkt.MaKhoanThu "
					+ "left join nop_tien nt on nt.IDKhoanThu = kt.IDKhoanThu "
					+ "where month(kt.NgayKetThucThu) = ? and year(kt.NgayKetThucThu) = ? "
					+ "group by lkt.MaKhoanThu, lkt.TenKhoanThu;";

			PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			preparedStatement.setInt(1, month);
			preparedStatement.setInt(2, year);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				LoaiKhoanThuModel loaiKhoanThu = new LoaiKhoanThuModel();
				loaiKhoanThu.setMaKhoanThu(rs.getInt("MaKhoanThu"));
				loaiKhoanThu.setTenKhoanThu(rs.getString("TenKhoanThu"));
				loaiKhoanThu.setTongSoTienCanThu(rs.getDouble("TongSoTienCanThu"));
				loaiKhoanThu.setTongSoTienDaThu(rs.getDouble("TongSoTienDaThu"));
				list.add(loaiKhoanThu);
			}
		}
		return list;
	}
}