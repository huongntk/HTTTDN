/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.TaiKhoanDAO;
import DTO.TaiKhoan;

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

}
