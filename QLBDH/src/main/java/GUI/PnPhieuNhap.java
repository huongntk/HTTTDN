
package GUI;

import BUS.PhieuNhapBUS;
import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;
import DTO.PhanQuyen;
import DTO.PhieuNhapDTO;
import BUS.CTPhieuNhapBUS;
import DTO.SanPhamDTO;
import BUS.SanPhamBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class PnPhieuNhap extends JPanel {
    private PhieuNhapBUS bus = new PhieuNhapBUS();
    private NhaCungCapDAO nccDao = new NhaCungCapDAO();
    private PhieuNhapBUS pnBus = new PhieuNhapBUS();
    private SanPhamBUS spBus = new SanPhamBUS(); 
    private PhanQuyen phanQuyen;
    private String tenNguoiDung;
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;
    private JButton btnThem, btnXoa, btnChiTiet, btnLamMoi,btnSearch;
    // cache MaNCC -> TenNCC để hiển thị
    private Map<Integer, String> nccMap = new HashMap<>();
    private CTPhieuNhapBUS ctBus = new CTPhieuNhapBUS();
    private JDateChooser dcsTuNgay, dcsDenNgay; 

    // formatter định dạng tiền
    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    public PnPhieuNhap(PhanQuyen pq) {     
        this.phanQuyen = pq;
        
        Font lblFont = new Font("Segoe UI", Font.PLAIN, 14);
        setPreferredSize(new Dimension(1100, 650));
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Quản lý Phiếu Nhập", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        add(lblTitle, BorderLayout.NORTH);
        // ====== PANEL TÌM KIẾM ======
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "TÌM KIẾM PHIẾU NHẬP",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        JLabel lblSearch = new JLabel("Từ khóa:");
        txtSearch = new JTextField(15);

        // Thêm chọn ngày
        JLabel lblTuNgay = new JLabel("Từ ngày:");
        dcsTuNgay = new JDateChooser();
        dcsTuNgay.setDateFormatString("dd/MM/yyyy");
        dcsTuNgay.setPreferredSize(new Dimension(120, 26));

        JLabel lblDenNgay = new JLabel("Đến ngày:");
        dcsDenNgay = new JDateChooser();
        dcsDenNgay.setDateFormatString("dd/MM/yyyy");
        dcsDenNgay.setPreferredSize(new Dimension(120, 26));

        btnSearch = createClassicButton("Tìm kiếm", "/icon/search.png");

        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(lblTuNgay);
        pnlSearch.add(dcsTuNgay);
        pnlSearch.add(lblDenNgay);
        pnlSearch.add(dcsDenNgay);
        pnlSearch.add(btnSearch);

        add(pnlSearch, BorderLayout.NORTH);

        // ====== DANH SÁCH PHIẾU NHẬP ======
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "DANH SÁCH PHIẾU NHẬP",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));

        // thay "Mã NCC" -> "Nhà cung cấp"
        // giữ cột MaPN_DB ẩn như cũ
        String[] cols = {"Mã PN", "MaPN_DB", "Nhà cung cấp", "Ngày lập", "Tổng tiền"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // ẩn MaPN_DB
        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);

        pnlTable.add(new JScrollPane(table), BorderLayout.CENTER);

        // ====== CÁC NÚT HÀNH ĐỘNG ======
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btnThem = createClassicButton("Thêm", "/icon/them.png");
//        btnSua = createClassicButton("Sửa", "/icon/sua.png");
        btnXoa = createClassicButton("Xóa", "/icon/xoa.png");
        btnChiTiet = createClassicButton("Chi tiết", "/icon/detail.png");
        btnLamMoi = createClassicButton("Làm mới", "/icon/undo.png");
        pnlButtons.add(btnThem);
//        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnChiTiet);
        pnlButtons.add(btnLamMoi);
        pnlTable.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlTable, BorderLayout.CENTER);

        // ====== SỰ KIỆN ======
        btnThem.addActionListener(e -> showAddDialog());
