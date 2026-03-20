package BUS;

import DAO.LuongDAO;
import DTO.Luong;
import java.util.ArrayList;

public class LuongBUS {
    private LuongDAO luongDAO;

    public LuongBUS() {
        luongDAO = new LuongDAO();
    }

    public ArrayList<Luong> layLuongTheoNV(int maNV) {
        return luongDAO.selectByMaNV(maNV);
    }

    public boolean tinhLuongTheoThang(int thang, int nam) {
        // Gọi DAO để tính lương (có thể là procedure hoặc logic phức tạp)
        // Ở đây giả sử có phương thức tinhLuong
        return luongDAO.tinhLuong(thang, nam);
    }

    public boolean themLuong(Luong luong) {
        return luongDAO.insert(luong) > 0;
    }

    public boolean suaLuong(Luong luong) {
        return luongDAO.update(luong) > 0;
    }

    public boolean xoaLuong(int maLuong) {
        return luongDAO.delete(maLuong) > 0;
    }
}