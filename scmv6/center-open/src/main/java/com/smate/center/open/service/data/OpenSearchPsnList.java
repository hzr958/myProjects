package com.smate.center.open.service.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenSearchPsnListException;
import com.smate.center.open.service.common.IrisCommonService;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.core.base.utils.model.security.Person;

/**
 * 
 * @ajb 根据人员信息查询人员列表的记录
 *
 */
@Transactional(rollbackFor = Exception.class)
public class OpenSearchPsnList extends ThirdDataTypeBase {

  @Autowired
  private PersonManager personManager;

  @Autowired
  private IrisCommonService irisCommonService;



  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {



    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验服务参数 serviceType
    Map<String, Object> serviceData = checkDataMapParamet(paramet, temp);

    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }

    if (serviceData.get("psnName") == null || serviceData.get("psnOrgName") == null
        || serviceData.get("psnEmail") == null) {
      logger.error("具体服务类型参数不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_227, paramet, "");
      return temp;
    }
    if (StringUtils.isNotBlank(serviceData.get("psnName").toString())
        || StringUtils.isNotBlank(serviceData.get("psnOrgName").toString())
        || StringUtils.isNotBlank(serviceData.get("psnEmail").toString())) {

      reBuildIsisDataPatamet(paramet, serviceData);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      return temp;
    } else {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      logger.error("具体服务类型参数不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_228, paramet, "");
      return temp;
    }

  }

  /**
   * 重新构造封装 isis成果在线数据参数
   * 
   * @param paramet
   * @param serviceData
   * @param serviceType
   */
  private void reBuildIsisDataPatamet(Map<String, Object> paramet, Map<String, Object> serviceData) {
    serviceData.put(OpenConsts.MAP_PSNID, paramet.get(OpenConsts.MAP_PSNID));
    serviceData.put(OpenConsts.MAP_GUID, paramet.get(OpenConsts.MAP_GUID));
    serviceData.put(OpenConsts.MAP_TOKEN, paramet.get(OpenConsts.MAP_TOKEN));
    paramet.put(OpenConsts.MAP_DATAPATAMETMAP, serviceData);
  }


  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    Map<String, Object> serviceData = (Map<String, Object>) paramet.get(OpenConsts.MAP_DATAPATAMETMAP);
    Map<String, Object> temp = new HashMap<String, Object>();
    String psnListXml = "<persons></persons>";
    try {
      List<Person> psnList = null;
      psnList = personManager.getPersonList(serviceData.get("psnName").toString(),
          serviceData.get("psnOrgName").toString(), serviceData.get("psnEmail").toString());
      if (CollectionUtils.isNotEmpty(psnList)) {
        psnListXml = irisCommonService.buildPsnListXmlStr(psnList);
      }
    } catch (Exception e) {
      // 抛异常
      logger.error("查询员列表的记录失败.", e);
      throw new OpenSearchPsnListException("查询员列表的记录失败 ", e);
    }
    // return psnListXml;
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "具体成果在线服务调用成功");// 响应成功
    // 封装结果参数
    Object object = psnListXml;
    Map<String, Object> data = new HashMap<String, Object>();
    data.put(OpenConsts.RESULT_NSFC_RESULT, object);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 所有返回数据 并须封装在list<map>里面
    dataList.add(data);


    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

}
