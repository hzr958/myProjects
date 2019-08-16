package com.smate.web.prj.form.fileimport;

import com.smate.core.base.utils.string.StringUtils;

import java.io.Serializable;

/**
 * 项目经费预算
 *
 * @author aijiangbin
 * @create 2019-08-03 15:14
 **/
public class PrjFundPlanDTO implements Serializable {


  private static final long serialVersionUID = -1395968248148305860L;
  private String prjNo = "";   //批准号
  private String seqNo = "";   // 序号
  private String itemName = ""; //科目名
  private String itemAmout = ""; //科目金额 金额  金额单位均为元
  private String ptAmout = "";  //  配套金额
  private String zcAmout = "";  // 自筹金额
  private String remark = "";  //说明
  private String pSeq = "";   //父序号


  public String getPrjNo() {
    return prjNo;
  }

  public void setPrjNo(String prjNo) {
    this.prjNo = prjNo;
  }

  public String getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(String seqNo) {
    this.seqNo = seqNo;
  }

  public String getpSeq() {
    return pSeq;
  }

  public void setpSeq(String pSeq) {
    this.pSeq = pSeq;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getItemAmout() {
    if(StringUtils.isBlank(itemAmout)){
      itemAmout = "0";
    }
    return itemAmout;
  }

  public void setItemAmout(String itemAmout) {
    this.itemAmout = itemAmout;
  }

  public String getPtAmout() {
    if(StringUtils.isBlank(ptAmout)){
      ptAmout = "0";
    }
    return ptAmout;
  }

  public void setPtAmout(String ptAmout) {
    this.ptAmout = ptAmout;
  }

  public String getZcAmout() {
    if(StringUtils.isBlank(zcAmout)){
      zcAmout = "0";
    }
    return zcAmout;
  }

  public void setZcAmout(String zcAmout) {
    this.zcAmout = zcAmout;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
