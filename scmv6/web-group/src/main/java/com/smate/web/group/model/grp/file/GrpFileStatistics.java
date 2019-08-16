package com.smate.web.group.model.grp.file;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 群组文件操作统计表
 * @author xiexing
 * @date 2019年5月15日
 */
@Entity
@Table(name = "V_GRP_FILE_STATISTICS")
public class GrpFileStatistics implements Serializable {

  /**
   * 序列号
   */
  private static final long serialVersionUID = -2649959242292639496L;

  /**
   * 群组文件id
   */
  @Id
  @Column(name = "GRP_FILE_ID")
  private Long grpFileId;

  /**
   * 分享数
   */
  @Column(name = "SHARE_COUNT")
  private Integer shareCount;

  public Long getGrpFileId() {
    return grpFileId;
  }

  public void setGrpFileId(Long grpFileId) {
    this.grpFileId = grpFileId;
  }

  public Integer getShareCount() {
    return shareCount;
  }

  public void setShareCount(Integer shareCount) {
    this.shareCount = shareCount;
  }

  public GrpFileStatistics(Long grpFileId, Integer shareCount) {
    super();
    this.grpFileId = grpFileId;
    this.shareCount = shareCount;
  }

  public GrpFileStatistics() {
    super();
    // TODO Auto-generated constructor stub
  }

  @Override
  public String toString() {
    return "GrpFileStatistics [grpFileId=" + grpFileId + ", shareCount=" + shareCount + "]";
  }
}
