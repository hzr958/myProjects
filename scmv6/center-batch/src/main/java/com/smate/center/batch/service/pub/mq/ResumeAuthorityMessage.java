package com.smate.center.batch.service.pub.mq;

import java.util.List;

/**
 * 工作经历，教育经历，项目录入编辑，成果录入编辑权限同步.
 * 
 * @author liangguokeng
 * 
 */
public class ResumeAuthorityMessage {

  /**
   * @author liangguokeng
   */
  private Long psnId;
  private String ids;// 成果或项目id集合，用逗号分割
  private List<String> idsResumes;// 成果权限集合，String为id和权限(公开：7,好友：6,本人：4)用逗号分割组成.
  private String type;// 成果pub, 项目prj, 工作work, 教育edu
  private Integer isDel;// 1=删除
  private List<Long> idList;// 需要删除的成果/项目/工作/教育id集合

  public ResumeAuthorityMessage() {
    super();
  }

  public ResumeAuthorityMessage(Long psnId, String ids, String type, Integer fromNodeId) {
    this.psnId = psnId;
    this.ids = ids;
    this.type = type;
  }

  public ResumeAuthorityMessage(Long psnId, List<String> idsResumes, String type, Integer fromNodeId) {
    this.psnId = psnId;
    this.idsResumes = idsResumes;
    this.type = type;
  }

  public ResumeAuthorityMessage(Long psnId, List<Long> idList, String type, Integer isDel, Integer fromNodeId) {
    this.psnId = psnId;
    this.idList = idList;
    this.type = type;
    this.isDel = isDel;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setpIds(String pIds) {
    this.ids = pIds;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getIds() {
    return ids;
  }

  public void setIds(String ids) {
    this.ids = ids;
  }

  public List<String> getIdsResumes() {
    return idsResumes;
  }

  public void setIdsResumes(List<String> idsResumes) {
    this.idsResumes = idsResumes;
  }

  public Integer getIsDel() {
    return isDel;
  }

  public List<Long> getIdList() {
    return idList;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  public void setIdList(List<Long> idList) {
    this.idList = idList;
  }

}
