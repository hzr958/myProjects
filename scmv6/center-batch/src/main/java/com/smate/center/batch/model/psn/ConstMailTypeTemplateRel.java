package com.smate.center.batch.model.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONST_MAIL_TYPE_TEMPLATE_REL")
public class ConstMailTypeTemplateRel implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6785689584500858128L;
  private Long relId;
  private Long typeId;
  private Integer templateId;

  @Id
  @Column(name = "rel_id")
  public Long getRelId() {
    return relId;
  }

  @Column(name = "type_id")
  public Long getTypeId() {
    return typeId;
  }

  @Column(name = "template_id")
  public Integer getTemplateId() {
    return templateId;
  }

  public void setRelId(Long relId) {
    this.relId = relId;
  }

  public void setTypeId(Long typeId) {
    this.typeId = typeId;
  }

  public void setTemplateId(Integer templateId) {
    this.templateId = templateId;
  }

}
