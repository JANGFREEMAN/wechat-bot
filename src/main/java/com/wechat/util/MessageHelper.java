package com.wechat.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wechat.inter.APIConstat;
import com.wechat.model.BaseRequest;
import com.wechat.model.Message;

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
        br.setSid("htTdo/Oq70DrB0+8");
        br.setSkey("skey");
        br.setDeviceID("123123123123");
        jsonObject.put("BaseRequest",br);
        jsonObject.put("Msg",msg);
        jsonObject.put("Scene",0);
        String result = HttpUtils.post(APIConstat.SEND_MSG,null,jsonObject.toJSONString());
        System.out.println(result);
    }

    public static void main(String[] args) {
//        Message msg = new Message();
//        msg.setType("0");
//        msg.setContent("i love you");
//        msg.setFromUserName("@dc6b06fa88776f3b37b13d8bcc2928a4");
//        msg.setToUserName("@dc6b06fa88776f3b37b13d8bcc2928a4");
//        msg.setLocalID("asfsadfsadfasdf");
//        msg.setClientMsgId("SFsadfsASDFAFASFAS");
//        sendMessage(msg);

        String data = HttpUtils.get(APIConstat.GET_CONTACT,null);
        System.out.println(data);
    }

}
