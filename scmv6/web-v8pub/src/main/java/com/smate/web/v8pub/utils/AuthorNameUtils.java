package com.smate.web.v8pub.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.CharUtils;

import com.smate.core.base.utils.data.XmlUtil;


/**
 * 作者名字处理拆分工具类.
 * 
 * @author yamingd
 */
public class AuthorNameUtils {

  /**
   * @param names 作者名字（长串).
   * @return List<String>
   */
  public static List<String> splitNames(String names) {
    // Lu J (Lu, Jie), Ruan D (Ruan, Da), Wu FJ (Wu, Fengjie), Zhang GQ
    // (Zhang, Guangquan)
    // L Sansores-Garcia, XM Chen, N Matijevic-Aleksic
    // Erkan Yurtcu; Yildiz Guney; Mehmet Ali Ergun; H. Zafer Guney; Canan
    // Uluoglu; Ayse Hicsonmez; Berna Yucel; Gul Ozbey ; Hakan Zengil
    // María José Chapela; Carmen G. Sotelo; Ricardo I. Pérez-Martín; Miguel
    // ángel Pardo; Bego?a Pérez-Villareal; Patricia Gilardi ; Juan Riese
    // Zhang, Zhe; Jiang, Jian; Wang, Huaiqing
    names = names.replaceAll("；", ";");
    names = names.replaceAll("，", ";");
    if (names == null) {
      return new ArrayList<String>();
    }

    names = names.trim();
    if (names.indexOf(";") > 0 && !names.endsWith(";")) {
      names = names + ";";
    }

    char[] cs = names.toCharArray();
    int i = 0;
    // boolean isDot = false;
    // boolean isSemicolon = false;
    boolean isLeftBracket = false;
    boolean isRightBracket = false;
    boolean isAnd = false;
    boolean isNameToken = false;
    boolean isDotAndSemicolon = false;

    int beginIndex = 0;
    int endIndex = 0;

    List<String> result = new ArrayList<String>();

    while (i < cs.length) {
      if (cs[i] == ',') {
        // isDot = true;
        if (names.indexOf(";", i) >= 0) {
          // 同时有,;
          isNameToken = false;
          isDotAndSemicolon = true;
        }
        if (!isDotAndSemicolon) {
          if (isLeftBracket) {
            if (isRightBracket) {
              isNameToken = true;
              endIndex = i;
            } else {
              isNameToken = false;
            }
          } else {
            if (isRightBracket) {
              isNameToken = true;
              endIndex = i;
            } else {
              isNameToken = true;
              endIndex = i;
            }
          }
        }
      } else if (cs[i] == ';') {
        // isSemicolon = true;
        if (isLeftBracket) {
          if (isRightBracket) {
            isNameToken = true;
            endIndex = i;
          } else {
            isNameToken = false;
          }
        } else {
          if (isRightBracket) {
            isNameToken = true;
            endIndex = i;
          } else {
            isNameToken = true;
            endIndex = i;
          }
        }
      } else if (cs[i] == '(') {
        isLeftBracket = true;
        isNameToken = false;
      } else if (cs[i] == ')') {
        isRightBracket = true;
      }

      if (CharUtils.isAscii(cs[i])) {
        String temp = "";
        boolean isspace = false;
        if (i == 0 && cs[0] == ' ') {
          isspace = true;
        } else if (i > 0 && cs[i - 1] == ' ') {
          isspace = true;
        }
        if (isspace) {
          if (i + 4 >= cs.length) {
            temp = names.substring(i - 1, names.length());
          } else {
            int p = i >= 1 ? i - 1 : 0;
            temp = names.substring(p, i + 4);
          }
          if (temp.trim().equalsIgnoreCase("and")) {
            isNameToken = true;
            endIndex = i;
            isAnd = true;
          }
        }
      }

      if (isNameToken) {
        String item = names.substring(beginIndex, endIndex).trim();
        if (!"".equals(item)) {
          result.add(item);
        }
        endIndex = -1;
        if (!isAnd) {
          beginIndex = i + 1;
        } else {
          beginIndex = i + 3;
        }
        isNameToken = false;
        isRightBracket = false;
        isLeftBracket = false;
        isAnd = false;
      }
      i++;
      if (i >= cs.length) {
        String item = names.substring(beginIndex, cs.length).trim();
        if (!"".equals(item)) {
          result.add(item);
        }
      }
    }
    for (int j = result.size() - 1; j >= 0; j--) {
      if ("".equals(result.get(j).trim())) {
        result.remove(j);
      }
    }
    return result;
  }

