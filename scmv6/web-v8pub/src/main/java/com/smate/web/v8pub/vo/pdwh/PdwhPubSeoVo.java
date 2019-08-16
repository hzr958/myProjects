package com.smate.web.v8pub.vo.pdwh;

import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.model.Page;
import com.smate.web.v8pub.po.seo.PubIndexThirdLevel;

public class PdwhPubSeoVo {
  private String key1;// code
  private Integer key2;// level
  private String lan;
  private String pageNo;
  public Integer pageSize = 200;

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  private Page<PubIndexThirdLevel> page = new Page<PubIndexThirdLevel>(200);
  private List<Map<String, String>> IndexSecondMapList;

  public String getKey1() {
    return key1;
  }

  public void setKey1(String key1) {
    this.key1 = key1;
  }

  public Integer getKey2() {
    return key2;
  }

  public void setKey2(Integer key2) {
    this.key2 = key2;
  }

  public String getLan() {
    return lan;
  }

  public void setLan(String lan) {
    this.lan = lan;
  }

  public String getPageNo() {
    return pageNo;
  }

  public void setPageNo(String pageNo) {
    this.pageNo = pageNo;
  }

  public Page<PubIndexThirdLevel> getPage() {
    return page;
  }

  public void setPage(Page<PubIndexThirdLevel> page) {
    this.page = page;
  }

  public List<Map<String, String>> getIndexSecondMapList() {
    return IndexSecondMapList;
  }

  public void setIndexSecondMapList(List<Map<String, String>> indexSecondMapList) {
    IndexSecondMapList = indexSecondMapList;
  }

}
