package com.smate.sie.center.task.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 社交机器人操作日志表
 * 
 * @author 叶星源
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "ROBOT_MAN_REFLUSH_lOG")
public class SieRobotManReflushLog implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_ROBOT_MAN_REFLUSH_LOG", allocationSize = 1)
  @Column(name = "log_id")
  private Long id; // 主键

  @Column(name = "ip")
  private String ip; // 机器人的IP地址

  @Column(name = "key_type")
  private Long keyType; // 1阅读，对应bh_read表，2分享，对应bh_share表，3下载，对应bh_download表，4引用，对应bh_citation表，8主页查看，9主页分享对应bh_index_share表

  @Column(name = "key_code")
  private Long keyCode; // bh_表主键

  @Column(name = "create_time")
  private Date createTime; // 创建时间

  public SieRobotManReflushLog() {
    super();
  }

  public Long getId() {
    return id;
  }

  public String getIp() {
    return ip;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Long getKeyType() {
    return keyType;
  }

  public Long getKeyCode() {
    return keyCode;
  }

  public void setKeyType(Long keyType) {
    this.keyType = keyType;
  }

  public void setKeyCode(Long keyCode) {
    this.keyCode = keyCode;
  }

}
