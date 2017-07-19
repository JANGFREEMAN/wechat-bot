package com.wechat.inter;

/**
 * 微信api接口常量配置
 */
public interface APIConstat {

    public static String GET_UUID = "https://login.weixin.qq.com/jslogin";

    public static String GET_QRCODE = "https://login.weixin.qq.com/qrcode";

    public static String QRCODE_STATUS = "https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login";

    public static String SYNCHECK = "https://webpush.wx.qq.com/cgi-bin/mmwebwx-bin/synccheck";

    public static String WEB_WX_SYNC = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsync";

    public static String GET_CONTACT = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact";

}
