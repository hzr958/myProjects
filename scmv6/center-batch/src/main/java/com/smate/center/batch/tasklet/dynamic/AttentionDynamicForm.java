package com.smate.center.batch.tasklet.dynamic;

public class AttentionDynamicForm {

  public Long psnId; // 当前人
  public Long attPsnId; // 添加关注 取消关注的人
  public Integer status; // 0 === 关注某人 ， 1==取消关注某人
  public Integer size = 100;
  public Integer pageNo = 1;
  public Integer count = 0;
  public Integer totalPage = 0;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getAttPsnId() {
    return attPsnId;
  }

  public void setAttPsnId(Long attPsnId) {
    this.attPsnId = attPsnId;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {

    if (count != null || count != 0) {
      if (count % size == 0) {
        totalPage = count / size;
      } else {
        totalPage = count / size + 1;
      }
    }

    this.count = count;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(Integer totalPage) {
    this.totalPage = totalPage;
  }



}
