package DTO;

public class CTPhieuNhapDTO {
    private int maPN;
    private int id;          // ID sản phẩm (SanPham.ID)
    private int soLuong;
    private int giaNhap;     // map vào cột GiaBan trong DB
    private int thanhTien;   // soLuong * giaNhap, lưu xuống DB

    public CTPhieuNhapDTO() {}

    public CTPhieuNhapDTO(int maPN, int id, int soLuong, int giaNhap, int thanhTien) {
        this.maPN = maPN;
        this.id = id;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.thanhTien = thanhTien;
    }

    public int getMaPN() { return maPN; }
    public void setMaPN(int maPN) { this.maPN = maPN; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public int getGiaNhap() { return giaNhap; }
    public void setGiaNhap(int giaNhap) { this.giaNhap = giaNhap; }

    public int getThanhTien() { return thanhTien; }
    public void setThanhTien(int thanhTien) { this.thanhTien = thanhTien; }
}
