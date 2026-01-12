/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

public class PhanQuyen {
    private String Quyen;
    private boolean QLNhapHang;
    private boolean QLSanPham;
    private boolean QLNhanVien;
    private boolean QLKhachHang;
    private boolean QLThongKe;
    private boolean QLKhuyenMai;
    private boolean QLPhanQuyen;
    private boolean QLBanHang;
    

    public PhanQuyen() {
    }

    public PhanQuyen(String Quyen, boolean QLNhapHang, boolean QLSanPham, boolean QLNhanVien, boolean QLKhachHang, boolean QLThongKe, boolean QLKhuyenMai, boolean QLBanHang, boolean QLPhanQuyen) {
        this.Quyen = Quyen;
        this.QLNhapHang = QLNhapHang;
        this.QLSanPham = QLSanPham;
        this.QLNhanVien = QLNhanVien;
        this.QLKhachHang = QLKhachHang;
        this.QLThongKe = QLThongKe;
        this.QLKhuyenMai = QLKhuyenMai;
        this.QLPhanQuyen = QLPhanQuyen;
        this.QLBanHang = QLBanHang;
        
    }
 

    public String getQuyen() {
        return Quyen;
    }

    public void setQuyen(String Quyen) {
        this.Quyen = Quyen;
    }

    public boolean isQLNhapHang() {
        return QLNhapHang;
    }

    public void setQLNhapHang(boolean QLNhapHang) {
        this.QLNhapHang = QLNhapHang;
    }

    public boolean isQLNhanVien() {
        return QLNhanVien;
    }

    public void setQLNhanVien(boolean QLNhanVien) {
        this.QLNhanVien = QLNhanVien;
    }

    public boolean isQLKhachHang() {
        return QLKhachHang;
    }

    public void setQLKhachHang(boolean QLKhachHang) {
        this.QLKhachHang = QLKhachHang;
    }

    public boolean isQLThongKe() {
        return QLThongKe;
    }

    public void setQLThongKe(boolean QLThongKe) {
        this.QLThongKe = QLThongKe;
    }

    public boolean isQLKhuyenMai() {
        return QLKhuyenMai;
    }

    public void setQLKhuyenMai(boolean QLKhuyenMai) {
        this.QLKhuyenMai = QLKhuyenMai;
    }

    public boolean isQLBanHang() {
        return QLBanHang;
    }

    public void setQLBanHang(boolean QLBanHang) {
        this.QLBanHang = QLBanHang;
    }

    public boolean isQLSanPham() {
        return QLSanPham;
    }

    public void setQLSanPham(boolean QLSanPham) {
        this.QLSanPham = QLSanPham;
    }
    
    public boolean isQLPhanQuyen() {
        return QLPhanQuyen;
    }

    public void setQLPhanQuyen(boolean QLPhanQuyen) {
        this.QLPhanQuyen = QLPhanQuyen;
    }

    
}
