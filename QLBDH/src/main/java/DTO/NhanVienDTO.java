package DTO;

public class NhanVienDTO {

    private int maNV;
    private String ho;
    private String ten;
    private String gioiTinh;
    private String soDienThoai;
    private String maCV;

    private String maQuyen;
    private boolean trangThai;

    private String tenTaiKhoan;
    private String matKhau;

    public NhanVienDTO() {
    }

    public NhanVienDTO(int maNV, String ho, String ten, String gioiTinh, String soDienThoai, String maCV, String maQuyen, boolean trangThai, String tenTaiKhoan, String matKhau) {
        this.maNV = maNV;
        this.ho = ho;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.maCV = maCV;
        this.maQuyen = maQuyen;
        this.trangThai = trangThai;
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
    }

    
    public NhanVienDTO(int maNV, String ho, String ten, String gioiTinh, String soDienThoai, String maCV, boolean trangThai, String tenTaiKhoan, String matKhau) {
        this.maNV = maNV;
        this.ho = ho;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.maCV = maCV;
        this.trangThai = trangThai;
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }

    public String getMaQuyen() {
        return maQuyen;
    }
    

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }
    public void setMaCV(String maCV) {
        this.maCV = maCV;
    }

    public String getMaCV() {
        return maCV;
    }
    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
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

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

}