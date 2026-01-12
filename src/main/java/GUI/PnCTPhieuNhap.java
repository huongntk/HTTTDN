package GUI;

import BUS.CTPhieuNhapBUS;
import BUS.PhieuNhapBUS;
import DAO.PhieuNhapDAO;
import DTO.CTPhieuNhapDTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.io.File; 
import javax.imageio.ImageIO; 
import java.awt.image.BufferedImage; 
import java.text.SimpleDateFormat;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PnCTPhieuNhap extends JDialog {

    private final int maPN; // MaPN thực trong DB
    private final int trangThaiPN;
    private JTextField txtMaPN;
    private JTextField txtID;
    private JTextField txtSoLuong;
    private JTextField txtGiaNhap;
    private JTextField txtThanhTien;
    private JLabel lblTongTien;

    private DefaultTableModel model;
    private JTable table;
    
    private JButton btnLuu; 
    private JButton btnSua; 
    private JButton btnXoa; 
    private JButton btnXuat; 
    private JButton btnDong;

    private final CTPhieuNhapBUS ctBus = new CTPhieuNhapBUS();
    private final PhieuNhapBUS pnBus = new PhieuNhapBUS();
    

    public PnCTPhieuNhap(Frame owner, int maPN, int trangThai) {
        super(owner, "Chi tiết Phiếu Nhập", true); // modal dialog
        this.maPN = maPN;
        this.trangThaiPN = trangThai;

        setSize(1150, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));

        Font lblFont = new Font("Segoe UI", Font.PLAIN, 14);

        /* ========== LEFT PANEL (Form nhập/sửa 1 dòng chi tiết) ========== */
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "THÔNG TIN CHI TIẾT PHIẾU NHẬP",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 12, 8, 12);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        JLabel lblMaPN = new JLabel("Mã PN:");
        JLabel lblID = new JLabel("ID (Mã sản phẩm):");
        JLabel lblSL = new JLabel("Số lượng:");
        JLabel lblGia = new JLabel("Giá nhập:");
        JLabel lblThanhTien = new JLabel("Thành tiền:");

        lblMaPN.setFont(lblFont);
        lblID.setFont(lblFont);
        lblSL.setFont(lblFont);
        lblGia.setFont(lblFont);
        lblThanhTien.setFont(lblFont);

        txtMaPN = new JTextField(String.valueOf(maPN));
        txtMaPN.setEditable(false);

        txtID = new JTextField();
        txtSoLuong = new JTextField();
        txtGiaNhap = new JTextField();
        txtThanhTien = new JTextField();
        txtThanhTien.setEditable(false);

        int row = 0;
        g.gridx=0; g.gridy=row; leftPanel.add(lblMaPN,g);
        g.gridx=1; leftPanel.add(txtMaPN,g); row++;

        g.gridx=0; g.gridy=row; leftPanel.add(lblID,g);
        g.gridx=1; leftPanel.add(txtID,g); row++;

        g.gridx=0; g.gridy=row; leftPanel.add(lblSL,g);
        g.gridx=1; leftPanel.add(txtSoLuong,g); row++;

        g.gridx=0; g.gridy=row; leftPanel.add(lblGia,g);
        g.gridx=1; leftPanel.add(txtGiaNhap,g); row++;

        g.gridx=0; g.gridy=row; leftPanel.add(lblThanhTien,g);
        g.gridx=1; leftPanel.add(txtThanhTien,g); row++;

        btnLuu = createClassicButton("Lưu chi tiết", "/icon/save.png");
        JPanel pnlAddHolder = new JPanel();
        pnlAddHolder.setOpaque(false);
        pnlAddHolder.add(btnLuu);

        row++;
        g.gridx=0; g.gridy=row; g.gridwidth=2;
        leftPanel.add(pnlAddHolder,g);

        /* ========== RIGHT PANEL (Bảng chi tiết + tổng tiền) ========== */
        JPanel rightPanel = new JPanel(new BorderLayout(8,8));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "DANH SÁCH CHI TIẾT PHIẾU NHẬP",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));

        String[] cols = {"Mã PN", "ID SP", "Số lượng", "Giá nhập", "Thành tiền"};
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

        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlTongTien.add(lblTongTien);
        rightPanel.add(pnlTongTien, BorderLayout.NORTH);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btnSua = createClassicButton("Sửa", "/icon/sua.png");
        btnXoa = createClassicButton("Xóa", "/icon/xoa.png");
        JButton btnLamMoi = createClassicButton("Làm mới", "/icon/undo.png");
