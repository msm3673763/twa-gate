package com.ucsmy.ucas.route.zuul.locator;

import com.esotericsoftware.yamlbeans.YamlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujunwei on 2017/5/18.
 */
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator{

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomRouteLocator.class);

    public static final String YML_FILE = "/config/route.yml";

    private ZuulProperties properties;

    public CustomRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        LOGGER.info("servletPath:{}",servletPath);
    }

    @Override
    protected void doRefresh() {
        super.doRefresh();
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<String, ZuulRoute>();
        //从application.yml中加载路由信息
        routesMap.putAll(super.locateRoutes());
        //从yml中加载路由信息
        routesMap.putAll(locateRoutesFromProperties());
        LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    private Map<String, ZuulRoute> locateRoutesFromProperties(){
        Map<String, ZuulRoute> routes = new LinkedHashMap<>();
        try {
            YamlReader reader = new YamlReader(new FileReader(new ClassPathResource(YML_FILE).getFile()));
            List<ZuulRoute> zuulRoutes = reader.read(List.class, ZuulRoute.class);
            if(zuulRoutes != null){
                for (ZuulRoute zuulRoute : zuulRoutes) {
                    if(StringUtils.isEmpty(zuulRoute.getPath()) || StringUtils.isEmpty(zuulRoute.getUrl()) ){
                        continue;
                    }
                    routes.put(zuulRoute.getPath(), zuulRoute);
                }
            }
        } catch (Exception e) {
            LOGGER.error("初始化路由配置表方法异常", e);
        }
        return routes;
    }

}