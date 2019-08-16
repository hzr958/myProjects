package com.smate.web.v8pub.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 全文上传记录表
 * 
 * @author zll
 *
 */
@Entity
@Table(name = "PUB_FULLTEXT_UPLOAD_LOG")
public class PubFulltextUploadLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3528428523657668437L;
  @Id
  @SequenceGenerator(name = "SEQ_PUB_FULLTEXT_UPLOAD_LOG", sequenceName = "SEQ_PUB_FULLTEXT_UPLOAD_LOG",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PUB_FULLTEXT_UPLOAD_LOG")
  @Column(name = "ID")
  private Long id;// 主键

  @Column(name = "UPLOAD_PSN_ID")
  private Long uploadPsnId;// 上传全文人员id

  @Column(name = "SNS_PUB_ID")
  private Long snsPubId;// 个人库成果id

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId;// 基准库成果id

  @Column(name = "FULLTEXT_FILE_ID")
  private Long fulltextFileId;// 全文id

  @Column(name = "STATUS")
  private Integer status;// 是否匹配了其他人

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 创建时间

  @Column(name = "PDWH_PUB_TO_IMAGE")
  private Integer pdwhPubToImage;// 基准库成果全文是否转成图片

  @Column(name = "SNS_PUB_TO_IMAGE")
  private Integer snsPubToImage;// 个人库成果全文是否转成图片

  @Column(name = "ERROR_MSG")
  private String errorMsg;// 转图片错误信息

  @Column(name = "IS_PRIVACY")
  private Integer isPrivacy = 0;// 是否是隐私全文

  @Column(name = "IS_DELETE")
  private Integer isDelete = 0;// 全文是否删除

  public PubFulltextUploadLog() {
    super();
  }

  public PubFulltextUploadLog(Long uploadPsnId, Long fulltextFileId, Integer Status, Date gmtCreate,
      Integer pdwhPubToImage, Integer snsPubToImage) {
    this.uploadPsnId = uploadPsnId;
    this.fulltextFileId = fulltextFileId;
    this.status = Status;
    this.gmtCreate = gmtCreate;
    this.pdwhPubToImage = pdwhPubToImage;
    this.snsPubToImage = snsPubToImage;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUploadPsnId() {
    return uploadPsnId;
  }

  public void setUploadPsnId(Long uploadPsnId) {
    this.uploadPsnId = uploadPsnId;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Long getFulltextFileId() {
    return fulltextFileId;
  }

  public void setFulltextFileId(Long fulltextFileId) {
    this.fulltextFileId = fulltextFileId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Integer getPdwhPubToImage() {
    return pdwhPubToImage;
  }

  public void setPdwhPubToImage(Integer pdwhPubToImage) {
    this.pdwhPubToImage = pdwhPubToImage;
  }

  public Integer getSnsPubToImage() {
    return snsPubToImage;
  }

  public void setSnsPubToImage(Integer snsPubToImage) {
    this.snsPubToImage = snsPubToImage;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Integer getIsPrivacy() {
    return isPrivacy;
  }

  public void setIsPrivacy(Integer isPrivacy) {
    this.isPrivacy = isPrivacy;
  }

  public Integer getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }

}
