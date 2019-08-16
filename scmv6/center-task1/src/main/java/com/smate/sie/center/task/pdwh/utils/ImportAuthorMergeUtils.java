package com.smate.sie.center.task.pdwh.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;

/**
 * 导入ISI成果XML作者重构工具类.
 * 
 * @author zk
 * 
 */
public class ImportAuthorMergeUtils {

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

    List<String[]> namesList = parseNameList(doc);
    Map<String, Object> parseResult = parseOldAuthor(doc);
    // 删除原有pub_authors节点
    doc.removeNode("/pub_authors");
    // 创建新的pub_authors节点
    Element authsEle = doc.createElement("/pub_authors");
    List<String> pubAddrs = parsePubAddr(doc);
    List<Map<String, String>> newAuthors = mergeNamesToOldAuthor(pubAddrs, namesList, parseResult);
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

    List<String[]> namesList = parseNameList(data);
    Map<String, Object> parseResult = parseOldAuthor(data.selectNodes("pub_authors/author"));
    if (pubAuthors != null) {
      // 删除原有pub_authors节点
      data.remove(pubAuthors);
    }

    // 创建新的pub_authors节点
    Element authsEle = data.addElement("pub_authors");
    List<String> pubAddrs = parsePubAddr(pubOrgs);
    List<Map<String, String>> newAuthors = mergeNamesToOldAuthor(pubAddrs, namesList, parseResult);
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
  @SuppressWarnings("unchecked")
  public static List<Map<String, String>> mergeNamesToOldAuthor(List<String> pubAddrs, List<String[]> namesList,
      Map<String, Object> oldAuthor) {

    List<Map<String, String>> newAuthors = new ArrayList<Map<String, String>>();

    String noPsnEmails = (String) oldAuthor.get("noPsnEmails");// 未匹配到用户的email
    List<Map<String, String>> mapPsns = (List<Map<String, String>>) oldAuthor.get("mapPsn");// 拆分出来的用户
    String pubAddr = "";
    // 如果成果只有一个地址，则将地址补充到所有作者里面
    if (CollectionUtils.isNotEmpty(pubAddrs) && pubAddrs.size() == 1) {
      pubAddr = pubAddrs.get(0);
    }

    int seqNo = 1;
    for (int i = 0; namesList != null && i < namesList.size(); i++) {
      String[] names = namesList.get(i);
      String abbr = names[0];
      String full = names[1];
      String name = StringUtils.isBlank(full) ? abbr : full;
      Map<String, String> newAuthor = new HashMap<String, String>();
      newAuthor.put("name", name);
      newAuthor.put("au", name);
      newAuthor.put("full_name", full);
      newAuthor.put("addr", pubAddr);
      newAuthor.put("abbr_name", StringUtils.trimToEmpty(abbr));
      newAuthor.put("seq_no", String.valueOf(seqNo));
      seqNo++;
      // 查重合并
      for (int j = 0; mapPsns != null && j < mapPsns.size(); j++) {
        Map<String, String> mapPsn = mapPsns.get(j);
        String authorName = mapPsn.get("author_name");
        // 匹配是否相同，相同则合并
        if (XmlUtil.compareAuthorName(abbr, authorName) || XmlUtil.compareAuthorName(full, authorName)) {
          mergeSameAu(newAuthor, mapPsn);
          mapPsns.remove(j);
          j--;
        }
      }
      newAuthors.add(newAuthor);
    }

    // 可能存在上下作者名对不上的情况，需要将剩下的作者补充过去.
    for (int j = 0; mapPsns != null && j < mapPsns.size(); j++) {
      String authorName = mapPsns.get(j).get("author_name");
      Map<String, String> newAuthor = new HashMap<String, String>();
      newAuthor.put("name", authorName);
      newAuthor.put("au", authorName);
      newAuthor.put("full_name", "");
      newAuthor.put("abbr_name", authorName);
      newAuthor.put("seq_no", String.valueOf(seqNo));
      String addr = mapPsns.get(j).get("addr");
      newAuthor.put("addr", StringUtils.isBlank(addr) ? pubAddr : addr);
      String psnId = mapPsns.get(j).get("psnId");
      psnId = NumberUtils.isDigits(psnId) ? psnId : "";
      newAuthor.put("psn_id", psnId);
      newAuthor.put("author_pos", mapPsns.get(j).get("author_pos"));
      newAuthor.put("email", mapPsns.get(j).get("email"));
      newAuthor.put("issearchorg", mapPsns.get(j).get("issearchorg"));
      newAuthors.add(newAuthor);
      seqNo++;
    }

    // 剩余EMAIL，作为一个独立的作者
    if (StringUtils.isNotBlank(noPsnEmails)) {
      Map<String, String> newAuthor = new HashMap<String, String>();
      newAuthor.put("email", noPsnEmails);
      newAuthors.add(newAuthor);
    }
    return newAuthors;
  }

