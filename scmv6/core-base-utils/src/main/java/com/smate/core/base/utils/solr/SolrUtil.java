package com.smate.core.base.utils.solr;

import org.apache.commons.lang.StringUtils;

/**
 * 使用solr进行检索时，使用到的一些工具
 * 
 * @author SYL
 *
 */
public class SolrUtil {

  /**
   * <b>对solr查询语句进行解码，去除某些字符对检索的干扰</b><br/>
   * 
   * 1.将solr查询语句参数中的特殊子符进行去除 \,+,-,!,(,),:,^,[,],",{,},~,*,?,|,&,;,/ <br/>
   * <br/>
   * 
   * 2.同时将所有字符转为小写（避免在语句中出现AND 或 OR 或NOT 这样的逻辑字符.只有大写字符才会有效果，所以这里要转为小写）
   * 
   * @param s
   * @return
   */
  public static String escapeQueryChars(String s) {
    if (StringUtils.isBlank(s)) {
      return s;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      // These characters are part of the query syntax and must be escaped
      if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':' || c == '^' || c == '['
          || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~' || c == '*' || c == '?' || c == '|' || c == '&'
          || c == ';' || c == '/' || Character.isWhitespace(c)) {
        sb.append('\\');
      }
      sb.append(c);
    }
    return sb.toString().toLowerCase();
  }
}
