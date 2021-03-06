package com.smate.center.open.service.data.pub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.interconnection.log.InterconnectionGetPubLogService;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
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
 * @Date 2017-10-23 获取群组成果 显示编目信息 接口
 *
 * @author tsz
 *
 *         group_code pub_id pub_type_id authors zh_title en_title has_full_text list_info zh_source
 *         en_source publish_year authenticated create_date full_text_img_url pub_detail_param
 *         produce_mark relevance labeled
 */
@Transactional(rollbackFor = Exception.class)
public class GetShowGroupPubInfoServiceImpl extends ThirdDataTypeBase {


  @Autowired
  private OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private InterconnectionGetPubLogService interconnectionGetPubLogService;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object groupCode = serviceData.get("groupCode");
    Object des3GrpId = serviceData.get("des3GrpId");
    Long psnId = Long.parseLong(paramet.get(OpenConsts.MAP_PSNID).toString());
    if (des3GrpId != null && StringUtils.isNotBlank(des3GrpId.toString())) {
      Long groupId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId.toString()));
      if (groupId == 0L || groupId == null) {
        logger.error("服务参数   des3GrpId无效");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_409, paramet, "scm-408  des3GrpId无效");
        return temp;
      }
      serviceData.put("groupId", groupId);

    } else if (groupCode != null) {
      Long groupId = openGroupUnionDao.findGroupIdByGroupCode(groupCode.toString(), psnId);
      if (groupId == null) {
        logger.error("服务参数   groupCode无效，或不属于当前人员的groupCode");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_408, paramet, "scm-408  groupCode无效，或不属于当前人员的groupCode");
        return temp;
      }
      serviceData.put("groupId", groupId);
    } else {
      logger.error("具体服务类型参数 groupCode不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_254, paramet, "具体服务类型参数 groupCode不能为空");
      return temp;
    }
    Object pageNo = serviceData.get("pageNo");
    Object pageSize = serviceData.get("pageSize");
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
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    Integer pageNo = NumberUtils.toInt(paramet.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(paramet.get("pageSize").toString());
    Long groupId = Long.parseLong(paramet.get("groupId").toString());
    Date getPubDate = DateUtils
        .parseStringToDate(paramet.get("lastGetPubDate") != null ? paramet.get("lastGetPubDate").toString() : "");

    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchGrpId(groupId);
    pubQueryDTO.setPageNo(pageNo);
    pubQueryDTO.setPageSize(pageSize);
    if (getPubDate != null) {
      pubQueryDTO.setPubUpdateDate(getPubDate);
    }
    pubQueryDTO.setServiceType(V8pubQueryPubConst.ISIS_GRP_PUB_LIST);
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    Long count = 0L;

    if (result.get("status").equals("success")) {
      count = Long.parseLong(result.get("totalCount").toString());
      if (count == null || count < 1) {
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
    if (count % pageSize == 0) {
      totalPages = count / pageSize;
    } else {
      totalPages = count / pageSize + 1;
    }
    Map<String, Object> temp1 = new HashMap<String, Object>();
    temp1.put("totalPages", totalPages);
    temp1.put("count", count);
    temp1.put("currentGetPubDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    dataList.add(temp1);
    List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("pubDetailVOList");
    if (resultList != null && resultList.size() > 0) {
      for (Map<String, Object> pubMap : resultList) {
        try {
          Map<String, Object> dataMap = new HashMap<String, Object>();
          if (pubMap == null || pubMap.get("pubId") == null) {
            continue;
          }
          if (pubMap.get("status").toString() == "1" || Integer.parseInt(pubMap.get("status").toString()) == 1) {
            dataMap.put("status", "1");
            dataMap.put("pub_id", pubMap.get("pubId"));
          } else {
            PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubMap));
            dataMap = parseXmlToMap(pubDetailVO);
            buildGroupPubUpdateTime(dataMap, pubDetailVO.getGmtModified());
            dataMap.put("status", "0");
            dataMap.put("relevance", pubDetailVO.getRelevance());
            dataMap.put("labeled", pubDetailVO.getLabeled());
            dataMap.put("pubShortUrl", pubDetailVO.getPubIndexUrl());
          }
          dataList.add(dataMap);
        } catch (Exception e) {
          logger.error("构建成果map数据出错 pubId=" + pubMap.get("pubId"), e);
        }
      }
      try {
        interconnectionGetPubLogService.saveGetPubLog(2, resultList.size(), paramet);
      } catch (Exception e) {

      }
    }
    return successMap(OpenMsgCodeConsts.SCM_000, PubDetailVoUtil.unescapeList(dataList));
  }

  private Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();
      dataMap.put("pub_id", pubId.toString());
      // 需要传递String类型 给互联互通使用-2018-09-25
      dataMap.put("pub_type_id", pubDetailVO.getPubType().toString());
      dataMap.put("authors", XmlUtil.subStr500char(pubDetailVO.getAuthorNames()));
      dataMap.put("zh_title", StringUtils.substring(pubDetailVO.getTitle(), 0, 1000));
      dataMap.put("en_title", StringUtils.substring(pubDetailVO.getTitle(), 0, 1000));
      dataMap.put("update_mark", pubDetailVO.getUpdateMark() != null ? pubDetailVO.getUpdateMark() : "");
      // 关键词
      dataMap.put("zh_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("en_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("cited_times", pubDetailVO.getCitations());
      // 通过psnid 查openId 没有openid要生成openId
      dataMap.put("pub_owner_openid", openUserUnionDao.getOpenIdByPsnId(pubDetailVO.getPubOwnerPsnId()));
      // list_info
      PubDetailVoUtil.fillListInfo(pubDetailVO, dataMap);
      dataMap.put("zh_source", StringUtils.substring(pubDetailVO.getBriefDesc(),
          pubDetailVO.getBriefDesc().length() - 500, pubDetailVO.getBriefDesc().length()));
      dataMap.put("en_source", StringUtils.substring(pubDetailVO.getBriefDesc(),
          pubDetailVO.getBriefDesc().length() - 500, pubDetailVO.getBriefDesc().length()));
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(pubDetailVO.getPublishDate()));

      PubDetailVoUtil.isOrNotAuthenticated(dataMap, pubDetailVO);
      PubDetailVoUtil.fillIsOwnerByPubDetailVO(dataMap, pubDetailVO); // 填充 作者 和 owner
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
      PubDetailVoUtil.fillCreateTime(pubDetailVO, dataMap);
      dataMap.put("publish_year", pubDetailVO.getPublishYear() != null ? pubDetailVO.getPublishYear() : "");
      dataMap.put("publish_month", pubDetailVO.getPublishMonth() != null ? pubDetailVO.getPublishMonth() : "");
      dataMap.put("publish_day", pubDetailVO.getPublishDay() != null ? pubDetailVO.getPublishDay() : "");
      Integer pubType = 7;
      if (dataMap.get("pub_type_id") != null && NumberUtils.isNumber(dataMap.get("pub_type_id").toString())) {
        pubType = Integer.parseInt(dataMap.get("pub_type_id").toString());
      }
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
          break;
        case 4:
          // 期刊
          buildJournal(dataMap, (JournalInfoDTO) pubTypeInfo);
          dataMap.put("publish_date", pubDetailVO.getPublishDate()); // 发表日期
          break;
        case 5:
          // 专利
          buildPatent(dataMap, (PatentInfoDTO) pubTypeInfo);
          break;
        case 10:
          // 书籍章节
          buildBookChapter(dataMap, (BookInfoDTO) pubTypeInfo);
          dataMap.put("publish_date", pubDetailVO.getPublishDate());// 发表日期
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


  /**
   * 获取成果更新时间
   *
   * @param dataMap
   * @param updateDate
   */
  private void buildGroupPubUpdateTime(Map<String, Object> dataMap, Date updateDate) {
    String updateDateStr = "";
    if (updateDate != null) {
      updateDateStr = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(updateDate);
    }
    dataMap.put("pub_update_time", updateDateStr);
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
    // 证书编号
    dataMap.put("zs_number", awardsInfo.getCertificateNo());
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



}
