package GUI;

import BUS.ThongKeBUS;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PnThongKe extends javax.swing.JPanel {

    private ThongKeBUS thongKeBUS;
    private DefaultTableModel modelTopSP, modelTopKH, modelTopNV;
    private JTabbedPane tabbedPane;
    private JTable tblTopSP, tblTopKH, tblTopNV;
    private Locale localeVN = new Locale("vi", "VN");
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);

    public PnThongKe() {
        initComponents(); 
        
        thongKeBUS = new ThongKeBUS();

        setupTables();

        dateChooserStart.setDate(new Date());
        dateChooserEnd.setDate(new Date());

        addEvents();
    }

    private void setupTables() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        modelTopSP = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelTopSP.setColumnIdentifiers(new String[]{"Mã SP", "Tên Sản Phẩm", "Đã Bán", "Tổng Giá Trị"});
        tblTopSP = new JTable(modelTopSP);
        setupTableStyle(tblTopSP);
        tabbedPane.addTab("Top Sản Phẩm", new JScrollPane(tblTopSP));

        modelTopKH = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelTopKH.setColumnIdentifiers(new String[]{"Mã KH", "Tên Khách Hàng", "Số HĐ Mua", "Tổng Chi Tiêu"});
        tblTopKH = new JTable(modelTopKH);
        setupTableStyle(tblTopKH);
        tabbedPane.addTab("Top Khách Hàng", new JScrollPane(tblTopKH));

        modelTopNV = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelTopNV.setColumnIdentifiers(new String[]{"Mã NV", "Tên Nhân Viên", "Số HĐ Lập", "Tổng Doanh Số"});
        tblTopNV = new JTable(modelTopNV);
        setupTableStyle(tblTopNV);
        tabbedPane.addTab("Top Nhân Viên", new JScrollPane(tblTopNV));

        pnlBang.remove(jScrollPane1); 
        pnlBang.add(tabbedPane, BorderLayout.CENTER); 
    }

    private void setupTableStyle(JTable table) {
        table.setFont(new Font("Segoe UI", 0, 14));
        table.setRowHeight(23);
        table.setGridColor(new java.awt.Color(51, 51, 51));
        table.setShowGrid(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        JTableHeader header = table.getTableHeader();
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void addEvents() {
        btnThongKe.addActionListener(e -> thucHienThongKe());
    }

    private void thucHienThongKe() {
        Date tuNgayUtil = dateChooserStart.getDate();
        Date denNgayUtil = dateChooserEnd.getDate();

        if (tuNgayUtil == null || denNgayUtil == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cả ngày bắt đầu và ngày kết thúc.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (denNgayUtil.before(tuNgayUtil)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được trước ngày bắt đầu.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.sql.Date tuNgay = new java.sql.Date(tuNgayUtil.getTime());
        java.sql.Date denNgay = new java.sql.Date(denNgayUtil.getTime());

        loadTopSanPham(tuNgay, denNgay);
        loadTopKhachHang(tuNgay, denNgay);
        loadTopNhanVien(tuNgay, denNgay);
        
        double tongDoanhThu = thongKeBUS.getTongDoanhThu(tuNgay, denNgay);
        txtTongDoanhThu.setText(currencyFormatter.format(tongDoanhThu));
    }
    
    private void loadTopSanPham(java.sql.Date tuNgay, java.sql.Date denNgay) {
        modelTopSP.setRowCount(0);
        ArrayList<Object[]> ds = thongKeBUS.getTopSanPham(tuNgay, denNgay);
        if (ds == null || ds.isEmpty()) {
            modelTopSP.addRow(new Object[]{"", "Không có dữ liệu", "", ""});
            return;
        }
        for (Object[] row : ds) {
            row[3] = currencyFormatter.format(row[3]);
            modelTopSP.addRow(row);
        }
    }

    private void loadTopKhachHang(java.sql.Date tuNgay, java.sql.Date denNgay) {
        modelTopKH.setRowCount(0);
        ArrayList<Object[]> ds = thongKeBUS.getTopKhachHang(tuNgay, denNgay);
        if (ds == null || ds.isEmpty()) {
            modelTopKH.addRow(new Object[]{"", "Không có dữ liệu", "", ""});
            return;
        }
        for (Object[] row : ds) {
            row[3] = currencyFormatter.format(row[3]);
            modelTopKH.addRow(row);
        }
    }

    private void loadTopNhanVien(java.sql.Date tuNgay, java.sql.Date denNgay) {
        modelTopNV.setRowCount(0);
        ArrayList<Object[]> ds = thongKeBUS.getTopNhanVien(tuNgay, denNgay);
         if (ds == null || ds.isEmpty()) {
            modelTopNV.addRow(new Object[]{"", "Không có dữ liệu", "", ""});
            return;
        }
        for (Object[] row : ds) {
            row[3] = currencyFormatter.format(row[3]);
            modelTopNV.addRow(row);
        }
    }

    @SuppressWarnings("unchecked")
    
    private void initComponents() {

        pnlBoLoc = new javax.swing.JPanel();
        lblTieuDeBoLoc = new javax.swing.JLabel();
        lblTuNgay = new javax.swing.JLabel();
        lblDenNgay = new javax.swing.JLabel();
        btnThongKe = new javax.swing.JButton();
        dateChooserStart = new com.toedter.calendar.JDateChooser();
        dateChooserEnd = new com.toedter.calendar.JDateChooser();
        lblTongDoanhThu = new javax.swing.JLabel();
        txtTongDoanhThu = new javax.swing.JTextField();
        pnlBang = new javax.swing.JPanel();
        lblTieuDeBang = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHolder = new javax.swing.JTable();

        pnlBoLoc.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));

        lblTieuDeBoLoc.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        lblTieuDeBoLoc.setText("Bộ lọc thống kê");

        lblTuNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblTuNgay.setText("Từ ngày:");

        lblDenNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblDenNgay.setText("Đến ngày:");

        btnThongKe.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        try {
            btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/statistics.png"))); 
        } catch (Exception e) {
            System.out.println("Không tìm thấy icon /icon/statistics.png");
        }
        btnThongKe.setText("Thống kê");

        dateChooserStart.setFont(new java.awt.Font("Segoe UI", 0, 14)); 

        dateChooserEnd.setFont(new java.awt.Font("Segoe UI", 0, 14)); 

        lblTongDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 16)); 
        lblTongDoanhThu.setText("TỔNG DOANH THU:");

        txtTongDoanhThu.setEditable(false);
        txtTongDoanhThu.setFont(new java.awt.Font("Segoe UI", 1, 16)); 
        txtTongDoanhThu.setForeground(new java.awt.Color(255, 0, 0));
        txtTongDoanhThu.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongDoanhThu.setText("0 ₫");

        javax.swing.GroupLayout pnlBoLocLayout = new javax.swing.GroupLayout(pnlBoLoc);
        pnlBoLoc.setLayout(pnlBoLocLayout);
        pnlBoLocLayout.setHorizontalGroup(
            pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBoLocLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBoLocLayout.createSequentialGroup()
                        .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDenNgay)
                            .addComponent(lblTuNgay))
                        .addGap(18, 18, 18)
                        .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserStart, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                            .addComponent(dateChooserEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(txtTongDoanhThu)
                    .addGroup(pnlBoLocLayout.createSequentialGroup()
                        .addComponent(lblTongDoanhThu)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBoLocLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
            .addGroup(pnlBoLocLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(lblTieuDeBoLoc)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBoLocLayout.setVerticalGroup(
            pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBoLocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTieuDeBoLoc)
                .addGap(40, 40, 40)
                .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTuNgay)
                    .addComponent(dateChooserStart, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlBoLocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDenNgay)
                    .addComponent(dateChooserEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(lblTongDoanhThu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBang.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));
        pnlBang.setLayout(new java.awt.BorderLayout());

        lblTieuDeBang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTieuDeBang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTieuDeBang.setText("Kết quả thống kê");
        lblTieuDeBang.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlBang.add(lblTieuDeBang, java.awt.BorderLayout.NORTH);

        tblHolder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblHolder);

        pnlBang.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlBoLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBang, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlBang, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addComponent(pnlBoLoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }


    
    private javax.swing.JButton btnThongKe;
    private com.toedter.calendar.JDateChooser dateChooserEnd;
    private com.toedter.calendar.JDateChooser dateChooserStart;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDenNgay;
    private javax.swing.JLabel lblTieuDeBang;
    private javax.swing.JLabel lblTieuDeBoLoc;
    private javax.swing.JLabel lblTongDoanhThu;
    private javax.swing.JLabel lblTuNgay;
    private javax.swing.JPanel pnlBang;
    private javax.swing.JPanel pnlBoLoc;
    private javax.swing.JTable tblHolder;
    private javax.swing.JTextField txtTongDoanhThu;
    
}