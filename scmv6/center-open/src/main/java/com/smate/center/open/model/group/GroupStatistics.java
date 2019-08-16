package com.smate.center.open.model.group;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 群组统计信息表.
 * 
 * @author mjg
 * @since 2014-11-28
 */
@Entity
@Table(name = "GROUP_STATISTICS")
public class GroupStatistics implements Serializable {

  private static final long serialVersionUID = -1819793792819160376L;
  // 群组ID
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_GROUP_STATISTICS", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;
  @Column(name = "GROUP_ID")
  private Long groupId;
  // 群组成员数
  @Column(name = "SUM_MEMBERS")
  private Integer sumMembers = 0;
  // 待确认群组成员数
  @Column(name = "SUM_TO_MEMBERS")
  private Integer sumToMembers = 0;
  // 群组话题数
  @Column(name = "SUM_SUBJECTS")
  private Integer sumSubjects = 0;
  // 群组成果数
  @Column(name = "SUM_PUBS")
  private Integer sumPubs = 0;
  // 群组项目数
  @Column(name = "SUM_PRJS")
  private Integer sumPrjs = 0;
  // 群组文献数
  @Column(name = "SUM_REFS")
  private Integer sumRefs = 0;
  // 群组文件数
  @Column(name = "SUM_FILES")
  private Integer sumFiles = 0;
  // 群组作业数
  @Column(name = "SUM_WORKS")
  private Integer sumWorks = 0;
  // 群组教学课件数
  @Column(name = "SUM_MATERIALS")
  private Integer sumMaterials = 0;
  // 群组成果数(文件夹未分类)
  @Column(name = "SUM_PUBS_NFOLDER")
  private Integer sumPubsNfolder = 0;
  // 群组项目数(文件夹未分类)
  @Column(name = "SUM_PRJS_NFOLDER")
  private Integer sumPrjsNfolder = 0;
  // 群组文献数(文件夹未分类)
  @Column(name = "SUM_REFS_NFOLDER")
  private Integer sumRefsNfolder = 0;
  // 群组文件数(文件夹未分类)
  @Column(name = "SUM_FILES_NFOLDER")
  private Integer sumFilesNfolder = 0;
  // 群组作业数(文件夹未分类)
  @Column(name = "SUM_WORKS_NFOLDER")
  private Integer sumWorksNfolder = 0;
  // 群组教学课件数(文件夹未分类)
  @Column(name = "SUM_MATERIALS_NFOLDER")
  private Integer sumMaterialsNfolder = 0;
  // 群组访问统计
  @Column(name = "VISIT_COUNT")
  private Long visitCount;

  public GroupStatistics() {
    super();
  }

  public GroupStatistics(Long groupId, Integer sumMembers, Integer sumToMembers, Integer sumSubjects, Integer sumPubs,
      Integer sumPrjs, Integer sumRefs, Integer sumFiles, Integer sumWorks, Integer sumMaterials,
      Integer sumPubsNfolder, Integer sumPrjsNfolder, Integer sumRefsNfolder, Integer sumFilesNfolder,
      Integer sumWorksNfolder, Integer sumMaterialsNfolder, Long visitCount) {
    super();
    this.groupId = groupId;
    this.sumMembers = sumMembers;
    this.sumToMembers = sumToMembers;
    this.sumSubjects = sumSubjects;
    this.sumPubs = sumPubs;
    this.sumPrjs = sumPrjs;
    this.sumRefs = sumRefs;
    this.sumFiles = sumFiles;
    this.sumWorks = sumWorks;
    this.sumMaterials = sumMaterials;
    this.sumPubsNfolder = sumPubsNfolder;
    this.sumPrjsNfolder = sumPrjsNfolder;
    this.sumRefsNfolder = sumRefsNfolder;
    this.sumFilesNfolder = sumFilesNfolder;
    this.sumWorksNfolder = sumWorksNfolder;
    this.sumMaterialsNfolder = sumMaterialsNfolder;
    this.visitCount = visitCount;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public Integer getSumMembers() {
    return sumMembers;
  }

  public void setSumMembers(Integer sumMembers) {
    this.sumMembers = sumMembers;
  }

  public Integer getSumToMembers() {
    return sumToMembers;
  }

  public void setSumToMembers(Integer sumToMembers) {
    this.sumToMembers = sumToMembers;
  }

  public Integer getSumSubjects() {
    return sumSubjects;
  }

  public void setSumSubjects(Integer sumSubjects) {
    this.sumSubjects = sumSubjects;
  }

  public Integer getSumPubs() {
    return sumPubs;
  }

  public void setSumPubs(Integer sumPubs) {
    this.sumPubs = sumPubs;
  }

  public Integer getSumPrjs() {
    return sumPrjs;
  }

  public void setSumPrjs(Integer sumPrjs) {
    this.sumPrjs = sumPrjs;
  }

  public Integer getSumRefs() {
    return sumRefs;
  }

  public void setSumRefs(Integer sumRefs) {
    this.sumRefs = sumRefs;
  }

  public Integer getSumFiles() {
    return sumFiles;
  }

  public void setSumFiles(Integer sumFiles) {
    this.sumFiles = sumFiles;
  }

  public Integer getSumWorks() {
    return sumWorks;
  }

  public void setSumWorks(Integer sumWorks) {
    this.sumWorks = sumWorks;
  }

  public Integer getSumMaterials() {
    return sumMaterials;
  }

  public void setSumMaterials(Integer sumMaterials) {
    this.sumMaterials = sumMaterials;
  }

  public Integer getSumPubsNfolder() {
    return sumPubsNfolder;
  }

  public void setSumPubsNfolder(Integer sumPubsNfolder) {
    this.sumPubsNfolder = sumPubsNfolder;
  }

  public Integer getSumPrjsNfolder() {
    return sumPrjsNfolder;
  }

  public void setSumPrjsNfolder(Integer sumPrjsNfolder) {
    this.sumPrjsNfolder = sumPrjsNfolder;
  }

  public Integer getSumRefsNfolder() {
    return sumRefsNfolder;
  }

  public void setSumRefsNfolder(Integer sumRefsNfolder) {
    this.sumRefsNfolder = sumRefsNfolder;
  }

  public Integer getSumFilesNfolder() {
    return sumFilesNfolder;
  }

  public void setSumFilesNfolder(Integer sumFilesNfolder) {
    this.sumFilesNfolder = sumFilesNfolder;
  }

  public Integer getSumWorksNfolder() {
    return sumWorksNfolder;
  }

  public void setSumWorksNfolder(Integer sumWorksNfolder) {
    this.sumWorksNfolder = sumWorksNfolder;
  }

  public Integer getSumMaterialsNfolder() {
    return sumMaterialsNfolder;
  }

  public void setSumMaterialsNfolder(Integer sumMaterialsNfolder) {
    this.sumMaterialsNfolder = sumMaterialsNfolder;
  }

  public Long getVisitCount() {
    return visitCount;
  }

  public void setVisitCount(Long visitCount) {
    this.visitCount = visitCount;
  }
}
