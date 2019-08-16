package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 动态与科研之友用户的关系表
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * 
 */
@Document(collection = "InspgDynRelationPsn")
public class InspgDynRelationPsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3553999752950693639L;
  @Id
  private String id; // 动态数据id
  @Indexed
  private String dynId; // 动态id 与 InspgDynamic中的id对应
  private Long producerInspgId; // 动态产生相关人所属机构id
  private Long producerPsnId; // 动态产生相关人id
  private Long consumerInspgId; // 此动态对应人的Id
  private Integer dynType; // 动态类型
  private Long createTime; // 动态刷新时间
  private Date completeTime; // 动态创建完成的时间
  private Integer status; // 默认为1; 1.表示动态正常;0.表示动态被删除
  // 与以下动态连用(此处无需持久化)，构造所需数据；
  @Transient
  private InspgDynamicFileShare inspgDynamicFileShare;
  @Transient
  private InspgDynamicLinkShare inspgDynamicLinkShare;
  @Transient
  private InspgDynamicMemberAdd inspgDynamicMemberAdd;
  @Transient
  private InspgDynamicImgUpload inspgDynamicImgUpload;
  @Transient
  private InspgDynamicNewsAdd inspgDynamicNewsAdd;


  public InspgDynRelationPsn() {
    super();
  }

  public InspgDynRelationPsn(String id, String dynId, Long producerInspgId, Long producerPsnId, Long consumerInspgId,
      Integer dynType, Long createTime, Date completeTime, Integer status) {
    super();
    this.id = id;
    this.dynId = dynId;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
  }

  public InspgDynRelationPsn(String id, String dynId, Long producerInspgId, Long producerPsnId, Long consumerInspgId,
      Integer dynType, Long createTime, Date completeTime, Integer status,
      InspgDynamicFileShare inspgDynamicFileShare) {
    super();
    this.id = id;
    this.dynId = dynId;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicFileShare = inspgDynamicFileShare;
  }

  public InspgDynRelationPsn(String id, String dynId, Long producerInspgId, Long producerPsnId, Long consumerInspgId,
      Integer dynType, Long createTime, Date completeTime, Integer status,
      InspgDynamicLinkShare inspgDynamicLinkShare) {
    super();
    this.id = id;
    this.dynId = dynId;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicLinkShare = inspgDynamicLinkShare;
  }

  public InspgDynRelationPsn(String id, String dynId, Long producerInspgId, Long producerPsnId, Long consumerInspgId,
      Integer dynType, Long createTime, Date completeTime, Integer status,
      InspgDynamicMemberAdd inspgDynamicMemberAdd) {
    super();
    this.id = id;
    this.dynId = dynId;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicMemberAdd = inspgDynamicMemberAdd;
  }

  public InspgDynRelationPsn(String id, String dynId, Long producerInspgId, Long producerPsnId, Long consumerInspgId,
      Integer dynType, Long createTime, Date completeTime, Integer status,
      InspgDynamicImgUpload inspgDynamicImgUpload) {
    super();
    this.id = id;
    this.dynId = dynId;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicImgUpload = inspgDynamicImgUpload;
  }

  public InspgDynRelationPsn(String id, String dynId, Long producerInspgId, Long producerPsnId, Long consumerInspgId,
      Integer dynType, Long createTime, Date completeTime, Integer status, InspgDynamicNewsAdd inspgDynamicNewsAdd) {
    super();
    this.id = id;
    this.dynId = dynId;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicNewsAdd = inspgDynamicNewsAdd;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDynId() {
    return dynId;
  }

  public void setDynId(String dynId) {
    this.dynId = dynId;
  }

  public Long getProducerInspgId() {
    return producerInspgId;
  }

  public void setProducerInspgId(Long producerInspgId) {
    this.producerInspgId = producerInspgId;
  }

  public Long getProducerPsnId() {
    return producerPsnId;
  }

  public void setProducerPsnId(Long producerPsnId) {
    this.producerPsnId = producerPsnId;
  }

  public Long getConsumerInspgId() {
    return consumerInspgId;
  }

  public void setConsumerInspgId(Long consumerInspgId) {
    this.consumerInspgId = consumerInspgId;
  }

  public Integer getDynType() {
    return dynType;
  }

  public void setDynType(Integer dynType) {
    this.dynType = dynType;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Date getCompleteTime() {
    return completeTime;
  }

  public void setCompleteTime(Date completeTime) {
    this.completeTime = completeTime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public InspgDynamicFileShare getInspgDynamicFileShare() {
    return inspgDynamicFileShare;
  }

  public void setInspgDynamicFileShare(InspgDynamicFileShare inspgDynamicFileShare) {
    this.inspgDynamicFileShare = inspgDynamicFileShare;
  }

  public InspgDynamicLinkShare getInspgDynamicLinkShare() {
    return inspgDynamicLinkShare;
  }

  public void setInspgDynamicLinkShare(InspgDynamicLinkShare inspgDynamicLinkShare) {
    this.inspgDynamicLinkShare = inspgDynamicLinkShare;
  }

  public InspgDynamicMemberAdd getInspgDynamicMemberAdd() {
    return inspgDynamicMemberAdd;
  }

  public void setInspgDynamicMemberAdd(InspgDynamicMemberAdd inspgDynamicMemberAdd) {
    this.inspgDynamicMemberAdd = inspgDynamicMemberAdd;
  }

  public InspgDynamicImgUpload getInspgDynamicImgUpload() {
    return inspgDynamicImgUpload;
  }

  public void setInspgDynamicImgUpload(InspgDynamicImgUpload inspgDynamicImgUpload) {
    this.inspgDynamicImgUpload = inspgDynamicImgUpload;
  }

  public InspgDynamicNewsAdd getInspgDynamicNewsAdd() {
    return inspgDynamicNewsAdd;
  }

  public void setInspgDynamicNewsAdd(InspgDynamicNewsAdd inspgDynamicNewsAdd) {
    this.inspgDynamicNewsAdd = inspgDynamicNewsAdd;
  }

  @Override
  public String toString() {
    return "InspgDynRelationPsn [id=" + id + ", dynId=" + dynId + ", producerInspgId=" + producerInspgId
        + ", producerPsnId=" + producerPsnId + ", consumerInspgId=" + consumerInspgId + ", dynType=" + dynType
        + ", createTime=" + createTime + ", completeTime=" + completeTime + ", status=" + status
        + ", inspgDynamicFileShare=" + inspgDynamicFileShare + ", inspgDynamicLinkShare=" + inspgDynamicLinkShare
        + ", inspgDynamicMemberAdd=" + inspgDynamicMemberAdd + ", inspgDynamicImgUpload=" + inspgDynamicImgUpload
        + ", inspgDynamicNewsAdd=" + inspgDynamicNewsAdd + "]";
  }



}
