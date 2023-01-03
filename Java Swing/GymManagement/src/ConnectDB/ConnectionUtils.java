/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ConnectDB;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Hindu
 */
public class ConnectionUtils {

    /**
     * @return 
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public static Connection getOracleConnection() throws ClassNotFoundException, SQLException {
        return ConnectionOracle.getDBConnection();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        System.out.println("Đang kết nối ...");
        Connection conn = ConnectionUtils.getOracleConnection();
        System.out.println("Kết nối đến " + conn);
        System.out.println("Kết nối thành công");
    }

}
