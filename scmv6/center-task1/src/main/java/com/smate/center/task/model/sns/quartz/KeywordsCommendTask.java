package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 关键词推荐任务开关，用于控制存储过程开、关状态.
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "KEYWORDS_COMMEND_TASK")
public class KeywordsCommendTask implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 3349891819107710040L;
  private Integer id;
  // 0关、1开、3完成、9错误
  private Integer flag;

  @Id
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  @Column(name = "FLAG")
  public Integer getFlag() {
    return flag;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

}
