package com.ucsmy.ucas.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.yml：aos配置
 * Created by ucs_liyongkang on 2017/8/6.
 */
@Component
@ConfigurationProperties("thirdparty.aos-api")
public class AOSApiConfig {
    private String url;

    private ThirdPartyApi aosApiManage;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ThirdPartyApi getAosApiManage() {
        return aosApiManage;
    }

    public void setAosApiManage(ThirdPartyApi aosApiManage) {
        this.aosApiManage = aosApiManage;
    }
}
