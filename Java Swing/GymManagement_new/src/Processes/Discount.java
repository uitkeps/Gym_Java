/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import Views.DiscountForm;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 *
 * @author Hindu
 */
public class Discount {

    public int addDiscount(String code, String percent)
            throws ClassNotFoundException, SQLException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_insert_discount = "BEGIN INSERT_DISCOUNT ('" + code + "', '"
                + percent + "'); END;";

        Statement stat = conn.createStatement();
        stat.executeUpdate(sql_insert_discount);

        Statement stat_temp = conn.createStatement();
        stat_temp.executeUpdate("SELECT * FROM DISCOUNT ORDER BY dis_id");

        i++;

        return i;
    }

    public int updateDiscount(String code, String percent)
            throws ClassNotFoundException, SQLException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_update_discount = "UPDATE DISCOUNT SET dis_code = '" + code
                + "', dis_percent = '" + percent + "' WHERE dis_id = " + DiscountForm.maGiamGia;

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_update_discount);

        return i;
    }

    public int deleteDiscount(String code)
            throws SQLException, ClassNotFoundException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_delete_discount = "DELETE FROM DISCOUNT WHERE dis_code = '" + code + "'";

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_delete_discount);

        return i;
    }

    public ResultSet insertTableDiscount() throws SQLException, ClassNotFoundException {

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_set_table = "SELECT * FROM DISCOUNT ORDER BY dis_id";

        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql_set_table);

        return rs;
    }

    private static final Random generator = new Random();
    private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public String generateCode(int lengthOfChar) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lengthOfChar; i++) {
            int number = randomNumber(0, CHAR_LIST.length() - 1);
            if (number > -1) {
                char ch = CHAR_LIST.charAt(number);
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }
}
