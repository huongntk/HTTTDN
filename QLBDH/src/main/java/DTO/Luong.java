package DTO;

public class Luong {
    private int maLuong;
    private int maNV;
    private int thang;
    private int nam;
    private double luongCoBan;
    private double phuCap;
    private double thuong;
    private double khauTru;
    private double thucLinh;
    private int trangThai; // 0: chưa tính, 1: đã tính

    public Luong() {}

    public Luong(int maLuong, int maNV, int thang, int nam, double luongCoBan, double phuCap, double thuong, double khauTru, double thucLinh, int trangThai) {
        this.maLuong = maLuong;
        this.maNV = maNV;
        this.thang = thang;
        this.nam = nam;
        this.luongCoBan = luongCoBan;
        this.phuCap = phuCap;
        this.thuong = thuong;
        this.khauTru = khauTru;
        this.thucLinh = thucLinh;
        this.trangThai = trangThai;
    }

    public int getMaLuong() { return maLuong; }
    public void setMaLuong(int maLuong) { this.maLuong = maLuong; }
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }
    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }
    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }
    public double getPhuCap() { return phuCap; }
    public void setPhuCap(double phuCap) { this.phuCap = phuCap; }
    public double getThuong() { return thuong; }
    public void setThuong(double thuong) { this.thuong = thuong; }
    public double getKhauTru() { return khauTru; }
    public void setKhauTru(double khauTru) { this.khauTru = khauTru; }
    public double getThucLinh() { return thucLinh; }
    public void setThucLinh(double thucLinh) { this.thucLinh = thucLinh; }
    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}