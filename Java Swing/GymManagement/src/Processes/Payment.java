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

/**
 *
 * @author Nguyen Bao Anh
 */
public class Payment {

    public static int addPayment(String paymentLastDay, String cusId) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
//            int TRANSACTION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;
//            if (TRANSACTION_SERIALIZABLE > 0) {
                Statement st = conn.createStatement();

                String sql = "BEGIN INSERT_PAYMENT ('" + paymentLastDay + "', null, "
                        + "null, " + cusId + ", null); END;";
                System.out.println(sql);
                i = st.executeUpdate(sql);
//            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE
                    , null, ex);
        }
        return i;
    }

    public static int addPayment(String paymentLastDay, String staffId, String cusId) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            Statement st = conn.createStatement();

            String sql = "BEGIN INSERT_PAYMENT ('" + paymentLastDay + "', null, "
                    + staffId + ", " + cusId + ", null); END;";
            System.out.println(sql);

            i = st.executeUpdate(sql);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int deletePayment(String paymentId) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "DELETE FROM payment_detail WHERE payment_id = " + paymentId + " ";
            String sql_1 = "DELETE FROM signup_class WHERE payment_id = " + paymentId + " ";
            String sql_2 = "DELETE FROM signup_membership WHERE payment_id = " + paymentId + " ";
            String sql_3 = "DELETE FROM payment WHERE payment_id = " + paymentId + " ";

            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
            i = st.executeUpdate(sql_1);
            i = st.executeUpdate(sql_2);
            i = st.executeUpdate(sql_3);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int addPaymentMode(String paymentId, String paymentMode) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "UPDATE payment "
                    + "SET payment_mode = '" + paymentMode + "' "
                    + "WHERE payment_id = " + paymentId;
            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int addDiscount(String paymentId, String disId) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "UPDATE payment "
                    + "SET dis_id = " + disId + " "
                    + "WHERE payment_id = " + paymentId + " ";
            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int addPaymentTotal(String paymentId) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "BEGIN sumPaymentTotal(" + paymentId + "); END;";
            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }
}
