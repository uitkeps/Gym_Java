/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import Views.CustomerForm;
import Views.CustomerPage;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author Hindu
 */
public class Customer {

    private String soDienThoai;

    public Customer() {

    }

    public Customer(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int addCustomer(String tenKH, String ngSinh, String GT, String ngDK,
            String diaChi, String SDT, String password)
            throws ClassNotFoundException, SQLException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_insert_customer = "BEGIN INSERT_CUSTOMER ('" + tenKH + "', '"
                + ngSinh + "', '" + GT + "', '" + password + "', '" + ngDK + "', '"
                + diaChi + "', '" + SDT + "',  NULL); END;";

        Statement stat = conn.createStatement();
        stat.executeUpdate(sql_insert_customer);

        Statement stat_temp = conn.createStatement();
        stat_temp.executeUpdate("SELECT * FROM CUSTOMER ORDER BY cus_id");

        i++;

        return i;
    }

    public int updateCustomer(String tenKH, String ngSinh, String GT, String diaChi, String SDT)
            throws ClassNotFoundException, SQLException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_update_customer = "UPDATE CUSTOMER SET cus_name = '" + tenKH
                + "', cus_birthday = to_date('" + ngSinh + "','dd/mm/yyyy')"
                + ", cus_gender = '" + GT + "', cus_address = '" + diaChi
                + "', cus_telephone = '" + SDT + "' WHERE cus_telephone = " + CustomerPage.SDT_cus;

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_update_customer);

        return i;
    }

    public int updateCustomerForStaff(String tenKH, String ngSinh, String GT, String diaChi, String SDT)
            throws ClassNotFoundException, SQLException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_update_customer = "UPDATE CUSTOMER SET cus_name = '" + tenKH
                + "', cus_birthday = to_date('" + ngSinh + "','dd/mm/yyyy')"
                + ", cus_gender = '" + GT + "', cus_address = '" + diaChi
                + "', cus_telephone = '" + SDT + "' WHERE cus_id = " + CustomerForm.MaKH;

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_update_customer);

        return i;
    }

    public int deleteCustomer(String SDT)
            throws SQLException, ClassNotFoundException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_delete_customer = "DELETE FROM CUSTOMER WHERE cus_telephone = " + SDT;

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_delete_customer);

        return i;
    }

    public int changePasswordCustomer(String SDT, String newPassword)
            throws SQLException, ClassNotFoundException {
        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_change_pass_cus = "UPDATE CUSTOMER SET cus_password = '" + newPassword
                + "' WHERE cus_telephone = " + SDT;

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_change_pass_cus);

        return i;
    }

    public static int deleteMembership(String cusid)
            throws SQLException, ClassNotFoundException {
        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_change_pass_cus = "UPDATE CUSTOMER SET cus_memexpired = null "
                + "WHERE cus_telephone = " + cusid;

        Statement stat = conn.createStatement();
        stat.executeUpdate(sql_change_pass_cus);

        String sql2 = "DELETE FROM SIGNUP_MEMBERSHIP WHERE cus_id = " + cusid;
        Statement stat1 = conn.createStatement();
        stat1.executeUpdate(sql2);

        i++;

        return i;
    }

    public ResultSet insertTableCustomer() throws SQLException, ClassNotFoundException {

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_set_table = "SELECT * FROM CUSTOMER ORDER BY cus_id";

        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql_set_table);

        return rs;
    }

    public static ResultSet getCustomerforPage() throws ClassNotFoundException, SQLException {
        Connection conn = ConnectionUtils.getOracleConnection();

        String sql = "SELECT * FROM CUSTOMER WHERE cus_telephone = " + CustomerPage.SDT_cus + "";

        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery(sql);

        return rs;

    }

    public static ResultSet getMembershipforPage(String SDT) throws ClassNotFoundException, SQLException {
        Connection conn = ConnectionUtils.getOracleConnection();

        String sql = "SELECT MEM_BONUS FROM MEMBERSHIP, SIGNUP_MEMBERSHIP, CUSTOMER "
                + " WHERE MEMBERSHIP.MEM_ID = SIGNUP_MEMBERSHIP.MEM_ID AND CUSTOMER.cus_id = SIGNUP_MEMBERSHIP.cus_id"
                + " AND cus_telephone = " + SDT + " AND sysdate < cus_memexpired ORDER BY PAYMENT_ID DESC";

        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery(sql);

        return rs;

    }

}
