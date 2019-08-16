package com.smate.web.v8pub.dom.pdwh;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 导入基准库原始数据
 * 
 * @author zll
 * 
 */
@Document(collection = "V_ORIGINAL_DATA")
public class OriginalDataDOM implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -3277297124937013272L;
  @Id
  @Indexed
  protected Long pubId; // 成果id
  protected String originalData;// 原始数据

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getOriginalData() {
    return originalData;
  }

  public void setOriginalData(String originalData) {
    this.originalData = originalData;
  }
}
