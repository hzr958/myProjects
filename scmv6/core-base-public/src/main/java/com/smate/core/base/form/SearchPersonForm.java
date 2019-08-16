package com.smate.core.base.form;

import com.smate.core.base.utils.model.security.Person;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 检索人员的form表单
 * 
 * @author aijiangbin
 * @date 2018年8月24日
 */
public class SearchPersonForm {

  public String searchKey; // 关键词检索，模糊检索
  public String name; // 姓名
  public String insName;// 机构单位
  public String position; // 职称
  public String email; // 邮件

  public Integer pageNo = 1;
  public Integer pageSize = 10;
  public Integer count = 0; // 统计数

  public List<Person> result = null;

  public String getName() {
    if (StringUtils.isNotBlank(name)) {
      return name.toLowerCase().trim();
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInsName() {
    if (StringUtils.isNotBlank(insName)) {
      return insName.toLowerCase().trim();
    }
    return insName;
  }

  public void setInsName(String insName) {
    this.insName = insName;
  }

  public String getPosition() {
    if (StringUtils.isNotBlank(position)) {
      return position.toLowerCase().trim();
    }
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getEmail() {
    if (StringUtils.isNotBlank(this.email)) {
      this.email = this.email.toLowerCase().trim();
    }
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public String getSearchKey() {
    if (StringUtils.isNotBlank(searchKey)) {
      searchKey = searchKey.trim().toLowerCase();
    }
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public List<Person> getResult() {
    return result;
  }

  public void setResult(List<Person> result) {
    this.result = result;
  }


}
