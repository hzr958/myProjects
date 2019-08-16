package com.smate.center.open.service.data.pub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.interconnection.log.InterconnectionGetPubLogService;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.v8pub.dto.AwardsInfoDTO;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.dto.SoftwareCopyrightDTO;
import com.smate.web.v8pub.dto.StandardInfoDTO;

/**
 * 2017-10-25 .根据人名+邮件 或者 psnid 拉取个人成果编目信息(按时间戳 取增量信息)
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class V_GetShowPubInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Autowired
  private InterconnectionGetPubLogService interconnectionGetPubLogService;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Value("${domainscm}")
  public String domainscm;

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
    // openId 转化的psnid
    Object psnId = paramet.get("psnId");
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

    if (psnId != null && StringUtils.isNotBlank(psnId.toString())) {
      if (!NumberUtils.isNumber(psnId.toString())) {
        logger.error("具体服务类型参数psnId必须为数字");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_292, paramet, "scm-292 具体服务类型参数  data参数中psnId格式不正确");
        return temp;
      }
    } else {
      // 通过 ：人名+email 构建用户的psnId 或者psnId
      buildPsnIdByEmailAndName(serviceData);
    }

    Object openIdObj = paramet.get("openid");
    // 防止传递的psnId覆盖了openId产生的psnId
    if (!"99999999".equals(openIdObj.toString())) {
      serviceData.remove("psnId");
    }
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  // 通过 ：人名+email 构建用户的psnId
  private void buildPsnIdByEmailAndName(Map<String, Object> serviceData) {

    Object openId = serviceData.get("openId");
    if (openId != null && NumberUtils.isDigits(openId.toString())) {
      OpenUserUnion union = openUserUnionDao.getOpenUserUnionByOpenId(NumberUtils.toLong(openId.toString()));
      if (union != null) {
        serviceData.put("psnId", union.getPsnId().toString());
        serviceData.remove("openId");
        return;
      }
    }
    Object psnIdObj = serviceData.get("psnId");
    if (psnIdObj != null && NumberUtils.isNumber(psnIdObj.toString())) {
      return;
    } else {
      // 防止psnId为空
      serviceData.put("psnId", 0L);
    }
    Object emailObj = serviceData.get("email");
    Object userNameObj = serviceData.get("userName");
    String email = "";
    String userName = "";
    if (emailObj != null) {
      email = StringUtils.trim(emailObj.toString());
    }
    if (userNameObj != null) {
      userName = StringUtils.trim(userNameObj.toString());
    }
    if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(userName)) {
      try {

        List<Long> psnIdList = personProfileDao.findPsnIdByEmailAndUsername(email, userName);
        if (psnIdList != null && psnIdList.size() > 0) {
          serviceData.put("psnIdList", psnIdList);
        } else {
          serviceData.put("psnIdList", null);
        }
      } catch (Exception e) {
        logger.error("通过名字+email获取psnid异常：email=" + email + " userName=" + userName);
        serviceData.put("psnIdList", null);
      }
    } else {
      serviceData.put("psnIdList", null);
    }
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Date getPubDate = DateUtils
        .parseStringToDate(paramet.get("lastGetPubDate") != null ? paramet.get("lastGetPubDate").toString() : "");
    Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());
    Long openid = NumberUtils.toLong(paramet.get("openid").toString());
    List<Long> excludePubIds = null;
    Object pubIdsObj = paramet.get("pubIds");
    excludePubIds = buildExcludePubIdList(pubIdsObj);
    List<Long> psnIdList = new ArrayList<Long>();
    if (psnId != 0L) {
      psnIdList.add(psnId);
    } else if (paramet.get("psnIdList") != null) {
      psnIdList = (List<Long>) paramet.get("psnIdList");
    } else {
      psnIdList.add(0L);
      // 没有找到psnId
      return super.errorMap(OpenMsgCodeConsts.SCM_277, paramet, "未查询到成果");
    }
    // 通过openId获取的psnId时，ownPsnId=psnId
    Long ownPsnId = 0L;
    if (openid.longValue() != OpenConsts.SYSTEM_OPENID.longValue()) {
      ownPsnId = psnId;
    }

    Integer pageNo = NumberUtils.toInt(paramet.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(paramet.get("pageSize").toString());
    ArrayList<Integer> pubTypeList = new ArrayList<Integer>();
    buildPubType(paramet, pubTypeList);

    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnIdList(psnIdList);
    pubQueryDTO.setSearchPsnId(psnIdList.get(0));
    pubQueryDTO.setExcludePsnIdList(excludePubIds);
    pubQueryDTO.setPubType(paramet.get("pubType").toString());
    if (ownPsnId == null || ownPsnId == 0L) {
      pubQueryDTO.setOpenPub(true);// 查询公开成果
      pubQueryDTO.setQueryAll(false); // 2019-04-25 ajb
    }
    pubQueryDTO.setPageNo(pageNo);
    pubQueryDTO.setPageSize(pageSize);
    if (getPubDate != null) {
      pubQueryDTO.setPubUpdateDate(getPubDate);
    }
    pubQueryDTO.setServiceType(V8pubQueryPubConst.ISIS_PUB_LIST);
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    Long totalCount = 0L;
    if (result.get("status").equals("success")) {
      totalCount = Long.parseLong(result.get("totalCount").toString());
      if (totalCount == null || totalCount < 1) {
        // 没有成果
        if (getPubDate != null) {
          return super.errorMap(OpenMsgCodeConsts.SCM_276, paramet, "没有成果被更新");
        }
        return super.errorMap(OpenMsgCodeConsts.SCM_277, paramet, "未查询到成果");
      }
    } else {
      logger.error("v8pub未查询到成果errorMsg=" + result);
      return super.errorMap(OpenMsgCodeConsts.SCM_277, paramet, "未查询到成果");
    }
    Long totalPages = 0L;
    if (totalCount % pageSize == 0) {
      totalPages = totalCount / pageSize;
    } else {
      totalPages = totalCount / pageSize + 1;
    }
    Map<String, Object> temp1 = new HashMap<String, Object>();
    temp1.put("totalPages", totalPages);
    temp1.put("count", totalCount);
    temp1.put("currentGetPubDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    dataList.add(temp1);
    List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("pubDetailVOList");
    // 删除的成果要有特别的标记
    if (resultList != null && resultList.size() > 0) {
      for (Map<String, Object> pubMap : resultList) {
        try {
          Map<String, Object> dataMap = new HashMap<String, Object>();
          if (pubMap == null || pubMap.get("pubId") == null) {
            continue;
          }
          if (pubMap.get("status") != null && Integer.parseInt(pubMap.get("status").toString()) == 1) {
            dataMap.put("status", "1");
            dataMap.put("pub_id", pubMap.get("pubId"));
          } else {
            PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubMap));
            dataMap = parseXmlToMap(pubDetailVO);
          }
          dataList.add(dataMap);
        } catch (Exception e) {
          logger.error("构建成果map数据出错 pubId=" + pubMap.get("pubId"), e);
        }
      }
      try {
        interconnectionGetPubLogService.saveGetPubLog(1, resultList.size(), paramet);
      } catch (Exception e) {
      }

    }

    return successMap(OpenMsgCodeConsts.SCM_000, PubDetailVoUtil.unescapeList(dataList));
  }

  /**
   * 构建排除的成果id
   * 
   * @param pubIdsObj
   */
  private List<Long> buildExcludePubIdList(Object pubIdsObj) {
    List<Long> excludePubIds = null;
    if (pubIdsObj != null && StringUtils.isNotBlank(pubIdsObj.toString())) {
      String pubIds = pubIdsObj.toString();
      String[] splitPubIds = pubIds.split(",");
      excludePubIds = new ArrayList<>();
      for (String pubIdStr : splitPubIds) {
        long pubId = NumberUtils.toLong(pubIdStr.trim());
        excludePubIds.add(pubId);
      }
      if (excludePubIds.size() > 1000) {
        excludePubIds.subList(0, 1000);
      }
    }
    return excludePubIds;
  }

  /**
   * 第三方系统 获取显示编目信息接口 (只取显示用字段 )
   * 
   * @author tsz
   * 
   *         pub_id pub_type_id authors zh_title en_title pub_owner_openid has_full_text list_info
   *         zh_source en_source publish_year authenticated create_date full_text_img_url
   *         pub_detail_param prodect_mark pub_update_time zh_keywords,en_keywords , doi
   */
  private Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();
      dataMap.put("pub_id", pubId.toString());

      Integer pubType = pubDetailVO.getPubType();
      Long pdwhPubId = pubPdwhSnsRelationDao.getPdwhPubIdBySnsPubId(pubId);
      dataMap.put("pdwh_pub_id", pdwhPubId == null ? "" : pdwhPubId);

      dataMap.put("update_mark", pubDetailVO.getUpdateMark() != null ? pubDetailVO.getUpdateMark() : "");
      dataMap.put("cited_times", pubDetailVO.getCitations() == null ? 0 : pubDetailVO.getCitations());
      dataMap.put("pub_type_id", pubType);
      dataMap.put("authors", XmlUtil.subStr500char(pubDetailVO.getAuthorNames()));
      dataMap.put("zh_title", StringUtils.substring(pubDetailVO.getTitle(), 0, 1000));
      dataMap.put("en_title", StringUtils.substring(pubDetailVO.getTitle(), 0, 1000));
      // 关键词
      dataMap.put("zh_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("en_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("doi", pubDetailVO.getDoi());
      dataMap.put("doi_url",
          StringUtils.isNotBlank(pubDetailVO.getDoi()) ? "http://dx.doi.org/" + pubDetailVO.getDoi() : "");
      // 通过psnid 查openId 没有openid要生成openId
      Long openId = openUserUnionDao.getOpenIdByPsnId(pubDetailVO.getPubOwnerPsnId());
      dataMap.put("pub_owner_openid", openId == null ? "" : openId);
      // list_info
      PubDetailVoUtil.fillListInfo(pubDetailVO, dataMap);
      PubDetailVoUtil.fillIsOwnerByPubDetailVO(dataMap, pubDetailVO); // 填充
      // 作者
      // 和
      // owner
      dataMap.put("zh_source", StringUtils.substring(pubDetailVO.getBriefDesc(),
          pubDetailVO.getBriefDesc().length() - 500, pubDetailVO.getBriefDesc().length()));
      dataMap.put("en_source", StringUtils.substring(pubDetailVO.getBriefDesc(),
          pubDetailVO.getBriefDesc().length() - 500, pubDetailVO.getBriefDesc().length()));
      dataMap.put("publish_year", pubDetailVO.getPublishYear() != null ? pubDetailVO.getPublishYear() : "");
      dataMap.put("publish_month", pubDetailVO.getPublishMonth() != null ? pubDetailVO.getPublishMonth() : "");
      dataMap.put("publish_day", pubDetailVO.getPublishDay() != null ? pubDetailVO.getPublishDay() : "");
      PubDetailVoUtil.isOrNotAuthenticated(dataMap, pubDetailVO);

      if (pubDetailVO.getFullText() != null) {
        dataMap.put("has_full_text", "1");
        dataMap.put("full_text_img_url", pubDetailVO.getFullText().getThumbnailPath());
      } else {
        dataMap.put("has_full_text", "0");
        dataMap.put("full_text_img_url", "");
      }
      int random = (int) (Math.random() * 10000);
      String pub_detail_param = ServiceUtil.encodeToDes3(String.valueOf(random) + "|" + pubId.toString());
      dataMap.put("pub_detail_param", pub_detail_param);
      dataMap.put("product_mark", pubDetailVO.getFundInfo());
      PubDetailVoUtil.buildPubUpdateTime(pubDetailVO, dataMap);
      PubDetailVoUtil.fillCreateTime(pubDetailVO, dataMap);
      dataMap.put("pubShortUrl", pubDetailVO.getPubIndexUrl());
      // 默认的，集体的在下面具体的类型中填写
      String language = "";
      dataMap.put("language", language);
      PubTypeInfoDTO pubTypeInfo = pubDetailVO.getPubTypeInfo();
      switch (pubType) {
        case 1:
          // 奖励
          buildAward(dataMap, (AwardsInfoDTO) pubTypeInfo);

          break;
        case 2:
          // 书 著作
          buildBookChapter(dataMap, (BookInfoDTO) pubTypeInfo);
          dataMap.put("publish_date", pubDetailVO.getPublishDate());// 发表日期
          break;
        case 3:
          // 会议
          buildConf(dataMap, (ConferencePaperDTO) pubTypeInfo);
          buildOther(dataMap, pubDetailVO);
          break;
        case 4:
          // 期刊
          buildJournal(dataMap, (JournalInfoDTO) pubTypeInfo);
          dataMap.put("publish_date", pubDetailVO.getPublishDate());// 发表日期
          break;
        case 5:
          // 专利
          buildPatent(dataMap, (PatentInfoDTO) pubTypeInfo);
          break;
        case 10:
          // 书籍章节
          buildBookChapter(dataMap, (BookInfoDTO) pubTypeInfo);
          break;
        case 12:
          // 标准
          buildStandardInfo(dataMap, (StandardInfoDTO) pubTypeInfo);
          break;
        case 13:
          // 软件著作权
          buildSoftwareCopyright(dataMap, (SoftwareCopyrightDTO) pubTypeInfo);
          break;
        default:// 其他
          ;
          break;
      }
      // 国家 城市
      buildOther(dataMap, pubDetailVO);
    }
    return dataMap;
  }

  private void buildOther(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {
    String country_name = pubDetailVO.getCountryName();
    dataMap.put("country_name", country_name);
    String city = pubDetailVO.getCityName();
    dataMap.put("city", city);
  }

  private void buildBookChapter(Map<String, Object> dataMap, BookInfoDTO bookInfo) {
    // 出版社 ，总字数，发表日期
    if (bookInfo == null) {
      return;
    }
    dataMap.put("pub_house", bookInfo.getPublisher());
    dataMap.put("t_word", bookInfo.getTotalWords());
    dataMap.put("language", StringUtils.isNotBlank(bookInfo.getLanguage()) ? bookInfo.getLanguage() : "");

  }

  private void buildStandardInfo(Map<String, Object> dataMap, StandardInfoDTO info) {
    if (info == null) {
      return;
    }
    // 标准号 公布机构 归口单位 标准类型
    dataMap.put("standard_no", com.smate.core.base.utils.string.StringUtils.defaultString(info.getStandardNo(), ""));
    dataMap.put("publish_agency", com.smate.core.base.utils.string.StringUtils.defaultString(info.getPublishAgency()));
    dataMap.put("technical_committees",
        com.smate.core.base.utils.string.StringUtils.defaultString(info.getTechnicalCommittees()));
    dataMap.put("standard_type", info.getType() == null ? "" : info.getType().getValue());

  }

  private void buildSoftwareCopyright(Map<String, Object> dataMap, SoftwareCopyrightDTO info) {
    if (info == null) {
      return;
    }
    // 登记号 权利获得方式 权利范围
    dataMap.put("register_no", com.smate.core.base.utils.string.StringUtils.defaultString(info.getRegisterNo(), ""));
    dataMap.put("sc_acquisition_type", info.getAcquisitionType() == null ? "" : info.getAcquisitionType().getValue());
    dataMap.put("sc_scope_type", info.getScopeType() == null ? "" : info.getScopeType().getValue());

  }

  private void buildPatent(Map<String, Object> dataMap, PatentInfoDTO patentInfo) {
    // 专利名称，专利状态，申请/授权日期，专利号
    // 专利名称 为title
    // <!-- 专利状态：0-申请,1-授权 -->
    if (patentInfo == null) {
      return;
    }
    String patentStatus = "";
    if (patentInfo.getStatus() != null && patentInfo.getStatus() == 1) {
      patentStatus = "授权";
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getStartDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getStartDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getStartDate()));
    } else if (patentInfo.getStatus() != null && patentInfo.getStatus() == 0) {
      patentStatus = "申请";
      // 申请状态下， 个人库中 下面字段没有数据
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getApplicationDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getApplicationDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getApplicationDate()));
    }
    dataMap.put("patent_status", patentStatus);
    dataMap.put("patent_num", patentInfo.getApplicationNo());
    dataMap.put("patent_name", patentInfo.getArea().getValue());
    dataMap.put("cpc_num", patentInfo.getCPC());
    dataMap.put("ch_patent_type", PubParamUtils.buildPatentTypeDesc(patentInfo.getType()));
  }

  private void buildJournal(Map<String, Object> dataMap, JournalInfoDTO journalInfo) {
    // 期刊名称 ，卷号，期号，起始页码，结束页码，发表日期
    if (journalInfo == null) {
      return;
    }
    dataMap.put("journal_name", journalInfo.getName());
    dataMap.put("volume", journalInfo.getVolumeNo()); // 卷号
    dataMap.put("issue", journalInfo.getIssue()); // 期号
    dataMap.put("begin_num", journalInfo.getPageNumber());

  }

  private void buildAward(Map<String, Object> dataMap, AwardsInfoDTO awardsInfo) {
    // 获奖机构 ， 获奖类别 ， 获奖等级 ，颁奖年份 prize_org award_type_name award_grade_name
    if (awardsInfo == null) {
      return;
    }
    dataMap.put("award_type_name", awardsInfo.getCategory());
    dataMap.put("award_grade_name", awardsInfo.getGrade());
    dataMap.put("prize_org", awardsInfo.getIssuingAuthority());
    // 证书编号
    dataMap.put("zs_number", awardsInfo.getCertificateNo());

    dataMap.put("publish_date", awardsInfo.getAwardDate());
    dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(awardsInfo.getAwardDate()));
    dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(awardsInfo.getAwardDate()));
    dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(awardsInfo.getAwardDate()));

  }

  private void buildConf(Map<String, Object> dataMap, ConferencePaperDTO conferencePaper) {
    // 会议名称 ， 会议地址 ， 会议开始时间，会议结束时间
    if (conferencePaper == null) {
      return;
    }
    dataMap.put("conf_name", conferencePaper.getName());
    dataMap.put("conf_start_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getStartDate()));
    dataMap.put("conf_start_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getStartDate()));
    dataMap.put("conf_start_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getStartDate()));
    dataMap.put("conf_end_year", PubDetailVoUtil.parseDateForYear(conferencePaper.getEndDate()));
    dataMap.put("conf_end_month", PubDetailVoUtil.parseDateForMonth(conferencePaper.getEndDate()));
    dataMap.put("conf_end_day", PubDetailVoUtil.parseDateForDay(conferencePaper.getEndDate()));
  }

  /**
   * 兼容 成果Type
   * 
   * @param serviceData
   * @param pubTypeList
   */
  private void buildPubType(Map<String, Object> serviceData, List<Integer> pubTypeList) {
    String type = "";
    Object pubTypeObj = serviceData.get("pubType");
    if (pubTypeObj != null && StringUtils.isNotBlank(pubTypeObj.toString())) {
      String[] pubTypeArr = pubTypeObj.toString().split(",");
      for (int i = 0; i < pubTypeArr.length; i++) {
        String pubType = StringUtils.trimToEmpty(pubTypeArr[i]);
        if (StringUtils.isNotBlank(pubType) && NumberUtils.isNumber(pubType)) {
          type += pubType + ",";
        }

      }
      if (StringUtils.isNotBlank(type)) {
        type = type.substring(0, type.length() - 1);
      }
    }
    serviceData.put("pubType", type);
  }
}
