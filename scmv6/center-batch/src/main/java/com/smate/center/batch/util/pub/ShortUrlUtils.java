package com.smate.center.batch.util.pub;

import org.springframework.util.Assert;

public class ShortUrlUtils {

  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final Integer BASE = ALPHABET.length();

  public static void main(String[] args) {

    Long length_1, length_2;
    length_1 = 2000000000068L;
    length_2 = 1000000000068L;
    System.out.println("length_1 = " + length_1);
    System.out.println("length_2 = " + length_2);
    System.out.println("shortUrl : " + shorten(length_1));
    System.out.println("shortUrl : " + shorten(length_2));
    System.out.println("revert url1 : " + revert(shorten(length_1)));
    System.out.println("revert url2 : " + revert(shorten(length_2)));

  }

  public static String shorten(Long num) {
    Assert.notNull(num, "The number/id of the url must not be null !");
    StringBuilder sb = new StringBuilder();

    while (num > 0L) {
      int index = Long.valueOf(num % BASE).intValue();
      sb.append(ALPHABET.charAt(index));
      num = num / BASE;
    }

    String rt = sb.reverse().toString();
    return rt;
  }

  public static Long revert(String st) {
    Assert.notNull(st, "The input string of the url must not be null !");
    int length = st.length();
    Long base = Long.parseLong(String.valueOf(BASE));
    Long result = 0L;

    for (int i = 0; i < length; i++) {
      result = result * base + ALPHABET.indexOf(st.charAt(i));
    }

    return result;
  }

}
