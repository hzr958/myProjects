package com.smate.web.psn.model.resume;

import org.springframework.web.util.HtmlUtils;

public class CVWorkInfo implements Comparable<CVWorkInfo> {
  private String seqWork;// 排序相当于id
  private String insName;// 单位名称
  private String department;// 部门名称
  private String degreeName;// 学位名称
  private String degreeCode;// 学位code
  private String description;// 描述
  private Long insId;// 单位id
  private Long isActive;// 1表示至今
  private Long fromYear;// 开始年
  private Long fromMonth;// 开始月
  private Long toYear;// 结束年
  private Long toMonth;// 结束月

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getDegreeName() {
    return degreeName;
  }

  public void setDegreeName(String degreeName) {
    this.degreeName = degreeName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = HtmlUtils.htmlUnescape(description);
  }

  public Long getIsActive() {
    return isActive;
  }

  public void setIsActive(Long isActive) {
    this.isActive = isActive;
  }

  public Long getFromYear() {
    return fromYear;
  }

  public void setFromYear(Long fromYear) {
    this.fromYear = fromYear;
  }

  public Long getFromMonth() {
    return fromMonth;
  }

  public void setFromMonth(Long fromMonth) {
    this.fromMonth = fromMonth;
  }

  public Long getToYear() {
    return toYear;
  }

  public void setToYear(Long toYear) {
    this.toYear = toYear;
  }

  public Long getToMonth() {
    return toMonth;
  }

  public void setToMonth(Long toMonth) {
    this.toMonth = toMonth;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getSeqWork() {
    return seqWork;
  }

  public void setSeqWork(String seqWork) {
    this.seqWork = seqWork;
  }

  public String getDegreeCode() {
    return degreeCode;
  }

  public void setDegreeCode(String degreeCode) {
    this.degreeCode = degreeCode;
  }

  @Override
  public int compareTo(CVWorkInfo o) {
    fromYear = fromYear == null ? 0 : fromYear;
    fromMonth = fromMonth == null ? 0 : fromMonth;
    isActive = isActive == null ? 0 : isActive;
    toYear = toYear == null ? 0 : toYear;
    toMonth = toMonth == null ? 0 : toMonth;
    Long pfromYear = o.getFromYear() == null ? 0 : o.getFromYear();
    Long pfromMonth = o.getFromMonth() == null ? 0 : o.getFromMonth();
    Long pisActive = o.getIsActive() == null ? 0 : o.getIsActive();
    Long ptoYear = o.getToYear() == null ? 0 : o.getToYear();
    Long ptoMonth = o.getToMonth() == null ? 0 : o.getToMonth();

    if (!fromYear.equals(pfromYear)) {
      Long m = fromYear - pfromYear;
      return Integer.valueOf(m.toString());
    } else if (!fromMonth.equals(pfromMonth)) {
      Long m = fromMonth - pfromMonth;
      return Integer.valueOf(m.toString());
    } else if (isActive == 1 || pisActive == 1) {
      Long m = isActive - pisActive;
      return Integer.valueOf(m.toString());
    } else if (!toYear.equals(ptoYear)) {
      Long m = toYear - ptoYear;
      return Integer.valueOf(m.toString());
    } else {
      Long m = toMonth - ptoMonth;
      return Integer.valueOf(m.toString());
    }
  }

}
