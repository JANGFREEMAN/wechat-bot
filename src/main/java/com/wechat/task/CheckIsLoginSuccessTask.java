package com.wechat.task;

import com.wechat.web.WebWechatClient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 检测是否成功登录调度
 */
public class CheckIsLoginSuccessTask extends TimerTask{


    private WebWechatClient wwc;

    private Timer timer;


    public CheckIsLoginSuccessTask(WebWechatClient wwc, Timer timer){
        this.wwc = wwc;
        this.timer = timer;
    }

    public void run() {
        if(wwc.getIsLogin()){
            System.out.println("登录成功！！！！！！");
            //取消任务调度
            timer.cancel();
            //已登录，做一些初始化操作。
            wwc.weChatInit();
        }
    }


}
