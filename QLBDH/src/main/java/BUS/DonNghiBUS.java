package BUS;

import DAO.DonNghiDAO;
import DTO.DonNghiDTO;
import DAO.ChamCongDAO;
import DTO.ChamCongDTO;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DonNghiBUS {
    private DonNghiDAO donNghiDAO;
    private ChamCongDAO chamCongDAO;

    public DonNghiBUS() {
        donNghiDAO = new DonNghiDAO();
        chamCongDAO = new ChamCongDAO();
    }

    public ArrayList layTatCaDon() {
        return donNghiDAO.selectAll();
    }

    public ArrayList layDonChoDuyet() {
        return donNghiDAO.selectByTrangThai("Chờ duyệt");
    }

    public ArrayList layDonTheoNhanVien(int maNV) {
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
        DonNghiDTO don = donNghiDAO.selectById(maDon);

        if (don == null) {
            return "Không tìm thấy đơn nghỉ!";
        }

        if (!"Chờ duyệt".equals(don.getTrangThai())) {
            return "Chỉ có thể duyệt đơn đang ở trạng thái Chờ duyệt!";
        }

        boolean ok = donNghiDAO.updateTrangThai(maDon, "Đã duyệt");
        if (!ok) {
            return "Duyệt thất bại!";
        }

        int soNgayTaoChamCong = taoChamCongNghiCoPhep(don);

        return "Đã duyệt đơn! Đã tạo " + soNgayTaoChamCong + " ngày chấm công nghỉ có phép.";
    }

    public String tuChoiDon(int maDon) {
        DonNghiDTO don = donNghiDAO.selectById(maDon);

        if (don == null) {
            return "Không tìm thấy đơn nghỉ!";
        }

        if (!"Chờ duyệt".equals(don.getTrangThai())) {
            return "Chỉ có thể từ chối đơn đang ở trạng thái Chờ duyệt!";
        }
        boolean ok = donNghiDAO.updateTrangThai(maDon, "Từ chối");
        return ok ? "Đã từ chối đơn!" : "Từ chối thất bại!";
    }
    
     private int taoChamCongNghiCoPhep(DonNghiDTO don) {
        int count = 0;

        Calendar cal = Calendar.getInstance();
        cal.setTime(don.getNgayBatDau());

        Calendar end = Calendar.getInstance();
        end.setTime(don.getNgayKetThuc());

        while (!cal.after(end)) {
            Date ngay = cal.getTime();

            // Nếu ngày đó đã có chấm công thì không ghi đè
            if (!chamCongDAO.exists(don.getMaNV(), ngay)) {
                ChamCongDTO cc = new ChamCongDTO();
                cc.setMaNV(don.getMaNV());
                cc.setNgayLam(ngay);
                cc.setCaLam("Cả ngày");
                cc.setGioVao(Time.valueOf("08:00:00"));
                cc.setGioRa(Time.valueOf("17:00:00"));
                cc.setTrangThai("Nghỉ có phép");
                cc.setGhiChu("Tự động tạo từ đơn nghỉ #" + don.getMaDon());

                if (chamCongDAO.insert(cc)) {
                    count++;
                }
            }

            cal.add(Calendar.DATE, 1);
        }

        return count;
    }
}