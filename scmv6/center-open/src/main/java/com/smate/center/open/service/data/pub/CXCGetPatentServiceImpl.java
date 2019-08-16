package com.smate.center.open.service.data.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dto.PatentInfoDTO;
import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.dto.ScienceAreaDTO;


/**
 * 创新城 获取专利信息接口 (以后在根据大结构统一调整) SCM-11379
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class CXCGetPatentServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());



  /**
   * 
   */
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;

  }

  /**
   * 
   * @throws OpenException
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> serviceData = JacksonUtils.jsonToMap(paramet.get(OpenConsts.MAP_DATA).toString());
    Long pubId = Long.parseLong(serviceData.get("pubIds").toString());
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubId.toString()));
    paramMap.put("serviceType", "openSnsPub");
    Map<String, Object> pubInfo = getRemotePubInfo(paramMap);
    try {
      // 返回未删除的成果
      if (pubInfo != null && Integer.parseInt(pubInfo.get("status").toString()) == 0) {
        PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfo));
        if (pubDetailVO != null) {
          Map<String, Object> map = this.parsePubInfoToMap(pubDetailVO);
          dataList.add(map);
        }
      }

    } catch (Exception e) {
      logger.error("构建返回数据出错" + paramet.toString(), e);
      super.errorMap("构建返回数据出错", paramet, e.toString());
    }
    return successMap("获取成果数据成功", dataList);
  }


  private Map<String, Object> parsePubInfoToMap(PubDetailVO<PubTypeInfoDTO> pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    Long pubId = pubDetailVO.getPubId();
    dataMap.put("pub_id", pubId.toString());
    dataMap.put("title", pubDetailVO.getTitle());
    dataMap.put("abstract", pubDetailVO.getSummary());
    dataMap.put("keyWords", pubDetailVO.getKeywords());

    // 该成果一定是专利
    if (pubDetailVO.getPubType() == 5) {
      PatentInfoDTO patentInfo = (PatentInfoDTO) pubDetailVO.getPubTypeInfo();
      dataMap.put("patent_applier", patentInfo.getApplier());
      dataMap.put("patentee", patentInfo.getPatentee());
      dataMap.put("patent_no", patentInfo.getApplicationNo());
      dataMap.put("patent_open_no", patentInfo.getPublicationOpenNo());
      dataMap.put("category_no", patentInfo.getIPC());
      // *专利类别：发明专利51 实用新型 52 外观专利53
      // *专利类别：发明专利51 实用新型 52 外观专利53
      dataMap.put("patent_type", PubParamUtils.buildPatentTypeDesc(patentInfo.getType()));
      dataMap.put("patent_status", patentInfo.getStatus());
      String startDate = patentInfo.getStartDate();
      if (StringUtils.isNoneEmpty(startDate)) {
        dataMap.put("start_year", PubDetailVoUtil.parseDateForYear(startDate));
        dataMap.put("start_month", PubDetailVoUtil.parseDateForMonth(startDate));
        dataMap.put("start_day", PubDetailVoUtil.parseDateForDay(startDate));
      } else {
        dataMap.put("start_year", "");
        dataMap.put("start_month", "");
        dataMap.put("start_day", "");
      }
      String endDate = patentInfo.getEndDate();
      if (endDate != null) {
        dataMap.put("end_year", PubDetailVoUtil.parseDateForYear(endDate));
        dataMap.put("end_month", PubDetailVoUtil.parseDateForMonth(endDate));
        dataMap.put("end_day", PubDetailVoUtil.parseDateForDay(endDate));
      } else {
        dataMap.put("end_year", "");
        dataMap.put("end_month", "");
        dataMap.put("end_day", "");
      }
      dataMap.put("patent_org", patentInfo.getIssuingAuthority());
      dataMap.put("country_name", patentInfo.getArea());
    }

    if (pubDetailVO.getApplicationStatus() == 0) {
      dataMap.put("application_date", pubDetailVO.getApplicationDate());
    }

    List<ScienceAreaDTO> scienceAreas = pubDetailVO.getScienceAreas();
    if (scienceAreas != null) {
      dataMap.put("science_area", scienceAreas);
    }


    dataMap.put("city", pubDetailVO.getCityName());
    dataMap.put("fulltext_url", pubDetailVO.getSrcFulltextUrl());
    return dataMap;
  }

  /**
   * 获取远程成果详情
   * 
   * @param map
   * @return
   */
  protected Map<String, Object> getRemotePubInfo(Map<String, Object> map) {
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(map), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    return object;
  }


}
