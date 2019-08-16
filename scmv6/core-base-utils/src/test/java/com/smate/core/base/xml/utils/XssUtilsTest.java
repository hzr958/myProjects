package com.smate.core.base.xml.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.smate.core.base.utils.xss.XssUtils;

/**
 * Xss安全测试代码.
 * 
 * @author mjg
 *
 */
public class XssUtilsTest {
  @Test
  public void test03() {
    String xss = "<<button<button>button>测试</button><<br>>>><>";
    String value = XssUtils.filterByXssStr(xss);
    System.out.println(value);
  }

  @Test
  public void test04() {
    Pattern pattern = Pattern.compile("<([^b<>][^>]*|b[^r>][^>]*|b|br[^>][^>]*)>",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    String xss = "<button href=\"javascript:confirm('咬我呀')\">你咬我呀</button><>";
    String xss1 = "<<我是一个小画家>>";
    String xss2 = "<>";
    String xss3 = "<<button<button>button>测试</button><<br>>>><>";
    xss = xss3;
    Matcher matcher = null;
    String value = xss3;
    String replaceValue = "";
    while (true) {
      matcher = pattern.matcher(value);
      replaceValue = matcher.replaceAll("");
      if (value.equals(replaceValue)) {
        break;
      } else {
        value = replaceValue;
      }
    }
    /*
     * while (matcher.find()) { String g = matcher.group(); System.out.println("matcher: " + g); xss =
     * xss.replace(g, ""); }
     */
    System.out.println("replaceValue: " + value);
  }

  @Test
  public void test05() {
    // 将字符串中的\替换为\\
    String str = "\\a\\b\\c";
    str = str.replaceAll("\\\\", "\\\\\\\\");
    System.out.println(str);
  }
}
