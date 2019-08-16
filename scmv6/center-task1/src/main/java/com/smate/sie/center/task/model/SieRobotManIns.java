package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 社交机器人配置信息表
 * 
 * @author 叶星源
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "ROBOT_MAN_INS")
public class SieRobotManIns implements Serializable {

  @Id
  @Column(name = "ins_id")
  private Long id; // insId

  @Column(name = "zh_name")
  private String zhName; // 机构名称

  @Column(name = "config_id")
  private Integer configId; // 访问本单位的机器人配置id

  public SieRobotManIns() {
    super();
  }

  /**
   * ins_Id
   */
  public Long getId() {
    return id;
  }

  /**
   * 机构名称
   */
  public String getZhName() {
    return zhName;
  }

  /**
   * 配置信息id(关联ROBOT_MAN_CONFIG表主键)
   */
  public Integer getConfigId() {
    return configId;
  }

  public void setConfigId(Integer configId) {
    this.configId = configId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setZhName(String zhName) {
    this.zhName = zhName;
  }

}
