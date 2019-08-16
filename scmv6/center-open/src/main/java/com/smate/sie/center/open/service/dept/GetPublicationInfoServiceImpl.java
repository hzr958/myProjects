package com.smate.sie.center.open.service.dept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenTokenServiceConstDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.role.Sie6InsPortalDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.sie.center.open.dao.dept.SiePublicationInfoDao;
import com.smate.sie.core.base.utils.dao.pub.Sie6PublicationListDao;
import com.smate.sie.core.base.utils.dao.pub.SiePubMemberDao;
import com.smate.sie.core.base.utils.model.pub.Sie6PublicationList;
import com.smate.sie.core.base.utils.model.pub.SiePubMember;
import com.smate.sie.core.base.utils.model.pub.SiePublication;
import com.smate.sie.core.base.utils.pub.dom.ConferencePaperBean;
import com.smate.sie.core.base.utils.pub.dom.JournalInfoBean;
import com.smate.sie.core.base.utils.pub.dom.MemberInsBean;
import com.smate.sie.core.base.utils.pub.dom.PubDetailDOM;
import com.smate.sie.core.base.utils.pub.dom.PubMemberBean;
import com.smate.sie.core.base.utils.pub.dto.MemberInsDTO;
import com.smate.sie.core.base.utils.pub.service.PubJsonPOService;

@SuppressWarnings("deprecation")
@Transactional(rollbackFor = Exception.class)
public class GetPublicationInfoServiceImpl extends ThirdDataTypeBase {

  @Autowired
  SiePublicationInfoDao siePublicationInfoDao;
  @Autowired
  Sie6InsPortalDao sieInsPortalDao;
  @Autowired
  private OpenTokenServiceConstDao openTokenServiceConstDao;
  @Autowired
  private PubJsonPOService pubJsonPOService;
  // @Autowired
  // PubXmlDao pubXmlDao;
  @Autowired
  private SiePubMemberDao siePubMemberDao;

