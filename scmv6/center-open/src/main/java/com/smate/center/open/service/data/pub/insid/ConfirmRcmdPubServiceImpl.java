package com.smate.center.open.service.data.pub.insid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.data.OpenThirdRegDao;
import com.smate.center.open.dao.psn.PsnInsDao;
import com.smate.center.open.dao.publication.PubAssignLogDao;
import com.smate.center.open.model.publication.PubAssignLog;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 2018-04-13 确认个人待认领的成果，需要insid权限判断
 * 
 * @author aijiangbin
 *
 */

@Transactional(rollbackFor = Exception.class)
public class ConfirmRcmdPubServiceImpl extends ThirdDataTypeBase {


  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenThirdRegDao openThirdRegDao;
  @Autowired
  private PsnInsDao psnInsDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubAssignLogDao pubAssignLogDao;


  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());

    Object pubConfirmIdObj = serviceData.get("pubConfirmId");
    if (pubConfirmIdObj == null || !NumberUtils.isNumber(pubConfirmIdObj.toString())) {
      logger.error("pubConfirmId不属于当前调用者，没有权限调用该接口  psnId=" + psnId + " pubConfirmId=" + pubConfirmIdObj);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2007, paramet, "pubConfirmId格式不正确，没有权限调用该接口");
      return temp;
    }
    Long pubConfirmId = NumberUtils.toLong(pubConfirmIdObj.toString());
    PubAssignLog pubAssignLog = pubAssignLogDao.getAssignLogByPubConfirmId(pubConfirmId);
    // 判断当前人的psnId 和 成果确认的 psnId是否相同
    if (pubAssignLog == null || pubAssignLog.getPsnId() == null
        || pubAssignLog.getPsnId().longValue() != psnId.longValue()) {
      logger.error("pubCfmId不属于当前调用者，没有权限调用该接口  psnId=" + psnId + " pubCfmId=" + pubConfirmId);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2006, paramet, "pubCfmId不属于当前调用者，没有权限调用该接口");
      return temp;
    }
    if (pubAssignLog.getConfirmResult() != 0) {
      logger.error("该pubConfirmId成果已认领  psnId=" + psnId + " pubCfmId=" + pubConfirmId);
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2008, paramet, "该pubConfirmId成果已认领  psnId=");
      return temp;
    }
    paramet.put("pdwhPubId", pubAssignLog.getPdwhPubId());
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Long pdwhPubId = NumberUtils.toLong(paramet.get("pdwhPubId").toString());
    Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());

    Object operate = paramet.get("operate");
    // 拒绝认领成果
    if (operate != null && operate.toString().equals("refuse")) {
      // 0 = 未认领 ， 1=已认领 ， 2 拒绝
      pubAssignLogDao.updateConfirmResult(pdwhPubId, psnId, 2);
      return successMap(OpenMsgCodeConsts.SCM_000, dataList);
    }
    String SERVER_URL = domainscm + V8pubQueryPubConst.PUB_CONFIRM;
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
    paramMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    Map<String, Object> resultMap = (Map<String, Object>) getRemotePubInfo(paramMap, SERVER_URL);
    String result = "";
    // 返回的格式：成功：{"status": "SUCCESS"} 失败：{"status": "ERROR","msg":"错误信息"}
    if (resultMap == null) {
      return errorMap(OpenMsgCodeConsts.SCM_2009, paramet, null);
    }
    if (resultMap.get("status").equals("SUCCESS")) {
      return successMap(OpenMsgCodeConsts.SCM_000, dataList);
    } else if (result.contains("dup")) {
      return errorMap(OpenMsgCodeConsts.SCM_2008, paramet, null);
    }
    return errorMap(OpenMsgCodeConsts.SCM_2009, paramet, resultMap.get("msg").toString());
  }



}
