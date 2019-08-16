package com.smate.center.open.service.data.nsfc.impl;

import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.model.nsfc.SyncProposalModel;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.NsfcService;
import com.smate.center.open.utils.xml.WebServiceUtils;

/**
 * 
 * @zjh 获取专家成果xml
 *
 */

@Transactional(rollbackFor = Exception.class)
public class GetExpertPubsByGuid extends IsisNsfcDataHandleBase {

  @Autowired
  private NsfcService nsfcService;

  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception {
    if (!WebServiceUtils.checkWsParams(dataParamet.get("syncXml").toString(), dataParamet.get("rolId").toString())) {
      return WebServiceUtils.setResult3("-3", "Missing Parameter");
    }
    SyncProposalModel prpModel = WebServiceUtils.toSyncPrpModel(dataParamet.get("syncXml").toString());
    prpModel.setPsnId(NumberUtils.toLong(dataParamet.get("psnId").toString()));
    dataParamet.put("prpModel", prpModel);
    return null;
  }

  @Override
  public String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception {
    return nsfcService.buildExpertPubsXml((SyncProposalModel) dataParamet.get("prpModel"));
  }

}
