package com.smate.core.base.utils.random;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 随机数
 * 
 * @author tsz
 *
 */
public class RandomNumber {

  private static char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
  private static char[] codeSequence1 = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

  /**
   * 获取随机数字 传入参数 int num 为需要随机数字的位数
   * 
   * @param num
   * @return
   */
  public static Long getRandomNumber(int num) {
    StringBuilder result = new StringBuilder();
    result.append(codeSequence1[new Random().nextInt(9)]);
    for (int i = 2; i <= num; i++) {
      result.append(codeSequence[new Random().nextInt(10)]);
    }

    return Long.parseLong(result.toString());
  }

  /**
   * 随机数字和字母 不生成有大写D的
   * 
   * @param num
   * @return
   */
  public static String getRandomStr(int num) {
    String temp = RandomStringUtils.random(num,
        new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'});
    return temp;
  }

  public static void main(String[] args) {
    System.out.println(getRandomNumber(8));
  }

}
