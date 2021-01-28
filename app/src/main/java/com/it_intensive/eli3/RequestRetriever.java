package com.it_intensive.eli3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestRetriever extends Thread{ //모비우스에 요청하는 부분
    private final Logger LOG = Logger.getLogger(RequestRetriever.class.getName());
    private IReceived receiver;
    private String ae_name="edu4";
    private String container_name="co2";

    public RequestRetriever(String aeName, String containerName){
        this.ae_name=aeName;
        this.container_name=containerName;
    }
    public RequestRetriever(){

    }
    public void setReceiver(IReceived handler){
        this.receiver = handler;
    }
    @Override
    public void run() {
        try {
            String sb = MobiusConfig.MOBIUS_ROOT_URL + "/" + ae_name + "/" + container_name + "/latest";
            URL mUrl = new URL(sb); //URL

            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection(); //HTTP 연결
            conn.setRequestMethod("GET");
            conn.setDoInput(true); //true : 서버로부터 응답을 받겠다는 의미, false: 응답을 받지 않음
            conn.setDoOutput(false); //true로 설정하면 내부적으로 POST방식으로 변경됨
            conn.setUseCaches(false);

            //정해진 포맷에 맞춰 설정
            conn.setRequestProperty("Accept", "application/xml");
            conn.setRequestProperty("nmtype", "long");
            conn.setRequestProperty("X-M2M-RI", "12345");
            conn.setRequestProperty("X-M2M-Origin", "S20170717074825768bp2l");

            conn.connect();

            String strResp="";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String strLine;

            while ((strLine = in.readLine()) != null) { //한 줄씩 데이터를 읽음
                strResp += strLine; //한 줄씩 읽은 데이터를 하나의 문자열로 합침
            }
            if (receiver != null) {
                receiver.getResponseBody(strResp);
            }
            conn.disconnect();
        } catch (Exception exp) {
            LOG.log(Level.WARNING, exp.getMessage());
        }
    }
}

interface IReceived{
    void getResponseBody(String msg);
}

class MobiusConfig{
    public static String MOBIUS_ROOT_URL ="http://172.20.10.4:7579/Mobius"; // ip
    public static void changeIp(String ip){
        MOBIUS_ROOT_URL = "http://" + ip + ":7579/Mobius";
    }
}