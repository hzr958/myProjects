package com.smate.center.batch.form.pub;

import java.io.Serializable;

import com.smate.core.base.utils.string.ServiceUtil;


/**
 * @author changwenli
 *
 */
public class PubReaderForm implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 7861210681080713116L;
  private Long pubId;
  private Long psnId;
  private String psnDes3Id;
  private String psnName;
  private Long psnDis;
  private String avatars;
  private String titolo;

  public PubReaderForm() {
    super();
  }

  public PubReaderForm(Long pubId, Long psnId) {
    this.pubId = pubId;
    this.psnId = psnId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getPsnDes3Id() {
    if (this.psnId != null && psnDes3Id == null) {
      psnDes3Id = ServiceUtil.encodeToDes3(this.psnId.toString());
    }
    return psnDes3Id;
  }

  public void setPsnDes3Id(String psnDes3Id) {
    this.psnDes3Id = psnDes3Id;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public Long getPsnDis() {
    return psnDis;
  }

  public void setPsnDis(Long psnDis) {
    this.psnDis = psnDis;
  }

  public String getAvatars() {
    return avatars;
  }

  public void setAvatars(String avatars) {
    this.avatars = avatars;
  }

  public String getTitolo() {
    return titolo;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }


}
