package com.smate.sie.center.task.pdwh.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;

/**
 * 基准库指派成果json转换为xml
 * 
 * @author sjzhou
 *
 */
public class PdwhPubJsonStrToXMLUtils {

  /**
   * pdwh成果json转map
   * 
   * @param pubJson
   * @return
   * @throws Exception
   */
  public static PubXmlDocument dealWithJsonStrToMap(String pubJson) throws Exception {
    Map mapData = JacksonUtils.jsonToMap(pubJson);
    return buildPubXmlDocument(mapData);
  }

  /**
   * 基准库的json数据构建XmlDocument.
   * 
   * @param data
   * @return
   * @throws Exception
   */
  public static PubXmlDocument buildPubXmlDocument(Map data) throws Exception {
    if (data == null) {
      throw new java.lang.NullPointerException("can't build XmlDocument with NULL paramters.");
    }
    PubXmlDocument xmlDoc = new PubXmlDocument();
    Iterator itor = data.keySet().iterator();
    while (itor.hasNext()) {
      String key = String.valueOf(itor.next());
      if (XmlUtil.isElementPath(key)) {
        String[] xpath = XmlUtil.splitXPath(key);
        // 保持原来pdwh的节点一致，只有publication一个大节点。
        Element element = (Element) xmlDoc.getNode("/publication");
        // 节点不存在，则创建
        if (element == null) {
          element = xmlDoc.createElement("/publication");
        }
        // 设置属性值
        if ("situations".equals(key)) {
          dealpubList(element, key, data);
        } else if ("typeInfo".equals(key)) {
          @SuppressWarnings("unchecked")
          Map<String, Object> indexMap = (Map<String, Object>) data.get(key);
          int pubType = Integer.parseInt(data.get("pubType").toString());
          switch (pubType) {
            case 1:
              dealPubAward(element, indexMap);
              break;
            case 2:
            case 10:
              dealPubBook(element, indexMap);
              break;
            case 3:
              dealPubConfPaper(element, indexMap);
              break;
            case 4:
              dealPubJournal(element, indexMap);
              break;
            case 5:
              dealPubPatent(element, indexMap);
              break;
            case 7:
              dealOther(element, indexMap);
              break;
            case 8:
              dealPubThesis(element, indexMap);
              break;
            default:
              break;
          }
        } else if ("members".equals(key)) {
          dealPubAuthors(xmlDoc, key, data);
        } else {
          dealPubBase(element, key, data);
        }
      }
    }
    return xmlDoc;
  }

  /**
   * 基本信息属性
   * 
   * @param element
   * @param key
   * @param data
   * @throws Exception
   */
  private static void dealPubBase(Element element, String key, Map data) throws Exception {
    String val = StringUtils.trimToEmpty(ObjectUtils.toString(data.get(key)));
    if ("title".equals(key)) {
      element.addAttribute("ctitle", HtmlUtils.htmlUnescape(val));
    } else if ("summary".equals(key)) {
      element.addAttribute("cabstract", HtmlUtils.htmlUnescape(val));
    } else if ("keywords".equals(key)) {
      element.addAttribute("ckeywords", val);
    } else if ("authorNames".equals(key)) {
      element.addAttribute("author_names", val);
    } else if ("srcFulltextUrl".equals(key)) {
      element.addAttribute("fulltext_url", val);
    } else if ("pubType".equals(key)) {
      element.addAttribute("pub_type", val);
    } else if ("citations".equals(key)) {
      element.addAttribute("isi_cited", val);
    } else if ("sourceId".equals(key)) {
      element.addAttribute("source_id", val);
    } else if ("citedUrl".equals(key)) {
      element.addAttribute("cite_record_url", val);
      element.addAttribute("tmpcite_record_url", val);
      // source_db_code可以说是source的机构,即source包含source_db_code
      // 后面将id替换为对应的name值
    } else if ("srcDbId".equals(key)) {
      element.addAttribute("source", val);
      element.addAttribute("source_db_code", val);
    } else if ("sourceUrl".equals(key)) {
      element.addAttribute("source_url", val);
      element.addAttribute("tmpsource_url", val);
    } else if ("publishDate".equals(key)) {
      // element.addAttribute("publish_date", val);
      element.addAttribute("pubyear", val);
    } else if ("countryId".equals(key)) {
      element.addAttribute("country_id", val);
    } else {// doi,fundinfo,organization
      element.addAttribute(key.toLowerCase(), val);
    }
  }

