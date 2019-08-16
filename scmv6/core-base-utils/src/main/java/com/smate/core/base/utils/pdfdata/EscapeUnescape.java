package com.smate.core.base.utils.pdfdata;

/**
 * 仿js中的escape和unescape
 * 
 * @author HWQ
 *
 */
public class EscapeUnescape {
  /**
   * 编译
   * 
   * @param srcStr
   * @return
   * 
   *         HWQ
   */
  public static String escape(String srcStr) {
    int i;
    char j;
    StringBuffer tmp = new StringBuffer();
    tmp.ensureCapacity(srcStr.length() * 6);
    for (i = 0; i < srcStr.length(); i++) {
      j = srcStr.charAt(i);
      if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
        tmp.append(j);
      } else if (j < 256) {
        tmp.append("%");
        if (j < 16) {
          tmp.append("0");
        }
        tmp.append(Integer.toString(j, 16));
      } else {
        tmp.append("%u");
        tmp.append(Integer.toString(j, 16));
      }
    }
    return tmp.toString();
  }

  /**
   * 反编译
   * 
   * @param srcStr
   * @return
   * 
   *         HWQ
   */
  public static String unescape(String srcStr) {
    StringBuffer tmp = new StringBuffer();
    tmp.ensureCapacity(srcStr.length());
    int lastPos = 0, pos = 0;
    char ch;
    while (lastPos < srcStr.length()) {
      pos = srcStr.indexOf("%", lastPos);
      if (pos == lastPos) {
        if (srcStr.charAt(pos + 1) == 'u') {
          ch = (char) Integer.parseInt(srcStr.substring(pos + 2, pos + 6), 16);
          tmp.append(ch);
          lastPos = pos + 6;
        } else {
          ch = (char) Integer.parseInt(srcStr.substring(pos + 1, pos + 3), 16);
          tmp.append(ch);
          lastPos = pos + 3;
        }
      } else {
        if (pos == -1) {
          tmp.append(srcStr.substring(lastPos));
          lastPos = srcStr.length();
        } else {
          tmp.append(srcStr.substring(lastPos, pos));
          lastPos = pos;
        }
      }
    }
    return tmp.toString();
  }

  /**
   * 将字符串重新编码Gb2312
   * 
   * @param src
   * @return
   * 
   *         HWQ
   * @throws Exception
   */
  public static String strToGB2312(String srcStr) throws Exception {
    String strRet = null;
    try {
      strRet = new String(srcStr.getBytes("ISO_8859_1"), "GB2312");
    } catch (Exception e) {
      throw new Exception("change to gb2312 code error!");
    }
    return strRet;
  }

  /**
   * @disc 将字符串重新编码UTF-8
   * @param src
   * @return
   * 
   *         HWQ
   * @throws Exception
   */
  public static String strToUTF8(String srcStr) throws Exception {
    String strRet = null;
    try {
      strRet = new String(srcStr.getBytes("ISO_8859_1"), "UTF-8");
    } catch (Exception e) {
      throw new Exception("change to utf8 code error!");
    }
    return strRet;
  }

}
