/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nguyen Bao Anh
 */
public class SignUpClass {

    public static boolean checkDangKy(String paymentId, String cusId,String classPeriod, String classDate) {
        int count = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "SELECT COUNT(*) "
                    + "FROM signup_class, class "
                    + "WHERE signup_class.class_id = class.class_id AND cus_id = " + cusId + " "
                    + " AND class_period = '"+classPeriod+"' "
                    + " AND class_date = '"+classDate+"' AND payment_id = "+paymentId+" ";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                count = rs.getInt("COUNT(*)");
            }
            if (count >= 1) {
                //Có tồn tại
                return true;
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Không tồn tại
        return false;
    }

    public static int addClass(String paymentId, String cusId, String classId, String dateSignUp) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "BEGIN INSERT_SIGNUP_CLASS (" + paymentId + ", " + cusId + ", " + classId + ", '" + dateSignUp + "'); END;";

            Statement st = conn.createStatement();

            i = st.executeUpdate(sql);

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SignUpClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int updateClass(String cusId, String classIdOld, String classIdNew) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "UPDATE SIGNUP_CLASS "
                    + "SET class_id = " + classIdNew + " "
                    + "WHERE cus_id = " + cusId + " AND class_id = " + classIdOld + " ";

            Statement st = conn.createStatement();
            System.out.println(sql);

            i = st.executeUpdate(sql);

        }  catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SignUpClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int deleteClass(String paymentId, String cusId, String classId) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "DELETE FROM SIGNUP_CLASS "
                    + "WHERE payment_id = " + paymentId + " AND cus_id = " + cusId + " AND class_id = " + classId + "";

            Statement st = conn.createStatement();

            i = st.executeUpdate(sql);

        } 
        catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SignUpClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }
}
