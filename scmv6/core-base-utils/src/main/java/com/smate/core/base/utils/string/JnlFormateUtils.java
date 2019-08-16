package com.smate.core.base.utils.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 期刊名格式化.
 * 
 * @author lichangwen
 * 
 */
public class JnlFormateUtils {

  /**
   * 格式化期刊名称用于查重.
   * 
   * @param ename
   * @return
   */
  public static String getStrAlias(String title) {
    if (StringUtils.isBlank(title))
      return null;
    try {
      String name = title.replace("\n", " ").replace("\r", " ");
      name = name.replaceAll("\\s+", " ").trim();
      name = name.replaceAll("&amp;", "&");
      name = name.replaceAll("&", "and");
      // 希腊字符转义
      name = name.replaceAll("Α", "alpha").replaceAll("α", "alpha");
      name = name.replaceAll("Β", "beta").replaceAll("β", "beta");
      name = name.replaceAll("Γ", "gamma").replaceAll("γ", "gamma");
      name = name.replaceAll("Δ", "delta").replaceAll("δ", "delta");
      name = name.replaceAll("Ε", "epsilon").replaceAll("ε", "epsilon");
      name = name.replaceAll("Ζ", "zeta").replaceAll("ζ", "zeta");
      name = name.replaceAll("Η", "eta").replaceAll("η", "eta");
      name = name.replaceAll("Θ", "theta").replaceAll("θ", "theta");
      name = name.replaceAll("Ι", "iota").replaceAll("ι", "iota");
      name = name.replaceAll("Κ", "kappa").replaceAll("κ", "kappa");
      name = name.replaceAll("Λ", "lambda").replaceAll("λ", "lambda");
      name = name.replaceAll("Μ", "mu").replaceAll("μ", "mu");
      name = name.replaceAll("Ν", "nu").replaceAll("ν", "nu");
      name = name.replaceAll("Ξ", "xi").replaceAll("ξ", "xi");
      name = name.replaceAll("Ο", "omicron").replaceAll("ο", "omicron");
      name = name.replaceAll("Π", "pi").replaceAll("π", "pi");
      name = name.replaceAll("Ρ", "rho").replaceAll("ρ", "rho");
      name = name.replaceAll("Σ", "sigma").replaceAll("σ", "sigma");
      name = name.replaceAll("Τ", "tau").replaceAll("τ", "tau");
      name = name.replaceAll("Υ", "upsilon").replaceAll("υ", "upsilon");
      name = name.replaceAll("Φ", "phi").replaceAll("φ", "phi");
      name = name.replaceAll("Χ", "chi").replaceAll("χ", "chi");
      name = name.replaceAll("Ψ", "psi").replaceAll("ψ", "psi");
      name = name.replaceAll("Ω", "omega").replaceAll("ω", "omega");

      Pattern p1 = Pattern.compile("[\uff00-\uffff]", Pattern.CASE_INSENSITIVE);
      Matcher m1 = p1.matcher(name);
      name = m1.replaceAll("");
      Pattern p = Pattern.compile(" the | or | of |[^a-zA-Z0-9\u4e00-\u9fa5]", Pattern.CASE_INSENSITIVE);
      Matcher m = p.matcher(name.toLowerCase());
      name = m.replaceAll(" ");
      if (StringUtils.isNotEmpty(name) && name.length() > 250) {
        // 超过250，截取开始至最后一个空格信息
        name = StringUtils.substring(name, 0, 250);
        int specIndex = name.lastIndexOf(" ");
        name = StringUtils.substring(name, 0, specIndex);
      }
      if (name.indexOf("the ") == 0) {
        name = name.substring(4);
      }
      name = name.replaceAll("\\s+", "").trim();
      return name.length() < 2 ? "" : name;
    } catch (Exception e) {
      return title.replaceAll("\\s+", "").toLowerCase().trim();
    }
  }

  public static boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
      return true;
    }
    return false;
  }

  public static boolean isChinese(String string) {
    string = StringUtils.trimToEmpty(string);
    char[] ch = string.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
      if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
          || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
          || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
          || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
          || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
          || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
        return true;
      }
    }
    return false;
  }

  public static String getLocalStr(String str, boolean isChinese) {
    if (StringUtils.isBlank(str))
      return "";
    String resutlStr = "";
    char[] ch = str.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (isChinese(c) && isChinese) {
        resutlStr += c;
      }
      if (!isChinese(c) && !isChinese) {
        resutlStr += c;
      }
    }
    return getStrAlias(resutlStr);
  }

  public static String getSubStr(String str) {
    if (StringUtils.isBlank(str))
      return "";
    if (str.indexOf("(") != -1)
      str = str.substring(0, str.indexOf("("));
    if (str.indexOf("（") != -1)
      str = str.substring(0, str.indexOf("（"));
    str = getStrAlias(str);
    return StringUtils.isBlank(str) ? "" : str + "%";
  }

  /**
   * 是否是doi格式，用于成果查询区分类别. The DOI syntax shall be made up of a DOI prefix and a DOI suffix separated
   * by a forward slash There is no defined limit on the length of the DOI name, or of the DOI prefix
   * or DOI suffix The DOI prefix shall be composed of a directory indicator followed by a registrant
   * code. The directory indicator shall be "10". The second element of the DOI prefix shall be the
   * registrant code. The registrant code is a unique string assigned to a registrant The DOI suffix
   * shall consist of a character string of any length chosen by the registrant.
   */
  public static boolean isDoi(String str) {
    if (StringUtils.isBlank(str))
      return false;

    String regEx = "10\\.[0-9\\.]+/[\\w]+";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(str);
    if (m.find()) {
      return true;
    } else {
      return false;
    }
  }
}
