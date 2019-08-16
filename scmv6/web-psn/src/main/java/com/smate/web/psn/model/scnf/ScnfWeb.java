package com.smate.web.psn.model.scnf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * web站点配置信息.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "SCNF_WEB")
public class ScnfWeb implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 978600667391127573L;

  // 站点主键
  private Long wsId;
  // WEB服务域名
  private String domainName;
  // web上下文路径
  private String contextPath;

  @Id
  @Column(name = "WS_ID")
  public Long getWsId() {
    return wsId;
  }

  @Column(name = "DOMAIN_NAME")
  public String getDomainName() {
    return domainName;
  }

  @Column(name = "CONTEXT_PATH")
  public String getContextPath() {
    return contextPath;
  }

  public void setWsId(Long wsId) {
    this.wsId = wsId;
  }

  public void setDomainName(String domainName) {
    this.domainName = domainName;
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

}
