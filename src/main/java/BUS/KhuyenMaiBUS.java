package BUS;

import DAO.KhuyenMaiDAO;
import DTO.KhuyenMai;

import java.util.ArrayList;
import java.util.Date;

public class KhuyenMaiBUS {
    private ArrayList<KhuyenMai> listKhuyenMai = null;
    private KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    public KhuyenMaiBUS() {
        docDanhSach();
    }
    
    public void docDanhSach() {
        this.listKhuyenMai = khuyenMaiDAO.layDanhSachKhuyenMai();
    }
    
//    public ArrayList<GiamGiaDTO> getListGiamGia() {
//        return dao.getAll();
//    }
    
    public ArrayList<KhuyenMai> layDanhSachKhuyenMai() {
        if (this.listKhuyenMai == null)
            docDanhSach();
        return this.listKhuyenMai;
    }

    public String themKhuyenMai(KhuyenMai km) {
        
        if (km.getTenGG().trim().isEmpty()) {
            return "Tên giảm giá không được để trống!";
        }
        
        if (km.getPhanTramGiam() <= 0) {
            return "Phần trăm giảm phải lớn hơn 0!";
        }
        
        // Kiểm tra ngày bắt đầu
        Date today = new Date(); // ngày hiện tại
        Date ngayBD = km.getNgayBD();
        Date ngayKT = km.getNgayKT();
        
        if (ngayBD.before(today)) {
            return "Ngày bắt đầu khuyến mãi phải sau ngày hiện tại!";
        }

        if (ngayKT.before(today)) {
            return "Ngày kết thúc khuyến mãi phải sau ngày hiện tại!";
        }

        if (!ngayKT.after(ngayBD)) {
            return "Ngày kết thúc phải sau ngày bắt đầu!";
        }
        
        // Gọi DAO để "Thêm"
        if (khuyenMaiDAO.themKhuyenMai(km)) {
            docDanhSach();
            return "Thêm khuyến mãi thành công!";
        } else {
            return "Thêm khuyến mãi thất bại!";
        }
    }
    
    public String suaKhuyenMai(KhuyenMai km) {

        if (km.getTenGG().trim().isEmpty()) {
            return "Tên giảm giá không được để trống!";
        }
        
        if (km.getPhanTramGiam() <= 0) {
            return "Phần trăm giảm phải lớn hơn 0!";
        }
       
        // Kiểm tra ngày bắt đầu
        Date today = new Date(); // ngày hiện tại
        Date ngayBD = km.getNgayBD();
        Date ngayKT = km.getNgayKT();
        
        if (ngayKT.before(today)) {
            return "Ngày kết thúc khuyến mãi phải sau ngày hiện tại!";
        }

        if (!ngayKT.after(ngayBD)) {
            return "Ngày kết thúc phải sau ngày bắt đầu!";
        }

        if (khuyenMaiDAO.suaKhuyenMai(km)) {
            docDanhSach();
            return "Sửa thông tin khuyến mãi thành công!";
        } else {
            return "Sửa thông tin khuyến mãi thất bại!";
        }
    }

    public String xoaKhuyenMai(int maGG) {
        if (maGG <= 0) {
            return "Mã khuyến mãi không hợp lệ!";
        }
        
        if (khuyenMaiDAO.xoaKhuyenMai(maGG)) {
            docDanhSach();
            return "Xóa khuyến mãi thành công!";
        } else {
            return "Xóa khuyến mãi thất bại!";
        }
    }
    
    public ArrayList<KhuyenMai> timKiemKhuyenMai(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            docDanhSach();
            return khuyenMaiDAO.layDanhSachKhuyenMai();
        }
        return khuyenMaiDAO.timKiemKhuyenMai(tuKhoa);
    }
}
