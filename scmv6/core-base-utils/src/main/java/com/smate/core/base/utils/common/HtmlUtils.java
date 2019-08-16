package com.smate.core.base.utils.common;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理html保留字符的转义.
 * 
 * @author chenxiangrong
 * 
 */
public class HtmlUtils {
  public static String toHtml(String str) {

    if (str == null)
      return null;
    StringBuffer sb = new StringBuffer();

    int len = str.length();
    for (int i = 0; i < len; i++) {
      char c = str.charAt(i);
      switch (c) {
        case ' ':
          sb.append("&nbsp;");
          break;
        case '\n':
          sb.append("<br>");
          break;
        case '\r':
          break;
        case '\'':
          sb.append("&#39;");
          break;
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '&':
          sb.append("&amp;");
          break;
        case '"':
          sb.append("&#34;");
          break;
        case '\\':
          sb.append("&#92;");
          break;
        default:
          sb.append(c);
      }
    }
    return sb.toString();
  }

  public static String replaceBlank(String str) {
    Pattern p = Pattern.compile("\t|\r|\n");
    Matcher m = p.matcher(str);
    String after = m.replaceAll("");
    return after.trim();
  }

  public static String Html2Text(String inputString) {
    String htmlStr = inputString; // 含html标签的字符串
    String textStr = "";
    java.util.regex.Pattern p_script;
    java.util.regex.Matcher m_script;
    java.util.regex.Pattern p_style;
    java.util.regex.Matcher m_style;
    java.util.regex.Pattern p_html;
    java.util.regex.Matcher m_html;

    java.util.regex.Pattern p_html1;
    java.util.regex.Matcher m_html1;

    try {
      String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义ｓｃｒｉｐｔ的正则表达式{或<ｓｃｒｉｐｔ[^>]*?>[\\s\\S]*?<\\/ｓｃｒｉｐｔ>
      // }
      String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
      // }
      String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
      String regEx_html1 = "<[^>]+";
      p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
      m_script = p_script.matcher(htmlStr);
      htmlStr = m_script.replaceAll(""); // 过滤script标签

      p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
      m_style = p_style.matcher(htmlStr);
      htmlStr = m_style.replaceAll(""); // 过滤style标签

      p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
      m_html = p_html.matcher(htmlStr);
      htmlStr = m_html.replaceAll(""); // 过滤html标签

      p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
      m_html1 = p_html1.matcher(htmlStr);
      htmlStr = m_html1.replaceAll(""); // 过滤html标签

      textStr = htmlStr;

    } catch (Exception e) {
    }

    return textStr;// 返回文本字符串
  }

  public static String subString(String text, int length, String endWith) {
    text = Html2Text(text);
    int textLength = text.length();

    int byteLength = 0;
    StringBuffer returnStr = new StringBuffer();
    for (int i = 0; i < textLength && byteLength < length * 3; i++) {
      String str_i = text.substring(i, i + 1);
      if (str_i.getBytes().length == 1) {// 英文
        byteLength++;
      } else {// 中文
        byteLength += 3;
      }
      returnStr.append(str_i);
    }
    try {
      if (byteLength < text.getBytes("UTF-8").length) {// getBytes("GBK")每个汉字长2，getBytes("UTF-8")每个汉字长度为3
        returnStr.append(endWith);
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return returnStr.toString();
  }

}
