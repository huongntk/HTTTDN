package DTO;

import java.sql.Date;
import java.sql.Time;

public class LichLamViecDTO {
    private int maLich;
    private int maNV;
    private int maCa;
    private Date ngayLam;
    private String ghiChu;
    private boolean trangThai;

    // Field phụ để hiển thị join
    private String hoTenNhanVien;
    private String tenCa;
    private Time gioBatDau;
    private Time gioKetThuc;
    private double soCong;
    private String trangThaiChamCong;
    private Time gioVao;
    private Time gioRa;

    public LichLamViecDTO() {
    }

    public int getMaLich() {
        return maLich;
    }

    public void setMaLich(int maLich) {
        this.maLich = maLich;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public int getMaCa() {
        return maCa;
    }

    public void setMaCa(int maCa) {
        this.maCa = maCa;
    }

    public Date getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(Date ngayLam) {
        this.ngayLam = ngayLam;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getHoTenNhanVien() {
        return hoTenNhanVien;
    }

    public void setHoTenNhanVien(String hoTenNhanVien) {
        this.hoTenNhanVien = hoTenNhanVien;
    }

    public String getTenCa() {
        return tenCa;
    }

    public void setTenCa(String tenCa) {
        this.tenCa = tenCa;
    }

    public Time getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(Time gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public Time getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(Time gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public double getSoCong() {
        return soCong;
    }

    public void setSoCong(double soCong) {
        this.soCong = soCong;
    }

    public String getTrangThaiChamCong() {
        return trangThaiChamCong;
    }

    public void setTrangThaiChamCong(String trangThaiChamCong) {
        this.trangThaiChamCong = trangThaiChamCong;
    }

    public Time getGioVao() {
        return gioVao;
    }

    public void setGioVao(Time gioVao) {
        this.gioVao = gioVao;
    }

    public Time getGioRa() {
        return gioRa;
    }

    public void setGioRa(Time gioRa) {
        this.gioRa = gioRa;
    }
}