package com.wechat.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 微信发送消息模型
 */
public class Message {

    /**消息类型*/
    @JSONField(name="Type")
    private String Type;

    /**消息内容*/
    @JSONField(name="Content")
    private String Content;

    /**发送人*/
    @JSONField(name="FromUserName")
    private String FromUserName;

    /**接收人*/
    @JSONField(name="ToUserName")
    private String ToUserName;

    /**不知何意*/
    @JSONField(name="LocalID")
    private String LocalID;

    /**消息ID，每次发送的时候必须不一样*/
    @JSONField(name="ClientMsgId")
    private String ClientMsgId;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getLocalID() {
        return LocalID;
    }

    public void setLocalID(String localID) {
        LocalID = localID;
    }

    public String getClientMsgId() {
        return ClientMsgId;
    }

    public void setClientMsgId(String clientMsgId) {
        ClientMsgId = clientMsgId;
    }
}
