package DAO;

import DTO.CTPhieuNhapDTO;
import UTIL.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CTPhieuNhapDAO {

    // Lấy danh sách chi tiết theo MaPN
    public List<CTPhieuNhapDTO> getByMaPN(int maPN) {
        List<CTPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT MaPN, ID, SoLuong, GiaNhap, ThanhTien FROM CTPhieuNhap WHERE MaPN = ?";

        try (ResultSet rs = DataProvider.executeQuery(sql, maPN)) {
            while (rs.next()) {
                CTPhieuNhapDTO ct = new CTPhieuNhapDTO(
                        rs.getInt("MaPN"),
                        rs.getInt("ID"),
                        rs.getInt("SoLuong"),
                        rs.getInt("GiaNhap"),
                        rs.getInt("ThanhTien")
                );
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm dòng chi tiết
    public boolean insert(CTPhieuNhapDTO ct) {
        String sql = "INSERT INTO CTPhieuNhap (MaPN, ID, SoLuong, GiaNhap, ThanhTien) VALUES (?, ?, ?, ?, ?)";
        int rows = DataProvider.executeUpdate(
                sql,
                ct.getMaPN(),
                ct.getId(),
                ct.getSoLuong(),
                ct.getGiaNhap(),
                ct.getThanhTien()
        );
        return rows > 0;
    }
    
    // Lấy số lượng hiện tại của một dòng chi tiết phiếu nhập
    public int getSoLuongCu(int maPN, int idSP) {
        String sql = "SELECT SoLuong FROM CTPhieuNhap WHERE MaPN = ? AND ID = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maPN, idSP)) {
            if (rs.next()) {
                return rs.getInt("SoLuong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu không tìm thấy
    }
    
    /**
    * Kiểm tra tồn kho hiện tại có đủ để trừ đi một lượng hay không
    * @param idSP          mã sản phẩm
    * @param soLuongTru    số lượng muốn trừ (số dương)
    * @return true nếu tồn kho >= soLuongTru, ngược lại false
    */
   public boolean kiemTraTonKhoDu(int idSP, int soLuongTru) {
       String sql = "SELECT ISNULL(SoLuong, 0) FROM SanPham WHERE ID = ?";
       try (ResultSet rs = DataProvider.executeQuery(sql, idSP)) {
           if (rs.next()) {
               int tonHienTai = rs.getInt(1);
               return tonHienTai >= soLuongTru;
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       // Nếu không tìm thấy sản phẩm hoặc lỗi -> coi như không đủ
       return false;
   }

    // Cập nhật 1 dòng chi tiết transaction
    public boolean updateWithStock(CTPhieuNhapDTO ctMoi, int soLuongCu) {
        int delta = ctMoi.getSoLuong() - soLuongCu;

        if (delta < 0) {
            // Trường hợp giảm số lượng -> cần kiểm tra tồn kho đủ để trừ
            int soLuongGiam = -delta; // số dương
            if (!kiemTraTonKhoDu(ctMoi.getId(), soLuongGiam)) {
                throw new RuntimeException("Không thể cập nhật! Tồn kho hiện tại của sản phẩm ID " 
                        + ctMoi.getId() + " không đủ để giảm thêm " + soLuongGiam + " sản phẩm.");
            }
        }

        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // 1. Cập nhật tồn kho nếu delta != 0
            if (delta != 0) {
                String sqlTon = "UPDATE SanPham SET SoLuong = ISNULL(SoLuong,0) + ? WHERE ID = ?";
                try (PreparedStatement psTon = conn.prepareStatement(sqlTon)) {
                    psTon.setInt(1, delta);
                    psTon.setInt(2, ctMoi.getId());
                    psTon.executeUpdate();
                }
            }

            // 2. Cập nhật dòng chi tiết
            String sqlCT = "UPDATE CTPhieuNhap SET SoLuong=?, GiaNhap=?, ThanhTien=? WHERE MaPN=? AND ID=?";
            try (PreparedStatement psCT = conn.prepareStatement(sqlCT)) {
                psCT.setInt(1, ctMoi.getSoLuong());
                psCT.setInt(2, ctMoi.getGiaNhap());
                psCT.setInt(3, ctMoi.getThanhTien());
                psCT.setInt(4, ctMoi.getMaPN());
                psCT.setInt(5, ctMoi.getId());
                psCT.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi cơ sở dữ liệu khi cập nhật chi tiết: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    public boolean deleteWithStock(int maPN, int idSP, int soLuongCu) {
        if (soLuongCu <= 0) {
            // Không cần điều chỉnh tồn kho
            return delete(maPN, idSP);
        }

        // Kiểm tra tồn kho trước khi trừ
        if (!kiemTraTonKhoDu(idSP, soLuongCu)) {
            // Ném ngoại lệ để BUS xử lý thông báo
            throw new RuntimeException("Không thể xóa! Tồn kho hiện tại của sản phẩm ID " + idSP 
                    + " không đủ để trừ " + soLuongCu + " sản phẩm.");
        }

        Connection conn = null;
        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // 1. Giảm tồn kho
            String sqlTon = "UPDATE SanPham SET SoLuong = ISNULL(SoLuong,0) - ? WHERE ID = ?";
            try (PreparedStatement psTon = conn.prepareStatement(sqlTon)) {
                psTon.setInt(1, soLuongCu);
                psTon.setInt(2, idSP);
                psTon.executeUpdate();
            }

            // 2. Xóa dòng chi tiết
            String sqlCT = "DELETE FROM CTPhieuNhap WHERE MaPN=? AND ID=?";
            try (PreparedStatement psCT = conn.prepareStatement(sqlCT)) {
                psCT.setInt(1, maPN);
                psCT.setInt(2, idSP);
                psCT.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi cơ sở dữ liệu khi xóa chi tiết: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Xóa chi tiết theo MaPN + ID sản phẩm
    public boolean delete(int maPN, int idSP) {
        String sql = "DELETE FROM CTPhieuNhap WHERE MaPN=? AND ID=?";
        int rows = DataProvider.executeUpdate(sql, maPN, idSP);
        return rows > 0;
    }

    // Tổng tiền của 1 phiếu nhập
    public double sumThanhTien(int maPN) {
        String sql = "SELECT ISNULL(SUM(ThanhTien),0) AS Tong FROM CTPhieuNhap WHERE MaPN = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maPN)) {
            if (rs.next()) {
                return rs.getDouble("Tong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
    * Điều chỉnh tồn kho sản phẩm (có thể tăng hoặc giảm)
    * @param idSP   Mã sản phẩm
    * @param delta  Số lượng thay đổi (dương: tăng, âm: giảm)
    */
   public void dieuChinhTonKho(int idSP, int delta) {
       if (delta == 0) return;
       String sql = "UPDATE SanPham SET SoLuong = ISNULL(SoLuong, 0) + ? WHERE ID = ?";
       DataProvider.executeUpdate(sql, delta, idSP);
   }
    
    
    public boolean isSanPhamInUse(int maSP) {
        String sql = "SELECT COUNT(*) FROM CTPhieuNhap WHERE Id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maSP);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
