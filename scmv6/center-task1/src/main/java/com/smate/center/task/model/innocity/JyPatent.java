package com.smate.center.task.model.innocity;

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

import org.apache.commons.lang3.StringUtils;


/**
 * 专利实体
 * 
 * @author yxs
 *
 * @since 2016年6月12日
 */

@Entity
@Table(name = "JY_PATENTS")
public class JyPatent implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -3429605333336442318L;
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_JY_PATENTS_ID", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long id;// 专利主键
  @Column(name = "ZH_TITLE")
  private String zhTitle;// 专利名称
  @Column(name = "ABSTRACT")
  private String abStract;// 摘要
  @Column(name = "ZH_KEYWORDS")
  private String zhKeyWords;// 关键字
  @Column(name = "PATENT_TYPE")
  private int patentType;// 专利分类
  @Column(name = "CLASSIFY_ID")
  private Long classifyId;// 行业ID
  @Column(name = "OWNER_TYPE")
  private int ownerType;// 专利所有人类型
  @Column(name = "OWNER_NAME")
  private String ownerName;// 专利所有人名称
  @Column(name = "AUTHOR_FILE_ID")
  private Long authorFileId;// 专利转让授权书文件编号
  @Column(name = "AUTHOR_FILE_ID_PATH")
  private String authorFileIdPath;// 专利转让授权书文件路径
  @Column(name = "PATENT_NO")
  private String patentNo;// 申请（专利）号
  @Column(name = "PATENT_OPEN_NO")
  private String patentOpenNo;// 公开（公告）号
  @Column(name = "IPC_CODE")
  private String ipcCode;// 主分类号
  @Column(name = "BEGIN_DATE")
  private Date beginDate;// 生效日期-开始日期
  @Column(name = "END_DATE")
  private Date endDate;// 生效日期-结束日期
  @Column(name = "PATENT_ORG")
  private String patentOrg;// 发证单位
  @Column(name = "COUNTRY_CODE")
  private Long countryCode;// 国家或地区编码
  @Column(name = "COUNTRY_CODE_NAME")
  private String countryCodeName;// 国家或地区名称
  @Column(name = "FULL_FILE_ID")
  private Long fullFileId;// 专利全文文件编号
  @Column(name = "FULL_FILE_ID_PATH")
  private String fullFileIdPath;// 专利全文文件路径
  @Column(name = "REMARK")
  private String remark;// 专利说明
  @Column(name = "TRANSACTION_TYPE")
  private int transactionType;// 交易类型(2、许可 3、众筹 1、转让)
  @Column(name = "PRICE")
  private Long price;// 销售价格/筹备金额
  @Column(name = "PRICE_TYPE")
  private int priceType;// 收费类型（1、货币2、面议）
  @Column(name = "IMAGE_ID")
  private Long imageId;// 专利图片编号
  @Column(name = "IMAGE_ID_PATH")
  private String imageIdPath;// 专利图片路径
  @Column(name = "PATENT_OWNER_ID")
  private Long patentOwnerId;// 专利所属人编号
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间
  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 更新时间
  @Column(name = "AUDIT_PSN_ID")
  private Long auditPsnId;// 审核人编号
  @Column(name = "AUDIT_DATE")
  private Date auditDate;// 审核时期
  @Column(name = "STATUS")
  private int status;// 状态
  @Column(name = "AUDIT_OPINION")
  private String auditOpinion;// 审核人编号
  @Column(name = "PSN_NAME")
  private String psnName;// 创建人名称
  @Column(name = "INS_NAME")
  private String insName;// 单位名称
  @Column(name = "MAXPRICE")
  private String maxPrice;// 筹备上限
  @Column(name = "DAYS")
  private int days;// 筹备天数
  @Column(name = "CSFUND_RESULT")
  private int csfundResult;// 众筹结果（1、众筹成功（待打款）2、众筹失败（待打款）3、众筹成功（已打款）4、众筹失败（已打款））
  @Column(name = "CSFUND_RESULT_REMARK")
  private String csfundResultRemark;// 众筹结果备注
  @Column(name = "PUB_ID")
  private String pubId;// SCM成果ID，用于专利交易，默认类型为许可
  @Column(name = "PRJ_ID")
  private String prjId;// SCM项目ID,用于项目众筹
  @Column(name = "CLASSIFY_IDS")
  private String classifyIds;// 行业ID,逗号隔开，如：1,2,3
  @Column(name = "CLASSIFY_IDS_NAME")
  private String classifyIdsName;// 行业分类名称,分号隔开，如：农业;生物;能源
  @Column(name = "EXPECTED_REVENUE")
  private Long expectedRevenue;// 期望收益
  @Column(name = "IDENT_CARD_FILE_ID")
  private Long identCardFileId;// 身份证fileID
  @Column(name = "IDENT_CARD_FILE_ID_PATH")
  private String identCardFileIdPath;// 身份证filePath
  @Column(name = "ORG_CODE_FILE_ID")
  private Long orgCodeFileId;// 企业营业执照(含组织结构代码证)fileID
  @Column(name = "ORG_CODE_FILE_ID_PATH")
  private String orgCodeFileIdPath;// 企业营业执照(含组织结构代码证)filePath
  @Column(name = "CERTIFICATE_FILE_ID")
  private Long certificateFileId;// 专利证书fileId
  @Column(name = "CERTIFICATE_FILE_ID_PATH")
  private String certificateFileIdPath;// 专利证书filePath
  @Column(name = "VALUATION_URL")
  private String valuationUrl;// 估值URL
  @Column(name = "IS_ASSESSMENT")
  private int isAssessment;// 是否进行过评估（0未评估，1已进行评估）
  @Transient
  private int orderNumber;// 订单数量
  @Transient
  private String authorFileTitle;// 授权书文件名
  @Transient
  private String fullFileTitle;// 全文文件名
  @Transient
  private int awardCount;// 赞数量
  @Transient
  private int commentTotal;// 评论总数
  @Transient
  private int isNew;// 是否显示New
  @Transient
  private String daysRemaining;// 剩余天数
  @Transient
  private String progress;// 进度
  @Transient
  private int cfundPrice;// 已筹金额
  @Transient
  private String identCardFileTitle;// 身份证文件名
  @Transient
  private String orgCodeFileTitle;// 企业营业执照文件名
  @Transient
  private String certificateFileTitle;// 专利证书文件名
  @Transient
  private String isTransactionOk;// 是否交易完成
  @Transient
  private int lookNum;// 浏览数
  @Transient
  private String sellerName;
  @Transient
  private String sellerMobile;
  @Transient
  private String sellerMail;
  @Transient
  private String totalReqs;

  public Long getId() {
    return id;
  }

  public void setId(String id) {
    if (StringUtils.isNotBlank(id)) {
      this.id = Long.valueOf(id);
    } else {
      this.id = 0L;
    }
  }

  public int getCommentTotal() {
    return commentTotal;
  }

  public void setCommentTotal(int commentTotal) {
    this.commentTotal = commentTotal;
  }

  public String getZhTitle() {
    return zhTitle;
  }

  public void setZhTitle(String zhTitle) {
    this.zhTitle = zhTitle;
  }

  public String getAbStract() {
    return abStract;
  }

  public void setAbStract(String abStract) {
    this.abStract = abStract;
  }

  public String getZhKeyWords() {
    return zhKeyWords;
  }

  public void setZhKeyWords(String zhKeyWords) {
    this.zhKeyWords = zhKeyWords;
  }

  public int getPatentType() {
    return patentType;
  }

  public void setPatentType(String patentType) {
    if (StringUtils.isNotBlank(patentType)) {
      this.patentType = Integer.valueOf(patentType);
    } else {
      this.patentType = 0;
    }
  }

  public int getOwnerType() {
    return ownerType;
  }

  public void setOwnerType(String ownerType) {
    if (StringUtils.isNotBlank(ownerType)) {
      this.ownerType = Integer.valueOf(ownerType);
    } else {
      this.ownerType = 0;
    }
  }

  public String getOwnerName() {
    return ownerName;
  }

  public void setOwnerName(String ownerName) {
    this.ownerName = ownerName;
  }

  public Long getAuthorFileId() {
    return authorFileId;
  }

  public void setAuthorFileId(String authorFileId) {
    if (StringUtils.isNotBlank(authorFileId)) {
      this.authorFileId = Long.valueOf(authorFileId);
    } else {
      this.authorFileId = 0L;
    }
  }

  public String getAuthorFileIdPath() {
    return authorFileIdPath;
  }

  public void setAuthorFileIdPath(Object authorFileIdPath) {}

  public void setAuthorFileIdPath(String authorFileIdPath) {
    this.authorFileIdPath = authorFileIdPath;
  }

  public String getPatentNo() {
    return patentNo;
  }

  public void setPatentNo(String patentNo) {
    this.patentNo = patentNo;
  }

  public String getPatentOpenNo() {
    return patentOpenNo;
  }

  public void setPatentOpenNo(String patentOpenNo) {
    this.patentOpenNo = patentOpenNo;
  }

  public String getIpcCode() {
    return ipcCode;
  }

  public void setIpcCode(String ipcCode) {
    this.ipcCode = ipcCode;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getPatentOrg() {
    return patentOrg;
  }

  public void setPatentOrg(String patentOrg) {
    this.patentOrg = patentOrg;
  }

  public Long getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    if (StringUtils.isNotBlank(countryCode)) {
      this.countryCode = Long.valueOf(countryCode);
    } else {
      this.countryCode = 0L;
    }
  }

  public String getCountryCodeName() {
    return countryCodeName;
  }

  public void setCountryCodeName(String countryCodeName) {
    this.countryCodeName = countryCodeName;
  }

  public Long getFullFileId() {
    return fullFileId;
  }

  public void setFullFileId(String fullFileId) {
    if (StringUtils.isNotBlank(fullFileId)) {
      this.fullFileId = Long.valueOf(fullFileId);
    } else {
      this.fullFileId = 0L;
    }
  }

  public String getFullFileIdPath() {
    return fullFileIdPath;
  }

  public void setFullFileIdPath(String fullFileIdPath) {
    this.fullFileIdPath = fullFileIdPath;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public int getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    if (StringUtils.isNotBlank(transactionType)) {
      this.transactionType = Integer.valueOf(transactionType);
    } else {
      this.transactionType = 0;
    }
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(String price) {
    if (StringUtils.isNotBlank(price)) {
      this.price = Long.valueOf(price);
    } else {
      this.price = 0L;
    }
  }

  public int getPriceType() {
    return priceType;
  }

  public void setPriceType(String priceType) {
    if (StringUtils.isNotBlank(priceType)) {
      this.priceType = Integer.valueOf(priceType);
    } else {
      this.priceType = 0;
    }
  }

  public Long getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    if (StringUtils.isNotBlank(imageId)) {
      this.imageId = Long.valueOf(imageId);
    } else {
      this.imageId = 0L;
    }
  }

  public String getImageIdPath() {
    return imageIdPath;
  }

  public void setImageIdPath(String imageIdPath) {
    this.imageIdPath = imageIdPath;
  }

  public Long getPatentOwnerId() {
    return patentOwnerId;
  }

  public void setPatentOwnerId(String patentOwnerId) {
    if (StringUtils.isNotBlank(patentOwnerId)) {
      this.patentOwnerId = Long.valueOf(patentOwnerId);
    } else {
      this.patentOwnerId = 0L;
    }
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Long getAuditPsnId() {
    return auditPsnId;
  }

  public void setAuditPsnId(String auditPsnId) {
    if (StringUtils.isNotBlank(auditPsnId)) {
      this.auditPsnId = Long.valueOf(auditPsnId);
    } else {
      this.auditPsnId = 0L;
    }
  }

  public Date getAuditDate() {
    return auditDate;
  }

  public void setAuditDate(Date auditDate) {
    this.auditDate = auditDate;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(String status) {
    if (StringUtils.isNotBlank(status)) {
      this.status = Integer.valueOf(status);
    } else {
      this.status = 0;
    }
  }

  public String getAuditOpinion() {
    return auditOpinion;
  }

  public void setAuditOpinion(String auditOpinion) {
    this.auditOpinion = auditOpinion;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    if (StringUtils.isNotBlank(orderNumber)) {
      this.orderNumber = Integer.valueOf(orderNumber);
    } else {
      this.orderNumber = 0;
    }
  }

  public Long getClassifyId() {
    return classifyId;
  }

  public void setClassifyId(String classifyId) {
    if (StringUtils.isNotBlank(classifyId)) {
      this.classifyId = Long.valueOf(classifyId);
    } else {
      this.classifyId = 0L;
    }
  }

  public String getAuthorFileTitle() {
    return authorFileTitle;
  }

  public void setAuthorFileTitle(String authorFileTitle) {
    this.authorFileTitle = authorFileTitle;
  }

  public String getFullFileTitle() {
    return fullFileTitle;
  }

  public void setFullFileTitle(String fullFileTitle) {
    this.fullFileTitle = fullFileTitle;
  }

  public int getAwardCount() {
    return awardCount;
  }

  public void setAwardCount(String awardCount) {
    if (StringUtils.isNotBlank(awardCount)) {
      this.awardCount = Integer.valueOf(awardCount);
    } else {
      this.awardCount = 0;
    }
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getInsName() {
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public int getIsNew() {
    return isNew;
  }

  public void setIsNew(String isNew) {
    if (StringUtils.isNotBlank(isNew)) {
      this.isNew = Integer.valueOf(isNew);
    } else {
      this.isNew = 0;
    }
  }

  public String getMaxPrice() {
    return maxPrice;
  }

  public void setMaxPrice(String maxPrice) {
    this.maxPrice = maxPrice;
  }

  public int getDays() {
    return days;
  }

  public void setDays(String days) {
    if (StringUtils.isNotBlank(days)) {
      this.days = Integer.valueOf(days);
    } else {
      this.days = 0;
    }
  }

  public String getProgress() {
    return progress;
  }

  public void setProgress(String progress) {
    this.progress = progress;
  }

  public int getCfundPrice() {
    return cfundPrice;
  }

  public void setCfundPrice(String cfundPrice) {
    if (StringUtils.isNotBlank(cfundPrice)) {
      this.cfundPrice = Integer.valueOf(cfundPrice);
    } else {
      this.cfundPrice = 0;
    }
  }

  public String getDaysRemaining() {
    return daysRemaining;
  }

  public void setDaysRemaining(String daysRemaining) {
    this.daysRemaining = daysRemaining;
  }

  public int getCsfundResult() {
    return csfundResult;
  }

  public void setCsfundResult(String csfundResult) {
    if (StringUtils.isNotBlank(csfundResult)) {
      this.csfundResult = Integer.valueOf(csfundResult);
    } else {
      this.csfundResult = 0;
    }
  }

  public String getCsfundResultRemark() {
    return csfundResultRemark;
  }

  public void setCsfundResultRemark(String csfundResultRemark) {
    this.csfundResultRemark = csfundResultRemark;
  }

  public String getPubId() {
    return pubId;
  }

  public void setPubId(String pubId) {
    this.pubId = pubId;
  }

  public String getPrjId() {
    return prjId;
  }

  public void setPrjId(String prjId) {
    this.prjId = prjId;
  }

  public String getClassifyIds() {
    return classifyIds;
  }

  public void setClassifyIds(Object classifyIds) {}

  public void setClassifyIds(String classifyIds) {
    this.classifyIds = classifyIds;
  }

  public String getClassifyIdsName() {
    return classifyIdsName;
  }

  public void setClassifyIdsName(Object classifyIdsName) {}

  public void setClassifyIdsName(String classifyIdsName) {
    this.classifyIdsName = classifyIdsName;
  }

  public Long getExpectedRevenue() {
    return expectedRevenue;
  }

  public void setExpectedRevenue(String expectedRevenue) {
    if (StringUtils.isNotBlank(expectedRevenue)) {
      this.expectedRevenue = Long.valueOf(expectedRevenue);
    } else {
      this.expectedRevenue = 0L;
    }
  }

  public Long getIdentCardFileId() {
    return identCardFileId;
  }

  public void setIdentCardFileId(String identCardFileId) {
    if (StringUtils.isNotBlank(identCardFileId)) {
      this.identCardFileId = Long.valueOf(identCardFileId);
    } else {
      this.identCardFileId = 0L;
    }
  }

  public String getIdentCardFileIdPath() {
    return identCardFileIdPath;
  }

  public void setIdentCardFileIdPath(String identCardFileIdPath) {
    this.identCardFileIdPath = identCardFileIdPath;
  }

  public Long getOrgCodeFileId() {
    return orgCodeFileId;
  }

  public void setOrgCodeFileId(String orgCodeFileId) {
    if (StringUtils.isNotBlank(orgCodeFileId)) {
      this.orgCodeFileId = Long.valueOf(orgCodeFileId);
    } else {
      this.orgCodeFileId = 0L;
    }
  }

  public String getOrgCodeFileIdPath() {
    return orgCodeFileIdPath;
  }

  public void setOrgCodeFileIdPath(String orgCodeFileIdPath) {
    this.orgCodeFileIdPath = orgCodeFileIdPath;
  }

  public Long getCertificateFileId() {
    return certificateFileId;
  }

  public void setCertificateFileId(String certificateFileId) {
    if (StringUtils.isNotBlank(certificateFileId)) {
      this.certificateFileId = Long.valueOf(certificateFileId);
    } else {
      this.certificateFileId = 0L;
    }
  }

  public String getCertificateFileIdPath() {
    return certificateFileIdPath;
  }

  public void setCertificateFileIdPath(String certificateFileIdPath) {
    this.certificateFileIdPath = certificateFileIdPath;
  }

  public String getIdentCardFileTitle() {
    return identCardFileTitle;
  }

  public void setIdentCardFileTitle(String identCardFileTitle) {
    this.identCardFileTitle = identCardFileTitle;
  }

  public String getOrgCodeFileTitle() {
    return orgCodeFileTitle;
  }

  public void setOrgCodeFileTitle(String orgCodeFileTitle) {
    this.orgCodeFileTitle = orgCodeFileTitle;
  }

  public String getCertificateFileTitle() {
    return certificateFileTitle;
  }

  public void setCertificateFileTitle(String certificateFileTitle) {
    this.certificateFileTitle = certificateFileTitle;
  }

  public String getValuationUrl() {
    return valuationUrl;
  }

  public void setValuationUrl(String valuationUrl) {
    this.valuationUrl = valuationUrl;
  }

  public int getIsAssessment() {
    return isAssessment;
  }

  public void setIsAssessment(String isAssessment) {
    if (StringUtils.isNotBlank(isAssessment)) {
      this.isAssessment = Integer.valueOf(isAssessment);
    } else {
      this.isAssessment = 0;
    }
  }

  public String getIsTransactionOk() {
    return isTransactionOk;
  }

  public void setIsTransactionOk(String isTransactionOk) {
    this.isTransactionOk = isTransactionOk;
  }

  public int getLookNum() {
    return lookNum;
  }

  public void setLookNum(int lookNum) {
    this.lookNum = lookNum;
  }

  public String getSellerName() {
    return sellerName;
  }

  public void setSellerName(String sellerName) {
    this.sellerName = sellerName;
  }

  public String getSellerMobile() {
    return sellerMobile;
  }

  public void setSellerMobile(String sellerMobile) {
    this.sellerMobile = sellerMobile;
  }

  public String getSellerMail() {
    return sellerMail;
  }

  public void setSellerMail(String sellerMail) {
    this.sellerMail = sellerMail;
  }

  public String getTotalReqs() {
    return totalReqs;
  }

  public void setTotalReqs(String totalReqs) {
    this.totalReqs = totalReqs;
  }


}
