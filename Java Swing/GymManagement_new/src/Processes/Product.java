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
 * @author ROG
 */
public class Product {

    public static int addProduct(String tenHangHoa, String giaTien, String loaiHangHoa, String soLuong) {
        int ketQua = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "BEGIN INSERT_PRODUCT ('" + tenHangHoa + "', "
                    + giaTien + ", '" + loaiHangHoa + "', " + soLuong + "); END;";

            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            
            Statement stat = conn.createStatement();
            stat.executeQuery("SELECT * FROM PRODUCT ORDER BY product_id");
            
            ketQua++;

        } catch (SQLException ex) {

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ketQua;
    }

    public static int updateProduct(String maHangHoa, String tenHangHoa, String giaTien, String loaiHangHoa, String soLuong) {
        int ketQua = 0;
        try {

            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "UPDATE PRODUCT SET product_name = '" + tenHangHoa
                    + "', product_price = " + giaTien + ", product_type = '" + loaiHangHoa
                    + "', product_amount = " + soLuong + " WHERE product_id = " + maHangHoa + " ";

            Statement st = conn.createStatement();
            ketQua = st.executeUpdate(sql);

            return ketQua;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ketQua;
    }

    public static int deleteProduct(String maHangHoa) {
        int ketQua = 0;
        try {

            Connection conn = ConnectionUtils.getOracleConnection();

            String sql = "DELETE FROM PRODUCT WHERE product_id = " + maHangHoa + " ";

            Statement st = conn.createStatement();
            ketQua = st.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ketQua;
    }
}
