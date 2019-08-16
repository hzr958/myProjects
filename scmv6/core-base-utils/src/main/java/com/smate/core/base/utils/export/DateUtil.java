/**
 * 
 */
package com.smate.core.base.utils.export;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yxs
 *
 * @since 2018年1月8日
 */
public class DateUtil {

  public static String getDateFormat(Date date, String formatStr) {
    if (StringUtils.isNotBlank(formatStr)) {
      return new SimpleDateFormat(formatStr).format(date);
    }
    return null;
  }

}
