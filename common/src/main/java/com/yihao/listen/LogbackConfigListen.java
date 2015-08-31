package com.yihao.listen;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Administrator on 2015/8/31.
 */
public class LogbackConfigListen implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(LogbackConfigListen.class);
    private static final String CONFIG_LOCATION="logbackConfigLocation";
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //从web.xml中加载指定文件名的日志配置文件
        String logbackConfigCation = servletContextEvent.getServletContext().getInitParameter(CONFIG_LOCATION);
        String fn = servletContextEvent.getServletContext().getRealPath(logbackConfigCation);
        System.out.println("logback.xml路径=====>"+fn);
        try {
//            UrlResource urlResource=new UrlResource(logbackConfigCation);
            LoggerContext loggerContext=(LoggerContext)LoggerFactory.getILoggerFactory();
            loggerContext.reset();
            JoranConfigurator joranConfigurator=new JoranConfigurator();
            joranConfigurator.setContext(loggerContext);
//            joranConfigurator.doConfigure(urlResource.getFile().getAbsolutePath());
            joranConfigurator.doConfigure(fn);
            logger.debug("loaded slf4j configure file from {}", fn);
        } catch (JoranException e) {
            e.printStackTrace();
            logger.error("can loading slf4j configure file from " + fn, e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
