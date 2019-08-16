package com.smate.center.open.service.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.nsfc.logs.LogInfoDao;
import com.smate.center.open.model.nsfc.logs.LogInfo;
import com.smate.center.open.service.interconnection.ScmUnionDataHandleService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 互联互通的服务服务
 * 
 * @author AiJiangBin
 *
 */
public class OpenUnionTypeImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private LogInfoDao logInfoDao;

  // 具体服务类型 ：获取互联互通的 ， 查询人员 ， 查询 url 查询成果
  private Map<String, ScmUnionDataHandleService> serviceMap;

  public Map<String, ScmUnionDataHandleService> getServiceMap() {
    return serviceMap;
  }

  public void setServiceMap(Map<String, ScmUnionDataHandleService> serviceMap) {
    this.serviceMap = serviceMap;
  }

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
    ScmUnionDataHandleService service = serviceMap.get(serviceType.toString());
    if (service == null) {
      logger.error("具体服务类型参数不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_228, paramet, "");
      return temp;
    }
    paramet.put("serviceType", serviceType);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      // 获取相关服务
      ScmUnionDataHandleService service = serviceMap.get(paramet.get(OpenConsts.MAP_SERVICE_TYPE).toString());
      // 调用具体的服务，,原始参数数据 已经封装进paramet了
      String isisResult = service.handleUnionData(paramet);
      return buildSuccessMap(isisResult);
    } catch (Exception e) {
      // 吃掉异常，当做open系统的正确请求来处理
      logger.error(e.getMessage() + JacksonUtils.mapToJsonStr(paramet), e);
      this.saveAccessLog(paramet, e.toString(), 2,
          "具体业务异常，服务类型为：" + paramet.get(OpenConsts.MAP_SERVICE_TYPE).toString());
      return buildSuccessMap("处理具体服务出现异常！");
    }

  }

  private Map<String, Object> buildSuccessMap(String isisResult) {
    Object object = isisResult;
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.UNION_RESULT, object);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 所有返回数据 并须封装在list<map>里面
    dataList.add(temp);
    return successMap("scm-000 互联互通在线服务调用成功", dataList);
  }

  /**
   * 日志信息记录
   * 
   * @param dataParamet
   * @param msg
   * @param status
   */
  private void saveAccessLog(Map<String, Object> dataParamet, String msg, int status, String desc) {
    LogInfo log = new LogInfo();
    log.setActionDate(new Date());
    log.setClientIP(dataParamet.get(OpenConsts.MAP_TOKEN).toString()); // token
    log.setActionResource(msg); // 操作结果 后者异常信息
    log.setMethodName(dataParamet.get(OpenConsts.MAP_SERVICE_TYPE).toString()); // 服务类型
    log.setParameters(JacksonUtils.mapToJsonStr(dataParamet)); // 调用open系统的参数
    log.setStatus(status); // 1成功 ,2失败
    log.setDescription(desc); // 描述-- 重点
    logInfoDao.save(log);
  }


  public static void main(String[] args) {
    String data = " {\"fromSys\":\"ISIS\", \"serviceType\":\"unionGroup\"  , " + " \"quicklyCreateGroup\":\"true\"  ,  "
        + " \"groupJson\": \"{   \\\"groupName\\\":\\\"群标题\\\" , \\\"applyCode\\\":\\\"1111\\\" ,\\\"fundingTypes\\\":\\\"ins\\\" , \\\"prjExternalNo\\\":\\\"0002341\\\" ,\\\"amount\\\":\\\"111\\\"  ,\\\"startDate\\\":\\\"2012-09\\\"  ,\\\"endDate\\\":\\\"2014-12-12\\\"   ,\\\"currency\\\":\\\"rmb\\\"  ,\\\"insName\\\":\\\"单位\\\"  ,\\\"partPsnNames\\\":\\\"ajb;ccd;\\\"  ,\\\"prjStatus\\\":\\\"1\\\" }\" "
        + " }";
    Map<String, Object> map = JacksonUtils.jsonToMap(data);
    Map<String, Object> map2 = new HashMap<String, Object>();
    map2.put("1", 123);
    map2.put("2", 12322);
    map2.put("3", 12333);
    Map<String, Object> map3 = new HashMap<String, Object>();
    map3.put("5", 555);
    map3.put("6", 555);
    map3.put("7", 555);
    map2.put("map3", map3);;
    String sss = JacksonUtils.mapToJsonStr(map2);
    System.out.println(JacksonUtils.jsonToMap(sss));
  }

}
