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

    public String suaNhanVien(NhanVienDTO nv, String lyDo) {
        String validationError = validateFields(nv, false);
        if (validationError != null) {
            return validationError;
        }

        boolean success = nhanVienDAO.suaNhanVien(nv, lyDo);
        return success ? "Sửa nhân viên thành công!" : "Sửa nhân viên thất bại!";
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
    
    private String layTenChucVuTuMa(String maCV) {
        if (maCV == null) return "Nhân viên";
        switch (maCV.trim()) {
            case "CV01": return "Giám đốc";
            case "CV02": return "Quản lý nhân sự";
            case "CV03": return "Quản lý kho";
            case "CV04": return "Quản lý bán hàng";
            case "CV05": return "Nhân viên bán hàng";
            case "CV06": return "Nhân viên nhập hàng";
            default: return "Nhân viên";
        }
    }

    public boolean capNhatChucVu(int maNV, String chucVuMoi, String ngayHieuLuc, String lyDo) {
        // Lấy nhân viên hiện tại
        NhanVienDTO nv = layNhanVienTheoMa(maNV);
        if (nv == null) return false;

        // Xử lý mã chức vụ cũ (Dịch MaCV sang TenCV để lấy đúng Object ChucVu)
        String tenCVCold = layTenChucVuTuMa(nv.getMaCV()); 
        ChucVu cvCu = chucVuBUS.layChucVuTheoTen(tenCVCold); 
        
        // Lấy chức vụ mới
        ChucVu cvMoi = chucVuBUS.layChucVuTheoTen(chucVuMoi);
        if (cvCu == null || cvMoi == null) return false;

        // QUAN TRỌNG: Cập nhật MaCV (VD: "CV01") vào DTO, thay vì tên ("Giám đốc")
        nv.setMaCV(cvMoi.getMaCV()); 
        boolean updateNV = nhanVienDAO.update(nv) > 0;
        if (!updateNV) return false;

        // Ghi lịch sử
        LichSuChucVu ls = new LichSuChucVu();
        ls.setMaNV(maNV);
        ls.setMaCVCu(cvCu.getMaCV());
        ls.setMaCVMoi(cvMoi.getMaCV());
        try {
            // Chuyển đổi chuỗi dd/MM/yyyy từ Form thành LocalDate
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate localDate = java.time.LocalDate.parse(ngayHieuLuc, formatter);
            
            // Chuyển từ LocalDate sang java.sql.Date
            ls.setNgayThayDoi(java.sql.Date.valueOf(localDate)); 
        } catch (Exception e) {
            System.err.println("Lỗi parse ngày tháng: " + e.getMessage());
            return false;
        }
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