  @Autowired
  private Sie6PublicationListDao siePublicationListDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || StringUtils.isEmpty(data.toString())) {
      logger.error("Open系统-接收接口-输入数据不能为空");
      temp = super.errorMap("Open系统-接收接口-输入数据不能为空", paramet, "Open系统-接收接口-输入数据不能为空");
      return temp;
    }

    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      logger.error(" 参数 格式不正确 不能转换成map");
      temp = super.errorMap(" 参数 格式不正确 不能转换成map", paramet, "");
      return temp;
    }
    if (dataMap.get("pubType") == null || !NumberUtils.isDigits(dataMap.get("pubType").toString())) {
      logger.error(" 参数 pubType 不能为空或非数字");
      temp = super.errorMap(" 参数 pubType 不能为空或非数字", paramet, "");
      return temp;
    }
    if (dataMap.get("pageNo") == null || !NumberUtils.isDigits(dataMap.get("pageNo").toString())) {
      logger.error(" 参数 pageNo 不能为空或非数字");
      temp = super.errorMap(" 参数 pageNo 不能为空或非数字", paramet, "");
      return temp;
    }
    if (dataMap.get("pageSize") == null || !NumberUtils.isDigits(dataMap.get("pageSize").toString())) {
      logger.error(" 参数 pageSize 不能为空或非数字");
      temp = super.errorMap(" 参数 pageSize 不能为空或非数字", paramet, "");
      return temp;
    }
    if (dataMap.get("publishYear") != null && !"".equals(dataMap.get("publishYear").toString().trim().replace(" ", ""))
        && !NumberUtils.isDigits(dataMap.get("publishYear").toString().trim().replace(" ", ""))) {
      logger.error(" 参数 publishYear 不能为非数字");
      temp = super.errorMap(" 参数 publishYear 不能为非数字", paramet, "");
      return temp;
    }
    // 校验pubids满足分割后的字符串时数字
    if (dataMap.get("pubids") != null && !"".equals(dataMap.get("pubids").toString())) {
      String tempArray[] = dataMap.get("pubids").toString().trim().split(",");
      for (int i = 0; i < tempArray.length; i++) {
        if (!NumberUtils.isDigits(tempArray[i])) {
          dataMap.get("pubids").toString().split(",");
          logger.error(" 参数 pubids 不能为非数字");
          temp = super.errorMap(" 参数 pubids 不能为非数字", paramet, "");
          return temp;
        }
      }
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(dataMap.get("orderType")))
        && (!"desc".equalsIgnoreCase(dataMap.get("orderType").toString()))
        && (!"asc".equals(dataMap.get("orderType").toString()))) {
      logger.error(" 参数 排序方式(orderType)错误，正确排序方式为'asc'或'desc'");
      temp = super.errorMap(" 参数  排序方式(orderType)错误，正确排序方式为'asc'或'desc'", paramet, "");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object mapData = paramet.get(OpenConsts.MAP_DATA);

    Map<String, Object> data = JacksonUtils.jsonToMap(mapData.toString());
    Page<SiePublication> page = new Page<SiePublication>();
    page.setPageNo(Integer.valueOf(data.get("pageNo").toString()));
    page.setPageSize(Integer.valueOf(data.get("pageSize").toString()));
    String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
    String serviceType = paramet.get(OpenConsts.MAP_TYPE).toString();
    OpenTokenServiceConst openTokenServiceConst =
        openTokenServiceConstDao.findObjBytokenAndServiceType(token, serviceType);
    if (openTokenServiceConst.getInsId() == null || openTokenServiceConst.getInsId() == 0) {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "单位id不能为空，请检查是否已配置单位id");
      return temp;
    }
    Long insId = openTokenServiceConst.getInsId();
    data.put("insId", insId);
    formatNumber(data);
    // 查询数据
    page = siePublicationInfoDao.getPubs(data, page);

    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("page_no", ObjectUtils.toString(page.getPageNo()));
    map.put("page_size", ObjectUtils.toString(page.getPageSize()));
    map.put("total_pages", ObjectUtils.toString(page.getTotalPages()));
    map.put("total_count", ObjectUtils.toString(page.getTotalCount()));
    dataList.add(map);
    if (!CollectionUtils.isEmpty(page.getResult())) {
      for (SiePublication pub : page.getResult()) {
        try {
          // 获取json字符串，并且构造成PubDetailDOM对象
          @SuppressWarnings("rawtypes")
          PubDetailDOM detail = pubJsonPOService.getDOMByIdAndType(pub.getPubId(), pub.getPubType());
          Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
          dataMap.put("pub_id", pub.getPubId());
          dataMap.put("pub_type", pub.getPubType());
          dataMap.put("title", ObjectUtils.toString(null != pub.getZhTitle() ? pub.getZhTitle() : pub.getEnTitle()));// 标题,无中文则返回英文
          dataMap.put("issue", ObjectUtils.toString(pub.getIssue()));// 期号
          dataMap.put("volume", ObjectUtils.toString(pub.getVolume()));// 卷号
          dataMap.put("start_page", ObjectUtils.toString(pub.getStartPage()));// 开始页码
          dataMap.put("end_page", ObjectUtils.toString(pub.getEndPage()));// 结束页码
          dataMap.put("doi", ObjectUtils.toString(pub.getDoi()));// doi
          dataMap.put("cited_list",
              ObjectUtils.toString(convertPubListToString(siePublicationListDao.get(pub.getPubId()))));// 收录情况
          dataMap.put("dis_code", "");// 学科代码
          dataMap.put("author_name", ObjectUtils.toString(pub.getAuthorNames()));// 论文作者
          dataMap.put("apply_time", getFormatDate(pub.getPublishYear(), pub.getPublishMonth(), pub.getPublishDay()));// 发表时间
          dataMap.put("publish_month", pub.getPublishMonth()); // 发表月份
          dataMap.put("publish_day", pub.getPublishDay()); // 发表日期
          if (pub.getPubType() == 4) {
            JournalInfoBean journal = (JournalInfoBean) detail.getTypeInfo();
            dataMap.put("journal_name", null != journal ? journal.getName() : "");// 期刊名称
            dataMap.put("issn", null != journal ? journal.getISSN() : "");// ISSN
            dataMap.put("article_no", null != journal ? journal.getArticleNo() : "");// 文章号
            dataMap.put("publish_state", null != journal ? journal.getPublishStatusName() : ""); // 发表状态
          } else {
            dataMap.put("journal_name", "");// 期刊名称
            dataMap.put("issn", "");// ISSN
            dataMap.put("article_no", "");
            dataMap.put("publish_state", ""); // 发表状态
          }

          dataMap.put("fundinfo", null != detail ? StringEscapeUtils.escapeHtml4(detail.getFundInfo()) : "");// 基金标注
          dataMap.put("summary", null != detail ? StringEscapeUtils.escapeHtml4(detail.getSummary()) : "");// 摘要
          dataMap.put("key_words", null != detail ? StringEscapeUtils.escapeHtml4(detail.getKeywords()) : "");// 关键字
          if (pub.getPubType() == 3) {// 非期刊论文 会议论文
            ConferencePaperBean conf = (ConferencePaperBean) detail.getTypeInfo();
            String meetingTitle = null != conf ? conf.getName() : "";
            String meetingOrganizer = null != conf ? conf.getOrganizer() : "";
            dataMap.put("meeting_title", StringEscapeUtils.escapeHtml4(meetingTitle));// 会议名称
            dataMap.put("meeting_organizers", meetingOrganizer);// 会议组织者
            /*
             * dataMap.put("meeting_time", getFormatDate(pub.getConfStartYear(), pub.getConfStartMonth(),
             * pub.getConfStartDay()));// 会议时间 dataMap.put("meeint_end_time",
             * getFormatDate(pub.getConfEndYear(), pub.getConfEndMonth(), pub.getConfEndDay()));
             */
            dataMap.put("meeting_time", conf.getStartDate());
            dataMap.put("meeint_end_time", conf.getEndDate());
            dataMap.put("meeting_address", getMeetingAddress(conf));// 会议地址
            dataMap.put("cn", "");// cn
            dataMap.put("accession_no", "");// 检索号
          }
          dataMap.put("cited_times", pub.getIsiCited()); // 引用数
          dataMap.put("impact_factors", "");// 影响因子

          dataMap.put("pub_members", getJsonAuthos2(detail)); // getJsonAuthos(pub.getPubId())
          dataMap.put("view_url", getPubAddress(pub.getPubId(), insId));
          dataList.add(dataMap);
        } catch (Exception e) {
          logger.error("分页获取成果数据，封装每条数据时出错，当前pubId:" + pub.getPubId());
          throw new OpenException(e);
        }
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

  private String getMeetingAddress(ConferencePaperBean conf) {
    StringBuilder sbl = new StringBuilder();
    if (conf != null) {
      sbl.append(conf.getCountry());
      String city = conf.getCity();
      if (!"".equals(city.trim())) {
        sbl.append(",");
        sbl.append(city);
      }
    }
    return sbl.toString();
  }

  /**
   * 获取成果作者信息 - 通过PubDetailDOM获取单位信息
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private Object getJsonAuthos2(PubDetailDOM dom) {
    List<Object> tempArr = new ArrayList<Object>();
    List<PubMemberBean> memberList = dom.getMembers();
    PubMemberBean member = null;
    Map<String, Object> tempObj = null;
    List<Map<String, String>> insList = null;
    Map<String, String> insName = null;
    for (int i = 0; i < memberList.size(); i++) {
      member = new PubMemberBean();
      member = memberList.get(i);
      boolean authorPos = member.isCommunicable(); // 是否是通讯作者
      tempObj = new HashMap<String, Object>();
      tempObj.put("seq_no", member.getSeqNo());
      tempObj.put("member_psn_name", member.getName());
      tempObj.put("author_pos", authorPos == false ? 0 : 1);
      tempObj.put("email", member.getEmail());
      List<MemberInsBean> insDetailList = member.getInsNames();
      if (CollectionUtils.isNotEmpty(insDetailList)) {
        insList = new ArrayList<Map<String, String>>();
        for (MemberInsBean ins : insDetailList) {
          insName = new HashMap<String, String>();
          insName.put("ins_name", ins.getInsName());
          insList.add(insName);
        }
        tempObj.put("ins_names", insList);
      } else {
        tempObj.put("ins_names", new ArrayList<Map<String, String>>());
      }
      tempArr.add(tempObj);
    }
    return tempArr;
  }

  /**
   * 获取成果作者信息
   */
  private Object getJsonAuthos(Long pubid) {
    List<Object> tempArr = new ArrayList<Object>();
    List<MemberInsDTO> insList = null;
    MemberInsDTO memIns = null;
    List<SiePubMember> siePubMemberList = siePubMemberDao.getMembersByPubId(pubid);
    for (SiePubMember siePubMember : siePubMemberList) {
      Integer authorPos = siePubMember.getAuthorPos();
      Map<String, Object> tempObj = new HashMap<String, Object>();
      tempObj.put("seq_no", siePubMember.getSeqNo());
      tempObj.put("member_psn_name", siePubMember.getMemberName());
      tempObj.put("author_pos", authorPos == null ? 0 : authorPos);
      tempObj.put("email", siePubMember.getEmail());
      if (StringUtils.isNotBlank(siePubMember.getInsName())) {
        memIns = new MemberInsDTO();
        memIns.setInsId(siePubMember.getInsId());
        memIns.setInsName(siePubMember.getInsName());
        insList = new ArrayList<MemberInsDTO>();
        insList.add(memIns);
        tempObj.put("ins_names", insList);
      } else {
        tempObj.put("ins_names", new ArrayList<MemberInsDTO>());
      }
      // tempObj.put("ins_name", siePubMember.getInsName());
      tempArr.add(tempObj);
    }
    return tempArr;
  }

  private Object getFormatDate(Integer Year, Integer Month, Integer Day) {
    StringBuilder sbl = new StringBuilder();
    if (null != Year) {
      sbl.append(Year);
      if (null != Month) {
        sbl.append("-");
        sbl.append(Month);
        if (null != Day) {
          sbl.append("-");
          sbl.append(Day);
        }
      }
    }
    return sbl.toString();
  }

  private void formatNumber(Map<String, Object> data) {
    List<Long> tempNumArray = new ArrayList<Long>();
    if (data.get("pubids") != null && !"".equals(data.get("pubids").toString())) {
      String tempArray[] = data.get("pubids").toString().trim().split(",");
      for (int i = 0; i < tempArray.length; i++) {
        tempNumArray.add(Long.valueOf(tempArray[i]));
      }
      data.replace("pubids", tempNumArray);
    } else {
      data.put("pubids", tempNumArray);
    }
  }

  private Object getPubAddress(Long pubId, Long insId) {
    StringBuilder sbl = new StringBuilder();
    sbl.append("https://");
    sbl.append(sieInsPortalDao.get(insId).getDomain());
    sbl.append("/pubweb/publication/view?des3Id=");
    sbl.append(ServiceUtil.encodeToDes3(pubId + ""));
    return sbl.toString();
  }


  /**
   * 转换收录情况的返回格式
   * 
   * @param pubList
   * @return
   */
  private String convertPubListToString(Sie6PublicationList pubList) {
    if (pubList == null) {
      return null;
    }
    StringBuilder retStr = new StringBuilder();
    if (pubList.getListEi() != null && pubList.getListEi() == 1) {
      retStr.append(",ei");
    }
    if (pubList.getListSci() != null && pubList.getListSci() == 1) {
      retStr.append(",sci");
    }
    if (pubList.getListIstp() != null && pubList.getListIstp() == 1) {
      retStr.append(",istp");
    }
    if (pubList.getListSsci() != null && pubList.getListSsci() == 1) {
      retStr.append(",ssci");
    }
    if (pubList.getListCssci() != null && pubList.getListCssci() == 1) {
      retStr.append(",cssci");
    }
    if (pubList.getListPku() != null && pubList.getListPku() == 1) {
      retStr.append(",pku");
    }
    if (pubList.getListCscd() != null && pubList.getListCscd() == 1) {
      retStr.append(",cscd");
    }
    if (pubList.getListOther() != null && pubList.getListOther() == 1) {
      retStr.append(",other");
    }
    return retStr.length() > 0 ? retStr.substring(1) : retStr.toString();
  }
}