  /**
   * @param name 作者名字
   * @return String
   */
  public static String getNameAbbr(String name) {
    if (org.apache.commons.lang.StringUtils.isEmpty(name)) {
      return "";
    }
    int pos = name.indexOf("(");
    if (pos > 0) {
      name = name.substring(0, pos);
    }
    String[] temp = name.split("[-|\\s+]");
    String abbr = "";
    for (int i = 0; i < temp.length - 1; i++) {
      if (!"".equals(temp[i])) {
        abbr += temp[i].toUpperCase().charAt(0);
      }
    }
    if (!"".equals(abbr)) {
      abbr = temp[temp.length - 1] + " " + abbr;
      return abbr;
    }
    return name;
  }

  /**
   * @param name 作者名字
   * @return boolean
   */
  public static boolean isChineseName(String name) {
    if (XmlUtil.isEmpty(name)) {
      return false;
    }

    return name.getBytes().length != name.length(); // 相等则没有汉字，不等则有汉字。

  }

  /**
   * @param firstName 名
   * @param lastName 姓
   * @return String
   */
  public static String getNameAbbr(String firstName, String lastName) {
    if (XmlUtil.isEmpty(firstName)) {
      return firstName;
    }

    String init = getNameInitial(firstName);
    String res = lastName + " " + init;
    return res;
  }

  /**
   * @param firstName 名
   * @param lastName 姓
   * @return String
   */
  public static String getNameInRGCReport(String firstName, String lastName) {
    return getNameInRGCReport(firstName, lastName, "");
  }

  /**
   * @param firstName 名
   * @param lastName 姓
   * @param otherName 其他名字
   * @return String
   */
  public static String getNameInRGCReport(String firstName, String lastName, String otherName) {
    if (XmlUtil.isEmpty(firstName)) {
      return lastName;
    }

    String init = getNameInitial(firstName + " " + otherName);
    String res = lastName + ",";
    for (int i = 0; i < init.length(); i++) {
      res = res + " " + String.valueOf(init.charAt(i));
    }
    return res.toUpperCase();
  }

  /**
   * @param name 名
   * @return String
   */
  public static String getNameInitial(String name) {
    if (XmlUtil.isEmpty(name)) {
      return "";
    }
    name = name.trim();
    int pos = name.indexOf("(");
    if (pos > 0) {
      name = name.substring(0, pos);
    }
    String[] temp = name.split("[\\.|-|\\s+]");
    String abbr = "";
    for (int i = 0; i < temp.length; i++) {
      if (!"".equals(temp[i])) {
        abbr += temp[i].toUpperCase().charAt(0);
      }
    }
    return abbr;
  }

  /**
   * @param fullName 作者全名
   * @return String[]
   */
  public static String[] getFirstNameAndLastNameWithComma(String fullName) {
    if (XmlUtil.isEmpty(fullName)) {
      return new String[] {"", ""};
    }
    fullName = org.apache.commons.lang.StringUtils.strip(fullName, ",").trim();
    if (fullName.indexOf(",") <= 0) {
      return new String[] {"", fullName};
    }
    int pos = fullName.indexOf("(");
    if (pos > 0) {
      fullName = fullName.substring(0, pos).trim();
    }
    String[] temp = fullName.split(",");
    if (temp.length < 2) {
      return new String[] {"", temp[0]};
    } else {
      String[] result = new String[2];
      result[1] = temp[0];
      String fname = "";
      for (int i = 1; i < temp.length; i++) {
        fname += temp[i] + " ";
      }
      result[0] = fname.trim();
      return result;
    }
  }

