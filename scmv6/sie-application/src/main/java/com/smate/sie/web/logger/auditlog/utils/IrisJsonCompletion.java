package com.smate.sie.web.logger.auditlog.utils;

import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

/**
 * 对一个格式完整的JSON格式串被截掉一部分后的字符串进行格式补充.
 * 
 * @author yaohai
 * 
 */
public class IrisJsonCompletion {

  /**
   * 对JSON字符串填充}, ], "
   * 
   * @param resource
   * @return
   */
  public static String completeExpect(String resource) {
    StringBuilder sb = new StringBuilder(resource);
    if (StringUtils.isBlank(resource)) {
      return "";
    }

    Stack<Character> stackExpect = new Stack<Character>();// 存储"
    Stack<Character> stackSet = new Stack<Character>();// 存储{, [
    // 1.循环JSON，补充符号"
    for (int i = 0; i < sb.length(); i++) {
      char ch = sb.charAt(i);

      switch (ch) {
        case '{':
          stackSet.push(ch);
          stackExpect.push(':');
          stackExpect.push('\"');
          stackExpect.push('\"');
          break;
        case '[':
          stackSet.push(ch);
          Character nextChar0 = null;
          // 跳过空格
          for (; i + 1 < sb.length(); i++) {
            nextChar0 = sb.charAt(i + 1);
            if (nextChar0 == ' ') {
              continue;
            } else {
              break;
            }
          }
          if (nextChar0 == null) {

          } else if (nextChar0 == '\"') {
            stackExpect.push('\"');
            stackExpect.push('\"');
          }
          break;
        case '\"':
          if (!stackExpect.isEmpty()) {
            stackExpect.pop();
          }
          break;
        case ']':
          stackSet.pop();
          break;
        case ',':
          Character nextChar1 = null;
          // 跳过空格
          for (; i + 1 < sb.length(); i++) {
            nextChar1 = sb.charAt(i + 1);
            if (nextChar1 == ' ') {
              continue;
            } else {
              break;
            }
          }
          if (nextChar1 == null) {

          } else if (nextChar1 == '\"') {
            if (stackSet.lastElement() != '[') {
              stackExpect.push(':');
            }
            stackExpect.push('\"');
            stackExpect.push('\"');
          }
          break;
        case ':':
          if (!stackExpect.isEmpty() && stackExpect.contains(':')) {
            stackExpect.clear();
          }
          Character nextChar2 = null;
          // 跳过空格
          for (; i + 1 < sb.length(); i++) {
            nextChar2 = sb.charAt(i + 1);
            if (nextChar2 == ' ') {
              continue;
            } else {
              break;
            }
          }
          if (nextChar2 == null) {
            if (stackExpect.isEmpty()) {
              stackExpect.push('\"');
              stackExpect.push('\"');
            }
          } else if (nextChar2 == '\"') {
            if (stackExpect.isEmpty()) {
              stackExpect.push('\"');
              stackExpect.push('\"');
            }
          }
          break;
        case '\\':
          i++;
          break;
        case '}':
          stackSet.pop();
          break;

        default:
          break;
      }
    }

    while (true) {
      if (stackExpect.isEmpty()) {
        break;
      }
      sb.append(stackExpect.lastElement());
      stackExpect.pop();
    }
    if (sb.charAt(sb.length() - 1) == ':') {
      sb.append("\"\"");
    }

    // 2.循环JSON，补充符号：}, ]
    return completion(sb.toString());
  }

  /**
   * 获取JSON格式匹配字符.
   * 
   * @param ch
   * @return
   */
  private static Character getCompletionCharacter(Character ch) {
    if (ch.charValue() == '{') {
      return '}';
    } else if (ch.charValue() == '[') {
      return ']';
    } else if (ch.charValue() == '\"') {
      return '\"';
    } else {
      return null;
    }
  }

  /**
   * 填充json数据格式，确保是一个正确格式的数据，好进行解析工作.
   * 
   * @param resource
   * @return
   */
  private static String completion(String resource) {
    StringBuilder sb = new StringBuilder(resource);
    if (StringUtils.isBlank(resource)) {
      return "";
    }

    String value = "";

    if (sb.charAt(sb.length() - 1) == ',') {
      sb.deleteCharAt(sb.length() - 1);
    }

    if (sb.charAt(sb.length() - 1) == ':') {

    } else if (sb.charAt(sb.length() - 1) == '\"') {

    } else {
      int quotaCount = 0;// 最后一个冒号后双引号数目
      int index = sb.lastIndexOf(":");
      for (int i = index; i < sb.length() && index != -1; i++) {
        if (sb.charAt(i) == '\"') {
          quotaCount++;
        }
      }
      if (quotaCount > 0) {
        // 保存最后一个双引号后的内容
        value = sb.substring(sb.lastIndexOf("\"") + 1, sb.length()).replaceAll("\"", "").replaceAll("]", "")
            .replaceAll("}", "");
        // 删除最后一个双引号后的内容
        sb.delete(sb.lastIndexOf("\"") + 1, sb.length());
      }
    }

    Stack<Character> stack = new Stack<Character>();// 存储{, [
    Stack<Character> stackQuote = new Stack<Character>();// 存储冒号后value值出现的引号数据，栈为空则将引号入栈，否则出栈
    boolean keyStart = true;
    for (int i = 0; i < sb.length(); i++) {
      char ch = sb.charAt(i);

      switch (ch) {
        case '{':
          stack.push(ch);
          if (stack.lastElement() == '{') {
            keyStart = true;
          }
          break;
        case '[':
          stack.push(ch);
          break;
        case '\"':
          if (!keyStart) {
            if (stackQuote.isEmpty()) {
              stackQuote.push(ch);
            } else {
              stackQuote.pop();
            }
          }
          break;
        case ']':
          stack.pop();
          break;
        case ',':
          if (stack.lastElement() == '{' && stackQuote.isEmpty()) {
            keyStart = true;
          }
          break;
        case ':':
          keyStart = false;
          break;
        case '\\':
          i++;
          break;
        case '}':
          stack.pop();
          break;

        default:
          break;
      }
    }

    if (keyStart) {
      int index = sb.lastIndexOf("\"");
      if (index != -1) {
        // sb.delete(index, sb.length());//这里对于["110","122"]这种会把后面的"]干掉，造成非法json格式，所以需注释掉该代码
        if (!stack.isEmpty() && stack.lastElement() == '\"') {
          stack.pop();
        }
      }
    } else {
      sb.append(value);
    }
    String tmp = sb.toString().trim();
    sb = new StringBuilder(tmp);
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.deleteCharAt(sb.length() - 1);
    }

    while (true) {
      if (stack.isEmpty()) {
        break;
      }
      sb.append(getCompletionCharacter(stack.lastElement()));
      stack.pop();
    }

    return sb.toString();
  }

}
