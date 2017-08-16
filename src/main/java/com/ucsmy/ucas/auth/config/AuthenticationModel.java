package com.ucsmy.ucas.auth.config;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 认证中心返回内容
 *
 * @author ucs_fucong
 */
public class AuthenticationModel implements Serializable {

    private static final long serialVersionUID = -8375642205455661401L;
    private String errcode;        //返回码
    private String errmsg;        //返回信息

    @JSONField(name="access_token")
    private String accessToken;  //认证令牌
    @JSONField(name="expires_in")
    private Integer expiresIn;    //认证令牌超时时间（单位：秒）
    @JSONField(name="refresh_token")
    private String refreshToken;

    // 第三方ticket
    @JSONField(name="call_ticket")
    private String callTicket;

    protected AuthenticationModel() {

    }

    public AuthenticationModel(String errcode) {
        this.errcode = errcode;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getCallTicket() {
        return callTicket;
    }

    public void setCallTicket(String callTicket) {
        this.callTicket = callTicket;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isSuccess() {
        return errcode == null || "".equals(errcode) || "0".equals(errcode);
    }

    @Override
    public String toString() {
        return "AuthenticationModel={" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", access_token='" + accessToken + '\'' +
                ", refresh_token='" + refreshToken + '\'' +
                ", expires_in=" + expiresIn +
                ", call_ticket='" + callTicket + '\'' +
                '}';
    }
}
