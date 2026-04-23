/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoan;
import java.util.ArrayList;

public class TaiKhoanBUS {
    private TaiKhoanDAO tkDAO = new TaiKhoanDAO();
    
    //Xử lý đăng nhập
    public TaiKhoan dangNhap(String user, String pass) {
        if (user == null || user.isEmpty()) {
            System.out.println("Tên đăng nhập không được để trống!");
            return null;
        }
        if (pass == null || pass.isEmpty()) {
            System.out.println("Mật khẩu không được để trống!");
            return null;
        }

        TaiKhoan tk = tkDAO.login(user, pass);
        if (tk == null) {
            System.out.println("Sai tài khoản hoặc mật khẩu!");
        }
        return tk;
    }
    // Xử lý đổi mật khẩu
    public boolean doiMatKhau(String user, String oldPass, String newPass, String confirmPass) {
        if (newPass == null || newPass.isEmpty() || confirmPass == null || confirmPass.isEmpty()) {
            System.out.println("Mật khẩu mới không được để trống!");
            return false;
        }
        if (!newPass.equals(confirmPass)) {
            System.out.println("Mật khẩu xác nhận không trùng khớp!");
            return false;
        }
        if (newPass.equals(oldPass)) {
            System.out.println("Mật khẩu mới không được trùng mật khẩu cũ!");
            return false;
        }
        
        // Kiểm tra tài khoản tồn tại và mật khẩu cũ đúng
        TaiKhoan tk = tkDAO.getTaiKhoanByUsername(user);
        if (tk == null) {
            System.out.println("Tài khoản không tồn tại!");
            return false;
        }
        if (!tk.getMatKhau().equals(oldPass)) {
            System.out.println("Mật khẩu cũ không đúng!");
            return false;
        }
        return tkDAO.doiMatKhau(user, newPass);
    }

     public ArrayList<TaiKhoan> layDanhSachTaiKhoan() {
        return tkDAO.layDanhSachTaiKhoan();
    }

    public TaiKhoan layTaiKhoanTheoMa(int maNV) {
        return tkDAO.layTaiKhoanTheoMa(maNV);
    }

    public TaiKhoan layTaiKhoanTheoTenDangNhap(String tenDangNhap) {
        return tkDAO.layTaiKhoanTheoTenDangNhap(tenDangNhap);
    }

    public boolean themTaiKhoan(TaiKhoan tk) {
        // Kiểm tra tên đăng nhập đã tồn tại chưa
        if (layTaiKhoanTheoTenDangNhap(tk.getTaiKhoan()) != null) {
            return false; // Tên đăng nhập đã tồn tại
        }
        return tkDAO.themTaiKhoan(tk);
    }

    public boolean suaTaiKhoan(TaiKhoan tk, boolean updatePassword) {
        // Nếu đổi tên đăng nhập, kiểm tra trùng (trừ chính nó)
        TaiKhoan existing = layTaiKhoanTheoTenDangNhap(tk.getTaiKhoan());
        if (existing != null && existing.getMaTK() != tk.getMaTK()) {
            return false; // Tên đăng nhập đã tồn tại ở tài khoản khác
        }
        return tkDAO.suaTaiKhoan(tk, updatePassword);
    }

    public boolean xoaTaiKhoan(int maNV) {
        return tkDAO.xoaTaiKhoan(maNV);
    }

}
