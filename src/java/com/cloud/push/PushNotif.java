package com.cloud.push;

import com.android.repository.RegistryRepository;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;

@ManagedBean
@RequestScoped
public class PushNotif {

    private String op;
    private String val;

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public void send() {
        try {
            String adr = "https://gcm-http.googleapis.com/gcm/send";
            RegistryRepository repo = new RegistryRepository();
            String reg_id = repo.find(1).getRegistrationId();
            repo.close();
            
            JsonObject body = Json.createObjectBuilder()
                    .add("to", reg_id)
                    .add("data",
                            Json.createObjectBuilder()
                            .add("op", op)
                            .add("val", val)
                    ).build();

            URL url = new URL(adr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Length", body.toString().length() + "");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "key=" + "AIzaSyCjAYSP7DCmI70cZAvQxL-jf1n0ZqxaiKg");
            con.setDoInput(true);
            con.setDoOutput(true);

            PrintWriter yaz = new PrintWriter(con.getOutputStream(), true);
            yaz.println(body.toString());
            int responseCode = con.getResponseCode();
            
            // request gider, response gelir, gelen response (code) ->> 200 = HTTP_OK anlamına gelir.
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
