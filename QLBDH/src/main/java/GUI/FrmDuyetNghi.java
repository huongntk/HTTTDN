package GUI;

import BUS.DonNghiBUS;
import DTO.DonNghiDTO;
import DTO.PhanQuyen;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class FrmDuyetNghi extends javax.swing.JPanel {

    private PhanQuyen phanQuyen;
    private DonNghiBUS donNghiBUS;
    private DefaultTableModel modelDon;

    public FrmDuyetNghi(PhanQuyen pq) {
        initComponents();
        this.phanQuyen = pq;
        donNghiBUS = new DonNghiBUS();
        modelDon = (DefaultTableModel) tblDonNghi.getModel();

        loadData();
        addEvents();
        setPreferredSize(new java.awt.Dimension(900, 600));
    }

    private void loadData() {
        modelDon.setRowCount(0);
        ArrayList<DonNghiDTO> list = donNghiBUS.layTatCaDon();   
        for (DonNghiDTO d : list) {
            modelDon.addRow(new Object[]{
                d.getMaDon(),
                d.getHoTen(),
                d.getLoaiDon(),         
                d.getNgayBatDau(),
                d.getNgayKetThuc(),
                d.getLyDo(),
                d.getTrangThai()
            });
        }
    }

    private void addEvents() {
        tblDonNghi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblDonNghi.getSelectedRow();
                    if (row != -1) {
                        int maDon = (int) modelDon.getValueAt(row, 0);
                        String trangThai = (String) modelDon.getValueAt(row, 6);

                        if ("Đã duyệt".equals(trangThai) || "Từ chối".equals(trangThai)) {
                            JOptionPane.showMessageDialog(FrmDuyetNghi.this, "Đơn này đã được xử lý!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        int confirm = JOptionPane.showConfirmDialog(FrmDuyetNghi.this,
                                "Bạn muốn DUYỆT đơn này?", "Xác nhận duyệt", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            String msg = donNghiBUS.duyetDon(maDon);
                            JOptionPane.showMessageDialog(FrmDuyetNghi.this, msg);
                            loadData();
                        }
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDonNghi = new javax.swing.JTable();
        btnDuyet = new javax.swing.JButton();
        btnTuChoi = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(170, 222, 215));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DUYỆT ĐƠN NGHỈ PHÉP / ỐM ĐAU / THAI SẢN / NGHỈ VIỆC");

        tblDonNghi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        tblDonNghi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Mã đơn", "Nhân viên", "Loại nghỉ", "Từ ngày", "Đến ngày", "Lý do", "Trạng thái"}
        ) {
            boolean[] canEdit = new boolean[] {false, false, false, false, false, false, false};
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit[columnIndex]; }
        });
        tblDonNghi.setRowHeight(28);
        jScrollPane1.setViewportView(tblDonNghi);

        btnDuyet.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnDuyet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xacnhan.png")));
        btnDuyet.setText("Duyệt");
        btnDuyet.addActionListener(evt -> {
            int row = tblDonNghi.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int maDon = (int) modelDon.getValueAt(row, 0);
            String msg = donNghiBUS.duyetDon(maDon);
            JOptionPane.showMessageDialog(this, msg);
            loadData();
        });

        btnTuChoi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnTuChoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/close.png")));
        btnTuChoi.setText("Từ chối");
        btnTuChoi.addActionListener(evt -> {
            int row = tblDonNghi.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int maDon = (int) modelDon.getValueAt(row, 0);
            String msg = donNghiBUS.tuChoiDon(maDon);
            JOptionPane.showMessageDialog(this, msg);
            loadData();
        });

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png")));
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(evt -> loadData());
        
        // Layout (NetBeans style)
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDuyet, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18)
                        .addComponent(btnTuChoi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20)
                .addComponent(jLabel1)
                .addGap(18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDuyet, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTuChoi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20))
        );
        this.setLayout(new java.awt.BorderLayout());
        this.add(jPanel1, java.awt.BorderLayout.CENTER);

    }

    // Variables declaration
    private javax.swing.JButton btnDuyet;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnTuChoi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDonNghi;
}