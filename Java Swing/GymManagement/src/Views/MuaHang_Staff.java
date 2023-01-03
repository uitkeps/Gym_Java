/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import ConnectDB.ConnectionUtils;
import Processes.ClockThread;
import Processes.Payment;
import Processes.PaymentDetail;
import Processes.API;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nguyen Bao Anh
 */
public class MuaHang_Staff extends javax.swing.JFrame {

    String sdt = "";
    String cusId = "";
    String staffId = "";
    String staff_id = "";
    String paymentId = "";
    String productId = "";
    String paymentTotal = "";
    int sl = 0;
    DefaultTableModel model;
    ArrayList<String> listDiscountCode = new ArrayList<>();
    ArrayList<Integer> listDiscountId = new ArrayList<>();
    LocalDate date = LocalDate.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String ngay = date.format(dateTimeFormatter);

    /**
     * Creates new form PaymentForm
     *
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public MuaHang_Staff() throws ClassNotFoundException, SQLException {
        initComponents();
        init();
    }

    private void init() throws SQLException, ClassNotFoundException {
        setResizable(false);
        setLocationRelativeTo(null);
        model = (DefaultTableModel) tablePayment.getModel();
        getStaffId();
        loadDiscount();

        setCbxProductType();
        setCbxPaymentMode();

        txtAddress.setEditable(false);
        dcDate.setDate(new Date());
        txtPrice.setEditable(false);
        txtIntoMoney.setEditable(false);
        txtTongTien.setEditable(false);
        initClock();

        cbxPaymentMode.setEnabled(false);

        refresh();
    }

    private void initClock() {
        ClockThread ct = new ClockThread(lbClock, lbTime);
        ct.start();
    }

    private void Enable() {
        cbxProductType.setEnabled(true);
        cbxProductName.setEnabled(true);
        txtPrice.setEnabled(true);
        txtIntoMoney.setEnabled(true);
        txtAmount.setEnabled(true);
    }

    private void Disable() {
        cbxProductType.setEnabled(false);
        cbxProductName.setEnabled(false);
        txtPrice.setEnabled(false);
        txtIntoMoney.setEnabled(false);
        txtAmount.setEnabled(false);
    }

    private void refresh() {
        lbPaymentId.setText("");
        btnDeletePayment.setEnabled(false);
        btnAddProduct.setEnabled(false);
        refreshProduct();
        cbxPaymentMode.setSelectedIndex(0);
        txtDiscount.setText("");
        txtTongTien.setText("");

        model.setRowCount(0);

        btnSave.setEnabled(false);
        btnUpdateProduct.setEnabled(false);
        btnDeleteProduct.setEnabled(false);

        Disable();
    }

    private void refreshProduct() {
        cbxProductType.setSelectedIndex(0);
        cbxProductName.removeAllItems();
        txtPrice.setText("");
        txtAmount.setText("");
        txtIntoMoney.setText("");
        lbTinhTrang.setText("Tình trạng sản phẩm: ");
    }

    private void getPaymentId() {
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT MAX(payment_id) FROM payment";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                paymentId = rs.getString("MAX(payment_id)");
                lbPaymentId.setText(paymentId);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getStaffId() throws SQLException, ClassNotFoundException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src\\Staff.txt");
            int i = 0;
            while ((i = fileInputStream.read()) != -1) {
                staffId += (char) i;
            }
            if ("admin".equals(staffId)) {
                lbStaffId.setText("ADMIN");
            } else {
                lbStaffId.setText("0" + staffId);
            }

            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "SELECT staff_id FROM STAFF WHERE staff_telephone = '" + staffId + "' ";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                staff_id = rs.getString("staff_id");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getCustomer() {
        dcDate.setDate(new Date());
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sdt = txtTelephone.getText();

            String sql = "SELECT * FROM customer where cus_telephone = '" + sdt + "' ";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cusId = rs.getString("cus_id");
                txtName.setText(rs.getString("cus_name"));
                txtAddress.setText(rs.getString("cus_address"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setCbxProductType() {
        cbxProductType.removeAllItems();
        cbxProductType.addItem("");
        cbxProductType.addItem("Dụng cụ luyện tập");
        cbxProductType.addItem("Thực phẩm bổ sung");
    }

    private void setCbxProductName() {
        cbxProductName.removeAllItems();
        txtIntoMoney.setText("");
        txtAmount.setText("");
        txtPrice.setText("");
        cbxProductName.addItem("");
        String loaiSanPham = cbxProductType.getSelectedItem().toString();
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT DISTINCT product_name FROM product WHERE product_type = '" + loaiSanPham + "' ";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String s = rs.getString("product_name");
                cbxProductName.addItem(s);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTxtProductPrice() {
        txtIntoMoney.setText("");
        txtAmount.setText("");
        String tenSanPham = cbxProductName.getSelectedItem().toString();
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT * FROM product WHERE product_name = '" + tenSanPham + "' ";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                productId = rs.getString("product_id");
                String s = rs.getString("product_price");
                sl = rs.getInt("product_amount");
                txtPrice.setText(s);
                if (sl > 0) {
                    lbTinhTrang.setText("Tình trạng: CÒN " + sl + " SẢN PHẨM");
                    txtAmount.setEnabled(true);

                } else {
                    lbTinhTrang.setText("Tình trạng sản phẩm: HẾT HÀNG");
                    txtAmount.setEnabled(false);
                }

            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setCbxPaymentMode() {
        cbxPaymentMode.removeAllItems();
        cbxPaymentMode.addItem("");
        cbxPaymentMode.addItem("Tiền mặt");
        cbxPaymentMode.addItem("Thẻ tín dụng");
        cbxPaymentMode.addItem("Ví điện tử MoMo");
    }

    private void setTablePayment() {
        model.setRowCount(0);
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT product.product_id, product_name, product_type, amount, (product_price * amount) AS into_money FROM payment_detail, product "
                    + "WHERE payment_detail.product_id = product.product_id AND payment_id = " + paymentId + "";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String productId = rs.getString("product_id");
                String productName = rs.getString("product_name");
                String productType = rs.getString("product_type");
                String amount = rs.getString("amount");
                int intoMoney = rs.getInt("into_money");

                model.addRow(new Object[]{productId, productName, productType, amount, intoMoney});
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
        setPaymentTotal();
    }

    private void setPaymentTotal() {
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "SELECT * FROM payment where payment_id = " + paymentId + " ";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                paymentTotal = rs.getString("total");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtTongTien.setText(paymentTotal + " VNĐ");
    }

    private void loadDiscount() {
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT * FROM DISCOUNT";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                listDiscountId.add(Integer.valueOf(rs.getString("dis_id")));
                listDiscountCode.add(rs.getString("dis_code"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkNull() {
        if (cbxProductType.getSelectedIndex() == 0) {
            return false;
        }
        if (String.valueOf(cbxProductName.getSelectedItem().toString()).length() == 0) {
            return false;
        }
        return String.valueOf(txtAmount.getText()).length() != 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTelephone = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        dcDate = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePayment = new javax.swing.JTable();
        btnXuatHoaDon = new javax.swing.JButton();
        txtDiscount = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        btnAddDiscount = new javax.swing.JButton();
        lbStatus = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnAddProduct = new javax.swing.JButton();
        btnUpdateProduct = new javax.swing.JButton();
        btnDeleteProduct = new javax.swing.JButton();
        lbPaymentId = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnAddPayment = new javax.swing.JButton();
        btnDeletePayment = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        cbxPaymentMode = new javax.swing.JComboBox<>();
        lbTinhTrang = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cbxProductType = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cbxProductName = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtAmount = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtIntoMoney = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbClock = new javax.swing.JLabel();
        lbTime = new javax.swing.JLabel();
        lvMinimize = new javax.swing.JLabel();
        lbExit = new javax.swing.JLabel();
        lbTinhTrang1 = new javax.swing.JLabel();
        lbStaffId = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(134, 1, 4));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setForeground(new java.awt.Color(134, 1, 4));

        jLabel2.setBackground(new java.awt.Color(134, 1, 4));
        jLabel2.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tên khách hàng");

        txtName.setBackground(new java.awt.Color(134, 1, 4));
        txtName.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(255, 255, 255));

        txtTelephone.setBackground(new java.awt.Color(134, 1, 4));
        txtTelephone.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtTelephone.setForeground(new java.awt.Color(255, 255, 255));
        txtTelephone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelephoneKeyPressed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(134, 1, 4));
        jLabel3.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Số điện thoại");

        jLabel5.setBackground(new java.awt.Color(134, 1, 4));
        jLabel5.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Ngày đặt hàng");

        jLabel4.setBackground(new java.awt.Color(134, 1, 4));
        jLabel4.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Địa chỉ");

        txtAddress.setBackground(new java.awt.Color(134, 1, 4));
        txtAddress.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtAddress.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator1.setForeground(new java.awt.Color(134, 1, 4));

        jSeparator4.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        dcDate.setBackground(new java.awt.Color(134, 1, 4));
        dcDate.setForeground(new java.awt.Color(255, 255, 255));
        dcDate.setDateFormatString("dd/MM/yyyy");

        jLabel14.setBackground(new java.awt.Color(134, 1, 4));
        jLabel14.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("+84");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator4)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelephone)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dcDate, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 262, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 262, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel14))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(dcDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 96, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 96, Short.MAX_VALUE)))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        tablePayment.setBackground(new java.awt.Color(255, 255, 255));
        tablePayment.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        tablePayment.setForeground(new java.awt.Color(134, 1, 4));
        tablePayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Số lượng", "Thành tiền"
            }
        ));
        tablePayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePaymentMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePayment);

        btnXuatHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        btnXuatHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-billing-machine-80.png"))); // NOI18N
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        txtDiscount.setBackground(new java.awt.Color(255, 255, 255));
        txtDiscount.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        txtDiscount.setForeground(new java.awt.Color(134, 1, 4));

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(134, 1, 4));
        jLabel13.setText("Mã giảm giá");

        btnAddDiscount.setBackground(new java.awt.Color(255, 255, 255));
        btnAddDiscount.setForeground(new java.awt.Color(255, 255, 255));
        btnAddDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-voucher-30.png"))); // NOI18N
        btnAddDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDiscountActionPerformed(evt);
            }
        });

        lbStatus.setBackground(new java.awt.Color(255, 255, 255));
        lbStatus.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        lbStatus.setForeground(new java.awt.Color(134, 1, 4));
        lbStatus.setText("Trạng thái: ");

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 28)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(134, 1, 4));
        jLabel15.setText("TỔNG");

        txtTongTien.setBackground(new java.awt.Color(255, 255, 255));
        txtTongTien.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 28)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(134, 1, 4));
        txtTongTien.setBorder(null);
        txtTongTien.setCaretColor(new java.awt.Color(134, 1, 4));
        txtTongTien.setPreferredSize(new java.awt.Dimension(10, 33));
        txtTongTien.setSelectedTextColor(new java.awt.Color(255, 255, 255));

        jSeparator3.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator3.setForeground(new java.awt.Color(134, 1, 4));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAddDiscount)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXuatHoaDon)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnAddDiscount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                                .addComponent(txtTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbStatus)))
                    .addComponent(btnXuatHoaDon))
                .addGap(2, 2, 2))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(134, 1, 4));
        jLabel6.setText("Mã đơn hàng: ");

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-refresh-64.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnAddProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnAddProduct.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        btnAddProduct.setForeground(new java.awt.Color(134, 1, 4));
        btnAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-add-tag-64.png"))); // NOI18N
        btnAddProduct.setText("THÊM SẢN PHẨM");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        btnUpdateProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdateProduct.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        btnUpdateProduct.setForeground(new java.awt.Color(134, 1, 4));
        btnUpdateProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-edit-product-64.png"))); // NOI18N
        btnUpdateProduct.setText("SỬA SẢN PHẨM");
        btnUpdateProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateProductActionPerformed(evt);
            }
        });

        btnDeleteProduct.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        btnDeleteProduct.setForeground(new java.awt.Color(134, 1, 4));
        btnDeleteProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-remove-64.png"))); // NOI18N
        btnDeleteProduct.setText("XÓA SẢN PHẨM");
        btnDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProductActionPerformed(evt);
            }
        });

        lbPaymentId.setBackground(new java.awt.Color(255, 255, 255));
        lbPaymentId.setFont(new java.awt.Font("#9Slide03 Montserrat Bold", 0, 18)); // NOI18N
        lbPaymentId.setForeground(new java.awt.Color(134, 1, 4));
        lbPaymentId.setText("ABCD");

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-save-30.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnAddPayment.setBackground(new java.awt.Color(255, 255, 255));
        btnAddPayment.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        btnAddPayment.setForeground(new java.awt.Color(134, 1, 4));
        btnAddPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-order-64.png"))); // NOI18N
        btnAddPayment.setText("THÊM ĐƠN HÀNG");
        btnAddPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPaymentActionPerformed(evt);
            }
        });

        btnDeletePayment.setBackground(new java.awt.Color(255, 255, 255));
        btnDeletePayment.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        btnDeletePayment.setForeground(new java.awt.Color(134, 1, 4));
        btnDeletePayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-cancel-64.png"))); // NOI18N
        btnDeletePayment.setText("Xóa đơn hàng");
        btnDeletePayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePaymentActionPerformed(evt);
            }
        });

        jSeparator8.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator8.setForeground(new java.awt.Color(134, 1, 4));
        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel12.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(134, 1, 4));
        jLabel12.setText("Hình thức");

        cbxPaymentMode.setBackground(new java.awt.Color(255, 255, 255));
        cbxPaymentMode.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxPaymentMode.setForeground(new java.awt.Color(134, 1, 4));

        lbTinhTrang.setBackground(new java.awt.Color(255, 255, 255));
        lbTinhTrang.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        lbTinhTrang.setForeground(new java.awt.Color(134, 1, 4));
        lbTinhTrang.setText("Tình trạng sản phẩm: ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPaymentId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnRefresh)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddPayment, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDeletePayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxPaymentMode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddProduct)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateProduct)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteProduct))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(lbTinhTrang)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave)))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator8)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(lbPaymentId)
                        .addComponent(jLabel12)
                        .addComponent(cbxPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSave)
                    .addComponent(lbTinhTrang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRefresh)
                        .addComponent(btnDeletePayment)
                        .addComponent(btnAddPayment))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUpdateProduct)
                        .addComponent(btnDeleteProduct)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(134, 1, 4));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setBackground(new java.awt.Color(134, 1, 4));
        jLabel7.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Loại sản phẩm");

        cbxProductType.setBackground(new java.awt.Color(134, 1, 4));
        cbxProductType.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxProductType.setForeground(new java.awt.Color(255, 255, 255));
        cbxProductType.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxProductTypePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel8.setBackground(new java.awt.Color(134, 1, 4));
        jLabel8.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Sản phẩm");

        cbxProductName.setBackground(new java.awt.Color(134, 1, 4));
        cbxProductName.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxProductName.setForeground(new java.awt.Color(255, 255, 255));
        cbxProductName.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxProductNamePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel9.setBackground(new java.awt.Color(134, 1, 4));
        jLabel9.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Giá tiền");

        jLabel10.setBackground(new java.awt.Color(134, 1, 4));
        jLabel10.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Số lượng");

        txtAmount.setBackground(new java.awt.Color(134, 1, 4));
        txtAmount.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtAmount.setForeground(new java.awt.Color(255, 255, 255));
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
        });

        txtPrice.setBackground(new java.awt.Color(134, 1, 4));
        txtPrice.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtPrice.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setBackground(new java.awt.Color(134, 1, 4));
        jLabel11.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("THÀNH TIỀN");

        txtIntoMoney.setBackground(new java.awt.Color(134, 1, 4));
        txtIntoMoney.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        txtIntoMoney.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator6.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator7.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator7.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator9.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator9.setForeground(new java.awt.Color(255, 255, 255));

        jLabel17.setBackground(new java.awt.Color(134, 1, 4));
        jLabel17.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("VNĐ");

        jLabel18.setBackground(new java.awt.Color(134, 1, 4));
        jLabel18.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("VNĐ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator7)
            .addComponent(jSeparator6)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxProductType, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtPrice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxProductName, 0, 219, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAmount)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jSeparator9)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxProductType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(cbxProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAmount)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPrice)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17))
                .addGap(10, 10, 10)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(11, 11, 11)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("#9Slide03 Montserrat Black", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(134, 1, 4));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ĐƠN MUA HÀNG");

        lbClock.setFont(new java.awt.Font("#9Slide03 HelvetIns", 0, 14)); // NOI18N
        lbClock.setForeground(new java.awt.Color(134, 1, 4));
        lbClock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbClock.setText("giờ");

        lbTime.setFont(new java.awt.Font("#9Slide03 HelvetIns", 0, 14)); // NOI18N
        lbTime.setForeground(new java.awt.Color(134, 1, 4));
        lbTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTime.setText("Ngày");

        lvMinimize.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lvMinimize.setForeground(new java.awt.Color(134, 1, 4));
        lvMinimize.setText("-");
        lvMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lvMinimizeMouseClicked(evt);
            }
        });

        lbExit.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbExit.setForeground(new java.awt.Color(134, 1, 4));
        lbExit.setText("X");
        lbExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbExitMouseClicked(evt);
            }
        });

        lbTinhTrang1.setBackground(new java.awt.Color(255, 255, 255));
        lbTinhTrang1.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        lbTinhTrang1.setForeground(new java.awt.Color(134, 1, 4));
        lbTinhTrang1.setText("Nhân viên:");

        lbStaffId.setBackground(new java.awt.Color(255, 255, 255));
        lbStaffId.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        lbStaffId.setForeground(new java.awt.Color(134, 1, 4));
        lbStaffId.setText("123456");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbTime)
                    .addComponent(lbClock)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTinhTrang1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbStaffId)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(296, 296, 296)
                .addComponent(lvMinimize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbExit, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbClock, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbTinhTrang1)
                            .addComponent(lbStaffId)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lbExit, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        Enable();
        btnAddProduct.setEnabled(false);
        btnUpdateProduct.setEnabled(false);
        btnDeleteProduct.setEnabled(false);
        btnAddProduct.setEnabled(false);
        cbxPaymentMode.setEnabled(true);
        btnSave.setEnabled(true);
        refreshProduct();
    }//GEN-LAST:event_btnAddProductActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshProduct();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (!checkNull()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int amount = Integer.valueOf(txtAmount.getText());
        if (amount > sl) {
            JOptionPane.showMessageDialog(rootPane, "Vượt quá số lượng sản phẩm còn hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int i = PaymentDetail.addProduct(paymentId, productId, txtAmount.getText());
        if (i > 0) {
            Payment.addPaymentTotal(paymentId);
            setTablePayment();
            Disable();
            refreshProduct();
            btnAddProduct.setEnabled(true);
            btnSave.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Sản phẩm này đã có trong đơn hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void cbxProductNamePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxProductNamePopupMenuWillBecomeInvisible
        if (cbxProductName.getItemCount() != 0) {
            setTxtProductPrice();
        }
    }//GEN-LAST:event_cbxProductNamePopupMenuWillBecomeInvisible

    private void btnAddPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPaymentActionPerformed
        if (txtTelephone.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại của khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        getCustomer();
        if (cusId.equals("")) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int i = 0;
        if (staffId.equals("admin")) {
            i = Payment.addPayment(ngay, cusId);
        } else {
            i = Payment.addPayment(ngay, staff_id, cusId);

        }
        if (i > 0) {
            getPaymentId();
            System.out.println(paymentId);
            try {
                try ( FileWriter fw = new FileWriter("src\\PaymentDetail.txt")) {
                    fw.write(paymentId);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            btnAddPayment.setEnabled(false);
            btnDeletePayment.setEnabled(true);
            btnAddProduct.setEnabled(true);
            cbxPaymentMode.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Thêm hóa đơn thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddPaymentActionPerformed

    private void tablePaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePaymentMouseClicked
        int Click = tablePayment.getSelectedRow();
        productId = model.getValueAt(Click, 0).toString();
        cbxProductType.setSelectedItem(model.getValueAt(Click, 2).toString());
        setCbxProductName();
        cbxProductName.setSelectedItem(model.getValueAt(Click, 1).toString());
        setTxtProductPrice();
        txtAmount.setText(model.getValueAt(Click, 3).toString());
        txtIntoMoney.setText(model.getValueAt(Click, 4).toString());

        Enable();
        btnAddProduct.setEnabled(true);
        btnUpdateProduct.setEnabled(true);
        btnDeleteProduct.setEnabled(true);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_tablePaymentMouseClicked

    private void btnDeletePaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePaymentActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa đơn hàng này không?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            int i = Payment.deletePayment(paymentId);
            if (i > 0) {
                String s = "";
                try (
                         FileOutputStream output = new FileOutputStream("src\\PaymentDetail.txt")) {
                    output.write(s.getBytes());
                } catch (IOException ex) {
                    Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
                }
                paymentId = "";
                refresh();
                btnAddPayment.setEnabled(true);
                cbxPaymentMode.setEnabled(false);
                Disable();
                JOptionPane.showMessageDialog(this, "Xóa đơn hàng THÀNH CÔNG!");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa đơn hàng THẤT BẠI!");
            }
        }

    }//GEN-LAST:event_btnDeletePaymentActionPerformed

    private void btnUpdateProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateProductActionPerformed
        int Click = tablePayment.getSelectedRow();
        String productIdOld = model.getValueAt(Click, 0).toString();;
        String productIdNew = productId;
        String amount = txtAmount.getText();
        
        if (Integer.valueOf(amount) > sl) {
            JOptionPane.showMessageDialog(rootPane, "Vượt quá số lượng sản phẩm còn hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PaymentDetail.updateProduct(paymentId, productIdOld, productIdNew, amount);
        setTablePayment();

        refreshProduct();
        btnAddProduct.setEnabled(true);
        btnUpdateProduct.setEnabled(false);
        btnDeleteProduct.setEnabled(false);
        Disable();
    }//GEN-LAST:event_btnUpdateProductActionPerformed

    private void cbxProductTypePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxProductTypePopupMenuWillBecomeInvisible
        if (cbxProductType.getSelectedIndex() != 0) {
            setCbxProductName();
        }
    }//GEN-LAST:event_cbxProductTypePopupMenuWillBecomeInvisible

    private void btnDeleteProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProductActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa linh kiện này không?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            PaymentDetail.deleteProduct(paymentId, productId);
            setTablePayment();
            refreshProduct();
            btnAddProduct.setEnabled(true);
            btnUpdateProduct.setEnabled(false);
            btnDeleteProduct.setEnabled(false);
            Disable();
        }
    }//GEN-LAST:event_btnDeleteProductActionPerformed

    private void txtAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAmountKeyReleased
        long giaTien;
        int soLuong;
        if (!txtAmount.getText().equals("") && !txtPrice.getText().equals("")) {
            giaTien = Long.parseLong(txtPrice.getText());
            soLuong = Integer.valueOf(txtAmount.getText());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng sản phẩm không hợp lệ", "Lỗi số lượng", JOptionPane.ERROR_MESSAGE);
                txtAmount.setText("");
            } else {
                if (soLuong > sl) {
                    JOptionPane.showMessageDialog(rootPane, "Vượt quá số lượng sản phẩm còn hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                txtIntoMoney.setText(giaTien * soLuong + " VNĐ");
            }
        }

    }//GEN-LAST:event_txtAmountKeyReleased

    private void btnAddDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDiscountActionPerformed
        if (!txtDiscount.equals("")) {
            String discount = txtDiscount.getText();
            for (String s : listDiscountCode) {
                if (s.equals(discount)) {
                    Payment.addDiscount(paymentId, listDiscountId.get(listDiscountCode.indexOf(s)) + "");
                    lbStatus.setText("Trạng thái: Thêm mã giảm giá THÀNH CÔNG");
                    Payment.addPaymentTotal(paymentId);
                    setTablePayment();
                    refreshProduct();
                    return;
                }
            }
            lbStatus.setText("Trạng thái: Mã giảm giá KHÔNG HỢP LỆ");
        }
    }//GEN-LAST:event_btnAddDiscountActionPerformed

    private void lvMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lvMinimizeMouseClicked
        // TODO add your handling code here:
        setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lvMinimizeMouseClicked

    private void lbExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbExitMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_lbExitMouseClicked

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm trong đơn hàng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String paymentMode = cbxPaymentMode.getSelectedItem().toString();
        switch (paymentMode) {
            case "" ->
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hình thức thanh toán!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            case "Ví điện tử MoMo" -> {
                try {
                    API.TrangThanhToanMoMo(paymentId, paymentTotal);
                } catch (IOException ex) {
                    Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        Payment.addPaymentMode(paymentId, paymentMode);
        JOptionPane.showMessageDialog(this, "Tính năng đang bảo trì, vui lòng quay lại sau!", "Bảo Trì", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

    private void txtTelephoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelephoneKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                Connection conn = ConnectionUtils.getOracleConnection();

                String sdt = txtTelephone.getText();

                String sql = "SELECT * FROM customer WHERE cus_telephone = '" + sdt + "' ";

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    String address = rs.getString("cus_address");
                    txtAddress.setText(address);
                    String name = rs.getString("cus_name");
                    txtName.setText(name);
                    cusId = rs.getString("cus_id");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtTelephoneKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MuaHang_Staff.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MuaHang_Staff.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MuaHang_Staff.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MuaHang_Staff.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new MuaHang_Staff().setVisible(true);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(MuaHang_Staff.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDiscount;
    private javax.swing.JButton btnAddPayment;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnDeletePayment;
    private javax.swing.JButton btnDeleteProduct;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdateProduct;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JComboBox<String> cbxPaymentMode;
    private javax.swing.JComboBox<String> cbxProductName;
    private javax.swing.JComboBox<String> cbxProductType;
    private com.toedter.calendar.JDateChooser dcDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lbClock;
    private javax.swing.JLabel lbExit;
    private javax.swing.JLabel lbPaymentId;
    private javax.swing.JLabel lbStaffId;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lbTinhTrang;
    private javax.swing.JLabel lbTinhTrang1;
    private javax.swing.JLabel lvMinimize;
    private javax.swing.JTable tablePayment;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtIntoMoney;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtTelephone;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables

}
