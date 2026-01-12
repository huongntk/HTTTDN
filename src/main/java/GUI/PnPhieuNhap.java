package GUI;

import BUS.PhieuNhapBUS;
import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;
import DTO.PhieuNhapDTO;

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

public class PnPhieuNhap extends JPanel {
    private PhieuNhapBUS bus = new PhieuNhapBUS();
    private NhaCungCapDAO nccDao = new NhaCungCapDAO();

    private DefaultTableModel model;
    private JTable table;
    private JTextField txtSearch;

    // cache MaNCC -> TenNCC để hiển thị
    private Map<Integer, String> nccMap = new HashMap<>();

    // formatter định dạng tiền
    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    public PnPhieuNhap() {     
        
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
        txtSearch = new JTextField(25);
        JButton btnSearch = createClassicButton("Tìm kiếm", "/icon/search.png");
        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearch);
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
        JButton btnThem = createClassicButton("Thêm", "/icon/them.png");
        JButton btnSua = createClassicButton("Sửa", "/icon/sua.png");
        JButton btnXoa = createClassicButton("Xóa", "/icon/xoa.png");
        JButton btnChiTiet = createClassicButton("Chi tiết", "/icon/detail.png");
        JButton btnLamMoi = createClassicButton("Làm mới", "/icon/undo.png");
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnChiTiet);
        pnlButtons.add(btnLamMoi);
        pnlTable.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlTable, BorderLayout.CENTER);

        // ====== SỰ KIỆN ======
        btnThem.addActionListener(e -> showAddDialog());
        btnSua.addActionListener(e -> showEditDialog());
        btnXoa.addActionListener(e -> deleteSelected());
        btnLamMoi.addActionListener(e -> loadData());
        btnSearch.addActionListener(e -> search());
        btnChiTiet.addActionListener(e -> openDetail());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) openDetail();
            }
        });

        // load NCC map 1 lần
        buildNccCache();

        // load bảng
        loadData();
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

    // ====== CÁC CHỨC NĂNG ======
    private void showAddDialog() {
        try {
            JPanel p = new JPanel(new GridLayout(4,2,8,8));
            String estimate = bus.nextDisplayEstimate();
            JTextField txtMaPN = new JTextField(estimate);
            txtMaPN.setEditable(false);

            // CHỈ NCC HOẠT ĐỘNG
        JComboBox<NhaCungCapDTO> cbo = new JComboBox<>();
        for (NhaCungCapDTO n : nccDao.getActive()) {
            cbo.addItem(n);
        }

            JTextField txtNgay = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
            txtNgay.setEditable(false);

            JTextField txtTong = new JTextField("0 VNĐ");
            txtTong.setEditable(false);

            p.add(new JLabel("Mã PN:")); p.add(txtMaPN);
            p.add(new JLabel("Nhà cung cấp:")); p.add(cbo);
            p.add(new JLabel("Ngày lập:")); p.add(txtNgay);
            p.add(new JLabel("Tổng tiền:")); p.add(txtTong);

            int opt = JOptionPane.showConfirmDialog(this, p, "Thêm phiếu nhập", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opt == JOptionPane.OK_OPTION) {
                NhaCungCapDTO sel = (NhaCungCapDTO) cbo.getSelectedItem();
                if (sel == null) { 
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp."); 
                    return; 
                }

                // tạo phiếu nhập mặc định TongTien=0
                PhieuNhapDTO pn = bus.createDefault(sel.getMaNCC());
                int newId = bus.add(pn);
                if (newId > 0) {

                    // mở form chi tiết phiếu nhập
                    PnCTPhieuNhap detailDlg = new PnCTPhieuNhap((Frame) SwingUtilities.getWindowAncestor(this), newId, 0);
                    

                    detailDlg.setVisible(true);

                    // reload lại cache NCC phòng trường hợp NCC mới vừa tạo (hiếm)
                    buildNccCache();
                    loadData();

                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage());
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
            List<PhieuNhapDTO> res = bus.search(key);
            model.setRowCount(0);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            for (PhieuNhapDTO pn : res) {
                String displayMaPN = bus.toDisplayCode(pn.getMaPN());

                String tenNCC = "";
                if (pn.getMaNCC() != null) {
                    tenNCC = nccMap.getOrDefault(pn.getMaNCC(), String.valueOf(pn.getMaNCC()));
                }

                String formattedMoney = moneyFormat.format(pn.getTongTien()) + " VNĐ";

                model.addRow(new Object[]{
                        displayMaPN,
                        pn.getMaPN(),
                        tenNCC,
                        pn.getNgayLap()==null?"":df.format(pn.getNgayLap()),
                        formattedMoney
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + ex.getMessage());
        }
    }

    private void openDetail() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 phiếu để xem chi tiết.");
            return;
        }
        int maPN = (int) model.getValueAt(r, 1);
        
        PhieuNhapDTO current = bus.getById(maPN);
        int trangThai = (current != null) ? current.getTrangThai() : 0;
        // mở chi tiết dạng modal
//        PnCTPhieuNhap detailDlg = new PnCTPhieuNhap(this, maPN);
        PnCTPhieuNhap detailDlg = new PnCTPhieuNhap((Frame) SwingUtilities.getWindowAncestor(this), maPN, trangThai);

        detailDlg.setVisible(true);

        // khi đóng chi tiết -> reload lại
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
