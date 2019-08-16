package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

/**
 * 合并列表数据封装类.
 * 
 * @author liqinghua
 * 
 */
public class PubDupQueryItemRow implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -859823870853483482L;
  private int rowSpan = 0;
  private PublicationRol publicationRol;

  public int getRowSpan() {
    return rowSpan;
  }

  public PublicationRol getPublicationRol() {
    return publicationRol;
  }

  public void setRowSpan(int rowSpan) {
    this.rowSpan = rowSpan;
  }

  public void setPublicationRol(PublicationRol publicationRol) {
    this.publicationRol = publicationRol;
  }

}
