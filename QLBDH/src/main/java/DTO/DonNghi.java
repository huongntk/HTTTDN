package DTO;

import java.util.Date;

public class DonNghi {
    private int maDon;
    private int maNV;
    private String hoTen;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String lyDo;
    private String trangThai; // "Chờ duyệt", "Đã duyệt", "Từ chối"
    private Date ngayGui;

    public DonNghi() {}

    public DonNghi(int maDon, int maNV, String hoTen, Date ngayBatDau, Date ngayKetThuc, String lyDo, String trangThai, Date ngayGui) {
        this.maDon = maDon;
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.lyDo = lyDo;
        this.trangThai = trangThai;
        this.ngayGui = ngayGui;
    }

    public int getMaDon() { return maDon; }
    public void setMaDon(int maDon) { this.maDon = maDon; }
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public Date getNgayGui() { return ngayGui; }
    public void setNgayGui(Date ngayGui) { this.ngayGui = ngayGui; }
}