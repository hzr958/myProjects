package com.smate.center.open.service.data.nsfc.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.SyncProposalModel;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.NsfcService;
import com.smate.center.open.utils.xml.WebServiceUtils;

/**
 * 获取申请书成果 服务实现
 * 
 * @author ajb 通过bean获得
 */
@Transactional(rollbackFor = Exception.class)
public class IrisNsfcGetPrpPubsServiceImpl extends IsisNsfcDataHandleBase {
  @Autowired
  private NsfcService nsfcService;

  /**
   * 参数校验
   */
  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) {
    String verifyResult = null;
    Object syncXml = dataParamet.get(OpenConsts.MAP_SYNCXML);

    if (syncXml == null || "".endsWith(syncXml.toString())) {
      verifyResult = WebServiceUtils.setResut2("-3", "Missing Parameter");
      return verifyResult;
    }
    return verifyResult;
  }

  /**
   * 数据操作
   * 
   * @throws Exception
   */
  @Override
  public String doHandlerIsisData(Map<String, Object> paramet) throws Exception {

    SyncProposalModel prpModel = WebServiceUtils.toSyncPrpModel(paramet.get(OpenConsts.MAP_SYNCXML).toString());

    prpModel.setPsnId(Long.valueOf(paramet.get(OpenConsts.MAP_PSNID).toString()));

    // 获取申请书成果，以XML的格式返回
    return nsfcService.buildPrpPubsXml(prpModel);
  }


}
