package BUS;

import DAO.CaLamDAO;
import DTO.CaLamDTO;
import java.util.ArrayList;

public class CaLamBUS {

    private CaLamDAO caLamDAO;

    public CaLamBUS() {
        caLamDAO = new CaLamDAO();
    }

    public ArrayList<CaLamDTO> layDanhSachCaLam() {
        return caLamDAO.selectAll();
    }

    public ArrayList<CaLamDTO> layCaLamDangHoatDong() {
        return caLamDAO.selectActive();
    }

    public CaLamDTO layCaLamTheoMa(int maCa) {
        return caLamDAO.selectById(maCa);
    }

    public String themCaLam(CaLamDTO ca) {
        if (ca == null) {
            return "Dữ liệu ca làm không hợp lệ!";
        }

        if (ca.getTenCa() == null || ca.getTenCa().trim().isEmpty()) {
            return "Tên ca không được để trống!";
        }

        if (ca.getGioBatDau() == null || ca.getGioKetThuc() == null) {
            return "Giờ bắt đầu và giờ kết thúc không được để trống!";
        }

        if (!ca.getGioKetThuc().after(ca.getGioBatDau())) {
            return "Giờ kết thúc phải sau giờ bắt đầu!";
        }

        boolean ok = caLamDAO.insert(ca);
        return ok ? "Thêm ca làm thành công!" : "Thêm ca làm thất bại!";
    }

    public String suaCaLam(CaLamDTO ca) {
        if (ca == null || ca.getMaCa() <= 0) {
            return "Dữ liệu ca làm không hợp lệ!";
        }

        if (ca.getTenCa() == null || ca.getTenCa().trim().isEmpty()) {
            return "Tên ca không được để trống!";
        }

        if (ca.getGioBatDau() == null || ca.getGioKetThuc() == null) {
            return "Giờ bắt đầu và giờ kết thúc không được để trống!";
        }

        if (!ca.getGioKetThuc().after(ca.getGioBatDau())) {
            return "Giờ kết thúc phải sau giờ bắt đầu!";
        }

        boolean ok = caLamDAO.update(ca);
        return ok ? "Cập nhật ca làm thành công!" : "Cập nhật ca làm thất bại!";
    }

    public String xoaCaLam(int maCa) {
        if (maCa <= 0) {
            return "Mã ca không hợp lệ!";
        }

        boolean ok = caLamDAO.delete(maCa);
        return ok ? "Xóa ca làm thành công!" : "Xóa ca làm thất bại!";
    }
}