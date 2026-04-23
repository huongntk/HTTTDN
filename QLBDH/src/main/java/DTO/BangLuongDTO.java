package DTO;

import java.util.Date;

public class BangLuongDTO {
    private int maBL;
    private int maNV;
    private int thang;
    private int nam;
    private double luongCoBan;
    private double phuCap;
    private double thuong;
    private double phat;
    private double tongLuong;
    private Date ngayTinh;
    private String ghiChu;
    private int trangThai; // 1: đã duyệt, 0: tạm tính

    // Constructor không tham số
    public BangLuongDTO() {}

    // Constructor đầy đủ tham số (không bao gồm maBL vì tự động sinh)
    public BangLuongDTO(int maNV, int thang, int nam, double luongCoBan, double phuCap,
                        double thuong, double phat, double tongLuong, Date ngayTinh, String ghiChu, int trangThai) {
        this.maNV = maNV;
        this.thang = thang;
        this.nam = nam;
        this.luongCoBan = luongCoBan;
        this.phuCap = phuCap;
        this.thuong = thuong;
        this.phat = phat;
        this.tongLuong = tongLuong;
        this.ngayTinh = ngayTinh;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    // Getters và Setters
    public int getMaBL() {
        return maBL;
    }

    public void setMaBL(int maBL) {
        this.maBL = maBL;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public double getPhuCap() {
        return phuCap;
    }

    public void setPhuCap(double phuCap) {
        this.phuCap = phuCap;
    }

    public double getThuong() {
        return thuong;
    }

    public void setThuong(double thuong) {
        this.thuong = thuong;
    }

    public double getPhat() {
        return phat;
    }

    public void setPhat(double phat) {
        this.phat = phat;
    }

    public double getTongLuong() {
        return tongLuong;
    }

    public void setTongLuong(double tongLuong) {
        this.tongLuong = tongLuong;
    }

    public Date getNgayTinh() {
        return ngayTinh;
    }

    public void setNgayTinh(Date ngayTinh) {
        this.ngayTinh = ngayTinh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}