package com.ucsmy.ucas.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fortune
 */
@Component
@ConfigurationProperties("thirdparty.auth-center")
public class AuthCenterConfig {

    private String url;
    private String accessToken;
    private String ticket;
    private String refreshToken;

    private AuthClientConfig dra;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AuthClientConfig getDra() {
        return dra;
    }

    public void setDra(AuthClientConfig dra) {
        this.dra = dra;
    }

    @Override
    public String toString() {
        return "AuthCenterApi{" +
            "url='" + url + '\'' +
            ", accessToken='" + accessToken + '\'' +
            ", ticket='" + ticket + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", dra=" + dra +
            '}';
    }
}
