package com.smate.center.open.model.publication;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 个人成果时间戳
 * 
 * @author AiJiangBin
 *
 */
@Entity
@Table(name = "V_OPEN_PSN_PUB_TIMESTAMP")
public class PsnPubTimestamp {

  private Long psnId;

  private Long time_stamp;


  @Id
  @Column(name = "PSNID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "TIME_STAMP")
  public Long getTime_stamp() {
    return time_stamp;
  }

  public void setTime_stamp(Long time_stamp) {
    this.time_stamp = time_stamp;
  }



}
