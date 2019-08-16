package com.smate.sie.core.base.utils.model.auditlog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 审计日志类型.
 * 
 * @author yxy
 */

@Entity
@Table(name = "COM_AUDIT_TYPE") // old:AUDIT_ACTION_TYPE
public class SieComAuditType implements Serializable {

  private static final long serialVersionUID = 1895794102389192434L;

  @Id
  @Column(name = "ID")
  private Long id;// 主键

  @Column(name = "NAME")
  private String name;// 日志类型名称

  @Column(name = "PARENT_ID")
  private Long parentId;// 父编号

  @Column(name = "AUD_ACTION")
  private String audAction;// 日志类型代码

  @Column(name = "TEMPLATE")
  private String template;// 生成日志内容模板

  @Column(name = "PARAM_TYPE")
  private String paramType;// 日志参数类型：condition检索条件参数，map参数，normal普通参数

  @Column(name = "REPLACE_KEYS")
  private String replaceKeys;// 将以key-value形式存储的日志中的key替换成中文配置

  @Column(name = "STATUS")
  private Integer status; // 是否解析，为0则此日志不作解析

  @Column(name = "TEMPLATE_SQL")
  private String templateSql;// 可查询出日志输出内容的SQL语句


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  public String getAudAction() {
    return audAction;
  }

  public void setAudAction(String audAction) {
    this.audAction = audAction;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getParamType() {
    return paramType;
  }

  public void setParamType(String paramType) {
    this.paramType = paramType;
  }

  public String getReplaceKeys() {
    return replaceKeys;
  }

  public void setReplaceKeys(String replaceKeys) {
    this.replaceKeys = replaceKeys;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getTemplateSql() {
    return templateSql;
  }

  public void setTemplateSql(String templateSql) {
    this.templateSql = templateSql;
  }

}
