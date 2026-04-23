package GUI;

import BUS.NhanVienBUS;
import BUS.LichSuChucVuBUS;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;

public class FrmThayDoiChucVu extends JDialog {
    private int maNV;
    private NhanVienBUS nvBUS;
    private LichSuChucVuBUS lichSuBUS;
    private JTextField txtMaNV, txtHoTen, txtChucVuHienTai;
    private JComboBox<String> cboChucVuMoi;
    private JTextField txtNgayHieuLuc;
    private JTextArea txtLyDo;
    private PhanQuyen phanQuyen;

    public FrmThayDoiChucVu(int maNV, PhanQuyen phanQuyen) {
        this.phanQuyen = phanQuyen;
        this.maNV = maNV;
        nvBUS = new NhanVienBUS();
        lichSuBUS = new LichSuChucVuBUS();
        initComponents();
        loadData();
        setTitle("Thay đổi chức vụ");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setModal(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("THAY ĐỔI CHỨC VỤ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlCenter.add(new JLabel("Mã NV:"), gbc);
        gbc.gridx = 1;
        txtMaNV = new JTextField(20);
        txtMaNV.setEditable(false);
        pnlCenter.add(txtMaNV, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlCenter.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1;
        txtHoTen = new JTextField(20);
        txtHoTen.setEditable(false);
        pnlCenter.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlCenter.add(new JLabel("Chức vụ hiện tại:"), gbc);
        gbc.gridx = 1;
        txtChucVuHienTai = new JTextField(20);
        txtChucVuHienTai.setEditable(false);
        pnlCenter.add(txtChucVuHienTai, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlCenter.add(new JLabel("Chức vụ mới:"), gbc);
        gbc.gridx = 1;
        cboChucVuMoi = new JComboBox<>();
        pnlCenter.add(cboChucVuMoi, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        pnlCenter.add(new JLabel("Ngày hiệu lực:"), gbc);
        gbc.gridx = 1;
        txtNgayHieuLuc = new JTextField(20);
        txtNgayHieuLuc.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        pnlCenter.add(txtNgayHieuLuc, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        pnlCenter.add(new JLabel("Lý do thay đổi:"), gbc);
        gbc.gridx = 1;
        txtLyDo = new JTextArea(3, 20);
        txtLyDo.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(txtLyDo);
        pnlCenter.add(scroll, gbc);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();
        JButton btnLuu = new JButton("Lưu");
        btnLuu.addActionListener(e -> luuThayDoi());
        JButton btnHuy = new JButton("Hủy");
        btnHuy.addActionListener(e -> dispose());
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);
    }

    private String layTenChucVuTuMa(String maCV) {
        if (maCV == null) return "Nhân viên";
        switch (maCV.trim()) {
            case "CV01": return "Giám đốc";
            case "CV02": return "Quản lý nhân sự";
            case "CV03": return "Quản lý kho";
            case "CV04": return "Quản lý bán hàng";
            case "CV05": return "Nhân viên bán hàng";
            case "CV06": return "Nhân viên nhập hàng";
            default: return "Nhân viên";
        }
    }

    private void loadData() {
        NhanVienDTO nv = nvBUS.layNhanVienTheoMa(maNV);
        if (nv != null) {
            txtMaNV.setText(String.valueOf(nv.getMaNV()));
            txtHoTen.setText(nv.getHo() + " " + nv.getTen());
            
            // Lấy tên chức vụ từ mã để hiển thị
            String tenChucVu = layTenChucVuTuMa(nv.getMaCV());
            txtChucVuHienTai.setText(tenChucVu); 
        }
        
        // Load danh sách chức vụ (trừ chức vụ hiện tại)
        ArrayList<String> dsChucVu = nvBUS.layDanhSachChucVu();
        dsChucVu.remove(txtChucVuHienTai.getText()); // Lúc này remove() mới hoạt động đúng vì đã cùng là "Tên"
        for (String cv : dsChucVu) {
            cboChucVuMoi.addItem(cv);
        }
    }

    private void luuThayDoi() {
        String chucVuMoi = (String) cboChucVuMoi.getSelectedItem();
        if (chucVuMoi == null) {
            JOptionPane.showMessageDialog(this, "Chọn chức vụ mới!");
            return;
        }
        String ngay = txtNgayHieuLuc.getText().trim();
        String lyDo = txtLyDo.getText().trim();
        // Gọi BUS để cập nhật chức vụ và lưu lịch sử
        boolean success = nvBUS.capNhatChucVu(maNV, chucVuMoi, ngay, lyDo);
        if (success) {
            JOptionPane.showMessageDialog(this, "Thay đổi chức vụ thành công!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thay đổi thất bại!");
        }
    }
}