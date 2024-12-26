package hust_cs.models;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class NhanKhauModel {
	int id;
	String cccd;
	String ten;
	private String gioiTinh;
	private Date ngaySinh;
	private String sdt;
	private String quanHeChuHo;
	private String ngheNghiep;
	private String queQuan;
	private String noiThuongTru;
	
	public NhanKhauModel() {}
	
    public NhanKhauModel(String cccd, String ten, String gioiTinh,Date ngaySinh, String sdt) {
        this.cccd = cccd;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
    }

    public NhanKhauModel(int id, String cccd, String ten,String gioiTinh, Date ngaySinh, String sdt) {
        this.id = id;
        this.cccd = cccd;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
    }

    public NhanKhauModel(int id, String cccd, String ten,String gioiTinh, String sdt) {
        this.id = id;
        this.cccd = cccd;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
    }
	    
    
    public NhanKhauModel(int id, String cccd, String ten, String gioiTinh, Date ngaySinh,String sdt, String ngheNghiep, String queQuan, String noiThuongTru) {
    	super();
    	this.id = id;
    	this.cccd = cccd;
    	this.ten = ten;
    	this.gioiTinh = gioiTinh;
    	this.ngaySinh = ngaySinh;
    	this.sdt = sdt;
    	this.ngheNghiep = ngheNghiep;
    	this.queQuan = queQuan;
    	this.noiThuongTru = noiThuongTru;
    	
    }
    
	public NhanKhauModel(int id, String ten, String quanHeChuHo) {
		super();
		this.id = id;
		this.ten = ten;
		this.quanHeChuHo = quanHeChuHo;
	}
    

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCccd() {
		return cccd;
	}

	public void setCccd(String cccd) {
		this.cccd = cccd;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	
	public Date getNgaySinh() {
		return ngaySinh;
	}
	
	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}
	
	private LocalDate convertToLocalDate(Date dateToConvert) {
		return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
	}
	
	public int getTuoi() {
		LocalDate birthDate = convertToLocalDate(ngaySinh);
		LocalDate currentDate = LocalDate.now();
		return Period.between(birthDate, currentDate).getYears();
	}
	
	public String getNgheNghiep() {
		return ngheNghiep;
	}
	
	public void setNgheNghiep(String ngheNghiep) {
		this.ngheNghiep = ngheNghiep;
	}
	
	public String getQueQuan() {
		return queQuan;
	}
	
	public void setQueQuan(String queQuan) {
		this.queQuan = queQuan;
	}
	
	public String getNoiThuongTru() {
		return noiThuongTru;
	}
	
	public void setNoiThuongTru(String noiThuongTru) {
		this.noiThuongTru = noiThuongTru;
	}
	
	public String getQuanHeChuHo() {
		return quanHeChuHo;
	}
	
	public void setQuanHeChuHo(String quanHeChuHo) {
		this.quanHeChuHo = quanHeChuHo;
	}
}