  /**
   * 解析成果收录信息
   * 
   * @param element
   * @param key
   * @param data
   * @return
   * @throws Exception
   */
  private static void dealpubList(Element element, String key, Map data) throws Exception {
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> listIndex = (List<Map<String, Object>>) data.get(key);
    StringBuffer sbIndex = new StringBuffer("");
    for (Map<String, Object> map : listIndex) {
      sbIndex.append(ObjectUtils.toString(map.get("libraryName")) + ";");
    }
    String citation_index = sbIndex.toString();
    if (citation_index.endsWith(";")) {
      citation_index = citation_index.substring(0, citation_index.length() - 1);
    }
    element.addAttribute("citation_index", StringUtils.trimToEmpty(citation_index));
  }

  /**
   * 解析著作信息
   * 
   * @param element
   * @param indexMap
   * @throws Exception
   */
  private static void dealPubBook(Element element, Map<String, Object> indexMap) throws Exception {
    Iterator otherItor = indexMap.keySet().iterator();
    while (otherItor.hasNext()) {
      String key = String.valueOf(otherItor.next());
      String val = StringUtils.trimToEmpty(ObjectUtils.toString(indexMap.get(key)));
      if ("name".equals(key)) {
        element.addAttribute("book_title", val);
      } else if ("type".equals(key)) {
        element.addAttribute("book_type", val);
      } else if ("totalWords".equals(key)) {
        element.addAttribute("total_words", val);
      } else if ("seriesName".equals(key)) {// 丛书名
        // element.addAttribute("series_name", val);
      } else if ("chapterNo".equals(key)) { // 章节号码
        // element.addAttribute("chapter_no", val);
      } else if ("publisher".equals(key) || "language".equals(key)) {// publisher,language
        element.addAttribute(key, val);
      } else if ("isbn".equals(key.toLowerCase())) {
        element.addAttribute("isbn", val);
      } else if ("pageNumber".equals(key)) {// 起止页码
        if (val.contains("-")) {
          String[] page = val.split("-");
          element.addAttribute("start_page", StringUtils.trimToEmpty(page[0]));
          element.addAttribute("end_page", StringUtils.trimToEmpty(page[1]));
        } else {// ROL-5417
          element.addAttribute("article_number", val);
        }
      }
    }
  }

  /**
   * 解析会议论文信息
   * 
   * @param root
   * @return
   */
  private static void dealPubConfPaper(Element element, Map<String, Object> indexMap) throws Exception {
    Iterator otherItor = indexMap.keySet().iterator();
    while (otherItor.hasNext()) {
      String key = String.valueOf(otherItor.next());
      String val = StringUtils.trimToEmpty(ObjectUtils.toString(indexMap.get(key)));
      if ("name".equals(key)) {// 会议名称
        element.addAttribute("proceeding_title", val);
      } else if ("startDate".equals(key)) {// 会议开始时间
        element.addAttribute("start_date", val);
      } else if ("endDate".equals(key)) {// 会议结束时间
        element.addAttribute("end_date", val);
      } else if ("original".equals(key) || "organizer".equals(key)) {// 集名，组织者
        element.addAttribute(key, val);
      }
    }
  }

  /**
   * 解析期刊类型成果信息
   * 
   * @param root
   * @return
   */
  private static void dealPubJournal(Element element, Map<String, Object> indexMap) throws Exception {
    Iterator otherItor = indexMap.keySet().iterator();
    while (otherItor.hasNext()) {
      String key = String.valueOf(otherItor.next());
      @SuppressWarnings("deprecation")
      String val = StringUtils.trimToEmpty(ObjectUtils.toString(indexMap.get(key)));
      if ("publishStatus".equals(key)) { // 发表状态(P已发表/A已接收),默认已发表
        element.addAttribute("publish_state", val);
      } else if ("pageNumber".equals(key)) {// 起止页码
        if (val.contains("-")) {
          String[] page = val.split("-");
          element.addAttribute("start_page", StringUtils.trimToEmpty(page[0]));
          element.addAttribute("end_page", StringUtils.trimToEmpty(page[1]));
        } else {// ROL-5417
          element.addAttribute("article_number", val);
        }
      } else if ("jid".equals(key) || "issue".equals(key) || "issn".equals(key)) {// jid,卷号,期号,issn
        element.addAttribute(key, val);
      } else if ("name".equals(key)) {
        element.addAttribute("original", val);
      } else if ("volumeNo".equals(key)) {
        element.addAttribute("volume", val);
      }
    }
  }

