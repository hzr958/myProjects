package com.smate.web.group.model.grp.grpbase;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组菜单配置表
 * 
 * @author AiJiangBin
 *
 */
@Entity
@Table(name = "V_GRP_CONTROL")
public class GrpControl implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6288246476105626308L;

  @Id
  @Column(name = "GRP_ID")
  private Long grpId; // 群组id 主键

  @Column(name = "IS_INDEX_DISCUSS_OPEN")
  private String isIndexDiscussOpen; // 主页是否显示群组动态 [1=是 ， 0=否 ] 默认1

  @Column(name = "IS_INDEX_MEMBER_OPEN")
  private String isIndexMemberOpen; // 主页是否显示群组成员 [1=是 ， 0=否 ] 默认1

  @Column(name = "IS_INDEX_PUB_OPEN")
  private String isIndexPubOpen; // 主页是否显示群组成果 [1=是 ， 0=否 ] 默认1

  @Column(name = "IS_INDEX_FILE_OPEN")
  private String isIndexFileOpen; // 主页是否显示群组文件 [1=是 ， 0=否 ] 默认1

  @Column(name = "IS_DISCUSS_SHOW")
  private String isDiscussShow; // 群组外成员是否显示群组动态 [1=是 ， 0=否 ] 默认1

  @Column(name = "IS_MEMBER_SHOW")
  private String isMemberShow; // 群组外成员是否显示群组成员 [1=是 ， 0=否 ] 默认1

  @Column(name = "IS_PUB_SHOW")
  private String isPubShow; // 群组外成员是否显示群组成果[1=是 ， 0=否 ] 默认1

  @Column(name = "IS_FILE_SHOW")
  private String isFileShow; // 群组外成员是否显示群组文件 [1=是 ， 0=否 ] 默认1

  @Column(name = "is_prj_pub_show")
  private String isPrjPubShow; // 群组外成员是否显示项目成果 [1=是 ， 0=否 ] 默认1

  @Column(name = "is_prj_ref_show")
  private String isPrjRefShow; // 群组外成员是否显示项目文献 [1=是 ， 0=否 ] 默认1

  @Column(name = "is_curware_file_show")
  private String isCurwareFileShow; // 群组外成员是否显示课件 [1=是 ， 0=否 ] 默认1

  @Column(name = "is_work_file_show")
  private String isWorkFileShow; // 群组外成员是否显示作业 [1=是 ， 0=否 ] 默认1

  public GrpControl() {
    super();
  }

  public GrpControl(Long grpId, String isIndexDiscussOpen, String isIndexMemberOpen, String isIndexPubOpen,
      String isIndexFileOpen, String isDiscussShow, String isMemberShow, String isPubShow, String isFileShow) {
    super();
    this.grpId = grpId;
    this.isIndexDiscussOpen = isIndexDiscussOpen;
    this.isIndexMemberOpen = isIndexMemberOpen;
    this.isIndexPubOpen = isIndexPubOpen;
    this.isIndexFileOpen = isIndexFileOpen;
    this.isDiscussShow = isDiscussShow;
    this.isMemberShow = isMemberShow;
    this.isPubShow = isPubShow;
    this.isFileShow = isFileShow;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getIsIndexDiscussOpen() {
    return isIndexDiscussOpen;
  }

  public void setIsIndexDiscussOpen(String isIndexDiscussOpen) {
    this.isIndexDiscussOpen = isIndexDiscussOpen;
  }

  public String getIsIndexMemberOpen() {
    return isIndexMemberOpen;
  }

  public void setIsIndexMemberOpen(String isIndexMemberOpen) {
    this.isIndexMemberOpen = isIndexMemberOpen;
  }

  public String getIsIndexPubOpen() {
    return isIndexPubOpen;
  }

  public void setIsIndexPubOpen(String isIndexPubOpen) {
    this.isIndexPubOpen = isIndexPubOpen;
  }

  public String getIsIndexFileOpen() {
    return isIndexFileOpen;
  }

  public void setIsIndexFileOpen(String isIndexFileOpen) {
    this.isIndexFileOpen = isIndexFileOpen;
  }

  public String getIsDiscussShow() {
    return isDiscussShow;
  }

  public void setIsDiscussShow(String isDiscussShow) {
    this.isDiscussShow = isDiscussShow;
  }

  public String getIsMemberShow() {
    return isMemberShow;
  }

  public void setIsMemberShow(String isMemberShow) {
    this.isMemberShow = isMemberShow;
  }

  public String getIsPubShow() {
    return isPubShow;
  }

  public void setIsPubShow(String isPubShow) {
    this.isPubShow = isPubShow;
  }

  public String getIsFileShow() {
    return isFileShow;
  }

  public void setIsFileShow(String isFileShow) {
    this.isFileShow = isFileShow;
  }

  @Override
  public String toString() {
    return "GrpControl [grpId=" + grpId + ", isIndexDiscussOpen=" + isIndexDiscussOpen + ", isIndexMemberOpen="
        + isIndexMemberOpen + ", isIndexPubOpen=" + isIndexPubOpen + ", isIndexFileOpen=" + isIndexFileOpen
        + ", isDiscussShow=" + isDiscussShow + ", isMemberShow=" + isMemberShow + ", isPubShow=" + isPubShow
        + ", isFileShow=" + isFileShow + "]";
  }

  public String getIsPrjPubShow() {
    return isPrjPubShow;
  }

  public void setIsPrjPubShow(String isPrjPubShow) {
    this.isPrjPubShow = isPrjPubShow;
  }

  public String getIsPrjRefShow() {
    return isPrjRefShow;
  }

  public void setIsPrjRefShow(String isPrjRefShow) {
    this.isPrjRefShow = isPrjRefShow;
  }



  public String getIsCurwareFileShow() {
    return isCurwareFileShow;
  }

  public void setIsCurwareFileShow(String isCurwareFileShow) {
    this.isCurwareFileShow = isCurwareFileShow;
  }

  public String getIsWorkFileShow() {
    return isWorkFileShow;
  }

  public void setIsWorkFileShow(String isWorkFileShow) {
    this.isWorkFileShow = isWorkFileShow;
  }


}
