package com.smate.center.open.service.data.nsfc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;

/**
 * isis成果在线 数据交互 入口实现
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class IsisNsfcDataAccessImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  // 具体服务类型 ：取成果，同步申请书......
  private Map<String, IsisNsfcDataHandleService> serviceMap;



  /**
   * 参数校验
   */
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验服务参数 serviceType
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }

    Object serviceType = serviceData.get("serviceType");
    if (serviceType == null) {
      logger.error("具体服务类型参数不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_227, paramet, "");
      return temp;
    }
    IsisNsfcDataHandleService service = serviceMap.get(serviceType.toString());
    if (service == null) {
      logger.error("具体服务类型参数不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_228, paramet, "");
      return temp;
    }
    paramet.put("serviceType", serviceType);
    reBuildIsisDataPatamet(paramet, serviceData, serviceType);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 重新构造封装 isis成果在线数据参数
   * 
   * @param paramet
   * @param serviceData
   * @param serviceType
   */
  private void reBuildIsisDataPatamet(Map<String, Object> paramet, Map<String, Object> serviceData,
      Object serviceType) {
    serviceData.put(OpenConsts.MAP_PSNID, paramet.get(OpenConsts.MAP_PSNID));
    serviceData.put(OpenConsts.MAP_GUID, paramet.get(OpenConsts.MAP_GUID));
    serviceData.put(OpenConsts.MAP_SERVICE_TYPE, serviceType);
    serviceData.put(OpenConsts.MAP_TOKEN, paramet.get(OpenConsts.MAP_TOKEN));
    paramet.put(OpenConsts.MAP_DATAPATAMETMAP, serviceData);
  }



  /**
   * 数据处理
   */
  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    IsisNsfcDataHandleService service = serviceMap.get(paramet.get(OpenConsts.MAP_SERVICE_TYPE).toString());
    // 调用具体的服务，注意psnId,guid,serviceType,原始参数数据 已经封装进paramet了
    String isisResult = service.handleIsisData((Map<String, Object>) paramet.get(OpenConsts.MAP_DATAPATAMETMAP));
    Object object = isisResult;
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_NSFC_RESULT, object);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 所有返回数据 并须封装在list<map>里面
    dataList.add(temp);
    return super.successMap("具体成果在线服务调用成功", dataList);
  }

  public Map<String, IsisNsfcDataHandleService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, IsisNsfcDataHandleService> serviceMap) {
    this.serviceMap = serviceMap;
  }

}
