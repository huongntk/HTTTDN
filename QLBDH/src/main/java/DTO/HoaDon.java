package DTO;

import java.util.Date;

public class HoaDon {
    private int maHD;
    private int maKH;
    private String tenKH;
    private int maNV;
    private String tenNV;
    private Date ngayLap;
    private double tongTien;
    private int maGG;
    private String ghiChu;
    private String trangThai;
    

    public HoaDon() {
    }

    public HoaDon(int maHD, int maKH, int maNV, String tenNV, Date ngayLap, double tongTien, int maGG, String ghiChu, String trangThai) {
        this.maHD = maHD;
        this.maKH = maKH;
        
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.maGG = maGG;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    public HoaDon(int maHD, int maKH, String tenKH, int maNV, String tenNV, Date ngayLap, double tongTien, int maGG, String ghiChu, String trangThai) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.maGG = maGG;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    public HoaDon(int maHD, int maKH, int maNV, Date ngayLap, double tongTien, int maGG, String ghiChu) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.maGG = maGG;
        this.ghiChu = ghiChu;
    }

    public HoaDon(int maHD, int maKH, int maNV, Date ngayLap, double tongTien, int maGG, String ghiChu, String trangThai) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.maGG = maGG;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }
    
    

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public int getMaGG() {
        return maGG;
    }

    public void setMaGG(int maGG) {
        this.maGG = maGG;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
