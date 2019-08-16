package com.smate.core.base.utils.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * web工具类.
 * 
 * @author lqh
 * 
 */
public class WebObjectUtil {

  /**
   * 判断字符串是否为空lqh add.
   * 
   * @param str
   * @return
   */
  public static boolean isStrNvl(String str) {

    if (str == null) {
      return true;
    } else if ("".equalsIgnoreCase(str.trim())) {
      return true;
    }

    return false;
  }

  /**
   * 将List转换成JSON格式[{'name':'lqh','tel':'111'}] lqh add.
   * 
   * @param list
   * @return
   */
  @SuppressWarnings("unchecked")
  public static String covertListToJson(List list) {

    StringBuilder sb = new StringBuilder();
    sb.append("[");
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {

        sb.append(covertMapToJson((Map) list.get(i)));
        if (i != list.size() - 1)
          sb.append(",");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * 将MAP转化成JSON格式{'name':'lqh','tel':'111'} lqh add.
   * 
   * @param map
   * @return
   */
  @SuppressWarnings("unchecked")
  public static String covertMapToJson(Map map) {

    StringBuilder sb = new StringBuilder();
    sb.append("{");
    Set set = map.keySet();
    Iterator it = set.iterator();
    while (it.hasNext()) {

      String key = it.next().toString();
      sb.append("\'").append(key).append("\':\'").append(map.get(key)).append("\'");
      if (it.hasNext())
        sb.append(",");
    }
    sb.append("}");

    return sb.toString();
  }

  /**
   * 获取以年月日为目录的文件路径 lqh add.
   * 
   * @return
   */
  public static String getYYYYMMDDDir() {

    SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd/");
    return ft.format(new Date());
  }

  /**
   * 转换日期格式.
   * 
   * @param date
   * @return
   */
  public static String covertDateToYMD(Date date) {
    SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
    return ft.format(date);
  }

  /**
   * 获取没有后缀的文件名 lqh add.
   * 
   * @param fileName
   * @return
   */
  public static String getFileNameWithoutExt(String fileName) {
    int index = fileName.lastIndexOf(".");
    if (index < 0) {
      return fileName.substring(0, fileName.length());
    } else {
      return fileName.substring(0, fileName.lastIndexOf("."));
    }
  }

  /**
   * 获取文件后缀 lqh add.
   * 
   * @param fileName
   * @return
   */
  public static String getFileNameExt(String fileName) {
    if (fileName.indexOf(".") > 0) {
      return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    return null;
  }

  /**
   * 文件下载时处理文件名称过长问题.
   * 
   * @param name
   * @return
   * @throws Exception
   */
  public static String formateFileName(String name) throws Exception {

    String fileName = java.net.URLEncoder.encode(name, "UTF-8");
    fileName = fileName.replaceAll("\\+", "%20");
    if (fileName.length() > 150) {

      // see http://ag-sherry.javaeye.com/blog/271705
      // see
      // http://topic.csdn.net/u/20080917/20/813e788b-f4ac-4367-90a8-a4d4cb290fb2.html
      // see http://support.microsoft.com/default.aspx?kbid=816868

      /*
       * 这是由于内容部署标头用于文件流是大于大约 150 字节， 拉丁字符集是等于 150 个字符。 如果是使用非拉丁字符集, 例如日语或俄语格式化内容部署标头可能会发生此行为。 因为 UTF-8
       * 编码方案使用 9 字节来表示单个日语字符, 但使用拉丁字符集中仅 1 字节例如, 17 个字符内容部署标头日语字符集中是 153 字节。
       */
      String charset = "";
      fileName = "";
      Locale locale = LocaleContextHolder.getLocale();

      if (locale.equals(Locale.CHINA)) {
        charset = "gb18030";
      } else if (locale.equals(Locale.TAIWAN)) {
        charset = "big5";
      }

      if (!"".equals(charset)) {
        // 可以满足如下长度下载：
        // 关于区间数决策矩阵的专家群体判断共识性研究关于区间数决策矩阵的专家群体判断共识性研究关于区间数决策矩阵的专家群体判断共识性研究关于关于关于关于关于关于关于.pdf

        if (name.length() <= 81)
          fileName = new String(name.getBytes(charset), "ISO8859-1");
        else {
          // 太长了，截取
          int pos = name.lastIndexOf(".");
          String tmp1 = pos > 0 ? name.substring(0, pos - 1) : name;
          String ext = pos > 0 ? name.substring(pos) : "";
          tmp1 = tmp1.substring(0, 81 - ext.length());
          fileName = tmp1 + ext;
          fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
          fileName = fileName.replaceAll("\\+", "%20");
        }
      }
    }

    return fileName;
  }

  public static String processFileName(String fileName, String agent) throws IOException {
    String codedfilename = null;
    if (null != agent && -1 != agent.indexOf("MSIE")) {
      String prefix = fileName.lastIndexOf(".") != -1 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
      String extension = fileName.lastIndexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".")) : "";
      String name = java.net.URLEncoder.encode(fileName, "UTF8");
      if (name.lastIndexOf("%0A") != -1) {
        name = name.substring(0, name.length() - 3);
      }
      int limit = 150 - extension.length();
      if (name.length() > limit) {
        name = java.net.URLEncoder.encode(prefix.substring(0, Math.min(prefix.length(), limit / 9)), "UTF-8");
        if (name.lastIndexOf("%0A") != -1) {
          name = name.substring(0, name.length() - 3);
        }
      }

      codedfilename = name + extension;
    } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
      codedfilename = "=?UTF-8?B?"
          + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
    } else {
      codedfilename = fileName;
    }
    return codedfilename;
  }

