/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UTIL;

import DTO.TaiKhoan;

public class Auth {
    private static TaiKhoan user;
   

    // Phương thức tĩnh để lưu thông tin tài khoản sau khi đăng nhập thành công
    public static void setUser(TaiKhoan loggedInUser) {
        Auth.user = loggedInUser;
    }

    // Phương thức tĩnh công khai để LẤY thông tin tài khoản (Fix lỗi 'cannot find symbol')
    public static TaiKhoan getUser() {
        return Auth.user;
    }

    // Phương thức kiểm tra đã đăng nhập chưa
    public static boolean isLogin() {
        return Auth.user != null;
    }
   
}

