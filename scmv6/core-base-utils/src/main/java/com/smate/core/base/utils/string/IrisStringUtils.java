package com.smate.core.base.utils.string;

import com.google.gdata.util.common.base.CharMatcher;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类.
 * 
 * @author liqinghua
 * 
 */
public class IrisStringUtils {
  private static final String[] ALLOW_STR = {"&lt;", "&gt;", "&amp;", "&apos;", "&quot;"};

  /**
   * 根据unicode长度截取字符串.
   * 
   * @param str
   * @param length
   * @return
   * @throws Exception
   */
  public static String bSubstring(String str, int length) throws Exception {

    if (str == null) {
      return str;
    }
    byte[] bytes = str.getBytes("Unicode");
    int n = 0; // 表示当前的字节数
    int i = 2; // 要截取的字节数，从第3个字节开始
    for (; i < bytes.length && n < length; i++) {
      // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
      if (i % 2 == 1) {
        n++; // 在UCS2第二个字节时n加1
      } else {
        // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
        if (bytes[i] != 0) {
          n++;
        }
      }
    }
    // 如果i为奇数时，处理成偶数
    if (i % 2 == 1)

    {
      // 该UCS2字符是汉字时，去掉这个截一半的汉字
      if (bytes[i - 1] != 0)
        i = i - 1;
      // 该UCS2字符是字母或数字，则保留该字符
      else
        i = i + 1;
    }

    return new String(bytes, 0, i, "Unicode");
  }

  // 去除增补字符 unicode说明定义的算法 计算出增补字符范围0x10000 至 0x10FFFF
  public static String filterSupplementaryChars(String text) {
    if (StringUtils.isBlank(text))
      return "";
    StringBuilder sb = new StringBuilder();

    char[] data = text.trim().toCharArray();
    for (int i = 0, len = data.length; i < len; i++) {

      char c = data[i];
      char high = c;
      char low;
      if (!Character.isHighSurrogate(high)) {
        sb.append(c);
        continue;

      }
      if (i + 1 == len) {
        break;
      }
      low = data[i + 1];
      // 先判断是否在代理范围（surrogate blocks）
      // 增补字符编码为两个代码单元，
      // 第一个单元来自于高代理（high surrogate）范围（0xD800 至 0xDBFF），
      // 第二个单元来自于低代理（low surrogate）范围（0xDC00 至 0xDFFF）。

      if (Character.isSurrogatePair(high, low)) { // 如果在代理范围

        int codePoint = Character.toCodePoint(high, low);
        if (Character.isSupplementaryCodePoint(codePoint)) {

          i++;

        } else {

          sb.append(c);

        }

      } else {

        sb.append(c);
      }

    }

    return sb.toString();
  }

