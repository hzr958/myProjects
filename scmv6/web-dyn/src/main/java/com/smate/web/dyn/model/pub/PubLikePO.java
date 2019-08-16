package com.smate.web.dyn.model.pub;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 成果赞
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */
@Entity
@Table(name = "V_PUB_LIKE")
public class PubLikePO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4213363803100739445L;

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_LIKE", sequenceName = "V_SEQ_PUB_LIKE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_LIKE")
  @Column(name = "LIKE_ID")
  private Long likeId; // 主键

  @Column(name = "PUB_ID")
  private Long pubId; // 成果id

  @Column(name = "PSN_ID")
  private Long psnId; // 赞人员id

  @Column(name = "STATUS")
  private Integer status; // 状态 0=没有赞 ； 1=已经赞

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 赞时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 更新时间

  public PubLikePO() {
    super();
  }

  public Long getLikeId() {
    return likeId;
  }

  public void setLikeId(Long likeId) {
    this.likeId = likeId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
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

}
