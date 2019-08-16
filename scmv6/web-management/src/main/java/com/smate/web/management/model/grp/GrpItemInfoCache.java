package com.smate.web.management.model.grp;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aijiangbin
 * @create 2019-07-10 11:21
 **/
public class GrpItemInfoCache implements Serializable {

  public List<GrpItemInfo> list = new ArrayList<>();

  public Long psnId = 0L;

  public List<GrpItemInfo> getList() {
    return list;
  }

  public void setList(List<GrpItemInfo> list) {
    this.list = list;
  }

  public Long getPsnId() {
     if(NumberUtils.isNullOrZero(psnId)) {
       psnId = SecurityUtils.getCurrentUserId();
     }
     return psnId;
   }


  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }
}
