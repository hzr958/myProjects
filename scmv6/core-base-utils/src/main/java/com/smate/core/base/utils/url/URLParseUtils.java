package com.smate.core.base.utils.url;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解析url工具类
 * 
 * @author zzx
 *
 */
public final class URLParseUtils {

  private static Logger logger = LoggerFactory.getLogger(URLParseUtils.class);

  /**
   * 获取url连接
   * 
   * @param url
   * @return
   */
  public static Connection getURLCon(String url) {
    return Jsoup.connect(url);
  }

  /**
   * 通过url获取页面doc对象
   * 
   * @param url
   * @return
   * @throws IOException
   */
  public static Document getDocByURL(String url) throws IOException {
    long startTimeContent = System.currentTimeMillis();
    Document document = Jsoup.connect(url).timeout(50000).get();
    long endTimeContent = System.currentTimeMillis();
    logger.warn("解析url获取doc时间：{}", endTimeContent - startTimeContent);
    return document;

  }

  /**
   * 通过hmtl片段获取页面doc对象
   * 
   * @param url
   * @return
   * @throws IOException
   */
  public static Document getDocByString(String html, String baseUrl) throws IOException {
    long startTimeContent = System.currentTimeMillis();
    Document document = Jsoup.parse(html, baseUrl);
    long endTimeContent = System.currentTimeMillis();
    logger.warn("解析html获取doc时间：{}", endTimeContent - startTimeContent);
    return document;

  }

  /**
   * 是否是合法的url
   * 
   * @param url
   * @return
   */
  public static boolean isUrl(String url) {
    if (url == null || "".equals(url)) {
      return false;
    }
    String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
        + "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
        + "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
        + "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
        + "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
        + "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
        + "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
        + "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
    return url.matches(regEx);
  }

  /**
   * 通过url和输入的标签获取Elements
   * 
   * @return
   * @throws IOException
   */
  public static Elements getElementsByURL(String url, String tag) throws IOException {

    return getDocByURL(url).getElementsByTag(tag);
  }

  /**
   * 通过url和选择条件返回Elements
   * 
   * @return
   * @throws IOException
   */
  public static Elements selectElements(String url, String select) throws IOException {
    return getDocByURL(url).select(select);
  }

  /**
   * selects
   * 
   * @return
   * @throws IOException
   */
  public static Map<String, String> selectAttrMap2(String url, List<String> selects) throws IOException {
    Document doc = getDocByURL(url);
    Map<String, String> map = new HashMap<String, String>();
    for (String s : selects) {
      String key = s.substring(s.indexOf("[") + 1, s.indexOf("]"));
      String attr = doc.select(s).attr(key);
      map.put(key, attr);
    }
    return map;

  }

  /**
   * 
   * @param url 要解析的url
   * @param select 查询唯一结果的选择条件
   * @param attrs 要查询的属性列表
   * @return
   * @throws Exception
   */
  public static Map<String, String> selectAttrMap(String url, String select, List<String> attrs) throws Exception {
    Elements element = getDocByURL(url).select(select);
    if (element == null || element.size() != 1) {
      throw new Exception("select的查询结果必须唯一！");
    }
    Map<String, String> map = new HashMap<String, String>();
    for (String attr : attrs) {
      map.put(attr, element.get(0).attr(attr));
    }
    return map;

  }
}