//        btnSua.addActionListener(e -> showEditDialog());
        btnXoa.addActionListener(e -> deleteSelected());
        btnLamMoi.addActionListener(e -> {
            txtSearch.setText("");
            if(dcsTuNgay != null) dcsTuNgay.setDate(null);
            if(dcsDenNgay != null) dcsDenNgay.setDate(null);
            loadData();
        });
        btnSearch.addActionListener(e -> search());
        btnChiTiet.addActionListener(e -> openDetail());
        txtSearch.addActionListener(e -> search());
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) openDetail();
            }
        });
              
        // load NCC map 1 lần
        buildNccCache();

        // load bảng
        loadData();
        configureByPermission();
    }
    
    // ====== build cache MaNCC -> TenNCC ======
    private void buildNccCache() {
    nccMap.clear();
    List<NhaCungCapDTO> ds = nccDao.getAll();
    for (NhaCungCapDTO n : ds) {
        if (n != null) {
            // vì getMaNCC() trong code của bạn là int chứ không phải Integer
            // nên ta lấy ra và đưa vào map luôn
            int id = n.getMaNCC();        // <-- kiểu int
            String ten = n.getTenNCC();   // có thể null nhưng không sao

            nccMap.put(id, ten != null ? ten : ("NCC #" + id));
        }
    }
}


    // ====== LOAD DỮ LIỆU ======
    private void loadData() {
        model.setRowCount(0);

        List<PhieuNhapDTO> list = bus.getAll();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        for (PhieuNhapDTO pn : list) {
            String displayMaPN = bus.toDisplayCode(pn.getMaPN());

            // lấy tên NCC từ cache
            String tenNCC = "";
            if (pn.getMaNCC() != null) {
                tenNCC = nccMap.getOrDefault(pn.getMaNCC(), String.valueOf(pn.getMaNCC()));
            }

            // định dạng tiền
            String formattedMoney = moneyFormat.format(pn.getTongTien()) + " VNĐ";
            String trangThaiText = (pn.getTrangThai() == 1) ? "Đã Xuất" : "Nháp";
            model.addRow(new Object[]{
                    displayMaPN,                    // Mã PN hiển thị (100000 + MaPN)
                    pn.getMaPN(),                   // MaPN_DB (ẩn)
                    tenNCC,                         // tên NCC thay cho mã NCC
                    pn.getNgayLap() == null ? "" : df.format(pn.getNgayLap()),
                    formattedMoney,// Tổng tiền add dấu phẩy + đơn vị
                    trangThaiText
            });
        }
    }
    
    private void configureByPermission() {
        // Giả sử các quyền liên quan: khoNhapHang, khoQuanLyNCC, khoXemSanPham?
        boolean coQuyenNhapHang = phanQuyen.isKhoNhapHang();

        // Các nút: btnThem, btnSua, btnXoa, btnChiTiet, btnLamMoi
        btnThem.setVisible(coQuyenNhapHang); // Thêm phiếu nhập
//        btnSua.setVisible(coQuyenNhapHang);  // Sửa phiếu nhập (chỉ khi chưa xuất)
        btnXoa.setVisible(coQuyenNhapHang);  // Xóa phiếu nhập (chỉ khi chưa xuất)
        btnChiTiet.setVisible(coQuyenNhapHang);   // Xem chi tiết
        btnLamMoi.setVisible(true);          // Làm mới ai cũng dùng được
        // Nút tìm kiếm vẫn hiện
    }
    
    // ====== CÁC CHỨC NĂNG ======
