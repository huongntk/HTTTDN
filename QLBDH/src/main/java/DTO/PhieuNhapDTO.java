package DTO;

import java.sql.Date;

public class PhieuNhapDTO {
    private int maPN;           // identity in DB
    private Integer maNCC;      // can be null
    private Date ngayLap;
    private double tongTien;
    private int trangThai;

    public PhieuNhapDTO() {}

    public PhieuNhapDTO(int maPN, Integer maNCC, Date ngayLap, double tongTien) {
        this.maPN = maPN;
        this.maNCC = maNCC;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
    }

    public PhieuNhapDTO(int maPN, Integer maNCC, Date ngayLap, double tongTien, int trangThai) {
        this.maPN = maPN;
        this.maNCC = maNCC;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }
    

    public int getMaPN() { return maPN; }
    public void setMaPN(int maPN) { this.maPN = maPN; }

    public Integer getMaNCC() { return maNCC; }
    public void setMaNCC(Integer maNCC) { this.maNCC = maNCC; }

    public Date getNgayLap() { return ngayLap; }
    public void setNgayLap(Date ngayLap) { this.ngayLap = ngayLap; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
    
    
}
