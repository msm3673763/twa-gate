package com.ucsmy.ucas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@ServletComponentScan
@SpringCloudApplication
@EnableZuulProxy
public class UcasCoreApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {
		SpringApplication.run(UcasCoreApplication.class, args);

	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UcasCoreApplication.class);
    }

}
