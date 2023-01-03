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
 * @author Nguyen Bao Anh
 */
public class Room {

    public static int addRoom(String capacity) {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "BEGIN INSERT_ROOM(" + capacity + "); END;";
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            Statement stat_temp = conn.createStatement();
            stat_temp.executeUpdate("SELECT * FROM ROOM ORDER BY room_id");
            i++;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int updateRoom(String maPhongTap, String sucChua) throws ClassNotFoundException {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "UPDATE room SET room_capacity = " + sucChua + " WHERE room_id = " + maPhongTap + " ";
            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }

    public static int deleteRoom(String maPhongTap) throws ClassNotFoundException {
        int i = 0;
        try {
            Connection conn = ConnectionUtils.getOracleConnection();
            String sql = "DELETE FROM room WHERE room_id = " + maPhongTap + " ";
            Statement st = conn.createStatement();
            i = st.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }
}
