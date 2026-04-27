package GUI;

import BUS.CTPhieuNhapBUS;
import BUS.PhieuNhapBUS;
import BUS.SanPhamBUS;
import DAO.NhaCungCapDAO;
import DTO.CTPhieuNhapDTO;
import DTO.NhaCungCapDTO;
import DTO.PhanQuyen;
import DTO.PhieuNhapDTO;
import DTO.SanPhamDTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PnCTPhieuNhap extends JDialog {

    private PhanQuyen phanQuyen;
    private final int maPN;
    private int trangThaiPN;
    private String tenNguoiDung;

    // Components bên phải - thông tin phiếu nhập
    private JTextField txtMaPN;
    private JLabel lblTenNCC;
    private JLabel lblNgayLap;
    private JLabel lblNguoiLap;
    private JLabel lblTongTien;

    // Components nhập chi tiết sản phẩm (bên trái)
    private JComboBox<String> cboSanPham;
    private Map<String, Integer> mapTenSP_Id;
    private JTextField txtSoLuong;
    private JTextField txtGiaNhap;
    private JTextField txtThanhTien;

    private DefaultTableModel model;
    private JTable table;

    private JButton btnLuu;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnXuat;
    private JButton btnDong;
    private JButton btnLamMoi;

    private final CTPhieuNhapBUS ctBus = new CTPhieuNhapBUS();
    private final PhieuNhapBUS pnBus = new PhieuNhapBUS();
    private final SanPhamBUS spBus = new SanPhamBUS();
    private final NhaCungCapDAO nccDao = new NhaCungCapDAO();

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public PnCTPhieuNhap(Frame owner, int maPN, int trangThai, PhanQuyen pq, String tenNV) {
        super(owner, "Chi tiết Phiếu Nhập" + maPN, true);
        this.maPN = maPN;
        this.trangThaiPN = trangThai;
        this.phanQuyen = pq;
        this.tenNguoiDung = tenNV;

        initComponents();
        if (lblNguoiLap != null) {
            lblNguoiLap.setText(tenNV);
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.9);
        int height = (int) (screenSize.height * 0.9);
        this.setSize(width, height);

        // Căn giữa Dialog trên màn hình
        this.setLocationRelativeTo(null);
        loadSanPhamToComboBox();
        loadThongTinPhieuNhap();
        reloadTable();
        setLocationRelativeTo(owner);
        configureByPermission();
        setTrangThaiGiaoDien(trangThaiPN == 1);
    }

    private void initComponents() {

        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(8, 8));

        // ===== PANEL TRÁI: NHẬP CHI TIẾT SẢN PHẨM =====
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(400, 0));

        JPanel pnlNhapChiTiet = new JPanel(new GridBagLayout());
        pnlNhapChiTiet.setBackground(new Color(248, 248, 255));
        pnlNhapChiTiet.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "NHẬP CHI TIẾT SẢN PHẨM",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 12, 8, 12);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;

        Font lblFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel lblSP = new JLabel("Sản phẩm:");
        JLabel lblSL = new JLabel("Số lượng:");
        JLabel lblGia = new JLabel("Giá nhập:");
        JLabel lblThanhTien = new JLabel("Thành tiền:");

        lblSP.setFont(lblFont);
        lblSL.setFont(lblFont);
        lblGia.setFont(lblFont);
        lblThanhTien.setFont(lblFont);

        cboSanPham = new JComboBox<>();
        cboSanPham.setFont(lblFont);
        cboSanPham.setEditable(false);

        txtSoLuong = new JTextField();
        txtSoLuong.setFont(lblFont);

        txtGiaNhap = new JTextField();
        txtGiaNhap.setFont(lblFont);

        txtThanhTien = new JTextField();
        txtThanhTien.setEditable(false);
        txtThanhTien.setFont(lblFont);
        txtThanhTien.setBackground(new Color(240, 240, 240));

        int r = 0;
        gc.gridx = 0;
        gc.gridy = r;
        pnlNhapChiTiet.add(lblSP, gc);
        gc.gridx = 1;
        pnlNhapChiTiet.add(cboSanPham, gc);
        r++;

        gc.gridx = 0;
        gc.gridy = r;
        pnlNhapChiTiet.add(lblSL, gc);
        gc.gridx = 1;
        pnlNhapChiTiet.add(txtSoLuong, gc);
        r++;

        gc.gridx = 0;
        gc.gridy = r;
        pnlNhapChiTiet.add(lblGia, gc);
        gc.gridx = 1;
        pnlNhapChiTiet.add(txtGiaNhap, gc);
        r++;

        gc.gridx = 0;
        gc.gridy = r;
        pnlNhapChiTiet.add(lblThanhTien, gc);
        gc.gridx = 1;
        pnlNhapChiTiet.add(txtThanhTien, gc);
        r++;

        btnLuu = createClassicButton("Thêm mới", "/icon/add.png");
        JPanel pnlBtnAdd = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtnAdd.setOpaque(false);
        pnlBtnAdd.add(btnLuu);

        gc.gridx = 0;
        gc.gridy = r;
        gc.gridwidth = 2;
        pnlNhapChiTiet.add(pnlBtnAdd, gc);

        leftPanel.add(pnlNhapChiTiet, BorderLayout.CENTER);

        // ===== PANEL PHẢI: THÔNG TIN PHIẾU + DANH SÁCH SẢN PHẨM =====
        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBackground(Color.WHITE);

        // 1. Thông tin phiếu nhập (phía trên bên phải)
        JPanel pnlThongTin = new JPanel(new GridBagLayout());
        pnlThongTin.setBackground(new Color(240, 248, 255));
        pnlThongTin.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "THÔNG TIN PHIẾU NHẬP",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 12, 8, 12);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lblMaPN_text = new JLabel("Mã phiếu nhập:");
        JLabel lblNCC_text = new JLabel("Nhà cung cấp:");
        JLabel lblNgayLap_text = new JLabel("Ngày lập:");
        JLabel lblNguoiLap_text = new JLabel("Người lập:");

        lblMaPN_text.setFont(infoFont);
        lblNCC_text.setFont(infoFont);
        lblNgayLap_text.setFont(infoFont);
        lblNguoiLap_text.setFont(infoFont);

        txtMaPN = new JTextField();
        txtMaPN.setEditable(false);
        txtMaPN.setFont(infoFont);
        txtMaPN.setBackground(Color.WHITE);

        lblTenNCC = new JLabel();
        lblTenNCC.setFont(infoFont);
        lblTenNCC.setForeground(new Color(0, 100, 0));

        lblNgayLap = new JLabel();
        lblNgayLap.setFont(infoFont);

        lblNguoiLap = new JLabel();
        lblNguoiLap.setFont(infoFont);
        lblNguoiLap.setForeground(new Color(0, 0, 150));

        int row = 0;
        g.gridx = 0;
        g.gridy = row;
        g.weightx = 0.3;
        pnlThongTin.add(lblMaPN_text, g);
        g.gridx = 1;
        g.weightx = 0.7;
        pnlThongTin.add(txtMaPN, g);
        row++;

        g.gridx = 0;
        g.gridy = row;
        pnlThongTin.add(lblNCC_text, g);
        g.gridx = 1;
        pnlThongTin.add(lblTenNCC, g);
        row++;

        g.gridx = 0;
        g.gridy = row;
        pnlThongTin.add(lblNgayLap_text, g);
        g.gridx = 1;
        pnlThongTin.add(lblNgayLap, g);
        row++;

        g.gridx = 0;
        g.gridy = row;
        pnlThongTin.add(lblNguoiLap_text, g);
        g.gridx = 1;
        pnlThongTin.add(lblNguoiLap, g);
        row++;

        // 2. Danh sách chi tiết sản phẩm
        JPanel pnlTableContainer = new JPanel(new BorderLayout());
        pnlTableContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "DANH SÁCH CHI TIẾT PHIẾU NHẬP",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        String[] cols = {"STT", "Mã SP", "Tên sản phẩm", "Số lượng", "Giá nhập", "Thành tiền"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Ẩn cột Mã SP (index 1)
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);

        pnlTableContainer.add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Panel chứa tổng tiền và các nút
        JPanel pnlBottomContainer = new JPanel(new BorderLayout());
        pnlBottomContainer.setBackground(Color.WHITE);

        // 3a. Tổng tiền
        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTongTien.setBackground(new Color(255, 255, 200));
        pnlTongTien.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setForeground(new Color(220, 20, 60));
        pnlTongTien.add(lblTongTien);

        // 3b. Các nút chức năng
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btnSua = createClassicButton("Sửa", "/icon/sua.png");
        btnXoa = createClassicButton("Xóa", "/icon/xoa.png");
        btnLamMoi = createClassicButton("Làm mới", "/icon/undo.png");
        btnXuat = createClassicButton("Xuất phiếu", "/icon/export.png");
        btnDong = createClassicButton("Đóng", "/icon/close.png");

        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnXuat);
        pnlButtons.add(btnDong);

        pnlBottomContainer.add(pnlTongTien, BorderLayout.NORTH);
        pnlBottomContainer.add(pnlButtons, BorderLayout.CENTER);

        rightPanel.add(pnlThongTin, BorderLayout.NORTH);
        rightPanel.add(pnlTableContainer, BorderLayout.CENTER);
        rightPanel.add(pnlBottomContainer, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setResizeWeight(0.3);
        split.setDividerLocation(400);
        add(split, BorderLayout.CENTER);

        // Sự kiện tính thành tiền
        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tinhThanhTien();
            }
        });

        txtGiaNhap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tinhThanhTien();
            }
        });

        // Click vào bảng để load lên form sửa
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    String tenSP = (String) model.getValueAt(row, 2);
                    int soLuong = (int) model.getValueAt(row, 3);
                    int giaNhap = (int) model.getValueAt(row, 4);

                    for (int i = 0; i < cboSanPham.getItemCount(); i++) {
                        if (cboSanPham.getItemAt(i).equals(tenSP)) {
                            cboSanPham.setSelectedIndex(i);
                            break;
                        }
                    }
                    txtSoLuong.setText(String.valueOf(soLuong));
                    txtGiaNhap.setText(String.valueOf(giaNhap));
                    tinhThanhTien();
                }
            }
        });

        // Xử lý sự kiện các nút
        btnLuu.addActionListener(e -> themMoiChiTiet());
        btnSua.addActionListener(e -> suaChiTiet());
        btnXoa.addActionListener(e -> xoaChiTiet());
        btnLamMoi.addActionListener(e -> clearInput());
        btnXuat.addActionListener(e -> finalizePhieuNhap());
        btnDong.addActionListener(e -> dispose());
    }

    private void loadSanPhamToComboBox() {
        cboSanPham.removeAllItems();
        mapTenSP_Id = new HashMap<>();

        try {
            List<SanPhamDTO> dsSP = spBus.getSanPham();
            if (dsSP != null && !dsSP.isEmpty()) {
                for (SanPhamDTO sp : dsSP) {
                    cboSanPham.addItem(sp.getTenSP());
                    mapTenSP_Id.put(sp.getTenSP(), sp.getID());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách sản phẩm: " + ex.getMessage());
        }
    }

    private void loadThongTinPhieuNhap() {
        PhieuNhapDTO pn = pnBus.getById(maPN);
        if (pn != null) {
            txtMaPN.setText(pnBus.toDisplayCode(maPN));

            NhaCungCapDTO ncc = nccDao.getById(pn.getMaNCC());
            if (ncc != null) {
                lblTenNCC.setText(ncc.getTenNCC());
            } else {
                lblTenNCC.setText("Không xác định");
            }

            if (pn.getNgayLap() != null) {
                lblNgayLap.setText(sdf.format(pn.getNgayLap()));
            } else {
                lblNgayLap.setText("Chưa có");
            }

            if (tenNguoiDung != null && !tenNguoiDung.isEmpty()) {
                lblNguoiLap.setText(tenNguoiDung);
            } else {
                lblNguoiLap.setText("Chưa xác định");
            }
        }
    }

    private void tinhThanhTien() {
        try {
            String slStr = txtSoLuong.getText().trim();
            String giaStr = txtGiaNhap.getText().trim();

            if (!slStr.isEmpty() && !giaStr.isEmpty()) {
                int soLuong = Integer.parseInt(slStr);
                int giaNhap = Integer.parseInt(giaStr);
                if (soLuong > 0 && giaNhap > 0) {
                    long thanhTien = (long) soLuong * giaNhap;
                    txtThanhTien.setText(String.format("%,d", thanhTien));
                } else {
                    txtThanhTien.setText("");
                }
            } else {
                txtThanhTien.setText("");
            }
        } catch (NumberFormatException ex) {
            txtThanhTien.setText("");
        }
    }

    private void themMoiChiTiet() {
        if (trangThaiPN == 1) {
            JOptionPane.showMessageDialog(this, "Phiếu đã được xuất, không thể thêm chi tiết mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String tenSP = (String) cboSanPham.getSelectedItem();
            if (tenSP == null || !mapTenSP_Id.containsKey(tenSP)) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int maSP = mapTenSP_Id.get(tenSP);

            String soLuongStr = txtSoLuong.getText().trim();
            if (soLuongStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocus();
                return;
            }

            int soLuong = Integer.parseInt(soLuongStr);
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocus();
                return;
            }

            String giaStr = txtGiaNhap.getText().trim();
            if (giaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập giá nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtGiaNhap.requestFocus();
                return;
            }

            int giaNhap = Integer.parseInt(giaStr);
            if (giaNhap <= 0) {
                JOptionPane.showMessageDialog(this, "Giá nhập phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtGiaNhap.requestFocus();
                return;
            }

            // Kiểm tra sản phẩm đã tồn tại - NẾU CÓ THÌ CỘNG DỒN
            boolean existed = false;
            int existingRow = -1;
            for (int i = 0; i < model.getRowCount(); i++) {
                int existingMaSP = (int) model.getValueAt(i, 1);
                if (existingMaSP == maSP) {
                    existed = true;
                    existingRow = i;
                    break;
                }
            }

            if (existed) {
                int currentSoLuong = (int) model.getValueAt(existingRow, 3);
                int newSoLuong = currentSoLuong + soLuong;

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Sản phẩm '" + tenSP + "' đã có trong phiếu.\n"
                        + "Số lượng hiện tại: " + currentSoLuong + "\n"
                        + "Số lượng thêm: " + soLuong + "\n"
                        + "Bạn có muốn cập nhật số lượng mới: " + newSoLuong + " không?",
                        "Xác nhận cập nhật", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean ok = ctBus.updateDetail(maPN, maSP, newSoLuong, giaNhap);
                    if (ok) {
                        reloadTable();
                        clearInput();
                        JOptionPane.showMessageDialog(this, "Đã cập nhật số lượng sản phẩm!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                boolean ok = ctBus.addDetail(maPN, maSP, soLuong, giaNhap);
                if (!ok) {
                    JOptionPane.showMessageDialog(this,
                            "Thêm chi tiết thất bại!\nVui lòng kiểm tra lại thông tin sản phẩm.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                reloadTable();
                clearInput();
                JOptionPane.showMessageDialog(this, "Đã thêm chi tiết sản phẩm thành công!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaChiTiet() {
        if (trangThaiPN == 1) {
            JOptionPane.showMessageDialog(this, "Phiếu đã được xuất, không thể sửa chi tiết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết cần sửa trong bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String tenSP = (String) cboSanPham.getSelectedItem();
            if (tenSP == null || !mapTenSP_Id.containsKey(tenSP)) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int maSP = mapTenSP_Id.get(tenSP);
            int maSPCu = (int) model.getValueAt(row, 1);

            if (maSP != maSPCu) {
                // Kiểm tra sản phẩm mới có bị trùng không
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (i != row && (int) model.getValueAt(i, 1) == maSP) {
                        JOptionPane.showMessageDialog(this,
                                "Sản phẩm '" + tenSP + "' đã tồn tại trong phiếu!",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int giaNhap = Integer.parseInt(txtGiaNhap.getText().trim());
            if (giaNhap <= 0) {
                JOptionPane.showMessageDialog(this, "Giá nhập phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok;
            if (maSP == maSPCu) {
                ok = ctBus.updateDetail(maPN, maSP, soLuong, giaNhap);
            } else {
                // Xóa cũ thêm mới
                ok = ctBus.deleteDetail(maPN, maSPCu);
                if (ok) {
                    ok = ctBus.addDetail(maPN, maSP, soLuong, giaNhap);
                }
            }

            if (ok) {
                reloadTable();
                clearInput();
                JOptionPane.showMessageDialog(this, "Đã cập nhật chi tiết thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaChiTiet() {
        if (trangThaiPN == 1) {
            JOptionPane.showMessageDialog(this, "Phiếu đã được xuất, không thể xóa chi tiết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết cần xóa trong bảng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maSP = (int) model.getValueAt(row, 1);
        String tenSP = (String) model.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa sản phẩm '" + tenSP + "' khỏi phiếu nhập?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean ok = ctBus.deleteDetail(maPN, maSP);
                if (ok) {
                    reloadTable();
                    clearInput();
                    JOptionPane.showMessageDialog(this, "Đã xóa chi tiết thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void finalizePhieuNhap() {
        if (trangThaiPN == 1) {
            JOptionPane.showMessageDialog(this, "Phiếu này đã được xuất trước đó!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Sau khi xuất, bạn sẽ KHÔNG THỂ sửa đổi (thêm/sửa/xóa) bất kỳ chi tiết nào của phiếu.\n"
                + "Bạn có chắc chắn muốn xuất phiếu nhập này không?",
                "Xác nhận xuất phiếu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean okTongTien = pnBus.updateTongTienFromDetail(maPN);
            if (!okTongTien) {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật tổng tiền phiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean okStatus = pnBus.updateTrangThai(maPN, 1);
            if (!okStatus) {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật trạng thái phiếu!\nVui lòng kiểm tra lại database.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Xuất phiếu nhập thành công!");

            this.trangThaiPN = 1;
            setTrangThaiGiaoDien(true);
            setTitle("Chi tiết Phiếu Nhập - Mã: " + pnBus.toDisplayCode(maPN) + " (ĐÃ XUẤT)");

            int saveOpt = JOptionPane.showConfirmDialog(this,
                    "Bạn có muốn lưu phiếu nhập này dưới dạng hình ảnh không?",
                    "Lưu phiếu", JOptionPane.YES_NO_OPTION);

            if (saveOpt == JOptionPane.YES_OPTION) {
                captureAndSaveReceipt();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất phiếu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reloadTable() {
        List<CTPhieuNhapDTO> ds = ctBus.getByMaPN(maPN);
        model.setRowCount(0);

        double tong = 0;
        int stt = 1;
        for (CTPhieuNhapDTO ct : ds) {
            String tenSP = "";
            try {
                List<SanPhamDTO> dsSP = spBus.getSanPham();
                if (dsSP != null) {
                    for (SanPhamDTO sp : dsSP) {
                        if (sp.getID() == ct.getId()) {
                            tenSP = sp.getTenSP();
                            break;
                        }
                    }
                }
                if (tenSP.isEmpty()) {
                    tenSP = "Không xác định";
                }
            } catch (Exception e) {
                tenSP = "Lỗi tải tên";
            }

            model.addRow(new Object[]{
                stt++,
                ct.getId(),
                tenSP,
                ct.getSoLuong(),
                ct.getGiaNhap(),
                ct.getThanhTien()
            });
            tong += ct.getThanhTien();
        }
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VNĐ", tong));
        pnBus.updateTongTienFromDetail(maPN);
    }

    private void clearInput() {
        if (cboSanPham.getItemCount() > 0) {
            cboSanPham.setSelectedIndex(0);
        }
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtThanhTien.setText("");
        table.clearSelection();
    }

    private void configureByPermission() {
        boolean coQuyen = phanQuyen.isKhoNhapHang();
        btnLuu.setEnabled(coQuyen);
        btnSua.setEnabled(coQuyen);
        btnXoa.setEnabled(coQuyen);
        btnXuat.setEnabled(coQuyen);
    }

    private void setTrangThaiGiaoDien(boolean isFinalized) {
        cboSanPham.setEnabled(!isFinalized);
        txtSoLuong.setEditable(!isFinalized);
        txtGiaNhap.setEditable(!isFinalized);

        btnLuu.setEnabled(!isFinalized && phanQuyen.isKhoNhapHang());
        btnSua.setEnabled(!isFinalized && phanQuyen.isKhoNhapHang());
        btnXoa.setEnabled(!isFinalized && phanQuyen.isKhoNhapHang());

        if (isFinalized) {
            btnXuat.setText("Đã xuất");
            btnXuat.setEnabled(false);
        } else {
            btnXuat.setText("Xuất phiếu");
            btnXuat.setEnabled(phanQuyen.isKhoNhapHang());
        }
    }

    private JButton createClassicButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setBackground(UIManager.getColor("Button.background"));
        btn.setBorder(BorderFactory.createLineBorder(new Color(160, 180, 200)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ImageIcon icon = loadScaledIcon(iconPath, 18, 18);
        if (icon != null) {
            btn.setIcon(icon);
        }
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(8);
        return btn;
    }

    private ImageIcon loadScaledIcon(String path, int w, int h) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                return null;
            }
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ex) {
            return null;
        }
    }

    private JPanel createPrintPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Tiêu đề
        JLabel title = new JLabel("PHIẾU NHẬP HÀNG", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // 2. Thông tin chung (Mã phiếu, NCC, Ngày, Người lập)
        JPanel info = new JPanel(new GridLayout(2, 2, 10, 10));
        info.setBackground(Color.WHITE);
        info.add(new JLabel("Mã phiếu: " + txtMaPN.getText()));
        info.add(new JLabel("Nhà cung cấp: " + lblTenNCC.getText()));
        info.add(new JLabel("Ngày lập: " + lblNgayLap.getText()));
        info.add(new JLabel("Người lập: " + this.tenNguoiDung)); // Sử dụng biến tenNguoiDung đã lấy
        panel.add(info);
        panel.add(Box.createVerticalStrut(20));

        // 3. Bảng sản phẩm (Lấy model từ table hiện tại)
        JTable printTable = new JTable(table.getModel());
        printTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        printTable.setRowHeight(25);

        // Đảm bảo bảng hiển thị hết số dòng trong ảnh
        JScrollPane scroll = new JScrollPane(printTable);
        scroll.setPreferredSize(new Dimension(600, table.getRowCount() * 25 + 30));
        panel.add(scroll);
        panel.add(Box.createVerticalStrut(10));

        // 4. Tổng tiền
        JLabel total = new JLabel("Tổng tiền: " + lblTongTien.getText(), SwingConstants.RIGHT);
        total.setFont(new Font("Segoe UI", Font.BOLD, 16));
        total.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(total);
        panel.add(Box.createVerticalStrut(30));

        // 5. Chữ ký
        JPanel footer = new JPanel(new GridLayout(1, 3));
        footer.setBackground(Color.WHITE);
        footer.add(new JLabel("Người giao", SwingConstants.CENTER));
        footer.add(new JLabel("Thủ kho", SwingConstants.CENTER));
        footer.add(new JLabel("Người lập phiếu", SwingConstants.CENTER));
        panel.add(footer);

        return panel;
    }

    private void captureAndSaveReceipt() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu phiếu nhập");
        chooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getAbsolutePath() + ".png");
            }

            // Tạo Panel ảo để in
            JPanel printView = createPrintPanel();

            // Cần set size cho panel ảo trước khi chụp (chiều cao linh hoạt theo số dòng)
            int height = 450 + (table.getRowCount() * 25);
            printView.setSize(new Dimension(700, height));

            // Gọi hàm doCapture đã có sẵn trong code của bạn
            if (doCapture(printView, file)) {
                JOptionPane.showMessageDialog(this, "Đã lưu phiếu nhập thành công!");
            }
        }
    }

    private JPanel createReceiptPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("PHIẾU NHẬP HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 8));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "THÔNG TIN PHIẾU NHẬP",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lblMaPN_text = new JLabel("Mã phiếu nhập:");
        lblMaPN_text.setFont(infoFont);
        JLabel lblMaPNValue = new JLabel(txtMaPN.getText());
        lblMaPNValue.setFont(infoFont);
        lblMaPNValue.setForeground(new Color(0, 100, 0));

        JLabel lblNCC_text = new JLabel("Nhà cung cấp:");
        lblNCC_text.setFont(infoFont);
        JLabel lblNCCValue = new JLabel(lblTenNCC.getText());
        lblNCCValue.setFont(infoFont);

        JLabel lblNgay_text = new JLabel("Ngày lập:");
        lblNgay_text.setFont(infoFont);
        JLabel lblNgayValue = new JLabel(lblNgayLap.getText());
        lblNgayValue.setFont(infoFont);

        JLabel lblNguoi_text = new JLabel("Người lập:");
        lblNguoi_text.setFont(infoFont);
        JLabel lblNguoiValue = new JLabel(lblNguoiLap.getText());
        lblNguoiValue.setFont(infoFont);
        lblNguoiValue.setForeground(new Color(0, 0, 150));

        infoPanel.add(lblMaPN_text);
        infoPanel.add(lblMaPNValue);
        infoPanel.add(lblNCC_text);
        infoPanel.add(lblNCCValue);
        infoPanel.add(lblNgay_text);
        infoPanel.add(lblNgayValue);
        infoPanel.add(lblNguoi_text);
        infoPanel.add(lblNguoiValue);

        panel.add(infoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "DANH SÁCH SẢN PHẨM NHẬP",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        String[] cols = {"STT", "Tên sản phẩm", "Số lượng", "Giá nhập", "Thành tiền"};
        DefaultTableModel tempModel = new DefaultTableModel(cols, 0);
        JTable tempTable = new JTable(tempModel);
        tempTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tempTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tempTable.setRowHeight(25);

        for (int i = 0; i < model.getRowCount(); i++) {
            Object[] row = new Object[5];
            row[0] = model.getValueAt(i, 0);
            row[1] = model.getValueAt(i, 2);
            row[2] = model.getValueAt(i, 3);
            row[3] = String.format("%,d", (int) model.getValueAt(i, 4)) + " đ";
            row[4] = String.format("%,d", (int) model.getValueAt(i, 5)) + " đ";
            tempModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(tempTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        JLabel lblTotal = new JLabel(lblTongTien.getText());
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(new Color(220, 20, 60));
        totalPanel.add(lblTotal);
        panel.add(totalPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel footerPanel = new JPanel(new GridLayout(2, 2, 50, 20));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblNguoiGiao = new JLabel("Người giao hàng", SwingConstants.CENTER);
        lblNguoiGiao.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        JLabel lblNguoiNhan = new JLabel("Người nhận hàng", SwingConstants.CENTER);
        lblNguoiNhan.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        JLabel lblKeToan = new JLabel("Kế toán", SwingConstants.CENTER);
        lblKeToan.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        JLabel lblThuKho = new JLabel("Thủ kho", SwingConstants.CENTER);
        lblThuKho.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        footerPanel.add(lblNguoiGiao);
        footerPanel.add(lblNguoiNhan);
        footerPanel.add(lblKeToan);
        footerPanel.add(lblThuKho);

        panel.add(footerPanel);

        JLabel lblPrintDate = new JLabel("Ngày xuất: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), SwingConstants.CENTER);
        lblPrintDate.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblPrintDate.setForeground(Color.GRAY);
        lblPrintDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblPrintDate);

        return panel;
    }

    private boolean doCapture(JComponent component, File file) {
        try {
            component.setSize(component.getPreferredSize());
            component.validate();
            component.doLayout();

            BufferedImage img = new BufferedImage(
                    component.getWidth(),
                    component.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g2d = img.createGraphics();
            component.printAll(g2d);
            g2d.dispose();

            ImageIO.write(img, "png", file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
