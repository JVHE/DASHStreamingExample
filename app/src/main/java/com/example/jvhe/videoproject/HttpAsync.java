package com.example.jvhe.videoproject;

/**
 * Created by rhwjd on 2017-09-22.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpAsync extends AsyncTask<Void, Integer, Void> {

    private String param;
    private String urlStr;
    private Handler handler;

    public HttpAsync(String param_, String url_, Handler handler_){
        this.param = param_;
        this.urlStr = url_;
        this.handler = handler_;
    }

    @Override
    protected Void doInBackground(Void... unused) {
/* 인풋 파라메터값 생성 */
        //String param = "email=" + email + "&password=" + password;
        try {
/* 서버연결 */
            //URL url = new URL("http://ghojeong.vps.phps.kr/emailLogin.php");
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

/* 안드로이드 -> 서버 파라메터값 전달 */
            OutputStream outs = conn.getOutputStream();
            outs.write(param.getBytes("UTF-8"));
            outs.flush();
            outs.close();

/* 서버 -> 안드로이드 파라메터값 전달 */
            InputStream is = null;
            BufferedReader in = null;
            String data = "";

            is = conn.getInputStream();
            in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
            String line = null;
            StringBuffer buff = new StringBuffer();
            while ( ( line = in.readLine() ) != null )
            {
                buff.append(line + "\n");
            }
            data = buff.toString().trim();
            //Log.d("RECV_DATA",data);
            Log.e("RECV_DATA",data);
            Message msg = handler.obtainMessage();
            msg.what =1;
            Bundle bund = new Bundle();
            bund.putString("data", data);
            msg.setData(bund);
            handler.sendMessage(msg);

        } catch (Exception e) {
            handler.sendEmptyMessage(0);
            Log.e("HttpError",e.toString());
            e.printStackTrace();
        }
        return null;
    }
}
