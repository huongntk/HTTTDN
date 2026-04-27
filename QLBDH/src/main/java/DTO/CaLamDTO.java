package DTO;

import java.sql.Time;

public class CaLamDTO {
    private int maCa;
    private String tenCa;
    private Time gioBatDau;
    private Time gioKetThuc;
    private double soCong;
    private boolean trangThai;

    public CaLamDTO() {
    }

    public CaLamDTO(int maCa, String tenCa, Time gioBatDau, Time gioKetThuc, double soCong, boolean trangThai) {
        this.maCa = maCa;
        this.tenCa = tenCa;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.soCong = soCong;
        this.trangThai = trangThai;
    }

    public int getMaCa() {
        return maCa;
    }

    public void setMaCa(int maCa) {
        this.maCa = maCa;
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

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return tenCa;
    }
}