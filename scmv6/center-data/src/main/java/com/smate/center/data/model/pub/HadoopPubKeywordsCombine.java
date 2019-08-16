package com.smate.center.data.model.pub;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HADOOP_PUB_KEYWORDS_COMBINE")
public class HadoopPubKeywordsCombine implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "PUB_KEY")
  private String pubKey;
  @Column(name = "CREATE_DATE")
  private Date createDate;

  public String getPubKey() {
    return pubKey;
  }

  public void setPubKey(String pubKey) {
    this.pubKey = pubKey;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
