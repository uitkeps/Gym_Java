/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author Hindu
 */
public class Admin {
    public int executeLogin (String SDT, String password) 
            throws ClassNotFoundException, SQLException {
        int i = 0;
        
        Connection conn = ConnectionUtils.getOracleConnection();
        
        String sql_login = "SELECT staff_telephone, staff_password FROM STAFF \n"
                + "WHERE staff_telephone = '" + SDT + "' AND staff_password = '" + password + "'";
        
        Statement stat = conn.createStatement();
        i = stat.executeUpdate(sql_login);
        
        return i;
    }
}
