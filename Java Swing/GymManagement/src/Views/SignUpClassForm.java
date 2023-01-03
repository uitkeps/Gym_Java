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
import Processes.SignUpClass;
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
import java.util.Date;
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
public class SignUpClassForm extends javax.swing.JFrame {

    String sdt = "";
    String cusId = "";
    String paymentId = "";
    String classId = "";
    String paymentTotal = "";
    int slHV;
    DefaultTableModel model;
    ArrayList<String> listDiscountCode = new ArrayList<>();
    ArrayList<Integer> listDiscountId = new ArrayList<>();
    LocalDate date = LocalDate.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String ngay = date.format(dateTimeFormatter);
    private String roomId;

    /**
     * Creates new form PaymentForm
     */
    public SignUpClassForm() {
        initComponents();
        init();

    }

    private void init() {
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        model = (DefaultTableModel) tableClass.getModel();
        getCustomerId();
        getCustomer();
        loadDiscount();
        setCbxClassTitle();
        setCbxPaymentMode();

        txtName.setEditable(false);
        txtDate.setEditable(false);
        txtTelephone.setEditable(false);
        txtAddress.setEditable(false);
        txtClassCost.setEditable(false);
        txtTongTien.setEditable(false);
        cbxPaymentMode.setEnabled(false);
        initClock();

        refresh();

    }

    private void initClock() {
        ClockThread ct = new ClockThread(lbClock, lbTime);
        ct.start();
    }

    private void Enable() {
        cbxClassTitle.setEnabled(true);
        cbxClassPeriod.setEnabled(true);
        cbxClassDate.setEnabled(true);
        txtClassCost.setEnabled(true);
    }

    private void Disable() {
        cbxClassTitle.setEnabled(false);
        cbxClassPeriod.setEnabled(false);
        cbxClassDate.setEnabled(false);
        txtClassCost.setEnabled(false);
    }

    private void refresh() {
        lbPaymentId.setText("");
        btnDeletePayment.setEnabled(false);
        btnAddClass.setEnabled(false);
        cbxClassTitle.setSelectedIndex(0);
        cbxClassPeriod.removeAllItems();
        cbxPaymentMode.setSelectedIndex(0);
        txtClassCost.setText("");
        txtTongTien.setText("");

        model.setRowCount(0);

        btnSave.setEnabled(false);
        btnDeleteClass.setEnabled(false);

        Disable();
    }

