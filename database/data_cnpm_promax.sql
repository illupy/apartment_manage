-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS QuanLyChungCu;
USE QuanLyChungCu;

-- Bảng Hộ Khẩu
CREATE TABLE HoKhau (
    id INT AUTO_INCREMENT PRIMARY KEY,
    soPhong VARCHAR(50) NOT NULL,
    soThanhVien INT NOT NULL,
    soXeMay INT NOT NULL,
    soOto INT NOT NULL
);





-- Bảng Nhân Khẩu
CREATE TABLE NhanKhau (
    id INT AUTO_INCREMENT PRIMARY KEY,
    soCCCD VARCHAR(12) NOT NULL UNIQUE,
    ten VARCHAR(100) NOT NULL,
    gioiTinh TINYINT(1) NOT NULL CHECK (gioiTinh IN (0, 1)),
    ngaySinh DATE NOT NULL,
    soDienThoai VARCHAR(15),
    queQuan VARCHAR(255) NOT NULL,
    laChuHo TINYINT(1) NOT NULL CHECK (laChuHo IN (0, 1)),
    quanHeChuHo VARCHAR(50),
    hoKhauId INT NOT NULL,
    FOREIGN KEY (hoKhauId) REFERENCES HoKhau(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Bảng Khoản Thu
CREATE TABLE KhoanThu (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maKhoanThu VARCHAR(50) NOT NULL UNIQUE,
    tenKhoanThu VARCHAR(100) NOT NULL,
    batBuoc TINYINT(1) NOT NULL CHECK (batBuoc IN (0, 1))
);

-- Bảng Nộp Tiền
CREATE TABLE NopTien (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ngayNop DATE NOT NULL,
    soTien DECIMAL(15, 0) NOT NULL,
    maHo INT NOT NULL,
    khoanThuId INT NOT NULL,
    FOREIGN KEY (maHo) REFERENCES HoKhau(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (khoanThuId) REFERENCES KhoanThu(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Bảng Số Tiền
CREATE TABLE SoTien (
	id INT AUTO_INCREMENT PRIMARY KEY,
    soTienNop DECIMAL(15, 0) NOT NULL,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    maHo INT NOT NULL,
    khoanThuId INT NOT NULL,
    FOREIGN KEY (maHo) REFERENCES HoKhau(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (khoanThuId) REFERENCES KhoanThu(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Bảng user
CREATE TABLE Users (
	id INT AUTO_INCREMENT PRIMARY KEY,
    username varchar(30) not null,
    password varchar(30) not null
);

-- Thêm dữ liệu mẫu cho bảng Hộ Khẩu
INSERT INTO HoKhau (soPhong, soThanhVien, soXeMay, soOto) VALUES
('A101', 4, 2, 1),
('B202', 3, 1, 0),
('C303', 5, 3, 2);

-- Thêm dữ liệu mẫu cho bảng Nhân Khẩu
INSERT INTO NhanKhau (soCCCD, ten, gioiTinh, ngaySinh, soDienThoai, queQuan, laChuHo, quanHeChuHo, hoKhauId) VALUES
('123456789012', 'Nguyen Van A', 1, '1980-05-15', '0909123456', 'Ha Noi', 1, NULL, 1),
('987654321098', 'Tran Thi B', 0, '1985-08-22', '0912345678', 'Hai Phong', 0, 'Vo', 1),
('567890123456', 'Le Van C', 1, '1990-01-10', '0987654321', 'Da Nang', 0, 'Con', 1),
('654321098765', 'Pham Thi D', 0, '1995-12-12', '0934567890', 'Quang Ninh', 1, NULL, 2);

-- Thêm dữ liệu mẫu cho bảng Khoản Thu
INSERT INTO KhoanThu (maKhoanThu, tenKhoanThu, batBuoc) VALUES
('KT01', 'Phi Bao Tri', 1),
('KT02', 'Tien Gui Xe', 1),
('KT03', 'Phi Ve Sinh', 0);

-- Thêm dữ liệu mẫu cho bảng Nộp Tiền
INSERT INTO NopTien (ngayNop, soTien, maHo, khoanThuId) VALUES
('2024-01-15', 500000, 1, 1),
('2024-01-16', 300000, 1, 2),
('2024-01-17', 200000, 2, 3);

-- Thêm dữ liệu mẫu cho bảng Số Tiền
INSERT INTO SoTien (soTienNop, ngayBatDau, ngayKetThuc, maHo, khoanThuId) VALUES
(500000, '2024-01-01', '2024-01-31', 1, 1),
(300000, '2024-01-01', '2024-01-31', 1, 2),
(200000, '2024-01-01', '2024-01-31', 2, 3);

INSERT INTO  users(username, password) values
('1', '1')
