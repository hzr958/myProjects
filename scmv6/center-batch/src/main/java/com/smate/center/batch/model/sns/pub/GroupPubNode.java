package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 当前人成果群组关系表.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "GROUP_PUB_NODE")
public class GroupPubNode implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -215970648046272417L;

  // 主键
  private Long id;

  // 成果ID
  private Long pubId;
  // 成果类型
  private Integer articleType;

  // 人员ID
  private Long psnId;
  //
  private Long groupId;

  // 成果加密ID
  private String des3PubId;

  public GroupPubNode() {
    super();
  }

  public GroupPubNode(Long pubId, Integer articleType, Long psnId, Long groupId) {
    super();
    this.pubId = pubId;
    this.articleType = articleType;
    this.psnId = psnId;
    this.groupId = groupId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_PUB_NODE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "GROUP_ID")
  public Long getGroupId() {
    return groupId;
  }

  @Column(name = "ARTICLE_TYPE")
  public Integer getArticleType() {
    return articleType;
  }

  public void setArticleType(Integer articleType) {
    this.articleType = articleType;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Transient
  public String getDes3PubId() {
    if (this.pubId != null && des3PubId == null) {
      des3PubId = ServiceUtil.encodeToDes3(this.pubId.toString());
    }
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    if (this.pubId == null && StringUtils.isNotBlank(des3PubId)) {
      this.pubId = Long.valueOf(ServiceUtil.decodeFromDes3(des3PubId));
    }
    this.des3PubId = des3PubId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
