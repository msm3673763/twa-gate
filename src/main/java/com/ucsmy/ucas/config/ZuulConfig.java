package com.ucsmy.ucas.config;

import com.ucsmy.ucas.route.zuul.filter.RouteFilter;
import com.ucsmy.ucas.route.zuul.locator.CustomRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xujunwei on 2017/5/18.
 */
//@Configuration
public class ZuulConfig {

    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;

    @Bean
    public CustomRouteLocator routeLocator() {
        CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServletPrefix(), this.zuulProperties);
        return routeLocator;
    }

    @Bean
    public RouteFilter routeFilter() {
        return new RouteFilter();
    }

}