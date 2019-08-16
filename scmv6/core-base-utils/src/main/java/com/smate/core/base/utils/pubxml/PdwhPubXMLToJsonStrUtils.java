package com.smate.core.base.utils.pubxml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.smate.core.base.utils.json.JacksonUtils;

/**
 * pdwh成果xml转json number 类型的结果，空值都返回0 ；
 * 
 * @author aijiangbin
 * @date 2018年7月11日
 */
public class PdwhPubXMLToJsonStrUtils {
  /**
   * pdwh成果xml转json
   * 
   * @param pubXml
   * @return
   * @throws Exception
   */
  public static String dealWithXML(String pubXml) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (StringUtils.isBlank(pubXml)) {
        return JacksonUtils.mapToJsonStr(result);
      }
      Document document = DocumentHelper.parseText(pubXml);
      Element root = document.getRootElement();
      // 成果基本信息
      result = dealPubBase(root);
      // 成员信息
      List<Map<String, Object>> pubMembersMapList = dealPubMembers(root);
      result.put("members", pubMembersMapList);
      int pubType = Integer.parseInt(result.get("pubType").toString());
      // 收录信息
      List<Map<String, Object>> pubInfoList = dealpubList(root);
      result.put("situations", pubInfoList);

      Map<String, Object> typeInfo = new HashMap<>();
      switch (pubType) {
        case 1:
          typeInfo = dealPubAward(root);
          break;
        case 2:
        case 10:
          typeInfo = dealPubBook(root);
          break;
        case 3:
          typeInfo = dealPubConfPaper(root);
          break;
        case 4:
          typeInfo = dealPubJournal(root);
          break;
        case 5:
          typeInfo = dealPubPatent(root);
          break;
        case 8:
          typeInfo = dealPubThesis(root);
          break;
        case 7:
          typeInfo = dealpubOther(root);
          break;
        default:
          typeInfo = dealpubOther(root);
          break;
      }
      result.put("pubTypeInfo", typeInfo);

    } catch (Exception e) {

      e.printStackTrace();
    }
    return JacksonUtils.mapToJsonStr(result);
  }

  /**
   * 解析成果列表信息
   * 
   * @param root
   * @return
   */
  private static List<Map<String, Object>> dealpubList(Element root) throws Exception {
    List<Map<String, Object>> list = new ArrayList<>();
    Element one = root.element("pub_list");
    if (one == null) {
      return list;
    }
    Map<String, Object> sci = new HashMap<>();
    sci.put("library", "SCI");
    sci.put("included", StringUtils.defaultString(one.attributeValue("list_sci")).equals("1") ? true : false);
    sci.put("originIncluded",
        StringUtils.defaultString(one.attributeValue("list_sci_source")).equals("1") ? true : false);
    sci.put("srcUrl", "");
    sci.put("srcDbId", "");
    sci.put("srcId", "");
    list.add(sci);

    Map<String, Object> ssci = new HashMap<>();
    ssci.put("library", "SSCI");
    ssci.put("included", StringUtils.defaultString(one.attributeValue("list_ssci")).equals("1") ? true : false);
    ssci.put("originIncluded",
        StringUtils.defaultString(one.attributeValue("list_ssci_source")).equals("1") ? true : false);
    ssci.put("srcUrl", "");
    ssci.put("srcDbId", "");
    ssci.put("srcId", "");
    list.add(ssci);

    Map<String, Object> ei = new HashMap<>();
    ei.put("library", "EI");
    ei.put("included", StringUtils.defaultString(one.attributeValue("list_ei")).equals("1") ? true : false);
    ei.put("originIncluded",
        StringUtils.defaultString(one.attributeValue("list_ei_source")).equals("1") ? true : false);
    ei.put("srcUrl", "");
    ei.put("srcDbId", "");
    ei.put("srcId", "");
    list.add(ei);

    Map<String, Object> istp = new HashMap<>();
    istp.put("library", "ISTP");
    istp.put("included", StringUtils.defaultString(one.attributeValue("list_istp")).equals("1") ? true : false);
    istp.put("originIncluded",
        StringUtils.defaultString(one.attributeValue("list_istp_source")).equals("1") ? true : false);
    istp.put("srcUrl", "");
    istp.put("srcDbId", "");
    istp.put("srcId", "");
    list.add(istp);

    Map<String, Object> cssci = new HashMap<>();
    cssci.put("library", "CSSCI");
    cssci.put("included", StringUtils.defaultString(one.attributeValue("list_cssci")).equals("1") ? true : false);
    cssci.put("originIncluded",
        StringUtils.defaultString(one.attributeValue("list_cssci_source")).equals("1") ? true : false);
    cssci.put("srcUrl", "");
    cssci.put("srcDbId", "");
    cssci.put("srcId", "");
    list.add(istp);

    Map<String, Object> pku = new HashMap<>();
    pku.put("library", "PKU");
    pku.put("included", StringUtils.defaultString(one.attributeValue("list_pku")).equals("1") ? true : false);
    pku.put("originIncluded",
        StringUtils.defaultString(one.attributeValue("list_pku_source")).equals("1") ? true : false);
    pku.put("srcUrl", "");
    pku.put("srcDbId", "");
    pku.put("srcId", "");
    list.add(istp);

    Map<String, Object> other = new HashMap<>();
    other.put("library", "OTHER");
    other.put("included", StringUtils.defaultString(one.attributeValue("list_other")).equals("1") ? true : false);
    other.put("originIncluded",
        StringUtils.defaultString(one.attributeValue("list_other_source")).equals("1") ? true : false);
    other.put("srcUrl", "");
    other.put("srcDbId", "");
    other.put("srcId", "");
    list.add(other);

    return list;
  }

  /**
   * 解析成果其他信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealpubOther(Element root) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 发表开始日期
    map.put("startDate", StringUtils.defaultString(one.attributeValue("start_date")));
    // 发表结束日期
    map.put("endDate", StringUtils.defaultString(one.attributeValue("end_date")));
    return map;
  }

  /**
   * 解析XX类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubThesis(Element root) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 学位
    map.put("degree", StringUtils.defaultString(one.attributeValue("thesis_programme")));
    // 颁发单位
    map.put("issuingAuthority", StringUtils.defaultString(one.attributeValue("thesis_ins_name")));
    // 部门
    map.put("department", StringUtils.defaultString(one.attributeValue("thesis_dept_name")));

    // 答辩日期
    map.put("defenseDate", StringUtils.defaultString(one.attributeValue("pubyear")));

    return map;
  }

  /**
   * 解析专利类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubPatent(Element root) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 申请(专利)号
    map.put("applicationNo", StringUtils.defaultString(one.attributeValue("patent_no")));
    // 公开（公告）号
    map.put("publicationOpenNo", StringUtils.defaultString(one.attributeValue("patent_open_no")));
    // 主分类号
    map.put("categoryNo", StringUtils.defaultString(one.attributeValue("patent_category_no")));
    // 专利国家
    map.put("area", StringUtils.defaultString(one.attributeValue("country")));
    // 专利类别 数字 51 52
    map.put("type", StringUtils.defaultString(one.attributeValue("patent_category")));
    // 生效开始日期
    map.put("startDate", StringUtils.defaultString(one.attributeValue("start_date")));
    // 生效截止日期
    map.put("endDate", StringUtils.defaultString(one.attributeValue("end_date")));
    // 发证单位
    map.put("issuingAuthority", StringUtils.defaultString(one.attributeValue("patent_agent_org")));
    // 专利状态(0申请，1授权)
    map.put("status", StringUtils.defaultString(one.attributeValue("legal_status").trim().equals("授权") ? "1" : "0"));
    // 专利授权人/申请人
    map.put("applier", StringUtils.defaultString(one.attributeValue("patent_agent_person")));
    // 专利转化状态 TODO
    map.put("transitionStatus", StringUtils.defaultString(one.attributeValue("patent_transition_status")));
    // 交易金额 amount patent_price
    map.put("price", StringUtils.defaultString(one.attributeValue("amount")));
    // 申请日期
    map.put("applicationDate", StringUtils.defaultString(one.attributeValue("application_date")));

    return map;
  }

  /**
   * 解析期刊类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubJournal(Element root) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 期刊ID
    map.put("jid", StringUtils.defaultString(one.attributeValue("jid")));
    Element publication = root.element("publication");
    if (publication != null) {
      // 影响因子(发表年/最新年)
      map.put("impactFactors", StringUtils.defaultString(publication.attributeValue("impact_factors")));
      // 发表状态(P已发表/A已接收)
      map.put("publishStatus", StringUtils.defaultString(publication.attributeValue("publish_state")));
      // 发表日期
      map.put("publishDate", StringUtils.defaultString(publication.attributeValue("pubyear")));
      // 期号
      map.put("volumeNo", StringUtils.defaultString(publication.attributeValue("volume")));
      // 卷号
      map.put("issue", StringUtils.defaultString(publication.attributeValue("issue")));
      // 起始页码
      map.put("startPage", NumberUtils.toInt(publication.attributeValue("start_page")));
      // 结束页码
      map.put("endPage", NumberUtils.toInt(publication.attributeValue("end_page")));
      // 文章号
      map.put("articleNo", StringUtils.defaultString(publication.attributeValue("article_number")));
      // 接收日期,发表状态为已接受才有
      map.put("acceptDate", StringUtils.defaultString(publication.attributeValue("accept_date")));
    }

    return map;
  }

  /**
   * 解析XX类型成果信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubConfPaper(Element root) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 类型 没有这个字段
    // map.put("conf_type", );
    // 论文类别 TODO
    map.put("paperType", StringUtils.defaultString(one.attributeValue("paper_type"),
        StringUtils.defaultString(one.attributeValue("conf_type"))));
    // 会议名称
    map.put("title", StringUtils.defaultString(one.attributeValue("proceeding_title")));
    // 会议组织者
    map.put("organizer", StringUtils.defaultString(one.attributeValue("organizer")));

    // 开始日期
    map.put("startDate", StringUtils.defaultString(one.attributeValue("start_date")));
    // 结束日期
    map.put("endDate", StringUtils.defaultString(one.attributeValue("end_date")));

    map.put("publishDate", StringUtils.defaultString(one.attributeValue("publish_date")));
    map.put("startPage", NumberUtils.toInt(one.attributeValue("start_page")));
    map.put("endPage", NumberUtils.toInt(one.attributeValue("end_page")));
    map.put("articleNo", StringUtils.defaultString(one.attributeValue("article_number")));

    return map;
  }

  /**
   * 解析XX类型成果信息 书籍章节和书著有点区别？？？
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubBook(Element root) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 书名
    map.put("name", StringUtils.defaultString(one.attributeValue("book_title")));
    // 书籍类型 no
    map.put("type", StringUtils.defaultString(one.attributeValue("book_type")));
    // 出版社
    map.put("publisher", StringUtils.defaultString(one.attributeValue("publisher")));
    // 页数 no
    map.put("totalPages", NumberUtils.toInt(one.attributeValue("total_pages"), 0));
    // 总字数 no
    map.put("totalWords", NumberUtils.toInt(one.attributeValue("total_words"), 0));
    // 丛书名
    map.put("seriesName", StringUtils.defaultString(one.attributeValue("series_name")));
    // 书籍编辑
    map.put("editors", StringUtils.defaultString(one.attributeValue("editors")));
    // 章节号码
    map.put("chapterNo", StringUtils.defaultString(one.attributeValue("chapter_no")));
    // 出版状态 0 =未出版 ； 1=已出版
    map.put("publishStatus", StringUtils.defaultString(one.attributeValue("publish_state")));

    // 出版日期
    map.put("publishDate", StringUtils.defaultString(one.attributeValue("publish_date")));
    // 起始页码
    map.put("startPage", NumberUtils.toInt(one.attributeValue("start_page")));
    // 结束页码
    map.put("endPage", NumberUtils.toInt(one.attributeValue("end_page")));
    // 文章号
    map.put("articleNo", StringUtils.defaultString(one.attributeValue("article_number")));
    // 书籍isbn
    map.put("isbn", StringUtils.defaultString(one.attributeValue("isbn")));

    return map;
  }

  /**
   * 解析成果奖励信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubAward(Element root) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 奖励类别
    map.put("category", StringUtils.defaultString(one.attributeValue("award_category")));
    // 奖励等级
    map.put("grade", StringUtils.defaultString(one.attributeValue("award_grade")));
    // 颁发机构
    map.put("issuingAuthority", StringUtils.defaultString(one.attributeValue("issue_ins_name")));
    // 颁发机构ID
    map.put("issueInsId", NumberUtils.toLong(one.attributeValue("issue_ins_id"), 0L));
    // 证书编号
    map.put("certificateNo", StringUtils.defaultString(one.attributeValue("serial_number")));
    Element publication = root.element("publication");
    if (publication != null) {
      // 授奖日期 2016/6/6
      map.put("awardDate", StringUtils.defaultString(publication.attributeValue("publish_date")));
    }
    return map;
  }

  /**
   * 解析成果基本信息
   * 
   * @param root
   * @return
   */
  private static Map<String, Object> dealPubBase(Element root) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Element one = root.element("publication");
    if (one == null) {
      return map;
    }
    // 中文标题//外文标题
    map.put("title", StringUtils.isNotBlank(one.attributeValue("ctitle")) ? one.attributeValue("ctitle")
        : one.attributeValue("etitle"));
    // 中文摘要//外文摘要
    map.put("summary", StringUtils.isNotBlank(one.attributeValue("cabstract")) ? one.attributeValue("cabstract")
        : one.attributeValue("eabstract"));
    // 中文关键词//外文关键词
    map.put("keywords", StringUtils.isNotBlank(one.attributeValue("ckeywords")) ? one.attributeValue("ckeywords")
        : one.attributeValue("ekeywords"));
    // 国家或地区ID TODO
    map.put("countryId", NumberUtils.toLong(one.attributeValue("country_id")));
    // 备注
    map.put("remarks", StringUtils.defaultString(one.attributeValue("remark")));
    // 来源 TODO 经过拼装的
    map.put("briefDesc", "");
    // 作者
    map.put("authorNames", StringUtils.defaultString(one.attributeValue("author_names")));
    // doi
    map.put("doi", StringUtils.defaultString(one.attributeValue("doi")));
    // 基金标注
    map.put("fundInfo", StringUtils.defaultString(one.attributeValue("fundinfo")));
    // "organization": "单位地址信息",
    map.put("organization", StringUtils.defaultString(one.attributeValue("organization")));
    // 来源全文路径
    map.put("srcFulltextUrl", StringUtils.defaultString(one.attributeValue("fulltext_url")));
    map.put("fulltextId", NumberUtils.toLong(one.attributeValue("file_id"), 0));
    map.put("pubType", NumberUtils.toLong(one.attributeValue("pub_type"), 7));
    map.put("pubId", NumberUtils.toLong(one.attributeValue("pub_id"), 0));
    return map;
  }

  /**
   * 解析成果类型信息
   * 
   * @param root
   * @return
   */
  private static Map<String, String> dealPubType(Element root) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    Element one = root.element("pub_type");
    if (one == null) {
      return map;
    }
    // 类别ID
    map.put("id", StringUtils.defaultString(one.attributeValue("id")));
    // 类别中文名
    map.put("zh_name", StringUtils.defaultString(one.attributeValue("zh_name")));
    // 类别英文名
    map.put("en_name", StringUtils.defaultString(one.attributeValue("en_name")));
    // 成果/文献/工作文档/项目标识
    map.put("article_type", StringUtils.defaultString(one.attributeValue("article_type")));
    return map;
  }

  /**
   * 解析成果全文信息
   * 
   * @param root
   * @return
   */
  private static Map<String, String> dealPubFulltext(Element root) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    Element one = root.element("pub_fulltext");
    if (one == null) {
      return map;
    }
    // 全文附件描述
    map.put("file_desc", StringUtils.defaultString(one.attributeValue("file_desc")));
    // 全文附件名
    map.put("file_name", StringUtils.defaultString(one.attributeValue("file_name")));
    // 全文附件扩展名
    map.put("file_ext", StringUtils.defaultString(one.attributeValue("file_ext")));
    // 全文附件ID
    map.put("file_id", StringUtils.defaultString(one.attributeValue("file_id")));
    // 上传日期
    map.put("upload_date", StringUtils.defaultString(one.attributeValue("upload_date")));
    // 全文链接
    map.put("fulltext_url", StringUtils.defaultString(one.attributeValue("fulltext_url")));
    // 域名信息
    map.put("domain_context", StringUtils.defaultString(one.attributeValue("domain_context")));
    // 单位ID
    map.put("ins_id", StringUtils.defaultString(one.attributeValue("ins_id")));
    // 节点ID
    map.put("node_id", StringUtils.defaultString(one.attributeValue("node_id")));
    return map;
  }

  /**
   * 解析成果附件信息
   * 
   * @param root
   * @return
   */
  private static List<Map<String, String>> dealPubAttachments(Element root) throws Exception {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    Element pub_attachments = root.element("pub_attachments");
    if (pub_attachments == null) {
      return list;
    }
    List<Element> elements = pub_attachments.elements("pub_attachment");
    if (elements != null && elements.size() > 0) {
      Map<String, String> map = null;
      for (Element one : elements) {
        map = new HashMap<String, String>();
        // 顺序号
        map.put("seq_no", StringUtils.defaultString(one.attributeValue("seq_no")));
        // 附件ID
        map.put("file_id", StringUtils.defaultString(one.attributeValue("file_id")));
        list.add(map);
      }
    }
    return list;
  }

  /**
   * 解析成果成员信息
   * 
   * @param root
   * @return
   */
  private static List<Map<String, Object>> dealPubMembers(Element root) throws Exception {
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    Element pub_members = root.element("pub_authors");
    if (pub_members == null) {
      return list;
    }
    List<Element> elements = pub_members.elements("author");
    if (elements != null && elements.size() > 0) {
      Map<String, Object> map = null;
      int index = 1;
      for (Element one : elements) {
        map = new HashMap<String, Object>();
        // 作者唯一标识
        map.put("id", NumberUtils.toLong(one.attributeValue("pm_id")));
        // 顺序号
        map.put("seqNo", NumberUtils.toLong(one.attributeValue("seq_no"), index++));
        // 作者名
        map.put("name", StringUtils.defaultString(one.attributeValue("au")));
        // 作者Email
        map.put("email", StringUtils.defaultString(one.attributeValue("email")));
        // 作者身份
        map.put("communicable", StringUtils.defaultString(one.attributeValue("author_pos")).equals("1") ? true : false);
        // 机构名称
        map.put("insName", StringUtils.defaultString(one.attributeValue("ins_name")));
        // 第一作者
        map.put("firstAuthor",
            StringUtils.defaultString(one.attributeValue("first_author")).equals("1") ? true : false);
        list.add(map);
      }
    }
    return list;
  }

}
