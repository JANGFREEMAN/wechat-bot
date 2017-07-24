package com.wechat.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 微信基本请求模型
 */
public class BaseRequest {

    /**用户唯一标识*/
    @JSONField(name="Uin")
    private String Uin;

    @JSONField(name="Sid")
    private String Sid;

    @JSONField(name="Skey")
    private String Skey;

    @JSONField(name="DeviceID")
    private String DeviceID;

    public String getUin() {
        return Uin;
    }

    public void setUin(String uin) {
        Uin = uin;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getSkey() {
        return Skey;
    }

    public void setSkey(String skey) {
        Skey = skey;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }
}
