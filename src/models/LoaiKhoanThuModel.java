package models;

public class LoaiKhoanThuModel {
    private int maKhoanThu;
    private String tenKhoanThu;
    

    public LoaiKhoanThuModel() {
		super();
	}

	public LoaiKhoanThuModel(int maKhoanThu, String tenKhoanThu) {
        this.maKhoanThu = maKhoanThu;
        this.tenKhoanThu = tenKhoanThu;
    }

    public int getMaKhoanThu() {
        return maKhoanThu;
    }

    public String getTenKhoanThu() {
        return tenKhoanThu;
    }

    public void setMaKhoanThu(int maKhoanThu) {
		this.maKhoanThu = maKhoanThu;
	}

	public void setTenKhoanThu(String tenKhoanThu) {
		this.tenKhoanThu = tenKhoanThu;
	}

	@Override
    public String toString() {
        return maKhoanThu + " - " + tenKhoanThu;
    }
}
