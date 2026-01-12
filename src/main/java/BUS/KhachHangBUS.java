package BUS;

import DAO.KhachHangDAO;
import DTO.KhachHangDTO;

import java.util.ArrayList;

public class KhachHangBUS {

    private KhachHangDAO khachHangDAO;

    public KhachHangBUS() {
        khachHangDAO = new KhachHangDAO();
    }

    
    public ArrayList<KhachHangDTO> layDanhSachKhachHang() {
        return khachHangDAO.layDanhSachKhachHang();
    }

   
    public String themKhachHang(KhachHangDTO kh) {
        
        if (kh.getHo().trim().isEmpty() || kh.getTen().trim().isEmpty()) {
            return "Họ và tên không được để trống";
        }
        
        if (kh.getSoDienThoai().trim().isEmpty()) {
            return "Số điện thoại không được để trống";
        }

      
        if (!kh.getSoDienThoai().matches("^0\\d{9}$")) {
            return "Số điện thoại không hợp lệ (phải đủ 10 số, bắt đầu bằng 0)";
        }
        
       
        if (khachHangDAO.themKhachHang(kh)) {
            return "Thêm khách hàng thành công";
        } else {
            return "Thêm khách hàng thất bại";
        }
    }

    
    public String suaKhachHang(KhachHangDTO kh) {
        // Kiểm tra cơ bản
        if (kh.getHo().trim().isEmpty() || kh.getTen().trim().isEmpty()) {
            return "Họ và tên không được để trống";
        }
        
        if (kh.getSoDienThoai().trim().isEmpty()) {
            return "Số điện thoại không được để trống";
        }

       
        if (!kh.getSoDienThoai().matches("^0\\d{9}$")) {
            return "Số điện thoại không hợp lệ (phải đủ 10 số, bắt đầu bằng 0)";
        }

        
        if (khachHangDAO.suaKhachHang(kh)) {
            return "Sửa thông tin khách hàng thành công";
        } else {
            return "Sửa thông tin khách hàng thất bại";
        }
    }

   
    public String xoaKhachHang(int maKH) {
        if (maKH <= 0) {
            return "Mã khách hàng không hợp lệ";
        }
        
        // Gọi DAO để "xóa"
        if (khachHangDAO.xoaKhachHang(maKH)) {
            return "Xóa khách hàng thành công";
        } else {
            return "Xóa khách hàng thất bại";
        }
    }

    
    public ArrayList<KhachHangDTO> timKiemKhachHang(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            
            return khachHangDAO.layDanhSachKhachHang();
        }
        return khachHangDAO.timKiemKhachHang(tuKhoa);
    }
}