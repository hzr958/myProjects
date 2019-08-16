package com.smate.center.batch.service.mail;

/**
 * 短信－存储构建短信或短信邮件需要参数的线程变量.
 * 
 * @author pwl
 * 
 */
public class ThreadLocalInsideMailParamBuilder {

  private static ThreadLocal<InsideMailParamBuilder> insideMailParamBuilder = new ThreadLocal<InsideMailParamBuilder>();

  public static InsideMailParamBuilder getInsideMailParamBuilder() {
    return insideMailParamBuilder.get();
  }

  public static void setInsideMailParamBuilder(InsideMailParamBuilder paramBuilder) {
    insideMailParamBuilder.set(paramBuilder);
  }

}
