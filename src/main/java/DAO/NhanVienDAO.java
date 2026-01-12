package DAO;

import DTO.NhanVienDTO;
import UTIL.DBConnect; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NhanVienDAO {

    public ArrayList<NhanVienDTO> layDanhSachNhanVien() {
        ArrayList<NhanVienDTO> danhSach = new ArrayList<>();
        String sql = "SELECT nv.*, tk.TaiKhoan, tk.MatKhau " +
                     "FROM NhanVien nv " +
                     "LEFT JOIN TaiKhoan tk ON nv.MaNV = tk.MaNV";
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
                        rs.getString("ChucVu"),
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
        String sqlNhanVien = "INSERT INTO NhanVien (Ho, Ten, GioiTinh, SoDienThoai, ChucVu, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlTaiKhoan = "INSERT INTO TaiKhoan (MaNV, TaiKhoan, MatKhau, Quyen, TrangThai) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement psNhanVien = null;
        PreparedStatement psTaiKhoan = null;
        ResultSet rsKey = null;

        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false); 

            
            psNhanVien = conn.prepareStatement(sqlNhanVien, Statement.RETURN_GENERATED_KEYS);
            psNhanVien.setString(1, nv.getHo());
            psNhanVien.setString(2, nv.getTen());
            psNhanVien.setString(3, nv.getGioiTinh());
            psNhanVien.setString(4, nv.getSoDienThoai());
            psNhanVien.setString(5, nv.getChucVu());
            psNhanVien.setBoolean(6, nv.isTrangThai());

            int rowsAffected = psNhanVien.executeUpdate();
            if (rowsAffected == 0) {
                conn.rollback();
                return false;
            }

            
            rsKey = psNhanVien.getGeneratedKeys();
            int maNV = 0;
            if (rsKey.next()) {
                maNV = rsKey.getInt(1);
            } else {
                conn.rollback();
                return false;
            }

            
            psTaiKhoan = conn.prepareStatement(sqlTaiKhoan);
            psTaiKhoan.setInt(1, maNV);
            psTaiKhoan.setString(2, nv.getTenTaiKhoan());
            psTaiKhoan.setString(3, nv.getMatKhau());
            psTaiKhoan.setString(4, nv.getChucVu()); 
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
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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

    public boolean suaNhanVien(NhanVienDTO nv) {
        String sqlNhanVien = "UPDATE NhanVien SET Ho = ?, Ten = ?, GioiTinh = ?, SoDienThoai = ?, ChucVu = ?, TrangThai = ? WHERE MaNV = ?";
        String sqlTaiKhoan = "UPDATE TaiKhoan SET TaiKhoan = ?, MatKhau = ?, Quyen = ?, TrangThai = ? WHERE MaNV = ?";
        
        Connection conn = null;
        PreparedStatement psNhanVien = null;
        PreparedStatement psTaiKhoan = null;

        try {
            conn = DBConnect.getConnection();
            conn.setAutoCommit(false);

            
            psNhanVien = conn.prepareStatement(sqlNhanVien);
            psNhanVien.setString(1, nv.getHo());
            psNhanVien.setString(2, nv.getTen());
            psNhanVien.setString(3, nv.getGioiTinh());
            psNhanVien.setString(4, nv.getSoDienThoai());
            psNhanVien.setString(5, nv.getChucVu());
            psNhanVien.setBoolean(6, nv.isTrangThai());
            psNhanVien.setInt(7, nv.getMaNV());
            
            int rowsAffectedNV = psNhanVien.executeUpdate();

            
            psTaiKhoan = conn.prepareStatement(sqlTaiKhoan);
            psTaiKhoan.setString(1, nv.getTenTaiKhoan());
            psTaiKhoan.setString(2, nv.getMatKhau());
            psTaiKhoan.setString(3, nv.getChucVu());
            psTaiKhoan.setBoolean(4, nv.isTrangThai());
            psTaiKhoan.setInt(5, nv.getMaNV());

            int rowsAffectedTK = psTaiKhoan.executeUpdate();

            
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

    public ArrayList<NhanVienDTO> timKiemNhanVien(String tuKhoa) {
        ArrayList<NhanVienDTO> danhSach = new ArrayList<>();
        String sql = "SELECT nv.*, tk.TaiKhoan, tk.MatKhau " +
                     "FROM NhanVien nv " +
                     "LEFT JOIN TaiKhoan tk ON nv.MaNV = tk.MaNV " +
                     "WHERE CAST(nv.MaNV AS VARCHAR) LIKE ? " +
                     "OR CONCAT(nv.Ho, ' ', nv.Ten) LIKE ? " +
                     "OR nv.SoDienThoai LIKE ? " +
                     "OR tk.TaiKhoan LIKE ?";
        
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
                        rs.getString("ChucVu"),
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
    
    public ArrayList<String> layDanhSachChucVu() {
        ArrayList<String> chucVuList = new ArrayList<>();
        // Truy vấn SQL: DISTINCT ChucVu để lấy các giá trị duy nhất
        String sql = "SELECT DISTINCT ChucVu FROM NhanVien";
        
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
                if (rs != null) rs.close();
            } catch (SQLException ex) {
                // Bỏ qua lỗi đóng
            }
        }
        return chucVuList;
    }
}