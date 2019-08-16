package com.smate.web.group.action.grp.form;


/**
 * 群组文件标签，显示的信息
 * 
 * @author aijiangbin
 *
 */
public class GrpFileLabelShowInfo {

  public String des3FileLabelId; // 文件标签id
  public String des3GrpLabelId; // 群组标签id

  public String labelName; // 标签名
  public String des3GrpFileId;; // 文件id
  public Boolean showDel = false;// 是否显示删除按钮



  public String getDes3FileLabelId() {
    return des3FileLabelId;
  }

  public void setDes3FileLabelId(String des3FileLabelId) {
    this.des3FileLabelId = des3FileLabelId;
  }

  public String getLabelName() {
    return labelName;
  }

  public void setLabelName(String labelName) {
    this.labelName = labelName;
  }

  public String getDes3GrpFileId() {
    return des3GrpFileId;
  }

  public void setDes3GrpFileId(String des3GrpFileId) {
    this.des3GrpFileId = des3GrpFileId;
  }

  public String getDes3GrpLabelId() {
    return des3GrpLabelId;
  }

  public void setDes3GrpLabelId(String des3GrpLabelId) {
    this.des3GrpLabelId = des3GrpLabelId;
  }

  public Boolean getShowDel() {
    return showDel;
  }

  public void setShowDel(Boolean showDel) {
    this.showDel = showDel;
  }



}
