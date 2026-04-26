package GUI;

import BUS.CaLamBUS;
import BUS.LichLamViecBUS;
import BUS.NhanVienBUS;
import DTO.CaLamDTO;
import DTO.LichLamViecDTO;
import DTO.NhanVienDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PnLichLamViec extends JPanel {

    private NhanVienBUS nhanVienBUS;
    private CaLamBUS caLamBUS;
    private LichLamViecBUS lichLamViecBUS;

    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;
    private JComboBox<CaLamDTO> cboCaLam;

    private JTable tblNhanVien;
    private DefaultTableModel modelNhanVien;

    private JTable table;
    private DefaultTableModel model;

    private JSpinner spnNgayBatDau;
    private JSpinner spnNgayKetThuc;

    private JCheckBox chkTuHomNay;
    private JTextField txtGhiChu;
    private JLabel lblNhanVienDaChon;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private int selectedMaLich = -1;
    private int selectedMaNV = -1;

    public PnLichLamViec() {
        nhanVienBUS = new NhanVienBUS();
        caLamBUS = new CaLamBUS();
        lichLamViecBUS = new LichLamViecBUS();

        initComponents();
        loadNhanVien();
        loadCaLam();
        loadData();
        addEvents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ LỊCH LÀM VIỆC");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlContent = new JPanel(new BorderLayout(10, 10));
        add(pnlContent, BorderLayout.CENTER);

        JPanel pnlLeft = new JPanel(new BorderLayout(8, 8));
        pnlLeft.setPreferredSize(new Dimension(460, 600));

        JPanel pnlNhanVien = new JPanel(new BorderLayout(5, 5));
        pnlNhanVien.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên - Tick để chọn"));

        String[] nvColumns = {"Chọn", "Mã NV", "Họ tên"};
        modelNhanVien = new DefaultTableModel(nvColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return Object.class;
            }
        };

        tblNhanVien = new JTable(modelNhanVien);
        tblNhanVien.setRowHeight(25);
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblNhanVien.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblNhanVien.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(60);
        tblNhanVien.getColumnModel().getColumn(2).setPreferredWidth(260);

        pnlNhanVien.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin xếp lịch"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblNhanVienDaChon = new JLabel("Chưa chọn nhân viên");
        lblNhanVienDaChon.setFont(new Font("Segoe UI", Font.BOLD, 13));

        java.util.Date today = getTodayUtilDate();

        spnNgayBatDau = new JSpinner(new SpinnerDateModel(today, today, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(spnNgayBatDau, "dd/MM/yyyy");
        spnNgayBatDau.setEditor(startEditor);

        spnNgayKetThuc = new JSpinner(new SpinnerDateModel(today, today, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(spnNgayKetThuc, "dd/MM/yyyy");
        spnNgayKetThuc.setEditor(endEditor);

        chkTuHomNay = new JCheckBox("Xếp từ hôm nay");
        chkTuHomNay.setSelected(true);

        cboCaLam = new JComboBox<>();
        cboCaLam.setPreferredSize(new Dimension(230, 28));
        cboCaLam.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                if (value instanceof CaLamDTO) {
                    CaLamDTO ca = (CaLamDTO) value;
                    value = ca.getTenCa() + " (" + ca.getGioBatDau() + " - " + ca.getGioKetThuc() + ")";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        txtGhiChu = new JTextField();

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        pnlForm.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(lblNhanVienDaChon, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        pnlForm.add(new JLabel("Tùy chọn:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(chkTuHomNay, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        pnlForm.add(new JLabel("Từ ngày:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(spnNgayBatDau, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        pnlForm.add(new JLabel("Đến ngày:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(spnNgayKetThuc, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        pnlForm.add(new JLabel("Ca làm:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(cboCaLam, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        pnlForm.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtGhiChu, gbc);

        row++;
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));

        btnThem = new JButton("Thêm lịch");
        btnSua = new JButton("Sửa lịch");
        btnXoa = new JButton("Xóa lịch");
        btnLamMoi = new JButton("Làm mới");

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        pnlForm.add(pnlButtons, gbc);

        pnlLeft.add(pnlNhanVien, BorderLayout.CENTER);
        pnlLeft.add(pnlForm, BorderLayout.SOUTH);

        pnlContent.add(pnlLeft, BorderLayout.WEST);

        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFilter.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem(i);
        }
        cboThang.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);

        cboNam = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            cboNam.addItem(i);
        }
        cboNam.setSelectedItem(currentYear);

        pnlFilter.add(new JLabel("Tháng:"));
        pnlFilter.add(cboThang);
        pnlFilter.add(new JLabel("Năm:"));
        pnlFilter.add(cboNam);

        pnlRight.add(pnlFilter, BorderLayout.NORTH);

        String[] columns = {
            "Mã lịch",
            "Mã NV",
            "Nhân viên",
            "Ngày làm",
            "Ca làm",
            "Giờ bắt đầu",
            "Giờ kết thúc",
            "Trạng thái",
            "Ghi chú"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setPreferredWidth(140);
        table.getColumnModel().getColumn(8).setPreferredWidth(160);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách lịch làm"));
        pnlRight.add(scrollPane, BorderLayout.CENTER);

        pnlContent.add(pnlRight, BorderLayout.CENTER);
    }

    private void addEvents() {
        cboThang.addActionListener(e -> loadData());
        cboNam.addActionListener(e -> loadData());

        btnThem.addActionListener(e -> themLichTheoKhoangNgay());
        btnSua.addActionListener(e -> suaLich());
        btnXoa.addActionListener(e -> xoaLich());
        btnLamMoi.addActionListener(e -> lamMoiForm());

        chkTuHomNay.addActionListener(e -> {
            if (chkTuHomNay.isSelected()) {
                java.util.Date today = getTodayUtilDate();
                spnNgayBatDau.setValue(today);
                spnNgayBatDau.setEnabled(false);
            } else {
                spnNgayBatDau.setEnabled(true);
            }
        });

        spnNgayBatDau.setEnabled(false);

        modelNhanVien.addTableModelListener(e -> capNhatNhanVienDaChon());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    fillFormFromTable(row);
                }
            }
        });
    }

    private void capNhatNhanVienDaChon() {
        ArrayList<Integer> list = getDanhSachMaNVSelected();

        if (list.isEmpty()) {
            selectedMaNV = -1;
            lblNhanVienDaChon.setText("Chưa chọn nhân viên");
        } else if (list.size() == 1) {
            selectedMaNV = list.get(0);

            for (int i = 0; i < modelNhanVien.getRowCount(); i++) {
                int maNV = Integer.parseInt(modelNhanVien.getValueAt(i, 1).toString());

                if (maNV == selectedMaNV) {
                    String hoTen = modelNhanVien.getValueAt(i, 2).toString();
                    lblNhanVienDaChon.setText(selectedMaNV + " - " + hoTen);
                    return;
                }
            }
        } else {
            selectedMaNV = -1;
            lblNhanVienDaChon.setText("Đã chọn " + list.size() + " nhân viên");
        }
    }

    private void loadNhanVien() {
        modelNhanVien.setRowCount(0);

        ArrayList<NhanVienDTO> list = nhanVienBUS.layDanhSachNhanVien();

        for (NhanVienDTO nv : list) {
            modelNhanVien.addRow(new Object[]{
                false,
                nv.getMaNV(),
                nv.getHo() + " " + nv.getTen()
            });
        }

        tblNhanVien.clearSelection();
        selectedMaNV = -1;
        lblNhanVienDaChon.setText("Chưa chọn nhân viên");
    }

    private void loadCaLam() {
        cboCaLam.removeAllItems();

        ArrayList<CaLamDTO> list = caLamBUS.layCaLamDangHoatDong();

        for (CaLamDTO ca : list) {
            cboCaLam.addItem(ca);
        }

        if (cboCaLam.getItemCount() > 0) {
            cboCaLam.setSelectedIndex(0);
        }
    }

    private void loadData() {
        if (cboThang.getSelectedItem() == null || cboNam.getSelectedItem() == null) {
            return;
        }

        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();

        ArrayList<LichLamViecDTO> list = lichLamViecBUS.layTatCaLichTheoThang(thang, nam);

        model.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (LichLamViecDTO lich : list) {
            model.addRow(new Object[]{
                lich.getMaLich(),
                lich.getMaNV(),
                lich.getHoTenNhanVien(),
                sdf.format(lich.getNgayLam()),
                lich.getTenCa(),
                lich.getGioBatDau() != null ? lich.getGioBatDau().toString() : "",
                lich.getGioKetThuc() != null ? lich.getGioKetThuc().toString() : "",
                getTrangThaiHienThi(lich),
                lich.getGhiChu() != null ? lich.getGhiChu() : ""
            });
        }
    }

    private void themLichTheoKhoangNgay() {
        ArrayList<Integer> danhSachMaNV = getDanhSachMaNVSelected();

        if (danhSachMaNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một nhân viên!");
            return;
        }

        CaLamDTO ca = (CaLamDTO) cboCaLam.getSelectedItem();

        if (ca == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ca làm!");
            return;
        }

        java.sql.Date ngayBatDau = getNgayBatDauSqlDate();
        java.sql.Date ngayKetThuc = getNgayKetThucSqlDate();

        if (ngayBatDau.before(getTodaySqlDate())) {
            JOptionPane.showMessageDialog(this, "Không thể tạo lịch cho ngày trong quá khứ!");
            return;
        }

        if (ngayKetThuc.before(ngayBatDau)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!");
            return;
        }

        StringBuilder danhSachTrung = new StringBuilder();
        int soLichTrung = 0;

        for (int maNV : danhSachMaNV) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ngayBatDau);

            Calendar end = Calendar.getInstance();
            end.setTime(ngayKetThuc);

            while (!cal.after(end)) {
                java.sql.Date ngay = new java.sql.Date(cal.getTimeInMillis());

                LichLamViecDTO old = lichLamViecBUS.layLichTheoNhanVienVaNgay(maNV, ngay);

                if (old != null) {
                    soLichTrung++;

                    if (soLichTrung <= 10) {
                        danhSachTrung
                                .append("- Mã NV ")
                                .append(maNV)
                                .append(" ngày ")
                                .append(formatDate(ngay))
                                .append(" đã có ca ")
                                .append(old.getTenCa())
                                .append("\n");
                    }
                }

                cal.add(Calendar.DATE, 1);
            }
        }

        if (soLichTrung > 0) {
            if (soLichTrung > 10) {
                danhSachTrung
                        .append("... và còn ")
                        .append(soLichTrung - 10)
                        .append(" lịch trùng khác.\n");
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Không thể thêm lịch vì bị trùng.\n"
                            + "Mỗi nhân viên chỉ được 1 ca trong 1 ngày.\n\n"
                            + danhSachTrung
                            + "\nVui lòng chọn ngày khác hoặc sửa lịch cũ.",
                    "Trùng lịch làm",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int tongCanThem = danhSachMaNV.size() * demSoNgay(ngayBatDau, ngayKetThuc);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn thêm " + tongCanThem + " lịch làm không?",
                "Xác nhận thêm lịch",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int thanhCong = 0;
        int thatBai = 0;
        String ghiChu = txtGhiChu.getText().trim();

        for (int maNV : danhSachMaNV) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ngayBatDau);

            Calendar end = Calendar.getInstance();
            end.setTime(ngayKetThuc);

            while (!cal.after(end)) {
                java.sql.Date ngay = new java.sql.Date(cal.getTimeInMillis());

                LichLamViecDTO lich = new LichLamViecDTO();
                lich.setMaNV(maNV);
                lich.setMaCa(ca.getMaCa());
                lich.setNgayLam(ngay);
                lich.setGhiChu(ghiChu);
                lich.setTrangThai(true);

                String message = lichLamViecBUS.themLichLamViec(lich);

                if (message.contains("thành công")) {
                    thanhCong++;
                } else {
                    thatBai++;
                }

                cal.add(Calendar.DATE, 1);
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Kết quả thêm lịch:\n"
                        + "- Thành công: " + thanhCong + " lịch\n"
                        + "- Thất bại: " + thatBai + " lịch",
                "Kết quả",
                JOptionPane.INFORMATION_MESSAGE
        );

        loadData();
    }

    private void suaLich() {
        if (selectedMaLich <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch làm cần sửa!");
            return;
        }

        if (selectedMaNV <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
            return;
        }

        CaLamDTO ca = (CaLamDTO) cboCaLam.getSelectedItem();

        if (ca == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ca làm!");
            return;
        }

        java.sql.Date ngayLam = getNgayBatDauSqlDate();

        if (ngayLam.before(getTodaySqlDate())) {
            JOptionPane.showMessageDialog(this, "Không thể sửa lịch làm sang ngày trong quá khứ!");
            return;
        }

        LichLamViecDTO lich = new LichLamViecDTO();
        lich.setMaLich(selectedMaLich);
        lich.setMaNV(selectedMaNV);
        lich.setMaCa(ca.getMaCa());
        lich.setNgayLam(ngayLam);
        lich.setGhiChu(txtGhiChu.getText().trim());
        lich.setTrangThai(true);

        String message = lichLamViecBUS.suaLichLamViec(lich);
        JOptionPane.showMessageDialog(this, message);

        if (message.contains("thành công")) {
            loadData();
            lamMoiForm();
        }
    }

    private void xoaLich() {
        if (selectedMaLich <= 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch làm cần xóa!");
            return;
        }

        int row = table.getSelectedRow();

        if (row != -1) {
            try {
                String ngayLamStr = model.getValueAt(row, 3).toString();
                java.util.Date ngayLam = new SimpleDateFormat("dd/MM/yyyy").parse(ngayLamStr);
                java.sql.Date ngaySql = new java.sql.Date(ngayLam.getTime());

                if (ngaySql.before(getTodaySqlDate())) {
                    JOptionPane.showMessageDialog(this, "Không thể xóa lịch làm trong quá khứ!");
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Không thể kiểm tra ngày làm!");
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa lịch làm này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String message = lichLamViecBUS.xoaLichLamViec(selectedMaLich);
        JOptionPane.showMessageDialog(this, message);

        if (message.contains("thành công")) {
            loadData();
            lamMoiForm();
        }
    }

    private void fillFormFromTable(int row) {
        try {
            selectedMaLich = Integer.parseInt(model.getValueAt(row, 0).toString());
            int maNV = Integer.parseInt(model.getValueAt(row, 1).toString());
            String hoTen = model.getValueAt(row, 2).toString();
            String ngayLamStr = model.getValueAt(row, 3).toString();
            String tenCa = model.getValueAt(row, 4).toString();
            String ghiChu = model.getValueAt(row, 8) != null ? model.getValueAt(row, 8).toString() : "";

            selectedMaNV = maNV;
            lblNhanVienDaChon.setText(maNV + " - " + hoTen);
            selectNhanVienByMa(maNV);
            selectCaLamByTen(tenCa);

            java.util.Date ngayLam = new SimpleDateFormat("dd/MM/yyyy").parse(ngayLamStr);
            spnNgayBatDau.setValue(ngayLam);
            spnNgayKetThuc.setValue(ngayLam);

            chkTuHomNay.setSelected(false);
            spnNgayBatDau.setEnabled(true);

            txtGhiChu.setText(ghiChu);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu lịch làm đã chọn!");
        }
    }

    private void selectNhanVienByMa(int maNV) {
        for (int i = 0; i < modelNhanVien.getRowCount(); i++) {
            int currentMaNV = Integer.parseInt(modelNhanVien.getValueAt(i, 1).toString());

            if (currentMaNV == maNV) {
                modelNhanVien.setValueAt(true, i, 0);
                tblNhanVien.setRowSelectionInterval(i, i);
            } else {
                modelNhanVien.setValueAt(false, i, 0);
            }
        }

        capNhatNhanVienDaChon();
    }

    private void selectCaLamByTen(String tenCa) {
        for (int i = 0; i < cboCaLam.getItemCount(); i++) {
            CaLamDTO ca = cboCaLam.getItemAt(i);

            if (ca != null && ca.getTenCa().equals(tenCa)) {
                cboCaLam.setSelectedIndex(i);
                return;
            }
        }
    }

    private void lamMoiForm() {
        selectedMaLich = -1;

        for (int i = 0; i < modelNhanVien.getRowCount(); i++) {
            modelNhanVien.setValueAt(false, i, 0);
        }

        tblNhanVien.clearSelection();
        selectedMaNV = -1;
        lblNhanVienDaChon.setText("Chưa chọn nhân viên");

        if (cboCaLam.getItemCount() > 0) {
            cboCaLam.setSelectedIndex(0);
        }

        java.util.Date today = getTodayUtilDate();

        chkTuHomNay.setSelected(true);
        spnNgayBatDau.setValue(today);
        spnNgayBatDau.setEnabled(false);
        spnNgayKetThuc.setValue(today);

        txtGhiChu.setText("");
        table.clearSelection();
    }

    private java.sql.Date getNgayBatDauSqlDate() {
        java.util.Date selectedDate = (java.util.Date) spnNgayBatDau.getValue();
        return new java.sql.Date(selectedDate.getTime());
    }

    private java.sql.Date getNgayKetThucSqlDate() {
        java.util.Date selectedDate = (java.util.Date) spnNgayKetThuc.getValue();
        return new java.sql.Date(selectedDate.getTime());
    }

    private String getTrangThaiHienThi(LichLamViecDTO lich) {
        if (!lich.isTrangThai()) {
            return "Đã hủy";
        }

        if (lich.getTrangThaiChamCong() != null && !lich.getTrangThaiChamCong().trim().isEmpty()) {
            return lich.getTrangThaiChamCong();
        }

        java.sql.Date today = getTodaySqlDate();

        if (lich.getNgayLam().after(today)) {
            return "Sắp tới";
        }

        if (lich.getNgayLam().equals(today)) {
            return "Hôm nay - chưa chấm công";
        }

        return "Chưa chấm công";
    }

    private java.sql.Date getTodaySqlDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new java.sql.Date(cal.getTimeInMillis());
    }

    private java.util.Date getTodayUtilDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    private String formatDate(java.sql.Date date) {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    private ArrayList<Integer> getDanhSachMaNVSelected() {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < modelNhanVien.getRowCount(); i++) {
            Object checkedObj = modelNhanVien.getValueAt(i, 0);
            boolean checked = checkedObj instanceof Boolean && (Boolean) checkedObj;

            if (checked) {
                int maNV = Integer.parseInt(modelNhanVien.getValueAt(i, 1).toString());
                list.add(maNV);
            }
        }

        return list;
    }

    private int demSoNgay(java.sql.Date ngayBatDau, java.sql.Date ngayKetThuc) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(ngayBatDau);

        Calendar end = Calendar.getInstance();
        end.setTime(ngayKetThuc);

        int count = 0;

        while (!cal.after(end)) {
            count++;
            cal.add(Calendar.DATE, 1);
        }

        return count;
    }
}