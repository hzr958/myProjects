package com.smate.web.v8pub.vo.sns.newresume;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 简历模块信息表
 * 
 * @author wsn
 *
 */
@Entity
@Table(name = "V_CV_MODULE_INFO")
public class CVModuleInfo implements Serializable {

  private static final long serialVersionUID = 8492488206252601731L;

  private Long id; // 主键
  private String moduleInfo; // 模块信息，json格式

  public CVModuleInfo() {
    super();
  }

  public CVModuleInfo(Long id) {
    super();
    this.id = id;
  }

  public CVModuleInfo(Long id, String moduleInfo) {
    super();
    this.id = id;
    this.moduleInfo = moduleInfo;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_RESUME_MODULE_INFO", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "MODULE_INFO")
  public String getModuleInfo() {
    return moduleInfo;
  }

  public void setModuleInfo(String moduleInfo) {
    this.moduleInfo = moduleInfo;
  }

}
