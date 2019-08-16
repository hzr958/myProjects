package com.smate.center.open.service.nsfc.continueproject;

import org.dom4j.DocumentException;

import com.smate.center.open.utils.xml.WsXmlDocument;
import com.smate.core.base.utils.string.IrisNumberUtils;


public class ConPrjRptXmlDocument extends WsXmlDocument {

  // 项目编号
  public static final String WS_PARAM_PRJCODE = "/prjcode";

  // 项目批准号
  public static final String WS_PARAM_PNO = "/pno";

  // 项目标题
  public static final String WS_PARAM_CTITLE = "/ctitle";

  // 报告年度
  public static final String WS_PARAM_RPTYEAR = "/rptyear";

  // 科代码1
  public static final String WS_PARAM_DISNO1 = "/disno1";

  // 人员guid
  public static final String WS_PARAM_PSNGUID = "/psnguid";

  // 系统标识ID，基金委：2565
  public static final String WS_PARAM_ROLID = "/rolId";
  // 依托单位
  public static final String WS_PARAM_ORGNAME = "/orgname";

  // isis系统中结题或者进展报告的ID
  public static final String WS_PARAM_NSFCRPTID = "/nsfcRptId";

  // 报告类型
  public static final String WS_PARAM_RPTTYPE = "/rptType";

  // 报告状态 0：允许修改成果信息 1：不允许修改成果信息
  public static final String WS_PARAM_STATUS = "/status";

  public ConPrjRptXmlDocument(String xmlDate) throws DocumentException {
    super(xmlDate);
  }

  public Long getPrjCode() {
    String prjCodeStr = this.getMethodParamVal(WS_PARAM_PRJCODE);
    return IrisNumberUtils.createLong(prjCodeStr);
  }

  public String getPno() {
    return this.getMethodParamVal(WS_PARAM_PNO);
  }

  public String getCtitle() {
    return this.getMethodParamVal(WS_PARAM_CTITLE);
  }

  public Integer getRptYear() {
    String rptYearStr = this.getMethodParamVal(WS_PARAM_RPTYEAR);
    return IrisNumberUtils.createInteger(rptYearStr);
  }

  public String getDisno1() {
    return this.getMethodParamVal(WS_PARAM_DISNO1);
  }

  public String getPsnGuid() {
    return this.getMethodParamVal(WS_PARAM_PSNGUID);
  }

  public String getOrgName() {
    return this.getMethodParamVal(WS_PARAM_ORGNAME);
  }

  public Long getRolId() {
    String rolStr = this.getMethodParamVal(WS_PARAM_ROLID);
    return IrisNumberUtils.createLong(rolStr);
  }

  public Long getNsfcRptId() {
    String nsfcRptIdStr = this.getMethodParamVal(WS_PARAM_NSFCRPTID);
    return IrisNumberUtils.createLong(nsfcRptIdStr);
  }

  public Integer getRptType() {
    String rptTypeStr = this.getMethodParamVal(WS_PARAM_RPTTYPE);
    return IrisNumberUtils.createInteger(rptTypeStr);
  }

  public Integer getStatus() {
    String statusStr = this.getMethodParamVal(WS_PARAM_STATUS);
    return IrisNumberUtils.createInteger(statusStr);
  }

}
