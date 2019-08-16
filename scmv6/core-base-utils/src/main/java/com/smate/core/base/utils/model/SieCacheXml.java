package com.smate.core.base.utils.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yxs
 * @descript xml缓存类
 */
@Entity
@Table(name = "CACHE_XML")
public class SieCacheXml {
  private String id;
  private String xml;
  private Date cacheDate;

  @Id
  @Column(name = "ID")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(name = "XML")
  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

  @Column(name = "CACHE_DATE")
  public Date getCacheDate() {
    return cacheDate;
  }

  public void setCacheDate(Date cacheDate) {
    this.cacheDate = cacheDate;
  }
}
