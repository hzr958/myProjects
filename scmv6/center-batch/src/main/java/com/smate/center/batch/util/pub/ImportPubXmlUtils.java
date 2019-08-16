package com.smate.center.batch.util.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 导入成果工具类.
 * 
 * @author liqinghua
 * 
 */
public class ImportPubXmlUtils {

  private static final String DATA_EXP = "\\<data.+?data\\>";
  // 成果类型
  private static final String PUB_TYPE_EXP = "( pub_type=\"\\s*(\\d+?)\\s*\")";
  private static final String PUB_SEQ_NO_EXP = "( seq_no=\"\\s*(\\d+?)\\s*\")";

  /**
   * 拆分在线导入成果xml到data节点字段.
   * 
   * @param batchXml
   * @return
   */
  public static List<String> splitOnlineXml(String batchXml) {

    if (StringUtils.isBlank(batchXml)) {
      return null;
    }

    // 使用正则表达式拆分
    Pattern pt = Pattern.compile(DATA_EXP, Pattern.MULTILINE | Pattern.DOTALL);
    Matcher mt = pt.matcher(batchXml);
    List<String> list = new ArrayList<String>();
    while (mt.find()) {
      String dataXml = mt.group();
      list.add(dataXml);
    }
    return list;
  }

  /**
   * 获取data段中的seq_no.
   * 
   * @param pubXml
   * @return
   */
  public static String getSeqNo(String pubXml) {

    if (StringUtils.isBlank(pubXml)) {
      return "";
    }

    Pattern seqpt = Pattern.compile(PUB_SEQ_NO_EXP, Pattern.MULTILINE);
    Matcher seqmt = seqpt.matcher(pubXml);
    if (seqmt.find()) {
      seqmt.group(2).trim();
    }
    return "";

  }

  /**
   * 获取成果类型.
   * 
   * @param data
   * @return
   */
  public static Integer parsePubType(String data) {
    Pattern seqpt = Pattern.compile(PUB_TYPE_EXP, Pattern.MULTILINE);
    Matcher seqmt = seqpt.matcher(data);

    Integer pubType = null;
    if (seqmt.find()) {
      pubType = IrisNumberUtils.createInteger(seqmt.group(2).trim());
    }
    return pubType;
  }

  /**
   * 拆分成果关键词.
   * 
   * @param keywords
   * @return
   */
  public static List<String> parsePubKeywords(String keywords) {
    if (StringUtils.isBlank(keywords)) {
      return null;
    }
    String[] kws = keywords.split("[,，;；/\\\\:：\"。]");
    List<String> list = new ArrayList<String>();
    for (String kw : kws) {
      kw = kw.trim();
      kw = kw.replaceAll("\\s+", " ");
      if (StringUtils.isNotBlank(kw) && kw.length() > 2) {
        // 最长两百
        list.add(StringUtils.substring(kw, 0, 200));
      }
    }
    return list;
  }

  /**
   * 获取XML中的DBCODE.
   * 
   * @param xmlData
   * @return
   */
  public static String getXmlSourceDbCode(String xmlData) {
    try {
      Pattern p = Pattern.compile("source_db_code=['|\"](.*?)['|\"]");
      Matcher m = p.matcher(xmlData);
      if (m.find()) {
        return m.group(1);
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  public static void main(String[] args) {

    String a = "asfa\\sf；s	     a        ,df as。f pub_t/ype=\"3\" ";
    List<String> kws = parsePubKeywords(a);
    for (String kw : kws) {
      System.out.println(kw);
    }

  }
}
