package com.smate.center.oauth.model.profile.psncnf;

import java.io.Serializable;

import com.smate.core.base.psn.dto.config.ConfigItem;

/**
 * 
 * @author liqinghua
 * 
 */
public class Config implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1141850854011724133L;
  private ConfigItem basic;
  // 联系方式
  private ConfigItem contact;
  // 教育经历
  private ConfigItem eduHistory;
  // 工作经历
  private ConfigItem workHistory;
  // 个人专长
  private ConfigItem expertise;
  // 研究项目
  private ConfigItem researchPrj;
  // 研究成果
  private ConfigItem researchPub;
  // 个人简介
  private ConfigItem brief;
  // 文献
  private ConfigItem researchRef;
  // 文件
  private ConfigItem file;
  // 所教课程
  private ConfigItem personTaught;
  // 关键词
  private ConfigItem keywords;
  // hindex
  private ConfigItem hindex;
  // ssiIncluded
  private ConfigItem ssiIncluded;

  // 新加 职称权限 tsz
  private ConfigItem position;

  public Config() {
    super();
  }

  public ConfigItem getBasic() {
    if (basic == null) {
      basic = new ConfigItem(0, 0, 1);
    }
    return basic;
  }

  public void setBasic(ConfigItem basic) {
    this.basic = basic;
  }

  public ConfigItem getContact() {
    if (contact == null) {
      contact = new ConfigItem(0, 0, 1);
    }
    return contact;
  }

  public void setContact(ConfigItem contact) {
    this.contact = contact;
  }

  public ConfigItem getEduHistory() {
    if (eduHistory == null) {
      eduHistory = new ConfigItem(0, 0, 1);
    }
    return eduHistory;
  }

  public void setEduHistory(ConfigItem eduHistory) {
    this.eduHistory = eduHistory;
  }

  public ConfigItem getWorkHistory() {
    if (workHistory == null) {
      workHistory = new ConfigItem(0, 0, 1);
    }
    return workHistory;
  }

  public void setWorkHistory(ConfigItem workHistory) {
    this.workHistory = workHistory;
  }

  public ConfigItem getExpertise() {
    if (expertise == null) {
      expertise = new ConfigItem(0, 0, 1);
    }
    return expertise;
  }

  public void setExpertise(ConfigItem expertise) {
    this.expertise = expertise;
  }

  public ConfigItem getResearchPrj() {
    if (researchPrj == null) {
      researchPrj = new ConfigItem(0, 0, 1);
    }
    return researchPrj;
  }

  public void setResearchPrj(ConfigItem researchPrj) {
    this.researchPrj = researchPrj;
  }

  public ConfigItem getResearchPub() {
    if (researchPub == null) {
      researchPub = new ConfigItem(0, 0, 1);
    }
    return researchPub;
  }

  public void setResearchPub(ConfigItem researchPub) {
    this.researchPub = researchPub;
  }

  public ConfigItem getBrief() {
    if (brief == null) {
      brief = new ConfigItem(0, 0, 1);
    }
    return brief;
  }

  public void setBrief(ConfigItem brief) {
    this.brief = brief;
  }

  public ConfigItem getResearchRef() {
    if (researchRef == null) {
      researchRef = new ConfigItem(0, 0, 1);
    }
    return researchRef;
  }

  public ConfigItem getFile() {
    if (file == null) {
      file = new ConfigItem(0, 0, 1);
    }
    return file;
  }

  public void setResearchRef(ConfigItem researchRef) {
    this.researchRef = researchRef;
  }

  public void setFile(ConfigItem file) {
    this.file = file;
  }

  public ConfigItem getPersonTaught() {
    return personTaught;
  }

  public void setPersonTaught(ConfigItem personTaught) {
    this.personTaught = personTaught;
  }

  public ConfigItem getKeywords() {
    return keywords;
  }

  public void setKeywords(ConfigItem keywords) {
    this.keywords = keywords;
  }

  public ConfigItem getHindex() {
    return hindex;
  }

  public ConfigItem getSsiIncluded() {
    return ssiIncluded;
  }

  public void setHindex(ConfigItem hindex) {
    this.hindex = hindex;
  }

  public void setSsiIncluded(ConfigItem ssiIncluded) {
    this.ssiIncluded = ssiIncluded;
  }

  public ConfigItem getPosition() {
    return position;
  }

  public void setPosition(ConfigItem position) {
    this.position = position;
  }

}
