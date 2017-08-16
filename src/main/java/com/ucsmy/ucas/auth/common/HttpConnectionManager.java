package com.ucsmy.ucas.auth.common;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ucs_mojiazhou on 2017/5/4.
 */
@Component
public class HttpConnectionManager {

    private static final int MAXTOTAL = 200;//最大连接数
    private static final int DEFAULTMAXPERROUTE = 2;//默认路由
    private static final int SOCKETTIMEOUT = 10000;
    private static final int CONNECTTIMEOUT = 10000;
    private static final int CONNECTIONREQUESTTIMEOUT = 10000;
    private static Logger log = LoggerFactory.getLogger(HttpConnectionManager.class);
    PoolingHttpClientConnectionManager cm = null;
    PoolingHttpClientConnectionManager cms = null;
    SSLConnectionSocketFactory sslConnectionSocketFactory = null;
    private RequestConfig requestConfig;//请求配置

    @Value("${thirdparty.uca-api.https-host-verifier}")
    private boolean HOST_VERIFIER;

    @PostConstruct
    public void init() {

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", new PlainConnectionSocketFactory())
            .build();
        cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(MAXTOTAL);
        cm.setDefaultMaxPerRoute(DEFAULTMAXPERROUTE);

        SSLContext sslcontext = null;
        try {
            sslcontext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 默认信任所有证书
                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.error("init erorr", e);
        }

        if (HOST_VERIFIER) {
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslcontext, new String[] {"TLSv1", "SSLv3"}, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        }else {
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslcontext, NoopHostnameVerifier.INSTANCE);
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistrys = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", sslConnectionSocketFactory)
            .build();
        cms = new PoolingHttpClientConnectionManager(socketFactoryRegistrys);
        cms.setMaxTotal(MAXTOTAL);
        cms.setDefaultMaxPerRoute(DEFAULTMAXPERROUTE);

        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(
            CONNECTIONREQUESTTIMEOUT).setSocketTimeout(SOCKETTIMEOUT).setConnectTimeout(
            CONNECTTIMEOUT).build();
    }

    /**
     * http请求
     */
    public CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(cm)
            .setDefaultRequestConfig(requestConfig)
            .setRetryHandler(this.requestRetryHandler())
            .build();
        return httpClient;
    }

    /****
     * https请求
     *
     * @return
     */
    public CloseableHttpClient getHttpsClient() {

        CloseableHttpClient httpclient = HttpClients.custom()
            .setSSLSocketFactory(sslConnectionSocketFactory)
            .setConnectionManager(cms)
            .setDefaultRequestConfig(requestConfig)
            .setRetryHandler(this.requestRetryHandler())
            .build();
        return httpclient;
    }

    /***
     * 重试处理
     **/
    private HttpRequestRetryHandler requestRetryHandler() {
        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
            return requestRetryJudge(exception, executionCount, context);
        };
        return httpRequestRetryHandler;
    }

    private boolean requestRetryJudge(IOException exception, int executionCount, HttpContext context) {
        if (executionCount >= 2) {// 如果已经重试了2次，就放弃
            return false;
        }

        HttpRequest request = HttpClientContext.adapt(context).getRequest();
        // 如果请求是幂等的，就再次尝试
        if (!(request instanceof HttpEntityEnclosingRequest) ||
            (exception instanceof NoHttpResponseException
                || exception instanceof InterruptedIOException
                || exception instanceof ConnectTimeoutException)) {
            // 如果服务器丢掉了连接，那么就重试
            return true;
        } else if (exception instanceof SSLHandshakeException
            || exception instanceof UnknownHostException
            || exception instanceof SSLException) {// 不要重试SSL握手异常
            return false;
        }

        return false;
    }
}
