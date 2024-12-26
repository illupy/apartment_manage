package models;

public class LoaiKhoanThuModel {
    private int maKhoanThu;
    private String tenKhoanThu;
    private Double tongSoTienCanThu;
    private Double tongSoTienDaThu;
   
    public Double getTongSoTienCanThu() {
		return tongSoTienCanThu;
	}

	public void setTongSoTienCanThu(Double tongSoTienCanThu) {
		this.tongSoTienCanThu = tongSoTienCanThu;
	}

	public Double getTongSoTienDaThu() {
		return tongSoTienDaThu;
	}

	public void setTongSoTienDaThu(Double tongSoTienDaThu) {
		this.tongSoTienDaThu = tongSoTienDaThu;
	}

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
