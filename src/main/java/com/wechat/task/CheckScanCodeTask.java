package com.wechat.task;

import com.wechat.exception.WeChatException;
import com.wechat.inter.APIConstat;
import com.wechat.util.HttpUtils;
import com.wechat.web.WebWechatClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * 检查二维码扫码状态
 */
public class CheckScanCodeTask extends TimerTask {

    private WebWechatClient webWechatClient;

    public CheckScanCodeTask(WebWechatClient webWechatClient){
        this.webWechatClient = webWechatClient;
    }


    public void run() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("uuid",webWechatClient.getUuid());
        params.put("tip","1");//这边要设置1，我也不太清楚这个参数具体意义是什么
        params.put("loginicon","true");
        params.put("r",String.valueOf(new Date().getTime()));
        params.put("_",String.valueOf(new Date().getTime()));
        String res = HttpUtils.get(APIConstat.QRCODE_STATUS,params);
        if(res == null)
            throw new RuntimeException("访问【查看二维码扫描状态接口】失败");
        System.out.println("res:"+res);
        String code  = res.split(";")[0].split("=")[1];
        System.out.println("code:"+code);
        if(code.startsWith("4")){
            System.out.println("用户还没有扫描了二维码，等待用户扫描二维码");
        }else if(code.equals("201")){
            System.out.println("用户扫描了二维码，但没有点击确认");
        }else if(code.equals("200")){
            System.out.println("用户扫描了二维码，已经点击了确认，准备重定向微信主界面");
            String redirect_uri =  res.split("\"")[1];
            webWechatClient.setIsLogin(true);
            webWechatClient.setRedirect_uri(redirect_uri);
            this.notifyAll();
            this.cancel();
        }else if(code.equals("")){
            //经验证，当二维码过期时候返回的是空字符串,这时候需要重新获取一下二维码
            webWechatClient.getQrcode();
            System.out.println("二维码已过期");
        }else{
            try {
                throw new WeChatException("未知状态码");
            } catch (WeChatException e) {
                e.printStackTrace();
            }
        }
        System.out.println("qrcode_status:=============="+res);
    }
}