private void showAddDialog() {
    if(!phanQuyen.isKhoNhapHang()){
        JOptionPane.showMessageDialog(this, "Bạn không có quyền thêm phiếu nhập!");
        return;
    }
    
    // Tạo dialog tùy chỉnh
    JDialog addDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "NHẬP HÀNG MỚI", true);
    addDialog.setSize(950, 700);
    addDialog.setLocationRelativeTo(this);
    addDialog.setLayout(new BorderLayout(10, 10));
    
    // Panel chọn nhà cung cấp
    JPanel pnlNCC = new JPanel(new GridBagLayout());
    pnlNCC.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createEtchedBorder(), "THÔNG TIN PHIẾU NHẬP",
        TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));
    pnlNCC.setBackground(new Color(248, 248, 255));
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    JLabel lblNCC = new JLabel("Nhà cung cấp:");
    lblNCC.setFont(new Font("Segoe UI", Font.BOLD, 13));
    JComboBox<NhaCungCapDTO> cboNCC = new JComboBox<>();
    cboNCC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    for (NhaCungCapDTO n : nccDao.getActive()) {
        cboNCC.addItem(n);
    }
    
    JLabel lblNgayLap = new JLabel("Ngày lập:");
    lblNgayLap.setFont(new Font("Segoe UI", Font.BOLD, 13));
    JTextField txtNgayLap = new JTextField(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
    txtNgayLap.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    txtNgayLap.setEditable(false);
    txtNgayLap.setBackground(new Color(240, 240, 240));
    
    gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
    pnlNCC.add(lblNCC, gbc);
    gbc.gridx = 1; gbc.weightx = 0.8;
    pnlNCC.add(cboNCC, gbc);
    gbc.gridx = 2; gbc.weightx = 0.2;
    pnlNCC.add(lblNgayLap, gbc);
    gbc.gridx = 3; gbc.weightx = 0.8;
    pnlNCC.add(txtNgayLap, gbc);
    
    // Panel nhập chi tiết sản phẩm
    JPanel pnlNhapHang = new JPanel(new BorderLayout(10, 10));
    pnlNhapHang.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createEtchedBorder(), "NHẬP CHI TIẾT SẢN PHẨM",
        TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14)));
    pnlNhapHang.setBackground(Color.WHITE);
    
    // Bảng danh sách sản phẩm
    String[] cols = {"Tên sản phẩm", "Mã SP", "Số lượng", "Giá nhập", "Thành tiền"};
    DefaultTableModel tempModel = new DefaultTableModel(cols, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    JTable tempTable = new JTable(tempModel);
    tempTable.setRowHeight(30);
    tempTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tempTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    tempTable.getTableHeader().setBackground(new Color(52, 152, 219));
    tempTable.getTableHeader().setForeground(Color.WHITE);
    
    // Ẩn cột Mã SP
    tempTable.getColumnModel().getColumn(1).setMinWidth(0);
    tempTable.getColumnModel().getColumn(1).setMaxWidth(0);
    
    JScrollPane scrollPane = new JScrollPane(tempTable);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    
    // Panel nhập liệu
    JPanel pnlInput = new JPanel(new GridBagLayout());
    pnlInput.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("THÊM SẢN PHẨM"),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    pnlInput.setBackground(new Color(245, 245, 250));
    
    GridBagConstraints gbcInput = new GridBagConstraints();
    gbcInput.insets = new Insets(5, 10, 5, 10);
    gbcInput.fill = GridBagConstraints.HORIZONTAL;
    
    // ComboBox chọn sản phẩm
    JComboBox<String> cboSanPham = new JComboBox<>();
    Map<String, Integer> mapTenSP_Id = new HashMap<>();
    
    // Load sản phẩm (thay bằng code thực tế của bạn)
    try {
        
         List<SanPhamDTO> dsSP = spBus.getSanPham();
         for (SanPhamDTO sp : dsSP) {
             cboSanPham.addItem(sp.getTenSP());
             mapTenSP_Id.put(sp.getTenSP(), sp.getID());
         }
        
        
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    
    cboSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    cboSanPham.setPreferredSize(new Dimension(200, 30));
    
    JLabel lblTenSP = new JLabel("Sản phẩm:*");
    lblTenSP.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblTenSP.setForeground(Color.RED);
    
    JLabel lblSoLuong = new JLabel("Số lượng:*");
    lblSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblSoLuong.setForeground(Color.RED);
    JTextField txtSoLuong = new JTextField();
    txtSoLuong.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    txtSoLuong.setPreferredSize(new Dimension(100, 30));
    
    JLabel lblGiaNhap = new JLabel("Giá nhập:*");
    lblGiaNhap.setFont(new Font("Segoe UI", Font.BOLD, 13));
    lblGiaNhap.setForeground(Color.RED);
    JTextField txtGiaNhap = new JTextField();
    txtGiaNhap.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    txtGiaNhap.setPreferredSize(new Dimension(150, 30));
    
    JLabel lblThanhTien = new JLabel("Thành tiền:");
    lblThanhTien.setFont(new Font("Segoe UI", Font.BOLD, 13));
    JTextField txtThanhTien = new JTextField();
    txtThanhTien.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    txtThanhTien.setEditable(false);
    txtThanhTien.setBackground(new Color(240, 240, 240));
    txtThanhTien.setPreferredSize(new Dimension(150, 30));
    
    JButton btnThemDong = createClassicButton("Thêm sản phẩm", "/icon/add.png");
    JButton btnXoaDong = createClassicButton("Xóa dòng", "/icon/remove.png");
    JButton btnXoaTatCa = createClassicButton("Xóa tất cả", "/icon/clear.png");
    
    // Layout
    gbcInput.gridx = 0; gbcInput.gridy = 0;
    pnlInput.add(lblTenSP, gbcInput);
    gbcInput.gridx = 1;
    pnlInput.add(cboSanPham, gbcInput);
    gbcInput.gridx = 2;
    pnlInput.add(lblSoLuong, gbcInput);
    gbcInput.gridx = 3;
    pnlInput.add(txtSoLuong, gbcInput);
    
    gbcInput.gridx = 0; gbcInput.gridy = 1;
    pnlInput.add(lblGiaNhap, gbcInput);
    gbcInput.gridx = 1;
    pnlInput.add(txtGiaNhap, gbcInput);
    gbcInput.gridx = 2;
    pnlInput.add(lblThanhTien, gbcInput);
    gbcInput.gridx = 3;
    pnlInput.add(txtThanhTien, gbcInput);
    
    gbcInput.gridx = 0; gbcInput.gridy = 2; gbcInput.gridwidth = 4;
    JPanel pnlButtonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    pnlButtonRow.setOpaque(false);
    pnlButtonRow.add(btnThemDong);
    pnlButtonRow.add(btnXoaDong);
    pnlButtonRow.add(btnXoaTatCa);
    pnlInput.add(pnlButtonRow, gbcInput);
    
    // Panel tổng tiền
    JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    pnlTongTien.setBackground(new Color(255, 255, 200));
    pnlTongTien.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    JLabel lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
    lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTongTien.setForeground(new Color(220, 20, 60));
    pnlTongTien.add(lblTongTien);
    
    // Panel button chính
    JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    pnlButton.setBackground(new Color(240, 248, 255));
    JButton btnLuu = createClassicButton("Lưu phiếu nhập", "/icon/save.png");
    btnLuu.setBackground(new Color(46, 204, 113));
    btnLuu.setForeground(Color.WHITE);
    JButton btnHuy = createClassicButton("Hủy bỏ", "/icon/cancel.png");
    btnHuy.setBackground(new Color(231, 76, 60));
    btnHuy.setForeground(Color.WHITE);
    pnlButton.add(btnLuu);
    pnlButton.add(btnHuy);
    
    // Add panels
    addDialog.add(pnlNCC, BorderLayout.NORTH);
    addDialog.add(pnlNhapHang, BorderLayout.CENTER);
    addDialog.add(pnlTongTien, BorderLayout.SOUTH);
    addDialog.add(pnlButton, BorderLayout.PAGE_END);
    
    pnlNhapHang.add(scrollPane, BorderLayout.CENTER);
    pnlNhapHang.add(pnlInput, BorderLayout.SOUTH);
    
    // ========== CÁC RÀNG BUỘC VÀ KIỂM TRA ==========
    
    // 1. Ràng buộc số lượng - chỉ cho phép nhập số
    txtSoLuong.addKeyListener(new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                e.consume();
                JOptionPane.showMessageDialog(addDialog, "Số lượng chỉ được nhập số!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        public void keyReleased(KeyEvent e) {
            tinhThanhTien(txtSoLuong, txtGiaNhap, txtThanhTien);
            validateSoLuong(txtSoLuong, addDialog);
        }
    });
    
    // 2. Ràng buộc giá nhập - chỉ cho phép nhập số
    txtGiaNhap.addKeyListener(new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                e.consume();
                JOptionPane.showMessageDialog(addDialog, "Giá nhập chỉ được nhập số!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        public void keyReleased(KeyEvent e) {
            tinhThanhTien(txtSoLuong, txtGiaNhap, txtThanhTien);
            validateGiaNhap(txtGiaNhap, addDialog);
        }
    });
    
    // 3. Kiểm tra khi thêm sản phẩm
    btnThemDong.addActionListener(e -> {
        // Kiểm tra sản phẩm đã chọn chưa
        String tenSP = (String) cboSanPham.getSelectedItem();
        if (tenSP == null || tenSP.equals("-- Chọn sản phẩm --") || mapTenSP_Id.get(tenSP) == -1) {
            JOptionPane.showMessageDialog(addDialog, "Vui lòng chọn sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            cboSanPham.requestFocus();
            return;
        }
        
        // Kiểm tra số lượng
        String soLuongStr = txtSoLuong.getText().trim();
        if (soLuongStr.isEmpty()) {
            JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập số lượng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSoLuong.requestFocus();
            return;
        }
        
        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(addDialog, "Số lượng phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocus();
                return;
            }
            if (soLuong > 1000000) {
                JOptionPane.showMessageDialog(addDialog, "Số lượng không được vượt quá 1.000.000!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(addDialog, "Số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoLuong.requestFocus();
            return;
        }
        
        // Kiểm tra giá nhập
        String giaStr = txtGiaNhap.getText().trim();
        if (giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập giá nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtGiaNhap.requestFocus();
            return;
        }
        
        int giaNhap;
        try {
            giaNhap = Integer.parseInt(giaStr);
            if (giaNhap <= 0) {
                JOptionPane.showMessageDialog(addDialog, "Giá nhập phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtGiaNhap.requestFocus();
                return;
            }
            if (giaNhap > 1000000000) {
                JOptionPane.showMessageDialog(addDialog, "Giá nhập không được vượt quá 1.000.000.000!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtGiaNhap.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(addDialog, "Giá nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaNhap.requestFocus();
            return;
        }
        
        int maSP = mapTenSP_Id.get(tenSP);
        int thanhTien = soLuong * giaNhap;
        
        // Kiểm tra trùng lặp sản phẩm
        boolean existed = false;
        int existingRow = -1;
        for (int i = 0; i < tempModel.getRowCount(); i++) {
            if ((int) tempModel.getValueAt(i, 1) == maSP) {
                existed = true;
                existingRow = i;
                break;
            }
        }
        
        if (existed) {
            int confirm = JOptionPane.showConfirmDialog(addDialog, 
                "Sản phẩm '" + tenSP + "' đã có trong danh sách!\nBạn có muốn cập nhật số lượng và giá không?",
                "Trùng sản phẩm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Cập nhật dòng hiện tại
                int oldSoLuong = (int) tempModel.getValueAt(existingRow, 2);
                int oldGia = (int) tempModel.getValueAt(existingRow, 3);
                int newSoLuong = oldSoLuong + soLuong;
                int newGia = giaNhap; // Hoặc giữ nguyên giá cũ tùy logic
                int newThanhTien = newSoLuong * newGia;
                
                tempModel.setValueAt(newSoLuong, existingRow, 2);
                tempModel.setValueAt(newGia, existingRow, 3);
                tempModel.setValueAt(newThanhTien, existingRow, 4);
                
                JOptionPane.showMessageDialog(addDialog, "Đã cập nhật số lượng cho sản phẩm '" + tenSP + "'!");
            }
        } else {
            // Thêm mới
            tempModel.addRow(new Object[]{tenSP, maSP, soLuong, giaNhap, thanhTien});
            JOptionPane.showMessageDialog(addDialog, "Đã thêm sản phẩm '" + tenSP + "' thành công!");
        }
        
        // Clear input
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtThanhTien.setText("");
        cboSanPham.setSelectedIndex(0);
        
        // Cập nhật tổng tiền
        long tong = 0;
        for (int i = 0; i < tempModel.getRowCount(); i++) {
            tong += (int) tempModel.getValueAt(i, 4);
        }
        lblTongTien.setText("Tổng tiền: " + String.format("%,d VNĐ", tong));
    });
    
    // Xóa dòng
    btnXoaDong.addActionListener(e -> {
        int row = tempTable.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(addDialog, "Xóa sản phẩm '" + tempModel.getValueAt(row, 0) + "'?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tempModel.removeRow(row);
                // Cập nhật tổng tiền
                long tong = 0;
                for (int i = 0; i < tempModel.getRowCount(); i++) {
                    tong += (int) tempModel.getValueAt(i, 4);
                }
                lblTongTien.setText("Tổng tiền: " + String.format("%,d VNĐ", tong));
            }
        } else {
            JOptionPane.showMessageDialog(addDialog, "Vui lòng chọn dòng cần xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    });
    
    // Xóa tất cả
    btnXoaTatCa.addActionListener(e -> {
        if (tempModel.getRowCount() > 0) {
            int confirm = JOptionPane.showConfirmDialog(addDialog, "Xóa tất cả sản phẩm trong danh sách?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tempModel.setRowCount(0);
                lblTongTien.setText("Tổng tiền: 0 VNĐ");
            }
        }
    });
    
    // Kiểm tra khi lưu phiếu
    btnLuu.addActionListener(e -> {
        // Kiểm tra nhà cung cấp
        NhaCungCapDTO selectedNCC = (NhaCungCapDTO) cboNCC.getSelectedItem();
        if (selectedNCC == null) {
            JOptionPane.showMessageDialog(addDialog, "Vui lòng chọn nhà cung cấp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Kiểm tra danh sách sản phẩm
        if (tempModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(addDialog, "Vui lòng nhập ít nhất một sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Xác nhận lưu
        int confirm = JOptionPane.showConfirmDialog(addDialog, 
            "Xác nhận lưu phiếu nhập với " + tempModel.getRowCount() + " sản phẩm?\nTổng tiền: " + lblTongTien.getText(),
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        
        // Tạo phiếu nhập
        PhieuNhapDTO pn = new PhieuNhapDTO();
        pn.setMaNCC(selectedNCC.getMaNCC());
        pn.setNgayLap(new java.sql.Date(new Date().getTime()));
        pn.setTongTien(0);
        pn.setTrangThai(0);
        
        int newId = bus.add(pn);
        if (newId > 0) {
            boolean allSuccess = true;
            StringBuilder errorProducts = new StringBuilder();
            
            for (int i = 0; i < tempModel.getRowCount(); i++) {
                int maSP = (int) tempModel.getValueAt(i, 1);
                int soLuong = (int) tempModel.getValueAt(i, 2);
                int giaNhap = (int) tempModel.getValueAt(i, 3);
                String tenSP = (String) tempModel.getValueAt(i, 0);
                
                boolean ok = ctBus.addDetail(newId, maSP, soLuong, giaNhap);
                if (!ok) {
                    allSuccess = false;
                    errorProducts.append("\n- ").append(tenSP);
                }
            }
            
            if (allSuccess) {
                bus.updateTongTienFromDetail(newId);
                JOptionPane.showMessageDialog(addDialog, "Thêm phiếu nhập thành công!\nMã phiếu: " + bus.toDisplayCode(newId));
                addDialog.dispose();
                buildNccCache();
                loadData();
            } else {
                bus.delete(newId);
                JOptionPane.showMessageDialog(addDialog, 
                    "Lỗi khi thêm các sản phẩm:" + errorProducts.toString() + 
                    "\nVui lòng kiểm tra lại sản phẩm đã tồn tại trong hệ thống!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(addDialog, "Tạo phiếu nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    });
    
    btnHuy.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(addDialog, "Bạn có chắc muốn hủy? Dữ liệu sẽ không được lưu!", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            addDialog.dispose();
        }
    });
    
    addDialog.setVisible(true);
}

// Helper methods kiểm tra
private void validateSoLuong(JTextField txtSoLuong, JDialog dialog) {
    try {
        String text = txtSoLuong.getText().trim();
        if (!text.isEmpty()) {
            int soLuong = Integer.parseInt(text);
            if (soLuong <= 0) {
                txtSoLuong.setBackground(new Color(255, 200, 200));
            } else if (soLuong > 1000000) {
                txtSoLuong.setBackground(new Color(255, 200, 200));
            } else {
                txtSoLuong.setBackground(Color.WHITE);
            }
        } else {
            txtSoLuong.setBackground(Color.WHITE);
        }
    } catch (NumberFormatException ex) {
        txtSoLuong.setBackground(new Color(255, 200, 200));
    }
}

private void validateGiaNhap(JTextField txtGiaNhap, JDialog dialog) {
    try {
        String text = txtGiaNhap.getText().trim();
        if (!text.isEmpty()) {
            int gia = Integer.parseInt(text);
            if (gia <= 0) {
                txtGiaNhap.setBackground(new Color(255, 200, 200));
            } else if (gia > 1000000000) {
                txtGiaNhap.setBackground(new Color(255, 200, 200));
            } else {
                txtGiaNhap.setBackground(Color.WHITE);
            }
        } else {
            txtGiaNhap.setBackground(Color.WHITE);
        }
    } catch (NumberFormatException ex) {
        txtGiaNhap.setBackground(new Color(255, 200, 200));
    }
}

private void tinhThanhTien(JTextField txtSL, JTextField txtGia, JTextField txtThanhTien) {
    try {
        String slStr = txtSL.getText().trim();
        String giaStr = txtGia.getText().trim();
        
        if (!slStr.isEmpty() && !giaStr.isEmpty()) {
            int sl = Integer.parseInt(slStr);
            int gia = Integer.parseInt(giaStr);
            if (sl > 0 && gia > 0) {
                long thanhTien = (long) sl * gia;
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

    private void showEditDialog() {
        int r = table.getSelectedRow();
        if (r < 0) { 
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 phiếu để sửa."); 
            return; 
        }
        int maPN = (int) model.getValueAt(r, 1);
        PhieuNhapDTO current = bus.getById(maPN);
        if (current != null && current.getTrangThai() == 1) {
             JOptionPane.showMessageDialog(this, "Phiếu nhập đã được xuất (hoàn tất) và không thể sửa đổi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
             return;
        }
        try {
            String ngayStr = (String) model.getValueAt(r, 3);
            String tongStrDisplay = (String) model.getValueAt(r, 4); // dạng "20,000,000 VNĐ"
//            String tongStrRaw = tongStrDisplay.replace("VNĐ","").replace("VND","").replace(",","").trim();
//            Double tongDouble = Double.parseDouble(tongStrRaw);

            // 1. Loại bỏ đơn vị tiền tệ để chuỗi chỉ còn là số được định dạng
            String tongStrClean = tongStrDisplay.replace("VNĐ","").replace("VND","").trim();

            // 2. Sử dụng DecimalFormat đã dùng để format (moneyFormat) để parse ngược lại
            Number num = moneyFormat.parse(tongStrClean);
            Double tongDouble = num.doubleValue();
            
            // hiện tại cột 2 trong bảng model là "Nhà cung cấp" (tên),
            // ta cần maNCC thực tế -> lấy từ DAO thay vì từ bảng
            Integer curMaNCC = (current != null) ? current.getMaNCC() : null;

            JPanel p = new JPanel(new GridLayout(3,2,8,8));

            JComboBox<NhaCungCapDTO> cbo = new JComboBox<>();
            List<NhaCungCapDTO> allNcc = nccDao.getAll();
            for (NhaCungCapDTO n : allNcc) {
                cbo.addItem(n);
            }
            // select ncc hiện tại
        if (curMaNCC != null) {
            for (int i = 0; i < cbo.getItemCount(); i++) {
                NhaCungCapDTO item = cbo.getItemAt(i);
                if (item != null) {
                    int idItem = item.getMaNCC(); // int
                    if (idItem == curMaNCC.intValue()) {
                        cbo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }


            JFormattedTextField txtNgay = new JFormattedTextField(new java.text.SimpleDateFormat("yyyy-MM-dd"));
            txtNgay.setValue(new java.text.SimpleDateFormat("yyyy-MM-dd").parse(ngayStr));

            // tổng tiền edit ở đây thực ra ít khi cho sửa tay,
            // nhưng bạn có để sửa nên mình vẫn map về raw số
            JTextField txtTong = new JTextField(String.valueOf(tongDouble.intValue()));

            p.add(new JLabel("Nhà cung cấp:")); p.add(cbo);
            p.add(new JLabel("Ngày lập:")); p.add(txtNgay);
            p.add(new JLabel("Tổng tiền:")); p.add(txtTong);

            int opt = JOptionPane.showConfirmDialog(this, p, "Sửa phiếu nhập", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opt == JOptionPane.OK_OPTION) {
                NhaCungCapDTO sel = (NhaCungCapDTO) cbo.getSelectedItem();
                java.sql.Date ngay = new java.sql.Date(((java.util.Date)txtNgay.getValue()).getTime());
                double tong = Double.parseDouble(txtTong.getText().replaceAll(",", "").trim());

                PhieuNhapDTO pn = new PhieuNhapDTO(maPN, sel.getMaNCC(), ngay, tong,current.getTrangThai());
                if (bus.update(pn)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    buildNccCache();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
         int r = table.getSelectedRow();
        if (r < 0) { 
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 phiếu để xóa."); 
            return; 
        }
        int maPN = (int) model.getValueAt(r, 1);
        PhieuNhapDTO current = bus.getById(maPN);
        if (current != null && current.getTrangThai() == 1) {
             JOptionPane.showMessageDialog(this, "Phiếu nhập đã được xuất (hoàn tất) và không thể xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
             return;
        }
        int opt = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa phiếu " + model.getValueAt(r,0) + " ?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (opt != JOptionPane.YES_OPTION) return;

        if (bus.delete(maPN)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }

    private void search() {
        try {
            String key = txtSearch.getText().trim();
           
            java.util.Date tuNgay = dcsTuNgay.getDate();
            java.util.Date denNgay = dcsDenNgay.getDate();

            // Gọi BUS (bạn cần thêm tham số ngày vào hàm search của PhieuNhapBUS)
            List<PhieuNhapDTO> res = bus.search(key, tuNgay, denNgay); 

            model.setRowCount(0);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            for (PhieuNhapDTO pn : res) {
                String displayMaPN = bus.toDisplayCode(pn.getMaPN());
                String tenNCC = pn.getMaNCC() != null ? nccMap.getOrDefault(pn.getMaNCC(), "") : "";
                String formattedMoney = moneyFormat.format(pn.getTongTien()) + " VNĐ";

                model.addRow(new Object[]{
                        displayMaPN,
                        pn.getMaPN(),
                        tenNCC,
                        pn.getNgayLap() == null ? "" : df.format(pn.getNgayLap()),
                        formattedMoney
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage());
        }
    }
    private void openDetail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập để xem chi tiết!");
            return;
        }

        // 1. Lấy mã phiếu nhập và trạng thái từ dòng được chọn
        int maPN = (int) model.getValueAt(row, 1); 
        // Giả sử bạn cần lấy trạng thái từ danh sách gốc hoặc cột ẩn
//        int trangThai = 1; 
        PhieuNhapDTO current = bus.getById(maPN);
        int trangThai = (current != null) ? current.getTrangThai() : 0;

        // 2. Lấy tên nhân viên đang đăng nhập 
        MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
        String tenNV = mainFrame.getTaiKhoan().getTenNV(); 
//        String tenNV = mainFrame.getTaiKhoan().getTaiKhoan(); 

        // 3. Khởi tạo Dialog với tham số tên nhân viên ở cuối
        PnCTPhieuNhap detailDlg = new PnCTPhieuNhap(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            maPN, 
            trangThai, 
            phanQuyen, 
            tenNV // Tham số mới truyền vào
        );

        detailDlg.setVisible(true);

        // Sau khi đóng dialog, load lại dữ liệu nếu cần
        buildNccCache();
        loadData();
    }

    // ====== HÀM TẠO NÚT PHONG CÁCH CỔ ĐIỂN ======
    private JButton createClassicButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setBackground(UIManager.getColor("Button.background"));
        btn.setBorder(BorderFactory.createLineBorder(new Color(160, 180, 200)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ImageIcon icon = loadScaledIcon(iconPath, 18, 18);
        if (icon != null) btn.setIcon(icon);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(8);
        return btn;
    }

    private ImageIcon loadScaledIcon(String path, int w, int h) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) return null;
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ex) {
            return null;
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new PnPhieuNhap().setVisible(true));
//    }
}