  /**
   * 解析专利信息
   * 
   * @param root
   * @return
   */
  private static void dealPubPatent(Element element, Map<String, Object> indexMap) throws Exception {
    Iterator otherItor = indexMap.keySet().iterator();
    while (otherItor.hasNext()) {
      String key = String.valueOf(otherItor.next());
      String val = StringUtils.trimToEmpty(ObjectUtils.toString(indexMap.get(key)));
      if ("applicationNo".equals(key)) { // 申请(专利)号
        element.addAttribute("patent_no", val);
      } else if ("ipc".equals(key)) {// 主分类号即ipc号
        element.addAttribute("patent_category_no", val);
      } else if ("type".equals(key)) {// 专利类型
        element.addAttribute("patent_category", val);
      } else if ("startDate".equals(key)) {// 开始日期,即授权日期
        element.addAttribute("start_date", val);
        element.addAttribute("effective_start_date", val);
      } else if ("issuingAuthority".equals(key)) {// 发证单位,跟表单属性名称保持一致。
        element.addAttribute("issue_org", val);
      } else if ("applier".equals(key) || "patentee".equals(key)) {// 申请
        element.addAttribute("patent_agent_person", val);
      } else if ("status".equals(key)) {
        if ("0".equals(val)) {
          element.addAttribute("patent_status", "申请");
        } else if ("1".equals(val)) {
          element.addAttribute("patent_status", "授权");
        }
      }
    }
  }

  /**
   * 解析获奖信息 基准库没有数据
   *
   * @param element
   * @param indexMap
   * @throws Exception
   */
  private static void dealPubAward(Element element, Map<String, Object> indexMap) throws Exception {
    Iterator otherItor = indexMap.keySet().iterator();
    while (otherItor.hasNext()) {
      String key = String.valueOf(otherItor.next());
      String val = StringUtils.trimToEmpty(ObjectUtils.toString(indexMap.get(key)));
      if ("certificateNo".equals(key)) { // 证书编号
        element.addAttribute("ceritficate_no", val);
      } else if ("awardDate".equals(key)) {// 授奖日期
        element.addAttribute("pubyear", val);
      } else if ("issuingAuthority".equals(key)) {// 授奖机构
        element.addAttribute("issue_ins_name", val);
      } else if ("grade".equals(key)) {// 等级
        element.addAttribute("award_grade_name", val);
      } else if ("category".equals(key)) {// 类别
        element.addAttribute("award_category_name", val);
      }
    }
  }

  /**
   * 解析学位论文信息
   *
   * @param element
   * @param indexMap
   * @throws Exception
   */
  private static void dealPubThesis(Element element, Map<String, Object> indexMap) throws Exception {
    Iterator otherItor = indexMap.keySet().iterator();
    while (otherItor.hasNext()) {
      String key = String.valueOf(otherItor.next());
      String val = StringUtils.trimToEmpty(ObjectUtils.toString(indexMap.get(key)));
      if ("issuingAuthority".equals(key)) { // 颁发单位
        element.addAttribute("issue_org", val);
      } else if ("defenseDate".equals(key)) {// 答辩日期
        element.addAttribute("pubyear", val);
      } else if ("department".equals(key)) {// 部门/专业
        element.addAttribute("department", val);
      } else if ("degree".equals(key)) {// 学位
        element.addAttribute("programme", val);
      }
    }
  }

  /**
   * 解析 其他 信息
   *
   * @param element
   * @param indexMap
   * @throws Exception
   */
  private static void dealOther(Element element, Map<String, Object> indexMap) throws Exception {

  }

  /**
   * 解析members
   *
   * @param xmlDoc
   * @param key
   * @param data
   * @throws Exception
   */
  @SuppressWarnings({"unchecked"})
  private static void dealPubAuthors(PubXmlDocument xmlDoc, String key, Map data) throws Exception {
    List<Map<String, Object>> authorsList = (List<Map<String, Object>>) data.get(key);
    if (authorsList != null) {
      Element pubEles = (Element) xmlDoc.getNode("/pub_authors");
      // 节点不存在，则创建
      if (pubEles == null) {
        pubEles = xmlDoc.createElement("/pub_authors");
      }
      int seqNo = 1;
      boolean flag = true;
      for (Map<String, Object> map : authorsList) {
        Element pubMember = pubEles.addElement("author");
        pubMember.addAttribute("seq_no", String.valueOf(seqNo));
        pubMember.addAttribute("member_psn_name", ObjectUtils.toString(map.get("name")));
        pubMember.addAttribute("email", ObjectUtils.toString(map.get("email")));
        List<Map<String, Object>> insNameList = (List<Map<String, Object>>) map.get("insNames");
        String insName = "";
        if (insNameList.size() > 0) {
          insName = ObjectUtils.toString(insNameList.get(0).get("insName"));
        }
        pubMember.addAttribute("ins_name", insName);
        pubMember.addAttribute("author_pos", flag == (boolean) map.get("communicable") ? "1" : "0");
        pubMember.addAttribute("first_author", flag == (boolean) map.get("communicable") ? "1" : "0");
        seqNo++;
      }
    }
  }

