package com.smate.center.open.service.data.nsfc.impl;

import java.util.Map;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.continueproject.SyncConProjectReport;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.continueproject.ConPrjRptService;
import com.smate.center.open.service.nsfc.continueproject.ConPrjRptXmlDocument;
import com.smate.center.open.utils.xml.WebServiceUtils;

/**
 * 成果在线 同步延续报告
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class NsfcSyncProjectReportComponentServiceImpl extends IsisNsfcDataHandleBase {


  @Autowired
  private ConPrjRptService conPrjRptService;

  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception {
    String verifyResult = null;
    Object syncXml = dataParamet.get(OpenConsts.MAP_PARAM_XML);
    if (syncXml == null || "".endsWith(syncXml.toString())) {
      verifyResult = WebServiceUtils.setResut2("-3", "同步延续报告 Missing Parameter");
      return verifyResult;
    }
    SyncConProjectReport report = this.parseXml(syncXml.toString());
    if (report == null) {
      verifyResult = WebServiceUtils.setResut2("-3", "同步延续报告 参数对象转换失败");
      return verifyResult;
    }
    StringBuilder missParams = new StringBuilder();
    if (report.getNsfcPrjCode() == null) {
      missParams.append("prjcode,");
    }
    if (report.getNsfcRptId() == null) {
      missParams.append("nsfcrPtId,");
    }
    if (missParams.length() > 0) {
      verifyResult = WebServiceUtils.returnMissParams(missParams.toString());
      return verifyResult;
    }
    dataParamet.put("report", report);
    return verifyResult;
  }

  @Override
  public String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception {

    try {
      Long psnId = (Long) dataParamet.get(OpenConsts.MAP_PSNID);
      SyncConProjectReport report = (SyncConProjectReport) dataParamet.get("report");
      conPrjRptService.synConPrjReport(report, psnId);
      return WebServiceUtils.setResutl("true");
    } catch (Exception e) {
      logger.error("同步延续报告信息出现异常", e);
      return WebServiceUtils.getResutlError(WebServiceUtils.SYNC_ROL_PROJECT);
    }

  }

  private SyncConProjectReport parseXml(String xmlData) throws DocumentException {
    ConPrjRptXmlDocument doc = new ConPrjRptXmlDocument(xmlData);
    SyncConProjectReport report =
        new SyncConProjectReport(doc.getPrjCode(), doc.getPno(), doc.getCtitle(), doc.getRptYear(), doc.getDisno1(),
            doc.getPsnGuid(), doc.getRolId(), doc.getOrgName(), doc.getNsfcRptId(), doc.getRptType(), doc.getStatus());
    return report;
  }


}
