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
    private String taiKhoan;
    private String matKhau;
    private String Quyen;
    private int trangThai;

    public TaiKhoan() {
    }

    public TaiKhoan(int maNV, String taiKhoan, String matKhau, String Quyen, int trangThai) {
        this.maNV = maNV;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.Quyen = Quyen;
        this.trangThai = trangThai;
    }

    public int getMaNV() {
        return maNV;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getQuyen() {
        return Quyen;
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

    public void setQuyen(String Quyen) {
        this.Quyen = Quyen;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    
    
}
