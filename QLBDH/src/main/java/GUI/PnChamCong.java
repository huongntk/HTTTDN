package GUI;

import BUS.ChamCongBUS;
import BUS.NhanVienBUS;
import DTO.ChamCongDTO;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;
import DTO.TaiKhoan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PnChamCong extends JPanel {
    private PhanQuyen phanQuyen;
    private TaiKhoan taiKhoan;
    private int maNVHienTai;
    private boolean isQuanLy;
    private ChamCongBUS chamCongBUS;
    private NhanVienBUS nhanVienBUS;
    private DefaultTableModel model;
    private JComboBox<Integer> cboThang, cboNam;
    private JLabel lblNhanVien;
    private JComboBox<NhanVienDTO> cboNhanVien;
    private JTable table;
    private JButton btnChamCong, btnSua, btnXoa, btnLamMoi;
    private JPanel pnlTop;
    // Ca làm và giờ mặc định
    private class CaLamInfo {
        String tenCa;
        String gioVao;
        String gioRa;
        
        CaLamInfo(String tenCa, String gioVao, String gioRa) {
            this.tenCa = tenCa;
            this.gioVao = gioVao;
            this.gioRa = gioRa;
        }
    }
    
    private final Map<String, CaLamInfo> DANH_SACH_CA = new LinkedHashMap<>();
    
    public PnChamCong(PhanQuyen pq, TaiKhoan tk) {
        this.phanQuyen = pq;
        this.taiKhoan =tk;
        this.maNVHienTai = tk.getMaNV();
        chamCongBUS = new ChamCongBUS();
        nhanVienBUS = new NhanVienBUS();
        this.isQuanLy = pq.isNsXemDanhSach() || pq.isNsThem() || pq.isNsXoa();
        // Khởi tạo danh sách ca làm
        DANH_SACH_CA.put("Ca sáng", new CaLamInfo("Ca sáng", "08:00:00", "12:00:00"));
        DANH_SACH_CA.put("Ca chiều", new CaLamInfo("Ca chiều", "13:00:00", "17:00:00"));
        DANH_SACH_CA.put("Cả ngày", new CaLamInfo("Cả ngày", "08:00:00", "17:00:00"));
        
        initComponents();
        if (isQuanLy) {
            loadNhanVien();            // Load danh sách nhân viên vào combobox
        } else {
          
            pnlTop.remove(cboNhanVien);
            pnlTop.remove(lblNhanVien);
        }
        loadData();
        addEvents();
        configureByPermission();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel trên cùng
        pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
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
        
        lblNhanVien = new JLabel("Nhân viên:");
        pnlTop.add(lblNhanVien);
        cboNhanVien = new JComboBox<>();
        cboNhanVien.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof NhanVienDTO) {
                    NhanVienDTO nv = (NhanVienDTO) value;
                    value = nv.getMaNV() + " - " + nv.getHo() + " " + nv.getTen();
                }
                return super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);
            }
        });
        pnlTop.add(cboNhanVien);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png")));
        pnlTop.add(btnLamMoi);

        add(pnlTop, BorderLayout.NORTH);

        // Bảng chấm công
        String[] columns = {"Ngày", "Ca làm", "Giờ vào", "Giờ ra", "Trạng thái", "Ghi chú"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Không cho sửa trực tiếp trên bảng, chỉ sửa qua dialog
            }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        
        // Căn giữa cột Ngày
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Panel nút dưới cùng
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnChamCong = new JButton("Chấm công hôm nay");
        btnChamCong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clock.png")));
        btnChamCong.setBackground(new Color(0, 153, 76));
        btnChamCong.setForeground(Color.WHITE);
        
        btnSua = new JButton("Sửa chấm công");
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sua.png")));
        
        btnXoa = new JButton("Xóa");
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xoa.png")));
        
        pnlBottom.add(btnChamCong);
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
        int maNV;
        
        if (isQuanLy) {
            if (cboNhanVien.getSelectedItem() == null) return;
            NhanVienDTO nv = (NhanVienDTO) cboNhanVien.getSelectedItem();
            maNV = nv.getMaNV();
        } else {
            maNV = maNVHienTai;
        }

        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();

        ArrayList<ChamCongDTO> list = chamCongBUS.layChamCongTheoNhanVienThang(maNV, thang, nam);
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Tạo map để dễ xử lý
        Map<String, ChamCongDTO> map = new HashMap<>();
        for (ChamCongDTO cc : list) {
            String ngayStr = sdf.format(cc.getNgayLam());
            map.put(ngayStr, cc);
        }

        // Duyệt tất cả các ngày trong tháng
        Calendar cal = Calendar.getInstance();
        cal.set(nam, thang - 1, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date homNay = new Date();
        
        for (int day = 1; day <= maxDay; day++) {
            cal.set(nam, thang - 1, day);
            Date ngay = cal.getTime();
            String ngayStr = sdf.format(ngay);
            
            ChamCongDTO cc = map.get(ngayStr);
            if (cc == null) {
                // Ngày chưa có dữ liệu
                String trangThaiHienThi = "";
                // Nếu là ngày trong quá khứ và chưa chấm công -> hiển thị cảnh báo
                if (ngay.before(homNay)) {
                    trangThaiHienThi = "❌ Chưa chấm công";
                } else if (isSameDay(ngay, homNay)) {
                    trangThaiHienThi = "⏰ Chưa chấm công hôm nay";
                }
                model.addRow(new Object[]{ngayStr, "", "", "", trangThaiHienThi, ""});
            } else {
                String trangThaiHienThi = cc.getTrangThai() != null ? cc.getTrangThai() : "";
                // Thêm icon cho trạng thái
                if ("Đi làm".equals(trangThaiHienThi)) trangThaiHienThi = "✅ " + trangThaiHienThi;
                else if ("Đi muộn".equals(trangThaiHienThi)) trangThaiHienThi = "⚠️ " + trangThaiHienThi;
                else if ("Nghỉ có phép".equals(trangThaiHienThi)) trangThaiHienThi = "📝 " + trangThaiHienThi;
                else if ("Nghỉ không phép".equals(trangThaiHienThi)) trangThaiHienThi = "❌ " + trangThaiHienThi;
                
                model.addRow(new Object[]{
                    ngayStr,
                    cc.getCaLam() != null ? cc.getCaLam() : "",
                    cc.getGioVao() != null ? cc.getGioVao().toString() : "",
                    cc.getGioRa() != null ? cc.getGioRa().toString() : "",
                    trangThaiHienThi,
                    cc.getGhiChu() != null ? cc.getGhiChu() : ""
                });
            }
        }
        
        // Highlight dòng hôm nay
        highlightTodayRow();
    }
    
    private void highlightTodayRow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String homNayStr = sdf.format(new Date());
        
        for (int i = 0; i < table.getRowCount(); i++) {
            String ngay = (String) table.getValueAt(i, 0);
            if (ngay.equals(homNayStr)) {
                table.setRowSelectionInterval(i, i);
                // Tô màu nền dòng hôm nay
                table.setRowHeight(i, 30);
                break;
            }
        }
    }
    
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private void addEvents() {
        cboThang.addActionListener(e -> loadData());
        cboNam.addActionListener(e -> loadData());
        cboNhanVien.addActionListener(e -> loadData());
        btnLamMoi.addActionListener(e -> loadData());

        btnChamCong.addActionListener(e -> chamCongHomNay());
        btnSua.addActionListener(e -> suaChamCong());
        btnXoa.addActionListener(e -> xoaChamCong());

        // Double click để sửa
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) suaChamCong();
                }
            }
        });
    }
    
    private int getSelectedMaNV() {
        if (isQuanLy) {
            NhanVienDTO nv = (NhanVienDTO) cboNhanVien.getSelectedItem();
            return nv != null ? nv.getMaNV() : -1;
        } else {
            return maNVHienTai;
        }
    }
    
    // Chấm công cho ngày hôm nay
    private void chamCongHomNay() {
         boolean coQuyen = isQuanLy ? phanQuyen.isNsThem() : phanQuyen.isNsChamCong();
        if (!coQuyen) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền chấm công!");
            return;
        }

        int maNV = getSelectedMaNV();
        if (maNV == -1) return;
        Date homNay = new Date();
        
        // Kiểm tra xem hôm nay đã chấm công chưa
        ArrayList<ChamCongDTO> list = chamCongBUS.layChamCongTheoNhanVienThang(maNV, 
            Calendar.getInstance().get(Calendar.MONTH) + 1, 
            Calendar.getInstance().get(Calendar.YEAR));
        
        boolean daCham = list.stream().anyMatch(cc -> isSameDay(cc.getNgayLam(), homNay));
        
        if (daCham) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Hôm nay đã có dữ liệu chấm công. Bạn có muốn cập nhật lại không?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Cập nhật lại chấm công hôm nay
                capNhatChamCongHomNay(maNV, homNay);
            }
            return;
        }
        
        // Hiển thị dialog chọn ca làm cho hôm nay
        hienThiDialogChamCongHomNay(maNV, homNay);
    }
    
    private void hienThiDialogChamCongHomNay(int maNV, Date ngay) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chấm công hôm nay", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JLabel lblNgay = new JLabel(sdf.format(ngay));
        lblNgay.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Lấy giờ hiện tại
        Calendar cal = Calendar.getInstance();
        String gioHienTai = String.format("%02d:%02d:%02d", 
            cal.get(Calendar.HOUR_OF_DAY), 
            cal.get(Calendar.MINUTE), 
            cal.get(Calendar.SECOND));
        
        JComboBox<String> cboCaLam = new JComboBox<>(DANH_SACH_CA.keySet().toArray(new String[0]));
        JTextField txtGioVao = new JTextField(gioHienTai);
        JTextField txtGioRa = new JTextField("17:00:00");
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đi làm", "Đi muộn", "Về sớm"});
        JTextField txtGhiChu = new JTextField();
        
        // Sự kiện khi chọn ca làm
        cboCaLam.addActionListener(e -> {
            String caLam = (String) cboCaLam.getSelectedItem();
            CaLamInfo caInfo = DANH_SACH_CA.get(caLam);
            if (caInfo != null) {
                txtGioVao.setText(caInfo.gioVao);
                txtGioRa.setText(caInfo.gioRa);
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Ngày:"), gbc);
        gbc.gridx = 1;
        dialog.add(lblNgay, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Ca làm:*"), gbc);
        gbc.gridx = 1;
        dialog.add(cboCaLam, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Giờ vào:*"), gbc);
        gbc.gridx = 1;
        dialog.add(txtGioVao, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Giờ ra:*"), gbc);
        gbc.gridx = 1;
        dialog.add(txtGioRa, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        dialog.add(cboTrangThai, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtGhiChu, gbc);
        
        JPanel pnlButtons = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Chấm công");
        JButton btnCancel = new JButton("Hủy");
        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        dialog.add(pnlButtons, gbc);
        
        btnSave.addActionListener(e -> {
            String caLam = (String) cboCaLam.getSelectedItem();
            String gioVaoStr = txtGioVao.getText().trim();
            String gioRaStr = txtGioRa.getText().trim();
            
            if (!kiemTraGioHopLe(gioVaoStr, gioRaStr)) return;
            
            try {
                ChamCongDTO cc = new ChamCongDTO();
                cc.setMaNV(maNV);
                cc.setNgayLam(ngay);
                cc.setCaLam(caLam);
                cc.setGioVao(Time.valueOf(gioVaoStr));
                cc.setGioRa(Time.valueOf(gioRaStr));
                cc.setTrangThai((String) cboTrangThai.getSelectedItem());
                cc.setGhiChu(txtGhiChu.getText());
                
                if (chamCongBUS.themChamCong(cc)) {
                    JOptionPane.showMessageDialog(dialog, "Chấm công thành công!");
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Chấm công thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }
    
    private void capNhatChamCongHomNay(int maNV, Date ngay) {
        // Lấy bản ghi chấm công hôm nay để sửa
        ArrayList<ChamCongDTO> list = chamCongBUS.layChamCongTheoNhanVienThang(maNV, 
            Calendar.getInstance().get(Calendar.MONTH) + 1, 
            Calendar.getInstance().get(Calendar.YEAR));
        
        ChamCongDTO cc = list.stream().filter(c -> isSameDay(c.getNgayLam(), ngay)).findFirst().orElse(null);
        if (cc == null) return;
        
        hienThiDialogSua(cc);
    }
    
    private void suaChamCong() {
        if (!phanQuyen.isNsSua()) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền sửa chấm công!");
            return;
        }
        
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày cần sửa!");
            return;
        }
        
        String ngayStr = (String) model.getValueAt(row, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date ngay;
        try {
            ngay = sdf.parse(ngayStr);
        } catch (Exception ex) {
            return;
        }
        
        NhanVienDTO nv = (NhanVienDTO) cboNhanVien.getSelectedItem();
        ArrayList<ChamCongDTO> list = chamCongBUS.layChamCongTheoNhanVienThang(nv.getMaNV(), 
            (int)cboThang.getSelectedItem(), (int)cboNam.getSelectedItem());
        ChamCongDTO cc = list.stream().filter(c -> c.getNgayLam().equals(ngay)).findFirst().orElse(null);
        
        if (cc == null) {
            JOptionPane.showMessageDialog(this, "Ngày này chưa có dữ liệu chấm công!\nVui lòng chấm công cho ngày hôm nay hoặc thêm mới (nếu được phép).");
            return;
        }
        
        hienThiDialogSua(cc);
    }
    
    private void hienThiDialogSua(ChamCongDTO cc) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa chấm công", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        dialog.setSize(450, 380);
        dialog.setLocationRelativeTo(this);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        JComboBox<String> cboCaLam = new JComboBox<>(DANH_SACH_CA.keySet().toArray(new String[0]));
        cboCaLam.setSelectedItem(cc.getCaLam());
        
        JTextField txtGioVao = new JTextField(cc.getGioVao() != null ? cc.getGioVao().toString() : "");
        JTextField txtGioRa = new JTextField(cc.getGioRa() != null ? cc.getGioRa().toString() : "");
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đi làm", "Đi muộn", "Về sớm", "Nghỉ có phép", "Nghỉ không phép"});
        cboTrangThai.setSelectedItem(cc.getTrangThai());
        JTextField txtGhiChu = new JTextField(cc.getGhiChu() != null ? cc.getGhiChu() : "");
        
        // Sự kiện khi chọn ca làm
        cboCaLam.addActionListener(e -> {
            String caLam = (String) cboCaLam.getSelectedItem();
            CaLamInfo caInfo = DANH_SACH_CA.get(caLam);
            if (caInfo != null) {
                txtGioVao.setText(caInfo.gioVao);
                txtGioRa.setText(caInfo.gioRa);
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Ngày:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JLabel(sdf.format(cc.getNgayLam())), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Ca làm:*"), gbc);
        gbc.gridx = 1;
        dialog.add(cboCaLam, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Giờ vào:*"), gbc);
        gbc.gridx = 1;
        dialog.add(txtGioVao, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Giờ ra:*"), gbc);
        gbc.gridx = 1;
        dialog.add(txtGioRa, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        dialog.add(cboTrangThai, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        dialog.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1;
        dialog.add(txtGhiChu, gbc);
        
        JPanel pnlButtons = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);
        
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        dialog.add(pnlButtons, gbc);
        
        btnSave.addActionListener(e -> {
            String caLam = (String) cboCaLam.getSelectedItem();
            String gioVaoStr = txtGioVao.getText().trim();
            String gioRaStr = txtGioRa.getText().trim();
            
            if (!kiemTraGioHopLe(gioVaoStr, gioRaStr)) return;
            
            try {
                cc.setCaLam(caLam);
                cc.setGioVao(Time.valueOf(gioVaoStr));
                cc.setGioRa(Time.valueOf(gioRaStr));
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
        if (!phanQuyen.isNsXoa()) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa chấm công!");
            return;
        }
        
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
        ArrayList<ChamCongDTO> list = chamCongBUS.layChamCongTheoNhanVienThang(nv.getMaNV(), 
            (int)cboThang.getSelectedItem(), (int)cboNam.getSelectedItem());
        ChamCongDTO cc = list.stream().filter(c -> c.getNgayLam().equals(ngay)).findFirst().orElse(null);
        if (cc == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Xóa chấm công ngày " + ngayStr + "?\nHành động này không thể hoàn tác!",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (chamCongBUS.xoaChamCong(cc.getMaChamCong())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        }
    }
    
    // Kiểm tra giờ hợp lệ
    private boolean kiemTraGioHopLe(String gioVaoStr, String gioRaStr) {
        try {
            if (gioVaoStr == null || gioVaoStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giờ vào không được để trống");
                return false;
            }
            if (gioRaStr == null || gioRaStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giờ ra không được để trống");
                return false;
            }
            
            Time gioVao = Time.valueOf(gioVaoStr);
            Time gioRa = Time.valueOf(gioRaStr);
            
            if (gioRa.before(gioVao) || gioRa.equals(gioVao)) {
                JOptionPane.showMessageDialog(this, "Giờ ra phải sau giờ vào!");
                return false;
            }
            
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Định dạng giờ không hợp lệ! (HH:MM:SS)");
            return false;
        }
    }

    private void configureByPermission() {
        boolean hasThem = phanQuyen.isNsThem();
        boolean hasSua = phanQuyen.isNsSua();
        boolean hasXoa = phanQuyen.isNsXoa();
        
       if (isQuanLy) {
            btnChamCong.setVisible(phanQuyen.isNsThem());
            btnSua.setVisible(phanQuyen.isNsSua());
            btnXoa.setVisible(phanQuyen.isNsXoa());
        } else {
            // Nhân viên thường: chỉ hiện nút Chấm công hôm nay, có thể cho sửa (nếu muốn)
            btnChamCong.setVisible(phanQuyen.isNsChamCong());
            btnSua.setVisible(phanQuyen.isNsChamCong());   // Cho phép sửa chấm công của mình
            btnXoa.setVisible(false);                      // Không cho xóa
        }
    }
}