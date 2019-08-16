package com.smate.web.v8pub.vo.importfile;

import java.util.List;

public class ImportSaveVo {

  private String cacheKey;// 时间戳
  private List<ChangePub> changPubList;// 修改的成果

  public String getCacheKey() {
    return cacheKey;
  }

  public void setCacheKey(String cacheKey) {
    this.cacheKey = cacheKey;
  }

  public List<ChangePub> getChangPubList() {
    return changPubList;
  }

  public void setChangPubList(List<ChangePub> changPubList) {
    this.changPubList = changPubList;
  }

}
