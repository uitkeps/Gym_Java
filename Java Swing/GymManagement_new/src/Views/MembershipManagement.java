/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Views;

import ConnectDB.ConnectionUtils;
import Processes.Membership;
import Processes.ClockThread;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Hindu
 */
public class MembershipManagement extends javax.swing.JFrame {

    DefaultTableModel modelMembership = null;
    String maDichVu;

    /**
     * Creates new form MembershipManagement
     *
     * @throws java.lang.ClassNotFoundException
     */
    public MembershipManagement() throws ClassNotFoundException {
        initComponents();
        init();
    }

    private void init() throws ClassNotFoundException {
        setResizable(false);
        this.setLocationRelativeTo(null);
        modelMembership = (DefaultTableModel) tableMembership.getModel();
        setTableMembership();
        initClock();
        resetClass();
    }

    private void resetClass() {
        tableMembership.setDefaultEditor(Object.class, null);
        txtSearch.setText("SEARCH");
        buttonGroup1.clearSelection();
        txtBonus.setText("");
        txtCost.setText("");
    }

    private void initClock() {
        ClockThread ct = new ClockThread(lbClock, lbTime);
        ct.start();
    }

    private void getRowClass(int Click) {
        Click = tableMembership.getSelectedRow();
        maDichVu = (String) tableMembership.getValueAt(Click, 0);
        String hang = (String) tableMembership.getValueAt(Click, 1);
        switch (hang) {
            case "Đồng" ->
                radBtnBronze.setSelected(true);
            case "Bạc" ->
                radBtnSilver.setSelected(true);
            case "Vàng" ->
                radBtnGold.setSelected(true);
            default -> {
            }
        }
        txtBonus.setText((String) tableMembership.getValueAt(Click, 2));
        txtCost.setText((String) tableMembership.getValueAt(Click, 3));
    }

    private void setTableMembership() throws ClassNotFoundException {
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT * FROM membership ORDER BY mem_id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            modelMembership.setRowCount(0);
            while (rs.next()) {
                maDichVu = rs.getString("mem_id");
                String rank = rs.getString("mem_level");
                String bonus = rs.getString("mem_bonus");
                String cost = rs.getString("mem_fee");
                modelMembership.addRow(new Object[]{
                    maDichVu, rank, bonus, cost
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkNull() {
        if (String.valueOf(radBtnBronze.getText()).length() == 0 || (radBtnSilver.getText()).length() == 0 || (radBtnGold.getText()).length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (String.valueOf(txtBonus.getText()).length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (String.valueOf(txtCost.getText()).length() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnReset = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtCost = new javax.swing.JTextField();
        radBtnBronze = new javax.swing.JRadioButton();
        radBtnSilver = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        radBtnGold = new javax.swing.JRadioButton();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtBonus = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMembership = new javax.swing.JTable();
        lbTime = new javax.swing.JLabel();
        lbClock = new javax.swing.JLabel();
        lvMinimize = new javax.swing.JLabel();
        lbExit = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(134, 1, 4));
        jLabel1.setText("Hạng");

        btnReset.setBackground(new java.awt.Color(255, 255, 255));
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-refresh-30.png"))); // NOI18N
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(134, 1, 4));
        jLabel17.setText("Dịch vụ sử dụng");

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(134, 1, 4));
        jLabel18.setText("Phí đăng ký");

        btnThem.setBackground(new java.awt.Color(255, 255, 255));
        btnThem.setFont(new java.awt.Font("#9Slide03 Montserrat Black", 0, 24)); // NOI18N
        btnThem.setForeground(new java.awt.Color(134, 1, 4));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-add-30 (1).png"))); // NOI18N
        btnThem.setText("THÊM DỊCH VỤ");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("#9Slide03 Montserrat Black", 1, 32)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(134, 1, 4));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("THÔNG TIN DỊCH VỤ");

        txtCost.setBackground(new java.awt.Color(255, 255, 255));
        txtCost.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtCost.setForeground(new java.awt.Color(134, 1, 4));
        txtCost.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        radBtnBronze.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radBtnBronze);
        radBtnBronze.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        radBtnBronze.setForeground(new java.awt.Color(134, 1, 4));
        radBtnBronze.setText("Đồng");

        radBtnSilver.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radBtnSilver);
        radBtnSilver.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        radBtnSilver.setForeground(new java.awt.Color(134, 1, 4));
        radBtnSilver.setText("Bạc");

        jSeparator1.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator1.setForeground(new java.awt.Color(134, 1, 4));

        jSeparator2.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator2.setForeground(new java.awt.Color(134, 1, 4));

        jSeparator3.setBackground(new java.awt.Color(134, 1, 4));
        jSeparator3.setForeground(new java.awt.Color(134, 1, 4));

        btnDelete.setBackground(new java.awt.Color(255, 255, 255));
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-delete-32.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(255, 255, 255));
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/icons8-edit-30.png"))); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        radBtnGold.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radBtnGold);
        radBtnGold.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        radBtnGold.setForeground(new java.awt.Color(134, 1, 4));
        radBtnGold.setText("Vàng");

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(134, 1, 4));
        jLabel20.setText("VNĐ");

