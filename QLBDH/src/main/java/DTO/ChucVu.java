package DTO;

public class ChucVu {
    private String maCV;
    private String tenCV;
    private double luongCoBan;

    public ChucVu() {}

    public ChucVu(String maCV, String tenCV, double luongCoBan) {
        this.maCV = maCV;
        this.tenCV = tenCV;
        this.luongCoBan = luongCoBan;
    }

    public String getMaCV() {
        return maCV;
    }

    public void setMaCV(String maCV) {
        this.maCV = maCV;
    }

    
    public String getTenCV() { return tenCV; }
    public void setTenCV(String tenCV) { this.tenCV = tenCV; }
    public double getLuongCoBan() { return luongCoBan; }
    public void setLuongCoBan(double luongCoBan) { this.luongCoBan = luongCoBan; }
}