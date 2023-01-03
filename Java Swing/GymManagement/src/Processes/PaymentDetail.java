/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Nguyen Bao Anh
 */
public class PaymentDetail {

    public static int addProduct(String paymentId, String productId, String amount) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "BEGIN Insert_PAYMENT_DETAIL (" + paymentId + ", " + productId + ", " + amount + "); END;";
            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int updateProduct(String paymentId, String productIdOld, String productIdNew, String amount) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "UPDATE payment_detail "
                    + "SET amount = " + amount + ", "
                    + "product_id = " + productIdNew + " "
                    + "WHERE payment_id = " + paymentId + " AND product_id = " + productIdOld + " ";
            System.out.println(sql);
            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Đã tồn tại mặt hàng này trong đơn hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int deleteProduct(String paymentId, String productId) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "DELETE FROM payment_detail WHERE payment_id = " + paymentId + " AND product_id = " + productId + " ";
            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }
    
}
