/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import ConnectDB.ConnectionUtils;
import Processes.ClockThread;
import Processes.Membership;
import Processes.Payment;
import Processes.API;
import Processes.ReportView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Nguyen Bao Anh
 */
public class MembershipForm extends javax.swing.JFrame {

    String sdt = "";
    String cusId = "";
    String paymentId = "";
    String memId = "";
    ArrayList<String> listMemId = new ArrayList<>();
    LocalDate date = LocalDate.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String ngay = date.format(dateTimeFormatter);

    /**
     * Creates new form PaymentForm
     *
     * @throws java.lang.ClassNotFoundException
     */
    public MembershipForm() throws ClassNotFoundException {
        initComponents();
        init();
    }

    private void init() throws ClassNotFoundException {
        setTitle("Đăng ký khóa tập");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        txtName.setEditable(false);
        lbPaymentId.setText("");
        txtDate.setEditable(false);
        txtTelephone.setEditable(false);
        txtAddress.setEditable(false);
        cbxBonus.setEnabled(false);
        cbxPaymentMode.setEnabled(false);
        txtFee.setEnabled(false);
        btnDeletePayment.setEnabled(false);
        btnConfirm.setEnabled(false);
        txtDate.setEnabled(false);
        setCbxPaymentMode();
        setCbxMembership();
        getCustomerId();
        getCustomer();
        initClock();
    }

    private void initClock() {
        ClockThread ct = new ClockThread(lbClock, lbTime);
        ct.start();
    }

    private void setCbxPaymentMode() {
        cbxPaymentMode.removeAllItems();
        cbxPaymentMode.addItem("");
        cbxPaymentMode.addItem("Tiền mặt");
        cbxPaymentMode.addItem("Thẻ tín dụng");
        cbxPaymentMode.addItem("Ví điện tử MoMo");
    }