  public static void main(String[] args) throws Exception {
    String aa = "{\r\n" + "    \"pubId\": 17,\r\n"
        + "    \"title\": \"An&uacute;n<a>cios</a> de medicamentos stability analysis of maximum nongaussianity estimation in   independent component analysis\",\r\n"
        + "    \"summary\": \"The local stability analysis of maximum nongaussianity estimation (NINE)   is investigated for nonquadratic functions in independent component   analysis (ICA). Using trigonometric function, we first derive the local   stability condition of NINE for nonquadratic functions without any   approximation as has been made in previous literatures. The research   shows that the condition is essentially the generalization Of Xu';s   one-bit-matching ICA theorem in MNE. Secondly. based on the generalized   Gaussian model (CGM), the availability of local stability condition and   robustness to outliers are addressed for three typical nonquadratic   functions for various distributed independent components.\",\r\n"
        + "    \"keywords\": \"\",\r\n" + "    \"citations\": 0,\r\n"
        + "    \"briefDesc\": \"ADVANCES IN NEURAL NETWORKS - ISNN 2006, PT 1, SPRINGER-VERLAG BERLIN, HEIDELBERGER PLATZ 3, D-14197 BERLIN, GERMANY, pp 1133-1139, 2006\",\r\n"
        + "    \"authorNames\": \"Wang, Gang; Xu, Xin; Hu, Dewen\",\r\n" + "    \"doi\": \"\",\r\n"
        + "    \"organization\": \"Natl Univ Def Technol, Coll Mechatron &amp; Automat, Changsha 410073, Hunan,   Peoples R China.   AF Engn Univ, Telecommun Engn Inst, Xian 710077, Shanxi, Peoples R China. Hu, DW (reprint author), Natl Univ Def Technol, Coll Mechatron &amp; Automat, Changsha 410073, Hunan, Peoples R China\",\r\n"
        + "    \"fundInfo\": \"\",\r\n" + "    \"situations\": [{\r\n" + "        \"libraryName\": \"SCI\",\r\n"
        + "        \"sitStatus\": true,\r\n" + "        \"sitOriginStatus\": false,\r\n"
        + "        \"srcUrl\": \"http://apps.webofknowledge.com/InboundService.do?SID=@SID@&uml_return_url=http://pcs.webofknowledge.com/uml/uml_view.cgi?product_sid%S2igJonHFDcff498A2H&product=WOS&product_st_thomas=http://esti.isiknowledge.com:8360/esti/xrpc&sort_opt=Date&action=retrieve&product=WOS&mode=FullRecord&viewType=fullRecord&frmUML=1&UT=000238112000167\",\r\n"
        + "        \"srcDbId\": \"16\",\r\n" + "        \"srcId\": \"000238112000167\"\r\n" + "    }],\r\n"
        + "    \"members\": [],\r\n" + "    \"pubType\": 10,\r\n" + "    \"typeInfo\": {\r\n"
        + "        \"name\": \"ADVANCES IN NEURAL NETWORKS - ISNN 2006, PT 1\",\r\n" + "        \"type\": \"NULL\",\r\n"
        + "        \"language\": \"\",\r\n"
        + "        \"publisher\": \"SPRINGER-VERLAG BERLIN, HEIDELBERGER PLATZ 3, D-14197 BERLIN, GERMANY\",\r\n"
        + "        \"totalPages\": 0,\r\n" + "        \"totalWords\": 0,\r\n" + "        \"seriesName\": \"\",\r\n"
        + "        \"editors\": \"\",\r\n" + "        \"chapterNo\": \"\",\r\n"
        + "        \"pageNumber\": \"1133-1139\",\r\n" + "        \"publishStatus\": \"\",\r\n"
        + "        \"isbn\": \"\"\r\n" + "    },\r\n" + "    \"srcFulltextUrl\": \"\",\r\n" + "    \"srcDbId\": 16,\r\n"
        + "    \"sourceUrl\": \"http://apps.webofknowledge.com/InboundService.do?SID=@SID@&uml_return_url=http://pcs.webofknowledge.com/uml/uml_view.cgi?product_sid%S2igJonHFDcff498A2H&product=WOS&product_st_thomas=http://esti.isiknowledge.com:8360/esti/xrpc&sort_opt=Date&action=retrieve&product=WOS&mode=FullRecord&viewType=fullRecord&frmUML=1&UT=000238112000167\",\r\n"
        + "    \"sourceId\": \"000238112000167\",\r\n" + "    \"publishDate\": \"2006\",\r\n"
        + "    \"citedUrl\": \"\",\r\n" + "    \"hp\": false,\r\n" + "    \"hcp\": false,\r\n" + "    \"oa\": false\r\n"
        + "}";
    PubXmlDocument a = dealWithJsonStrToMap(aa);
    String ctitle = a.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "ctitle");
    String title = HtmlUtils.htmlUnescape(ctitle);
    // htmlEscape(ctitle, "ISO-8859-1");
    System.out.println("title：" + title);
  }
}
