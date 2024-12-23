package models;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class NhanKhauModel {
    private int id;
    private String cccd;
    private String ten;
    private String gioiTinh;
    private Date ngaySinh; // Sử dụng LocalDate thay vì Date
    private String sdt;
    private String queQuan;
    private Integer maHo; // Mã hộ khẩu, cho phép null
    private String quanHeChuHo; // Quan hệ với chủ hộ, cho phép null

    
    public NhanKhauModel() {};
    
    // Constructor không bắt buộc mã hộ và quan hệ chủ hộ
    public NhanKhauModel(String cccd, String ten, String gioiTinh, Date ngaySinh, String sdt, String queQuan) {
        this.cccd = cccd;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.queQuan = queQuan;
        this.maHo = null; // Chưa nhập
        this.quanHeChuHo = null; // Chưa nhập
    }

    // Constructor đầy đủ (trường hợp cần dùng sau khi đã có mã hộ và quan hệ)
    public NhanKhauModel(int id, String cccd, String ten, String gioiTinh, Date ngaySinh, String sdt, 
                         Integer maHo, String quanHeChuHo) {
        this.id = id;  // ID tự động tăng trong cơ sở dữ liệu
        this.cccd = cccd;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.maHo = maHo;  // Có thể là null nếu chưa điền
        this.quanHeChuHo = quanHeChuHo; // Có thể là null nếu chưa điền
    }

    public NhanKhauModel(int id, String cccd, String ten, String gioiTinh, Date ngaySinh, String sdt, String quanHeChuHo) {
    	this.id = id;  // ID tự động tăng trong cơ sở dữ liệu
    	this.cccd = cccd;
    	this.ten = ten;
    	this.gioiTinh = gioiTinh;
    	this.ngaySinh = ngaySinh;
    	this.sdt = sdt;
    	this.quanHeChuHo = quanHeChuHo; // Có thể là null nếu chưa điền
    }

    public NhanKhauModel(int id, String cccd, String ten, String gioiTinh, Date ngaySinh, String sdt) {
    	this.id = id;  // ID tự động tăng trong cơ sở dữ liệu
    	this.cccd = cccd;
    	this.ten = ten;
    	this.gioiTinh = gioiTinh;
    	this.ngaySinh = ngaySinh;
    	this.sdt = sdt;
    }
    
    public NhanKhauModel(String cccd, String ten, String gioiTinh, Date ngaySinh, String sdt, 
            Integer maHo, String quanHeChuHo) {
    	this.cccd = cccd;
    	this.ten = ten;
    	this.gioiTinh = gioiTinh;
    	this.ngaySinh = ngaySinh;
    	this.sdt = sdt;
    	this.maHo = maHo;  // Có thể là null nếu chưa điền
    	this.quanHeChuHo = quanHeChuHo; // Có thể là null nếu chưa điền
    }
    
    // Getters và Setters
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

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }
    
    public String getQueQuan() {
        return queQuan;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Integer getMaHo() {
        return maHo;
    }

    public void setMaHo(Integer maHo) {
        this.maHo = maHo;
    }

    public String getQuanHeChuHo() {
        return quanHeChuHo;
    }

    public void setQuanHeChuHo(String quanHeChuHo) {
        this.quanHeChuHo = quanHeChuHo;
    }

    
	private LocalDate convertToLocalDate(Date dateToConvert) {
		return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
	}
	
	public int getTuoi() {
		LocalDate birthDate = convertToLocalDate(ngaySinh);
		LocalDate currentDate = LocalDate.now();
		return Period.between(birthDate, currentDate).getYears();
	}
    

    // Kiểm tra trạng thái
    public boolean daCoThongTinHoKhau() {
        return this.maHo != null && this.quanHeChuHo != null && !this.quanHeChuHo.isEmpty();
    }
}
