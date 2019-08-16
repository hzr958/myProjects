package com.smate.center.task.service.email;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

/**
 * 发送邮件的基础服务类.
 * 
 * @author mjg
 * 
 */
public final class EmailInfoBaseMethod {

  /**
   * 获取下一次执行时间<以当前时间为准>.
   * 
   * @param addTime 增加的小时数.
   * @return
   */
  public static final Date getNextExeTime(Date curDate, int addTime) {
    Calendar cal = Calendar.getInstance();
    if (curDate == null)
      curDate = new Date();
    cal.setTime(curDate);
    cal.add(Calendar.HOUR_OF_DAY, addTime);
    return cal.getTime();

  }

  /**
   * 获取出错日志信息.
   * 
   * @param e
   * @return
   */
  public static final String getErrorMsg(Exception e) {
    Writer result = new StringWriter();
    PrintWriter printWriter = new PrintWriter(result);
    e.printStackTrace(printWriter);
    return result.toString();
  }
}
