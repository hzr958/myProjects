package com.smate.core.base.utils.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;

/**
 * 专利 ， 论文 ， 人员 , 过滤掉所有的特殊字符
 *
 */
public class FilterAllSpecialCharacter {

  public static String StringFilter(String str) throws PatternSyntaxException {
    if (null != str && !"".equals(str.trim())) {
      String regEx = "[~·`!！@#￥$%^……*（()）——_=【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"《<。》>？?]";
      Pattern p = Pattern.compile(regEx);
      Matcher m = p.matcher(str);
      return FilterAllSpecialCharacter.excludeStringAndOr(m.replaceAll(" "));
    }
    return null;
  }


  // 判断是否是这4个 _ . / - 需要排除的字符，因为会影响到英文的分词
  public static Boolean isExcludedChars(String str) throws PatternSyntaxException {
    if (StringUtils.isEmpty(str)) {
      return false;
    }

    String regEx = "^[\\._/\\-]+$";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(str);
    return m.matches();
  }


  // 排除这五个 _ . / -" " 字符 不过滤
  public static String stringFilterExcludeFiveChar(String str) throws PatternSyntaxException {
    if (null != str && !"".equals(str.trim())) {
      String regEx = "[~·`!！@#￥$%^……*（()）=【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"《<。》>？?.-]";
      Pattern p = Pattern.compile(regEx);
      Matcher m = p.matcher(str);
      return FilterAllSpecialCharacter.excludeStringAndOr(m.replaceAll(" "));
    }
    return null;
  }

  // 排除or与and
  public static String excludeStringAndOr(String str) {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    String regEx = "\\bor\\b|\\band\\b";
    return str.replaceAll(regEx, "");

  }

  public static void main(String[] args) {
    String regEx = "10\\.[0-9\\.]+/[\\w]+";
    String regEx_1 = "^[_/\\-\\.][_/\\-\\.]*$";
    String regtest1 = "\\bor\\b|\\band\\b";
    Pattern p = Pattern.compile(regtest1);
    String test3 = "tom and jerry and someone else";
    Matcher m = p.matcher(test3);
    Matcher m1 = p.matcher(test3);

    System.out.println(test3.replaceAll(regtest1, ""));

    if (Pattern.matches(regEx_1, test3)) {
      System.out.println("test3, true");
    } else {
      System.out.println("test3, false");
    }

    if (m.matches()) {
      System.out.println("true");
    } else {
      System.out.println("false");
    }


    if (m1.matches()) {
      System.out.println("true");
    } else {
      System.out.println("false");
    }

    System.out.println(StringFilter("10.1089/\\dna.2009-0979"));
  }



}
