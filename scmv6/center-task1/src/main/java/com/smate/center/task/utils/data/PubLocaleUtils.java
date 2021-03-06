package com.smate.center.task.utils.data;

import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class PubLocaleUtils {
  /**
   * 判断中英文情况，判断依据：存在一个中文字符则是中文环境<br/>
   * "aa" Locale.US <br/>
   * "说说" Locale.CHINA <br/>
   * "说说aa" Locale.CHINA <br/>
   * "说,@说aa" Locale.CHINA <br/>
   * 
   * @param title
   * @return
   */
  public static Locale getLocale(String title) {
    if (StringUtils.isEmpty(title)) {
      return Locale.US;
    }
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    if (p.matcher(title).find()) {
      return Locale.CHINA;
    }
    return Locale.US;
  }

}
