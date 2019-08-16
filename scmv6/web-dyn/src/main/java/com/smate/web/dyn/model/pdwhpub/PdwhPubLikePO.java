package com.smate.web.dyn.model.pdwhpub;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 基准库成果赞
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */
@Entity
@Table(name = "V_PUB_PDWH_LIKE")
public class PdwhPubLikePO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4989082715501970225L;

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_PDWH_LIKE", sequenceName = "V_SEQ_PUB_PDWH_LIKE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_PDWH_LIKE")
  @Column(name = "LIKE_ID")
  private Long likeId; // 主键

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 成果id

  @Column(name = "PSN_ID")
  private Long psnId; // 赞人员id

  @Column(name = "STATUS")
  private Integer status; // 状态 0=没有赞 ； 1=已经赞

  @Column(name = "GMT_CREATE")
  private Date gmtCreate; // 赞时间

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified; // 更新时间

  public PdwhPubLikePO() {
    super();
  }

  public Long getLikeId() {
    return likeId;
  }

  public void setLikeId(Long likeId) {
    this.likeId = likeId;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
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

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
