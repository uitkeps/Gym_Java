/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author Hindu
 */
public class Login {

    public static String customer_id;

    public static String getCustomer_id() {
        return customer_id;
    }

    public static void setCustomer_id(String customer_id) {
        Login.customer_id = customer_id;
    }

    public int executeLoginCustomer(String SDT, String password)
            throws ClassNotFoundException, SQLException, IOException {
        int i = 0;
        

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_login_customer = "SELECT cus_telephone, cus_password FROM CUSTOMER \n"
                + "WHERE cus_telephone = '" + SDT + "' AND cus_password = '" + password + "'";

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_login_customer);
        
        
        try (FileOutputStream output = new FileOutputStream("src\\Customer.txt")) {
            output.write(SDT.getBytes());
        }

        return i;
    }

    public int executeLoginStaff(String SDT, String password)
            throws ClassNotFoundException, SQLException, IOException {
        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_login_staff = "SELECT staff_telephone, staff_password FROM STAFF \n"
                + "WHERE staff_telephone = '" + SDT + "' AND staff_password = '" + password + "'";

        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_login_staff);
        
        try (FileOutputStream output = new FileOutputStream("src\\Staff.txt")) {
            output.write(SDT.getBytes());
        }

        return i;
    }

}
