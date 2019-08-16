package com.smate.web.group.action.grp.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.model.grp.label.GrpLabel;

/**
 * 群组标签form表单
 * 
 * @author aijiangbin
 *
 */
public class GrpLabelForm {

  public Long psnId;
  public String des3PsnId;
  public Long grpId;
  public String des3GrpId;

  public String labelName; // 群组标签名
  public Long labelId;// 标签id
  public String des3LabelId;

  public Long grpFileId;
  public String des3GrpFileId;

  public Long fileLabelId;// 文件标签id
  public String des3FileLabelId;

  public Integer fileModuleType = 0; // 文件所属模块[0: 群组文件;1: 作业;2: 教学课件]

  public List<GrpLabelShowInfo> grpLabelShowInfoList = new ArrayList<>();

  public Integer result = 1;

  public Long getPsnId() {
    if (psnId == null || psnId == 0L) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getGrpId() {
    if ((grpId == null || grpId == 0L) && StringUtils.isNotBlank(des3GrpId)) {
      grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3GrpId));
      Long.getLong("");
    }
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getDes3GrpId() {
    return des3GrpId;
  }


  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }



  public String getLabelName() {
    if (this.labelName != null) {
      this.labelName = labelName.trim();
      if (this.labelName.length() > 30) {
        this.labelName = this.labelName.substring(0, 30);
      }
    }
    return this.labelName;
  }

  public void setLabelName(String labelName) {
    this.labelName = labelName;
  }



  public Long getLabelId() {
    if (labelId == null && StringUtils.isNotBlank(this.des3LabelId)) {
      labelId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3LabelId), 0L);
    }
    return labelId;
  }

  public void setLabelId(Long labelId) {
    this.labelId = labelId;
  }

  public String getDes3LabelId() {
    return des3LabelId;
  }

  public void setDes3LabelId(String des3LabelId) {
    this.des3LabelId = des3LabelId;
  }

  public Long getGrpFileId() {
    if (grpFileId == null && StringUtils.isNotBlank(this.des3GrpFileId)) {
      grpFileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3GrpFileId), 0L);
    }
    return grpFileId;
  }

  public void setGrpFileId(Long grpFileId) {
    this.grpFileId = grpFileId;
  }

  public String getDes3GrpFileId() {
    return des3GrpFileId;
  }

  public void setDes3GrpFileId(String des3GrpFileId) {
    this.des3GrpFileId = des3GrpFileId;
  }

  public Long getFileLabelId() {
    if (fileLabelId == null && StringUtils.isNotBlank(this.des3FileLabelId)) {
      fileLabelId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3FileLabelId), 0L);
    }
    return fileLabelId;
  }

  public void setFileLabelId(Long fileLabelId) {
    this.fileLabelId = fileLabelId;
  }

  public String getDes3FileLabelId() {
    return des3FileLabelId;
  }

  public void setDes3FileLabelId(String des3FileLabelId) {
    this.des3FileLabelId = des3FileLabelId;
  }

  public Integer getFileModuleType() {
    return fileModuleType;
  }

  public void setFileModuleType(Integer fileModuleType) {
    this.fileModuleType = fileModuleType;
  }

  public List<GrpLabelShowInfo> getGrpLabelShowInfoList() {
    return grpLabelShowInfoList;
  }

  public void setGrpLabelShowInfoList(List<GrpLabelShowInfo> grpLabelShowInfoList) {
    this.grpLabelShowInfoList = grpLabelShowInfoList;
  }

  public Integer getResult() {
    return result;
  }

  public void setResult(Integer result) {
    this.result = result;
  }



}
