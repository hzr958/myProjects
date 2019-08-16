package com.smate.center.batch.util.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;

import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * 导入ISI成果XML作者重构工具类.
 * 
 * @author zk
 * 
 */
public class ImportCommonAuthorMergeUtils {

  /**
   * 将导入成果XML作者进行查重合并.
   * 
   * @param doc
   * @return
   */
  public static PubXmlDocument mergeImportAuthor(PubXmlDocument doc) {

    // 判断是否已经合并，如果已经合并过，不需要再次合并
    Element pubAuthors = doc.getPubAuthors();
    if (pubAuthors != null) {
      String mergeFlag = pubAuthors.attributeValue("merge_flag");
      if ("1".equals(mergeFlag)) {
        return doc;
      }
    }
    List<String> namesList = parseNameList(doc);
    List<Map<String, String>> parseResult = parseOldAuthor(doc);
    // 删除原有pub_authors节点
    doc.removeNode("/pub_authors");
    // 创建新的pub_authors节点
    Element authsEle = doc.createElement("/pub_authors");
    List<Map<String, String>> newAuthors = mergeNamesToOldAuthor(namesList, parseResult);
    // 添加author节点
    for (Map<String, String> newAuthor : newAuthors) {
      Element authEle = authsEle.addElement("author");
      doc.fillAttribute(authEle, newAuthor);
    }
    // 标记已经合并
    authsEle.addAttribute("merge_flag", "1");
    return doc;
  }

  /**
   * 将导入成果XML作者进行查重合并.
   * 
   * @param doc
   * @return
   */
  public static Element mergeImportAuthor(Element data) {
    // 判断是否已经合并，如果已经合并过，不需要再次合并
    Element pubAuthors = (Element) data.selectSingleNode("pub_authors");
    if (pubAuthors != null) {
      String mergeFlag = pubAuthors.attributeValue("merge_flag");
      if ("1".equals(mergeFlag)) {
        return data;
      }
    }
    List<String> namesList = parseNameList(data);
    List<Map<String, String>> parseResult = parseOldAuthor(data.selectNodes("pub_authors/author"));
    if (pubAuthors != null) {
      // 删除原有pub_authors节点
      data.remove(pubAuthors);
    }
    // 创建新的pub_authors节点
    Element authsEle = data.addElement("pub_authors");
    List<Map<String, String>> newAuthors = mergeNamesToOldAuthor(namesList, parseResult);
    // 添加author节点
    for (Map<String, String> newAuthor : newAuthors) {
      Element authEle = authsEle.addElement("author");
      Iterator<String> iter = newAuthor.keySet().iterator();
      while (iter.hasNext()) {
        String attrName = iter.next();
        authEle.addAttribute(attrName, StringUtils.trimToEmpty(newAuthor.get(attrName)));
      }
    }
    // 标记已经合并
    authsEle.addAttribute("merge_flag", "1");
    return data;
  }

  /**
   * 合并导入成果中的作者到authors节点下面.
   * 
   * @param namesList
   * @param oldAuthor
   * @return
   */
  public static List<Map<String, String>> mergeNamesToOldAuthor(List<String> namesList,
      List<Map<String, String>> oldAuthor) {

    List<Map<String, String>> newAuthors = new ArrayList<Map<String, String>>();

    int seqNo = 1;
    for (int i = 0; namesList != null && i < namesList.size(); i++) {
      String name = namesList.get(i);
      Map<String, String> newAuthor = new HashMap<String, String>();
      newAuthor.put("name", name);
      newAuthor.put("au", name);
      newAuthor.put("addr", "");
      newAuthor.put("seq_no", String.valueOf(seqNo));
      seqNo++;
      // 查重合并
      for (int j = 0; oldAuthor != null && j < oldAuthor.size(); j++) {
        Map<String, String> mapPsn = oldAuthor.get(j);
        String authorName = mapPsn.get("author_name");
        // 匹配是否相同，相同则合并
        if (XmlUtil.compareAuthorName(name, authorName)) {
          mergeSameAu(newAuthor, mapPsn);
          oldAuthor.remove(j);
          j--;
        }
      }
      newAuthors.add(newAuthor);
    }

    // 可能存在上下作者名对不上的情况，需要将剩下的作者补充过去.正常其他未做特殊要求的，都暂时没这个情况
    // for (int j = 0; oldAuthor != null && j < oldAuthor.size(); j++) {
    // String name = oldAuthor.get(j).get("author_name");
    // Map<String, String> newAuthor = new HashMap<String, String>();
    // newAuthor.put("name", name);
    // newAuthor.put("au", name);
    // newAuthor.put("seq_no", String.valueOf(seqNo));
    // newAuthor.put("addr", oldAuthor.get(j).get("addr"));
    // String psnId = oldAuthor.get(j).get("psnId");
    // psnId = NumberUtils.isDigits(psnId) ? psnId : "";
    // newAuthor.put("psn_id", psnId);
    // newAuthor.put("author_pos", oldAuthor.get(j).get("author_pos"));
    // newAuthor.put("issearchorg", oldAuthor.get(j).get("issearchorg"));
    // newAuthors.add(newAuthor);
    // seqNo++;
    // }

    return newAuthors;
  }

