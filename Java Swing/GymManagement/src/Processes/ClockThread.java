/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Processes;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JLabel;

/**
 *
 * @author Hindu
 */
public class ClockThread extends Thread {

    private JLabel lb1, lb2;

    public ClockThread(JLabel lb1, JLabel lb2) {
        this.lb1 = lb1;
        this.lb2 = lb2;
    }

    public void run() {
        try {
            while (true) {
                Calendar cal = new GregorianCalendar();
                int second = cal.get(Calendar.SECOND);
                int minute = cal.get(Calendar.MINUTE);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                String thu;
                String gio = "", phut = "", giay = "";
                if (hour <= 9) {
                    gio = "0" + hour;
                } else {
                    gio = "" + hour;
                }
                if (minute <= 9) {
                    phut = "0" + minute;
                } else {
                    phut = "" + minute;
                }
                if (second <= 9) {
                    giay = "0" + second;
                } else {
                    giay = "" + second;
                }
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == 1) {
                    thu = "Chủ Nhật";
                } else {
                    thu = "Thứ " + Integer.toString(dayOfWeek);
                }
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                lb1.setText(gio + ":" + phut + ":" + giay);
                lb2.setText(thu + " ngày " + dayOfMonth + " tháng " + (month + 1) + " năm " + year);
                sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
