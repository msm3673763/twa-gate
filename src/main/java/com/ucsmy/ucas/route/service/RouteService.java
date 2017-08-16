package com.ucsmy.ucas.route.service;


import com.ucsmy.ucas.commons.aop.exception.result.AosResult;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;

/**
 * Created by ucs_xujunwei on 2017/4/26.
 */
public interface RouteService {

    /**
     *  刷新路由配置
     */
    void refreshRoute();
}
