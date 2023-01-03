/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes;

import ConnectDB.ConnectionOracle;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;

/**
 *
 * @author Nguyen Minh Nhut
 */
public class ReportView extends JFrame {

    public ReportView(String fileName) throws SQLException, JRException, ClassNotFoundException {
        this(fileName, null);
    }

    public ReportView(String fileName, HashMap parameter) throws SQLException, JRException, ClassNotFoundException {
        JasperReport jsr = JasperCompileManager.compileReport(fileName);
        JasperPrint print = JasperFillManager.fillReport(jsr, parameter, ConnectionOracle.getDBConnection());
        JasperViewer.viewReport(print, false);
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws SQLException, JRException, ClassNotFoundException {
        // TODO code application logic here
        String makh = JOptionPane.showInputDialog(null, "Nhập vào mã khách hàng");
        HashMap hs = new HashMap();
        hs.put("makh", makh);
        String localDir = System.getProperty("user.dir");

        ReportView viewer = new ReportView(localDir + "\\src\\Report\\report1.jrxml", hs);
        viewer.setVisible(true);
    }

}
