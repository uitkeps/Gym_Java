/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import Views.StaffForm;
import Views.StaffPage;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;

/**
 *
 * @author Hindu
 */
public class Staff {

    public int addStaff(String tenNV, String ngSinh,
            String ngDK, String GT, String diaChi, String SDT, String matKhau,
            String tienLuong, String viTri, String namKN, String chuyenMon)
            throws ClassNotFoundException, SQLException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_insert_staff = "BEGIN INSERT_STAFF ('" + tenNV + "', '"
                + ngSinh + "', '" + GT + "', '" + viTri + "', '" + ngDK + "', '"
                + diaChi + "', '" + SDT + "', '" + matKhau + "', '"
                + tienLuong + "', '" + namKN + "', '" + chuyenMon + "'); END;";

        Statement stat = conn.createStatement();
        stat.executeUpdate(sql_insert_staff);
        Statement stat_temp = conn.createStatement();
        stat_temp.executeUpdate("SELECT * FROM STAFF ORDER BY STAFF_ID");
        i++;

        return i;
    }

    public int updateStaff(String tenNV, String ngSinh, String ngVL, String GT,
            String diaChi, String SDT, String tienLuong, String viTri, String chuyenMon, String namKN)
            throws ClassNotFoundException, SQLException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_update_staff = "UPDATE STAFF SET staff_name = '" + tenNV
                + "', staff_birthday = to_date('" + ngSinh + "','dd/mm/yyyy')"
                + ", staff_datejoin = to_date('" + ngVL + "', 'dd/mm/yyyy')"
                + ", staff_gender = '" + GT + "', staff_address = '" + diaChi
                + "', staff_telephone = '" + SDT + "', staff_salary = '" + tienLuong
                + "', staff_type = '" + viTri + "', staff_expertise = '" + chuyenMon
                + "', staff_experience = '" + namKN + "' WHERE staff_id = " + StaffForm.MaNV;

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_update_staff);

        return i;
    }

    public int deleteStaff(String SDT)
            throws SQLException, ClassNotFoundException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_delete_staff = "DELETE FROM STAFF WHERE staff_telephone = " + SDT;

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_delete_staff);

        return i;
    }

    public int changePasswordStaff(String SDT, String newPassword)
            throws SQLException, ClassNotFoundException {
        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_change_pass_staff = "UPDATE STAFF SET staff_password = '" + newPassword
                + "' WHERE staff_telephone = " + SDT;

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_change_pass_staff);

        return i;
    }

    public ResultSet insertTableStaff() throws SQLException, ClassNotFoundException {

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_set_table = "SELECT * FROM STAFF ORDER BY staff_id";

        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql_set_table);

        return rs;
    }

    public ResultSet getStafforPage() throws ClassNotFoundException, SQLException {
        Connection conn = ConnectionUtils.getOracleConnection();

        String sql = "SELECT staff_name, staff_type FROM STAFF WHERE staff_telephone = " + StaffPage.SDT_staff;

        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery(sql);

        return rs;

    }

    public static ArrayList<String> data = new ArrayList<>();

    public static ArrayList<String> getData() {
        data.add("Gym");
        data.add("Yoga");
        data.add("Boxing");
        data.add("Aerobic");
        data.add("Muay Thai");
        return data;
    }

}
