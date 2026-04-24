package DTO;

import java.util.Date;

public class LichSuChucVu {
    private int maLS;
    private int maNV;
    private String maCVCu;  // chức vụ cũ
    private String maCVMoi; // chức vụ mới
    private Date ngayThayDoi;
    private String ghiChu;

    public LichSuChucVu() {}

    public LichSuChucVu(int maLS, int maNV, String maCVCu, String maCVMoi, Date ngayThayDoi, String ghiChu) {
        this.maLS = maLS;
        this.maNV = maNV;
        this.maCVCu = maCVCu;
        this.maCVMoi = maCVMoi;
        this.ngayThayDoi = ngayThayDoi;
        this.ghiChu = ghiChu;
    }

    public int getMaLS() { return maLS; }
    public void setMaLS(int maLS) { this.maLS = maLS; }
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }

    public String getMaCVCu() {
        return maCVCu;
    }

    public void setMaCVCu(String maCVCu) {
        this.maCVCu = maCVCu;
    }

    public String getMaCVMoi() {
        return maCVMoi;
    }

    public void setMaCVMoi(String maCMoi) {
        this.maCVMoi = maCMoi;
    }
    
    public Date getNgayThayDoi() { return ngayThayDoi; }
    public void setNgayThayDoi(Date ngayThayDoi) { this.ngayThayDoi = ngayThayDoi; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}