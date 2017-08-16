package com.ucsmy.ucas.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ucs_fucong
 * @date 2017/08/03
 */
@Component
@ConfigurationProperties("thirdparty.uca-api")
public class UCAApiConfig {

    private String url;

    private ThirdPartyApi downloadCrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ThirdPartyApi getDownloadCrl() {
        return downloadCrl;
    }

    public void setDownloadCrl(ThirdPartyApi downloadCrl) {
        this.downloadCrl = downloadCrl;
    }

    @Override
    public String toString() {
        return "UCAApiConfig{" +
            "url='" + url + '\'' +
            ", downloadCrl=" + downloadCrl +
            '}';
    }
}
