package DTO;

import java.sql.Time;
import java.util.Date;

public class ChamCongDTO {
    private int maChamCong;
    private int maNV;
    private Date ngayLam;       // java.util.Date
    private String caLam;
    private Time gioVao;        // java.sql.Time
    private Time gioRa;
    private String trangThai;
    private String ghiChu;

    public ChamCongDTO() {}

    public ChamCongDTO(int maChamCong, int maNV, Date ngayLam, String caLam, Time gioVao, Time gioRa, String trangThai, String ghiChu) {
        this.maChamCong = maChamCong;
        this.maNV = maNV;
        this.ngayLam = ngayLam;
        this.caLam = caLam;
        this.gioVao = gioVao;
        this.gioRa = gioRa;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    // Getters & Setters
    public int getMaChamCong() { return maChamCong; }
    public void setMaChamCong(int maChamCong) { this.maChamCong = maChamCong; }
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    public Date getNgayLam() { return ngayLam; }
    public void setNgayLam(Date ngayLam) { this.ngayLam = ngayLam; }
    public String getCaLam() { return caLam; }
    public void setCaLam(String caLam) { this.caLam = caLam; }
    public Time getGioVao() { return gioVao; }
    public void setGioVao(Time gioVao) { this.gioVao = gioVao; }
    public Time getGioRa() { return gioRa; }
    public void setGioRa(Time gioRa) { this.gioRa = gioRa; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}