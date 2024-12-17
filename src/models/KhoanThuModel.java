package models;

import java.util.Date;

public class KhoanThuModel {
	private int idKhoanThu;
	private String tenKhoanThu;
	private double soTien;
	private int loaiKhoanThu; // tự nguyện là 1 bắt buộc là 0
	private Date ngayBatDau;
	private Date ngayKetThuc;
	private int maHo;
	private int maKhoanThu;
	
	public KhoanThuModel() {}

	public KhoanThuModel(String tenKhoanThu, double soTien, int loaiKhoanThu ) {
		this.tenKhoanThu=tenKhoanThu;
		this.soTien = soTien;
		this.loaiKhoanThu = loaiKhoanThu;
	}
	
	public KhoanThuModel(int idKhoanThu ,String tenKhoanThu, double soTien, int loaiKhoanThu ) {
		this.idKhoanThu = idKhoanThu;
		this.tenKhoanThu=tenKhoanThu;
		this.soTien = soTien;
		this.loaiKhoanThu = loaiKhoanThu;
	}	

	public KhoanThuModel(int idKhoanThu, String tenKhoanThu, double soTien, Date ngayBatDau, Date ngayKetThuc,
			int maHo) {
		this.idKhoanThu = idKhoanThu;
		this.tenKhoanThu = tenKhoanThu;
		this.soTien = soTien;
		this.ngayBatDau = ngayBatDau;
		this.ngayKetThuc = ngayKetThuc;
		this.maHo = maHo;
	}

	public int getIdKhoanThu() {
		return idKhoanThu;
	}

	public void setIDKhoanThu(int idKhoanThu) {
		this.idKhoanThu = idKhoanThu;
	}

	public String getTenKhoanThu() {
		return tenKhoanThu;
	}

	public void setTenKhoanThu(String tenKhoanThu) {
		this.tenKhoanThu = tenKhoanThu;
	}

	public double getSoTien() {
		return soTien;
	}

	public void setSoTien(double soTien) {
		this.soTien = soTien;
	}

	public int getLoaiKhoanThu() {
		return loaiKhoanThu;
	}

	public void setLoaiKhoanThu(int loaiKhoanThu) {
		this.loaiKhoanThu = loaiKhoanThu;
	}
	
	public Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public int getMaKhoanThu() {
		return maKhoanThu;
	}
	
	public void setMaKhoanThu(int maKhoanThu) {
		this.maKhoanThu = maKhoanThu;
	}

	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}
	
	public int getMaHo() {
		return maHo;
	}

	public void setMaHo(int maHo) {
		this.maHo = maHo;
	}
	
	

	
}
