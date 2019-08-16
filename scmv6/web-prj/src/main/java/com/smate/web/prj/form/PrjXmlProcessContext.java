package com.smate.web.prj.form;

import com.smate.web.prj.consts.PrjXmlOperationEnum;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;

import java.util.Locale;

/**
 * XML组件上下文对象. 这个只能适用于项目.
 * 
 * @author liqinghua
 * 
 */
public class PrjXmlProcessContext {

  /**
   * 当前项目ID.
   */
  private Long currentPrjId;
  /**
   * .当前项目的加密ID
   */
  private String currentDes3PrjId;
  /**
   * 当前单位ID.
   */
  private Long currentInsId;
  /**
   * 当前用户ID.
   */
  private Long currentUserId;
  /**
   * 当前语言.
   */
  private String currentLanguage;
  /**
   * 当前操作:onlineImport/fileImport / create / edit / enter .
   */
  private PrjXmlOperationEnum currentAction;
  /**
   * 当前语言.
   */
  private Locale locale;
  /**
   * 查重后的操作 skip:跳过，refresh:更新， add：新增， no_dup:没有查到重复项目.
   */
  private String dupOperation;

  private PrjInfoDTO prjInfo ;  // 文件模板导入的数据

  public PrjInfoDTO getPrjInfo() {
    return prjInfo;
  }

  public void setPrjInfo(PrjInfoDTO prjInfo) {
    this.prjInfo = prjInfo;
  }

  public Long getCurrentInsId() {
    return currentInsId;
  }

  public void setCurrentInsId(Long currentInsId) {
    this.currentInsId = currentInsId;
  }


  public Long getCurrentUserId() {
    return currentUserId;
  }


  public void setCurrentUserId(Long currentUserId) {
    this.currentUserId = currentUserId;
  }


  public String getCurrentLanguage() {
    return currentLanguage;
  }

  public void setCurrentLanguage(String currentLanguage) {
    this.currentLanguage = currentLanguage;
  }

  public PrjXmlOperationEnum getCurrentAction() {
    return currentAction;
  }

  public void setCurrentAction(PrjXmlOperationEnum currentAction) {
    this.currentAction = currentAction;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public Locale getLocale() {
    return locale;
  }

  public Long getCurrentPrjId() {
    return currentPrjId;
  }

  public void setCurrentPrjId(Long currentPrjId) {
    this.currentPrjId = currentPrjId;
  }

  public String getCurrentDes3PrjId() {
    return currentDes3PrjId;
  }

  public void setCurrentDes3PrjId(String currentDes3PrjId) {
    this.currentDes3PrjId = currentDes3PrjId;
  }

  public String getDupOperation() {
    return dupOperation;
  }

  public void setDupOperation(String dupOperation) {
    this.dupOperation = dupOperation;
  }

}
