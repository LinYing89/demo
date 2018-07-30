package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

public class ApplicationEventListener implements ApplicationListener {

    Logger logger = LoggerFactory.getLogger(ApplicationEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        logger.info("onApplicationEvent");
        if (applicationEvent instanceof ContextStartedEvent) {
            // 应用启动，需要在代码动态添加监听器才可捕获
            logger.info("ContextStartedEvent");
        }else if (applicationEvent instanceof ContextStoppedEvent) {
            // 应用停止
            logger.info("ContextStoppedEvent");
        }else if (applicationEvent instanceof ContextClosedEvent) {
            // 应用关闭
            logger.info("ContextClosedEvent");
        }
    }
}
