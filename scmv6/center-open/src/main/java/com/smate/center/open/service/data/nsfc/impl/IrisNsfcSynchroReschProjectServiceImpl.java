package com.smate.center.open.service.data.nsfc.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.project.NsfcReschProject;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.NsfcService;
import com.smate.center.open.utils.xml.WebServiceUtils;

/**
 * 9.4.2同步研究成果报告
 * 
 * @author ajb 通过bean获得
 */
@Transactional(rollbackFor = Exception.class)
public class IrisNsfcSynchroReschProjectServiceImpl extends IsisNsfcDataHandleBase {
  @Autowired
  private NsfcService nsfcService;

  /**
   * 参数校验
   */
  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) {
    String verifyResult = null;
    Object syncXml = dataParamet.get(OpenConsts.MAP_SYNCXML);
    Object code = dataParamet.get(OpenConsts.MAP_CODE); // 还要验证code
    Object rolId = dataParamet.get(OpenConsts.MAP_ROLID); // 还要验证code

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
    // xml数据转换为 对象
    NsfcSyncProject nsfcSyncProject = WebServiceUtils.toNsfcSyncProject(paramet.get(OpenConsts.MAP_SYNCXML).toString());
    nsfcSyncProject.setPsnid(paramet.get(OpenConsts.MAP_PSNID).toString());
    Long snsPrjId = Long.parseLong(nsfcSyncProject.getPrjcode());

    // 同步到scm
    NsfcReschProject snsNsfcReschProject = nsfcService.saveNsfcReschSyncProject(nsfcSyncProject);

    if (snsNsfcReschProject != null) {
      return WebServiceUtils.setResut2("1", "success");
    }
    return WebServiceUtils.getResutlError(WebServiceUtils.SYNC_ROL_PROJECT);
  }


}
