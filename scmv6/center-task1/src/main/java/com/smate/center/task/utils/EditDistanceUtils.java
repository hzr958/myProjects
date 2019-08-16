package com.smate.center.task.utils;

import com.smate.core.base.utils.string.ServiceUtil;

public class EditDistanceUtils {
  private static int min(int one, int two, int three) {
    int min = one;
    if (two < min) {
      min = two;
    }
    if (three < min) {
      min = three;
    }
    return min;
  }

  public static int ld(String str1, String str2) {
    int d[][]; // 矩阵
    int n = str1.length();
    int m = str2.length();
    int i; // 遍历str1的
    int j; // 遍历str2的
    char ch1; // str1的
    char ch2; // str2的
    int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
    if (n == 0) {
      return m;
    }
    if (m == 0) {
      return n;
    }
    d = new int[n + 1][m + 1];
    for (i = 0; i <= n; i++) { // 初始化第一列
      d[i][0] = i;
    }
    for (j = 0; j <= m; j++) { // 初始化第一行
      d[0][j] = j;
    }
    for (i = 1; i <= n; i++) { // 遍历str1
      ch1 = str1.charAt(i - 1);
      // 去匹配str2
      for (j = 1; j <= m; j++) {
        ch2 = str2.charAt(j - 1);
        if (ch1 == ch2) {
          temp = 0;
        } else {
          temp = 1;
        }
        // 左边+1,上边+1, 左上角+temp取最小
        d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
      }
    }
    return d[n][m];
  }

  /**
   * 计算两个字符串的相似度（编辑距离算法）
   * 
   * @param str1
   * @param str2
   * @return
   */
  public static double sim(String str1, String str2) {
    int ld = ld(str1, str2);
    return 1 - (double) ld / Math.max(str1.length(), str2.length());
  }

  public static void main(String[] args) {

    String str1 = "mhqiao";
    String str2 = "qiaominghua";
    String str3 = "zhuyuanhdjh";
    System.err.println(str1 + "---》" + str2);
    System.out.println("ld=" + ld(str1, str2));
    System.out.println("sim=" + sim(str1, str2));
    System.err.println("************************");
    System.err.println(str1 + "---》" + str3);
    System.out.println("ld=" + ld(str1, str3));
    System.out.println("sim=" + sim(str1, str3));
    System.err.println("L.Mod-olo11@absacciai.it;".split("@")[0].toLowerCase().trim().replace(".", "").replace("-", "")
        .replaceAll("\\d+", ""));
    String matchedeamil = "L.Mod-olo11@absacciai.it";
    if (";".equals(matchedeamil.substring(matchedeamil.length() - 1, matchedeamil.length()))) {
      matchedeamil = matchedeamil.substring(0, matchedeamil.length() - 1);
    }
    System.err.println(matchedeamil);
    System.err.println(ServiceUtil.encodeToDes3("1000000733234"));
  }
}
