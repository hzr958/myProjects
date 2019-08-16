package com.smate.sie.core.base.utils.model.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.string.StringUtils;

/**
 * 专利
 * 
 * @author hd
 *
 */
@Entity
@Table(name = "PATENT")
public class SiePatent implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1431956135353411351L;
  // 主键
  private Long patId;
  // 单位ID
  private Long insId;
  // 数据源，对应const_ref_db
  private Integer dbId;
  // 专利标题
  private String zhTitle;
  // 英文标题
  private String enTitle;
  // 专利类型：const_pat_type.type_id
  private Integer patType;
  // 法律状态，const_dictionary.category=pub_legal_status
  private String levelStatus;
  // 申请号/专利号
  private String applyNo;
  // 授权号
  private String authNo;
  // 申请年度
  private Integer applyYear;
  // 申请月份
  private Integer applyMonth;
  // 申请日
  private Integer applyDay;
  // 授权年度
  private Integer authYear;
  // 授权月份
  private Integer authMonth;
  // 授权日
  private Integer authDay;
  // 证书编号
  private String ceritfNo;
  // 发明成员姓名，逗号分割
  private String authors;
  // 流程状态，const_dictionary.category=status
  private String status;
  // 最后编辑时间
  private Date updateDt;
  // 最后编辑人
  private Long upPsnId;
  // 创建时间
  private Date createDt;
  // 创建人
  private Long createPsnId;
  // 数据来源：0表单新增，1xls导入，2标准文件导入，3联邦检索，4基准库指派，9脚本修复
  private Integer dataFrom;
  // 记录是否完整：0不完整，1完整
  private Integer isVaild;
  // 来源（中文）
  private String briefDesc;
  // 来源（英文）
  private String briefDescEn;
  // 部门名称
  private String unitname;
  // 是否公开：0不公开1公开，默认1
  private int isPublic = 1;

  private String title;
  private String brief;
  private Long readNum = 0L;
  private Long downloadNum = 0L;
  private String tranAmount = "0.00";

  private String fulltextUrl;
  private String fulltextImgPath;
  private String fulltextFileid; // 全文文件的ID
  private String fullTextDownloadUrl; // 全文图片的下载地址
  private String fulltextImagePath; // 全文图片的路径
  private Integer fulltextFileType; // 全文类型，3为基准库，6为人员在SIE中上传的

  // 查重时，是否新增
  private int isJnlInsert = 0;
  private boolean pdwhImportStatus = false;

  public SiePatent() {
    super();
  }



  public SiePatent(Long patId, String zhTitle, String enTitle) {
    super();
    this.patId = patId;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
  }



  public SiePatent(Long patId, String authors, String zhTitle, String enTitle, String briefDesc, String briefDescEn) {
    super();
    this.patId = patId;
    this.authors = authors;
    this.zhTitle = zhTitle;
    this.enTitle = enTitle;
    this.briefDesc = briefDesc;
    this.briefDescEn = briefDescEn;
    this.title = getTitle();
    this.brief = getBrief();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUBLICATION", allocationSize = 1)
  @Column(name = "PAT_ID")
  public Long getPatId() {
    return patId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "DB_ID")
  public Integer getDbId() {
    return dbId;
  }

  @Column(name = "ZH_TITLE")
  public String getZhTitle() {
    return zhTitle;
  }

  @Column(name = "EN_TITLE")
  public String getEnTitle() {
    return enTitle;
  }

  @Column(name = "PAT_TYPE")
  public Integer getPatType() {
    return patType;
  }

  @Column(name = "LEGAL_STATUS")
  public String getLevelStatus() {
    return levelStatus;
  }

  @Column(name = "APPLY_NO")
  public String getApplyNo() {
    return applyNo;
  }

  @Column(name = "AUTH_NO")
  public String getAuthNo() {
    return authNo;
  }

  @Column(name = "APPLY_YEAR")
  public Integer getApplyYear() {
    return applyYear;
  }

  @Column(name = "APPLY_MONTH")
  public Integer getApplyMonth() {
    return applyMonth;
  }

  @Column(name = "APPLY_DAY")
  public Integer getApplyDay() {
    return applyDay;
  }

  @Column(name = "AUTH_YEAR")
  public Integer getAuthYear() {
    return authYear;
  }

  @Column(name = "AUTH_MONTH")
  public Integer getAuthMonth() {
    return authMonth;
  }

  @Column(name = "AUTH_DAY")
  public Integer getAuthDay() {
    return authDay;
  }

  @Column(name = "CERITFICATE_NO")
  public String getCeritfNo() {
    return ceritfNo;
  }

  @Column(name = "AUTHOR_NAMES")
  public String getAuthors() {
    return authors;
  }

  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDt() {
    return updateDt;
  }

  @Column(name = "UPDATE_PSN_ID")
  public Long getUpPsnId() {
    return upPsnId;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDt() {
    return createDt;
  }

  @Column(name = "CREATE_PSN_ID")
  public Long getCreatePsnId() {
    return createPsnId;
  }

  @Column(name = "DATA_FROM")
  public Integer getDataFrom() {
    return dataFrom;
  }

  @Column(name = "IS_VALID")
  public Integer getIsVaild() {
    return isVaild;
  }

  @Column(name = "BRIEF_DESC")
  public String getBriefDesc() {
    return briefDesc;
  }

  @Column(name = "BRIEF_DESC_EN")
  public String getBriefDescEn() {
    return briefDescEn;
  }


  @Transient
  public String getUnitname() {
    return unitname;
  }

  @Transient
  public String getFulltextImgPath() {
    return fulltextImgPath;
  }

  @Transient
  public String getTitle() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if (StringUtils.isBlank(title)) {
      if ("zh".equals(language)) {
        this.title = StringUtils.isNotBlank(zhTitle) ? zhTitle : enTitle;
      } else {
        this.title = StringUtils.isNotBlank(enTitle) ? enTitle : zhTitle;
      }
    }
    return title;
  }

  @Transient
  public String getBrief() {
    String language = LocaleContextHolder.getLocale().getLanguage();
    if (StringUtils.isBlank(brief)) {
      if ("zh".equals(language)) {
        this.brief = StringUtils.isNotBlank(briefDesc) ? briefDesc : briefDescEn;
      } else {
        this.brief = StringUtils.isNotBlank(briefDescEn) ? briefDescEn : briefDesc;
      }
    }
    return brief;
  }

  @Transient
  public Long getReadNum() {
    return readNum;
  }

  @Transient
  public Long getDownloadNum() {
    return downloadNum;
  }

  @Transient
  public String getTranAmount() {
    return tranAmount;
  }

  @Transient
  public int getIsJnlInsert() {
    return isJnlInsert;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public void setEnTitle(String enTitle) {
    this.enTitle = enTitle;
  }

  public void setPatType(Integer patType) {
    this.patType = patType;
  }

  public void setLevelStatus(String levelStatus) {
    this.levelStatus = levelStatus;
  }

  public void setApplyNo(String applyNo) {
    this.applyNo = applyNo;
  }

  public void setAuthNo(String authNo) {
    this.authNo = authNo;
  }

  public void setApplyYear(Integer applyYear) {
    this.applyYear = applyYear;
  }

  public void setApplyMonth(Integer applyMonth) {
    this.applyMonth = applyMonth;
  }

  public void setApplyDay(Integer applyDay) {
    this.applyDay = applyDay;
  }

  public void setAuthYear(Integer authYear) {
    this.authYear = authYear;
  }

  public void setAuthMonth(Integer authMonth) {
    this.authMonth = authMonth;
  }

  public void setAuthDay(Integer authDay) {
    this.authDay = authDay;
  }

  public void setCeritfNo(String ceritfNo) {
    this.ceritfNo = ceritfNo;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setUpdateDt(Date updateDt) {
    this.updateDt = updateDt;
  }

  public void setUpPsnId(Long upPsnId) {
    this.upPsnId = upPsnId;
  }

  public void setCreateDt(Date createDt) {
    this.createDt = createDt;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public void setDataFrom(Integer dataFrom) {
    this.dataFrom = dataFrom;
  }

  public void setIsVaild(Integer isVaild) {
    this.isVaild = isVaild;
  }

  public void setBriefDesc(String briefDesc) {
    this.briefDesc = briefDesc;
  }

  public void setBriefDescEn(String briefDescEn) {
    this.briefDescEn = briefDescEn;
  }


  public void setUnitname(String unitname) {
    this.unitname = unitname;
  }

  public void setFulltextImgPath(String fulltextImgPath) {
    this.fulltextImgPath = fulltextImgPath;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }

  public void setReadNum(Long readNum) {
    this.readNum = readNum;
  }

  public void setDownloadNum(Long downloadNum) {
    this.downloadNum = downloadNum;
  }

  public void setTranAmount(String tranAmount) {
    this.tranAmount = tranAmount;
  }

  public void setIsJnlInsert(int isJnlInsert) {
    this.isJnlInsert = isJnlInsert;
  }

  @Column(name = "IS_PUBLIC")
  public int getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(int isPublic) {
    this.isPublic = isPublic;
  }

  @Column(name = "FULLTEXT_FILEID")
  public String getFulltextFileid() {
    return fulltextFileid;
  }

  @Transient
  public String getFullTextDownloadUrl() {
    return fullTextDownloadUrl;
  }

  @Transient
  public String getFulltextImagePath() {
    return fulltextImagePath;
  }

  @Transient
  public Integer getFulltextFileType() {
    return fulltextFileType;
  }

  public void setFulltextFileid(String fulltextFileid) {
    this.fulltextFileid = fulltextFileid;
  }

  public void setFullTextDownloadUrl(String fullTextDownloadUrl) {
    this.fullTextDownloadUrl = fullTextDownloadUrl;
  }

  public void setFulltextImagePath(String fulltextImagePath) {
    this.fulltextImagePath = fulltextImagePath;
  }

  public void setFulltextFileType(Integer fulltextFileType) {
    this.fulltextFileType = fulltextFileType;
  }

  @Column(name = "FULLTEXT_URL")
  public String getFulltextUrl() {
    return fulltextUrl;
  }

  public void setFulltextUrl(String fulltextUrl) {
    this.fulltextUrl = fulltextUrl;
  }

  @Transient
  public boolean getPdwhImportStatus() {
    return pdwhImportStatus;
  }

  public void setPdwhImportStatus(boolean pdwhImportStatus) {
    this.pdwhImportStatus = pdwhImportStatus;
  }


}
