package com.smate.center.open.service.data.nsfc.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.NsfcService;
import com.smate.center.open.service.project.SyncRolService;
import com.smate.center.open.utils.xml.WebServiceUtils;

/**
 * 8.4.1syncProject同步进展/结题报告
 * 
 * @author ajb 通过bean获得
 */
@Transactional(rollbackFor = Exception.class)
public class IrisNsfcSynchroProjectServiceImpl extends IsisNsfcDataHandleBase {
  @Autowired
  private NsfcService nsfcService;

  @Autowired
  private SyncRolService syncRolService;

  /**
   * 参数校验
   */
  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) {
    String verifyResult = null;
    Object syncXml = dataParamet.get(OpenConsts.MAP_SYNCXML);
    Object code = dataParamet.get(OpenConsts.MAP_CODE); // 待定Code
    Object rolId = dataParamet.get(OpenConsts.MAP_ROLID); // 待定rolId

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
    NsfcProject snsNsfcProject = nsfcService.syncNsfcProject(nsfcSyncProject);

    if (snsNsfcProject != null) {
      // 同步本地(删除原有的数据，再重新同步sns保存成功的同步nsfc数据)
      syncRolService.deleteRolProject(snsPrjId);

      syncRolService.saveSyncRolProject(snsNsfcProject);
      return WebServiceUtils.setResutl("true");
    }
    return WebServiceUtils.getResutlError(WebServiceUtils.SYNC_ROL_PROJECT);
  }


}