    private void refreshClass() {
        cbxClassTitle.setSelectedIndex(0);
        cbxClassPeriod.removeAllItems();
        cbxClassDate.removeAllItems();
        txtClassCost.setText("");
        lbTinhTrang.setText("Tình trạng lớp:");
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
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCustomer() {
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
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCbxClassTitle() {
        cbxClassTitle.removeAllItems();
        cbxClassTitle.addItem("");
        cbxClassDate.removeAllItems();
        cbxClassDate.addItem("");
        cbxClassPeriod.removeAllItems();
        cbxClassPeriod.addItem("");
        txtClassCost.setText("");
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT DISTINCT class_title FROM class";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String s = rs.getString("class_title");
                cbxClassTitle.addItem(s);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCbxClassDate() {
        cbxClassDate.removeAllItems();
        cbxClassDate.addItem("");
        cbxClassPeriod.removeAllItems();
        cbxClassPeriod.addItem("");
        txtClassCost.setText("");
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String tenKhoaTap = cbxClassTitle.getSelectedItem().toString();

            String sql = "SELECT DISTINCT class_date FROM class WHERE class_title = '" + tenKhoaTap + "' ";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Date dNgay = rs.getDate("class_date");
                String s = String.format("%1$td/%1$tm/%1$tY", dNgay);
                cbxClassDate.addItem(s);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClassForm.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCbxClassPeriod() {
        cbxClassPeriod.removeAllItems();
        cbxClassPeriod.addItem("");
        txtClassCost.setText("");
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String tenKhoaTap = cbxClassTitle.getSelectedItem().toString();
            String sNgay = cbxClassDate.getSelectedItem().toString();

            String sql = "SELECT * FROM class, room WHERE class.room_id = room.room_id AND "
                    + "class_title = '" + tenKhoaTap + "' AND class_date = '" + sNgay + "' ";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String s = rs.getString("class_period");
                cbxClassPeriod.addItem(s);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setTxtClassCost() {
        txtClassCost.setText("");
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String tenKhoaTap = cbxClassTitle.getSelectedItem().toString();
            String ngayTap = cbxClassDate.getSelectedItem().toString();
            String gioTap = cbxClassPeriod.getSelectedItem().toString();
            String sql = "SELECT * FROM class WHERE class_title = '" + tenKhoaTap
                    + "' AND class_date = '" + ngayTap + "' AND class_period = '"
                    + gioTap + "' ";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                classId = rs.getString("class_id");
                String s = rs.getString("class_cost");
                txtClassCost.setText(s);
                roomId = rs.getString("room_id");
            }

            String sql_1 = "SELECT COUNT(*) AS SLHV FROM signup_class "
                    + "WHERE class_id = " + classId + " GROUP BY class_id";
            rs = st.executeQuery(sql_1);
            int slhv = 0;
            while (rs.next()) {
                slhv = rs.getInt("SLHV");
            }

            String sql_2 = "SELECT * FROM room WHERE room_id = " + roomId + " ";
            rs = st.executeQuery(sql_2);
            int sucChua = 0;
            while (rs.next()) {
                sucChua = rs.getInt("room_capacity");
            }

            int conTrong = sucChua - slhv;
            lbTinhTrang.setText("Tình trạng lớp: Sỉ số tổng: " + sucChua
                    + "; Đã Đăng Ký: " + slhv + " học viên " + "; Còn trống: "
                    + conTrong + " chỗ");
            System.out.println("class id: " + classId);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCbxPaymentMode() {
        cbxPaymentMode.removeAllItems();
        cbxPaymentMode.addItem("");
        cbxPaymentMode.addItem("Tiền mặt");
        cbxPaymentMode.addItem("Thẻ tín dụng");
        cbxPaymentMode.addItem("Ví điện tử MoMo");
    }

    private void setTableClass() {
        model.setRowCount(0);
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            int i = Connection.TRANSACTION_SERIALIZABLE;
            if (i > 0) {
                String sql = "SELECT CLASS.class_id, class_title, class_date, "
                        + "class_period, class_cost AS COST, room_id FROM CLASS,"
                        + "SIGNUP_CLASS WHERE CLASS.class_id = SIGNUP_CLASS.class_id "
                        + "AND payment_id = " + paymentId;

                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    String class_Id = rs.getString("class_id");
                    String classTitle = rs.getString("class_title");
                    Date dNgay = rs.getDate("class_date");
                    String sNgay = String.format("%1$td/%1$tm/%1$tY", dNgay);
                    String classPeriod = rs.getString("class_period");
                    int classCost = rs.getInt("COST");
                    String room_Id = rs.getString("room_id");
                    model.addRow(new Object[]{
                        class_Id, classTitle, classPeriod, sNgay, classCost, room_Id
                    });
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
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
        if (cbxClassTitle.getSelectedIndex() == 0) {
            return false;
        }
        if (String.valueOf(cbxClassDate.getSelectedItem().toString()).length() == 0) {
            return false;
        }
        if (String.valueOf(cbxClassPeriod.getSelectedItem().toString()).length() == 0) {
            return false;
        }
        return String.valueOf(txtClassCost.getText()).length() != 0;
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
        txtDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        btnAddClass = new javax.swing.JButton();
        btnDeleteClass = new javax.swing.JButton();
        lbPaymentId = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnAddPayment = new javax.swing.JButton();
        btnDeletePayment = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableClass = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btnXuatHoaDon = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JTextField();
        lbStatus = new javax.swing.JLabel();
        btnAddDiscount = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        cbxPaymentMode = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        lbTinhTrang = new javax.swing.JLabel();
        btnInformation = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cbxClassTitle = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cbxClassPeriod = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtClassCost = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cbxClassDate = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        lvMinimize = new javax.swing.JLabel();
        lbExit = new javax.swing.JLabel();
        lbTime = new javax.swing.JLabel();
        lbClock = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(134, 1, 4));
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(134, 1, 4));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setForeground(new java.awt.Color(134, 1, 4));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(134, 1, 4));
        jLabel2.setText("Tên khách hàng");

        txtName.setBackground(new java.awt.Color(255, 255, 255));
        txtName.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtName.setForeground(new java.awt.Color(134, 1, 4));

        txtTelephone.setBackground(new java.awt.Color(255, 255, 255));
        txtTelephone.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtTelephone.setForeground(new java.awt.Color(134, 1, 4));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(134, 1, 4));
        jLabel3.setText("Số điện thoại");

        txtDate.setBackground(new java.awt.Color(255, 255, 255));
        txtDate.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtDate.setForeground(new java.awt.Color(134, 1, 4));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(134, 1, 4));
        jLabel5.setText("Ngày đăng ký");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(134, 1, 4));
        jLabel4.setText("Địa chỉ");

        txtAddress.setBackground(new java.awt.Color(255, 255, 255));
        txtAddress.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtAddress.setForeground(new java.awt.Color(134, 1, 4));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDate, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(txtAddress))
                .addGap(49, 49, 49))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelephone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(134, 1, 4));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(134, 1, 4));
        jLabel6.setFont(new java.awt.Font("#9Slide03 Roboto", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 255));
        jLabel6.setText("Mã đơn hàng: ");

        btnRefresh.setBackground(new java.awt.Color(255, 255, 255));
        btnRefresh.setFont(new java.awt.Font("#9Slide03 Roboto", 0, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(134, 1, 4));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-refresh-30.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnAddClass.setBackground(new java.awt.Color(255, 255, 255));
        btnAddClass.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        btnAddClass.setForeground(new java.awt.Color(134, 1, 4));
        btnAddClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-add-tag-64.png"))); // NOI18N
        btnAddClass.setText("THÊM KHÓA TẬP");
        btnAddClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddClassActionPerformed(evt);
            }
        });

        btnDeleteClass.setBackground(new java.awt.Color(255, 255, 255));
        btnDeleteClass.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        btnDeleteClass.setForeground(new java.awt.Color(134, 1, 4));
        btnDeleteClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-remove-64.png"))); // NOI18N
        btnDeleteClass.setText("XÓA KHÓA TẬP");
        btnDeleteClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteClassActionPerformed(evt);
            }
        });

        lbPaymentId.setBackground(new java.awt.Color(134, 1, 4));
        lbPaymentId.setFont(new java.awt.Font("#9Slide03 Montserrat Black", 1, 18)); // NOI18N
        lbPaymentId.setForeground(new java.awt.Color(204, 204, 255));
        lbPaymentId.setText("ABCD");

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
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

        jPanel3.setBackground(new java.awt.Color(134, 1, 4));
        jPanel3.setForeground(new java.awt.Color(134, 1, 4));

        tableClass.setBackground(new java.awt.Color(255, 255, 255));
        tableClass.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        tableClass.setForeground(new java.awt.Color(134, 1, 4));
        tableClass.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã khóa tập", "Tên khóa tập", "Giờ tập", "Ngày", "Giá tiền ", "Phòng tập"
            }
        ));
        tableClass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClassMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableClass);

        jLabel15.setBackground(new java.awt.Color(134, 1, 4));
        jLabel15.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 28)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("TỔNG");

        txtTongTien.setBackground(new java.awt.Color(255, 255, 255));
        txtTongTien.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 28)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(134, 1, 4));
        txtTongTien.setBorder(null);
        txtTongTien.setCaretColor(new java.awt.Color(134, 1, 4));
        txtTongTien.setPreferredSize(new java.awt.Dimension(10, 33));
        txtTongTien.setSelectedTextColor(new java.awt.Color(255, 255, 255));

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        btnXuatHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        btnXuatHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-billing-machine-80.png"))); // NOI18N
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(134, 1, 4));
        jLabel10.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Mã giảm giá");

        txtDiscount.setBackground(new java.awt.Color(255, 255, 255));
        txtDiscount.setForeground(new java.awt.Color(255, 0, 0));

        lbStatus.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 18)); // NOI18N
        lbStatus.setForeground(new java.awt.Color(255, 255, 255));
        lbStatus.setText("Trạng thái: ");

        btnAddDiscount.setBackground(new java.awt.Color(255, 255, 255));
        btnAddDiscount.setForeground(new java.awt.Color(255, 255, 255));
        btnAddDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-voucher-30.png"))); // NOI18N
        btnAddDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDiscountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtDiscount))
                    .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnAddDiscount)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtDiscount)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(17, 17, 17)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lbStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jSeparator6.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        cbxPaymentMode.setBackground(new java.awt.Color(255, 255, 255));
        cbxPaymentMode.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxPaymentMode.setForeground(new java.awt.Color(134, 1, 4));

        jLabel12.setBackground(new java.awt.Color(134, 1, 4));
        jLabel12.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Hình thức");

        lbTinhTrang.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        lbTinhTrang.setForeground(new java.awt.Color(255, 255, 255));
        lbTinhTrang.setText("Tình trạng lớp:");

        btnInformation.setBackground(new java.awt.Color(255, 255, 255));
        btnInformation.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        btnInformation.setForeground(new java.awt.Color(134, 1, 4));
        btnInformation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-information-50.png"))); // NOI18N
        btnInformation.setText("THÔNG TIN LỚP");
        btnInformation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInformationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddPayment))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPaymentId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDeletePayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxPaymentMode, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnAddClass)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteClass)
                        .addGap(18, 18, 18)
                        .addComponent(btnInformation)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbTinhTrang)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave)))
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(lbPaymentId)
                                        .addComponent(cbxPaymentMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                    .addComponent(lbTinhTrang))))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnRefresh, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddClass)
                                        .addComponent(btnDeleteClass)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnDeletePayment)
                                    .addComponent(btnAddPayment)))
                            .addComponent(btnInformation)))
                    .addComponent(jSeparator6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(134, 1, 4));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Tên khóa tập");

        cbxClassTitle.setBackground(new java.awt.Color(255, 255, 255));
        cbxClassTitle.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxClassTitle.setForeground(new java.awt.Color(134, 1, 4));
        cbxClassTitle.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxClassTitlePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(134, 1, 4));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("Giờ tập");

        cbxClassPeriod.setBackground(new java.awt.Color(255, 255, 255));
        cbxClassPeriod.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxClassPeriod.setForeground(new java.awt.Color(134, 1, 4));
        cbxClassPeriod.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxClassPeriodPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(134, 1, 4));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setText("Giá tiền");

        txtClassCost.setBackground(new java.awt.Color(255, 255, 255));
        txtClassCost.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtClassCost.setForeground(new java.awt.Color(134, 1, 4));

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(134, 1, 4));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel13.setText("Ngày");

        cbxClassDate.setBackground(new java.awt.Color(255, 255, 255));
        cbxClassDate.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        cbxClassDate.setForeground(new java.awt.Color(134, 1, 4));
        cbxClassDate.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cbxClassDatePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxClassPeriod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxClassTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtClassCost, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxClassDate, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxClassDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(cbxClassTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(cbxClassPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(txtClassCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("#9Slide03 Montserrat Black", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ĐĂNG KÝ KHÓA TẬP");

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

        lbTime.setBackground(new java.awt.Color(134, 1, 4));
        lbTime.setFont(new java.awt.Font("#9Slide03 HelvetIns", 0, 14)); // NOI18N
        lbTime.setForeground(new java.awt.Color(255, 255, 255));
        lbTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTime.setText("Ngày");

        lbClock.setBackground(new java.awt.Color(134, 1, 4));
        lbClock.setFont(new java.awt.Font("#9Slide03 HelvetIns", 0, 14)); // NOI18N
        lbClock.setForeground(new java.awt.Color(255, 255, 255));
        lbClock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbClock.setText("giờ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbClock)
                    .addComponent(lbTime))
                .addGap(205, 205, 205)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbExit)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbExit)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbClock, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbTime))
                    .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddClassActionPerformed
        Enable();
        btnAddClass.setEnabled(false);
        btnDeleteClass.setEnabled(false);
        btnAddClass.setEnabled(false);
        btnSave.setEnabled(true);
        refreshClass();
    }//GEN-LAST:event_btnAddClassActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshClass();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (!checkNull()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            String classPeriod = cbxClassPeriod.getSelectedItem().toString();
            String classDate = cbxClassDate.getSelectedItem().toString();
            boolean check = SignUpClass.checkDangKy(paymentId, cusId, classPeriod, classDate);
            if (!check) {
                int i;
                i = SignUpClass.addClass(paymentId, cusId, classId, ngay);
                if (i > 0) {
                    Payment.addPaymentTotal(paymentId);
                    setTableClass();
                    refreshClass();
                    Disable();
                    btnAddClass.setEnabled(true);
                    btnSave.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Khóa tập này đã có trong đơn hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Đã đăng ký khóa tập trong thời gian này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }


    }//GEN-LAST:event_btnSaveActionPerformed

    private void cbxClassPeriodPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxClassPeriodPopupMenuWillBecomeInvisible
        if (cbxClassPeriod.getItemCount() != 0) {
            setTxtClassCost();
        }
    }//GEN-LAST:event_cbxClassPeriodPopupMenuWillBecomeInvisible

    private void btnAddPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPaymentActionPerformed
        int them = Payment.addPayment(ngay, cusId);
        if (them > 0) {
            getPaymentId();
            try {
                try ( FileWriter fw = new FileWriter("src\\SignUpClass.txt")) {
                    fw.write(paymentId);
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            btnAddPayment.setEnabled(false);
            btnDeletePayment.setEnabled(true);
            btnAddClass.setEnabled(true);
            cbxPaymentMode.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Không thể thêm", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAddPaymentActionPerformed

    private void tableClassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableClassMouseClicked
        int Click = tableClass.getSelectedRow();
        classId = model.getValueAt(Click, 0).toString();
        cbxClassTitle.setSelectedItem(model.getValueAt(Click, 1).toString());
        setCbxClassDate();
        cbxClassDate.setSelectedItem(model.getValueAt(Click, 3).toString());
        setCbxClassPeriod();
        cbxClassPeriod.setSelectedItem(model.getValueAt(Click, 2).toString());
        txtClassCost.setText(model.getValueAt(Click, 4).toString());

        Enable();
        btnAddClass.setEnabled(true);
        btnDeleteClass.setEnabled(true);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_tableClassMouseClicked

    private void btnDeletePaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePaymentActionPerformed

        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa đơn hàng này không?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            int i = Payment.deletePayment(paymentId);
            if (i > 0) {
                String s = "";
                try (
                         FileOutputStream output = new FileOutputStream("src\\SignUpClass.txt")) {
                    output.write(s.getBytes());
                } catch (IOException ex) {
                    Logger.getLogger(MuaHang.class.getName()).log(Level.SEVERE, null, ex);
                }
                paymentId = "";
                refresh();
                btnAddPayment.setEnabled(true);
                cbxPaymentMode.setEnabled(false);
                txtDiscount.setText("");
                Disable();
                JOptionPane.showMessageDialog(this, "Xóa đơn hàng THÀNH CÔNG!");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa đơn hàng THẤT BẠI!");

            }
        }

    }//GEN-LAST:event_btnDeletePaymentActionPerformed

    private void cbxClassTitlePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxClassTitlePopupMenuWillBecomeInvisible
        if (cbxClassTitle.getSelectedIndex() != 0) {
            setCbxClassDate();
        }
    }//GEN-LAST:event_cbxClassTitlePopupMenuWillBecomeInvisible

    private void btnDeleteClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteClassActionPerformed
        int Click = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa khóa tập này không?", "Thông Báo", 2);
        if (Click == JOptionPane.YES_OPTION) {
            SignUpClass.deleteClass(paymentId, cusId, classId);
            setTableClass();
            refreshClass();
            btnAddClass.setEnabled(true);
            btnDeleteClass.setEnabled(false);
            Disable();
        }
    }//GEN-LAST:event_btnDeleteClassActionPerformed

    private void cbxClassDatePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cbxClassDatePopupMenuWillBecomeInvisible
        if (cbxClassDate.getItemCount() == 0) {
            return;
        }
        if (cbxClassDate.getSelectedIndex() != 0) {
            setCbxClassPeriod();
        }

    }//GEN-LAST:event_cbxClassDatePopupMenuWillBecomeInvisible

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
            JOptionPane.showMessageDialog(this, "Không có khóa tập trong đơn hàng!", "Lỗi", JOptionPane.WARNING_MESSAGE);
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
            default -> {
            }
        }
        Payment.addPaymentMode(paymentId, paymentMode);
        JOptionPane.showMessageDialog(this, "Tính năng đang bảo trì, vui lòng quay lại sau!", "Bảo Trì", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

    private void btnAddDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDiscountActionPerformed
        if (!txtDiscount.equals("")) {
            String discount = txtDiscount.getText();
            for (String s : listDiscountCode) {
                if (s.equals(discount)) {
                    Payment.addDiscount(paymentId, listDiscountId.get(listDiscountCode.indexOf(s)) + "");
                    lbStatus.setText("Trạng thái: Thêm mã giảm giá THÀNH CÔNG");
                    Payment.addPaymentTotal(paymentId);
                    setPaymentTotal();
                    return;
                }
            }
            lbStatus.setText("Trạng thái: Mã giảm giá KHÔNG HỢP LỆ");
        }
    }//GEN-LAST:event_btnAddDiscountActionPerformed

    private void ReportClass(String s) throws JRException, SQLException, ClassNotFoundException {
        String MaLop = s;
        HashMap hs = new HashMap();
        hs.put("class_id", MaLop);
        String localDir = System.getProperty("user.dir");

        ReportView viewer = new ReportView(localDir + "\\src\\Report\\report1.jrxml", hs);
        viewer.setVisible(true);
    }

    private void btnInformationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInformationActionPerformed
        try {
            // TODO add your handling code here:
            ReportClass(classId);
        } catch (JRException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClassForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnInformationActionPerformed

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
            java.util.logging.Logger.getLogger(SignUpClassForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignUpClassForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignUpClassForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignUpClassForm.class
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
            new SignUpClassForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddClass;
    private javax.swing.JButton btnAddDiscount;
    private javax.swing.JButton btnAddPayment;
    private javax.swing.JButton btnDeleteClass;
    private javax.swing.JButton btnDeletePayment;
    private javax.swing.JButton btnInformation;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JComboBox<String> cbxClassDate;
    private javax.swing.JComboBox<String> cbxClassPeriod;
    private javax.swing.JComboBox<String> cbxClassTitle;
    private javax.swing.JComboBox<String> cbxPaymentMode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lbClock;
    private javax.swing.JLabel lbExit;
    private javax.swing.JLabel lbPaymentId;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lbTinhTrang;
    private javax.swing.JLabel lvMinimize;
    private javax.swing.JTable tableClass;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtClassCost;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtTelephone;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables

}
