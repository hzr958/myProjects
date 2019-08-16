package com.smate.core.base.utils.xss;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * 过滤 js html xml vb方法.
 * 
 * @author sxc
 * 
 */
public class CrossXssMethod {

  // Private variables
  private static String EmptyString_JavaScript = "''";
  private static String EmptyString_VBS = "\"\"";
  private static String EmptyString = "";
  private static StringBuffer strb;
  private static StringCharacterIterator sci;

  /**
   * 编码转换HTML内容.
   * 
   * @param strInput
   * @return
   */
  @Deprecated
  private static String encodeHtml(String strInput) {
    if (strInput.length() == 0) {
      return EmptyString;
    }
    StringBuffer builder = new StringBuffer(strInput.length() * 2);
    CharacterIterator it = new StringCharacterIterator(strInput);
    for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
      if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '['))) || (((ch == ' ') || ((ch > '/') && (ch < ':')))
          || (((ch == '.') || (ch == ',')) || ((ch == '-') || (ch == '_'))))) {
        builder.append(ch);
      } else {
        builder.append("&#" + (int) ch + ";");
      }
    }
    return builder.toString();
  }

  /**
   * 编码转换HTML属性.
   * 
   * @param strInput
   * @return
   */
  @Deprecated
  private static String encodeHtmlAttribute(String strInput) {
    if (strInput.length() == 0) {
      return EmptyString;
    }
    StringBuffer builder = new StringBuffer(strInput.length() * 2);
    CharacterIterator it = new StringCharacterIterator(strInput);
    for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
      if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '[')))
          || (((ch > '/') && (ch < ':')) || (((ch == '.') || (ch == ',')) || ((ch == '-') || (ch == '_'))))) {
        builder.append(ch);
      } else {
        builder.append("&#" + (int) ch + ";");
      }
    }
    return builder.toString();
  }

  /**
   * 编码转换JS.
   * 
   * @param strInput
   * @return
   */
  @Deprecated
  private static String encodeJs(String strInput) {
    if (strInput.length() == 0) {
      return EmptyString_JavaScript;
    }
    StringBuffer builder = new StringBuffer("'");
    CharacterIterator it = new StringCharacterIterator(strInput);
    for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
      if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '['))) || (((ch == ' ') || ((ch > '/') && (ch < ':')))
          || (((ch == '.') || (ch == ',')) || ((ch == '-') || (ch == '_'))))) {
        builder.append(ch);
      } else if (ch > '\u007f') {
        builder.append("\\u" + twoByteHex(ch));
      } else {
        builder.append("\\x" + singleByteHex(ch));
      }
    }
    builder.append("'");
    return builder.toString();
  }

  /**
   * 编码转换URL.
   * 
   * @param strInput
   * @return
   */
  @Deprecated
  private static String encodeUrl(String strInput) {
    if (strInput.length() == 0) {
      return EmptyString;
    }
    StringBuffer builder = new StringBuffer(strInput.length() * 2);
    CharacterIterator it = new StringCharacterIterator(strInput);
    for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
      if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '[')))
          || (((ch > '/') && (ch < ':')) || (((ch == '.') || (ch == '-')) || (ch == '_')))) {
        builder.append(ch);
      } else if (ch > '\u007f') {
        builder.append("%u" + twoByteHex(ch));
      } else {
        builder.append("%" + singleByteHex(ch));
      }
    }
    return builder.toString();
  }

  /**
   * 编码转换Vbs.
   * 
   * @param strInput
   * @return
   */
  @Deprecated
  private static String encodeVbs(String strInput) {
    if (strInput.length() == 0) {
      return EmptyString_VBS;
    }
    StringBuffer builder = new StringBuffer(strInput.length() * 2);
    boolean flag = false;
    CharacterIterator it = new StringCharacterIterator(strInput);
    for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
      if ((((ch > '`') && (ch < '{')) || ((ch > '@') && (ch < '['))) || (((ch == ' ') || ((ch > '/') && (ch < ':')))
          || (((ch == '.') || (ch == ',')) || ((ch == '-') || (ch == '_'))))) {
        if (!flag) {
          builder.append("&\"");
          flag = true;
        }
        builder.append(ch);
      } else {
        if (flag) {
          builder.append("\"");
          flag = false;
        }
        builder.append("&chrw(" + (long) ch + ")");
      }
    }
    if ((builder.length() > 0) && (builder.charAt(0) == '&')) {
      builder.delete(0, 1);
    }
    if (builder.length() == 0) {
      builder.insert(0, "\"\"");
    }
    if (flag) {
      builder.append("\"");
    }
    return builder.toString();
  }

  /**
   * 编码转换XML.
   * 
   * @param strInput
   * @return
   */
  @Deprecated
  private static String encodeXml(String strInput) {
    return encodeHtml(strInput);
  }

  /**
   * 编码转换Xml属性.
   * 
   * @param strInput
   * @return
   */
  @Deprecated
  private static String encodeXmlAttribute(String strInput) {
    return encodeHtmlAttribute(strInput);
  }

  /**
   * Returns a string object encoded to be used in an HTML attribute.
   * <p>
   * This method will return characters a-z, A-Z, 0-9, full stop, comma, dash, and underscore
   * unencoded, and encode all other character in decimal HTML entity format (i.e. < is encoded as
   * &#60;).
   * 
   * @param s a string to be encoded for use in an HTML attribute context
   * @return the encoded string
   */
  @Deprecated
  public static String htmlAttributeEncode(String s) {
    return encodeHtmlAttribute(s);
  }

  /**
   * Returns a string object encoded to use in HTML.
   * <p>
   * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore
   * unencoded, and encode all other character in decimal HTML entity format (i.e. < is encoded as
   * &#60;).
   * 
   * @param s a string to be encoded for use in an HTML context
   * @return the encoded string
   */
  @Deprecated
  public static String htmlEncode(String s) {
    return encodeHtml(s);
  }

  /**
   * Returns a string object encoded to use in JavaScript as a string.
   * <p>
   * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore
   * unencoded, and encode all other character in a 2 digit hexadecimal escaped format for non-unicode
   * characters (e.g. \x17), and in a 4 digit unicode format for unicode character (e.g. \u0177).
   * <p>
   * The encoded string will be returned enclosed in single quote characters (i.e. ').
   * 
   * @param s a string to be encoded for use in a JavaScript context
   * @return the encoded string
   */
  @Deprecated
  public static String javaScriptEncode(String s) {
    return encodeJs(s);
  }

  @Deprecated
  private static String singleByteHex(char c) {
    long num = c;
    return leftPad(Long.toString(num, 16), "0", 2);
  }

  @Deprecated
  private static String twoByteHex(char c) {
    long num = c;
    return leftPad(Long.toString(num, 16), "0", 4);
  }

  /**
   * Returns a string object encoded to use in a URL context.
   * <p>
   * This method will return characters a-z, A-Z, 0-9, full stop, dash, and underscore unencoded, and
   * encode all other characters in short hexadecimal URL notation. for non-unicode character (i.e. <
   * is encoded as %3c), and as unicode hexadecimal notation for unicode characters (i.e. %u0177).
   * 
   * @param s a string to be encoded for use in a URL context
   * @return the encoded string
   */
  @Deprecated
  public static String urlEncode(String s) {
    return encodeUrl(s);
  }

  /**
   * Returns a string object encoded to use in VBScript as a string.
   * <p>
   * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore
   * unencoded (each substring enclosed in double quotes), and encode all other characters in
   * concatenated calls to chrw(). e.g. foo' will be encoded as "foo"&chrw(39).
   * 
   * @param s a string to be encoded for use in a JavaScript context
   * @return the encoded string
   */
  @Deprecated
  public static String visualBasicScriptEncodeString(String s) {
    return encodeVbs(s);
  }

  /**
   * Returns a string object encoded to be used in an XML attribute.
   * <p>
   * This method will return characters a-z, A-Z, 0-9, full stop, comma, dash, and underscore
   * unencoded, and encode all other character in decimal entity format (i.e. < is encoded as &#60;).
   * 
   * @param s a string to be encoded for use in an XML attribute context
   * @return the encoded string
   */
  @Deprecated
  public static String xmlAttributeEncode(String s) {
    return encodeXmlAttribute(s);
  }

  /**
   * Returns a string object encoded to use in XML.
   * <p>
   * This method will return characters a-z, A-Z, space, 0-9, full stop, comma, dash, and underscore
   * unencoded, and encode all other character in decimal entity format (i.e. < is encoded as &#60;).
   * 
   * @param s a string to be encoded for use in an XML context
   * @return the encoded string
   */
  @Deprecated
  public static String xmlEncode(String s) {
    return encodeXml(s);
  }

  @Deprecated
  private static String leftPad(String stringToPad, String padder, int size) {
    if (padder.length() == 0) {
      return stringToPad;
    }
    strb = new StringBuffer(size);
    sci = new StringCharacterIterator(padder);

    while (strb.length() < (size - stringToPad.length())) {
      for (char ch = sci.first(); ch != CharacterIterator.DONE; ch = sci.next()) {
        if (strb.length() < size - stringToPad.length()) {
          strb.insert(strb.length(), String.valueOf(ch));
        }
      }
    }
    return strb.append(stringToPad).toString();
  }
}
