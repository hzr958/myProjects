package com.smate.center.open.service.data.pub.insid;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.consts.ConstPubTypeDao;
import com.smate.center.open.dao.data.OpenThirdRegDao;
import com.smate.center.open.dao.psn.PsnInsDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.web.v8pub.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 2018-04-13 获取个人所有的成果显示信息，需要insid权限判断
 * 
 * @author aijiangbin
 *
 */

@Transactional(rollbackFor = Exception.class)
public class ObtainShowPubInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenThirdRegDao openThirdRegDao;
  @Autowired
  private PsnInsDao psnInsDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    if(paramet.get("data") != null){
      Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
      paramet.putAll(serviceData);
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());
    Person person = personDao.get(psnId);
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnId(psnId);
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PUB_SITUATION_LIST);
    pubQueryDTO.setPubType(com.smate.core.base.utils.string.StringUtils.IsNotBlankObject(paramet.get("pubType"))?paramet.get("pubType").toString() :"");
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    int count = (int) result.get("totalCount");
    Map<String, Object> temp1 = new HashMap<String, Object>();
    temp1.put("count", count);
    dataList.add(temp1);
    List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
    if (resultList != null && resultList.size() > 0) {
      if (resultList != null && resultList.size() > 0) {
        for (Map<String, Object> pubMap : resultList) {
          try {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
            Map<String, Object> map = new HashMap<>();
            map.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubMap.get("pubId").toString()));
            map.put("serviceType", V8pubQueryPubConst.QUERY_SIE_OPEN_SNS_PUB);
            Map<String, Object> pubInfoMap = (Map<String, Object>) getRemotePubInfo(map, SERVER_URL);
            if (pubInfoMap != null) {
              PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
              dataMap = parseXmlToMap(pubDetailVO,person);
            }
            dataMap.put("pubShortUrl", pubMap.get("pubIndexUrl"));
            dataList.add(dataMap);
          } catch (Exception e) {
            logger.error("构建成果map数据出错 pubId=" + pubMap.get("pubId"), e);
          }
        }
      }
    }
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

  /**
   * authors zh_title en_title zh_keywords en_keywords doi doi_url list_info zh_source en_source
   * publish_year full_text_img_url product_mark pubShortUrl 成果短地址 【新增】
   *
   * @param pubDetailVO
   * @return
   * @throws Exception
   */
  private Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO , Person person) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();

      dataMap.put("authors", pubDetailVO.getAuthorNames());
      dataMap.put("zh_title", pubDetailVO.getTitle());
      dataMap.put("en_title", pubDetailVO.getTitle());
      // 关键词
      dataMap.put("zh_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      dataMap.put("en_keywords", IrisStringUtils.subMaxLengthString(pubDetailVO.getKeywords(), 200));
      // doi
      String doi = pubDetailVO.getDoi();
      dataMap.put("doi", doi);
      dataMap.put("doi_url", StringUtils.isNotBlank(doi) ? "http://dx.doi.org/" + doi : "");
      // list_info
      fillListInfo(pubDetailVO, dataMap);
      dataMap.put("zh_source", pubDetailVO.getBriefDesc());
      dataMap.put("en_source", pubDetailVO.getBriefDesc());
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(pubDetailVO.getPublishDate()));
      dataMap.put("publish_month", PubDetailVoUtil.parseDateForMonth(pubDetailVO.getPublishDate()));
      dataMap.put("publish_day", PubDetailVoUtil.parseDateForDay(pubDetailVO.getPublishDate()));
      //构建作者
      buildPubMember(dataMap,pubDetailVO,person);

      if (pubDetailVO.getFullText() != null) {
        dataMap.put("full_text_img_url", pubDetailVO.getFullText().getThumbnailPath());
      } else {
        dataMap.put("full_text_img_url", "");
      }
      ConstPubType pubType = constPubTypeDao.get(pubDetailVO.getPubType());
      dataMap.put("pub_type", pubType != null ? pubType.getZhName() : "");
      dataMap.put("product_mark", pubDetailVO.getFundInfo());
      dataMap.put("impactFactors", pubDetailVO.getImpactFactors());
      dataMap.put("record_from", pubDetailVO.getRecordFrom().getValue());
      PubDetailVoUtil.buildWos(dataMap,pubDetailVO);
      buildpubIndexUrl(dataMap, pubDetailVO);
      dataMap.put("original_mail" , pubDetailVO.getOriginalMail());
      dataMap.put("update_mark",pubDetailVO.getUpdateMark());
      PubTypeInfoDTO pubTypeInfo = pubDetailVO.getPubTypeInfo();
      switch (pubDetailVO.getPubType()) {
        case 3:
          // 会议
          buildConf(dataMap, (ConferencePaperDTO) pubTypeInfo);
          String country_name = pubDetailVO.getCountryName();
          String city = pubDetailVO.getCityName();
          dataMap.put("meeting_address", getNotNullStr(country_name) + getNotNullStr(city));
          break;
        case 4:
          // 期刊
          buildJournal(dataMap, (JournalInfoDTO) pubTypeInfo);
          dataMap.put("appliy_time", PubDetailVoUtil.dealDateFormat( getNotNullStr(pubDetailVO.getPublishDate())));
          break;
        case 5:
          // 专利
          buildPatent(dataMap, (PatentInfoDTO) pubTypeInfo);
          break;

        default:// 其他
          ;
          break;
      }
    }
    return dataMap;
  }

  /**
   * sie提供出去取成果专用
   *
   * @param dataMap
   * @param pubDetailVO
   */
  public static void buildPubMember(Map<String, Object> dataMap, PubDetailVO pubDetailVO , Person person) {

    List<PubMemberDTO> pubMemberList = pubDetailVO.getMembers();
    List<Map<String, String>> authorsList = new ArrayList<Map<String, String>>();
    if (pubMemberList != null && pubMemberList.size() > 0) {
      for (int j = 0; j < pubMemberList.size(); j++) {
        Map<String, String> authorMap = new HashMap<String, String>();
        PubMemberDTO pubMember = pubMemberList.get(j);
        String psnName = pubMember.getName(); // 作者名
        String orgName = "";
        // lamda表达式空指针异常
        if (pubMember.getInsNames() != null && pubMember.getInsNames().size() > 0) {
          orgName =
              Optional.ofNullable(pubMember.getInsNames()).map(list -> list.get(0)).map(s -> s.getInsName()).orElse(""); // 作者机构
        }
        authorMap.put("seq_no", Integer.toString(j + 1));
        // 作者名
        authorMap.put("member_psn_name", StringUtils.isNotBlank(psnName) ? psnName.trim() : "");
        authorMap.put("ins_name", StringUtils.isNotBlank(orgName) ? orgName : "");
        //基准库成果作者要判断
        if(StringUtils.isNotBlank(psnName) && person != null && pubDetailVO.getRecordFrom() != null && pubDetailVO.getRecordFrom().getValue() == 3){
          psnName = psnName.trim();
          if(PubDetailVoUtil.compareName(person.getName(),psnName) ||PubDetailVoUtil.compareName(person.getEnName(),psnName) ||
              PubDetailVoUtil.compareName(person.getFirstName()+" "+person.getLastName(),psnName)
              ||PubDetailVoUtil.compareName(person.getLastName()+" "+person.getFirstName(),psnName)){
            pubMember.setOwner(true);
          }
        }
        authorMap.put("owner", pubMember.isOwner() ? "1":"0");
        authorsList.add(authorMap);
      }
    }
    dataMap.put("pub_members", authorsList);
  }

  /**
   * 得到成果收录情况
   *
   * @return
   */
  public void fillListInfo(PubDetailVO pubDetailVO, Map<String, Object> dataMap) {
    List<PubSituationDTO> sitations = pubDetailVO.getSituations();
    String listInfoListStr = "";
    if (sitations != null && sitations.size() > 0) {
      for (PubSituationDTO sitation : sitations) {
        if (sitation.isSitStatus()) {
          listInfoListStr += sitation.getLibraryName() + ",";
        }
      }
      if (StringUtils.isNotBlank(listInfoListStr)) {
        listInfoListStr = listInfoListStr.substring(0, listInfoListStr.length() - 1).toLowerCase();
      }
    }
    dataMap.put("list_info", listInfoListStr);
  }

  /**
   * 构建期刊
   * 
   * @param dataMap
   * @param pubTypeInfo
   */
  private void buildJournal(Map<String, Object> dataMap, JournalInfoDTO pubTypeInfo) {
    String page_number = pubTypeInfo.getPageNumber();
    dataMap.put("journal_name", getNotNullStr(pubTypeInfo.getName()));
    dataMap.put("issue", getNotNullStr(pubTypeInfo.getIssue())); // 期号
    dataMap.put("volume", getNotNullStr(pubTypeInfo.getVolumeNo())); // 卷号
    dataMap.put("page_number", page_number); // 卷号
    dataMap.put("issn", getNotNullStr(pubTypeInfo.getISSN())); // 卷号
  }

  /**
   * 构建会议
   * 
   * @param dataMap
   * @param pubTypeInfo
   */
  private void buildConf(Map<String, Object> dataMap, ConferencePaperDTO pubTypeInfo) {

    dataMap.put("meeting_title", pubTypeInfo.getName());
    dataMap.put("meeting_organizers", pubTypeInfo.getOrganizer());
    String start_date = pubTypeInfo.getStartDate();
    String end_date = pubTypeInfo.getEndDate();
    String meeting_time = "";
    if (StringUtils.isNotBlank(start_date) && StringUtils.isNotBlank(end_date)) {
      meeting_time = PubDetailVoUtil.dealDateFormat(start_date )+ "-" + PubDetailVoUtil.dealDateFormat( end_date );
    } else {
      meeting_time = PubDetailVoUtil.dealDateFormat( start_date ) + PubDetailVoUtil.dealDateFormat( end_date );
    }
    dataMap.put("meeting_time", meeting_time);
    String page_number = pubTypeInfo.getPageNumber();
    dataMap.put("page_number", page_number); // 卷号
  }

  private void buildPatent(Map<String, Object> dataMap, PatentInfoDTO pubTypeInfo) {


    dataMap.put("patent_type", PubParamUtils.buildPatentTypeDesc(pubTypeInfo.getType()));
    dataMap.put("apply_no", getNotNullStr(pubTypeInfo.getApplicationNo()));
    // 授权号
    dataMap.put("grant_no", getNotNullStr(pubTypeInfo.getPublicationOpenNo()));
    // 申请时间
    dataMap.put("apply_time", PubDetailVoUtil.dealDateFormat( getNotNullStr(pubTypeInfo.getApplicationDate()) ));
    // ipc号
    dataMap.put("ipc_no", getNotNullStr(pubTypeInfo.getIPC()));

  }

  /**
   * 构建成果短地址
   *
   * @param dataMap
   * @param pubDetailVO
   */
  private void buildpubIndexUrl(Map<String, Object> dataMap, PubDetailVO pubDetailVO) {
    String pubIndexUrl = pubDetailVO.getPubIndexUrl();
    if (pubIndexUrl != null) {
      dataMap.put("pubShortUrl", pubIndexUrl);
      dataMap.put("unique_code", pubIndexUrl.substring(pubIndexUrl.lastIndexOf("/") + 1, pubIndexUrl.length()));
    } else {
      dataMap.put("pubShortUrl", "");
      dataMap.put("unique_code", "");
    }
  }


  public String getNotNullStr(String str) {
    if (StringUtils.isBlank(str)) {
      return "";
    }
    return str;
  }

  public Object getNotNullObj(Object obj) {
    if (obj == null || StringUtils.isBlank(obj.toString())) {
      return "";
    }
    return obj;
  }

  public static void main(String[] args) {
    String str = "htj://ssss.scho.sss/uuuuu";
    System.out.println(str.substring(str.lastIndexOf("/"), str.length()));
  }

}
