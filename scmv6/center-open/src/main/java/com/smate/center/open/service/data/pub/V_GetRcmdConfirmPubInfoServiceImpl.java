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

/**
 * .根据人 拉取个人未认领成果编目信息(按时间戳 取增量信息)
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class V_GetRcmdConfirmPubInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private InterconnectionGetPubLogService interconnectionGetPubLogService;

  @Value("${domainscm}")
  public String domainscm;
  @Autowired
  private PersonProfileDao personProfileDao;

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
      // 通过 ：人名+email 构建用户的psnId
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
    List<Long> psnIdList = new ArrayList<Long>();
    if (psnId != 0L) {
      psnIdList.add(psnId);
    } else if (paramet.get("psnIdList") != null) {
      psnIdList = (List<Long>) paramet.get("psnIdList");
    } else {
      psnIdList.add(0L);
    }
    Integer pageNo = NumberUtils.toInt(paramet.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(paramet.get("pageSize").toString());
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnIdList(psnIdList);
    pubQueryDTO.setSearchPsnId(psnIdList.get(0));
    pubQueryDTO.setPageNo(pageNo);
    pubQueryDTO.setPageSize(pageSize);
    pubQueryDTO.setIsAll(1);
    if (getPubDate != null) {
      pubQueryDTO.setPubUpdateDate(getPubDate);
    }
    pubQueryDTO.setServiceType(V8pubQueryPubConst.ISIS_PUB_ASSIGN_LOG_LIST);
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
          String status = pubMap.get("status").toString();
          dataMap.put("pub_id", pubMap.get("pubConfirmId"));
          dataMap.put("pdwh_pub_id", pubMap.get("pubId"));
          if ("1".equals(status)) {
            dataMap.put("status", "1");// 已经确认
            // ，或者拒绝的成果直接返回，正常结果不存在该字段
          } else {
            PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubMap));
            Map<String, Object> resultMap = parseXmlToMap(pubDetailVO);
            dataMap.putAll(resultMap);
            dataMap.put("pubShortUrl", pubMap.get("pubIndexUrl"));
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

  private Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();
      Integer pubType = pubDetailVO.getPubType();
      dataMap.put("pub_type_id", pubType);
      dataMap.put("update_mark", pubDetailVO.getUpdateMark());
      dataMap.put("cited_times", pubDetailVO.getCitations() == null ? 0 : pubDetailVO.getCitations());
      // 截取与数据库保存成果相同长度
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
      dataMap.put("pub_owner_openid", openUserUnionDao.getOpenIdByPsnId(pubDetailVO.getPubOwnerPsnId()));
      // list_info
      PubDetailVoUtil.fillListInfo(pubDetailVO, dataMap);
      dataMap.put("zh_source", StringUtils.substring(pubDetailVO.getBriefDesc(),
          pubDetailVO.getBriefDesc().length() - 500, pubDetailVO.getBriefDesc().length()));
      dataMap.put("en_source", StringUtils.substring(pubDetailVO.getBriefDesc(),
          pubDetailVO.getBriefDesc().length() - 500, pubDetailVO.getBriefDesc().length()));
      dataMap.put("publish_year", pubDetailVO.getPublishYear() != null ? pubDetailVO.getPublishYear() : "");
      dataMap.put("publish_month", pubDetailVO.getPublishMonth() != null ? pubDetailVO.getPublishMonth() : "");
      dataMap.put("publish_day", pubDetailVO.getPublishDay() != null ? pubDetailVO.getPublishDay() : "");
      PubDetailVoUtil.isOrNotAuthenticated(dataMap, pubDetailVO);
      PubDetailVoUtil.fillIsOwnerByPubDetailVO(dataMap, pubDetailVO); // 填充作者和owner

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
      buildPubUpdateTime(dataMap, pubDetailVO.getGmtModified());
      PubDetailVoUtil.fillCreateTime(pubDetailVO, dataMap);
      PubTypeInfoDTO pubTypeInfo = pubDetailVO.getPubTypeInfo();
      switch (pubType) {
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
    dataMap.put("pub_house", bookInfo.getPublisher());
    dataMap.put("t_word", bookInfo.getTotalWords());

  }

  private void buildPatent(Map<String, Object> dataMap, PatentInfoDTO patentInfo) {
    // 专利名称，专利状态，申请/授权日期，专利号
    // 专利名称 为title
    // <!-- 专利状态：0-申请,1-授权 -->
    String patentStatus = "";
    if (patentInfo.getStatus() != null && patentInfo.getStatus() == 1) {
      patentStatus = "授权";
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(patentInfo.getStartDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(patentInfo.getStartDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(patentInfo.getStartDate()));
    } else if (patentInfo.getStatus() != null && patentInfo.getStatus() == 0) {
      patentStatus = "申请";
      // 申请状态下
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
    dataMap.put("award_type_name", awardsInfo.getCategory());
    dataMap.put("award_grade_name", awardsInfo.getGrade());
    dataMap.put("prize_org", awardsInfo.getIssuingAuthority());
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
   * 获取成果更新时间
   * 
   * @param dataMap
   * @param updateDate
   */
  private void buildPubUpdateTime(Map<String, Object> dataMap, Date updateDate) {
    String updateDateStr = "";
    if (updateDate != null) {
      updateDateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(updateDate);
    }
    dataMap.put("pub_update_time", updateDateStr);
  }



}
