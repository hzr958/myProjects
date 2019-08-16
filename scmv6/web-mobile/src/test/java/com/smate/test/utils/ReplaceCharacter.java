package com.smate.test.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

public class ReplaceCharacter {
  public static String getReplaceCharacter(String source) {
    if (StringUtils.isBlank(source)) {
      return source;
    }
    Pattern exp = Pattern.compile("(\\.)");
    Matcher matcher = exp.matcher(source);
    while (matcher.find()) {
      source = source.replace(matcher.group(), "/");
    }
    return source;
  }

  @Test
  public void test() {
    String source = "com.smate.web.psn.action.search.properties";
    System.out.println(getReplaceCharacter(source));
  }

}
