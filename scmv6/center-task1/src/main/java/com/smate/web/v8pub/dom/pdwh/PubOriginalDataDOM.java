package com.smate.web.v8pub.dom.pdwh;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 成果原始数据
 * 
 * @author zll
 *
 */
@Document(collection = "V_PUB_ORIGINAL_DATA")
public class PubOriginalDataDOM implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -2041921155195136156L;
  @Id
  @Indexed
  private Long id = 0L;// Id
  private String pubData;// 原始数据

  public PubOriginalDataDOM(Long id, String pubData) {
    this.id = id;
    this.pubData = pubData;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPubData() {
    return pubData;
  }

  public void setPubData(String pubData) {
    this.pubData = pubData;
  }


}
