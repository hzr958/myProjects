package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

/**
 * 清除成果提示消息.
 * 
 * @author liqinghua
 * 
 */
public class ClearTaskNoticeEvent implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 4132341500197788272L;
  private Long insId;
  // 成果KPI统计未完善
  private int kpiUnVlid = 0;
  // 待合并成果数
  private int pubMeger = 0;
  // 待确认成果人员
  private int pubPsnConfirm = 0;
  // 审核人员
  private int psnApprove = 0;
  // 无部门人员统计
  private int psnNoUnit = 0;


  public ClearTaskNoticeEvent() {
    super();
  }

  public ClearTaskNoticeEvent(Long insId, int kpiUnVlid, int pubMeger, int pubPsnConfirm, int psnApprove) {
    super();
    this.insId = insId;
    this.kpiUnVlid = kpiUnVlid;
    this.pubMeger = pubMeger;
    this.pubPsnConfirm = pubPsnConfirm;
    this.psnApprove = psnApprove;
  }

  /**
   * 获取实例.
   * 
   * @param insId
   * @param kpiUnVlid KPI统计数据完善缓存
   * @param pubMeger 等待合并的成果数
   * @param pubPsnConfirm 等待确认的单位成果
   * @return
   */
  public static ClearTaskNoticeEvent getInstance(Long insId, int kpiUnVlid, int pubMeger, int pubPsnConfirm) {

    return new ClearTaskNoticeEvent(insId, kpiUnVlid, pubMeger, pubPsnConfirm, 0);
  }

  public Long getInsId() {
    return insId;
  }

  public int getKpiUnVlid() {
    return kpiUnVlid;
  }

  public int getPubMeger() {
    return pubMeger;
  }

  public int getPubPsnConfirm() {
    return pubPsnConfirm;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setKpiUnVlid(int kpiUnVlid) {
    this.kpiUnVlid = kpiUnVlid;
  }

  public void setPubMeger(int pubMeger) {
    this.pubMeger = pubMeger;
  }

  public void setPubPsnConfirm(int pubPsnConfirm) {
    this.pubPsnConfirm = pubPsnConfirm;
  }

  public int getPsnApprove() {
    return psnApprove;
  }

  public void setPsnApprove(int psnApprove) {
    this.psnApprove = psnApprove;
  }

  public int getPsnNoUnit() {
    return psnNoUnit;
  }

  public void setPsnNoUnit(int psnNoUnit) {
    this.psnNoUnit = psnNoUnit;
  }

}
