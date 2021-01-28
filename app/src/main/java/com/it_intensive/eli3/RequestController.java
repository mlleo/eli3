package com.it_intensive.eli3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestController extends Thread { //서버로 데이터 전송
    private final Logger LOG = Logger.getLogger(RequestController.class.getName());

    private IReceived receiver;

    private String ae_name = "edu4"; //Application Entity name
    private String container_name = "led"; //container name

    public ContentInstanceObject instance; //xml 값을 가지게 됨

    public RequestController(String comm) { //생성자
        instance = new ContentInstanceObject();
        instance.setAeName(ae_name);
        instance.setContainerName(container_name);
        instance.setContent(comm);
    }

    public RequestController(String aeName, String containerName, String comm) { //생성자
        this.ae_name = aeName;
        this.container_name = containerName;

        instance = new ContentInstanceObject();
        instance.setAeName(ae_name);
        instance.setContainerName(container_name);
        instance.setContent(comm);
    }

    public void setReceiver(IReceived handler) {
        this.receiver = handler;
    }

    public void run() {
        try {
            String sb = MobiusConfig.MOBIUS_ROOT_URL + "/" + ae_name + "/" + container_name;
            URL mUrl = new URL(sb); //URL

            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection(); //HTTP 연결
            conn.setRequestMethod("POST");
            conn.setDoInput(true); //true : 서버로부터 응답을 받겠다는 의미, false: 응답을 받지 않음
            conn.setDoOutput(false); //true로 설정하면 내부적으로 POST방식으로 변경됨
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false); //리다이렉션 자동으로 못하게 함

            //정해진 포맷에 맞춰 설정
            conn.setRequestProperty("Accept", "application/xml");
            conn.setRequestProperty("Content-Type", "application/vnd.onem2m-res+xml;ty=4");
            conn.setRequestProperty("X-M2M-RI", "123sdfgd45");
            conn.setRequestProperty("X-M2M-Origin", "S20170717074825768bp2l");

            String reqContent = instance.makeBodyXML();//보낼 XML값을 조립함

            conn.setRequestProperty("Content-Length", String.valueOf(reqContent.length()));

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(reqContent.getBytes());//DB에 변경된 XML값을 저장함
            dos.flush();
            dos.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String resp = "";
            String strLine;
            while ((strLine = in.readLine()) != null) { //한 줄씩 데이터를 읽음
                resp += strLine; //한 줄씩 읽은 데이터를 하나의 문자열로 합침
            }
            if (receiver != null) {
                receiver.getResponseBody(resp);
            }
            conn.disconnect();
        } catch (Exception exp) {
            LOG.log(Level.SEVERE, exp.getMessage());
        }
    }
}

// XML를 변경하여 가지고 있는 CLASS
class ContentInstanceObject {
    private String aeName = "";
    private String containerName = "";
    private String content = "";

    public void setAeName(String value) {
        this.aeName = value;
    }

    public void setContainerName(String value) {
        this.containerName = value;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String makeBodyXML() { //1,2,3,4에 맞게 content 추가하여 xml를 만듬
        String xml = "";

        xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<m2m:cin "
                + "xmins:m2m=\"http://www.onem2m.org/xml/protocols\" "
                + "xmins:xsi=\"http:www/w3/org/2001/XMLSchema-instance\">"
                + "<cnf>text</cnf>"
                + "<con>" + content + "</con>"
                + "</m2m:cin>";

        return xml;
    }

}