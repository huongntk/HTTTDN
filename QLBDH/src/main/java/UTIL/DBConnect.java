package UTIL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QLBanDongHo;encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "18012003";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("ket noi thanh cong db :" + conn.getCatalog());
        } catch (ClassNotFoundException e) {
            System.out.println("Khong tim thay driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Khong the ket noi db");
            e.printStackTrace();
        }
        return conn;
    }
}