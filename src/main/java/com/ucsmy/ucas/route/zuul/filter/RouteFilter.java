package com.ucsmy.ucas.route.zuul.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.ucsmy.commons.utils.JsonUtils;
import com.ucsmy.ucas.route.rabbitmq.Send;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由转发过滤器
 * Created by ucs_xujunwei on 2017/5/12.
 */
public class RouteFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZuulFilter.class);

    @Autowired
    private Send mq;

    /**
     * 返回这个过滤器的类型
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 返回这个过滤器在过滤器链的顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * true启动
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 具体逻辑
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        if(requestURI.equals(contextPath + "/oauth/accessToken")){
            String deviceNo = request.getParameter("deviceNo");
            String account = request.getParameter("username");
            String accountName = request.getParameter("username");
            Date date = new Date();
            Map<String, Object> map = new HashMap<>();
            map.put("deviceNo", deviceNo);
            map.put("account", account);
            map.put("accountName", accountName);
            map.put("loginDate", date.getTime());
            try {
                String data = JsonUtils.formatObjectToJson(map);
                mq.sendMsg(data);
            } catch (JsonProcessingException e) {
                LOGGER.error("路由转发过滤器方法异常", e);
            } catch (Exception e) {
                LOGGER.error("路由转发过滤器方法异常", e);
            }
        }
        return null;
    }

}