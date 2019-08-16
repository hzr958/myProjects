package com.smate.core.base.utils.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

public class URIencodeUtils {
  public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

  public static String decodeURIComponent(String encodedURI) {
    char actualChar;
    StringBuffer buffer = new StringBuffer();
    int bytePattern, sumb = 0;
    for (int i = 0, more = -1; i < encodedURI.length(); i++) {
      actualChar = encodedURI.charAt(i);
      switch (actualChar) {
        case '%': {
          actualChar = encodedURI.charAt(++i);
          int hb =
              (Character.isDigit(actualChar) ? actualChar - '0' : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
          actualChar = encodedURI.charAt(++i);
          int lb =
              (Character.isDigit(actualChar) ? actualChar - '0' : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
          bytePattern = (hb << 4) | lb;
          break;
        }
        case '+': {
          bytePattern = ' ';
          break;
        }
        default: {
          bytePattern = actualChar;
        }
      }
      if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
        sumb = (sumb << 6) | (bytePattern & 0x3f);
        if (--more == 0)
          buffer.append((char) sumb);
      } else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
        buffer.append((char) bytePattern);
      } else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
        sumb = bytePattern & 0x1f;
        more = 1;
      } else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
        sumb = bytePattern & 0x0f;
        more = 2;
      } else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
        sumb = bytePattern & 0x07;
        more = 3;
      } else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
        sumb = bytePattern & 0x03;
        more = 4;
      } else { // 1111110x
        sumb = bytePattern & 0x01;
        more = 5;
      }
    }
    return buffer.toString();

  }

  public static String encodeURIComponent(String input) {
    if (StringUtils.isBlank(input)) {
      return input;
    }
    int l = input.length();
    StringBuilder o = new StringBuilder(l * 3);
    try {
      for (int i = 0; i < l; i++) {
        String e = input.substring(i, i + 1);
        if (ALLOWED_CHARS.indexOf(e) == -1) {
          byte[] b = e.getBytes("utf-8");
          o.append(getHex(b));
          continue;
        }
        o.append(e);
      }
      return o.toString();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return input;
  }

  private static String getHex(byte buf[]) {
    StringBuilder o = new StringBuilder(buf.length * 3);
    for (int i = 0; i < buf.length; i++) {
      int n = (int) buf[i] & 0xff;
      o.append("%");
      if (n < 0x10) {
        o.append("0");
      }
      o.append(Long.toString(n, 16).toUpperCase());
    }
    return o.toString();
  }

  public static void main(String[] args) {
    String title = "当归属药用植物及药材的DNA条形码鉴别研究";
    String code =
        "%E5%BD%93%E5%BD%92%E5%B1%9E%E8%8D%AF%E7%94%A8%E6%A4%8D%E7%89%A9%E5%8F%8A%E8%8D%AF%E6%9D%90%E7%9A%84DNA%E6%9D%A1%E5%BD%A2%E7%A0%81%E9%89%B4%E5%88%AB%E7%A0%94%E7%A9%B6";
    System.out.println(encodeURIComponent(title));
    System.out.println(encodeURIComponent(title).equalsIgnoreCase(code));
    System.out.println(decodeURIComponent(encodeURIComponent(title)));
  }
}
