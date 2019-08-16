package com.smate.center.open.service.data.pub;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.interconnection.log.InterconnectionGetPubLogService;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 返回的是更新成果 包括删除和未删除的
 * 
 * 第三方系统 获取显示编目信息接口 (只取显示用字段 )
 * 
 * @author tsz
 * 
 *         pub_id pub_type_id authors zh_title en_title pub_owner_openid has_full_text list_info
 *         zh_source en_source publish_year authenticated create_date full_text_img_url
 *         pub_detail_param prodect_mark
 *
 */
@Transactional(rollbackFor = Exception.class)
public class GetShowPubInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());



  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private InterconnectionGetPubLogService interconnectionGetPubLogService;


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
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Date getPubDate = DateUtils
        .parseStringToDate(paramet.get("lastGetPubDate") != null ? paramet.get("lastGetPubDate").toString() : "");
    Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());
    Long openId = NumberUtils.toLong(paramet.get("openid").toString());
    Integer pageNo = NumberUtils.toInt(paramet.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(paramet.get("pageSize").toString());


    ArrayList<Integer> pubTypeList = new ArrayList<Integer>();
    buildPubType(paramet, pubTypeList);

    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnId(psnId);
    pubQueryDTO.setPubType(paramet.get("pubType").toString());
    pubQueryDTO.setPageNo(pageNo);
    pubQueryDTO.setPageSize(pageSize);
    if (getPubDate != null) {
      pubQueryDTO.setPubUpdateDate(getPubDate);
    }

    pubQueryDTO.setServiceType(V8pubQueryPubConst.CENTER_OPEN_PUB_LIST);
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
      logger.error("v8pub未查询到成果errorMsg="+result);
      return super.errorMap(OpenMsgCodeConsts.SCM_277, paramet, "未查询到成果");
    }
    Long totalPages = 0L;
    if (totalCount % pageSize == 0) {
      totalPages = totalCount / pageSize;
    } else {
      totalPages = totalCount / pageSize + 1;
    }
    List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
    Map<String, Object> temp1 = new HashMap<String, Object>();
    temp1.put("totalPages", totalPages);
    temp1.put("count", totalCount);
    // 当前时间
    temp1.put("currentGetPubDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    dataList.add(temp1);
    // 删除的成果要有特别的标记
    if (resultList != null && resultList.size() > 0) {
      for (Map<String, Object> pubMap : resultList) {
        try {
          Map<String, Object> dataMap = new HashMap<String, Object>();
          if (Integer.parseInt(pubMap.get("status").toString()) == 1) {
            dataMap.put("status", "1");
            dataMap.put("pub_id", pubMap.get("pubId"));
          } else {
            SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
            Map<String, Object> map = new HashMap<>();
            map.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubMap.get("pubId").toString()));
            map.put("serviceType", V8pubQueryPubConst.QUERY_OPEN_SNS_PUB);
            Map<String, Object> pubInfoMap = (Map<String, Object>) getRemotePubInfo(map, SERVER_URL);
            if (pubInfoMap != null) {
              PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubInfoMap));
              dataMap = parseXmlToMap(pubDetailVO);
            }
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

    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

  /**
   * 第三方系统 获取显示编目信息接口 (只取显示用字段 )
   * 
   * @author tsz
   * 
   *         pub_id pub_type_id authors zh_title en_title pub_owner_openid has_full_text list_info
   *         zh_source en_source publish_year authenticated create_date full_text_img_url
   *         pub_detail_param prodect_mark pub_update_time
   */
  private Map<String, Object> parseXmlToMap(PubDetailVO pubDetailVO) throws Exception {
    Map<String, Object> dataMap = new HashMap<String, Object>();

    if (pubDetailVO != null) {
      Long pubId = pubDetailVO.getPubId();
      dataMap.put("pub_id", pubId.toString());
      dataMap.put("status", "0");


      dataMap.put("pub_type_id", pubDetailVO.getPubType());
      dataMap.put("authors", pubDetailVO.getAuthorName());
      dataMap.put("zh_title", pubDetailVO.getTitle());
      dataMap.put("en_title", pubDetailVO.getTitle());
      // 通过psnid 查openId 没有openid要生成openId
      dataMap.put("pub_owner_openid", openUserUnionDao.getOpenIdByPsnId(pubDetailVO.getPubOwnerPsnId()));
      // list_info
      PubDetailVoUtil.fillListInfo(pubDetailVO, dataMap);
      dataMap.put("zh_source", pubDetailVO.getBriefDesc());
      dataMap.put("en_source", pubDetailVO.getBriefDesc());
      dataMap.put("publish_year", PubDetailVoUtil.parseDateForYear(pubDetailVO.getPublishDate()));

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
    }
    return dataMap;
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
