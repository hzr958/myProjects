package com.smate.core.base.utils.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.smate.core.base.utils.factory.config.DynamicServerConfig;

/**
 * 自定义启动监听
 * 
 * @author zk
 *
 */
public class StartupListener implements ServletContextListener {
  static Logger logger = LoggerFactory.getLogger(StartupListener.class);

  @Override
  public void contextDestroyed(ServletContextEvent event) {

  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    logger.debug("Initializing context...");
    ServletContext context = event.getServletContext();
    setupContext(context);
    logger.debug("initialization complete");
  }

  /**
   * 加载常量等信息 放入context中.
   * 
   * @param context The servlet context
   */
  public static void setupContext(ServletContext context) {
    ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

    DynamicServerConfig app = (DynamicServerConfig) ctx.getBean("propertyPlaceholderConfigurer");
    try {
      Properties props = app.mergeProperties();
      if (props != null) {
        for (Iterator<Object> iterator = props.keySet().iterator(); iterator.hasNext();) {
          String name = (String) iterator.next();
          context.setAttribute(name, props.getProperty(name) == null ? "" : props.getProperty(name));
        }
      }
    } catch (IOException e) {
      logger.error("startup properties error :", e);
    }

  }

}
