package models;
import java.util.Date;

public class NopTienModel {
	
	private int idNopTien;
	private int maKhoanThu;
	private double soTien;
	private Date ngayThu;
	private int idKhoanThu;
	private int maHo;
	
	public NopTienModel() {}
	
	public NopTienModel(int idNopTien, int maKhoanThu) {
		this.idNopTien = idNopTien;
		this.maKhoanThu = maKhoanThu;
	}
	
	public NopTienModel(int idNopTien, int maKhoanThu, double soTien, Date ngayThu) {
		this.idNopTien = idNopTien;
		this.maKhoanThu = maKhoanThu;
		this.soTien = soTien;
		this.ngayThu = ngayThu;
	}

	public int getIdNopTien() {
		return idNopTien;
	}
	public void setIdNopTien(int idNopTien) {
		this.idNopTien = idNopTien;
	}
	public int getMaKhoanThu() {
		return maKhoanThu;
	}
	public void setMaKhoanThu(int maKhoanThu) {
		this.maKhoanThu = maKhoanThu;
	}
	public Date getNgayThu() {
		return ngayThu;
	}
	public void setNgayThu(Date ngayThu) {
		this.ngayThu = ngayThu;
	}

	public double getSoTien() {
		return soTien;
	}

	public void setSoTien(double soTien) {
		this.soTien = soTien;
	}

	public int getIdKhoanThu() {
		return idKhoanThu;
	}

	public void setIdKhoanThu(int idKhoanThu) {
		this.idKhoanThu = idKhoanThu;
	}

	public int getMaHo() {
		return maHo;
	}

	public void setMaHo(int maHo) {
		this.maHo = maHo;
	}

}
