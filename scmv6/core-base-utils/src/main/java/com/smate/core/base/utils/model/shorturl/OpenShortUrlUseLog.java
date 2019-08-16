package com.smate.core.base.utils.model.shorturl;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 短地址使用日志
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_OPEN_SHORT_URL_USE_LOG")
public class OpenShortUrlUseLog implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_OPEN_SHORT_URL_USE_LOG", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键ID
  @Column(name = "SHORT_URL_PARAMET")
  private String shortUrlParamet;// 短地址
  @Column(name = "USE_DATE")
  private Date useDate;// 使用日期
  @Column(name = "USE_PSN_ID")
  private Long usePsnId;// 使用人
  @Column(name = "USE_IP")
  private String useIp;// 来源IP

  public OpenShortUrlUseLog() {
    super();
  }

  public OpenShortUrlUseLog(Long id, String shortUrlParamet, Date useDate, Long usePsnId, String useIp) {
    super();
    this.id = id;
    this.shortUrlParamet = shortUrlParamet;
    this.useDate = useDate;
    this.usePsnId = usePsnId;
    this.useIp = useIp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getShortUrlParamet() {
    return shortUrlParamet;
  }

  public void setShortUrlParamet(String shortUrlParamet) {
    this.shortUrlParamet = shortUrlParamet;
  }

  public Date getUseDate() {
    return useDate;
  }

  public void setUseDate(Date useDate) {
    this.useDate = useDate;
  }

  public Long getUsePsnId() {
    return usePsnId;
  }

  public void setUsePsnId(Long usePsnId) {
    this.usePsnId = usePsnId;
  }

  public String getUseIp() {
    return useIp;
  }

  public void setUseIp(String useIp) {
    this.useIp = useIp;
  }

  @Override
  public String toString() {
    return "OpenShortUrlUseLog [id=" + id + ", shortUrlParamet=" + shortUrlParamet + ", useDate=" + useDate
        + ", usePsnId=" + usePsnId + ", useIp=" + useIp + "]";
  }



}
