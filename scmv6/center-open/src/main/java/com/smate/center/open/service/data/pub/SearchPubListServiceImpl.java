package com.smate.center.open.service.data.pub;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.consts.ConstPubTypeDao;
import com.smate.center.open.exception.OpenNsfcException;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.interconnection.log.InterconnectionGetPubLogService;
import com.smate.center.open.service.nsfc.IrisExcludedPubService;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.center.open.utils.ConvertObjectUtils;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 查询某人的成果统计数和成果编目信息 个人成果数据
 * 
 * @author yhx
 *
 */
public class SearchPubListServiceImpl extends ThirdDataTypeBase {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IrisExcludedPubService irisExcludedPubService;
  @Autowired
  private InterconnectionGetPubLogService interconnectionGetPubLogService;
  @Autowired
  private ConstPubTypeDao constPubTypeDao;
  @Autowired
  private SysDomainConst sysDomainConst;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object psnId = serviceData.get("psnId");
    if (psnId != null && StringUtils.isNotBlank(psnId.toString())) {
      if (!NumberUtils.isNumber(psnId.toString())) {
        psnId = Des3Utils.decodeFromDes3(psnId.toString());
        serviceData.put("psnId", psnId);
      }
      if (!NumberUtils.isNumber(psnId.toString())) {
        logger.error("具体服务类型参数psnId必须为数字");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_292, paramet, "scm-292 具体服务类型参数  data参数中psnId格式不正确");
        return temp;
      }
    } else {
      logger.error("具体服务类型参数psnId不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_295, paramet, "scm-295 具体服务类型参数  psn_id 不能为空");
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
    Object openIdObj = paramet.get("openid");
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    try {
      // {"psnId":"psnId",keywords":"用户输入的标题","authors":"作者","excludedPubIds":"排除的成果Id","sortType":"排序","pubTypes":"成果类型","beginPublishYear":"开始发表日期","endPublishYear","最后发表日期"}
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      Integer pageSize = ConvertObjectUtils.objectToInt(paramet.get("pageSize"));
      String excludedPubIds = ConvertObjectUtils.objectToString(paramet.get("excludedPubIds"));
      String uuid = saveExcludedPubIds(excludedPubIds);

      buildParams(pubQueryDTO, paramet, uuid, pageSize);

      String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
      pubQueryDTO.setServiceType(V8pubQueryPubConst.CENTER_OPEN_PUB_LIST);
      Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
      Long totalCount = 0L;
      if (result.get("status").equals("success")) {
        totalCount = Long.parseLong(result.get("totalCount").toString());
        Long totalPages = 0L;
        if (totalCount % pageSize == 0) {
          totalPages = totalCount / pageSize;
        } else {
          totalPages = totalCount / pageSize + 1;
        }
        Map<String, Object> temp1 = new HashMap<String, Object>();
        temp1.put("totalPages", totalPages);
        temp1.put("count", totalCount);
        dataList.add(temp1);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
        if (resultList != null && resultList.size() > 0) {
          // 返回数据
          dealResult(dataList, resultList);
          try {
            interconnectionGetPubLogService.saveGetPubLog(1, resultList.size(), paramet);
          } catch (Exception e) {
          }
        }
      } else {
        logger.error("v8pub未查询到成果errorMsg="+result);
        return super.errorMap(OpenMsgCodeConsts.SCM_277, paramet, "未查询到成果");
      }

      deleteExcludedPubIds(excludedPubIds, uuid);
    } catch (Exception e) {
      logger.error("返回该人员个人成果数据失败", e);
      throw new OpenNsfcException("返回该人员个人成果数据失败", e);
    }
    return super.successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

  private void dealResult(List<Map<String, Object>> dataList, List<Map<String, Object>> resultList) {
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    for (Map<String, Object> pubMap : resultList) {
      try {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<>();
        map.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubMap.get("pubId").toString()));
        map.put("serviceType", V8pubQueryPubConst.QUERY_OPEN_SNS_PUB);
        Map<String, Object> pubInfoMap = (Map<String, Object>) getRemotePubInfo(map, SERVER_URL);
        if (pubInfoMap != null) {
          PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
          dataMap = parseXmlToMap(pubDetailVO);
        }
        dataMap.put("pub_id", pubMap.get("pubId"));
        dataMap.put("title", pubMap.get("title"));
        dataMap.put("authors", pubMap.get("authorNames") == null ? "" : pubMap.get("authorNames"));
        dataMap.put("source", pubMap.get("briefDesc") == null ? "" : pubMap.get("briefDesc"));
        dataList.add(dataMap);
      } catch (Exception e) {
        logger.error("构建成果map数据出错 pubId=" + pubMap.get("pubId"), e);
      }
    }
  }

  private Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (pubDetailVO != null) {
      ConstPubType pubType = constPubTypeDao.get(pubDetailVO.getPubType());
      dataMap.put("pub_type", pubType != null ? pubType.getZhName() : "");
      dataMap.put("listed", PubDetailVoUtil.fillListInfo(pubDetailVO));
      dataMap.put("cited", "");
      dataMap.put("isicited", pubDetailVO.getCitations() == null ? 0 : pubDetailVO.getCitations());
      dataMap.put("doi", pubDetailVO.getDoi());
      String http = pubDetailVO.getPubIndexUrl();
      dataMap.put("http", http);
      dataMap.put("organization", pubDetailVO.getOrganization());
    }
    return dataMap;
  }

  private void deleteExcludedPubIds(String excludedPubIds, String uuid) throws Exception {
    if (StringUtils.isNotBlank(excludedPubIds)) {
      this.irisExcludedPubService.deleteIrisExcludedPub(uuid);
    }
  }

  /**
   * 准备参数
   * 
   * @param pubQueryDTO
   * @param paramet
   * @param uuid
   * @param pageSize
   */
  private void buildParams(PubQueryDTO pubQueryDTO, Map<String, Object> paramet, String uuid, Integer pageSize) {
    String psnId = paramet.get(OpenConsts.MAP_PSNID).toString();
    Integer pageNo = ConvertObjectUtils.objectToInt(paramet.get("pageNo"));
    String keywords = ConvertObjectUtils.objectToString(paramet.get("keywords"));
    String authors = ConvertObjectUtils.objectToString(paramet.get("authors"));
    String pubTypes = ConvertObjectUtils.objectToString(paramet.get("pubTypes"));
    String beginPublishYear = ConvertObjectUtils.objectToString(paramet.get("beginPublishYear"));
    String endPublishYear = ConvertObjectUtils.objectToString(paramet.get("endPublishYear"));
    String sortType = ConvertObjectUtils.objectToString(paramet.get("sortType"));
    pubQueryDTO.setSearchPsnId(NumberUtils.toLong(psnId));
    pubQueryDTO.setPubType(pubTypes);
    pubQueryDTO.setAuthorNames(authors);
    pubQueryDTO.setSearchKey(keywords);
    pubQueryDTO.setBeginPublishYear(checkPublishYear(beginPublishYear));
    pubQueryDTO.setEndPublishYear(checkPublishYear(endPublishYear));
    pubQueryDTO.setUuid(uuid);
    pubQueryDTO.setOrderBy(sortType);
    pubQueryDTO.setQueryAll(false);// 是否查询所有成果 true:查询所有 false:查询未被删除的成果
    if (pageNo != null) {
      pubQueryDTO.setPageNo(pageNo);
    }
    if (pageSize != null) {
      pubQueryDTO.setPageSize(pageSize);
    }
  }

  private String saveExcludedPubIds(String excludedPubIds) throws Exception {
    if (StringUtils.isNotBlank(excludedPubIds) && !excludedPubIds.matches(ServiceConstants.IDPATTERN)) {
      excludedPubIds = null;
    }
    String uuid = null;
    if (StringUtils.isNotBlank(excludedPubIds)) {
      uuid = UUID.randomUUID().toString();
      this.irisExcludedPubService.saveIrisExcludedPub(ServiceUtil.splitStrToLong(excludedPubIds), uuid);
    }
    return uuid;
  }

  private Integer checkPublishYear(String beginPublishYear) {
    if (StringUtils.isNotBlank(beginPublishYear) && beginPublishYear.matches("^\\d+$")) {
      return NumberUtils.toInt(beginPublishYear);
    }
    return null;
  }
}
