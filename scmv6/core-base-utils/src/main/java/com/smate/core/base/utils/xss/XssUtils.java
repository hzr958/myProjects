package com.smate.core.base.utils.xss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;

import com.smate.core.base.utils.json.JacksonUtils;

/**
 * XSS安全拦截工具类。
 * 
 * @author tsz
 *
 */
public class XssUtils {
  private static List<Pattern> expPatterns = null;
  static {
    expPatterns = new ArrayList<Pattern>();
    expPatterns
        .add(Pattern.compile("<\\s*/?(script|(i)?frame|vbscript|javascript|link)(.*?)>", Pattern.CASE_INSENSITIVE));
    expPatterns.add(Pattern.compile(
        "(src[\\r\\n]*=[\\r\\n]*\\'(.*?)\\')|(src[\\r\\n]*=[\\r\\n]*\"(.*?)\")|(eval\\((.*?)\\))|(alert\\((.*?)\\))|(script:)|(javascript:)|(vbscript:)|(onload\\s*=)|(expression\\((.*?)\\))|(function\\s*\\()|(window[\\.|\\[]\\'*location)|(\\*/)|(/\\*)|(:alert)",
        Pattern.CASE_INSENSITIVE));
    expPatterns.add(Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.CASE_INSENSITIVE));
    expPatterns.add(Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    expPatterns
        .add(Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    expPatterns.add(Pattern.compile("(javascript:|vbscript:|view-source:)+", Pattern.CASE_INSENSITIVE));
    /*
     * 排除“<br>”的情况，过滤“<任意多个字符>”的情况。 这里排除<br>标签是因为发表动态要支持换行，换行通过添加<br>标签来实现的。
     * 
     * @author ChuanjieHou
     * 
     * @date 2017-8-31
     */
    /*
     * expPatterns.add(Pattern.compile("<([^b<>][^>]*|b[^r>][^>]*|b|br[^>][^>]*|)>",
     * Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
     */
    expPatterns.add(Pattern.compile(
        "<[/]?(a|abbr|acronym|address|applet|area|article|aside|audio|base|basefont|bdi|bdo|big|blockquote|body|button|canvas|caption|center|cite|code|col|colgroup|command|datalist|dd|del|details|dir|div|dfn|dialog|dl|dt|em|embed|fieldset|figcaption|figure|font|footer|form|frame|frameset|h1|head|header|hr|html|i|iframe|img|input|ins|isindex|kbd|keygen|label|legend|li|link|map|mark|menu|menuitem|meta|meter|nav|noframes|noscript|object|ol|optgroup|option|output|p|param|pre|progress|q|rp|rt|ruby|s|samp|script|section|select|small|source|span|strike|strong|style|sub|summary|sup|table|tbody|td|textarea|tfoot|th|thead|time|title|tr|track|tt|u|ul|var|video|wbr|xmp)([^>]*)>",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    expPatterns.add(Pattern.compile("<!--[^-->]*-->", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    expPatterns.add(Pattern.compile("<!DOCTYPE[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    expPatterns.add(Pattern.compile(
        "(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()+",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
    expPatterns.add(Pattern.compile(
        "<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload|href)+\\s*=+",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL));
  }

  /**
   * 拦截过滤xss字符.
   * 
   * @regionAuthor tanshaozhi
   * @modifyAuthor ChuanjieHou
   * @modifyDate 2017年8月31日
   * @param value
   * @return
   */
  public static String filterByXssStr(String value) {
    // 先转义回来 兼容富文本框 tsz
    value = StringEscapeUtils.unescapeHtml4(value);
    value = xssReplace(value);
    return value;
  }

  /**
   * 新的 xss过滤器 用
   * 
   * @author tsz
   * @param value
   * @return
   */
  public static String xssReplace(String value) {
    if (value.contains("apps.webofknowledge.com")) {
      return value;
    }
    for (Pattern expPattern : expPatterns) {
      Matcher matcher = null;
      String replaceValue = value;
      while (true) {
        matcher = expPattern.matcher(replaceValue);
        while (matcher.find()) {
          String g = matcher.group();
          // 判断匹配到的字符 是不是我们系统自己的连接 如果是 就直接跳过 不替换 tsz_20150911
          Pattern notInclude = Pattern.compile("(http|https)://(dev|test|www|uat).(scholarmate|smate).com");
          Matcher matcher1 = notInclude.matcher(g);
          if (matcher1.find()) {
            continue;
          }
          replaceValue = replaceValue.replace(g, "");
        }
        // 执行一次替换后，检查是否有替换，有的话，继续执行替换
        if (value.equals(replaceValue)) {
          break;
        } else {
          value = replaceValue;
        }
      }
    }
    value = StringEscapeUtils.escapeHtml4(value);
    return value;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static void transferJson(Map map) {
    Iterator<Map.Entry> it = map.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = it.next();
      Object object = entry.getValue();
      if (object instanceof java.util.List) {
        transferJson((List) object);
      } else if (object != null && object.toString().startsWith("{") && (object instanceof java.util.Map)) {
        Map<String, Object> map1 = (Map<String, Object>) object;
        transferJson(map1);
        map.put(entry.getKey(), JacksonUtils.jsonObject(JacksonUtils.jsonObjectSerializer(map1)));
      } else if (object != null) {
        map.put(entry.getKey(), xssReplace(object.toString()));
      }
    }
  }

  @SuppressWarnings({"rawtypes"})
  public static void transferJson(List list) {
    for (Object object : list) {
      if (object instanceof java.util.Map) {
        transferJson((Map) object);
      } else {
        XssUtils.xssReplace(object.toString());
      }
    }
  }

  @SuppressWarnings({"rawtypes"})
  public static String transferJson(String string) {
    if (StringUtils.isEmpty(string)) {
      return string;
    }
    if (string.startsWith("[") && string.endsWith("]") && JacksonUtils.isJsonList(string)) {
      List list = JacksonUtils.jsonToList(string);
      transferJson(list);
      return JacksonUtils.listToJsonStr(list);
    } else {
      Map<String, Object> map = JacksonUtils.json2HashMap(string);
      transferJson(map);
      return JacksonUtils.jsonObjectSerializer(map);
    }
  }

  /**
   * 处理文档对象属性值
   * 
   * @author ztg
   * @param ele文档对象根节点
   */
  public static void filterXmlByXssStr(Element ele) {
    filterRecursive(ele);
  }

  /**
   * @author ztg
   */
  @SuppressWarnings("unchecked")
  public static void filterRecursive(Element node) {
    List<Attribute> listAttr = node.attributes();
    for (Attribute attr : listAttr) {
      String name = attr.getName();
      String value = attr.getValue();
      attr.setValue(XssUtils.filterByXssStrNoescpeHtml4(value));
    }
    List<Element> listElement = node.elements();
    for (Element e : listElement) {
      filterRecursive(e);// 递归调用
    }
  }

  /**
   * 处理文档对象属性值
   * 
   * @author ztg
   * @param value
   * @return
   */
  public static String filterByXssStrNoescpeHtml4(String value) {
    // 先转义回来 兼容富文本框 tsz
    value = StringEscapeUtils.unescapeHtml4(value);
    if (value.contains("apps.webofknowledge.com")) {
      return value;
    }
    for (Pattern expPattern : expPatterns) {
      Matcher matcher = null;
      String replaceValue = value;
      while (true) {
        matcher = expPattern.matcher(replaceValue);
        while (matcher.find()) {
          String g = matcher.group();
          // 判断匹配到的字符 是不是我们系统自己的连接 如果是 就直接跳过 不替换 tsz_20150911
          Pattern notInclude = Pattern.compile("(http|https)://(dev|test|www|uat).(scholarmate|smate).com");
          Matcher matcher1 = notInclude.matcher(g);
          if (matcher1.find()) {
            continue;
          }
          replaceValue = replaceValue.replace(g, "");
        }
        // 执行一次替换后，检查是否有替换，有的话，继续执行替换
        if (value.equals(replaceValue)) {
          break;
        } else {
          value = replaceValue;
        }
      }
    }
    // value = StringEscapeUtils.escapeHtml4(value);
    return value;
  }

  public static void main(String[] args) {
    System.out.println(filterByXssStr("&lt;h2&gt;111从CKEditor检索数据&lt;/h2&gt;\n" + "\n"
        + "&lt;p&gt;器实&lt;span style=&quot;color:#e74c3c&quot;&gt;例。对于ID为editor1，如下所示：&lt;/span&gt;&lt;/p&gt;"));
  }
}
