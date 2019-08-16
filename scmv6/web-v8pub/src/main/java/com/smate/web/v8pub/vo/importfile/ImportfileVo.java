package com.smate.web.v8pub.vo.importfile;

import com.smate.web.v8pub.po.pubType.ConstPubType;
import com.smate.web.v8pub.vo.PendingImportPubVO;

import java.io.Serializable;
import java.util.List;

public class ImportfileVo implements Serializable {
  private static final long serialVersionUID = 1L;
  // 参考PublicationArticleType
  private Integer publicationArticleType;// 成果大类别1、成果。2、文献
  private String dbType;// 数据来源
  @SuppressWarnings("rawtypes")
  private List<PendingImportPubVO> pubList;// 成果对象
  private List<ConstPubType> pubTypeList;// 全部成果类型
  private String cacheKey;
  private String des3PubFileId ="";

  public String getDes3PubFileId() {
    return des3PubFileId;
  }

  public void setDes3PubFileId(String des3PubFileId) {
    this.des3PubFileId = des3PubFileId;
  }

  @SuppressWarnings("rawtypes")
  public List<PendingImportPubVO> getPubList() {
    return pubList;
  }

  @SuppressWarnings("rawtypes")
  public void setPubList(List<PendingImportPubVO> pubList) {
    this.pubList = pubList;
  }

  public List<ConstPubType> getPubTypeList() {
    return pubTypeList;
  }

  public void setPubTypeList(List<ConstPubType> pubTypeList) {
    this.pubTypeList = pubTypeList;
  }

  public Integer getPublicationArticleType() {
    return publicationArticleType;
  }

  public void setPublicationArticleType(Integer publicationArticleType) {
    this.publicationArticleType = publicationArticleType;
  }

  public String getDbType() {
    return dbType;
  }

  public void setDbType(String dbType) {
    this.dbType = dbType;
  }

  public String getCacheKey() {
    return cacheKey;
  }

  public void setCacheKey(String cacheKey) {
    this.cacheKey = cacheKey;
  }


}