  /**
   * 合并相同的作者.
   * 
   * @param remainAuthor
   * @param mapPsn
   */
  private static void mergeSameAu(Map<String, String> remainAuthor, Map<String, String> mapPsn) {
    // 地址
    String addr = StringUtils.trimToEmpty(remainAuthor.get("addr"));
    String fillAddr = mapPsn.get("addr");
    if (StringUtils.isNotBlank(fillAddr)) {
      addr = StringUtils.isBlank(addr) ? fillAddr : addr + "." + fillAddr;
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

    // email
    String email = StringUtils.trimToEmpty(remainAuthor.get("email"));
    String fillEmail = mapPsn.get("email");
    if (StringUtils.isNotBlank(fillEmail)) {
      email = StringUtils.isBlank(email) ? fillEmail : email + " " + fillEmail;
    }
    remainAuthor.put("email", email);
    remainAuthor.put("issearchorg", mapPsn.get("issearchorg"));
  }

  /**
   * 比较作者名是否相同.
   * 
   * @param abbr
   * @param full
   * @param authorName
   * @return
   */
  private static boolean compareUserName(String abbr, String full, String authorName) {

    // 有full_name先比较full_name
    if (authorName.equalsIgnoreCase(full)) {
      return true;
    } else if (authorName.equalsIgnoreCase(abbr)) {
      return true;
    }
    return false;
  }

  /**
   * 解析抓取数据publication下面用户名称数据.
   * 
   * @param doc
   * @return
   */
  private static List<String[]> parseNameList(PubXmlDocument doc) {

    String authorNamesStr = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    String authorNamesAbbrStr = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names_abbr");
    if (!"".equals(authorNamesStr)) {
      return parseNameList(authorNamesStr, authorNamesAbbrStr);
    } else {
      authorNamesStr = doc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
      return parseNameList(authorNamesStr, authorNamesAbbrStr);
    }
  }

  private static List<String[]> parseNameList(String authorNamesStr, String authorNamesAbbrStr) {
    String[] abbrNames = null;
    String[] fullNames = null;
    if (StringUtils.isNotBlank(authorNamesAbbrStr)) {
      // 去掉<>符号
      authorNamesAbbrStr = authorNamesAbbrStr.replaceAll("<.*?>", ";");
      abbrNames = authorNamesAbbrStr.split(";");
    }
    if (StringUtils.isNotBlank(authorNamesStr)) {
      // 去掉<>符号
      authorNamesStr = authorNamesStr.replaceAll("<.*?>", ";");
      fullNames = authorNamesStr.split(";");
    }
    List<String[]> namesList = null;
    if (abbrNames != null) {
      namesList = new ArrayList<String[]>();
      for (int i = 0; i < abbrNames.length; i++) {
        String fullName = null;
        String abbrName = abbrNames[i].trim();
        if (fullNames.length > i) {
          fullName = fullNames[i].trim();
        }
        if (abbrName.equalsIgnoreCase(fullName)) {
          fullName = "";
        }
        String[] names = new String[] {abbrName, fullName};
        namesList.add(names);
      }
    }
    return namesList;
  }

  /**
   * 解析抓取数据publication下面用户名称数据.
   * 
   * @param doc
   * @return
   */
  private static List<String[]> parseNameList(Element data) {

    Element pub = (Element) data.selectSingleNode("publication");
    String authorNamesStr = pub.attributeValue("author_names");
    String authorNamesAbbrStr = pub.attributeValue("author_names_abbr");
    return parseNameList(authorNamesStr, authorNamesAbbrStr);
  }

  /**
   * 解析导入成果pub_authors/author节点下的数据.
   * 
   * @param doc
   * @return
   */
  @SuppressWarnings("rawtypes")
  private static Map<String, Object> parseOldAuthor(PubXmlDocument doc) {
    List pubAuthors = doc.getPubAuthorList();
    return parseOldAuthor(pubAuthors);
  }

  /**
   * 解析导入成果pub_authors/author节点下的数据.
   * 
   * @param doc
   * @return
   */
  @SuppressWarnings("rawtypes")
  private static Map<String, Object> parseOldAuthor(Element aue) {

    return parseOldAuthor(aue.selectNodes("/author"));
  }

  private static Map<String, Object> parseOldAuthor(List pubAuthors) {
    List<Map<String, String>> mapPsn = null;// 拆分出来的用户
    StringBuilder noPsnEmails = new StringBuilder();// 未匹配到用户的email
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
        // 是否是搜索当前单位的成果
        String issearchorg = StringUtils.trimToEmpty(ele.attributeValue("issearchorg"));
        String email = StringUtils.trimToEmpty(ele.attributeValue("email"));
        String psnId = StringUtils.trimToEmpty(ele.attributeValue("psn_id"));
        String authorpos = StringUtils.trimToEmpty(ele.attributeValue("author_pos"));
        if (!"1".equals(authorpos)) {
          authorpos = "";
        }
        if (StringUtils.isBlank(au)) {
          if (StringUtils.isNotBlank(email)) {
            if (noPsnEmails.length() > 0) {
              noPsnEmails.append(";").append(email);
            } else {
              noPsnEmails.append(email);
            }
          }
        } else {
          // 存在的用户
          mapPsn = mapPsn == null ? new ArrayList<Map<String, String>>() : mapPsn;

          // 查重
          Map<String, String> dup = null;
          for (Map<String, String> psn : mapPsn) {
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
          map.put("email", email);
          map.put("psnId", psnId);
          map.put("author_pos", authorpos);
          // 重复，则合并
          if (dup != null) {
            ImportAuthorMergeUtils.mergeSameAu(dup, map);
          } else {
            mapPsn.add(map);
          }
        }
      }
    }
    Map<String, Object> parseResult = new HashMap<String, Object>();
    parseResult.put("mapPsn", mapPsn);
    parseResult.put("noPsnEmails", noPsnEmails.length() == 0 ? "" : noPsnEmails.toString());
    return parseResult;
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
      Set<String> pubAddrSet = XmlUtil.parseIsiPubAddrs(orgsStr);
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

  public static void main(String[] args) {

    System.out.println("1;2<br fasfds>3;4<br fsdf>5".replaceAll("<.*?>", ";"));
  }
}
