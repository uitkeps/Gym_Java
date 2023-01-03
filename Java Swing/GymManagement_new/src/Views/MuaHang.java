/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import ConnectDB.ConnectionUtils;
import Processes.ClockThread;
import Processes.Payment;
import Processes.PaymentDetail;
import Processes.ReportView;
import Processes.API;
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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Nguyen Bao Anh
 */
public class MuaHang extends javax.swing.JFrame {

    String sdt = "";
    String cusId = "";
    String paymentId = "";
    String productId = "";
    String paymentTotal = "";
    int sl;
    DefaultTableModel model;
    ArrayList<String> listDiscountCode = new ArrayList<>();
    ArrayList<Integer> listDiscountId = new ArrayList<>();
    LocalDate date = LocalDate.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String ngay = date.format(dateTimeFormatter);

    /**
     * Creates new form PaymentForm
     */
    public MuaHang() {
        initComponents();
        init();
    }

    private void init() {
        setResizable(false);
        setLocationRelativeTo(null);
        model = (DefaultTableModel) tablePayment.getModel();

        getCustomerId();
        setCustomer();
        loadDiscount();
        setCbxProductType();
        setCbxPaymentMode();

        tablePayment.setDefaultEditor(Object.class, null);
        txtName.setEditable(false);
        txtDate.setEditable(false);
        txtTelephone.setEditable(false);
        txtAddress.setEditable(false);
        txtPrice.setEditable(false);
        txtIntoMoney.setEditable(false);
        txtTongTien.setEditable(false);

        cbxPaymentMode.setEnabled(false);

        refresh();
        initClock();
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

    private void getCustomerId() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src\\Customer.txt");
            int i = 0;
            while ((i = fileInputStream.read()) != -1) {
                sdt += (char) i;
            }
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT * FROM customer WHERE cus_telephone='" + sdt + "'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cusId = rs.getString("cus_id");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setCustomer() {
        txtDate.setText(ngay);
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            txtTelephone.setText(sdt);

            String sql = "SELECT cus_name, cus_address FROM customer where cus_telephone = '" + sdt + "' ";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                txtName.setText(rs.getString("cus_name"));
                txtAddress.setText(rs.getString("cus_address"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
        }
        setPaymentTotal();
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
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtTelephone = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
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
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnAddProduct = new javax.swing.JButton();
        btnDeleteProduct = new javax.swing.JButton();
        lbPaymentId = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnAddPayment = new javax.swing.JButton();
        btnDeletePayment = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
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
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lbClock = new javax.swing.JLabel();
        lbTime = new javax.swing.JLabel();
        lvMinimize = new javax.swing.JLabel();
        lbExit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(134, 1, 4));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("#9Slide03 Montserrat Black", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MUA SẢN PHẨM");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(134, 1, 4));
        jLabel2.setText("Tên khách hàng");

        txtName.setBackground(new java.awt.Color(255, 255, 255));
        txtName.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(134, 1, 4));
        txtName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtTelephone.setBackground(new java.awt.Color(255, 255, 255));
        txtTelephone.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtTelephone.setForeground(new java.awt.Color(134, 1, 4));
        txtTelephone.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(134, 1, 4));
        jLabel3.setText("Số điện thoại");

        txtDate.setBackground(new java.awt.Color(255, 255, 255));
        txtDate.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtDate.setForeground(new java.awt.Color(134, 1, 4));
        txtDate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(134, 1, 4));
        jLabel5.setText("Ngày đặt hàng");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(134, 1, 4));
        jLabel4.setText("Địa chỉ");

        txtAddress.setBackground(new java.awt.Color(255, 255, 255));
        txtAddress.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtAddress.setForeground(new java.awt.Color(134, 1, 4));
        txtAddress.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jSeparator5.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator5.setForeground(new java.awt.Color(134, 1, 4));

        jLabel14.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(134, 1, 4));
        jLabel14.setText("+84");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtAddress)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel3.setBackground(new java.awt.Color(134, 1, 4));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        tablePayment.setBackground(new java.awt.Color(134, 1, 4));
        tablePayment.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        tablePayment.setForeground(new java.awt.Color(255, 255, 255));
        tablePayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Số lượng", "Thành tiền"
            }
        ));
        tablePayment.setShowGrid(true);
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
        txtDiscount.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N

        jLabel13.setBackground(new java.awt.Color(134, 1, 4));
        jLabel13.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Mã giảm giá");

        btnAddDiscount.setBackground(new java.awt.Color(255, 255, 255));
        btnAddDiscount.setForeground(new java.awt.Color(255, 255, 255));
        btnAddDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-voucher-30.png"))); // NOI18N
        btnAddDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDiscountActionPerformed(evt);
            }
        });

        lbStatus.setBackground(new java.awt.Color(134, 1, 4));
        lbStatus.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        lbStatus.setForeground(new java.awt.Color(255, 255, 255));
        lbStatus.setText("Trạng thái: ");

        jLabel15.setBackground(new java.awt.Color(134, 1, 4));
        jLabel15.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 28)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("TỔNG");

        txtTongTien.setBackground(new java.awt.Color(134, 1, 4));
        txtTongTien.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 28)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(255, 255, 255));
        txtTongTien.setBorder(null);
        txtTongTien.setCaretColor(new java.awt.Color(134, 1, 4));
        txtTongTien.setPreferredSize(new java.awt.Dimension(10, 33));
        txtTongTien.setSelectedTextColor(new java.awt.Color(255, 255, 255));
        txtTongTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTongTienActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAddDiscount)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(549, 549, 549)))
                        .addComponent(btnXuatHoaDon)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnXuatHoaDon)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnAddDiscount, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDiscount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addComponent(lbStatus))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(txtTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(134, 1, 4));

        jLabel6.setBackground(new java.awt.Color(134, 1, 4));
        jLabel6.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 255));
        jLabel6.setText("Mã đơn hàng: ");

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setFont(new java.awt.Font("#9Slide03 Roboto", 0, 14)); // NOI18N
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

        btnDeleteProduct.setBackground(new java.awt.Color(255, 255, 255));
        btnDeleteProduct.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        btnDeleteProduct.setForeground(new java.awt.Color(134, 1, 4));
        btnDeleteProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-remove-64.png"))); // NOI18N
        btnDeleteProduct.setText("XÓA SẢN PHẨM");
        btnDeleteProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProductActionPerformed(evt);
            }
        });

        lbPaymentId.setBackground(new java.awt.Color(134, 1, 4));
        lbPaymentId.setFont(new java.awt.Font("#9Slide03 Montserrat Black", 1, 18)); // NOI18N
        lbPaymentId.setForeground(new java.awt.Color(204, 204, 255));
        lbPaymentId.setText("ABCD");

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setFont(new java.awt.Font("#9Slide03 Roboto", 0, 14)); // NOI18N
        btnSave.setForeground(new java.awt.Color(134, 1, 4));
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
        btnDeletePayment.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        btnDeletePayment.setForeground(new java.awt.Color(134, 1, 4));
        btnDeletePayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-cancel-64.png"))); // NOI18N
        btnDeletePayment.setText("XÓA ĐƠN HÀNG");
        btnDeletePayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePaymentActionPerformed(evt);
            }
        });

        jSeparator6.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel12.setBackground(new java.awt.Color(134, 1, 4));
        jLabel12.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Hình thức");

        cbxPaymentMode.setBackground(new java.awt.Color(255, 255, 255));
        cbxPaymentMode.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxPaymentMode.setForeground(new java.awt.Color(134, 1, 4));

        lbTinhTrang.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        lbTinhTrang.setForeground(new java.awt.Color(255, 255, 255));
        lbTinhTrang.setText("Tình trạng sản phẩm:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnRefresh)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddPayment)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPaymentId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDeletePayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxPaymentMode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnAddProduct)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteProduct)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbTinhTrang, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                        .addGap(72, 72, 72)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lbPaymentId)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12)
                                    .addComponent(cbxPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnDeletePayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnSave)
                                    .addComponent(lbTinhTrang))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAddPayment, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddProduct)
                                        .addComponent(btnDeleteProduct)))))))
                .addGap(0, 0, 0))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setForeground(new java.awt.Color(134, 1, 4));
        jPanel5.setPreferredSize(new java.awt.Dimension(539, 113));
        jPanel5.setRequestFocusEnabled(false);

        jLabel7.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(134, 1, 4));
        jLabel7.setText("Loại sản phẩm");

        cbxProductType.setBackground(new java.awt.Color(255, 255, 255));
        cbxProductType.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxProductType.setForeground(new java.awt.Color(134, 1, 4));
        cbxProductType.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cbxProductType.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxProductTypePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cbxProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxProductTypeActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(134, 1, 4));
        jLabel8.setText("Sản phẩm");

        cbxProductName.setBackground(new java.awt.Color(255, 255, 255));
        cbxProductName.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxProductName.setForeground(new java.awt.Color(134, 1, 4));
        cbxProductName.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxProductNamePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel9.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(134, 1, 4));
        jLabel9.setText("Giá tiền");

        jLabel10.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(134, 1, 4));
        jLabel10.setText("Số lượng");

        txtAmount.setBackground(new java.awt.Color(255, 255, 255));
        txtAmount.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtAmount.setForeground(new java.awt.Color(134, 1, 4));
        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAmountKeyReleased(evt);
            }
        });

        txtPrice.setBackground(new java.awt.Color(255, 255, 255));
        txtPrice.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtPrice.setForeground(new java.awt.Color(134, 1, 4));

        jLabel11.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(134, 1, 4));
        jLabel11.setText("THÀNH TIỀN");

        txtIntoMoney.setBackground(new java.awt.Color(255, 255, 255));
        txtIntoMoney.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        txtIntoMoney.setForeground(new java.awt.Color(134, 1, 4));

        jSeparator3.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator3.setForeground(new java.awt.Color(134, 1, 4));

        jSeparator4.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator4.setForeground(new java.awt.Color(134, 1, 4));

        jLabel16.setBackground(new java.awt.Color(221, 229, 232));
        jLabel16.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(134, 1, 4));
        jLabel16.setText("VNĐ");

        jLabel17.setBackground(new java.awt.Color(221, 229, 232));
        jLabel17.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(134, 1, 4));
        jLabel17.setText("VNĐ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator3)
            .addComponent(jSeparator4)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 170, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(txtIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addGap(175, 175, 175))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxProductType, 0, 168, Short.MAX_VALUE)
                            .addComponent(txtAmount))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxProductName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrice)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel16)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cbxProductType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxProductName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIntoMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4))
        );

        lbClock.setBackground(new java.awt.Color(134, 1, 4));
        lbClock.setFont(new java.awt.Font("#9Slide03 HelvetIns", 0, 14)); // NOI18N
        lbClock.setForeground(new java.awt.Color(255, 255, 255));
        lbClock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbClock.setText("giờ");

        lbTime.setBackground(new java.awt.Color(134, 1, 4));
        lbTime.setFont(new java.awt.Font("#9Slide03 HelvetIns", 0, 14)); // NOI18N
        lbTime.setForeground(new java.awt.Color(255, 255, 255));
        lbTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTime.setText("Ngày");

        lvMinimize.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lvMinimize.setForeground(new java.awt.Color(255, 255, 255));
        lvMinimize.setText("-");
        lvMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lvMinimizeMouseClicked(evt);
            }
        });

        lbExit.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbExit.setForeground(new java.awt.Color(255, 255, 255));
        lbExit.setText("X");
        lbExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbExitMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbClock)
                    .addComponent(lbTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(332, 332, 332)
                .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbExit)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbClock, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbTime))
                    .addComponent(lbExit)
                    .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        btnDeleteProduct.setEnabled(false);
        btnAddProduct.setEnabled(false);
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
        btnAddPayment.setEnabled(false);
        btnDeletePayment.setEnabled(true);
        btnAddProduct.setEnabled(true);
        cbxPaymentMode.setEnabled(true);
        int themPayment = Payment.addPayment(ngay, cusId);
        if (themPayment > 0) {
            getPaymentId();
            try {
                try ( FileWriter fw = new FileWriter("src\\PaymentDetail.txt")) {
                    fw.write(paymentId);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không thể thêm hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                    Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
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
        }
        btnAddProduct.setEnabled(true);
        btnDeleteProduct.setEnabled(false);
        Disable();
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
        if (!txtDiscount.getText().equals("")) {
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

    private void cbxProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxProductTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxProductTypeActionPerformed

    private void XuatHoaDonNoDiscount(String s) throws JRException, SQLException, ClassNotFoundException {
        HashMap hs = new HashMap();
        hs.put("parameter1", s);
        String localDir = System.getProperty("user.dir");

        ReportView viewer = new ReportView(localDir + "/src/Report/report3.jrxml", hs);
        viewer.setVisible(false);
    }

    private void XuatHoaDonDiscount(String s) throws JRException, SQLException, ClassNotFoundException {
        HashMap hs = new HashMap();
        hs.put("parameter1", s);
        String localDir = System.getProperty("user.dir");

        ReportView viewer = new ReportView(localDir + "/src/Report/report3_1.jrxml", hs);
        viewer.setVisible(false);
    }

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
        try {
            Payment.addPaymentMode(paymentId, paymentMode);
            if (lbStatus.getText().equals("Trạng thái: Thêm mã giảm giá THÀNH CÔNG")) {
                XuatHoaDonDiscount(paymentId);
            } else {
                XuatHoaDonNoDiscount(paymentId);
            }
        } catch (JRException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

    private void txtTongTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongTienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongTienActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MuaHang.class
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
            new MuaHang().setVisible(true);
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
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JComboBox<String> cbxPaymentMode;
    private javax.swing.JComboBox<String> cbxProductName;
    private javax.swing.JComboBox<String> cbxProductType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lbClock;
    private javax.swing.JLabel lbExit;
    private javax.swing.JLabel lbPaymentId;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lbTinhTrang;
    private javax.swing.JLabel lvMinimize;
    private javax.swing.JTable tablePayment;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtIntoMoney;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtTelephone;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables

}
