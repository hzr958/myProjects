package com.smate.center.open.service.nsfc.continueproject;

import org.dom4j.DocumentException;

import com.smate.center.open.utils.xml.WsXmlDocument;
import com.smate.core.base.utils.string.IrisNumberUtils;



public class GetPrjRptPubXmlDocument extends WsXmlDocument {

  // 人员guid
  public static final String WS_PARAM_PSNGUID = "/psnGuid";

  // 系统标识ID，基金委：2565
  public static final String WS_PARAM_ROLID = "/rolId";

  // isis系统中结题或者进展报告的ID
  public static final String WS_PARAM_NSFCRPTID = "/nsfcRptId";

  public GetPrjRptPubXmlDocument(String xmlDate) throws DocumentException {
    super(xmlDate);
  }

  public String getPsnGuid() {
    return this.getMethodParamVal(WS_PARAM_PSNGUID);
  }

  public Long getRolId() {
    String rolStr = this.getMethodParamVal(WS_PARAM_ROLID);
    return IrisNumberUtils.createLong(rolStr);
  }

  public Long getNsfcRptId() {
    String nsfcRptIdStr = this.getMethodParamVal(WS_PARAM_NSFCRPTID);
    return IrisNumberUtils.createLong(nsfcRptIdStr);
  }

}
