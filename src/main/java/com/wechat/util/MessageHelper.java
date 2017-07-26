package com.wechat.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wechat.inter.APIConstat;
import com.wechat.model.BaseRequest;
import com.wechat.model.Message;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * 微信发送消息工具类
 */
public class MessageHelper {

    /**
     * 发送消息
     * @param msg
     */
    public static void sendMessage(Message msg){
        JSONObject jsonObject = new JSONObject();
        BaseRequest br = new BaseRequest();
        br.setUin("933014580");
        br.setSid("Gs1qFlcKKkpxTu46");
        br.setSkey("skey");
        br.setDeviceID("123123123123");
        jsonObject.put("BaseRequest",br);
        jsonObject.put("Msg",msg);
        jsonObject.put("Scene",0);
        String result = HttpUtils.post(APIConstat.SEND_MSG,null,jsonObject.toJSONString());
        System.out.println(result);
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.setType("0");
                msg.setContent("hi,man,what are you doing now");
                msg.setFromUserName("@c8250f617d682b48ff7d64b8be10a206");
                msg.setToUserName("@471188422eab4a698a6d0948c2cfe858");
                msg.setLocalID("asfsadfsadfasdf");
                msg.setClientMsgId(String.valueOf(new Date().getTime()));
                sendMessage(msg);
            }
        },100,1000);
//        String data = HttpUtils.get(APIConstat.GET_CONTACT,null);
//        System.out.println(data);
    }

}
