package com.smate.center.task.single.model.rol.pub;

import java.io.Serializable;

/**
 * 部门资源数.
 * 
 * @author tsz
 * 
 */

public class InsUnitResourcesCount implements Serializable {

  private static final long serialVersionUID = 7404452269976832156L;
  /**
   * 
   */
  private Long id;
  private Long unitId;
  private String unitZhName;
  private String unitEnName;
  private Long researchers;
  private Long projects;
  private Long publications;
  private Long patents;

  public InsUnitResourcesCount() {
    super();
  }

  public InsUnitResourcesCount(Long id, Long unitId, String unitZhName, String unitEnName, Long researchers,
      Long projects, Long publications, Long patents) {
    super();
    this.id = id;
    this.unitId = unitId;
    this.unitZhName = unitZhName;
    this.unitEnName = unitEnName;
    this.researchers = researchers;
    this.projects = projects;
    this.publications = publications;
    this.patents = patents;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUnitId() {
    return unitId;
  }

  public void setUnitId(Long unitId) {
    this.unitId = unitId;
  }

  public String getUnitZhName() {
    return unitZhName;
  }

  public void setUnitZhName(String unitZhName) {
    this.unitZhName = unitZhName;
  }

  public String getUnitEnName() {
    return unitEnName;
  }

  public void setUnitEnName(String unitEnName) {
    this.unitEnName = unitEnName;
  }

  public Long getResearchers() {
    return researchers;
  }

  public void setResearchers(Long researchers) {
    this.researchers = researchers;
  }

  public Long getProjects() {
    return projects;
  }

  public void setProjects(Long projects) {
    this.projects = projects;
  }

  public Long getPublications() {
    return publications;
  }

  public void setPublications(Long publications) {
    this.publications = publications;
  }

  public Long getPatents() {
    return patents;
  }

  public void setPatents(Long patents) {
    this.patents = patents;
  }

}
