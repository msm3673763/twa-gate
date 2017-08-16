package com.ucsmy.ucas.auth.config;

/**
 * @author ucs_fucong
 * @date 2017/08/01
 */
public class AuthClientConfig {
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String scope;
    private String redirectUrl;
    private String accessTokenKey;
    private String accessTicketKey;
    private String authTicketKey;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getAccessTokenKey() {
        return accessTokenKey;
    }

    public String getAccessTicketKey() {
        return accessTicketKey;
    }

    public void setAccessTicketKey(String accessTicketKey) {
        this.accessTicketKey = accessTicketKey;
    }

    public void setAccessTokenKey(String accessTokenKey) {
        this.accessTokenKey = accessTokenKey;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthTicketKey() {
        return authTicketKey;
    }

    public void setAuthTicketKey(String authTicketKey) {
        this.authTicketKey = authTicketKey;
    }

    @Override
    public String toString() {
        return "AuthClientConfig{" +
            "clientId='" + clientId + '\'' +
            ", clientSecret='" + clientSecret + '\'' +
            ", grantType='" + grantType + '\'' +
            ", scope='" + scope + '\'' +
            ", redirectUrl='" + redirectUrl + '\'' +
            ", accessTokenKey='" + accessTokenKey + '\'' +
            ", accessTicketKey='" + accessTicketKey + '\'' +
            ", authTicketKey='" + authTicketKey + '\'' +
            '}';
    }
}
