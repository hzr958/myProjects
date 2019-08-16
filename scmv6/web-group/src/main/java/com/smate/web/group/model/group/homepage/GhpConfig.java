package com.smate.web.group.model.group.homepage;

import java.io.Serializable;

/**
 * 
 * 群组主页设置主配置JSON对象实体.
 * 
 * @author liqinghua
 * 
 */
public class GhpConfig implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7170019831421271790L;
  /**
   * 
   */
  // 群组基本信息
  private GhpConfigItem basic;
  // 联系方式
  private GhpConfigItem contact;
  // 群组成员
  private GhpConfigItem gpMember;
  // 群组介绍
  private GhpConfigItem gpInfo;
  // 研究项目
  private GhpConfigItem researchPrj;
  // 研究成果
  private GhpConfigItem researchPub;
  // 文献
  private GhpConfigItem researchRef;
  // 文件
  private GhpConfigItem file;

  // 课件
  private GhpConfigItem course;

  public GhpConfig() {
    super();
  }

  public GhpConfigItem getBasic() {
    return basic;
  }

  public GhpConfigItem getContact() {
    return contact;
  }

  public GhpConfigItem getResearchPrj() {
    return researchPrj;
  }

  public GhpConfigItem getResearchPub() {
    return researchPub;
  }

  public GhpConfigItem getResearchRef() {
    return researchRef;
  }

  public GhpConfigItem getFile() {
    return file;
  }

  public void setBasic(GhpConfigItem basic) {
    this.basic = basic;
  }

  public void setContact(GhpConfigItem contact) {
    this.contact = contact;
  }

  public void setResearchPrj(GhpConfigItem researchPrj) {
    this.researchPrj = researchPrj;
  }

  public void setResearchPub(GhpConfigItem researchPub) {
    this.researchPub = researchPub;
  }

  public void setResearchRef(GhpConfigItem researchRef) {
    this.researchRef = researchRef;
  }

  public void setFile(GhpConfigItem file) {
    this.file = file;
  }

  public GhpConfigItem getGpMember() {
    return gpMember;
  }

  public void setGpMember(GhpConfigItem gpMember) {
    this.gpMember = gpMember;
  }

  public GhpConfigItem getGpInfo() {
    return gpInfo;
  }

  public void setGpInfo(GhpConfigItem gpInfo) {
    this.gpInfo = gpInfo;
  }

  public GhpConfigItem getCourse() {
    return course;
  }

  public void setCourse(GhpConfigItem course) {
    this.course = course;
  }

}
