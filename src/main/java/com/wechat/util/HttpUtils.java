package com.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.wechat.inter.APIConstat;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.*;

/**
 * http工具类
 */
public class HttpUtils {

    private CookieStore cookieStore;
    static {
        // 创建cookie store的本地实例
        CookieStore cookieStore = new BasicCookieStore();
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = HttpUtils.class.getResourceAsStream("/cookieStore.properties");
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Set<Map.Entry<Object, Object>> entrys = properties.entrySet();//返回的属性键值对实体
        for(Map.Entry<Object, Object> entry:entrys){
            String name = entry.getKey().toString();
            String _value = entry.getValue().toString();
            String value = _value.split(";")[0];
            String domain = _value.split(";")[1];
            BasicClientCookie  cookie = new BasicClientCookie(name,value);
            cookie.setDomain(domain);
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
        }
    }




    CloseableHttpResponse res = null;

    private static String GET_METHOD = "GET";

    private static String POST_METHOD = "POST";

    public static String get(String url,Map<String,String> params){
        System.setProperty("jsse.enableSNIExtension", "false");
        DefaultHttpClient  httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        if(params!=null && params.size() > 0 ){
            url = url + "?";
            for(Map.Entry entry : params.entrySet()){
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        httpGet.getParams().setParameter("charset","utf-8");
        if(url.equals(APIConstat.GET_CONTACT)){
            CookieStore cookieStore = httpClient.getCookieStore();
            List<Cookie> cookies = cookieStore.getCookies();
            for(Cookie cookie : cookies){
                System.out.println("name:"+cookie.getName()+"===value:"+cookie.getValue()+"=====domain:"+cookie.getDomain());
            }
        }

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //entityutils默认是用iso__8859_1来解码的。这边需要转码一下。不然返回中文是乱码
                return new String(EntityUtils.toString(entity).getBytes("ISO_8859_1"),"UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url,Map<String,String> params,String json){
        System.setProperty("jsse.enableSNIExtension", "false");
        DefaultHttpClient  httpClient = new DefaultHttpClient();
        if(params!=null && params.size() > 0 ){
            url = url + "?";
            for(Map.Entry entry : params.entrySet()){
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        HttpPost post = new HttpPost(url);
        try {
            System.out.println(json);
            StringEntity s = new StringEntity(json);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = httpClient.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            httpClient.close();
        }
        return null;
    }


    public static byte[] getByteImg(String url){
        System.setProperty("jsse.enableSNIExtension", "false");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream in = entity.getContent();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int len = 0 ;
                while((len = in.read(b))!=-1){
                    baos.write(b,0,len);
                }
                return baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws IOException {
        //获取uui的接口
//        Map<String,String> params = new HashMap<String, String>();
//        params.put("appid","wx782c26e4c19acffb");
//        params.put("redirect_uri","https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage");
//        params.put("fun","new");
//        params.put("lang","zh_CN");
//        params.put("_",String.valueOf(new Date().getTime()));
//        String res = HttpUtils.get("https://login.weixin.qq.com/jslogin",params);
//        System.out.println(res);


        //获取二维码图片
        Map<String,String> params = new HashMap<String, String>();
//        params.put("uuid","weqjH-Py6w==");
        String res = HttpUtils.get("https://login.weixin.qq.com/qrcode/weqjH-Py6w==",params);
        System.out.println(res);
    }

    /**
     * 获取cookieStore
     * @param url
     * @param params
     * @return
     */
    public static CookieStore getCookieStore(String url,Map<String,String> params) {
        System.setProperty("jsse.enableSNIExtension", "false");
        DefaultHttpClient  httpClient = new DefaultHttpClient();
        if(params!=null && params.size() > 0 ){
            url = url + "?";
            for(Map.Entry entry : params.entrySet()){
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpget);
            return httpClient.getCookieStore();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

