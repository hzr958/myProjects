package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "DEPT_ALIAS")
public class DeptAlias implements Serializable {

  private static final long serialVersionUID = -5935843538536246302L;
  private Long id;
  private Long insId;
  private Long deptId;
  private Long dbId;
  private String aliasName;
  private DeptAliasFetch deptAliasFetch;
  private Long fetchId;
  private String des3Id;

  public DeptAlias() {
    super();

  }

  public DeptAlias(Long insId, Long deptId, Long dbId, String aliasName) {
    super();
    this.insId = insId;
    this.deptId = deptId;
    this.dbId = dbId;
    this.aliasName = aliasName;
  }

  public DeptAlias(Long insId, Long deptId, Long dbId, String aliasName, Long fetchId) {
    super();
    this.insId = insId;
    this.deptId = deptId;
    this.dbId = dbId;
    this.aliasName = aliasName;
    this.fetchId = fetchId;
  }

  @Id
  @Column(name = "ALIAS_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_INS_UNIT", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "DEPT_ID")
  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  @Column(name = "DB_ID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Column(name = "ALIAS_NAME")
  public String getAliasName() {
    return aliasName;
  }

  public void setAliasName(String aliasName) {
    this.aliasName = aliasName;
  }

  @Column(name = "ALIAS_FETCH_ID")
  public Long getFetchId() {
    return fetchId;
  }

  public void setFetchId(Long fetchId) {
    this.fetchId = fetchId;
  }

  @Transient
  public DeptAliasFetch getDeptAliasFetch() {
    return deptAliasFetch;
  }

  public void setDeptAliasFetch(DeptAliasFetch deptAliasFetch) {
    this.deptAliasFetch = deptAliasFetch;
  }

  @Transient
  public String getDes3Id() {
    if (this.id != null && des3Id == null) {
      des3Id = ServiceUtil.encodeToDes3(this.id.toString());
    }
    return des3Id;
  }

  public void setDes3Id(String des3Id) {
    this.des3Id = des3Id;
  }

  @Override
  public String toString() {

    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((aliasName == null) ? 0 : aliasName.hashCode());
    result = prime * result + ((dbId == null) ? 0 : dbId.hashCode());
    result = prime * result + ((deptAliasFetch == null) ? 0 : deptAliasFetch.hashCode());
    result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
    result = prime * result + ((insId == null) ? 0 : insId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DeptAlias other = (DeptAlias) obj;
    if (aliasName == null) {
      if (other.aliasName != null)
        return false;
    } else if (!aliasName.equals(other.aliasName))
      return false;
    if (dbId == null) {
      if (other.dbId != null)
        return false;
    } else if (!dbId.equals(other.dbId))
      return false;
    if (deptAliasFetch == null) {
      if (other.deptAliasFetch != null)
        return false;
    } else if (!deptAliasFetch.equals(other.deptAliasFetch))
      return false;
    if (deptId == null) {
      if (other.deptId != null)
        return false;
    } else if (!deptId.equals(other.deptId))
      return false;
    if (insId == null) {
      if (other.insId != null)
        return false;
    } else if (!insId.equals(other.insId))
      return false;
    return true;
  }

}
