package com.smate.web.management.model.analysis;

import java.io.Serializable;

public class KeywordsDistrForm implements Serializable {

  /**
   * 研究主题词分布显示
   */
  private static final long serialVersionUID = -5571637897637552317L;

  private int mId;// 左菜单Id
  private String mName;// 左菜单名,学科名
  private int modId;// 研究关键分析分类，1:任务2:关系3：论文
  private String mmId;
  private String selectedId;


  public int getModId() {
    return modId;
  }

  public void setModId(int modId) {
    this.modId = modId;
  }

  public int getmId() {
    return mId;
  }

  public void setmId(int mId) {
    this.mId = mId;
  }

  public String getmName() {
    return mName;
  }

  public void setmName(String mName) {
    this.mName = mName;
  }

  public String getMmId() {
    return mmId;
  }

  public void setMmId(String mmId) {
    this.mmId = mmId;
  }

  public String getSelectedId() {
    return selectedId;
  }

  public void setSelectedId(String selectedId) {
    this.selectedId = selectedId;
  }

}
