/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * @author ltd9605
 *         Product entity class
 */
public class Product {
    private int ID;
    private String TenSP;
    private String ThuongHieu;
    private String XuatXu;
    private int MaLoai;
    private String TenLoai;
    private String GioiTinh;
    private BigDecimal GiaBan;
    private int SoLuong;
    private String HinhAnh;
    private String MoTa;
    private int maNCC;
    private String TenNCC;
    private Boolean TrangThai;

    // Constructor rỗng (mặc định)
    public Product() {
    }

    public Product(int ID, String TenSP, String ThuongHieu, String XuatXu, int MaLoai, String TenLoai, String GioiTinh, BigDecimal GiaBan, int SoLuong, String HinhAnh, String MoTa, int maNCC, String TenNCC, Boolean TrangThai) {
        this.ID = ID;
        this.TenSP = TenSP;
        this.ThuongHieu = ThuongHieu;
        this.XuatXu = XuatXu;
        this.MaLoai = MaLoai;
        this.TenLoai = TenLoai;
        this.GioiTinh = GioiTinh;
        this.GiaBan = GiaBan;
        this.SoLuong = SoLuong;
        this.HinhAnh = HinhAnh;
        this.MoTa = MoTa;
        this.maNCC = maNCC;
        this.TenNCC = TenNCC;
        this.TrangThai = TrangThai;
    }
    // Getter và Setter cho từng thuộc tính
    public int getID() { return ID; }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String TenSP) {
        this.TenSP = TenSP;
    }

    public String getThuongHieu() {
        return ThuongHieu;
    }

    public void setThuongHieu(String ThuongHieu) {
        this.ThuongHieu = ThuongHieu;
    }

    public String getXuatXu() {
        return XuatXu;
    }

    public void setXuatXu(String XuatXu) {
        this.XuatXu = XuatXu;
    }

    public int getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(int MaLoai) {
        this.MaLoai = MaLoai;
    }

    public String getTenLoai() {
        return TenLoai;
    }

    public void setTenLoai(String TenLoai) {
        this.TenLoai = TenLoai;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public BigDecimal getGiaBan() {
        return GiaBan;
    }

    public void setGiaBan(BigDecimal GiaBan) {
        this.GiaBan = GiaBan;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String HinhAnh) {
        this.HinhAnh = HinhAnh;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String MoTa) {
        this.MoTa = MoTa;
    }

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return TenNCC;
    }

    public void setTenNCC(String TenNCC) {
        this.TenNCC = TenNCC;
    }

    public Boolean getTrangThai() {
        return TrangThai;
    }

    
    public void setTrangThai(Boolean TrangThai) {    
        this.TrangThai = TrangThai;
    }
    
      // Helper: Trả về chuỗi định dạng tiền tệ VN
    public String getGiaBanFormatted() {
        if (GiaBan == null) return "0";
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(GiaBan) + " đ";
    }

    // Helper: Trả về chỉ số (không đơn vị)
    public String getGiaBanVND() {
        if (GiaBan == null) return "0";
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(GiaBan);
    }


    @Override
    public String toString() {
        return TenSP;
    } // để combobox hiện tên

    
    
}
