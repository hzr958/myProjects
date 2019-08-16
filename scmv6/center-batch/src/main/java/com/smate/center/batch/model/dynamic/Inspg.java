package com.smate.center.batch.model.dynamic;

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

/**
 * 机构主页主体实体类
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Entity
@Table(name = "V_INSPG")
public class Inspg implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -514687065211182337L;
  private Long id; // 主键,需要序列
  private Long psnId;
  private String name; // 注册的机构名称,用户自填
  private String enName;
  private String zhName;
  private Long insId;
  private String insEmail;
  private Integer inspgType;// 1.高校 2.公司 3.期刊 4.联盟 99.其他
  private String inspgUrl; // 机构主页对外地址，用于pageurl
  private Integer status; // 1：有效，99:无效(删除)
  private Date createTime; // 创建时间
  private String logoUrl; // 机构主页logo(头像)
  private Integer attSum;// 关注数
  private Integer isFollow;// 是否关注字段 1.关注 2.未关注


  public Inspg() {
    super();
  }

  public Inspg(Long id, Long psnId, String name, String enName, Long insId, String insEmail, Integer inspgType,
      String inspgUrl, Integer status, Date createTime) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.name = name;
    this.enName = enName;
    this.insId = insId;
    this.insEmail = insEmail;
    this.inspgType = inspgType;
    this.inspgUrl = inspgUrl;
    this.status = status;
    this.createTime = createTime;
  }

  public Inspg(Long id, String enName, String zhName, String inspgUrl, String logoUrl) {
    super();
    this.id = id;
    this.enName = enName;
    this.zhName = zhName;
    this.inspgUrl = inspgUrl;
    this.logoUrl = logoUrl;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_INSPG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "ENNAME")
  public String getEnName() {
    return enName;
  }

  public void setEnName(String enName) {
    this.enName = enName;
  }


  @Column(name = "ZHNAME")
  public String getZhName() {
    return zhName;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "INS_EMAIL")
  public String getInsEmail() {
    return insEmail;
  }

  public void setInsEmail(String insEmail) {
    this.insEmail = insEmail;
  }

  @Column(name = "INSPG_TYPE")
  public Integer getInspgType() {
    return inspgType;
  }

  public void setInspgType(Integer inspgType) {
    this.inspgType = inspgType;
  }

  @Column(name = "INSPG_URL")
  public String getInspgUrl() {
    return inspgUrl;
  }

  public void setInspgUrl(String inspgUrl) {
    this.inspgUrl = inspgUrl;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Column(name = "CREATE_TIME")
  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Column(name = "logo_url")
  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  @Transient
  public Integer getAttSum() {
    return attSum;
  }

  public void setAttSum(Integer attSum) {
    this.attSum = attSum;
  }

  @Transient
  public Integer getIsFollow() {
    return isFollow;
  }

  public void setIsFollow(Integer isFollow) {
    this.isFollow = isFollow;
  }


}
