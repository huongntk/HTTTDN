package DAO;
import UTIL.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {

    // Hàm thực thi lệnh INSERT, UPDATE, DELETE
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            setParameters(ps, params);
            rows = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    // Hàm thực thi lệnh SELECT (trả về ResultSet)
    public static ResultSet executeQuery(String sql, Object... params) {
        ResultSet rs = null;
        try {
            Connection conn = DBConnect.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            setParameters(ps, params);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    // Gán giá trị cho dấu ? trong câu SQL
    private static void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}
