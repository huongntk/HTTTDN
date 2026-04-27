/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author PC
 */
public class TaiKhoan {
    private int maNV;
    private String tenNV;
    private String taiKhoan;
    private String matKhau;
    private String maQuyen;
    private int trangThai;

    public TaiKhoan() {
    }

    public TaiKhoan(int maNV, String taiKhoan, String matKhau, String maQuyen, int trangThai) {
        this.maNV = maNV;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.maQuyen = maQuyen;
        this.trangThai = trangThai;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public int getMaNV() {
        return maNV;
    }

    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }
    
    public String getTaiKhoan() {
        return taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

   

    public int getTrangThai() {
        return trangThai;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    
    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Object getMaTK() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    
}
