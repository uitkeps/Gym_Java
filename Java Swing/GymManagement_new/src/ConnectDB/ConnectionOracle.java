/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Hindu
 */
public class ConnectionOracle {

    public static Connection getDBConnection() throws ClassNotFoundException, SQLException {
        String hostname = "localhost";
        String sid = "oracl";
        String username = "gym";
        String password = "123456789";

        return getDBConnection(hostname, sid, username, password);
    }

    public static Connection getDBConnection(String hostname, String sid, 
            String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        String url = "jdbc:oracle:thin:@" + hostname + ":1521:" + sid;

        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }
}
