package com.smate.center.open.service.data.pub;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.solr.service.SolrIndexSerivceImpl;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;

/**
 * 检索科研之友数据信息服务类 , 调用solr service服务类
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class V_SearchSmateDataInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  public SolrIndexService solrIndexService;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Value("${domainscm}")
  private String scmDomain;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object pageNo = serviceData.get("pageNo");
    Object pageSize = serviceData.get("pageSize");
    Object searchKey = serviceData.get("searchKey");
    Object dataType = serviceData.get("dataType");
    if (pageNo == null) {
      logger.error("具体服务类型参数pageNo不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_272, paramet, "具体服务类型参数pageNo不能为空");
      return temp;
    }
    if (pageSize == null) {
      logger.error("具体服务类型参数pageSize不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_273, paramet, "具体服务类型参数pageSize不能为空");
      return temp;
    }
    if (!NumberUtils.isNumber(pageNo.toString()) || NumberUtils.toInt(serviceData.get("pageNo").toString()) < 1) {
      logger.error("具体服务类型参数pageNo格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_274, paramet, "具体服务类型参数pageNo格式不正确");
      return temp;
    }
    if (!NumberUtils.isNumber(pageSize.toString()) || NumberUtils.toInt(serviceData.get("pageSize").toString()) < 1) {
      logger.error("具体服务类型参数pageSize格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_275, paramet, "具体服务类型参数pageSize格式不正确");
      return temp;
    }
    if (searchKey == null || StringUtils.isBlank(searchKey.toString())) {
      logger.error("具体服务类型参数searchKey不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_293, paramet, "具体服务类型参数searchKey不能为空");
      return temp;
    }
    String serviceType = dataType == null ? "" : dataType.toString();

    if (serviceType == null
        || (!"paper".equals(serviceType) && !"patent".equals(serviceType) && !"person".equals(serviceType))) {
      logger.error("具体服务类型参数dataType数据不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_294, paramet, "具体服务类型参数dataType数据不正确");
      return temp;
    }
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 用于判断详情页面优先显示的内容
    String orderBy = paramet.get("orderBy") == null ? "DEFAULT" : paramet.get("orderBy").toString();
    Object openIdObj = paramet.get("openId");
    /*
     * String language = paramet.get("language") == null ? QueryFields.LANGUAGE_ALL :
     * paramet.get("language").toString();
     */
    List<Integer> pubTypeList = parsePubType(paramet);
    List<Long> excludePubId = parseExcludePubId(paramet);
    Integer pageNo = NumberUtils.toInt(paramet.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(paramet.get("pageSize").toString());
    String searchKey = paramet.get("searchKey").toString();
    QueryFields queryFields = new QueryFields();
    // 过滤成果类型
    if (pubTypeList.size() > 0 && "paper".equals(paramet.get("dataType"))) {
      queryFields.setPubTypeIdList(pubTypeList);
    }
    // 过滤成果id
    if (excludePubId.size() > 0) {
      queryFields.setExcludedPubIds(excludePubId);
    }
    queryFields.setSearchString(searchKey);
    queryFields.setOrderBy(orderBy);
    if (XmlUtil.containZhChar(searchKey)) {
      queryFields.setLanguage(queryFields.LANGUAGE_ZH);
    } else {
      queryFields.setLanguage(queryFields.LANGUAGE_EN);
    }
    // 直接检索
    Map<String, Object> mapData = new HashMap<String, Object>();
    try {
      if ("paper".equals(paramet.get("dataType"))) {
        String string = queryFields.toString();
        mapData = solrIndexService.queryPubs(pageNo, pageSize, queryFields);
        buildExtraInfo(mapData, "pubId");
      } else if ("person".equals(paramet.get("dataType"))) {
        try {
          if (openIdObj != null && StringUtils.isNotBlank(openIdObj.toString()) && openIdObj.toString().length() == 8) { // 检索人员信息
                                                                                                                         // openId优先
            queryFields.setSearchString(openIdObj.toString());
          }
          this.getUserNameAndInsFromStr(queryFields);
        } catch (Exception e) {
          logger.error("从人员检索输入中提取人员姓名与机构名称出错: ", e);
        }
        mapData = solrIndexService.queryPersons(pageNo, pageSize, queryFields);
        // 拼接个人必要信息 人员短地址 人员头像 人员成果数
        rebuildPsnInfo(mapData);
      } else if ("patent".equals(paramet.get("dataType"))) {
        mapData = solrIndexService.queryPatents(pageNo, pageSize, queryFields);
        buildExtraInfo(mapData, "patId");
      }
    } catch (Exception e) {
      logger.error("检索数据异常!", e);
    }
    dataList.add(mapData);
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

  /**
   * @param paramet
   */
  private List<Integer> parsePubType(Map<String, Object> paramet) {
    List<Integer> pubTypeList = new ArrayList<Integer>();
    Object pubTypeObj = paramet.get("pubType");
    if (pubTypeObj != null && StringUtils.isNotBlank(pubTypeObj.toString())) {
      String[] split = pubTypeObj.toString().split(",");
      if (split != null && split.length > 0) {
        for (int i = 0; i < split.length; i++) {
          if (split[i] != null && NumberUtils.isNumber(split[i].trim())) {
            pubTypeList.add(Integer.parseInt(split[i].trim()));
          }
        }
      }
    }
    return pubTypeList;
  }

  /**
   * @param paramet
   */
  private List<Long> parseExcludePubId(Map<String, Object> paramet) {
    List<Long> pubTypeList = new ArrayList<Long>();
    Object pubTypeObj = paramet.get("pubIds");
    if (pubTypeObj != null && StringUtils.isNotBlank(pubTypeObj.toString())) {
      String[] split = pubTypeObj.toString().split(",");
      if (split != null && split.length > 0) {
        for (int i = 0; i < split.length; i++) {
          if (split[i] != null && NumberUtils.isNumber(split[i].trim())) {
            pubTypeList.add(Long.parseLong(split[i].trim()));
          }
        }
      }
    }
    return pubTypeList;
  }

  /**
   * 重新构建人员数据
   */
  private void rebuildPsnInfo(Map<String, Object> mapData) throws Exception {
    String items = (String) mapData.get(SolrIndexSerivceImpl.RESULT_ITEMS);
    List<Map<String, Object>> listItems = JacksonUtils.jsonToList(items);
    if (listItems != null && listItems.size() > 0) {
      for (int i = 0; i < listItems.size(); i++) {
        Map<String, Object> item = listItems.get(i);
        Long psnId = NumberUtils.toLong(ObjectUtils.toString(item.get("psnId")));
        // 取人员细信息 来拼接显示
        Person psn = personProfileDao.getAvatars(psnId);
        if (psn != null) {
          item.put("psnId", psnId);
          item.put("avatars", psn.getAvatars());
          PsnProfileUrl psnUrl = psnProfileUrlDao.find(psnId);
          if (psnUrl != null && psnUrl.getPsnIndexUrl() != null) {
            item.put("psnUrl", scmDomain + "/P/" + psnUrl.getPsnIndexUrl());
          }
          PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
          if (psnStatistics != null) {
            item.put("prjSum", psnStatistics.getPrjSum());
            item.put("pubSum", psnStatistics.getPubSum());
          }

        }
      }
      String newItems = JacksonUtils.listToJsonStr(listItems);
      mapData.put(SolrIndexSerivceImpl.RESULT_ITEMS, newItems);
    }
  }

  /**
   * 构建额外的返回数据
   * 
   * @param map
   */
  private void buildExtraInfo(Map<String, Object> map, String source) {
    if (map == null || map.get("items") == null) {
      return;
    }
    List<Map<String, Object>> items = JacksonUtils.jsonToList(map.get("items").toString());
    List<Map<String, Object>> itemsResult = new ArrayList<Map<String, Object>>();
    if (items == null || items.size() < 1) {
      return;
    }
    for (int i = 0; i < items.size(); i++) {
      addExtraPubInfo(items.get(i), source);
      dealErrorPubShortUrl(items, i);
      itemsResult.add(items.get(i));
    }
    map.remove("items");
    map.put("items", JacksonUtils.listToJsonStr(itemsResult));
  }

  /**
   * 处理错误的 成果短地址
   * 
   * @param items
   * @param i
   */
  private void dealErrorPubShortUrl(List<Map<String, Object>> items, int i) {
    // pubShortUrl=http://dev.scholarmate.com/S/VnmEJ3, fundInfo=,
    // fullText=1, journalGrade=9, journalName
    Object pubShortUrlObj = items.get(i).get("pubShortUrl");
    if (pubShortUrlObj != null && pubShortUrlObj.toString().startsWith("null/")) {
      String replace = pubShortUrlObj.toString().replace("null/", this.scmDomain + "/");
      items.get(i).put("pubShortUrl", replace);
    }
  }

  private void addExtraPubInfo(Map<String, Object> dataMap, String source) {
    Object pubIdObj = dataMap.get(source);
    if ("patId".equalsIgnoreCase(source)) {// 专利添加返回类型
      dataMap.put("pubTypeId", "5");
    } else {
      Object pubTypeIdObj = dataMap.get("pubTypeId");
      dataMap.put("pubTypeId", pubTypeIdObj == null ? "" : pubTypeIdObj.toString());
    }
    if (pubIdObj == null || !NumberUtils.isNumber(pubIdObj.toString())) {
      return;
    }

    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    Map<String, Object> parmaMap = new HashMap<>();
    parmaMap.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubIdObj.toString()));
    parmaMap.put("serviceType", V8pubQueryPubConst.OPEN_PDWH_PUB);
    Map<String, Object> pubInfo = (Map<String, Object>) getRemotePubInfo(parmaMap, SERVER_URL);
    PubDetailVO pubDetailVO = null;
    try {
      if (pubInfo != null) {
        pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfo));
      }
    } catch (Exception e1) {
      logger.error("解析成果星期信息异常", e1);
    }
    if (pubDetailVO == null) {
      return;
    }
    Long pubId = NumberUtils.toLong(pubIdObj.toString());
    dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(pubDetailVO.getPublishDate()));
    dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(pubDetailVO.getPublishDate()));
    dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(pubDetailVO.getPublishDate()));

    int pub_Type = pubDetailVO.getPubType();
    Long ownPsnId = 0L;// 不是自己的成果
    PubDetailVoUtil.fillIsOwnerByPubDetailVO(dataMap, pubDetailVO); // 填充 作者
    // 和
    // owner
    PubTypeInfoDTO pubTypeInfo = pubDetailVO.getPubTypeInfo();
    switch (pub_Type) {
      case 1:
        // 奖励
        buildAward(dataMap, (AwardsInfoDTO) pubTypeInfo);
        break;
      case 2:
        // 书 著作
        buildBookChapter(dataMap, (BookInfoDTO) pubTypeInfo);
        break;
      case 3:
        // 会议
        buildConf(dataMap, (ConferencePaperDTO) pubTypeInfo);
        buildOther(dataMap, pubDetailVO);
        break;
      case 4:
        // 期刊
        buildJournal(dataMap, (JournalInfoDTO) pubTypeInfo);
        break;
      case 5:
        // 专利
        buildPatent(dataMap, (PatentInfoDTO) pubTypeInfo);
        break;
      case 10:
        // 书籍章节
        buildBookChapter(dataMap, (BookInfoDTO) pubTypeInfo);
        break;
      default:// 其他
        ;
        break;
    }

  }

  private void buildBookChapter(Map<String, Object> dataMap, BookInfoDTO bookInfo) {
    // 出版社 ，总字数，发表日期publish_date
    dataMap.put("pub_house", bookInfo.getPublisher());
    dataMap.put("t_word", bookInfo.getTotalWords());
  }

  private void buildPatent(Map<String, Object> dataMap, PatentInfoDTO patentInfo) {
    // 专利名称，专利状态，申请/授权日期，专利号
    // 专利状态
    String patentStatus = "";
    if (patentInfo.getStatus() == 1) {
      patentStatus = "授权";
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getStartDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getStartDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getStartDate()));
    } else if (patentInfo.getStatus() == 0) {
      patentStatus = "申请";
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getApplicationDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getApplicationDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getApplicationDate()));
    }
    dataMap.put("patent_status", patentStatus);
    dataMap.put("patent_num", patentInfo.getApplicationNo());
  }

  private void buildJournal(Map<String, Object> dataMap, JournalInfoDTO journalInfo) {
    // 期刊名称 ，卷号，期号，起始页码，结束页码，发表日期
    dataMap.put("journal_name", journalInfo.getName());
    dataMap.put("volume", journalInfo.getVolumeNo()); // 卷号
    dataMap.put("issue", journalInfo.getIssue()); // 期号
    dataMap.put("begin_num", journalInfo.getPageNumber());
  }

  private void buildAward(Map<String, Object> dataMap, AwardsInfoDTO awardsInfo) {
    // 获奖机构 ， 获奖类别 ， 获奖等级 ，颁奖年份 prize_org award_type_name award_grade_name
    // publish_date
    dataMap.put("award_type_name", awardsInfo.getCategory());
    dataMap.put("award_grade_name", awardsInfo.getGrade());
    dataMap.put("prize_org", awardsInfo.getIssuingAuthority());
  }

  // 已处理
  private void buildOther(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {
    dataMap.put("country_name", pubDetailVO.getCountryName());
    dataMap.put("city", pubDetailVO.getCityName());
  }

  private void buildConf(Map<String, Object> dataMap, ConferencePaperDTO conferencePaper) {
    // 会议名称 ， 会议地址 ， 会议开始时间，会议结束时间
    dataMap.put("conf_name", conferencePaper.getName());
    dataMap.put("conf_start_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getStartDate()));
    dataMap.put("conf_start_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getStartDate()));
    dataMap.put("conf_start_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getStartDate()));
    dataMap.put("conf_end_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getEndDate()));
    dataMap.put("conf_end_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getEndDate()));
    dataMap.put("conf_end_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getEndDate()));
  }

  /**
   * @param arrDate
   */
  private void parsePatentPublishDate(String publishDateXml, String[] arrDate) {

    Date parseDate = DateUtils.parseStringToDate(publishDateXml);
    if (parseDate != null) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(parseDate);
      switch (publishDateXml.length()) {
        case 4:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = "";
          arrDate[2] = "";
          break;
        case 7:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = instance.get(Calendar.MONTH) + 1 + "";
          arrDate[2] = "";
          break;
        default:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = instance.get(Calendar.MONTH) + 1 + "";
          arrDate[2] = instance.get(Calendar.DAY_OF_MONTH) + "";
          break;
      }
    } else {
      arrDate[0] = "";
      arrDate[1] = "";
      arrDate[2] = "";
    }
  }

  /**
   * @param pubXmlDoc
   * @param arrDate
   */
  private void parsePublishDate(PubXmlDocument pubXmlDoc, String[] arrDate) {
    String publishDateXml = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "pubyear");
    if (StringUtils.isBlank(publishDateXml)) {
      publishDateXml = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date");
    }

    Date parseDate = DateUtils.parseStringToDate(publishDateXml);
    if (parseDate != null) {
      Calendar instance = Calendar.getInstance();
      instance.setTime(parseDate);
      switch (publishDateXml.length()) {
        case 4:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = "";
          arrDate[2] = "";
          break;
        case 7:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = instance.get(Calendar.MONTH) + 1 + "";
          arrDate[2] = "";
          break;
        default:
          arrDate[0] = instance.get(Calendar.YEAR) + "";
          arrDate[1] = instance.get(Calendar.MONTH) + 1 + "";
          arrDate[2] = instance.get(Calendar.DAY_OF_MONTH) + "";
          break;
      }
    } else {
      arrDate[0] = "";
      arrDate[1] = "";
      arrDate[2] = "";
    }
  }

  private void getUserNameAndInsFromStr(QueryFields queryFields) throws Exception {
    String search = queryFields.getSearchString();
    if (StringUtils.isEmpty(search)) {
      return;
    }
    Map<String, Set<String>> mp = this.getExtractUserAndInsName(search);
    queryFields.setInsNames(mp.get("scm_ins_name"));
    queryFields.setUseNames(mp.get("scm_user_name"));
  }

  private Map<String, Set<String>> getExtractUserAndInsName(String str) throws Exception {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    str = str.replaceAll(";|,|，|；", " ");
    str = str.toLowerCase().trim().replaceAll("\\s+", "空格");
    // 直接使用，在服务器启动时加载
    Result kwRs = DicAnalysis.parse(str);
    Set<String> name = new TreeSet<String>();
    Set<String> ins = new TreeSet<String>();
    Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      if ("scm_user_name".equals(t.getNatureStr()) || "nr".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          name.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
      if ("scm_ins_name".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          ins.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
    }
    if (name.size() > 0) {
      mp.put("scm_user_name", name);
    }
    if (ins.size() > 0) {
      mp.put("scm_ins_name", ins);
    }
    return mp;
  }

}