  /**
   * 去除增补字符 unicode说明定义的算法 计算出增补字符范围0x10000 至 0x10FFFF<br/>
   * 过虑xml的无效字符<br/>
   * 处理&：如果&后面跟随的不是如下6个字符串（&lt; &gt; &amp; &apos; &quot;），则将&变为&amp;
   * 
   * @param data
   * @return
   */
  public static String filterSupplementaryCharsChina(String data) {
    StringBuilder sb = new StringBuilder();
    if (data == null) {
      return "";
    }
    int len = data.length();
    for (int i = 0; i < len; i++) {

      char c = data.charAt(i);
      char high = c;
      char low;

      // 过虑xml的无效字符： 0x00-0x08、0x0b-0x0c、0x0e-0x1f
      if ((c >= 0x00 && c <= 0x08) || (c >= 0x0b && c <= 0x0c) || (c >= 0x0e && c <= 0x1f)) {
        continue;
      }

      if (c == '&') {
        boolean isAllow = false;
        for (String str : ALLOW_STR) {
          if (i + str.length() > len) {
            continue;
          }

          int j = 0;
          for (; j < str.length(); j++) {
            if (data.charAt(i + j) != str.charAt(j)) {
              break;
            }
          }

          if (j == str.length()) {
            isAllow = true;
            break;
          }
        }
        if (!isAllow) {
          sb.append("&amp;");
          continue;
        }
      }

      if (!Character.isHighSurrogate(high)) {
        sb.append(c);
        continue;
      }
      if (i + 1 == len) {
        break;
      }
      low = data.charAt(i + 1);
      // 先判断是否在代理范围（surrogate blocks）
      // 增补字符编码为两个代码单元，
      // 第一个单元来自于高代理（high surrogate）范围（0xD800 至 0xDBFF），
      // 第二个单元来自于低代理（low surrogate）范围（0xDC00 至 0xDFFF）。
      if (Character.isSurrogatePair(high, low)) { // 如果在代理范围

        int codePoint = Character.toCodePoint(high, low);
        if (Character.isSupplementaryCodePoint(codePoint)) {

          i++;

        } else {
          sb.append(c);
        }

      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static String filterIllegalXmlChar(final String str) {
    String result = null;
    result = filterSupplementaryChars(str);
    result = CharMatcher.WHITESPACE.trimFrom(result);
    result = CharMatcher.JAVA_ISO_CONTROL.removeFrom(result);

    return result;

  }

  public static String filterXmlStr(final String str) {
    if (StringUtils.isEmpty(str)) {
      return "";
    } else {
      return filterIllegalXmlChar(str);
    }
  }

  /**
   * 全角转半角.
   * 
   * @param full
   * @return String
   */
  public static String full2Half(String full) {
    if (StringUtils.isNotEmpty(full)) {
      char c[] = full.toCharArray();
      for (int i = 0; i < c.length; i++) {
        if (c[i] == '\u3000') {
          c[i] = ' ';
        } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
          c[i] = (char) (c[i] - 65248);
        }
      }
      return new String(c);
    } else {
      return "";
    }
  }

  /**
   * 判断字符串是否有中文词.
   * 
   * @param strName
   * @return
   */
  public static boolean hasChineseWord(String strName) {

    char[] ch = strName.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (isChineseChar(c)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断字符串是否有中文词.
   * 
   * @param strName
   * @return
   */
  public static boolean isAllChineseWord(String strName) {
    boolean result = true;
    if (StringUtils.isBlank(strName)) {
      return false;
    }
    char[] ch = strName.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (!isChineseChar(c)) {
        result = false;
        return result;
      }
    }
    return result;
  }

  /**
   * GENERAL_PUNCTUATION 判断中文的“号,CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号,HALFWIDTH_AND_FULLWIDTH_FORMS
   * 判断中文的，号.
   * 
   * @param c
   * @return
   */
  public static boolean isChineseChar(char c) {

    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
      return true;
    }
    return false;
  }

  /**
   * 数据库的字段，获取最大长度值
   * 
   * @param str
   * @param maxLength
   * @return
   */
  public static String subMaxLengthString(String str, int maxLength) {
    if (StringUtils.isBlank(str)) {
      return "";
    }
    if (maxLength < 0) {
      maxLength = 0;
    }
    if (str.length() <= maxLength) {
      return str;
    } else {
      String restult = str.substring(0, maxLength - 5);
      return restult + "...";
    }
  }

  /**
   * 处理空字符串
   * 
   * @param str
   * @return
   */
  public static String handlerNullStr(String str) {
    if (StringUtils.isBlank(str)) {
      return "";
    }
    return str.trim();
  }

  /**
   * 空对象默认返回空字符串
   * 
   * @param obj
   * @return
   */
  public static String defaultNullStr(Object obj) {
    return obj == null ? "" : obj.toString();
  }


  /**
   * 转化sql 定义字符 &. 将 &xxx; 转化为% 主要是给like 语句使用
   * 
   * @param content
   * @return
   */
  public static String tranSqlDefineChr(String content) {
    Pattern expPattern = Pattern.compile("(&[^&]+?;)");
    Matcher matcher = expPattern.matcher(content);
    while (matcher.find()) {
      String g = matcher.group();
      content = content.replace(g, "%");
    }
    return content;

  }

  /**
   * 得到最大字节长度的字符串
   * 汉字默认为三个字节  ，utf-8 编码
   * @param s
   * @param maxLen
   * @return
   */
  public static String getMaxCharacterLength(String s , int maxLen) {
    if(StringUtils.isBlank(s)){
      return "";
    }
    if(maxLen <0) maxLen = 0;
    StringBuffer sb = new StringBuffer();
    int length = 0;
    for (int i = 0; i < s.length(); i++) {
      int ascii = Character.codePointAt(s, i);
      if (ascii >= 0 && ascii <= 255) {
        length++;
      } else {
        length += 3;
      }
      if(length < maxLen){
        sb.append(s.charAt(i));
      }else{
        break;
      }
    }
    return sb.toString();
  }
  /**
   * 得到最大字节长度的字符串
   * 汉字默认为两个个字节  ，gbk 编码
   * @param s
   * @param maxLen
   * @return
   */
  public static String getMaxCharacterLength2(String s , int maxLen) {
    if(StringUtils.isBlank(s)){
      return "";
    }
    if(maxLen <0) maxLen = 0;
    StringBuffer sb = new StringBuffer();
    int length = 0;
    for (int i = 0; i < s.length(); i++) {
      int ascii = Character.codePointAt(s, i);
      if (ascii >= 0 && ascii <= 255) {
        length++;
      } else {
        length += 2;
      }
      if(length < maxLen){
        sb.append(s.charAt(i));
      }else{
        break;
      }
    }
    return sb.toString();
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    System.out.println(getMaxCharacterLength("aa的值太大",10));
  }
}
