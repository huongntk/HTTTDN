package DAO;

import DTO.NhanVienDTO;
import UTIL.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class NhanVienDAO {

    public ArrayList<NhanVienDTO> layDanhSachNhanVien() {
        ArrayList<NhanVienDTO> danhSach = new ArrayList<>();
        String sql = "SELECT nv.*, cv.TenCV, tk.TaiKhoan, tk.MatKhau "
                + "FROM NhanVien nv "
                + "LEFT JOIN ChucVu cv ON nv.MaCV = cv.MaCV "
                + "LEFT JOIN TaiKhoan tk ON nv.MaNV = tk.MaNV";
        ResultSet rs = null;

        try {

            rs = DataProvider.executeQuery(sql);

            while (rs != null && rs.next()) {
                NhanVienDTO nv = new NhanVienDTO(
                        rs.getInt("MaNV"),
                        rs.getString("Ho"),
                        rs.getString("Ten"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDienThoai"),
                        rs.getString("TenCV"),
                        rs.getBoolean("TrangThai"),
                        rs.getString("TaiKhoan"),
                        rs.getString("MatKhau")
                );
                danhSach.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (rs != null) {
                try {
                    Statement stmt = rs.getStatement();
                    Connection conn = stmt.getConnection();
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return danhSach;
    }

    public boolean kiemTraTenTaiKhoan(String tenTaiKhoan, int maNV) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE TaiKhoan = ? AND MaNV != ?";
        ResultSet rs = null;
        boolean exists = false;

        try {

            rs = DataProvider.executeQuery(sql, tenTaiKhoan, maNV);

            if (rs != null && rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (rs != null) {
                try {
                    Statement stmt = rs.getStatement();
                    Connection conn = stmt.getConnection();
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return exists;
    }

    public boolean themNhanVien(NhanVienDTO nv) {
        String sqlNhanVien = "INSERT INTO NhanVien (Ho, Ten, GioiTinh, SoDienThoai, MaCV, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlTaiKhoan = "INSERT INTO TaiKhoan (MaNV, TaiKhoan, MatKhau, MaQuyen, TrangThai) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement psNhanVien = null;
        PreparedStatement psTaiKhoan = null;
        ResultSet rsKey = null;
        int maNVMoi = 0;

        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // 1. Thêm Nhân viên (IDENTITY tự sinh MaNV)
            psNhanVien = conn.prepareStatement(sqlNhanVien, Statement.RETURN_GENERATED_KEYS);
            psNhanVien.setString(1, nv.getHo());
            psNhanVien.setString(2, nv.getTen());
            psNhanVien.setString(3, nv.getGioiTinh());
            psNhanVien.setString(4, nv.getSoDienThoai());
            psNhanVien.setString(5, nv.getMaCV());
            psNhanVien.setBoolean(6, nv.isTrangThai());

            int rowsAffected = psNhanVien.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return false;
            }

            // 2. Lấy MaNV vừa sinh
            rsKey = psNhanVien.getGeneratedKeys();
            if (rsKey.next()) {
                maNVMoi = rsKey.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            // 3. Thêm Tài khoản (dùng đúng tên cột TaiKhoan)
            psTaiKhoan = conn.prepareStatement(sqlTaiKhoan);
            psTaiKhoan.setInt(1, maNVMoi);
            psTaiKhoan.setString(2, nv.getTenTaiKhoan());  // map vào cột TaiKhoan
            psTaiKhoan.setString(3, nv.getMatKhau());
            psTaiKhoan.setString(4, nv.getMaQuyen());
            psTaiKhoan.setBoolean(5, nv.isTrangThai());

            rowsAffected = psTaiKhoan.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            try {
                if (rsKey != null) rsKey.close();
                if (psNhanVien != null) psNhanVien.close();
                if (psTaiKhoan != null) psTaiKhoan.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean suaNhanVien(NhanVienDTO nv, String lyDo) {
        String sqlNhanVien = "UPDATE NhanVien SET Ho = ?, Ten = ?, GioiTinh = ?, SoDienThoai = ?, MaCV = ?, TrangThai = ? WHERE MaNV = ?";
        String sqlTaiKhoan = "UPDATE TaiKhoan SET TaiKhoan = ?, MatKhau = ?, MaQuyen = ?, TrangThai = ? WHERE MaNV = ?";
        String sqlLichSu = "INSERT INTO LichSuChucVu (MaNV, MaCVCu, MaCVMoi, NgayThayDoi, GhiChu) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement psNhanVien = null;
        PreparedStatement psTaiKhoan = null;
        PreparedStatement psLichSu = null;
        ResultSet rsOld = null;

        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            // 1. Lấy chức vụ cũ
            String oldMaCV = null;
            String sqlSelectOld = "SELECT MaCV FROM NhanVien WHERE MaNV = ?";
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelectOld)) {
                psSelect.setInt(1, nv.getMaNV());
                rsOld = psSelect.executeQuery();
                if (rsOld.next()) {
                    oldMaCV = rsOld.getString("MaCV");
                }
            }

            // 2. Cập nhật NhanVien
            psNhanVien = conn.prepareStatement(sqlNhanVien);
            psNhanVien.setString(1, nv.getHo());
            psNhanVien.setString(2, nv.getTen());
            psNhanVien.setString(3, nv.getGioiTinh());
            psNhanVien.setString(4, nv.getSoDienThoai());
            psNhanVien.setString(5, nv.getMaCV());
            psNhanVien.setBoolean(6, nv.isTrangThai());
            psNhanVien.setInt(7, nv.getMaNV());
            int rowsAffectedNV = psNhanVien.executeUpdate();

            // 3. Cập nhật TaiKhoan
            psTaiKhoan = conn.prepareStatement(sqlTaiKhoan);
            psTaiKhoan.setString(1, nv.getTenTaiKhoan());
            psTaiKhoan.setString(2, nv.getMatKhau());
            psTaiKhoan.setString(3, nv.getMaQuyen());
            psTaiKhoan.setBoolean(4, nv.isTrangThai());
            psTaiKhoan.setInt(5, nv.getMaNV());
            int rowsAffectedTK = psTaiKhoan.executeUpdate();

            // 4. Ghi lịch sử nếu chức vụ thay đổi
            boolean chucVuChanged = (oldMaCV != null && !oldMaCV.equals(nv.getMaCV()));
            if (chucVuChanged) {
                psLichSu = conn.prepareStatement(sqlLichSu);
                psLichSu.setInt(1, nv.getMaNV());
                psLichSu.setString(2, oldMaCV);
                psLichSu.setString(3, nv.getMaCV());
                psLichSu.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                psLichSu.setString(5, lyDo != null && !lyDo.isEmpty() ? lyDo : "Cập nhật chức vụ");
                psLichSu.executeUpdate();
            }

            if (rowsAffectedNV > 0 || rowsAffectedTK > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {

            try {
                if (rsOld != null) {
                    rsOld.close();
                }
                if (psNhanVien != null) {
                    psNhanVien.close();
                }
                if (psTaiKhoan != null) {
                    psTaiKhoan.close();
                }
                if (psLichSu != null) {
                    psLichSu.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int update(NhanVienDTO nv) {
        String sql = "UPDATE NhanVien SET Ho = ?, Ten = ?, GioiTinh = ?, SoDienThoai = ?, MaCV = ?, TrangThai = ? WHERE MaNV = ?";
        return DataProvider.executeUpdate(sql,
                nv.getHo(), nv.getTen(), nv.getGioiTinh(), nv.getSoDienThoai(), nv.getMaCV(), nv.isTrangThai(), nv.getMaNV());
    }

    public boolean xoaNhanVien(int maNV) {
        String sqlNhanVien = "UPDATE NhanVien SET TrangThai = 0 WHERE MaNV = ?";
        String sqlTaiKhoan = "UPDATE TaiKhoan SET TrangThai = 0 WHERE MaNV = ?";

        Connection conn = null;
        PreparedStatement psNhanVien = null;
        PreparedStatement psTaiKhoan = null;

        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            psNhanVien = conn.prepareStatement(sqlNhanVien);
            psNhanVien.setInt(1, maNV);
            int rowsAffectedNV = psNhanVien.executeUpdate();

            psTaiKhoan = conn.prepareStatement(sqlTaiKhoan);
            psTaiKhoan.setInt(1, maNV);
            int rowsAffectedTK = psTaiKhoan.executeUpdate();

            if (rowsAffectedNV > 0 && rowsAffectedTK > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {

            try {
                if (psNhanVien != null) {
                    psNhanVien.close();
                }
                if (psTaiKhoan != null) {
                    psTaiKhoan.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<NhanVienDTO> timKiemNhanVien(String tuKhoa) {
        ArrayList<NhanVienDTO> danhSach = new ArrayList<>();
        String sql = "SELECT nv.*, tk.TaiKhoan, tk.MatKhau "
                + "FROM NhanVien nv "
                + "LEFT JOIN TaiKhoan tk ON nv.MaNV = tk.MaNV "
                + "WHERE CAST(nv.MaNV AS VARCHAR) LIKE ? "
                + "OR CONCAT(nv.Ho, ' ', nv.Ten) LIKE ? "
                + "OR nv.SoDienThoai LIKE ? "
                + "OR tk.TaiKhoan LIKE ?";

        ResultSet rs = null;
        String keyword = "%" + tuKhoa + "%";

        try {

            rs = DataProvider.executeQuery(sql, keyword, keyword, keyword, keyword);

            while (rs != null && rs.next()) {
                NhanVienDTO nv = new NhanVienDTO(
                        rs.getInt("MaNV"),
                        rs.getString("Ho"),
                        rs.getString("Ten"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDienThoai"),
                        rs.getString("MaCV"),
                        rs.getBoolean("TrangThai"),
                        rs.getString("TaiKhoan"),
                        rs.getString("MatKhau")
                );
                danhSach.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (rs != null) {
                try {
                    Statement stmt = rs.getStatement();
                    Connection conn = stmt.getConnection();
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return danhSach;
    }

    public String getTenNhanVien(int maNV) {

        String sql = "SELECT Ho, Ten FROM NhanVien WHERE MaNV = ?";
        String tenNV = null;
        String ho = null;
        String ten = null;
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            if (rs.next()) {
                ho = rs.getString("Ho");
                ten = rs.getString("Ten");
                tenNV = ho + " " + ten;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy tên NV: " + e.getMessage());
        }
        return tenNV != null ? tenNV : "Không tìm thấy";
    }

    public ArrayList<NhanVienDTO> layNhanVienTheoMa(int maNV) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV = ?";
        ArrayList<NhanVienDTO> ds = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maNV);

            while (rs != null && rs.next()) {
                NhanVienDTO nv = new NhanVienDTO(
                        rs.getInt("MaNV"),
                        rs.getString("Ho"),
                        rs.getString("Ten"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDienThoai"),
                        rs.getString("MaCV"),
                        rs.getBoolean("TrangThai"),
                        rs.getString("TaiKhoan"),
                        rs.getString("MatKhau")
                );
                ds.add(nv);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy tên NV: " + e.getMessage());
        }
        return ds;
    }

    public NhanVienDTO selectById(int maNV) {
        String sql = "SELECT nv.*, tk.TaiKhoan, tk.MatKhau "
                + "FROM NhanVien nv JOIN TaiKhoan tk ON nv.MaNV = tk.MaNV WHERE nv.MaNV = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            if (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setHo(rs.getString("Ho"));
                nv.setTen(rs.getString("Ten"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setMaCV(rs.getString("MaCV"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                nv.setTenTaiKhoan(rs.getString("TaiKhoan"));
                nv.setMatKhau(rs.getString("MatKhau"));
                return nv;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> layDanhSachChucVu() {
        ArrayList<String> chucVuList = new ArrayList<>();
        // Truy vấn SQL: DISTINCT ChucVu để lấy các giá trị duy nhất
        String sql = "SELECT TenCV FROM ChucVu ORDER BY TenCV";

        // Sử dụng DataProvider để thực thi truy vấn
        ResultSet rs = DataProvider.executeQuery(sql);

        try {
            while (rs.next()) {
                // Lấy giá trị từ cột "ChucVu"
                String chucVu = rs.getString("ChucVu");
                chucVuList.add(chucVu);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn chức vụ: " + e.getMessage());
        } finally {
            // Đóng ResultSet sau khi sử dụng (Nên có thêm logic đóng Connection/Statement trong DataProvider)
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                // Bỏ qua lỗi đóng
            }
        }
        return chucVuList;
    }

    // Lấy lương cơ bản theo mã chức vụ
    public double getLuongCoBanByChucVu(String maCV) {
        String sql = "SELECT LuongCoBan FROM LuongCoBanTheoChucVu WHERE MaCV = ?";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maCV);
            if (rs != null && rs.next()) {
                return rs.getDouble("LuongCoBan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return 0; // trả về 0 nếu không có cấu hình
    }

    // Lấy phụ cấp chức vụ theo mã chức vụ
    public double getPhuCapByChucVu(String maCV) {
        String sql = "SELECT PhuCapChucVu FROM LuongCoBanTheoChucVu WHERE MaCV = ?";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maCV);
            if (rs != null && rs.next()) {
                return rs.getDouble("PhuCapChucVu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return 0;
    }

    // Helper method đóng ResultSet (nếu chưa có)
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                Statement stmt = rs.getStatement();
                rs.close();
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<NhanVienDTO> selectAll() {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setHo(rs.getString("Ho"));
                nv.setTen(rs.getString("Ten"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setMaCV(rs.getString("MaCV"));
                nv.setMaQuyen(rs.getString("MaQuyen"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                nv.setTenTaiKhoan(rs.getString("TenTaiKhoan"));
                nv.setMatKhau(rs.getString("MatKhau"));
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
