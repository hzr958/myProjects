package com.smate.web.management.model.grp;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 群组显示实体类
 *
 * @author aijiangbin
 * @create 2019-07-09 14:14
 **/
public class GrpBaseShowInfo {

  public  String  grpName = "";
  public  String  image  = "";
  public  String  brief  = "";
  public  Long  grpId  = 0L ;
  public  String  des3GrpId  = "" ;



  public String getDes3GrpId() {
    if(StringUtils.isBlank(des3GrpId)){
      des3GrpId = Des3Utils.encodeToDes3(grpId.toString());
    }
    return des3GrpId;
  }

  public void setDes3GrpId(String des3GrpId) {
    this.des3GrpId = des3GrpId;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getGrpName() {
    return grpName;
  }

  public void setGrpName(String grpName) {
    this.grpName = grpName;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getBrief() {
    return brief;
  }

  public void setBrief(String brief) {
    this.brief = brief;
  }
}
