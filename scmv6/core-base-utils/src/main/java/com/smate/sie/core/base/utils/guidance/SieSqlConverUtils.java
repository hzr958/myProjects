package com.smate.sie.core.base.utils.guidance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smate.sie.core.base.utils.date.IrisStringUtils;


/**
 * 本工具类基于SqlConverUtils.java修改产生, 实现sql语句中变量的替换
 * 
 * @author ztg
 */
public class SieSqlConverUtils {

  /**
   * 通过业务类传过来的参数对SQL中对应的参数进行替换,SQL中参数格式为[@参数名@].
   * 
   * @param sql sql语句
   * @param map 用于替换sql语句中特殊字符"[@ @]" 的map
   * @param params 执行sql语句时传入的占位符变量
   * @return
   */
  public static String transSql(String sql, Map<String, Object> map, List<Object> params) {
    Map<String, Object> constMap = new HashMap<String, Object>();

    // roleId
    // constMap.put("roleid", SecurityUtils.getCurrentUserRoleId());
    // constMap.put("role_id", SecurityUtils.getCurrentUserRoleId());

    if (map == null) {
      // map = constMap;
    } else {// 加入常量map，如果有相同的key则会覆盖，以传入进来的map为主
      constMap.putAll(map);
    }

    // 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
    Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);
    Matcher m = p.matcher(sql);
    String key;
    String paramKey;

    while (m.find()) {
      key = m.group().toLowerCase();
      paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
      sql = IrisStringUtils.regexReplaceString(sql, "\\[@" + paramKey + "@\\]", "?");
      params.add(constMap.get(paramKey));
    }
    return sql;
  }

  /**
   * 将map key lowercase处理.
   */
  public static Map<String, Object> mapKey2Lowercase(Map<String, Object> oldMap) {
    Map<String, Object> newMap = new HashMap<String, Object>();
    for (Map.Entry<String, Object> entry : oldMap.entrySet()) {
      newMap.put(entry.getKey().toLowerCase(),
          entry.getValue() instanceof java.lang.String ? entry.getValue().toString().trim() : entry.getValue());
    }
    oldMap.clear();
    oldMap.putAll(newMap);
    return oldMap;
  }

  /**
   * 带[@@]字符串转换为可执行的sql语句. 同时初始化params参数list
   * 
   * @param sql 将要替换的sql 字符串
   * @param sqlParam 将要替换sql字符串的map变量
   * @param params 执行sql语句所需的变量值
   */
  public static String str2Sql(String sql, Map<String, Object> sqlParam, List<Object> params) {
    if (params == null) {
      params = new ArrayList<Object>();
    }
    // 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
    Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);
    Matcher m = p.matcher(sql);
    String key;
    String paramKey;

    while (m.find()) {
      key = m.group().toLowerCase();
      paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
      sql = IrisStringUtils.regexReplaceString(sql, "\\[@" + paramKey + "@\\]", "?");
      params.add(sqlParam.get(paramKey));
    }
    return sql;
  }

  /**
   * 替换带[@@]字符串. 直接生成sql语句
   * 
   * @param str 将要替换的字符串
   * @param param 将要替换字符串的map变量
   */
  public static String str2str(String str, Map<String, Object> param) {

    // 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
    Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);
    Matcher m = p.matcher(str);
    String key;
    String paramKey;

    while (m.find()) {
      key = m.group().toLowerCase();
      paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
      str = IrisStringUtils.regexReplaceString(str, "\\[@" + paramKey + "@\\]", param.get(paramKey));
    }
    return str;
  }

  /**
   * SQL注入 防护,过滤字符串
   * 
   * @param str 放置sql注入的内容
   */
  public static String sqlParamFilter(String str) {
    if (str == null) {
      return null;
    }
    String expression =
        "\\s*(exec|execute|insert|create|drop|table|having|user|union|where|select|delete|update|count|xp_cmdshell|chr|mid|master|truncate|char|declare|and|ltrim|%27|set|cast|exec)(\\s|%20|\\+|%27|%28)(\\s||%2B|%27|\\+|%7C)+|(<|%3E|%3C)(\\s|%20|\\+|%27)*(/|%2F)?(script|iframe|frame|style|img|link|a|base|br|embed|svg|object)*(/|%2F)?(\\s|%20|\\+|%27|>|%3E|%3C)*|(eval|alert|confirm|prompt|unescape|escape|function|expression|setTimeout|setInterval)\\s*\\(.*\\)|(document\\.cookie|document\\.write|window\\.|window\\[.*\\]|location\\.|javaScript\\s*:|toString\\s*:\\s*alert|toString\\s*:|valueOf\\s*:|alert|Content\\-Type\\s*:|Content\\-Location\\s*:|Content\\-Transfer\\-Encoding\\s*:)|(\\S*%28select\\+\\S*%29)|((%27|\\s)*\\+(and|or)+\\+%27\\S*(%27|%3D)*)|((%27|%2B)+\\+)|(onmouseover|onmousemove|onclick|onchange|onclick|onload|onmouseout|onfocus|onblur|onkeydown|onkeyup)\\s*(=|%3d)";
    // 支持逗号分割模式
    if (expression.indexOf(",") > -1) {
      expression = expression.replaceAll("\\s*,\\s*", "|");
    }
    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE); // 匹配忽略大小写
    Matcher matcher = pattern.matcher(str);
    // 循环匹配和替换，防止替换后重新组成不安全元素
    while (matcher.find()) {
      str = matcher.replaceAll("");
      matcher = matcher.reset(str);
    }
    return str;
  }

  public static void main(String args[]) {
    // String str = "[@count@]>1";
    String sql = "select * from table where id=[@count@]";
    /*
     * Map<String, Object> map = new HashMap<String, Object>(); map.put("count", 2); List<Object> list =
     * new ArrayList<Object>(); String out = str2Sql(sql, map, list); System.out.println(out);
     * System.out.println(list.size()); System.out.println(str2str(str, map));
     */
    System.out.println(sqlParamFilter(sql));
  }
}
