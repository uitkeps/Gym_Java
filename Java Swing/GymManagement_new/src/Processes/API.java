/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Processes;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Hindu
 */
public class API {

    public static void browser(String link) {
        try {
            String exam = "\"";

            String[] res = link.split(exam);

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    URI uri = URI.create(res[1]);
                    System.out.println(uri.toString());
                    desktop.browse(uri);
                }
            }
        } catch (IOException | InternalError e) {

        }
    }

    public static void TrangThanhToanMoMo(String paymentid, String cost) throws IOException {
        URL url = new URL("http://localhost:3000/payment");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String detail = "THANH TOÁN cho hệ thống phòng Gym - VIETGYM";
        String jsonInputString = "{\"id\":\"" + paymentid + "\","
                + "\"payment\":\"" + cost + "\","
                + "\"detail\":\"" + detail + "\"}";

        try ( OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try ( BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            browser(response.toString());
        }

    }
}
