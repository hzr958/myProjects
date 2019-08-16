package com.smate.core.base.pub.util;


import org.junit.jupiter.api.Test;

class AuthorNamesUtilsTest {

  @Test
  void testCleanBracket() {
    String authorName = "谭俊峰{; }; 刘洋{; };";
    String regex = "\\{[^{}]+\\}|\\{\\}";
    String regex1 = "\\[[^\\[\\]]+\\]";
    String regex2 = "\\([^\\(\\)]+\\)";
    String regex3 = "（[^（）]+）|（）";
    System.out.println(authorName.replaceAll(regex, ""));

    System.out.println();
  }

}