  /**
   * 处理JSON特殊字符.
   * 
   * @param str
   * @return
   */
  public static String replaceJsonStr(String str) {

    if (str != null) {

      /* str = str.replace("\"", "&quot;"); */
      // str = str.replace("&", " &amp;");
      str = str.replace("<", " &lt;");
      str = str.replace(">", "&gt");
      str = str.replace("\'", "\\'");
      // str = str.replace("[", "\\[");
      // str = str.replace("]", "\\]");
    }
    return str;
  }

  public static String getFileType(String fileName) {
    String suffix = getFileNameExt(fileName);
    String fileType = "file";
    if (suffix != null) {
      fileType = suffix.toLowerCase();
    }
    if ("xls".equalsIgnoreCase(suffix) || "xlsx".equalsIgnoreCase(suffix))
      fileType = "xls";
    else if ("doc".equalsIgnoreCase(suffix) || "docx".equalsIgnoreCase(suffix))
      fileType = "doc";
    else if ("ppt".equalsIgnoreCase(suffix) || "pptx".equalsIgnoreCase(suffix))
      fileType = "ppt";
    else if ("rar".equalsIgnoreCase(suffix) || "zip".equalsIgnoreCase(suffix) || "gzip".equalsIgnoreCase(suffix)
        || "tar".equalsIgnoreCase(suffix) || "7z".equalsIgnoreCase(suffix))
      fileType = "rar";
    else if ("txt".equalsIgnoreCase(suffix) || "text".equalsIgnoreCase(suffix))
      fileType = "txt";
    else if ("pdf".equalsIgnoreCase(suffix))
      fileType = "pdf";
    else if ("jpg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix) || "gif".equalsIgnoreCase(suffix)
        || "jpeg".equalsIgnoreCase(suffix) || "bmp".equalsIgnoreCase(suffix))
      fileType = "imgIc";
    else if ("flv".equalsIgnoreCase(suffix) || "wmv".equalsIgnoreCase(suffix) || "wma".equalsIgnoreCase(suffix)
        || "avi".equalsIgnoreCase(suffix) || "rmvb".equalsIgnoreCase(suffix) || "rm".equalsIgnoreCase(suffix)
        || "rmvb".equalsIgnoreCase(suffix) || "mkv".equalsIgnoreCase(suffix) || "mp4".equalsIgnoreCase(suffix)) {
      fileType = "movie";
    } else if ("mp3".equalsIgnoreCase(suffix) || "wav".equalsIgnoreCase(suffix)) {
      fileType = "music";
    } else if ("html".equalsIgnoreCase(suffix) || "htm".equalsIgnoreCase(suffix)) {
      fileType = "html";
    }
    return fileType;
  }
}
