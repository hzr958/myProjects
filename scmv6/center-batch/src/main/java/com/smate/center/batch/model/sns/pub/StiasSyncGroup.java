package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * STIAS创建群组model
 * 
 * @author oyh
 */
public class StiasSyncGroup implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3817387593123698366L;
  // 群组名称
  private String gname;
  // 实验室ID
  private String labid;
  // stias 人员标识(群组创建人)
  private String pcode;
  // 群组创建人Email
  private String email;
  // 群组创建人的psnname
  private String pname;
  private Long rolInsId;
  private Long psnId;
  private Long groupId;
  private String status;
  private String prpname;
  private String year;
  private List<Map<String, String>> members = new ArrayList();

  /**
   * @return the gname
   */
  public String getGname() {
    return gname;
  }

  /**
   * @param gname the gname to set
   */
  public void setGname(String gname) {
    this.gname = gname;
  }

  /**
   * @return the labid
   */
  public String getLabid() {
    return labid;
  }

  /**
   * @param labid the labid to set
   */
  public void setLabid(String labid) {
    this.labid = labid;
  }

  /**
   * @return the pcode
   */
  public String getPcode() {
    return pcode;
  }

  /**
   * @param pcode the pcode to set
   */
  public void setPcode(String pcode) {
    this.pcode = pcode;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the pname
   */
  public String getPname() {
    return pname;
  }

  /**
   * @param pname the pname to set
   */
  public void setPname(String pname) {
    this.pname = pname;
  }

  /**
   * @return the rolInsId
   */
  public Long getRolInsId() {
    return rolInsId;
  }

  /**
   * @param rolInsId the rolInsId to set
   */
  public void setRolInsId(Long rolInsId) {
    this.rolInsId = rolInsId;
  }

  /**
   * @return the members
   */
  public List<Map<String, String>> getMembers() {
    return members;
  }

  /**
   * @param members the members to set
   */
  public void setMembers(List<Map<String, String>> members) {
    this.members = members;
  }

  /**
   * @return the psnId
   */
  public Long getPsnId() {
    return psnId;
  }

  /**
   * @param psnId the psnId to set
   */
  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  /**
   * @return the groupId
   */
  public Long getGroupId() {
    return groupId;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(String year) {
    this.year = year;
  }

  /**
   * @return the prpname
   */
  public String getPrpname() {
    return prpname;
  }

  /**
   * @param prpname the prpname to set
   */
  public void setPrpname(String prpname) {
    this.prpname = prpname;
  }

}
