package BUS;

import DAO.LuongCoBanTheoChucVuDAO;
import DAO.NhanVienDAO;
import DTO.ChucVu;
import DTO.LichSuChucVu;
import DTO.NhanVienDTO;

import java.util.ArrayList;

public class NhanVienBUS {

    private NhanVienDAO nhanVienDAO;
    private LichSuChucVuBUS lichSuBUS;
    private ChucVuBUS chucVuBUS;
    
    public NhanVienBUS() {
        nhanVienDAO = new NhanVienDAO();
        lichSuBUS = new LichSuChucVuBUS();
        chucVuBUS = new ChucVuBUS();
    }

    public ArrayList<NhanVienDTO> layDanhSachNhanVien() {
        return nhanVienDAO.layDanhSachNhanVien();
    }

    private String validateFields(NhanVienDTO nv, boolean isAdding) {
        if (nv.getHo().trim().isEmpty() || nv.getTen().trim().isEmpty()) {
            return "Họ và tên không được để trống";
        }
        if (nv.getSoDienThoai().trim().isEmpty()) {
            return "Số điện thoại không được để trống";
        }
        if (!nv.getSoDienThoai().matches("^0\\d{9,10}$")) {
            return "Số điện thoại không hợp lệ (phải 10-11 số, bắt đầu bằng 0)";
        }
        if (nv.getTenTaiKhoan().trim().isEmpty()) {
            return "Tên tài khoản không được để trống";
        }
        if (nv.getMatKhau().trim().isEmpty()) {
            return "Mật khẩu không được để trống";
        }
        
        int maNVCheck = isAdding ? 0 : nv.getMaNV();
        if (nhanVienDAO.kiemTraTenTaiKhoan(nv.getTenTaiKhoan(), maNVCheck)) {
            return "Tên tài khoản đã tồn tại. Vui lòng chọn tên khác.";
        }
        
        return null; 
    }

    public String themNhanVien(NhanVienDTO nv) {
        String validationError = validateFields(nv, true);
        if (validationError != null) {
            return validationError;
        }

        if (nhanVienDAO.themNhanVien(nv)) {
            return "Thêm nhân viên thành công";
        } else {
            return "Thêm nhân viên thất bại";
        }
    }

    public String suaNhanVien(NhanVienDTO nv) {
        String validationError = validateFields(nv, false);
        if (validationError != null) {
            return validationError;
        }

        if (nhanVienDAO.suaNhanVien(nv)) {
            return "Sửa thông tin nhân viên thành công";
        } else {
            return "Sửa thông tin nhân viên thất bại";
        }
    }

    public String xoaNhanVien(int maNV) {
        if (maNV <= 0) {
            return "Mã nhân viên không hợp lệ";
        }
        
        if (nhanVienDAO.xoaNhanVien(maNV)) {
            return "Vô hiệu hóa nhân viên thành công";
        } else {
            return "Vô hiệu hóa nhân viên thất bại";
        }
    }

    public ArrayList<NhanVienDTO> timKiemNhanVien(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return nhanVienDAO.layDanhSachNhanVien();
        }
        return nhanVienDAO.timKiemNhanVien(tuKhoa);
    }
    
    public ArrayList<String> layDanhSachChucVu() {
        ArrayList<String> listTen = new ArrayList<>();
        ArrayList<ChucVu> list = chucVuBUS.layDanhSachChucVu();
        for (ChucVu cv : list) {
            listTen.add(cv.getTenCV());
        }
        return listTen;
    }

    public NhanVienDTO layNhanVienTheoMa(int maNV) {
        try {
            return nhanVienDAO.selectById(maNV);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean capNhatChucVu(int maNV, String chucVuMoi, String ngayHieuLuc, String lyDo) {
        // Lấy nhân viên hiện tại
        NhanVienDTO nv = layNhanVienTheoMa(maNV);
        if (nv == null) return false;

        // Lấy mã chức vụ cũ và mới
        ChucVu cvCu = chucVuBUS.layChucVuTheoTen(nv.getMaCV()); // cần thêm phương thức
        ChucVu cvMoi = chucVuBUS.layChucVuTheoTen(chucVuMoi);
        if (cvCu == null || cvMoi == null) return false;

        // Cập nhật chức vụ cho nhân viên
        nv.setMaCV(chucVuMoi);
        boolean updateNV = nhanVienDAO.update(nv) > 0;
        if (!updateNV) return false;

        // Ghi lịch sử
        LichSuChucVu ls = new LichSuChucVu();
        ls.setMaNV(maNV);
        ls.setMaCVu(cvCu.getMaCV());
        ls.setMaCMoi(cvMoi.getMaCV());
        ls.setNgayThayDoi(java.sql.Date.valueOf(ngayHieuLuc)); // cần parse
        ls.setGhiChu(lyDo);
        return lichSuBUS.themLichSu(ls);
    }
    public double getLuongCoBanByChucVu(String maCV) {
        return new LuongCoBanTheoChucVuDAO().getLuongCoBan(maCV);
    }
    public double getPhuCapByChucVu(String maCV) {
        return new LuongCoBanTheoChucVuDAO().getPhuCap(maCV);
    }
}