package com.wechat.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wechat.inter.APIConstat;
import com.wechat.redis.RedisUtils;
import com.wechat.task.CheckIsLoginSuccessTask;
import com.wechat.task.CheckScanCodeTask;
import com.wechat.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 微信客户端
 */
public class WebWechatClient {


    /**二维码是否过期*/
    private boolean isQrCodeExpired = true;

    /**UUID*/
    private String uuid ;

    /**重定向url*/
    private String redirect_uri;

    /**是否登录成功*/
    private boolean isLogin = false;

    /**
     * 微信登录
     */
    public  void login(){
        //获取二维码
        this.getQrcode();
        //轮询扫码状态
        Timer timer = new Timer();
        timer.schedule(new CheckScanCodeTask(this,timer),1,500);
    }


    public void checkLogin(){
        Timer timer = new Timer();
        timer.schedule(new CheckIsLoginSuccessTask(this,timer),1,500);
    }


    public void getQrcode(){
        //获取UUID
        uuid = getUUID();
        if(uuid == null){
            System.out.println("获取uuid失败，uuid为空");
            return;
        }
        //获取二维码
        byte[] qrcode = getQrcode(uuid);
        if(qrcode == null){
            System.out.println("获取二维码图片失败，二维码字节流为空");
            return ;
        }
        //保存二维码到本地
        saveQrcode("d:/qrcode.jpg",qrcode);
    }


    /**
     * 微信初始化工作
     */
    public void weChatInit(){
        //这个时候需要重定向到url，然后获取返回的cookie，保存到本地，之后需要用的权限接口需要把这个cookie带过去（返回cookie中的值，最有用的是data_ticket,其他参数具体什么意思，我也不太清楚）
        CookieStore cookieStore = HttpUtils.getCookieStore(this.getRedirect_uri(),null);
        saveCookie(cookieStore);
        //保存cookie之后去获取联系人列表并缓存到redis中
        String data = HttpUtils.get(APIConstat.GET_CONTACT,null);
        saveUser(data);
    }


    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * 保存cookie到本地
     * @param cookieStore
     */
    private void saveCookie(CookieStore cookieStore){
        if(cookieStore != null){
            FileOutputStream fis = null;
            try {
                fis = new FileOutputStream(new File(WebWechatClient.class.getResource("/cookieStore.properties").getPath()));
                List<Cookie> cookies = cookieStore.getCookies();
                JSONArray jsonArray = new JSONArray();
                for(Cookie cookie : cookies){
                    JSONObject jsonObject = new JSONObject();
                    String name = cookie.getName();
                    String value = cookie.getValue();
                    String domain = cookie.getDomain() == null ? "":cookie.getDomain();
                    String path = cookie.getPath() == null ? "":cookie.getPath();
                    jsonObject.put("name",name);
                    jsonObject.put("value",value);
                    jsonObject.put("domain",domain);
                    jsonObject.put("path",path);
                    jsonArray.add(jsonObject);
                }
                fis.write(jsonArray.toJSONString().getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fis !=null ){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 异步检测
     */
    public void sysCheck(){

    }



    /**
     * 保存用户
     * @param json
     */
    private void saveUser(String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("MemberList");
        RedisUtils.saveUserArr(jsonArray);
    }

    private String getData() throws IOException {
        InputStream in = WebWechatClient.class.getResourceAsStream("/user.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine(); // 读取第一行
        StringBuffer buffer = new StringBuffer();
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        return buffer.toString();
    }


    /**
     * 获取uuid
     * @return
     */
    private String getUUID(){
        Map<String,String> params = new HashMap<String, String>();
        params.put("appid","wx782c26e4c19acffb");
        params.put("fun","new");
        params.put("lang","zh_CN");
        params.put("_",String.valueOf(new Date().getTime()));
        String resString =  HttpUtils.get(APIConstat.GET_UUID,params);
        if(resString !=null){
            resString = resString.split(";")[1].split("\"")[1].replaceAll("\"","");
        }
        return resString;
    }


    /**
     *  获取二维码连接地址
     * */
    private String getQrCodeUrl(){
        return APIConstat.GET_QRCODE + "/" + this.getUUID();
    }


    /**
     * 获取二维码字节数组
     * @param uuid
     * @return
     */
    private byte[] getQrcode(String uuid){
        String url = APIConstat.GET_QRCODE + "/" + uuid;
        return HttpUtils.getByteImg(url);
    }


    /**
     * 保存二维码
     * @param file
     * @param imgByte
     */
    private void saveQrcode(String file,byte[] imgByte){
        try {
            FileOutputStream fos = new FileOutputStream(new File(file));
            try {
                fos.write(imgByte);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebWechatClient wwc = new WebWechatClient();
        wwc.login();
        wwc.checkLogin();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }
}
