/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import Views.CustomerPage;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nguyen Bao Anh
 */
public class Membership {

    public static int addMembership(String paymentId, String cusId, String memId, String date) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            Statement st = conn.createStatement();

            String sql = "BEGIN Insert_SIGNUP_MEMBERSHIP(" + paymentId + ", " + cusId + ", " + memId + ", '" + date + "'); END;";
            st.executeUpdate(sql);

            Statement stat_temp = conn.createStatement();
            stat_temp.executeUpdate("SELECT * FROM SIGNUP_MEMBERSHIP ORDER BY payment_id");
                      
            i++;

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Membership.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int addMembershipforStaff(String rank, String bonus, String fee) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            Statement st = conn.createStatement();

            String sql = "BEGIN Insert_MEMBERSHIP('" + rank + "', " + fee + ", '" + bonus + "'); END;";
            i = st.executeUpdate(sql);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Membership.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int deleteMembership(String maDV) throws SQLException, ClassNotFoundException {
        int ketQua = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql = "DELETE FROM MEMBERSHIP WHERE mem_id = '" + maDV + "' ";

        Statement st = conn.createStatement();
        ketQua = st.executeUpdate(sql);

        return ketQua;
    }

    public static int updateMembership(String maDV, String rank, String bonus, String fee)
            throws SQLException, ClassNotFoundException {
        int ketQua = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql = "UPDATE MEMBERSHIP SET mem_level = '" + rank
                + "', mem_bonus = '" + bonus + "'"
                + ", mem_fee = '" + fee + "WHERE class_id = '" + maDV + "' ";

        Statement st = conn.createStatement();
        ketQua = st.executeUpdate(sql);

        return ketQua;
    }
}
