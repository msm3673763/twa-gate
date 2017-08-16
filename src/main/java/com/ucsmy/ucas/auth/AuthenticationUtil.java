package com.ucsmy.ucas.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.ucsmy.ucas.auth.config.AuthCenterConfig;
import com.ucsmy.ucas.auth.config.AuthClientConfig;
import com.ucsmy.ucas.auth.config.AuthenticationModel;
import com.ucsmy.ucas.auth.service.Impl.SysHttpRequestServerImpl;
import com.ucsmy.ucas.certification.service.impl.SysRedisServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用于存放统一认证中心配置
 * <ul>
 * <li>本地jeesite.properties需要定义参数三个
 * 1. redis存储access_token的key :  access_token_key
 * 2. 口令：app_client_id
 * 3. 秘钥： app_client_secret
 * </li>
 * <p>
 * </ul>
 *
 * @author ucs_fucong
 */
@Component
public class AuthenticationUtil {

    private static Logger log = LoggerFactory.getLogger(AuthenticationUtil.class);

    @Autowired
    private AuthCenterConfig authCenterApi;

    @Autowired
    SysRedisServiceImpl sysRedisService;

    @Autowired
    SysHttpRequestServerImpl sysHttpRequestServer;

    public String getAccessToken(AuthClientConfig clientConfig) {
        String tokenCacheKey = clientConfig.getAccessTokenKey();
        String accessToken = sysRedisService.getString(tokenCacheKey);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        } else {
            String url = genGetAccessTokenUrl();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("client_id", clientConfig.getClientId());
            paramMap.put("client_secret", clientConfig.getClientSecret());
            paramMap.put("grant_type",
                StringUtils.isEmpty(clientConfig.getGrantType()) ? "client_credentials" : clientConfig.getGrantType());
            paramMap.put("scope", clientConfig.getScope() != null ? clientConfig.getScope() : "client_ticket");
            String responseStr;
            try {
                responseStr = sysHttpRequestServer.sendHttpGetMap(url, paramMap);
            } catch (IOException e) {
                log.error("请求 [获取ACCESS_TOKEN] 接口失败！", e);
                return accessToken;
            }
            AuthenticationModel responseModel = JSON.parseObject(responseStr, new TypeReference<AuthenticationModel>() {
            });
            if (responseModel != null && responseModel.isSuccess()) {
                accessToken = responseModel.getAccessToken();
                sysRedisService.set(tokenCacheKey, accessToken, responseModel.getExpiresIn());
            } else {
                log.error("获取ACCESS_TOKEN失败！{}", responseStr);
                sysRedisService.delete(tokenCacheKey);
            }
        }
        return accessToken;
    }

    /**
     * 获取ticket
     *
     * @param authClientConfig
     * @param scope
     * @return
     */
    public String getClientTicket(AuthClientConfig authClientConfig, String scope) {
        String accessToken = getAccessToken(authClientConfig);
        String ticket = null;
        if (accessToken != null) {
            String accessTicketKey = authClientConfig.getAccessTicketKey();
            ticket = getCacheTicket(accessTicketKey, scope);
            if (ticket == null) {
                AuthenticationModel responseModel = getClientTicket(accessToken, scope);
                if (responseModel != null) {
                    ticket = responseModel.getCallTicket();
                    cacheTicket(accessTicketKey, scope, responseModel.getCallTicket(), responseModel.getExpiresIn());
                }
            } else {
                // ticket失败时token也重新获取
                sysRedisService.delete(authClientConfig.getAccessTokenKey());
                removeCacheTicket(accessTicketKey, scope);
            }
        }
        return ticket;
    }

    private void removeCacheTicket(String key, String scope) {
        sysRedisService.delete(key + ":" + scope);
    }

    private void cacheTicket(String key, String scope, String ticket, Integer expiresIn) {
        log.info("缓存ticket, key={}, scope={}, ticket={}, expiresIn={}", key, scope, ticket, expiresIn);
        sysRedisService.set(key + ":" + scope, ticket, expiresIn);
    }

    private String getCacheTicket(String key, String scope) {
        if (StringUtils.isEmpty(scope)) {
            return null;
        }
        String ticket = sysRedisService.getString(key + ":" + scope);
        log.info("获取缓存ticket, key={}, scope={}, ticket={}", key, scope, ticket);
        return ticket;
    }

    private AuthenticationModel getClientTicket(String accessToken, String scope) {
        String url = genGetTicketUrl();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("access_token", accessToken);
        paramMap.put("scope", scope);
        String responseStr;
        try {
            responseStr = sysHttpRequestServer.sendHttpGetMap(url, paramMap);
        } catch (IOException e) {
            log.error("请求 [获取三方接口ticket] 接口失败！", e);
            return null;
        }
        AuthenticationModel responseModel = JSON.parseObject(responseStr, new TypeReference<AuthenticationModel>() {
        });
        if (responseModel != null && responseModel.isSuccess()) {
            return responseModel;
        } else {
            log.error("获取TICKET失败！{}", responseStr);
        }
        return null;
    }

    private String genGetTicketUrl() {
        return authCenterApi.getUrl() + authCenterApi.getTicket();
    }

    private String genGetAccessTokenUrl() {
        return authCenterApi.getUrl() + authCenterApi.getAccessToken();
    }
}

