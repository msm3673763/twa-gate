package com.ucsmy.ucas.certification.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.ucsmy.ucas.auth.AuthenticationUtil;
import com.ucsmy.ucas.auth.config.AuthCenterConfig;
import com.ucsmy.ucas.auth.config.UCAApiConfig;
import com.ucsmy.ucas.auth.service.Impl.SysHttpRequestServerImpl;
import com.ucsmy.ucas.certification.ext.CrlConfig;
import com.ucsmy.ucas.certification.service.CertificationService;
import com.ucsmy.ucas.commons.aop.exception.result.AosResult;
import com.ucsmy.ucas.config.UCARespBase;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
@Service
public class CertificationServiceImpl implements CertificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationServiceImpl.class);

    //@Autowired
    //private SysCacheService sysRedisService;
    //
    //@Autowired
    //private MongoFileMapper mongoFileMapper;

    @Autowired
    private CrlConfig crlConfig;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    @Autowired
    private SysHttpRequestServerImpl sysHttpRequestServer;

    @Autowired
    private UCAApiConfig ucaApi;

    @Autowired
    private AuthCenterConfig authCenterConfig;

    @Autowired
    private UCAApiConfig ucaApiConfig;

    ///**
    // * 刷新crl
    // * 从redis获取最新的crl，如果redis的不存在，则从mongodb里面获取
    // *
    // * @return
    // */
    //@Override
    //public AosResult refreshCrl() {
    //    FileOutputStream out = null;
    //    try {
    //        //先获取redis的数据
    //        String crlRedis = sysRedisService.getString(crlConfig.getRedisPath());
    //        byte[] bytes;
    //        if (crlRedis != null) {
    //            bytes = Base64.decodeBase64(crlRedis);
    //        } else {
    //            //从mongodb里面拿
    //            CaCerMongoFile crlMongoFile = mongoFileMapper.findOne(crlConfig.getMongoId());
    //            if (crlMongoFile == null) {
    //                return AosResult.retFailureMsg("crl文件不存在，刷新失败");
    //            }
    //            bytes = crlMongoFile.getFileData();
    //        }
    //        File dir = new File(crlConfig.getLocalPathDir());
    //        if (!dir.exists()) {
    //            dir.mkdirs();
    //        }
    //        File file = new File(crlConfig.getLocalPathDir() + crlConfig.getLocalPathFile());
    //        out = new FileOutputStream(file);
    //        out.write(bytes);
    //        return AosResult.retSuccessMsg("刷新成功");
    //    } catch (Exception e) {
    //        LOGGER.error("刷新url方法异常", e);
    //        return AosResult.retFailureMsg("刷新失败");
    //    } finally {
    //        if (out != null) {
    //            try {
    //                out.close();
    //            } catch (IOException e) {
    //                LOGGER.error("刷新url方法异常", e);
    //            }
    //        }
    //    }
    //}

    @Override
    public AosResult refreshCrl() {
        try {
            String crlFileStr = downloadCrl();
            if (crlFileStr != null) {
                byte[] bytes = Base64.decodeBase64(crlFileStr);
                File dir = new File(crlConfig.getLocalPathDir());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(crlConfig.getLocalPathDir() + crlConfig.getLocalPathFile());
                Files.write(bytes, file);
            }
        } catch (Exception e) {
            return AosResult.retFailureMsg("刷新失败");
        }
        return AosResult.retSuccessMsg("刷新成功");
    }

    private String downloadCrl() {
        //Get ticket
        String ticket = authenticationUtil.getClientTicket(authCenterConfig.getDra(),
            ucaApiConfig.getDownloadCrl().getScope());

        Map<String, Object> params = Maps.newHashMap();
        params.put("ticket", ticket);
        String requestStr = JSON.toJSONString(params);
        LOGGER.info("[UCA]downloadCrl req: {}", requestStr);
        String responseStr = null;
        try {
            responseStr = sysHttpRequestServer.sendHttpPostJson(
                ucaApi.getUrl() + ucaApi.getDownloadCrl().getApi(),
                requestStr);
            LOGGER.info("responseStr={}", responseStr);
        } catch (IOException e) {
            LOGGER.error("[UCA]downloadCrl FAIL！", e);
        }
        UCARespBase<String> resp = JSON.parseObject(responseStr,
            new TypeReference<UCARespBase<String>>() {});

        if (resp != null && resp.isSuccess()) {
            return resp.getData();
        } else {
            LOGGER.warn("[UCA]downloadCrl resp: ", responseStr);
        }
        return null;
    }
}
