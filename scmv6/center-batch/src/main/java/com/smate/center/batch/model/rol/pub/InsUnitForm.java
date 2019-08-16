package com.smate.center.batch.model.rol.pub;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.sns.pub.PsnHtml;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 部门实体,供action使用.
 * 
 * @author liqinghua
 * 
 */
public class InsUnitForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2528741621604353667L;
  private Long unitId;
  // 合并部门，保留的部门
  private Long toUnitId;
  private String unitName;
  private String des3UnitId;
  private String des3UnitIds;// 部门批量删除使用
  private String insName;
  private Long insId;
  private Long superId;
  private String superName;
  private String name;
  private String zhName;
  private String enName;
  private String abbr;
  private String insUnitTr;
  private String unitTree;
  private List<InsUnit> insUnitList;
  private String forwardUrl;
  private String errorMsg;
  private String message;
  private String queryUnitName;
  private Long queryUnitId;
  private String psnName;
  private String psnIds;
  private List<RolPsnIns> psnInsList;
  private boolean hasAddUnit = false;
  // 导入部门
  private File xlsFile;
  private String xlsFileContentType;
  private String xlsFileFileName;
  private List<Map<String, String>> unitList;
  // 导入部门，正确导入列表
  private List<InsUnitForm> successList;
  // 导入部门，错误导入列表
  private List<InsUnitForm> errorList;
  // 是否院部门
  private boolean collegeUnit;
  // 是否常用人员
  private Integer cyFlag;
  private boolean showSelectUnit = true;

  private int roleId;
  private String unitUrl;

  private List<PsnHtml> psnHtml;

  private int tempCode;
  private String mark;

  public InsUnitForm() {
    super();
  }

  public InsUnitForm(String zhName, String enName, String abbr, String superName) {
    super();
    this.superName = superName;
    this.zhName = zhName;
    this.enName = enName;
    this.abbr = abbr;
  }

  public InsUnitForm(String zhName, String enName, String abbr, String superName, String unitUrl) {
    super();
    this.superName = superName;
    this.zhName = zhName;
    this.enName = enName;
    this.abbr = abbr;
    this.unitUrl = unitUrl;
  }

  public InsUnitForm(String zhName, String enName, Long insId, Long superInsUnitId) {
    super();
    this.zhName = zhName;
    this.enName = enName;
    this.insId = insId;
    this.superId = superInsUnitId;
  }

  public void copyForm(InsUnitForm result) {

    this.unitId = result.getUnitId();
    this.toUnitId = result.getToUnitId();
    this.unitName = result.getUnitName();
    this.insName = result.getInsName();
    this.insId = result.getInsId();
    this.superId = result.getSuperId();
    this.superName = result.getSuperName();
    this.name = result.getName();
    this.zhName = result.getZhName();
    this.enName = result.getEnName();
    this.abbr = result.getAbbr();
    this.unitTree = result.getUnitTree();
    this.insUnitList = result.getInsUnitList();
    this.psnName = result.getPsnName();
    this.psnIds = result.getPsnIds();
    this.psnInsList = result.getPsnInsList();
    this.hasAddUnit = result.isHasAddUnit();
    this.successList = result.getSuccessList();
    this.errorList = result.getErrorList();
    this.showSelectUnit = result.isShowSelectUnit();
    this.unitUrl = result.getUnitUrl();
  }

  public List<Map<String, String>> getUnitList() {
    return unitList;
  }

  public void setUnitList(List<Map<String, String>> unitList) {
    this.unitList = unitList;
  }

  public List<InsUnit> getInsUnitList() {
    return insUnitList;
  }

  public void setInsUnitList(List<InsUnit> insUnitList) {
    this.insUnitList = insUnitList;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAbbr() {
    return abbr;
  }

  public void setAbbr(String abbr) {
    this.abbr = abbr;
  }

  public String getDes3UnitId() {

    if (this.unitId != null && des3UnitId == null) {
      des3UnitId = ServiceUtil.encodeToDes3(this.unitId.toString());
    }
    return des3UnitId;
  }

  public void setDes3UnitId(String des3UnitId) {
    this.des3UnitId = des3UnitId;
  }

  public String getInsUnitTr() {
    return insUnitTr;
  }

  public void setInsUnitTr(String insUnitTr) {
    this.insUnitTr = insUnitTr;
  }

  public Long getSuperId() {
    return superId;
  }

  public void setSuperId(Long superId) {
    this.superId = superId;
  }

  public String getSuperName() {
    return superName;
  }

  public void setSuperName(String superName) {
    this.superName = superName;
  }

  public String getUnitTree() {
    return unitTree;
  }

  public void setUnitTree(String unitTree) {
    this.unitTree = unitTree;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public String getInsName() {
    return insName;
  }

  public String getZhName() {
    return zhName;
  }

  public String getEnName() {
    return enName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getInsUnitListSize() {
    if (insUnitList == null)
      return 0;
    return insUnitList.size();
  }

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public Long getQueryUnitId() {
    return queryUnitId;
  }

  public void setQueryUnitId(Long queryUnitId) {
    this.queryUnitId = queryUnitId;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public List<RolPsnIns> getPsnInsList() {
    return psnInsList;
  }

  public void setPsnInsList(List<RolPsnIns> psnInsList) {
    this.psnInsList = psnInsList;
  }

  public int getPsnInsListSize() {
    if (this.psnInsList == null) {
      return 0;
    }
    return psnInsList.size();
  }

  public String getQueryUnitName() {
    return queryUnitName;
  }

  public void setQueryUnitName(String queryUnitName) {
    this.queryUnitName = queryUnitName;
  }

  public String getPsnIds() {
    return psnIds;
  }

  public void setPsnIds(String psnIds) {
    this.psnIds = psnIds;
  }

  public Long getToUnitId() {
    return toUnitId;
  }

  public void setToUnitId(Long toUnitId) {
    this.toUnitId = toUnitId;
  }

  public File getXlsFile() {
    return xlsFile;
  }

  public String getXlsFileContentType() {
    return xlsFileContentType;
  }

  public String getXlsFileFileName() {
    return xlsFileFileName;
  }

  public void setXlsFile(File xlsFile) {
    this.xlsFile = xlsFile;
  }

  public void setXlsFileContentType(String xlsFileContentType) {
    this.xlsFileContentType = xlsFileContentType;
  }

  public void setXlsFileFileName(String xlsFileFileName) {
    this.xlsFileFileName = xlsFileFileName;
  }

  public boolean isHasAddUnit() {
    return hasAddUnit;
  }

  public void setHasAddUnit(boolean hasAddUnit) {
    this.hasAddUnit = hasAddUnit;
  }

  public List<InsUnitForm> getSuccessList() {
    return successList;
  }

  public List<InsUnitForm> getErrorList() {
    return errorList;
  }

  public void setSuccessList(List<InsUnitForm> successList) {
    this.successList = successList;
  }

  public void setErrorList(List<InsUnitForm> errorList) {
    this.errorList = errorList;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public int getSuccessListSize() {
    if (successList == null)
      return 0;
    return successList.size();
  }

  public int getErrorListSize() {
    if (errorList == null)
      return 0;
    return errorList.size();
  }

  /**
   * @return the collegeUnit
   */
  public boolean isCollegeUnit() {
    return collegeUnit;
  }

  /**
   * @param collegeUnit the collegeUnit to set
   */
  public void setCollegeUnit(boolean collegeUnit) {
    this.collegeUnit = collegeUnit;
  }

  public Integer getCyFlag() {
    return cyFlag;
  }

  public void setCyFlag(Integer cyFlag) {
    this.cyFlag = cyFlag;
  }

  public boolean isShowSelectUnit() {
    return showSelectUnit;
  }

  public void setShowSelectUnit(boolean showSelectUnit) {
    this.showSelectUnit = showSelectUnit;
  }

  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  public String getUnitUrl() {
    return unitUrl;
  }

  public void setUnitUrl(String unitUrl) {
    this.unitUrl = unitUrl;
  }

  public List<PsnHtml> getPsnHtml() {
    return psnHtml;
  }

  public void setPsnHtml(List<PsnHtml> psnHtml) {
    this.psnHtml = psnHtml;
  }

  public int getTempCode() {
    return tempCode;
  }

  public void setTempCode(int tempCode) {
    this.tempCode = tempCode;
  }

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

  public String getDes3UnitIds() {
    return des3UnitIds;
  }

  public void setDes3UnitIds(String des3UnitIds) {
    this.des3UnitIds = des3UnitIds;
  }

}
