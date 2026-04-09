package GUI;

import BUS.ChamCongBUS;
import BUS.NhanVienBUS;
import DTO.ChamCongDTO;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PnChamCong extends JPanel {
    private PhanQuyen phanQuyen;
    private ChamCongBUS chamCongBUS;
    private NhanVienBUS nhanVienBUS;
    private DefaultTableModel model;
    private JComboBox<Integer> cboThang, cboNam;
    private JComboBox<NhanVienDTO> cboNhanVien;
    private JTable table;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public PnChamCong(PhanQuyen pq) {
        this.phanQuyen = pq;
        chamCongBUS = new ChamCongBUS();
        nhanVienBUS = new NhanVienBUS();
        initComponents();
        loadNhanVien();
        loadData();
        addEvents();
        configureByPermission();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel trên cùng: chọn tháng, năm, nhân viên
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlTop.add(new JLabel("Tháng:"));
        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cboThang.addItem(i);
        cboThang.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);
        pnlTop.add(cboThang);

        pnlTop.add(new JLabel("Năm:"));
        cboNam = new JComboBox<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = year - 5; i <= year + 5; i++) cboNam.addItem(i);
        cboNam.setSelectedItem(year);
        pnlTop.add(cboNam);

        pnlTop.add(new JLabel("Nhân viên:"));
        cboNhanVien = new JComboBox<>();
        cboNhanVien.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof NhanVienDTO) {
                    NhanVienDTO nv = (NhanVienDTO) value;
                    // Giả sử NhanVienDTO có getMaNV() và getTenNV() 
                    // (nếu tên phương thức khác, sửa lại cho đúng)
                    value = nv.getMaNV() + " - " + nv.getHo() + " " + nv.getTen();
                }
                return super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);
            }
        });
        pnlTop.add(cboNhanVien);

        btnLamMoi = new JButton("Làm mới");
        pnlTop.add(btnLamMoi);

        add(pnlTop, BorderLayout.NORTH);

        // Bảng chấm công
        String[] columns = {"Ngày", "Ca làm", "Giờ vào", "Giờ ra", "Trạng thái", "Ghi chú"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col >= 1; // Cho phép sửa cột Ca làm, Giờ vào, Giờ ra, Trạng thái, Ghi chú
            }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Panel nút dưới cùng
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnThem = new JButton("Thêm chấm công");
        btnSua = new JButton("Cập nhật");
        btnXoa = new JButton("Xóa");
        pnlBottom.add(btnThem);
        pnlBottom.add(btnSua);
        pnlBottom.add(btnXoa);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadNhanVien() {
        ArrayList<NhanVienDTO> list = nhanVienBUS.layDanhSachNhanVien();
        cboNhanVien.removeAllItems();
        for (NhanVienDTO nv : list) {
            cboNhanVien.addItem(nv);
        }
        if (cboNhanVien.getItemCount() > 0) cboNhanVien.setSelectedIndex(0);
    }

    private void loadData() {
        if (cboNhanVien.getSelectedItem() == null) return;
        NhanVienDTO nv = (NhanVienDTO) cboNhanVien.getSelectedItem();
        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();

        ArrayList<ChamCongDTO> list = chamCongBUS.layChamCongTheoNhanVienThang(nv.getMaNV(), thang, nam);
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Tạo một map để dễ xử lý: ngày -> ChamCongDTO
        java.util.Map<String, ChamCongDTO> map = new java.util.HashMap<>();
        for (ChamCongDTO cc : list) {
            String ngayStr = sdf.format(cc.getNgayLam());
            map.put(ngayStr, cc);
        }

        // Duyệt tất cả các ngày trong tháng để hiển thị đủ
        Calendar cal = Calendar.getInstance();
        cal.set(nam, thang - 1, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= maxDay; day++) {
            cal.set(nam, thang - 1, day);
            Date ngay = cal.getTime();
            String ngayStr = sdf.format(ngay);
            ChamCongDTO cc = map.get(ngayStr);
            if (cc == null) {
                // Ngày chưa có dữ liệu: để trống
                model.addRow(new Object[]{ngayStr, "", "", "", "", ""});
            } else {
                model.addRow(new Object[]{
                    ngayStr,
                    cc.getCaLam() != null ? cc.getCaLam() : "",
                    cc.getGioVao() != null ? cc.getGioVao().toString() : "",
                    cc.getGioRa() != null ? cc.getGioRa().toString() : "",
                    cc.getTrangThai() != null ? cc.getTrangThai() : "",
                    cc.getGhiChu() != null ? cc.getGhiChu() : ""
                });
            }
        }
    }

    private void addEvents() {
        cboThang.addActionListener(e -> loadData());
        cboNam.addActionListener(e -> loadData());
        cboNhanVien.addActionListener(e -> loadData());
        btnLamMoi.addActionListener(e -> loadData());

        btnThem.addActionListener(e -> themChamCong());
        btnSua.addActionListener(e -> capNhatChamCong());
        btnXoa.addActionListener(e -> xoaChamCong());

        // Double click để sửa nhanh
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) hienThiDialogSua(row);
                }
            }
        });
    }

    private void themChamCong() {
        // Lấy ngày từ dòng được chọn (nếu có)
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một ngày để thêm chấm công");
            return;
        }
        String ngayStr = (String) model.getValueAt(row, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date ngay;
        try {
            ngay = sdf.parse(ngayStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày");
            return;
        }

        NhanVienDTO nv = (NhanVienDTO) cboNhanVien.getSelectedItem();
        // Kiểm tra đã có chưa
        if (chamCongBUS.layChamCongTheoNhanVienThang(nv.getMaNV(), (int)cboThang.getSelectedItem(), (int)cboNam.getSelectedItem())
                .stream().anyMatch(cc -> cc.getNgayLam().equals(ngay))) {
            JOptionPane.showMessageDialog(this, "Ngày này đã có dữ liệu chấm công. Hãy chọn Sửa.");
            return;
        }

        ChamCongDTO cc = new ChamCongDTO();
        cc.setMaNV(nv.getMaNV());
        cc.setNgayLam(ngay);
        cc.setCaLam("Cả ngày");
        cc.setGioVao(Time.valueOf("08:00:00"));
        cc.setGioRa(Time.valueOf("17:00:00"));
        cc.setTrangThai("Đi làm");
        cc.setGhiChu("");

        if (chamCongBUS.themChamCong(cc)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại (có thể trùng ngày)");
        }
    }

    private void capNhatChamCong() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần cập nhật");
            return;
        }
        hienThiDialogSua(row);
    }

    private void hienThiDialogSua(int row) {
        String ngayStr = (String) model.getValueAt(row, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date ngay;
        try {
            ngay = sdf.parse(ngayStr);
        } catch (Exception ex) {
            return;
        }
        NhanVienDTO nv = (NhanVienDTO) cboNhanVien.getSelectedItem();
        ArrayList<ChamCongDTO> list = chamCongBUS.layChamCongTheoNhanVienThang(nv.getMaNV(), (int)cboThang.getSelectedItem(), (int)cboNam.getSelectedItem());
        ChamCongDTO cc = list.stream().filter(c -> c.getNgayLam().equals(ngay)).findFirst().orElse(null);
        if (cc == null) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu chấm công ngày này, hãy thêm mới");
            return;
        }

        // Tạo dialog sửa
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa chấm công", true);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField txtCaLam = new JTextField(cc.getCaLam());
        JTextField txtGioVao = new JTextField(cc.getGioVao() != null ? cc.getGioVao().toString() : "");
        JTextField txtGioRa = new JTextField(cc.getGioRa() != null ? cc.getGioRa().toString() : "");
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đi làm", "Đi muộn", "Về sớm", "Nghỉ có phép", "Nghỉ không phép"});
        cboTrangThai.setSelectedItem(cc.getTrangThai());
        JTextField txtGhiChu = new JTextField(cc.getGhiChu());

        dialog.add(new JLabel("Ca làm:")); dialog.add(txtCaLam);
        dialog.add(new JLabel("Giờ vào (HH:MM:SS):")); dialog.add(txtGioVao);
        dialog.add(new JLabel("Giờ ra (HH:MM:SS):")); dialog.add(txtGioRa);
        dialog.add(new JLabel("Trạng thái:")); dialog.add(cboTrangThai);
        dialog.add(new JLabel("Ghi chú:")); dialog.add(txtGhiChu);
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        dialog.add(btnSave);
        dialog.add(btnCancel);

        btnSave.addActionListener(e -> {
            try {
                cc.setCaLam(txtCaLam.getText());
                cc.setGioVao(Time.valueOf(txtGioVao.getText()));
                cc.setGioRa(Time.valueOf(txtGioRa.getText()));
                cc.setTrangThai((String) cboTrangThai.getSelectedItem());
                cc.setGhiChu(txtGhiChu.getText());
                if (chamCongBUS.suaChamCong(cc)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công");
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi nhập giờ: " + ex.getMessage());
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    private void xoaChamCong() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        String ngayStr = (String) model.getValueAt(row, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date ngay;
        try {
            ngay = sdf.parse(ngayStr);
        } catch (Exception ex) {
            return;
        }
        NhanVienDTO nv = (NhanVienDTO) cboNhanVien.getSelectedItem();
        ArrayList<ChamCongDTO> list = chamCongBUS.layChamCongTheoNhanVienThang(nv.getMaNV(), (int)cboThang.getSelectedItem(), (int)cboNam.getSelectedItem());
        ChamCongDTO cc = list.stream().filter(c -> c.getNgayLam().equals(ngay)).findFirst().orElse(null);
        if (cc == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa chấm công ngày " + ngayStr + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            if (chamCongBUS.xoaChamCong(cc.getMaChamCong())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        }
    }

    private void configureByPermission() {
        // Ẩn nút nếu không có quyền (tùy theo yêu cầu)
        boolean hasEdit = phanQuyen.isNsSua(); // ví dụ dùng quyền sửa nhân sự
        btnThem.setVisible(hasEdit);
        btnSua.setVisible(hasEdit);
        btnXoa.setVisible(hasEdit);
        table.setEnabled(hasEdit);
    }
}