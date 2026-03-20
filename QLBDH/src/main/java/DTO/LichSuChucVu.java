package DTO;

import java.util.Date;

public class LichSuChucVu {
    private int maLS;
    private int maNV;
    private String maCVu;  // chức vụ cũ
    private String maCMoi; // chức vụ mới
    private Date ngayThayDoi;
    private String ghiChu;

    public LichSuChucVu() {}

    public LichSuChucVu(int maLS, int maNV, String maCVu, String maCMoi, Date ngayThayDoi, String ghiChu) {
        this.maLS = maLS;
        this.maNV = maNV;
        this.maCVu = maCVu;
        this.maCMoi = maCMoi;
        this.ngayThayDoi = ngayThayDoi;
        this.ghiChu = ghiChu;
    }

    public int getMaLS() { return maLS; }
    public void setMaLS(int maLS) { this.maLS = maLS; }
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }

    public String getMaCVu() {
        return maCVu;
    }

    public void setMaCVu(String maCVu) {
        this.maCVu = maCVu;
    }

    public String getMaCMoi() {
        return maCMoi;
    }

    public void setMaCMoi(String maCMoi) {
        this.maCMoi = maCMoi;
    }
    
    public Date getNgayThayDoi() { return ngayThayDoi; }
    public void setNgayThayDoi(Date ngayThayDoi) { this.ngayThayDoi = ngayThayDoi; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}