        txtBonus.setBackground(new java.awt.Color(255, 255, 255));
        txtBonus.setColumns(20);
        txtBonus.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        txtBonus.setForeground(new java.awt.Color(134, 1, 4));
        txtBonus.setRows(5);
        txtBonus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane2.setViewportView(txtBonus);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnReset)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDelete)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate))
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator3)
                    .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(radBtnBronze)
                        .addGap(18, 18, 18)
                        .addComponent(radBtnSilver)
                        .addGap(18, 18, 18)
                        .addComponent(radBtnGold))
                    .addComponent(jLabel17)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(txtCost)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(radBtnBronze)
                    .addComponent(radBtnSilver)
                    .addComponent(radBtnGold))
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(134, 1, 4));
        jPanel2.setForeground(new java.awt.Color(134, 1, 4));

        tableMembership.setBackground(new java.awt.Color(134, 1, 4));
        tableMembership.setFont(new java.awt.Font("#9Slide03 Roboto Condensed", 0, 14)); // NOI18N
        tableMembership.setForeground(new java.awt.Color(255, 255, 255));
        tableMembership.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã", "Hạng", "Dịch vụ sử dụng", "Phí"
            }
        ));
        tableMembership.setShowGrid(true);
        tableMembership.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMembershipMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableMembership);

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

        lvMinimize.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        lvMinimize.setForeground(new java.awt.Color(221, 229, 232));
        lvMinimize.setText("-");
        lvMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lvMinimizeMouseClicked(evt);
            }
        });

        lbExit.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbExit.setForeground(new java.awt.Color(221, 229, 232));
        lbExit.setText("X");
        lbExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbExitMouseClicked(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(134, 1, 4));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/ic_search_white_18dp.png"))); // NOI18N

        txtSearch.setBackground(new java.awt.Color(134, 1, 4));
        txtSearch.setFont(new java.awt.Font("#9Slide03 Roboto Condensed Bold", 0, 36)); // NOI18N
        txtSearch.setForeground(new java.awt.Color(255, 255, 255));
        txtSearch.setText("Search");
        txtSearch.setBorder(null);
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchFocusGained(evt);
            }
        });
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });

        jSeparator8.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator8.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbExit)
                        .addGap(8, 8, 8))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbTime)
                            .addComponent(lbClock)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbExit)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearch)
                    .addComponent(lvMinimize, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addComponent(lbClock, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTime)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        resetClass();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        if (checkNull()) {
            try {
                String rank = null;
                if (radBtnBronze.isSelected()) {
                    rank = radBtnBronze.getText();
                } else if (radBtnSilver.isSelected()) {
                    rank = radBtnSilver.getText();
                } else if (radBtnGold.isSelected()) {
                    rank = radBtnGold.getText();
                }
                String tenDichVu = txtBonus.getText();
                String giaTien = txtCost.getText();

                Object[] options = {"Có", "Không", "Hủy"};
                int result = JOptionPane.showOptionDialog(rootPane,
                        "Bạn có chắc muốn thêm dịch vụ " + tenDichVu + " hay không?",
                        "Thêm dịch vụ", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (result == JOptionPane.YES_OPTION) {
                    int themDV = Membership.addMembershipforStaff(rank, tenDichVu, giaTien);
                    if (themDV > 0) {
                        modelMembership.setRowCount(0);
                        setTableMembership();
                        JOptionPane.showMessageDialog(this, "Thêm Dịch vụ THÀNH CÔNG!");
                        resetClass();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm Dịch vụ THẤT BẠI!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        resetClass();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Hủy thao tác thêm Dịch vụ!");
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClassManagement.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Thêm Dịch vụ THẤT BẠI!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            String tenDichVu = txtBonus.getText();
            Object[] options = {"Có", "Không", "Hủy"};
            int result = JOptionPane.showOptionDialog(rootPane,
                    "Bạn có chắc muốn xóa dịch vụ " + tenDichVu + " hay không?",
                    "Xóa dịch vụ", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (result == JOptionPane.YES_OPTION) {
                int xoaMem = Membership.deleteMembership(maDichVu);
                if (xoaMem > 0) {
                    modelMembership.setRowCount(0);
                    setTableMembership();
                    JOptionPane.showMessageDialog(this, "Xóa Dịch vụ THÀNH CÔNG!");
                    resetClass();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa Dịch vụ THẤT BẠI!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    resetClass();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Hủy thao tác xóa Dịch vụ!");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClassManagement.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Xóa Dịch vụ THẤT BẠI!", "Lỗi", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            String rank = null;
            if (radBtnBronze.isSelected()) {
                rank = radBtnBronze.getText();
            } else if (radBtnSilver.isSelected()) {
                rank = radBtnSilver.getText();
            } else if (radBtnGold.isSelected()) {
                rank = radBtnGold.getText();
            }

            String tenDV = txtBonus.getText();
            String giaTien = txtCost.getText();

            int updateMem = Membership.updateMembership(maDichVu, rank, tenDV, giaTien);
            if (updateMem > 0) {
                modelMembership.setRowCount(0);
                setTableMembership();
                JOptionPane.showMessageDialog(this, "Cập nhật Khóa Tập THÀNH CÔNG!");
                resetClass();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật Khóa Tập THẤT BẠI!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                resetClass();
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClassManagement.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Cập nhật Khóa Tập THẤT BẠI!", "Lỗi", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void tableMembershipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMembershipMouseClicked
        //        try {
        //            int Click = tableMembership.getSelectedRow();
        //            maKhoaTap = (String) tableMembership.getValueAt(Click, 0);
        //            String gioHoc = (String) tableMembership.getValueAt(Click, 1);
        //            if (gioHoc.equals("5h30 - 7h00")) {
        //                radBtn5h30.setSelected(true);
        //            } else {
        //                radBtn17h30.setSelected(true);
        //            }
        //            Date ngS = new SimpleDateFormat("dd/mm/yyyy").parse((String) tableMembership.getValueAt(Click, 2));
        //            dateChooserNgay.setDate(ngS);
        //            txtTitle.setText((String) tableMembership.getValueAt(Click, 3));
        //            txtCost.setText((String) tableMembership.getValueAt(Click, 4));
        //            cbxRoomId.setSelectedItem(tableMembership.getValueAt(Click, 5).toString());
        //        } catch (ParseException ex) {
        //            Logger.getLogger(ClassManagement.class.getName()).log(Level.SEVERE, null, ex);
        //        }
        getRowClass(tableMembership.getSelectedRow());
    }//GEN-LAST:event_tableMembershipMouseClicked

    private void lvMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lvMinimizeMouseClicked
        // TODO add your handling code here:
        setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lvMinimizeMouseClicked

    private void lbExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbExitMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_lbExitMouseClicked

    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        // TODO add your handling code here:
        txtSearch.setText("");
    }//GEN-LAST:event_txtSearchFocusGained

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // TODO add your handling code here:
        DefaultTableModel SearchTable = (DefaultTableModel) tableMembership.getModel();
        String timkiemKH = txtSearch.getText();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(SearchTable);
        tableMembership.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(timkiemKH));
    }//GEN-LAST:event_txtSearchKeyPressed

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
            java.util.logging.Logger.getLogger(MembershipManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MembershipManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MembershipManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MembershipManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new MembershipManagement().setVisible(true);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MembershipManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lbClock;
    private javax.swing.JLabel lbExit;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lvMinimize;
    private javax.swing.JRadioButton radBtnBronze;
    private javax.swing.JRadioButton radBtnGold;
    private javax.swing.JRadioButton radBtnSilver;
    private javax.swing.JTable tableMembership;
    private javax.swing.JTextArea txtBonus;
    private javax.swing.JTextField txtCost;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
