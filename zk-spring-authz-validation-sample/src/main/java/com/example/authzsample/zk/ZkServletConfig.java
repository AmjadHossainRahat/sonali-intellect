package com.example.authzsample.zk;

import jakarta.servlet.Servlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;

@Configuration
public class ZkServletConfig {

    @Bean
    public ServletRegistrationBean<Servlet> zkLoaderServlet() {
        ServletRegistrationBean<Servlet> reg = new ServletRegistrationBean<>(new DHtmlLayoutServlet(), "*.zul");
        reg.setName("zkLoader");
        reg.addInitParameter("update-uri", "/zkau");
        return reg;
    }

    @Bean
    public ServletRegistrationBean<Servlet> zkAuServlet() {
        ServletRegistrationBean<Servlet> reg = new ServletRegistrationBean<>(new DHtmlUpdateServlet(), "/zkau/*");
        reg.setName("zkau");
        return reg;
    }
}
