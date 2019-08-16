package com.smate.web.prj.form.fileimport;

import java.io.Serializable;

/**
 * 项目报告
 *
 * @author aijiangbin
 * @create 2019-08-03 15:24
 **/
public class PrjReportDTO implements Serializable {

  private static final long serialVersionUID = -7326696247225893892L;
  private String prjNo ="" ; // 批准号
  private String seqNo ="" ; // 序号
  private String abortDate ="" ; // 提交截止时间
  private String reportType ="" ; // 报告类型（进展报告 或 结题报告）  1进展报告2中期报告3审计报告5结题报告6验收报告。


  public String getPrjNo() {
    return prjNo;
  }

  public void setPrjNo(String prjNo) {
    this.prjNo = prjNo;
  }

  public String getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }

  public String getAbortDate() {
    return abortDate;
  }

  public void setAbortDate(String abortDate) {
    this.abortDate = abortDate;
  }

  public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportType) {
    this.reportType = reportType;
  }
}