    private void getCustomerId() {
        try {
            FileInputStream fileInputStream = new FileInputStream("src\\Customer.txt");
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
            Logger.getLogger(MembershipForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MembershipForm.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MembershipForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getCustomer() {
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
            Logger.getLogger(MembershipForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setCbxMembership() {
        cbxBonus.addItem("");
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT * FROM membership";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cbxBonus.addItem(rs.getString("mem_bonus"));
                listMemId.add(rs.getString("mem_id"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MembershipForm.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        jSeparator2 = new javax.swing.JSeparator();
        txtDate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lbPaymentId = new javax.swing.JLabel();
        btnConfirm = new javax.swing.JButton();
        btnAddPayment = new javax.swing.JButton();
        btnDeletePayment = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        cbxPaymentMode = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtFee = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cbxBonus = new javax.swing.JComboBox<>();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbClock = new javax.swing.JLabel();
        lbTime = new javax.swing.JLabel();
        lvMinimize = new javax.swing.JLabel();
        lbExit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(134, 1, 4));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(134, 1, 4));
        jLabel2.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(134, 1, 4));
        jLabel2.setText("Tên khách hàng");

        txtName.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(134, 1, 4));
        txtName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtTelephone.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        txtTelephone.setForeground(new java.awt.Color(134, 1, 4));
        txtTelephone.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setBackground(new java.awt.Color(134, 1, 4));
        jLabel3.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(134, 1, 4));
        jLabel3.setText("Số điện thoại");

        jLabel5.setBackground(new java.awt.Color(134, 1, 4));
        jLabel5.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(134, 1, 4));
        jLabel5.setText("Ngày đăng ký");

        jLabel4.setBackground(new java.awt.Color(134, 1, 4));
        jLabel4.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(134, 1, 4));
        jLabel4.setText("Địa chỉ");

        txtAddress.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        txtAddress.setForeground(new java.awt.Color(134, 1, 4));
        txtAddress.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jSeparator1.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator1.setForeground(new java.awt.Color(134, 1, 4));

        jSeparator2.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator2.setForeground(new java.awt.Color(134, 1, 4));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(134, 1, 4));
        jLabel1.setText("+84");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAddress)
                    .addComponent(txtDate))
                .addContainerGap())
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(txtName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTelephone)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(134, 1, 4));

        jLabel6.setBackground(new java.awt.Color(134, 1, 4));
        jLabel6.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 255));
        jLabel6.setText("Mã đơn hàng: ");

        lbPaymentId.setBackground(new java.awt.Color(134, 1, 4));
        lbPaymentId.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 1, 18)); // NOI18N
        lbPaymentId.setForeground(new java.awt.Color(204, 204, 255));
        lbPaymentId.setText("ABCD");

        btnConfirm.setBackground(new java.awt.Color(255, 255, 255));
        btnConfirm.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 24)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(134, 1, 4));
        btnConfirm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-choose-50.png"))); // NOI18N
        btnConfirm.setText("XÁC NHẬN DỊCH VỤ");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
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

        cbxPaymentMode.setBackground(new java.awt.Color(255, 255, 255));
        cbxPaymentMode.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxPaymentMode.setForeground(new java.awt.Color(134, 1, 4));

        jLabel12.setBackground(new java.awt.Color(134, 1, 4));
        jLabel12.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Hình thức");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnAddPayment)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeletePayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPaymentId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(0, 4, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(cbxPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbPaymentId)
                                        .addComponent(jLabel6)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnDeletePayment)
                                    .addComponent(btnAddPayment)))
                            .addComponent(btnConfirm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(134, 1, 4));
        jLabel8.setText("Chi phí");

        txtFee.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        txtFee.setForeground(new java.awt.Color(134, 1, 4));
        txtFee.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFeeActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(134, 1, 4));
        jLabel9.setText("Đặc quyền");

        cbxBonus.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        cbxBonus.setForeground(new java.awt.Color(134, 1, 4));
        cbxBonus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cbxBonus.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxBonusPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jSeparator4.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator4.setForeground(new java.awt.Color(134, 1, 4));

        jSeparator5.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator5.setForeground(new java.awt.Color(134, 1, 4));

        jLabel10.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(134, 1, 4));
        jLabel10.setText("VNĐ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxBonus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtFee, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jSeparator4)
            .addComponent(jSeparator5)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbxBonus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("#9Slide03 Montserrat Black", 0, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("ĐĂNG KÝ DỊCH VỤ");
        jLabel7.setAlignmentY(0.0F);

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
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbClock)
                            .addComponent(lbTime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(159, 159, 159)
                        .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbExit)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbClock, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbTime))
                            .addComponent(lbExit)
                            .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LayHoaDonMem(String s) throws SQLException, JRException, ClassNotFoundException {
        HashMap hs = new HashMap();
        hs.put("parameter1", s);
        String localDir = System.getProperty("user.dir");

        ReportView viewer = new ReportView(localDir + "/src/Report/report6_2_1.jrxml", hs);
        viewer.setVisible(false);
    }

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        memId = listMemId.get(cbxBonus.getSelectedIndex() - 1);
        int i = Membership.addMembership(paymentId, cusId, memId, ngay);
        String paymentMode = cbxPaymentMode.getSelectedItem().toString();
        if (i > 0) {
            if (paymentMode.equals("")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hình thức thanh toán!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            } else if (paymentMode.equals("Ví điện tử MoMo")) {
                try {
                    int btn = JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng ký dịch vụ này không?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (btn == JOptionPane.YES_OPTION) {
                        Payment.addPaymentTotal(paymentId);
                        try {
                            API.TrangThanhToanMoMo(paymentId, txtFee.getText());
                        } catch (IOException ex) {
                            Logger.getLogger(MembershipForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(rootPane, "Đăng ký dịch vụ THÀNH CÔNG");
                        Payment.addPaymentMode(paymentId, paymentMode);
                        LayHoaDonMem(lbPaymentId.getText());
                        JOptionPane.showMessageDialog(rootPane, "Vui lòng LÀM MỚI trạng thái sau khi đăng ký", "Thông báo", JOptionPane.CANCEL_OPTION);
                        this.dispose();
                    } else if (btn == JOptionPane.NO_OPTION) {
                        remove(this);
                    }
                } catch (SQLException | JRException | ClassNotFoundException ex) {
                    Logger.getLogger(MembershipForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                int btn = JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng ký dịch vụ này không?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (btn == JOptionPane.YES_OPTION) {
                    try {
                        Payment.addPaymentTotal(paymentId);
                        JOptionPane.showMessageDialog(rootPane, "Đăng ký dịch vụ THÀNH CÔNG");
                        Payment.addPaymentMode(paymentId, paymentMode);
                        LayHoaDonMem(lbPaymentId.getText());
                        JOptionPane.showMessageDialog(rootPane, "Vui lòng LÀM MỚI trạng thái sau khi đăng ký", "Thông báo", JOptionPane.CANCEL_OPTION);
                        this.dispose();
                    } catch (SQLException | JRException | ClassNotFoundException ex) {
                        Logger.getLogger(MembershipForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (btn == JOptionPane.NO_OPTION) {
                    remove(this);
                }
            }
        }
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int Click = JOptionPane.showConfirmDialog(null, "Bạn Có Muốn Thoát Khỏi Chương Trình Hay Không?", "Thông Báo", 2);
        if (Click == JOptionPane.OK_OPTION) {
            String s = "";
            try (
                     FileOutputStream output = new FileOutputStream("src\\Membership.txt")) {
                output.write(s.getBytes());
                this.dispose();

            } catch (IOException ex) {
                Logger.getLogger(MuaHang.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);
        } else {
            if (Click == JOptionPane.CANCEL_OPTION) {
                this.setVisible(true);
            }
        }
    }//GEN-LAST:event_formWindowClosing

    private void cbxBonusPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxBonusPopupMenuWillBecomeInvisible
        txtFee.setText("");
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String dacQuyen = cbxBonus.getSelectedItem().toString();

            String sql = "SELECT * FROM membership WHERE mem_bonus = '" + dacQuyen + "' ";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                txtFee.setText(rs.getString("mem_fee"));

            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MembershipForm.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbxBonusPopupMenuWillBecomeInvisible

    private void btnAddPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPaymentActionPerformed
        cbxPaymentMode.setEnabled(true);
        txtDate.setEnabled(true);
        btnAddPayment.setEnabled(false);
        btnDeletePayment.setEnabled(true);
        cbxBonus.setEnabled(true);
        txtFee.setEnabled(true);
        txtFee.setEditable(false);
        btnConfirm.setEnabled(true);
        txtDate.setText(ngay);

        int them = Payment.addPayment(ngay, cusId);
        if (them > 0) {
            getPaymentId();
            try {
                try ( FileWriter fw = new FileWriter("src\\SignUpMembership.txt")) {
                    fw.write(paymentId);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không thể thêm", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddPaymentActionPerformed

    private void btnDeletePaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePaymentActionPerformed
        lbPaymentId.setText("");
        btnAddPayment.setEnabled(true);
        cbxPaymentMode.setEnabled(false);
        cbxPaymentMode.setSelectedIndex(0);
        cbxBonus.setSelectedIndex(0);
        txtDate.setEnabled(false);
        btnDeletePayment.setEnabled(false);
        txtFee.setText("");
        Payment.deletePayment(paymentId);
        String s = "";
        try (
                 FileOutputStream output = new FileOutputStream("src\\SignUpClass.txt")) {
            output.write(s.getBytes());

        } catch (IOException ex) {
            Logger.getLogger(MuaHang.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDeletePaymentActionPerformed

    private void lvMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lvMinimizeMouseClicked
        // TODO add your handling code here:
        setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lvMinimizeMouseClicked

    private void lbExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbExitMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_lbExitMouseClicked

    private void txtFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFeeActionPerformed

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
            java.util.logging.Logger.getLogger(MembershipForm.class
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
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new MembershipForm().setVisible(true);

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MembershipForm.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddPayment;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnDeletePayment;
    private javax.swing.JComboBox<String> cbxBonus;
    private javax.swing.JComboBox<String> cbxPaymentMode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lbClock;
    private javax.swing.JLabel lbExit;
    private javax.swing.JLabel lbPaymentId;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lvMinimize;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtFee;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTelephone;
    // End of variables declaration//GEN-END:variables

}
