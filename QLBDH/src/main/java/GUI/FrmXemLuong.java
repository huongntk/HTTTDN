package GUI;

import BUS.NhanVienBUS;
import DTO.NhanVienDTO;
import BUS.BangLuongBUS;
import DTO.BangLuongDTO;
import DTO.PhanQuyen;
import UTIL.Auth;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FrmXemLuong extends JFrame {
    private int maNV;
    private PhanQuyen phanQuyen;
    private BangLuongBUS bangLuongBUS;
    private JTable tblLuong;
    private DefaultTableModel model;
    private JComboBox<Integer> cbThang, cbNam;
    private JButton btnXem, btnIn, btnClose;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public FrmXemLuong(int maNV, PhanQuyen phanQuyen) {
        this.maNV = maNV;
        this.phanQuyen = phanQuyen;
        this.bangLuongBUS = new BangLuongBUS();
        initComponents();
        loadData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Xem bảng lương");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel lọc
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFilter.add(new JLabel("Tháng:"));
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);
        pnlFilter.add(cbThang);

        pnlFilter.add(new JLabel("Năm:"));
        cbNam = new JComboBox<>();
        for (int i = 2023; i <= 2030; i++) cbNam.addItem(i);
        pnlFilter.add(cbNam);

        btnXem = new JButton("Xem");
        btnXem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search.png")));
        btnXem.addActionListener(e -> loadData());
        pnlFilter.add(btnXem);

        btnIn = new JButton("In bảng lương");
        btnIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/print.png")));
        btnIn.addActionListener(e -> inBangLuong());
        if (!phanQuyen.isNsInBangLuong()) btnIn.setEnabled(false);
        pnlFilter.add(btnIn);
        
        JButton btnCachTinh = new JButton("Cách tính lương");
        btnCachTinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/caculator.png")));
        btnCachTinh.addActionListener(e -> hienThiCachTinh());
        pnlFilter.add(btnCachTinh);

        JButton btnInNam = new JButton("In lương năm");
        btnInNam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/print.png")));
        btnInNam.addActionListener(e -> inLuongNam());
        pnlFilter.add(btnInNam);                                                                                                

        btnClose = new JButton("Đóng");
        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/close.png")));
        btnClose.addActionListener(e -> dispose());
        pnlFilter.add(btnClose);

        add(pnlFilter, BorderLayout.NORTH);

        // Bảng hiển thị lương
        model = new DefaultTableModel(new String[]{"Tháng", "Năm", "Lương cơ bản", "Phụ cấp", "Thưởng", "Phạt", "Tổng lương"}, 0);
        tblLuong = new JTable(model);
        tblLuong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLuong.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(tblLuong), BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        int thang = (int) cbThang.getSelectedItem();
        int nam = (int) cbNam.getSelectedItem();
        // Lấy bảng lương của nhân viên theo tháng (nếu có)
        BangLuongDTO bl = bangLuongBUS.getByMaNVAndMonth(maNV, thang, nam);
        if (bl != null) {
            model.addRow(new Object[]{
                bl.getThang(), bl.getNam(),
                currencyFormat.format(bl.getLuongCoBan()),
                currencyFormat.format(bl.getPhuCap()),
                currencyFormat.format(bl.getThuong()),
                currencyFormat.format(bl.getPhat()),
                currencyFormat.format(bl.getTongLuong())
            });
        }
    }

    private void inBangLuong() {
        if (tblLuong.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để in.");
            return;
        }

        try {
            int thang = (int) cbThang.getSelectedItem();
            int nam = (int) cbNam.getSelectedItem();

            NhanVienDTO nv = new NhanVienBUS().layNhanVienTheoMa(maNV);
            String tenNV = nv.getHo() + " " + nv.getTen();
            
            MessageFormat header = new MessageFormat(
                    "BẢNG LƯƠNG - " + tenNV + " - THÁNG " + thang + "/" + nam
            );

            MessageFormat footer = new MessageFormat("Trang {0}");

            tblLuong.print(JTable.PrintMode.FIT_WIDTH, header, footer);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi in: " + e.getMessage());
        }
    }
//    private void inBangLuong() {
//        // In trực tiếp bảng lương hoặc gọi FrmInBangLuong
//        new FrmInBangLuong(maNV).setVisible(true);
//    }
    
    private void hienThiCachTinh() {
        String msg = "CÁCH TÍNH LƯƠNG (áp dụng từ tháng có hỗ trợ thay đổi chức vụ):\n\n"
                + "1. Lương cơ bản & Phụ cấp:\n"
                + "   - Lấy từ bảng Lương Cơ Bản Theo Chức Vụ (LuongCoBanTheoChucVu).\n"
                + "   - Mỗi chức vụ có một mức lương cơ bản và phụ cấp riêng.\n\n"
                + "2. Công chuẩn:\n"
                + "   - Tổng số công theo lịch làm việc (SUM SoCong của ca) trong tháng.\n"
                + "   - Nếu nhân viên thay đổi chức vụ trong tháng, công chuẩn được tính riêng\n"
                + "     cho từng khoảng thời gian giữ chức vụ đó.\n\n"
                + "3. Công thực tế:\n"
                + "   - Tổng số công từ bảng Chấm Công (ChamCong) join với CaLam.\n"
                + "   - Chỉ những ngày có lịch làm việc và đã chấm công mới được tính.\n\n"
                + "4. Công thức tính lương:\n"
                + "   - Đối với mỗi khoảng chức vụ:\n"
                + "     Lương = (Lương cơ bản / Công chuẩn của khoảng) × Công thực tế\n"
                + "     Phụ cấp = (Phụ cấp chức vụ / Công chuẩn của khoảng) × Công thực tế\n"
                + "   - Sau khi tính hết các khoảng, cộng thêm Thưởng và trừ Phạt (nhập tay).\n\n"
                + "5. Thưởng / Phạt:\n"
                + "   - Được nhập thủ công khi tính lương hoặc sửa sau.\n"
                + "   - Ảnh hưởng trực tiếp vào Tổng lương thực lĩnh.\n\n"
                + "=> Tổng lương = (Tổng lương các khoảng) + Thưởng - Phạt\n\n"
                + "Ghi chú: Nếu không có lịch làm việc, công chuẩn = 0, nhân viên sẽ không được\n"
                + "tính lương cho những ngày đó dù có chấm công (cần xếp lịch trước).";

        JTextArea textArea = new JTextArea(msg);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(UIManager.getColor("Panel.background"));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(550, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Cách tính lương", JOptionPane.INFORMATION_MESSAGE);
    }
    private void inLuongNam() {
        int nam = Integer.parseInt(
                JOptionPane.showInputDialog(this, "Nhập năm:", cbNam.getSelectedItem())
        );

        // chỉ truyền năm, bỏ tháng
        new FrmInBangLuongNam(maNV, nam).setVisible(true);
    }
}