package com.smate.center.open.service.data.interconnection.pub.log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.interconnection.log.InterconnectionImportPubLogDao;
import com.smate.center.open.model.interconnection.log.InterconnectionImportPubLog;
import com.smate.center.open.service.data.ThirdDataTypeBase;


/**
 * 成果成果导入日志服务
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class InterconnectionImportPubLogServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InterconnectionImportPubLogDao interconnectionImportPubLogDao;
  @Autowired
  private OpenGroupUnionDao ppenGroupUnionDao;



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

    Object pubIds = serviceData.get("pubIds");
    if (pubIds == null) {
      logger.error("服务参数 业务系统导入的成果id为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_251, paramet, "服务参数 业务系统导入的成果id为空");
      return temp;
    }
    Object groupCode = serviceData.get("groupCode");
    if (groupCode != null) {
      paramet.put("groupCode", groupCode);
    }
    paramet.put("pubIds", pubIds);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    String[] pubIdsArray = paramet.get("pubIds").toString().split(",");
    Object groupCode = paramet.get("groupCode");
    Long groupId = null;
    if (groupCode != null) {
      groupId = ppenGroupUnionDao.findGroupIdByGroupCode(paramet.get("groupCode").toString(),
          Long.valueOf(paramet.get(OpenConsts.MAP_PSNID).toString()));
    }
    for (String pubIdstr : pubIdsArray) {
      InterconnectionImportPubLog log = new InterconnectionImportPubLog();
      log.setOpenId(Long.valueOf(paramet.get(OpenConsts.MAP_OPENID).toString()));
      log.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
      if (NumberUtils.isNumber(pubIdstr)) {
        log.setPubId(Long.valueOf(pubIdstr));
      } else {
        log.setDescribe("错误记录   pubid=" + pubIdstr);
      }
      log.setGroupId(groupId);
      log.setImportDate(new Date());
      interconnectionImportPubLogDao.save(log);
    }
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.MAP_DATA, null);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "成果验证成功");// 响应成功
    return temp;
  }

}
