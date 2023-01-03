/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import ConnectDB.ConnectionUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author Hindu
 */
public class Inbody {

    private String maKH;

    public Inbody() {

    }

    public Inbody(String maKH) {
        this.maKH = maKH;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public int addInbody(String maKH, String klCo, String klMo, String canNang,
            String chieuCao, String vongBung, String vongHong, String dam, String khoang,
            String tbw)
            throws ClassNotFoundException, SQLException {

        int i = 0;

        Connection conn = ConnectionUtils.getOracleConnection();

        String sql_insert_inbody = "BEGIN INSERT_INBODY (" + maKH + ", "
                + klCo + ", " + klMo + ", " + canNang + ", " + chieuCao + ", "
                + vongBung + ", " + vongHong + ", " + dam + ", " + khoang + ", "
                + tbw + "); END;";

        Statement stat = conn.createStatement();
        stat.executeUpdate(sql_insert_inbody);

        Statement stat_temp = conn.createStatement();
        stat_temp.executeUpdate("SELECT * FROM INBODY ORDER BY cus_id");

        i++;

        return i;
    }

    public ResultSet getInbodyforPage() throws ClassNotFoundException, SQLException {
        Connection conn = ConnectionUtils.getOracleConnection();

        String sql = "SELECT cus_id FROM INBODY";

        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery(sql);

        return rs;

    }
}
