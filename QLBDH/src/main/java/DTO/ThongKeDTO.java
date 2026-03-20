package DTO;

public class ThongKeDTO {
    // Thuộc tính thời gian
    private int thang;
    private int quy;
    private int nam;

    // Thuộc tính tài chính
    private double doanhThu;
    private double giaVon;
    private double loiNhuan;

    // Thuộc tính nhân sự
    private String tenBoPhan;
    private int soLuongNV;
    private double tongChiPhi;

    // Thuộc tính kho (nếu cần)
    private String tenSP;
    private int soLuongTon;

    public ThongKeDTO() {
    }

    // Constructor cho Thống kê Lợi nhuận
    public ThongKeDTO(int quy, double doanhThu, double giaVon, double loiNhuan) {
        this.quy = quy;
        this.doanhThu = doanhThu;
        this.giaVon = giaVon;
        this.loiNhuan = loiNhuan;
    }

    // Constructor cho Thống kê Nhân sự
    public ThongKeDTO(String tenBoPhan, int soLuongNV, double tongChiPhi) {
        this.tenBoPhan = tenBoPhan;
        this.soLuongNV = soLuongNV;
        this.tongChiPhi = tongChiPhi;
    }

    // Getters và Setters
    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getQuy() { return quy; }
    public void setQuy(int quy) { this.quy = quy; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public double getDoanhThu() { return doanhThu; }
    public void setDoanhThu(double doanhThu) { this.doanhThu = doanhThu; }

    public double getGiaVon() { return giaVon; }
    public void setGiaVon(double giaVon) { this.giaVon = giaVon; }

    public double getLoiNhuan() { return loiNhuan; }
    public void setLoiNhuan(double loiNhuan) { this.loiNhuan = loiNhuan; }

    public String getTenBoPhan() { return tenBoPhan; }
    public void setTenBoPhan(String tenBoPhan) { this.tenBoPhan = tenBoPhan; }

    public int getSoLuongNV() { return soLuongNV; }
    public void setSoLuongNV(int soLuongNV) { this.soLuongNV = soLuongNV; }

    public double getTongChiPhi() { return tongChiPhi; }
    public void setTongChiPhi(double tongChiPhi) { this.tongChiPhi = tongChiPhi; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public int getSoLuongTon() { return soLuongTon; }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon = soLuongTon; }
}