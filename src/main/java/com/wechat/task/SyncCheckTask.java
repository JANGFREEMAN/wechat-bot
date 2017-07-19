package com.wechat.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wechat.inter.APIConstat;
import com.wechat.util.HttpUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * 从服务端轮询是否有新的消息
 */
public class SyncCheckTask extends TimerTask{


    public void run() {
        Map<String,String> params = new HashMap<String, String>();
//        params.put("uuid",webWechatClient.getUuid());
        params.put("tip","1");//这边要设置1，我也不太清楚这个参数具体意义是什么
        params.put("loginicon","true");
        params.put("r",String.valueOf(new Date().getTime()));
        params.put("_",String.valueOf(new Date().getTime()));
        String res = HttpUtils.get(APIConstat.SYNCHECK,params);//window.synccheck={retcode:"0",selector:"0"}
        if(res == null)
            System.out.println("异常");
        int selector = JSONObject.parseObject(res.split("=")[1]).getIntValue("selector") ;
        if(selector > 0){
            //大于0表示有新的消息需要同步,这时候需要向服务器拉取消息
//            String res = HttpUtils.get(APIConstat.WEB_WX_SYNC,null);

            //拉取消息之后，需要向服务端更新synkey，确认已经收到消息

        }
    }
}
