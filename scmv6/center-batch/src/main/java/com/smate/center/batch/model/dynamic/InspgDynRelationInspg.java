package com.smate.center.batch.model.dynamic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 动态与机构主页机构的关系表
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * 
 */
@Document(collection = "InspgDynRelationInspg")
public class InspgDynRelationInspg implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 5457026473359431149L;
  @Id
  private Long id; // 动态数据id 与 InspgDynamic中的id对应
  @Indexed
  private Long producerInspgId; // 动态产生机构id
  private Long producerPsnId; // 动态产生的操作人<管理员>id
  private Long consumerInspgId; // 动态对应的机构id
  private Integer dynType; // 动态类型
  private Long createTime; // 动态刷新时间
  private Date completeTime; // 动态创建完成的时间
  private Integer status; // 默认为1; 1.表示动态正常;0.表示动态被删除
  private List<Map<String, String>> detailInfo; // 动态详情集合
  private String inspgAvatar; // 机构头像地址
  private String inspgName; // 机构名称
  private String inspgZhName;
  private String inspgEnName;
  private String inspgHomePage; // 机构Url
  private String content; // 发表内容
  private Long createPsnId; // 创建人Id

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

  public InspgDynRelationInspg() {
    super();
  }

  public InspgDynRelationInspg(Long id, Long producerInspgId, Long producerPsnId, Long consumerInspgId, Integer dynType,
      Long createTime, Date completeTime, Integer status) {
    super();
    this.id = id;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
  }

  public InspgDynRelationInspg(Long producerInspgId, Long producerPsnId, Long consumerInspgId, Integer dynType,
      Long createTime, Date completeTime, Integer status) {
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;

  }

  public InspgDynRelationInspg(Long id, Long producerInspgId, Long producerPsnId, Long consumerInspgId, Integer dynType,
      Long createTime, Date completeTime, Integer status, InspgDynamicFileShare inspgDynamicFileShare) {
    super();
    this.id = id;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicFileShare = inspgDynamicFileShare;
  }



  public InspgDynRelationInspg(Long id, Long producerInspgId, Long producerPsnId, Long consumerInspgId, Integer dynType,
      Long createTime, Date completeTime, Integer status, InspgDynamicLinkShare inspgDynamicLinkShare) {
    super();
    this.id = id;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicLinkShare = inspgDynamicLinkShare;
  }

  public InspgDynRelationInspg(Long id, Long producerInspgId, Long producerPsnId, Long consumerInspgId, Integer dynType,
      Long createTime, Date completeTime, Integer status, InspgDynamicMemberAdd inspgDynamicMemberAdd) {
    super();
    this.id = id;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicMemberAdd = inspgDynamicMemberAdd;
  }

  public InspgDynRelationInspg(Long id, Long producerInspgId, Long producerPsnId, Long consumerInspgId, Integer dynType,
      Long createTime, Date completeTime, Integer status, InspgDynamicImgUpload inspgDynamicImgUpload) {
    super();
    this.id = id;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicImgUpload = inspgDynamicImgUpload;
  }

  public InspgDynRelationInspg(Long id, Long producerInspgId, Long producerPsnId, Long consumerInspgId, Integer dynType,
      Long createTime, Date completeTime, Integer status, InspgDynamicNewsAdd inspgDynamicNewsAdd) {
    super();
    this.id = id;
    this.producerInspgId = producerInspgId;
    this.producerPsnId = producerPsnId;
    this.consumerInspgId = consumerInspgId;
    this.dynType = dynType;
    this.createTime = createTime;
    this.completeTime = completeTime;
    this.status = status;
    this.inspgDynamicNewsAdd = inspgDynamicNewsAdd;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    return "InspgDynRelationInspg [id=" + id + ", producerInspgId=" + producerInspgId + ", producerPsnId="
        + producerPsnId + ", consumerInspgId=" + consumerInspgId + ", dynType=" + dynType + ", createTime=" + createTime
        + ", completeTime=" + completeTime + ", status=" + status + ", inspgDynamicFileShare=" + inspgDynamicFileShare
        + ", inspgDynamicLinkShare=" + inspgDynamicLinkShare + ", inspgDynamicMemberAdd=" + inspgDynamicMemberAdd
        + ", inspgDynamicImgUpload=" + inspgDynamicImgUpload + ", inspgDynamicNewsAdd=" + inspgDynamicNewsAdd + "]";
  }

  public List<Map<String, String>> getDetailInfo() {
    return detailInfo;
  }

  public void setDetailInfo(List<Map<String, String>> detailInfo) {
    this.detailInfo = detailInfo;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getInspgAvatar() {
    return inspgAvatar;
  }

  public void setInspgAvatar(String inspgAvatar) {
    this.inspgAvatar = inspgAvatar;
  }

  public String getInspgName() {
    return inspgName;
  }

  public void setInspgName(String inspgName) {
    this.inspgName = inspgName;
  }

  public String getInspgHomePage() {
    return inspgHomePage;
  }

  public void setInspgHomePage(String inspgHomePage) {
    this.inspgHomePage = inspgHomePage;
  }

  public Long getCreatePsnId() {
    return createPsnId;
  }

  public void setCreatePsnId(Long createPsnId) {
    this.createPsnId = createPsnId;
  }

  public String getInspgZhName() {
    return inspgZhName;
  }

  public void setInspgZhName(String inspgZhName) {
    this.inspgZhName = inspgZhName;
  }

  public String getInspgEnName() {
    return inspgEnName;
  }

  public void setInspgEnName(String inspgEnName) {
    this.inspgEnName = inspgEnName;
  }


}
