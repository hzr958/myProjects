package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 智能匹配邮件同步.
 * 
 * @author cwli
 * 
 */
@Entity
@Table(name = "DEPT_ALIAS_FETCH")
public class DeptAliasFetch implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 105718785380124748L;

  // Id
  private Long id;
  // 单位Id
  private Long insId;
  // 抓取的文献所在单位的部门名称
  private String aliasName;
  // 第三方数据库编码
  private Long dbId;
  private String dbName;
  // 抓取时间
  private Date fetchDate;
  private Long isMapping;
  private String deptIdList;
  private Long deptId;
  // 使用次数
  private Integer usedNum;
  private String des3Id;
  private List<Long> aliasIdList;
  private List<DeptAlias> deptAliasList;

  public DeptAliasFetch() {
    super();
  }

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FETCH_DATE")
  public Date getFetchDate() {
    return fetchDate;
  }

  public void setFetchDate(Date fetchDate) {
    this.fetchDate = fetchDate;
  }

  @Transient
  public Long getIsMapping() {
    return isMapping;
  }

  public void setIsMapping(Long isMapping) {
    this.isMapping = isMapping;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  @Column(name = "ALIAS_NAME")
  public String getAliasName() {
    return aliasName;
  }

  public void setAliasName(String aliasName) {
    this.aliasName = aliasName;
  }

  @Column(name = "DB_ID")
  public Long getDbId() {
    return dbId;
  }

  public void setDbId(Long dbId) {
    this.dbId = dbId;
  }

  @Transient
  public List<Long> getAliasIdList() {
    return aliasIdList;
  }

  public void setAliasIdList(List<Long> aliasIdList) {
    this.aliasIdList = aliasIdList;
  }

  @Transient
  public List<DeptAlias> getDeptAliasList() {
    return deptAliasList;
  }

  public void setDeptAliasList(List<DeptAlias> deptAliasList) {
    this.deptAliasList = deptAliasList;
  }

  @Column(name = "DEPTIDLIST")
  public String getDeptIdList() {
    return deptIdList;
  }

  public void setDeptIdList(String deptIdList) {
    this.deptIdList = deptIdList;
  }

  @Transient
  public Long getDeptId() {
    return deptId;
  }

  public void setDeptId(Long deptId) {
    this.deptId = deptId;
  }

  @Transient
  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  @Column(name = "USED_NUM")
  public Integer getUsedNum() {
    return usedNum;
  }

  public void setUsedNum(Integer usedNum) {
    this.usedNum = usedNum;
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

}
