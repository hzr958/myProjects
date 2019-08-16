package com.smate.web.group.action.grp.form;


/**
 * 群组标签，显示的信息
 * 
 * @author aijiangbin
 *
 */
public class GrpLabelShowInfo {

  public String des3LabelId; // 标签id
  public String labelName; // 标签名
  public Integer resCount; // 标签资源数
  public Boolean showDel = false; // 是否显示删除操作


  public String getDes3LabelId() {
    return des3LabelId;
  }

  public void setDes3LabelId(String des3LabelId) {
    this.des3LabelId = des3LabelId;
  }

  public String getLabelName() {
    return labelName;
  }

  public void setLabelName(String labelName) {
    this.labelName = labelName;
  }

  public Integer getResCount() {
    return resCount;
  }

  public void setResCount(Integer resCount) {
    this.resCount = resCount;
  }

  public Boolean getShowDel() {
    return showDel;
  }

  public void setShowDel(Boolean showDel) {
    this.showDel = showDel;
  }



}
