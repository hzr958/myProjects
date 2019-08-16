package com.smate.core.base.utils.string;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 获取首字母.
 * 
 * @author chenxiangrong
 * 
 */
public class StrUtils {
  private static final int[] LISECPOSVALUE = {1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472,
      3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};

  private static final String[] LCFIRSTLETTER = {"a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "o",
      "p", "q", "r", "s", "t", "w", "x", "y", "z"};

  /**
   * 
   * 取得给定汉字串的首字母串,即声母串.
   * 
   * 
   * 
   * @param str 给定汉字串
   * 
   * @return 声母串
   */

  public static String getAllFirstLetter(String str) {

    if (str == null || str.trim().length() == 0) {
      return "";
    }
    String str1 = "";
    for (int i = 0; i < str.length(); i++) {
      str1 = str1 + getFirstLetter(str.substring(i, i + 1));
    }
    return str1;
  }

  /**
   * 
   * 取得给定汉字的首字母,即声母.
   * 
   * 
   * 
   * @param chinese 给定的汉字
   * 
   * @return 给定汉字的声母
   */

  public static String getFirstLetter(String chinese) {
    if (chinese == null || chinese.trim().length() == 0) {
      return "";
    }
    chinese = conversionStr(chinese, "GBK", "ISO8859-1");
    if (chinese.length() > 1) {// 判断是不是汉字
      int liSectorCode = (int) chinese.charAt(0); // 汉字区码
      int liPositionCode = (int) chinese.charAt(1); // 汉字位码

      liSectorCode = liSectorCode - 160;
      liPositionCode = liPositionCode - 160;
      int liSecPosCode = liSectorCode * 100 + liPositionCode; // 汉字区位码
      if (liSecPosCode > 1600 && liSecPosCode < 5590) {
        for (int i = 0; i < 23; i++) {
          if (liSecPosCode >= LISECPOSVALUE[i] && liSecPosCode < LISECPOSVALUE[i + 1]) {
            chinese = LCFIRSTLETTER[i];
            break;
          }
        }

      } else {// 非汉字字符,如图形符号或ASCII码
        chinese = conversionStr(chinese, "ISO8859-1", "GBK");
        chinese = chinese.substring(0, 1);
      }
    }
    return chinese;
  }

  /**
   * 
   * 字符串编码转换.
   * 
   * 
   * 
   * @param str 要转换编码的字符串
   * 
   * @param charsetName 原来的编码
   * 
   * @param toCharsetName 转换后的编码
   * 
   * @return 经过编码转换后的字符串
   */

  private static String conversionStr(String str, String charsetName, String toCharsetName) {
    try {
      str = new String(str.getBytes(charsetName), toCharsetName);
    } catch (UnsupportedEncodingException ex) {
      System.out.println("字符串编码转换异常：" + ex.getMessage());
    }
    return str;
  }

  public static String toXmlChar(String str) {
    if (StringUtils.isBlank(str))
      return "";
    str = str.replace("&amp;amp;", "&amp;");
    str = str.replace(" & ", "&amp;");
    return str;
  }

  /**
   * 过滤特殊字符.
   * 
   * @param info
   * @return
   */
  public static String filterSpecChar(String info) {
    return StringUtils.trimToNull(info.replaceAll(
        "[\\~|\\`|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\-|\\|\\_|\\=|\\+|\\{|\\}|\\[|\\]|\\:|\\;|\"|\\'|\\||\\|\\<|\\>|\\,|\\.|\\?|\\/|『|』|￥|…|×|（|）|——|]",
        ""));
  }

  public static boolean isEmail(String str) {
    // String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*" ;
    String regex = "[a-z0-9A-Z]+[a-z0-9A-Z_\\-.]*@([a-z0-9A-Z\\-]+\\.)+[a-zA-Z\\-]{2,10}";
    return match(regex, str);
  }

  /**
   * @param regex 正则表达式字符串
   * @param str 要匹配的字符串
   * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
   */
  private static boolean match(String regex, String str) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(str);
    return matcher.matches();
  }
}
