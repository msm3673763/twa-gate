package com.ucsmy.ucas.filter;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.ucsmy.ucas.certification.ext.CrlConfig;
import com.ucsmy.ucas.certification.service.SysCacheService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 证书验证拦截器
 *
 * @author ucs_fucong
 */
@Component
public class VerifyFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(VerifyFilter.class);
    @Autowired
    private SysCacheService sysRedisService;

    @Autowired
    private CrlConfig crlConfig;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        try {
            X509Certificate[] certs = (X509Certificate[])request.getAttribute(
                "javax.servlet.request.X509Certificate");
            if (certs == null || certs.length == 0) {
                setFailResp(ctx, "证书验证失败");
                return null;
            }
            X509Certificate cert = certs[0];
            cert.checkValidity();

            //获取redis的数据
            log.info("redis:path={}", crlConfig.getRedisPath());
            byte[] bytes = null;
            try {
                String crlRedis = null;
                crlRedis = sysRedisService.getString(crlConfig.getRedisPath());
                if (crlRedis != null) {
                    bytes = Base64.decodeBase64(crlRedis);
                }
            } catch (Exception e) {
                bytes = readLocalCrl(crlConfig.getLocalPathDir() + "/" + crlConfig.getLocalPathFile());
            }

            if (bytes != null) {
                boolean isRevoked = isRevoked(cert, bytes);
                if (isRevoked) {
                    log.info("证书已被吊销");
                    setFailResp(ctx, "证书已被吊销");
                    return null;
                }
            } else {
                log.info("证书吊销信息不存在...");
            }
        } catch (CertificateExpiredException e) {
            log.info("证书已过期");
            setFailResp(ctx, "证书已过期");
        } catch (CertificateNotYetValidException e) {
            log.info("证书已过期");
            setFailResp(ctx, "证书已过期");
        } catch (Exception e) {
            log.error("证书验证执行异常", e);
            setFailResp(ctx, "证书验证失败");
        }
        return null;
    }

    private byte[] readLocalCrl(String pathStr) {
        log.info("读取本地crl={}" + pathStr);
        try {
            Path path = Paths.get(pathStr);
            if (path.toFile().exists()) {
                return Files.readAllBytes(path);
            }
        } catch (Exception e) {
            log.error("读取本地crl失败", e);
        }
        return null;
    }

    private boolean isRevoked(X509Certificate cert, byte[] bytes) throws CertificateException, CRLException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509CRL crl = (X509CRL)cf.generateCRL(new ByteArrayInputStream(bytes));
        return crl.isRevoked(cert);
    }

    private void setFailResp(RequestContext ctx, String summary) {
        ctx.setSendZuulResponse(false);
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
        ctx.setResponseStatusCode(401);
        CertificateValidityException authTicketException = new CertificateValidityException(summary);
        ctx.setResponseBody(authTicketException.toJson());
    }
}