  /**
   * 合并相同的作者信息.
   * 
   * @param remainAuthor
   * @param mapPsn
   */
  private static void mergeSameAu(Map<String, String> remainAuthor, Map<String, String> mapPsn) {
    // 地址
    String addr = StringUtils.trimToEmpty(remainAuthor.get("addr"));
    String fillAddr = mapPsn.get("addr");
    if (StringUtils.isNotBlank(fillAddr)) {
      addr = StringUtils.isBlank(addr) ? fillAddr : addr + ";" + fillAddr;
    }
    remainAuthor.put("addr", addr);
    // 人员ID
    String psnId = StringUtils.trimToEmpty(remainAuthor.get("psn_id"));
    String fillPsnId = mapPsn.get("psnId");
    psnId = NumberUtils.isDigits(psnId) ? psnId : fillPsnId;
    remainAuthor.put("psn_id", psnId);
    // 通信作者，保留1的结果
    String authorPos = remainAuthor.get("author_pos");
    authorPos = "1".equals(authorPos) ? authorPos : mapPsn.get("author_pos");
    remainAuthor.put("author_pos", authorPos);
    remainAuthor.put("issearchorg", mapPsn.get("issearchorg"));
  }

  /**
   * 解析抓取数据publication下面用户名称数据.
   * 
   * @param doc
   * @return
   */
  private static List<String> parseNameList(Element data) {

    Element pub = (Element) data.selectSingleNode("publication");
    String authorNamesStr = pub.attributeValue("author_names");
    return parseNameList(authorNamesStr);
  }

  /**
   * 解析抓取数据publication下面用户名称数据.
   * 
   * @param doc
   * @return
   */
  private static List<String> parseNameList(PubXmlDocument doc) {

    String authorNamesStr = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    return parseNameList(authorNamesStr);
  }

  private static List<String> parseNameList(String authorNamesStr) {
    String[] authorNames = null;
    if (StringUtils.isNotBlank(authorNamesStr)) {
      authorNames = authorNamesStr.split("，|；|;| and ");
    }
    List<String> namesList = null;
    if (authorNames != null) {
      namesList = new ArrayList<String>();
      for (String authorName : authorNames) {
        if (StringUtils.isNotBlank(authorName)) {
          namesList.add(authorName.trim());
        }
      }
    }
    return namesList;
  }

  /**
   * 解析导入成果pub_authors/author节点下的数据.
   * 
   * @param doc
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static List<Map<String, String>> parseOldAuthor(PubXmlDocument doc) {
    List pubAuthors = doc.getPubAuthorList();
    return parseOldAuthor(pubAuthors);
  }

  private static List<Map<String, String>> parseOldAuthor(List pubAuthors) {
    List<Map<String, String>> mapPsnList = null;// 拆分出来的用户
    if (pubAuthors != null && pubAuthors.size() > 0) {
      for (int k = 0; k < pubAuthors.size(); k++) {
        Element ele = (Element) pubAuthors.get(k);
        // 作者全名
        String au = StringUtils.trimToEmpty(ele.attributeValue("au"));
        // 机构地址
        String addr = StringUtils.trimToEmpty(ele.attributeValue("dept"));
        // 可能之前已经合并过了
        if (StringUtils.isBlank(addr)) {
          addr = StringUtils.trimToEmpty(ele.attributeValue("addr"));
        }
        String psnId = StringUtils.trimToEmpty(ele.attributeValue("psn_id"));
        // 是否是搜索当前单位的成果
        String issearchorg = StringUtils.trimToEmpty(ele.attributeValue("issearchorg"));
        String authorpos = StringUtils.trimToEmpty(ele.attributeValue("author_pos"));
        if (!"1".equals(authorpos)) {
          authorpos = "";
        }
        if (StringUtils.isNotBlank(au)) {
          // 存在的用户
          mapPsnList = mapPsnList == null ? new ArrayList<Map<String, String>>() : mapPsnList;
          // 查重
          Map<String, String> dup = null;
          for (Map<String, String> psn : mapPsnList) {
            String aname = psn.get("author_name");
            if (XmlUtil.compareAuthorName(au, aname)) {
              dup = psn;
              break;
            }
          }
          Map<String, String> map = new HashMap<String, String>();
          map.put("author_name", au);
          map.put("addr", addr);
          map.put("issearchorg", issearchorg);
          map.put("psnId", psnId);
          map.put("author_pos", authorpos);
          // 重复，则只合并
          if (dup != null) {
            ImportCommonAuthorMergeUtils.mergeSameAu(dup, map);
          } else {

            mapPsnList.add(map);
          }
        }
      }
    }
    return mapPsnList;
  }
}
