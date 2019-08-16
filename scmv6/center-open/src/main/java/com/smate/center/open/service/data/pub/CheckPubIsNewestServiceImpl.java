package com.smate.center.open.service.data.pub;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 检查成果是不是最新的 传入参数 pubid (pubid只查个人库 基准库成果不能被跟新,如果pubid在个人库没找到就忽略)
 * 
 * @author tsz
 * 
 *         时间格式 yyyy-mm-dd HH:MM:SS 参数格式 data :{pubIds:[{pubId:xxxx,importDate:xxxx},{xxx}....]}
 * 
 * 
 *         返回格式 {[{pubId:xxx,isNewest:true},{xxx}....]} //isNewest:true true标识是最新的 false标识不是最新的
 *         为false标识 需要更新
 */
@Transactional(rollbackFor = Exception.class)
public class CheckPubIsNewestServiceImpl extends ThirdDataTypeBase {

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    //
    Object obj = serviceData.get("pubIds");
    if (!(obj instanceof List)) {
      logger.error("服务参数格式不正确");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_225, paramet, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return temp;
    }
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 取出
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    List<Map<String, String>> list = (List<Map<String, String>>) paramet.get("pubIds");
    for (Map<String, String> map : list) {
      String pubIdStr = map.get("pubId");
      if (StringUtils.isBlank(pubIdStr) || !NumberUtils.isNumber(pubIdStr))
        continue;
      String importDate = map.get("importDate");
      // 为空 或者不是时间格式
      if (StringUtils.isBlank(importDate) || !importDate.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}"))
        continue;
      // 查询数据库
      Long pubId = Long.valueOf(pubIdStr);
      if (pubId == null)
        continue;
      try {
        Date date = DateUtils.parseStringToDate(importDate);
        String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
        PubQueryDTO pubQueryDTO = new PubQueryDTO();
        pubQueryDTO.setSearchPubId(pubId);
        pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PUB_BY_PUB_ID_SERVICE);
        Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
        // 拼接返回参数
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("pubId", pubId);
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
        if (resultList != null && resultList.size() > 0) {
          String gmtModified = (String) resultList.get(0).get("gmtModifiedStr");
          if (StringUtils.isNotBlank(gmtModified)) {
            Date modifiedDate = DateUtils.parseStringToDate(gmtModified);
            if (modifiedDate.compareTo(date) != -1) {// 更新时间大于等于导入时间
              // false
              resultMap.put("isNewest", false);
              resultMap.put("updateDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(modifiedDate));
            } else {
              resultMap.put("isNewest", true);
            }
          } else {
            resultMap.put("isNewest", true);
          }
          resultMap.put("status", resultList.get(0).get("status"));//0 正常，1删除
          if(Integer.parseInt(resultList.get(0).get("status").toString()) == 1){
            resultMap.put("isNewest", "");
          }
        } else {
          resultMap.put("isNewest", "");
          resultMap.put("status", 2);//不存在
        }
        dataList.add(resultMap);
      } catch (Exception e) {
        logger.error("检查时间是不最新成果出错 ，pubId=" + pubId);
        // logger.error("paramet=" + JacksonUtils.mapToJsonStr(paramet));
        continue;
      }
    }

    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }
}
