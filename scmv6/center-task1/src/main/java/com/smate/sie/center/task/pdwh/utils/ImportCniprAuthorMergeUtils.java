package com.smate.sie.center.task.pdwh.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;

/**
 * 导入CNKI成果XML作者重构工具类.
 * 
 * @author liqinghua
 * 
 */
public class ImportCniprAuthorMergeUtils {

  /**
   * 将导入成果XML作者进行查重合并.
   * 
   * @param doc
   * @return
   */
  public static PubXmlDocument megergeImportAuthor(PubXmlDocument doc) {

    // 判断是否已经合并，如果已经合并过，不需要再次合并
    Element pubAuthors = doc.getPubAuthors();
    if (pubAuthors != null) {
      String mergeFlag = pubAuthors.attributeValue("merge_flag");
      if ("1".equals(mergeFlag)) {
        return doc;
      }
    }

    List<String> namesList = parseNameList(doc);
    // 删除原有pub_authors节点
    doc.removeNode("/pub_authors");
    // 创建新的pub_authors节点
    Element authsEle = doc.createElement("/pub_authors");
    List<String> pubAddrs = parsePubAddr(doc);
    List<Map<String, String>> newAuthors = covertNamesToAuthor(pubAddrs, namesList);
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
  public static Element mergeImportAuthor(String pubOrgs, Element data) {
    // 判断是否已经合并，如果已经合并过，不需要再次合并
    Element pubAuthors = (Element) data.selectSingleNode("pub_authors");
    if (pubAuthors != null) {
      String mergeFlag = pubAuthors.attributeValue("merge_flag");
      if ("1".equals(mergeFlag)) {
        return data;
      }
    }
    List<String> namesList = parseNameList(data);
    if (pubAuthors != null) {
      // 删除原有pub_authors节点
      data.remove(pubAuthors);
    }
    // 创建新的pub_authors节点
    Element authsEle = data.addElement("pub_authors");
    List<String> pubAddrs = parsePubAddr(pubOrgs);
    List<Map<String, String>> newAuthors = covertNamesToAuthor(pubAddrs, namesList);
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
  public static List<Map<String, String>> covertNamesToAuthor(List<String> pubAddrs, List<String> namesList) {

    List<Map<String, String>> newAuthors = new ArrayList<Map<String, String>>();

    String pubAddr = "";
    // 如果成果只有一个地址，则将地址补充到所有作者里面
    if (CollectionUtils.isNotEmpty(pubAddrs) && pubAddrs.size() == 1) {
      pubAddr = pubAddrs.get(0);
    }

    int seqNo = 1;
    for (int i = 0; namesList != null && i < namesList.size(); i++) {
      String name = namesList.get(i);
      Map<String, String> newAuthor = new HashMap<String, String>();
      newAuthor.put("au", name);
      newAuthor.put("name", name);
      newAuthor.put("addr", pubAddr);
      newAuthor.put("seq_no", String.valueOf(seqNo));
      seqNo++;
      newAuthors.add(newAuthor);
    }
    return newAuthors;
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

  private static List<String> parseNameList(String authorNamesStr) {
    String[] authorNames = null;
    if (StringUtils.isNotBlank(authorNamesStr)) {
      authorNames = authorNamesStr.split(";");
    }
    List<String> namesList = null;
    if (authorNames != null) {
      namesList = new ArrayList<String>();
      for (String authorName : authorNames) {
        if (StringUtils.isNotBlank(authorName)) {
          namesList.add(authorName);
        }
      }
    }
    return namesList;
  }

  /**
   * 解析成果地址.
   * 
   * @param doc
   * @return
   */
  public static List<String> parsePubAddr(String orgsStr) {

    if (StringUtils.isNotBlank(orgsStr)) {

      // 获取机构名称列表，构造cnkiPubCacheOrgs对象
      Set<String> pubAddrSet = XmlUtil.parseCniprPubAddrs(orgsStr);
      List<String> addrList = new ArrayList<String>();
      outer_loop: for (String pubAddr : pubAddrSet) {
        if (StringUtils.isBlank(pubAddr)) {
          continue outer_loop;
        }
        // 判断包含关系
        for (int i = 0; i < addrList.size(); i++) {
          String laddr = addrList.get(i).toLowerCase();
          String lpubAddr = pubAddr.toLowerCase();
          if (laddr.indexOf(lpubAddr) > -1) {
            continue outer_loop;
          } else if (lpubAddr.indexOf(laddr) > -1) {
            addrList.set(i, pubAddr);
            continue outer_loop;
          }
        }
        addrList.add(pubAddr);
      }
      return addrList;
    }
    return null;
  }

  /**
   * 解析成果地址.
   * 
   * @param doc
   * @return
   */
  public static List<String> parsePubAddr(PubXmlDocument doc) {

    if (doc == null) {
      return null;
    }
    // 解析成果地址
    String orgsStr = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "organization");
    if (StringUtils.isNotBlank(orgsStr)) {
      return parsePubAddr(orgsStr);
    }
    return null;
  }

}
