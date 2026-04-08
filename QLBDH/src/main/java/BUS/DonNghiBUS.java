package BUS;

import DAO.DonNghiDAO;
import DTO.DonNghiDTO;
import java.util.ArrayList;

public class DonNghiBUS {
    private DonNghiDAO donNghiDAO;

    public DonNghiBUS() {
        donNghiDAO = new DonNghiDAO();
    }

    public ArrayList<DonNghiDTO> layTatCaDon() {
        return donNghiDAO.selectAll();
    }

    public ArrayList<DonNghiDTO> layDonChoDuyet() {
        return donNghiDAO.selectByTrangThai("Chờ duyệt");
    }

    public ArrayList<DonNghiDTO> layDonTheoNhanVien(int maNV) {
        return donNghiDAO.selectByMaNV(maNV);
    }

    public String themDonNghi(DonNghiDTO dn) {
        if (dn.getNgayBatDau() == null || dn.getNgayKetThuc() == null || dn.getLyDo().trim().isEmpty()) {
            return "Vui lòng nhập đầy đủ thông tin!";
        }
        if (dn.getNgayKetThuc().before(dn.getNgayBatDau())) {
            return "Ngày kết thúc phải sau ngày bắt đầu!";
        }
        dn.setTrangThai("Chờ duyệt");
        int result = donNghiDAO.insert(dn);
        return result > 0 ? "Đã gửi đơn nghỉ, chờ duyệt!" : "Thêm đơn thất bại!";
    }

    public String duyetDon(int maDon) {
        boolean ok = donNghiDAO.updateTrangThai(maDon, "Đã duyệt");
        return ok ? "Đã duyệt đơn!" : "Duyệt thất bại!";
    }

    public String tuChoiDon(int maDon) {
        boolean ok = donNghiDAO.updateTrangThai(maDon, "Từ chối");
        return ok ? "Đã từ chối đơn!" : "Từ chối thất bại!";
    }
}