  /**
   * @param name 作者名字
   * @return String
   */
  @SuppressWarnings("unused")
  private static String trimInvalidChar(String name) {
    int start = 0;
    int end = name.length();
    for (int i = 0; i < name.length(); i++) {
      if (CharUtils.isAscii(name.charAt(i)) || name.charAt(i) == '(') {
        start = i;
        break;
      }
    }
    for (int i = name.length() - 1; i >= 0; i--) {
      if (CharUtils.isAscii(name.charAt(i)) || name.charAt(i) == ')') {
        end = i + 1;
        break;
      }
    }
    if (start > 0 || end < name.length()) {
      return name.substring(start, end);
    }
    return name;
  }

  /**
   * @param names 名字列表
   */
  private static void printNames(List<String> names) {
    for (int i = 0; i < names.size(); i++) {
      System.out.println(names.get(i));
    }
  }

  /**
   * 替换常见字符
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public static String replaceChars(String string) {

    string = string.replace("，", ",").replace("（", "(").replace("）", ")").replace("。", ".");
    string = string.replace(" ", "空格").replace(",", "逗号").replace(".", "点符号").replace("(", "左括号").replace(")", "右括号")
        .replace("&", "和符号").replace("'", "撇号").replace("-", "杠符号").replace("《", "前书名号").replace("》", "后书名号");
    return string;

  }

  /**
   * 还原字符
   * 
   * @param string
   * @return
   * @author LIJUN
   * @date 2018年3月26日
   */
  public static String resetChars(String string) {
    string = string.replace("空格", " ").replace("逗号", ",").replace("点符号", ".").replace("左括号", "(").replace("右括号", ")")
        .replace("和符号", "&").replace("撇号", "'").replace("杠符号", "-").replace("前书名号", "《").replace("后书名号", "》");
    return string;

  }


  /**
   * @param args 参数
   */
  public static void main(final String[] args) {

    try {

      List<String> names = null;

      names = splitNames("Lu J (Lu, Jie), Ruan D (Ruan, Da), Wu FJ (Wu, Fengjie), Zhang GQ (Zhang, Guangquan)");
      printNames(names);
      names = splitNames("L Sansores-Garcia, XM Chen, N Matijevic-Aleksic");
      printNames(names);
      names = splitNames(
          "Erkan Yurtcu; Yildiz Guney; Mehmet Ali Ergun; H. Zafer Guney; Canan Uluoglu; Ayse Hicsonmez; Berna Yucel; Gul Ozbey ; Hakan Zengil");
      printNames(names);
      names = splitNames(
          "María José Chapela; Carmen G. Sotelo; Ricardo I. Pérez-Martín; Miguel ángel Pardo; Bego?a Pérez-Villareal; Patricia Gilardi ; Juan Riese");
      printNames(names);
      names = splitNames("Lu J (Lu; Jie); Ruan D (Ruan; Da); Wu FJ (Wu; Fengjie) and Zhang GQ (Zhang; Guangquan)");
      printNames(names);
      names = splitNames("Cao LB (Cao;Longbing)");
      printNames(names);
      names = splitNames("Cao LB (Cao;Longbing);;abc;;cdefgh");
      printNames(names);
      names = splitNames("(Huiyu Su);; Shenglin D;; Nong Zhang;; Changchun W");
      printNames(names);
      names = splitNames(
          "Hwi-Chang Che;; Hsien-Feng Kun;; Wen-Chieh Che;; Wen-Feng Li;; Deng-Fwu Hwan;; Yi-Chen Lee;; Yung-Hsiang Tsa");
      printNames(names);
      names = splitNames("Cheng-Lung Huang;Hung-Chang Liao and Mu-Chen Chen");
      printNames(names);

      names = splitNames("通讯员 董新华;");
      printNames(names);

      String temp = getNameAbbr("Xiao Ming Chen");
      System.out.println(temp);

      temp = getNameAbbr("XiaoMing Chen");
      System.out.println(temp);

      temp = getNameAbbr("Xiao-Ming Chen");
      System.out.println(temp);

      temp = getNameAbbr("XiaoMingChen");
      System.out.println(temp);

      String[] st = getFirstNameAndLastNameWithComma("XM Chen(XiaoMing Chen)");
      for (int i = 0; i < st.length; i++) {
        System.out.println(st[i]);
      }

      temp = getNameInitial("A.h.s");
      System.out.println(temp);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
