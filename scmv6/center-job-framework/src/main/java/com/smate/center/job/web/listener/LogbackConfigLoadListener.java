package com.smate.center.job.web.listener;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import com.smate.core.base.utils.string.StringUtils;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.LoggerFactory;

public class LogbackConfigLoadListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      String runEnv = System.getenv("RUN_ENV");
      runEnv = StringUtils.defaultString(runEnv, "dev");
      Matcher matcher = Pattern.compile("(dev|alpha|test|uat|run)(.*)$").matcher(runEnv);
      matcher.find();
      String group = StringUtils.defaultString(matcher.group(1));
      String configPath = "/log/logback-" + group + ".xml";
      URL configUrl = getClass().getResource(configPath);
      if(configUrl != null){
        JoranConfigurator configurator = new JoranConfigurator();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        configurator.doConfigure(configUrl);
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {

  }
}
