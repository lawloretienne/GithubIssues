package com.etiennelawlor.issues.utilities;

import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by etiennelawlor on 12/6/14.
 */
public class HttpUtility {

    /** Initiates the fetch operation. */
    public static String loadFromNetwork(String urlString) throws IOException {
        String str = downloadUrl(urlString);
        return str;
    }

    // It is important to make an HTTP call with gzip to reduce network bandwith
    // and HttpUrlConnection handles this by default
    public static String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        Log.d("", "myurl - "+myurl);
        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url
                .openConnection();

        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");

        conn.setDoInput(true);
        // Starts the query
        conn.connect();

//        int responseCode = conn.getResponseCode();
//        String responseMessage = conn.getResponseMessage();
//        conn.getHeaderFields();
//
//        Log.d("", "response - "+responseCode);
//        Log.d("", "responseMessage - "+responseMessage);
//
//        for (String header : conn.getHeaderFields().keySet()) {
//            if (header != null) {
//                for (String value : conn.getHeaderFields().get(header)) {
//                    Log.d("HTTPUtility", header + ":" + value);
//                }
//            }
//        }

        try {
            is = new BufferedInputStream(conn.getInputStream());

            String content = readInputStream(is);

            return content;
        } finally {
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            if (is != null) {
                is.close();
            }

            conn.disconnect();

        }
    }

    public static String readInputStream(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(isr);
        String read = null;
        try {
            read = br.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (read != null) {
            sb.append(read);
            try {
                read = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