//        JButton btnLuu = createClassicButton("Lưu", "/icon/boxes.png");
        btnDong = createClassicButton("Đóng", "/icon/logout.png"); // Nút này chỉ đóng
        btnXuat = createClassicButton("Xuất Phiếu", "/icon/export.png"); 
        JButton btnIn = createClassicButton("Lưu ảnh", "/icon/print.png");
        pnlBottom.add(btnSua);
        pnlBottom.add(btnXoa);
        pnlBottom.add(btnLamMoi);
//        pnlBottom.add(btnLuu);
        pnlBottom.add(btnXuat);
        pnlBottom.add(btnDong);
        rightPanel.add(pnlBottom, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setResizeWeight(0.35);
        split.setDividerLocation(380);
        add(split, BorderLayout.CENTER);

        /* ========== EVENT HANDLERS ========== */

        // Click 1 dòng -> đổ lên form để sửa / xoá
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    txtID.setText(model.getValueAt(r,1).toString());
                    txtSoLuong.setText(model.getValueAt(r,2).toString());
                    txtGiaNhap.setText(model.getValueAt(r,3).toString());
                    txtThanhTien.setText(model.getValueAt(r,4).toString());
                }
            }
        });
        
        // Nút Đóng 
        btnDong.addActionListener(e -> dispose());
        
        // Nút Xuất Phiếu 
        btnXuat.addActionListener(e -> finalizePhieuNhap());
        btnIn.addActionListener(e -> captureAndSaveReceipt());
        // Lưu chi tiết (thêm mới nếu chưa có; nếu đã có trong table -> báo user dùng Sửa)
        btnLuu.addActionListener(e -> {
            try {
                // đọc input
                int idSP = Integer.parseInt(txtID.getText().trim());
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
                int giaNhap = Integer.parseInt(txtGiaNhap.getText().trim());
                int thanhTien = soLuong * giaNhap;
                txtThanhTien.setText(String.valueOf(thanhTien));

                // kiểm tra xem chi tiết này đã tồn tại trong DB/table chưa
                boolean existed = false;
                for (int i=0; i<model.getRowCount(); i++) {
                    int rowMaPN = Integer.parseInt(model.getValueAt(i,0).toString());
                    int rowID   = Integer.parseInt(model.getValueAt(i,1).toString());
                    if (rowMaPN == maPN && rowID == idSP) {
                        existed = true;
                        break;
                    }
                }

                if (existed) {
                    // Nếu đã có, để tránh user hiểu lầm "thêm mới",
                    // ta có thể yêu cầu họ bấm nút Sửa thay vì tự động ghi đè.
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Sản phẩm này đã có trong phiếu. Bạn muốn cập nhật (Sửa) thay vì thêm mới?",
                            "Xác nhận",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean ok = ctBus.updateDetail(maPN, idSP, soLuong, giaNhap);
                        if (!ok) {
                            JOptionPane.showMessageDialog(this,
                                    "Cập nhật thất bại! Kiểm tra lại dữ liệu.",
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        reloadTable();
                        clearInput();
                        JOptionPane.showMessageDialog(this, "Đã cập nhật chi tiết!");
                    }
                } else {
                    // thêm mới vào DB
                    boolean ok = ctBus.addDetail(maPN, idSP, soLuong, giaNhap);
                    if (!ok) {
                        // thường rơi vào đây nếu ID SP không tồn tại -> vi phạm FK
                        JOptionPane.showMessageDialog(this,
                                "Sản phẩm mới chưa có trong hệ thống.\n" +
                        "Vui lòng thêm sản phẩm vào hệ thống trước khi nhập hàng!!!",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    reloadTable();
                    clearInput();
                    JOptionPane.showMessageDialog(this, "Đã thêm chi tiết!");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập số hợp lệ cho ID SP / Số lượng / Giá nhập!",
                        "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi: " + ex.getMessage(),
                        "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Sửa: ép cập nhật dòng hiện tại
        btnSua.addActionListener(e -> {
            if (trangThaiPN == 1) {
                JOptionPane.showMessageDialog(this, "Phiếu đã được xuất (hoàn tất) và không thể thêm/sửa chi tiết.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
       
            try {
                int idSP = Integer.parseInt(txtID.getText().trim());
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
                int giaNhap = Integer.parseInt(txtGiaNhap.getText().trim());

                boolean ok = ctBus.updateDetail(maPN, idSP, soLuong, giaNhap);
                if (!ok) {
                    JOptionPane.showMessageDialog(this,
                            "Cập nhật thất bại! Kiểm tra lại ID SP đã tồn tại trong phiếu.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                reloadTable();
                clearInput();
                JOptionPane.showMessageDialog(this, "Đã cập nhật chi tiết!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập số hợp lệ!",
                        "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi: " + ex.getMessage(),
                        "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Xóa
        btnXoa.addActionListener(e -> {
            if (trangThaiPN == 1) {
                JOptionPane.showMessageDialog(this, "Phiếu đã được xuất (hoàn tất) và không thể xóa chi tiết.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int r = table.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Chọn 1 dòng trong bảng để xóa.");
                return;
            }
            int idSP = Integer.parseInt(model.getValueAt(r,1).toString());
            int opt = JOptionPane.showConfirmDialog(this,
                    "Xóa sản phẩm ID " + idSP + " khỏi phiếu?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (opt != JOptionPane.YES_OPTION) return;

            boolean ok = ctBus.deleteDetail(maPN, idSP);
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Xóa thất bại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            reloadTable();
            clearInput();
        });

        // Nút Làm mới
        btnLamMoi.addActionListener(e -> {
            clearInput();
            reloadTable();
        });

        
        /* ===== Lần đầu mở form: load dữ liệu từ DB ===== */
        reloadTable();
    }
    
    private void finalizePhieuNhap() {
        
        int opt = JOptionPane.showConfirmDialog(this, 
                "Sau khi xuất, bạn sẽ không thể sửa đổi (thêm/sửa/xóa) bất kỳ chi tiết nào của phiếu. Bạn có chắc chắn muốn xuất phiếu nhập này?", 
                "Xác nhận Xuất Phiếu", JOptionPane.YES_NO_OPTION);
        if (opt != JOptionPane.YES_OPTION) return;

        try {
            // 1. Cập nhật tổng tiền lần cuối
            boolean okTongTien = pnBus.updateTongTienFromDetail(maPN);
            if (!okTongTien) {
                JOptionPane.showMessageDialog(this, "Không cập nhật được tổng tiền phiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Cập nhật trạng thái Đã Xuất (1)
            boolean okStatus = pnBus.updateTrangThai(maPN, 1); // 1 = Đã Xuất

            if (!okStatus) {
                JOptionPane.showMessageDialog(this, "Không cập nhật được trạng thái phiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 3. Thông báo và khóa giao diện
            JOptionPane.showMessageDialog(this, "Phiếu nhập đã được xuất và hoàn tất!");
            setTrangThaiGiaoDien(true);
            setTitle("Chi tiết Phiếu Nhập - Mã: " + pnBus.toDisplayCode(maPN) + " (ĐÃ XUẤT)");
            
            int saveOpt = JOptionPane.showConfirmDialog(this, 
                "Bạn có muốn lưu Phiếu Nhập này dưới dạng hình ảnh (PNG) không?", 
                "Lưu Phiếu", JOptionPane.YES_NO_OPTION);
            
            //Lưu phiếu nhập
            if (saveOpt == JOptionPane.YES_OPTION) {
                captureAndSaveReceipt();}

        } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Lỗi khi xuất phiếu: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setTrangThaiGiaoDien(boolean isFinalized) {
        // Vô hiệu hóa các trường nhập liệu chi tiết
        txtID.setEditable(!isFinalized);
        txtSoLuong.setEditable(!isFinalized);
        txtGiaNhap.setEditable(!isFinalized);
        
        // Vô hiệu hóa các nút chỉnh sửa/thêm/xóa
        if (btnLuu != null) btnLuu.setEnabled(!isFinalized);
        if (btnSua != null) btnSua.setEnabled(!isFinalized);
        if (btnXoa != null) btnXoa.setEnabled(!isFinalized);

        // Cập nhật trạng thái nút Xuất
        if (btnXuat != null) {
            if (isFinalized) {
                btnXuat.setText("Đã Xuất");
                btnXuat.setEnabled(false);
            } else {
                btnXuat.setText("Xuất Phiếu");
                btnXuat.setEnabled(true);
            }
        }
    }
    // Load lại bảng chi tiết từ DB + cập nhật tổng tiền label
    private void reloadTable() {
        List<CTPhieuNhapDTO> ds = ctBus.getByMaPN(maPN);
        model.setRowCount(0);

        double tong = 0;
        for (CTPhieuNhapDTO ct : ds) {
            model.addRow(new Object[]{
                    ct.getMaPN(),
                    ct.getId(),
                    ct.getSoLuong(),
                    ct.getGiaNhap(),
                    ct.getThanhTien()
            });
            tong += ct.getThanhTien();
        }
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VNĐ", tong));
    }

    // clear input bên trái (trừ mã PN)
    private void clearInput() {
        txtID.setText("");
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtThanhTien.setText("");
    }

    // giống style nút bên form chính
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
    
    private int getTrangThaiPN() {
        
        try {
            DTO.PhieuNhapDTO pn = pnBus.getById(maPN);
            // GIẢ ĐỊNH bạn đã thêm getTrangThai() vào DTO
            return (pn != null) ? 1 : 0; // Giả định: 1 là Đã Xuất, 0 là Nháp
        } catch (Exception e) {
            return 0;
        }
    }
    
    private boolean doCapture(JComponent component, File file) {
        try {
            // Tạo BufferedImage với kích thước của component
            BufferedImage img = new BufferedImage(
                    component.getWidth(),
                    component.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            // Vẽ component lên BufferedImage
            Graphics g = img.getGraphics();
            component.paint(g); // Sử dụng paint để chụp lại toàn bộ component
            g.dispose();

            // Lưu ảnh ra file PNG
            ImageIO.write(img, "png", file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void captureAndSaveReceipt() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Phiếu Nhập dưới dạng ảnh");
        // Thiết lập thư mục mặc định là Desktop (tùy chọn)
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        // Đặt tên file mặc định
        String defaultFileName = "PN_" + pnBus.toDisplayCode(maPN) + "_" + 
                                 new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + ".png";
        fileChooser.setSelectedFile(new File(defaultFileName));

        // Thêm bộ lọc file (chỉ cho phép .png)
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Đảm bảo tên file kết thúc bằng .png
            if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
            }

            // Thực hiện chụp và lưu
            if (doCapture(this.getRootPane(), fileToSave)) {
                JOptionPane.showMessageDialog(this, 
                        "Phiếu nhập đã được lưu thành công tại:\n" + fileToSave.getAbsolutePath(), 
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lưu file thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
