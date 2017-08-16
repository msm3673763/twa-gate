package com.ucsmy.ucas.certification.ext;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ucs_xujunwei on 2017/8/9.
 */
@Component
@ConfigurationProperties(prefix = "crl")
public class CrlConfig {
    private String redisPath;
    private String mongoId;
    private String localPathDir;
    private String localPathFile;

    public String getRedisPath() {
        return redisPath;
    }

    public void setRedisPath(String redisPath) {
        this.redisPath = redisPath;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }


    public String getLocalPathDir() {
        return localPathDir;
    }

    public void setLocalPathDir(String localPathDir) {
        this.localPathDir = localPathDir;
    }

    public String getLocalPathFile() {
        return localPathFile;
    }

    public void setLocalPathFile(String localPathFile) {
        this.localPathFile = localPathFile;
    }
}
