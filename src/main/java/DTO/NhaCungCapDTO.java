package DTO;

public class NhaCungCapDTO {
    private int maNCC;
    private String tenNCC;
    private String diaChi;
    private String soDienThoai;
    private boolean trangThai;

    public NhaCungCapDTO() {}

    public NhaCungCapDTO(int maNCC, String tenNCC, String diaChi, String soDienThoai, boolean trangThai) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.trangThai = trangThai;
    }

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    @Override
    public String toString() {
        // Hiển thị tên nhà cung cấp trong JComboBox
        return tenNCC != null ? tenNCC : ("NCC #" + maNCC);
    }

}
