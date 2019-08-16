package com.smate.core.base.utils.string;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 扩展commons-lang3包StringUtils工具类
 *
 * @author houchuanjie
 * @date 2018年1月17日 下午2:41:22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
  /**
   * <p>
   * Splits the provided text into an array with a maximum length, separators specified.
   * </p>
   *
   * <p>
   * The separator is not included in the returned String array. Adjacent separators are treated as
   * one separator.
   * </p>
   *
   * <p>
   * A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
   * whitespace.
   * </p>
   *
   * <p>
   * If more than {@code max} delimited substrings are found, the first returned string includes all
   * characters before the first {@code max - 1} returned strings (including separator characters).
   * </p>
   *
   * <p>
   * This method is very similar to {@link #split(String, String, int)}, but that method splits the
   * {@code str} from left to right, this method splits by reverse, it means this method splits the
   * {@code str} from right to left.
   * </p>
   *
   * <b>Examples:</b>
   *
   * <pre>
   * StringUtils.split(null, *, *)            = null
   * StringUtils.split("", *, *)              = []
   * StringUtils.split("ab cd ef", null, 0)   = ["ab", "cd", "ef"]
   * StringUtils.split("ab   cd ef", null, 0) = ["ab", "cd", "ef"]
   * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
   * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab:cd", "ef"]
   * </pre>
   *
   * @param str the String to parse, may be null
   * @param separatorChars the characters used as the delimiters, {@code null} splits on whitespace
   * @param max the maximum number of elements to include in the array. A zero or negative value
   *        implies no limit
   * @return an array of parsed Strings, {@code null} if null String input
   */
  public static String[] splitByReverse(String str, String separatorChars, int max) {
    return splitReverseWorker(str, separatorChars, max, false);
  }

  /**
   * <p>
   * Splits the provided text into an array with a maximum length, separators specified, preserving
   * all tokens, including empty tokens created by adjacent separators.
   * </p>
   *
   * <p>
   * The separator is not included in the returned String array. Adjacent separators are treated as
   * separators for empty tokens. Adjacent separators are treated as one separator.
   * </p>
   *
   * <p>
   * A {@code null} input String returns {@code null}. A {@code null} separatorChars splits on
   * whitespace.
   * </p>
   *
   * <p>
   * If more than {@code max} delimited substrings are found, the first returned string includes all
   * characters before the first {@code max - 1} returned strings (including separator characters).
   * </p>
   *
   * <p>
   * This method is very similar to {@link #splitPreserveAllTokens(String, String, int)}, but that
   * method splits the {@code str} from left to right, this method splits by reverse, it means this
   * method splits the {@code str} from right to left.
   * </p>
   *
   * <b>Examples:</b>
   *
   * <pre>
   * StringUtils.splitPreserveAllTokens(null, *, *)            = null
   * StringUtils.splitPreserveAllTokens("", *, *)              = []
   * StringUtils.splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
   * StringUtils.splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
   * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
   * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab:cd", "ef"]
   * StringUtils.splitPreserveAllTokens("ab   de fg", null, 2) = ["ab   de", "fg"]
   * StringUtils.splitPreserveAllTokens("ab   de fg", null, 3) = ["ab  ", "de", "fg"]
   * StringUtils.splitPreserveAllTokens("ab   de fg", null, 4) = ["ab ", "", "de", "fg"]
   * </pre>
   *
   * @param str the String to parse, may be {@code null}
   * @param separatorChars the characters used as the delimiters, {@code null} splits on whitespace
   * @param max the maximum number of elements to include in the array. A zero or negative value
   *        implies no limit
   * @return an array of parsed Strings, {@code null} if null String input
   * @since 2.1
   */
  public static String[] splitPreserveAllTokensByReverse(String str, String separatorChars, int max) {
    return splitReverseWorker(str, separatorChars, max, true);
  }

  private static String[] splitReverseWorker(final String str, final String separatorChars, final int max,
      final boolean preserveAllTokens) {
    // Performance tuned for 2.0 (JDK1.4)
    // Direct code is quicker than StringTokenizer.
    // Also, StringTokenizer uses isSpace() not isWhitespace()

    if (str == null) {
      return null;
    }
    final int len = str.length();
    if (len == 0) {
      return ArrayUtils.EMPTY_STRING_ARRAY;
    }
    final List<String> list = new ArrayList<String>();
    int sizePlus1 = 1;
    int i = len - 1, end = len;
    boolean match = false;
    boolean lastMatch = false;
    if (separatorChars == null) {
      // Null separator means use whitespace
      while (i >= 0) {
        if (Character.isWhitespace(str.charAt(i))) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = -1;
              lastMatch = false;
            }
            list.add(str.substring(i + 1, end));
            match = false;
          }
          end = i--;
          continue;
        }
        lastMatch = false;
        match = true;
        i--;
      }
    } else if (separatorChars.length() == 1) {
      // Optimise 1 character case
      final char sep = separatorChars.charAt(0);
      while (i >= 0) {
        if (str.charAt(i) == sep) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = -1;
              lastMatch = false;
            }
            list.add(str.substring(i + 1, end));
            match = false;
          }
          end = i--;
          continue;
        }
        lastMatch = false;
        match = true;
        i--;
      }
    } else {
      // standard case
      while (i >= 0) {
        if (separatorChars.indexOf(str.charAt(i)) >= 0) {
          if (match || preserveAllTokens) {
            lastMatch = true;
            if (sizePlus1++ == max) {
              i = -1;
              lastMatch = false;
            }
            list.add(str.substring(i + 1, end));
            match = false;
          }
          end = i--;
          continue;
        }
        lastMatch = false;
        match = true;
        i--;
      }
    }
    if (match || preserveAllTokens && lastMatch) {
      list.add(str.substring(i + 1, end));
    }
    Collections.reverse(list);
    return list.toArray(new String[list.size()]);
  }

  /**
   * 以提供的分割符串separatorChars将字符串str分割，并将分割后的字符串转换为指定的类型T，转换规则需要给定，
   * 分割后的字符串是否保留空白需要指定，如果preserveWhiteSpace为true，则保留空白符，为false则会使用{@link #trimToEmpty(String)}去除空白符。
   *
   * @see StringUtils#split(String, String)
   * @author houchuanjie
   * @date 2018年3月2日 下午4:11:23
   * @param str
   * @param separatorChars
   * @return 转换后的T类型值的List集合
   */
  public static <T> List<T> split2List(String str, String separatorChars, StringConverter<T> converter,
      boolean preserveWhiteSpace) {
    String[] splits = split(str, separatorChars);
    if (splits == null || splits.length == 0) {
      return Collections.emptyList();
    }
    ArrayList<T> list = new ArrayList<>();
    for (String s : splits) {
      T convertedValue = converter.convert(preserveWhiteSpace ? s : trimToEmpty(s));
      if (convertedValue != null) {
        list.add(convertedValue);
      }
    }
    return list;
  }

  /**
   * 以提供的分割符串separatorChars将字符串str分割，并将分割后的字符串转换为指定的类型T，转换规则需要给定，
   * 分割后的字符串不会保留空白符，会使用{@link #trimToEmpty(String)}去除空白符。
   *
   * @see StringUtils#split(String, String)
   * @author houchuanjie
   * @date 2018年3月2日 下午4:11:23
   * @param str
   * @param separatorChars
   * @return 转换后的T类型值的List集合
   */
  public static <T> List<T> split2List(String str, String separatorChars, StringConverter<T> converter) {
    return split2List(str, separatorChars, converter, false);
  }

  /**
   * <p>
   * 驼峰命名转换为下划线命名
   * </p>
   *
   * <b>Examples:</b>
   *
   * <pre>
   *   helloWord     =>  hello_word
   *   HelloWord    =>  hello_word
   * </pre>
   * 
   * @param camelCaseName 驼峰命名的字符串
   * @return 下划线分割命名的字符串
   */
  public static String underscoreName(String camelCaseName) {
    StringBuilder result = new StringBuilder();
    if (camelCaseName != null && camelCaseName.length() > 0) {
      result.append(camelCaseName.substring(0, 1).toLowerCase());
      for (int i = 1; i < camelCaseName.length(); i++) {
        char ch = camelCaseName.charAt(i);
        if (Character.isUpperCase(ch)) {
          result.append("_");
          result.append(Character.toLowerCase(ch));
        } else {
          result.append(ch);
        }
      }
    }
    return result.toString();
  }

  /**
   * <p>
   * 下划线命名转换为驼峰命名
   * </p>
   *
   * <b>Examples:</b>
   *
   * <pre>
   *   hello_word     =>  helloWord
   *   _hello_word    =>  HelloWord
   *   Hello_word     =>  HelloWord
   * </pre>
   * 
   * @param underscoreName 下划线分割命名的字符串
   * @return 驼峰命名的字符串
   */
  public static String camelCaseName(String underscoreName) {
    StringBuilder result = new StringBuilder();
    if (underscoreName != null && underscoreName.length() > 0) {
      boolean flag = false;
      for (int i = 0; i < underscoreName.length(); i++) {
        char ch = underscoreName.charAt(i);
        if ('_' == ch) {
          flag = true;
        } else {
          if (flag) {
            result.append(Character.toUpperCase(ch));
            flag = false;
          } else {
            result.append(ch);
          }
        }
      }
    }
    return result.toString();
  }

  /**
   * 截取长度的字符串
   * @param s
   * @param length
   * @return
   */
  public static String subString(String s , int length){
    if(StringUtils.isBlank(s) || s.length()<length){
      return s ;
    }
    return s.substring(0,length);
  }

  /**
   * 判断是否是手机号
   * @param num
   * @return
   */
  public static Boolean isMobileNumber(String num){
    if(StringUtils.isBlank(num)) return false ;
    Pattern p =  Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
    Matcher matcher = p.matcher(num);
    boolean b = matcher.find();
    return b ;
  }

  /**
   * 获取最大长度值
   *
   * @param str
   * @param maxLength
   * @return
   */
  public static String subMaxLengthString(String str, int maxLength) {
    if (org.apache.commons.lang.StringUtils.isBlank(str)) {
      return "";
    }
    if (maxLength < 0) {
      maxLength = 0;
    }
    if (str.length() <= maxLength) {
      return str;
    } else {
      String restult = str.substring(0, maxLength);
      return restult;
    }
  }

  public static boolean IsNotBlankObject(Object obj){
    if(obj == null) return false ;
    return  StringUtils.isNotBlank(obj.toString());
  }
  public static void main(String[] args) {
    String s1 =  StringEscapeUtils.escapeHtml4("!@#$%^&*(<>{}");
    String s2 = StringEscapeUtils.escapeHtml4(s1) ;
    String s3 = StringEscapeUtils.escapeHtml4(s2) ;
    System.out.println(s1);
    System.out.println(s2);
    System.out.println(s3);

  }
}
