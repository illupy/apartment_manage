package models;

public class HoKhauModel {
    private int maHo;
    private String tenChuHo;
    private int soThanhvien;
    private String soPhong;
    private Integer soOto; // Có thể null
    private Integer soXeMay; // Có thể null
    private double tienCanDong;
    private double tienDaDong;

    public HoKhauModel() {}

    public HoKhauModel(int soThanhVien, String soPhong) {
        this.soThanhvien = soThanhVien;
        this.soPhong = soPhong;
    }

    public HoKhauModel(int maHo, int soThanhVien, String soPhong) {
        this.maHo = maHo;
        this.soThanhvien = soThanhVien;
        this.soPhong = soPhong;
    }

    public HoKhauModel(int maHo, String tenChuHo, int soThanhVien, String soPhong) {
        this.maHo = maHo;
        this.tenChuHo = tenChuHo;
        this.soThanhvien = soThanhVien;
        this.soPhong = soPhong;
    }
    
    public HoKhauModel(int maHo, String tenChuHo, int soThanhVien, String soPhong, Integer soOto, Integer soXeMay) {
        this.maHo = maHo;
        this.tenChuHo = tenChuHo;
        this.soThanhvien = soThanhVien;
        this.soPhong = soPhong;
        this.soOto = soOto;
        this.soXeMay = soXeMay;
    }
    
    public HoKhauModel(int maHo, int soThanhVien, String soPhong, Integer soOto, Integer soXeMay) {
        this.maHo = maHo;
        this.soThanhvien = soThanhVien;
        this.soPhong = soPhong;
        this.soOto = soOto;
        this.soXeMay = soXeMay;
    }
    
    public HoKhauModel(int maHo, String soPhong, Integer soOto, Integer soXeMay) {
        this.maHo = maHo;
        this.soPhong = soPhong;
        this.soOto = soOto;
        this.soXeMay = soXeMay;
    }

    
    public int getMaHo() {
        return maHo;
    }

    public void setMaHo(int maHo) {
        this.maHo = maHo;
    }

    public int getSoThanhvien() {
        return soThanhvien;
    }

    public void setSoThanhvien(int soThanhvien) {
        this.soThanhvien = soThanhvien;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getTenChuHo() {
        return tenChuHo;
    }

    public void setTenChuHo(String tenChuHo) {
        this.tenChuHo = tenChuHo;
    }

    // Getter và Setter cho soOto
    public Integer getSoOto() {
        return soOto;
    }

    public void setSoOto(Integer soOto) {
        this.soOto = soOto;
    }

    // Getter và Setter cho soXeMay
    public Integer getSoXeMay() {
        return soXeMay;
    }

    public void setSoXeMay(Integer soXeMay) {
        this.soXeMay = soXeMay;
    }
    
    public double getTienCanDong() {
    	return tienCanDong;
    }
    
    public void setTienCanDong(double tienCanDong) {
    	this.tienCanDong = tienCanDong;
    }
    
    public double getTienDaDong() {
    	return tienDaDong;
    }
    
    public void setTienDaDong(double tienDaDong) {
    	this.tienDaDong = tienDaDong;
    }
    

    // Getter an toàn: trả về giá trị mặc định nếu là null
    public int getSoOtoSafe() {
        return (soOto == null) ? 0 : soOto;
    }

    public int getSoXeMaySafe() {
        return (soXeMay == null) ? 0 : soXeMay;
    }

}
