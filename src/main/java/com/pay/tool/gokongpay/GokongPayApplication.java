package com.pay.tool.gokongpay;

import cn.hutool.log.StaticLog;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.pay.tool.gokongpay.dao")
public class GokongPayApplication {


    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector()); // 添加http
        return tomcat;
    }

    // 配置http
    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(81);
        connector.setSecure(false);
        connector.setRedirectPort(8043);
        return connector;
    }

    public static void main(String[] args) {
        /**
         * 隐藏banner启动方式
         */
        SpringApplication springApplication = new SpringApplication(GokongPayApplication.class);
        //设置banner的模式为控制台显示
        springApplication.setBannerMode(Banner.Mode.CONSOLE);
        //启动springboot应用程序
        springApplication.run(args);
        StaticLog.error("========================启动完毕========================");
    }


}
