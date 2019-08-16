package com.smate.center.open.model.psn;

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
 * 好友选择记录实体类.
 * 
 * @author zzx
 * 
 */
@Entity
@Table(name = "RECENT_SELECTED")
public class RecentSelected implements Serializable {

  private static final long serialVersionUID = 5360842010407987841L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_RECENT_SELECTED", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键
  @Column(name = "PSN_ID")
  private Long psnId;// 操作者ID
  @Column(name = "SELECTED_PSN_ID")
  private Long selectedPsnId;// 被选择的好友的ID
  @Column(name = "SELECTED_DATE")
  private Date selectedDate; // 选择日期

  public RecentSelected() {
    super();
  }

  public RecentSelected(Long id, Long psnId, Long selectedPsnId, Date selectedDate) {
    super();
    this.id = id;
    this.psnId = psnId;
    this.selectedPsnId = selectedPsnId;
    this.selectedDate = selectedDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getSelectedPsnId() {
    return selectedPsnId;
  }

  public void setSelectedPsnId(Long selectedPsnId) {
    this.selectedPsnId = selectedPsnId;
  }

  public Date getSelectedDate() {
    return selectedDate;
  }

  public void setSelectedDate(Date selectedDate) {
    this.selectedDate = selectedDate;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

}
