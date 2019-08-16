package com.smate.web.v8pub.vo;

/**
 * 其他相似全文信息
 * 
 * @author yhx
 *
 */
public class PubFulltextSimilarVO {

  private String fulltextImg;// 全文图片
  private String psnName;// 全文所属成果的拥有者名称
  private String psnTitle;// 全文所属成果的拥有者头衔(机构+部门+职称)
  private String fulltextName;// 全文名称
  private Long fulltextSize;// 全文大小
  private String downloadUrl;// 全文下载链接

  public String getFulltextImg() {
    return fulltextImg;
  }

  public void setFulltextImg(String fulltextImg) {
    this.fulltextImg = fulltextImg;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public String getPsnTitle() {
    return psnTitle;
  }

  public void setPsnTitle(String psnTitle) {
    this.psnTitle = psnTitle;
  }

  public String getFulltextName() {
    return fulltextName;
  }

  public void setFulltextName(String fulltextName) {
    this.fulltextName = fulltextName;
  }

  public Long getFulltextSize() {
    return fulltextSize;
  }

  public void setFulltextSize(Long fulltextSize) {
    this.fulltextSize = fulltextSize;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

}
