package GUI;

import BUS.ChamCongBUS;
import BUS.NhanVienBUS;
import BUS.LichLamViecBUS;

import DTO.ChamCongDTO;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;
import DTO.TaiKhoan;
import DTO.LichLamViecDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PnChamCong extends JPanel {

    private PhanQuyen phanQuyen;
    private TaiKhoan taiKhoan;
    private int maNVHienTai;
    private boolean isQuanLy;

    private ChamCongBUS chamCongBUS;
    private NhanVienBUS nhanVienBUS;
    private LichLamViecBUS lichLamViecBUS;

    private DefaultTableModel model;
    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;
    private JLabel lblNhanVien;
    private JComboBox<NhanVienDTO> cboNhanVien;
    private JTable table;

    private JButton btnChamCong;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private JPanel pnlTop;

    // Giữ lại danh sách ca cũ để phục vụ dialog sửa chấm công của quản lý.
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
        this.taiKhoan = tk;
        this.maNVHienTai = tk.getMaNV();

        this.chamCongBUS = new ChamCongBUS();
        this.nhanVienBUS = new NhanVienBUS();
        this.lichLamViecBUS = new LichLamViecBUS();

        this.isQuanLy = laQuanLyChamCong();

        DANH_SACH_CA.put("Ca sáng", new CaLamInfo("Ca sáng", "08:00:00", "12:00:00"));
        DANH_SACH_CA.put("Ca chiều", new CaLamInfo("Ca chiều", "13:00:00", "17:00:00"));
        DANH_SACH_CA.put("Cả ngày", new CaLamInfo("Cả ngày", "08:00:00", "17:00:00"));

        initComponents();

        if (isQuanLy) {
            loadNhanVien();
        } else {
            pnlTop.remove(cboNhanVien);
            pnlTop.remove(lblNhanVien);
        }

        loadData();
        addEvents();
        configureByPermission();
    }
    private boolean laQuanLyChamCong() {
        if (phanQuyen == null) {
            return false;
        }

        /*
         * Chỉ nhóm quản lý nhân sự thật sự mới được xem/sửa/xóa chấm công người khác.
         * Nhân viên bán hàng dù có quyền chấm công cũng chỉ xem của chính mình.
         */
        return phanQuyen.isNsDuyetNghi()
                || phanQuyen.isNsThayDoiChucVu()
                || phanQuyen.isNsTinhLuong()
                || phanQuyen.isNsXoa()
                || phanQuyen.isAdminQuanLyUser()
                || phanQuyen.isPqQuanLyPhanQuyen();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

        pnlTop.add(new JLabel("Tháng:"));
        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem(i);
        }
        cboThang.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);
        pnlTop.add(cboThang);

        pnlTop.add(new JLabel("Năm:"));
        cboNam = new JComboBox<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = year - 5; i <= year + 5; i++) {
            cboNam.addItem(i);
        }
        cboNam.setSelectedItem(year);
        pnlTop.add(cboNam);

        lblNhanVien = new JLabel("Nhân viên:");
        pnlTop.add(lblNhanVien);

        cboNhanVien = new JComboBox<>();
        cboNhanVien.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                if (value instanceof NhanVienDTO) {
                    NhanVienDTO nv = (NhanVienDTO) value;
                    value = nv.getMaNV() + " - " + nv.getHo() + " " + nv.getTen();
                }

                return super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );
            }
        });
        pnlTop.add(cboNhanVien);

        btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png")));
        pnlTop.add(btnLamMoi);

        add(pnlTop, BorderLayout.NORTH);

        String[] columns = {
            "Ngày",
            "Ca làm",
            "Giờ vào",
            "Giờ ra",
            "Trạng thái",
            "Ghi chú"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

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

        if (cboNhanVien.getItemCount() > 0) {
            cboNhanVien.setSelectedIndex(0);
        }
    }

    private void loadData() {
        int maNV = getSelectedMaNV();

        if (maNV <= 0) {
            return;
        }

        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();

        ArrayList<ChamCongDTO> listChamCong =
                chamCongBUS.layChamCongTheoNhanVienThang(maNV, thang, nam);

        ArrayList<LichLamViecDTO> listLich =
                lichLamViecBUS.layLichTheoNhanVienThang(maNV, thang, nam);

        model.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Map<String, ChamCongDTO> mapChamCong = new HashMap<>();
        for (ChamCongDTO cc : listChamCong) {
            String ngayStr = sdf.format(cc.getNgayLam());
            mapChamCong.put(ngayStr, cc);
        }

        Map<String, LichLamViecDTO> mapLich = new HashMap<>();
        for (LichLamViecDTO lich : listLich) {
            String ngayStr = sdf.format(lich.getNgayLam());
            mapLich.put(ngayStr, lich);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(nam, thang - 1, 1);

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date homNay = new Date();

        for (int day = 1; day <= maxDay; day++) {
            cal.set(nam, thang - 1, day);

            Date ngay = cal.getTime();
            String ngayStr = sdf.format(ngay);

            ChamCongDTO cc = mapChamCong.get(ngayStr);
            LichLamViecDTO lich = mapLich.get(ngayStr);

            if (cc != null) {
                model.addRow(new Object[]{
                    ngayStr,
                    cc.getCaLam() != null ? cc.getCaLam() : "",
                    cc.getGioVao() != null ? cc.getGioVao().toString() : "",
                    cc.getGioRa() != null ? cc.getGioRa().toString() : "",
                    formatTrangThaiChamCong(cc.getTrangThai()),
                    cc.getGhiChu() != null ? cc.getGhiChu() : ""
                });
            } else if (lich != null) {
                model.addRow(new Object[]{
                    ngayStr,
                    lich.getTenCa() != null ? lich.getTenCa() : "",
                    lich.getGioBatDau() != null ? lich.getGioBatDau().toString() : "",
                    lich.getGioKetThuc() != null ? lich.getGioKetThuc().toString() : "",
                    formatTrangThaiLichChuaChamCong(ngay, homNay),
                    lich.getGhiChu() != null ? lich.getGhiChu() : ""
                });
            } else {
                model.addRow(new Object[]{
                    ngayStr,
                    "",
                    "",
                    "",
                    "",
                    ""
                });
            }
        }

        highlightTodayRow();
    }

    private String formatTrangThaiChamCong(String trangThai) {
        if (trangThai == null) {
            return "";
        }

        switch (trangThai) {
            case "Đủ công":
                return "✅ Đủ công";
            case "Đi trễ":
                return "⚠️ Đi trễ";
            case "Về sớm":
                return "⚠️ Về sớm";
            case "Nghỉ có phép":
                return "📝 Nghỉ có phép";
            case "Nghỉ không phép":
                return "❌ Nghỉ không phép";
            default:
                return trangThai;
        }
    }

    private String formatTrangThaiLichChuaChamCong(Date ngay, Date homNay) {
        if (isSameDay(ngay, homNay)) {
            return "⏰ Chưa chấm công hôm nay";
        }

        if (isDateBeforeOnly(ngay, homNay)) {
            return "❌ Chưa chấm công";
        }

        return "📅 Sắp tới";
    }

    private boolean isDateBeforeOnly(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(d1);
        c2.setTime(d2);

        setStartOfDay(c1);
        setStartOfDay(c2);

        return c1.before(c2);
    }

    private void setStartOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private void highlightTodayRow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String homNayStr = sdf.format(new Date());

        for (int i = 0; i < table.getRowCount(); i++) {
            String ngay = (String) table.getValueAt(i, 0);

            if (ngay.equals(homNayStr)) {
                table.setRowSelectionInterval(i, i);
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

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    private void addEvents() {
        cboThang.addActionListener(e -> loadData());
        cboNam.addActionListener(e -> loadData());
        cboNhanVien.addActionListener(e -> loadData());
        btnLamMoi.addActionListener(e -> loadData());

        btnChamCong.addActionListener(e -> chamCongHomNay());
        btnSua.addActionListener(e -> suaChamCong());
        btnXoa.addActionListener(e -> xoaChamCong());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();

                    if (row != -1) {
                        suaChamCong();
                    }
                }
            }
        });
    }

    private int getSelectedMaNV() {
        if (!isQuanLy) {
            return maNVHienTai;
        }

        NhanVienDTO nv = (NhanVienDTO) cboNhanVien.getSelectedItem();
        return nv != null ? nv.getMaNV() : -1;
    }

    private int getMaNVCanChamCong() {
        if (!isQuanLy) {
            return maNVHienTai;
        }

        return getSelectedMaNV();
    }

    private void chamCongHomNay() {
        int maNV = getMaNVCanChamCong();

        if (maNV <= 0) {
            JOptionPane.showMessageDialog(this, "Không xác định được nhân viên cần chấm công!");
            return;
        }

        java.sql.Date homNay = getTodaySqlDate();

        // 1. Kiểm tra hôm nay đã có chấm công/nghỉ phép chưa
        if (chamCongBUS.daChamCong(maNV, homNay)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nhân viên này đã có dữ liệu chấm công hôm nay.\n"
                            + "Nếu cần thay đổi, vui lòng dùng chức năng Sửa chấm công.",
                    "Đã có dữ liệu chấm công",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // 2. Kiểm tra hôm nay có lịch làm chưa
        LichLamViecDTO lichHomNay =
                lichLamViecBUS.layLichTheoNhanVienVaNgay(maNV, homNay);

        if (lichHomNay == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nhân viên này chưa được xếp lịch làm hôm nay.",
                    "Chưa có lịch làm",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 3. Lấy ca từ LichLamViec + CaLam
        String tenCa = lichHomNay.getTenCa();
        Time gioBatDauCa = lichHomNay.getGioBatDau();
        Time gioKetThucCa = lichHomNay.getGioKetThuc();

        if (tenCa == null || gioBatDauCa == null || gioKetThucCa == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Dữ liệu ca làm hôm nay chưa đầy đủ.\nVui lòng kiểm tra lại lịch làm.",
                    "Lỗi dữ liệu lịch làm",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        LocalTime gioHienTai = LocalTime.now().withNano(0);
        LocalTime batDauCa = gioBatDauCa.toLocalTime();
        LocalTime ketThucCa = gioKetThucCa.toLocalTime();

        /*
         * Chỉ cho chấm công trong thời gian ca làm.
         * Ngoài thời gian ca thì báo lỗi và không lưu chấm công.
         */
        if (!isTrongThoiGianCa(gioHienTai, batDauCa, ketThucCa)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ngoài thời gian chấm công.\n\n"
                    + "Ca làm: " + tenCa + "\n"
                    + "Thời gian ca: " + formatTime(gioBatDauCa) + " - " + formatTime(gioKetThucCa),
                    "Không thể chấm công",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Time gioVao = Time.valueOf(gioHienTai);
        Time gioRa = gioKetThucCa;

        /*
         * Nếu đang trong ca nhưng vào sau giờ bắt đầu thì tự set Đi trễ.
         */
        String trangThai = xacDinhTrangThaiTheoLich(gioVao, gioBatDauCa);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận chấm công hôm nay?\n\n"
                        + "Ca làm: " + tenCa + "\n"
                        + "Giờ bắt đầu ca: " + formatTime(gioBatDauCa) + "\n"
                        + "Giờ kết thúc ca: " + formatTime(gioKetThucCa),
//                        + "Giờ vào hiện tại: " + formatTime(gioVao) + "\n"
//                        + "Trạng thái dự kiến: " + trangThai,
                "Xác nhận chấm công",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        ChamCongDTO cc = new ChamCongDTO();
        cc.setMaNV(maNV);
        cc.setNgayLam(homNay);
        cc.setCaLam(tenCa);
        cc.setGioVao(gioVao);
        cc.setGioRa(gioRa);
        cc.setTrangThai(trangThai);
        cc.setGhiChu("Chấm công tự động theo lịch làm #" + lichHomNay.getMaLich());

        boolean ok = chamCongBUS.themChamCong(cc);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Chấm công thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Chấm công thất bại!\nCó thể nhân viên này đã có dữ liệu chấm công hôm nay.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void suaChamCong() {
        if (!isQuanLy) {
        JOptionPane.showMessageDialog(this, "Bạn chỉ được xem và chấm công của chính mình.");
        return;
    }
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
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ!");
            return;
        }

        int maNV = getSelectedMaNV();

        ArrayList<ChamCongDTO> list =
                chamCongBUS.layChamCongTheoNhanVienThang(
                        maNV,
                        (int) cboThang.getSelectedItem(),
                        (int) cboNam.getSelectedItem()
                );

        ChamCongDTO cc = null;

        for (ChamCongDTO item : list) {
            if (isSameDay(item.getNgayLam(), ngay)) {
                cc = item;
                break;
            }
        }

        if (cc == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ngày này chưa có dữ liệu chấm công!\n"
                            + "Chỉ có thể sửa khi đã có bản ghi chấm công.",
                    "Chưa có dữ liệu",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        hienThiDialogSua(cc);
    }

    private void hienThiDialogSua(ChamCongDTO cc) {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Sửa chấm công",
                true
        );

        dialog.setLayout(new GridBagLayout());
        dialog.setSize(480, 300);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        int maNV = cc.getMaNV();
        java.sql.Date ngayLamSql = new java.sql.Date(cc.getNgayLam().getTime());

        /*
         * Lấy ca làm theo lịch làm việc.
         * Không cho chọn tay ca làm.
         */
        LichLamViecDTO lich = lichLamViecBUS.layLichTheoNhanVienVaNgay(maNV, ngayLamSql);

        if (lich == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ngày này chưa có lịch làm việc.\nKhông thể sửa chấm công theo lịch.",
                    "Chưa có lịch làm",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String tenCaTheoLich = lich.getTenCa();
        Time gioBatDauCa = lich.getGioBatDau();
        Time gioKetThucCa = lich.getGioKetThuc();

        if (tenCaTheoLich == null || gioBatDauCa == null || gioKetThucCa == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Dữ liệu ca làm trong lịch chưa đầy đủ.\nVui lòng kiểm tra lại lịch làm việc.",
                    "Lỗi dữ liệu lịch làm",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JLabel lblNgayValue = new JLabel(sdf.format(cc.getNgayLam()));

        JTextField txtCaLamTheoLich = new JTextField(tenCaTheoLich);
        txtCaLamTheoLich.setEditable(false);
        txtCaLamTheoLich.setFocusable(false);
        txtCaLamTheoLich.setBackground(new Color(240, 240, 240));

        JTextField txtGioBatDauCa = new JTextField(gioBatDauCa.toString());
        txtGioBatDauCa.setEditable(false);
        txtGioBatDauCa.setFocusable(false);
        txtGioBatDauCa.setBackground(new Color(240, 240, 240));

        JTextField txtGioKetThucCa = new JTextField(gioKetThucCa.toString());
        txtGioKetThucCa.setEditable(false);
        txtGioKetThucCa.setFocusable(false);
        txtGioKetThucCa.setBackground(new Color(240, 240, 240));

        JTextField txtGhiChu = new JTextField(
                cc.getGhiChu() != null ? cc.getGhiChu() : ""
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Ngày:"), gbc);

        gbc.gridx = 1;
        dialog.add(lblNgayValue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Ca làm theo lịch:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtCaLamTheoLich, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Giờ bắt đầu ca:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtGioBatDauCa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Giờ kết thúc ca:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtGioKetThucCa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(new JLabel("Ghi chú:"), gbc);

        gbc.gridx = 1;
        dialog.add(txtGhiChu, gbc);

        JPanel pnlButtons = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        pnlButtons.add(btnSave);
        pnlButtons.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        dialog.add(pnlButtons, gbc);

        btnSave.addActionListener(e -> {
            /*
             * Form sửa chấm công bây giờ chỉ cho sửa ghi chú.
             * Ca làm vẫn đồng bộ theo lịch.
             * Giờ vào, giờ ra, trạng thái giữ nguyên dữ liệu cũ.
             */
            cc.setCaLam(tenCaTheoLich);
            cc.setGhiChu(txtGhiChu.getText().trim());

            if (chamCongBUS.suaChamCong(cc)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật ghi chú thành công");
                loadData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Cập nhật thất bại!",
                        "Không thể cập nhật",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void xoaChamCong() {
        if (!isQuanLy) {
        JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa chấm công.");
        return;
    }
        if (!phanQuyen.isNsXoa()) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa chấm công!");
            return;
        }

        int row = table.getSelectedRow();

        if (row == -1) {
            return;
        }

        String ngayStr = (String) model.getValueAt(row, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date ngay;

        try {
            ngay = sdf.parse(ngayStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ!");
            return;
        }

        int maNV = getSelectedMaNV();

        ArrayList<ChamCongDTO> list =
                chamCongBUS.layChamCongTheoNhanVienThang(
                        maNV,
                        (int) cboThang.getSelectedItem(),
                        (int) cboNam.getSelectedItem()
                );

        ChamCongDTO cc = null;

        for (ChamCongDTO item : list) {
            if (isSameDay(item.getNgayLam(), ngay)) {
                cc = item;
                break;
            }
        }

        if (cc == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xóa chấm công ngày " + ngayStr + "?\nHành động này không thể hoàn tác!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (chamCongBUS.xoaChamCong(cc.getMaChamCong())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        }
    }

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
        if (isQuanLy) {
            btnChamCong.setVisible(phanQuyen.isNsChamCong() || phanQuyen.isNsThem());
            btnSua.setVisible(phanQuyen.isNsSua());
            btnXoa.setVisible(phanQuyen.isNsXoa());
        } else {
            /*
             * Nhân viên bán hàng:
             * - Được chấm công hôm nay
             * - Không được sửa
             * - Không được xóa
             * - Không được xem/chọn nhân viên khác
             */
            btnChamCong.setVisible(true);
            btnSua.setVisible(false);
            btnXoa.setVisible(false);
        }
    }

    private String xacDinhTrangThaiTheoLich(Time gioVao, Time gioBatDauCa) {
        if (gioVao == null || gioBatDauCa == null) {
            return "Đủ công";
        }

        LocalTime vao = gioVao.toLocalTime();
        LocalTime batDau = gioBatDauCa.toLocalTime();

        long soPhutTre = Duration.between(batDau, vao).toMinutes();

        if (soPhutTre > 5) {
            return "Đi trễ";
        }

        return "Đủ công";
    }

    private String xacDinhTrangThaiTheoLich(
            Time gioVao,
            Time gioRa,
            Time gioBatDauCa,
            Time gioKetThucCa
    ) {
        if (gioVao == null || gioBatDauCa == null || gioKetThucCa == null) {
            return "Đủ công";
        }

        long soPhutTre = (gioVao.getTime() - gioBatDauCa.getTime()) / (60 * 1000);

        if (soPhutTre > 5) {
            return "Đi trễ";
        }

        if (gioRa != null && gioRa.before(gioKetThucCa)) {
            return "Về sớm";
        }

        return "Đủ công";
    }

    private String xacDinhTrangThaiTuDong(String caLam, String gioVaoStr, String gioRaStr) {
        CaLamInfo caInfo = DANH_SACH_CA.get(caLam);

        if (caInfo == null) {
            return "Đủ công";
        }

        Time gioVao = Time.valueOf(gioVaoStr);
        Time gioRa = Time.valueOf(gioRaStr);
        Time gioBatDauCa = Time.valueOf(caInfo.gioVao);
        Time gioKetThucCa = Time.valueOf(caInfo.gioRa);

        return xacDinhTrangThaiTheoLich(gioVao, gioRa, gioBatDauCa, gioKetThucCa);
    }

    private java.sql.Date getTodaySqlDate() {
        Calendar cal = Calendar.getInstance();
        setStartOfDay(cal);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    private String formatTime(Time time) {
        if (time == null) {
            return "";
        }

        return time.toLocalTime().toString();
    }
    private boolean isTrongThoiGianCa(LocalTime hienTai, LocalTime gioBatDau, LocalTime gioKetThuc) {
        if (hienTai == null || gioBatDau == null || gioKetThuc == null) {
            return false;
        }

        // Ca bình thường trong cùng ngày, ví dụ 08:00 - 17:00
        if (!gioKetThuc.isBefore(gioBatDau)) {
            return !hienTai.isBefore(gioBatDau) && !hienTai.isAfter(gioKetThuc);
        }

        // Trường hợp ca qua đêm, ví dụ 22:00 - 06:00
        return !hienTai.isBefore(gioBatDau) || !hienTai.isAfter(gioKetThuc);
    }
}