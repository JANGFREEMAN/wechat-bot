package com.wechat.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * http工具类
 */
public class HttpUtils {

    //http客户端
    private static CloseableHttpClient httpClient ;

    private static HttpClientContext context;

    static {
        // 全局请求设置
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        // 创建cookie store的本地实例
        CookieStore cookieStore = new BasicCookieStore();
        // 创建HttpClient上下文
        context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        // 创建一个HttpClient
        httpClient = HttpClients.custom().setDefaultRequestConfig(globalConfig)
                .setDefaultCookieStore(cookieStore).build();
    }

    CloseableHttpResponse res = null;

    private static String GET_METHOD = "GET";

    private static String POST_METHOD = "POST";

    public static String get(String url,Map<String,String> params){
        System.setProperty("jsse.enableSNIExtension", "false");
        if(params!=null && params.size() > 0 ){
            url = url + "?";
            for(Map.Entry entry : params.entrySet()){
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpget,context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] getByteImg(String url){
        System.setProperty("jsse.enableSNIExtension", "false");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(httpget,context);
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
}

