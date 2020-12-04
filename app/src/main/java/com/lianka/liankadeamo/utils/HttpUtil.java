package com.lianka.liankadeamo.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.provider.Telephony.Mms.Part.CHARSET;


public class HttpUtil {
    public static String mpost(String url, Map<String, Object> map) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object value = map.get(key);
            if (value instanceof String[]) {
                String[] strings = (String[]) value;
                for (String temp : strings) {
                    params.add(new BasicNameValuePair(key, temp));
                }
            } else {
                params.add(new BasicNameValuePair(key, value.toString()));
            }
        }
        UrlEncodedFormEntity uefEntity;
        String resData = null;
        try {
            uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);
            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            resData = EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resData;
    }

    /**
     * 提交数据
     *
     * @param url
     * @param map
     * @return
     */
    public static String mPost(String url, Map<String, String> map) {
        Map<String, String> headerMap = new HashMap<>();
//        headerMap.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjM5MzQ1NzgzMzgsImFwcElkIjoiN1YzZHh5aUtzOFdteG1iNmF4aW9idTFlZHRhaHVSWlMiLCJ1dWlkIjoxNjQ4ODc4NzgxNTgwNTMzMjM1fQ.wEMJUPwsJE1HhJsKOR-5_87TueqoKeyyEDL4hB5iVpU");
        headerMap.put("Content-Type", "application/json");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<>();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            Object value = map.get(key);
            if (value instanceof String[]) {
                String[] strings = (String[]) value;
                for (String temp : strings) {
                    params.add(new BasicNameValuePair(key, temp));
                }
            } else {
                params.add(new BasicNameValuePair(key, value.toString()));
            }
        }
        UrlEncodedFormEntity uefEntity;
        String resData = null;
        try {
            uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);
            if (!headerMap.isEmpty()) {
                for (Map.Entry<String, String> vo : headerMap.entrySet()) {
                    httpPost.setHeader(vo.getKey(), vo.getValue());
                }
            }
            HttpResponse httpResponse;
            try {
                httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
                resData = EntityUtils.toString(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resData;
    }

    /**
     * 以GET方式的请求，并返回结果字符串。
     *
     * @param url 请求地址
     * @return 如果失败，返回为null
     */
    public static String get(String url) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            URI uri = new URI(url);
            HttpGet get = new HttpGet(uri);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                StringBuffer buffer = new StringBuffer();
                try (InputStream in = response.getEntity().getContent()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, CHARSET));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    reader.close();
                }
                return buffer.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
