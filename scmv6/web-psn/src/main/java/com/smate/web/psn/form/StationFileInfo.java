package com.smate.web.psn.form;

import com.smate.core.base.psn.model.StationFile;

/**
 * 个人文件表单
 * 
 * @author AiJiangBin
 *
 */
public class StationFileInfo extends StationFile {

  /**
   * 
   */
  private static final long serialVersionUID = 3453096349976683645L;

  private Boolean existsGrpFile = false; // 是否是群组文件

  public Boolean getExistsGrpFile() {
    return existsGrpFile;
  }

  public void setExistsGrpFile(Boolean existsGrpFile) {
    this.existsGrpFile = existsGrpFile;
  }

}
