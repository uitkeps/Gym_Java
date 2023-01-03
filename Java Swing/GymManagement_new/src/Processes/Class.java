/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nguyen Bao Anh
 */
public class Class {

    public int addClass(String gioHoc, String ngay, String tenKhoaTap,
            String giaTien, String maPhongTap) throws SQLException, ClassNotFoundException {
        int ketQua = 0;

        Connection conn = ConnectionUtils.getOracleConnection();
        String sql = "BEGIN INSERT_CLASS ('" + gioHoc + "', '" + ngay + "', '"
                + tenKhoaTap + "', " + giaTien + ", " + maPhongTap + "); END;";

        Statement st = conn.createStatement();
        st.executeUpdate(sql);

        Statement stat_temp = conn.createStatement();
        stat_temp.executeUpdate("SELECT * FROM ROOM ORDER BY room_id");

        ketQua++;
        return ketQua;
    }

    public int updateClass(String maKhoaTap, String gioHoc, String ngay,
            String tenKhoaTap, String giaTien, String maPhongTap)
            throws SQLException, ClassNotFoundException {
        int ketQua = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql = "UPDATE CLASS SET class_period = '" + gioHoc + "', class_date = '" + ngay + "'"
                + ", class_title = '" + tenKhoaTap + "', class_cost = " + giaTien + ", room_id = " + maPhongTap + " "
                + "WHERE class_id = '" + maKhoaTap + "' ";

        Statement st = conn.createStatement();
        ketQua = st.executeUpdate(sql);

        return ketQua;
    }

    public int deleteClass(String maKhoaTap) throws SQLException, ClassNotFoundException {
        int ketQua = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql = "DELETE FROM CLASS WHERE class_id = '" + maKhoaTap + "' ";

        Statement st = conn.createStatement();
        ketQua = st.executeUpdate(sql);

        return ketQua;
    }

}
