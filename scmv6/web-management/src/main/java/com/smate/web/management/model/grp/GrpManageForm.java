package com.smate.web.management.model.grp;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组管理form表单
 *
 * @author aijiangbin
 * @create 2019-07-09 14:05
 **/
public class GrpManageForm {
  private Long psnId = 0L;
  private Long grpId = 0L;  // 群组id
  private String des3gGrpId = "";  // 群组id
  private String des3PsnId = "";
  private String searchKey = "";
  private Page<GrpBaseinfo> page = new Page();
  private List<GrpBaseShowInfo> showInfos = new ArrayList<>();
  private Integer  count = 0  ;
  private List<SearchPubShowInfo> pubInfo =  new ArrayList<>();
  private String desGrpIds = "" ;   //  群组id
  private String pdwhPubIds = "" ;  //  基准库成果id



  public String getDes3gGrpId() {
    return des3gGrpId;
  }

  public void setDes3gGrpId(String des3gGrpId) {
    this.des3gGrpId = des3gGrpId;
  }

  public String getDesGrpIds() {
    return desGrpIds;
  }

  public void setDesGrpIds(String desGrpIds) {
    this.desGrpIds = desGrpIds;
  }

  public String getPdwhPubIds() {
    return pdwhPubIds;
  }

  public void setPdwhPubIds(String pdwhPubIds) {
    this.pdwhPubIds = pdwhPubIds;
  }

  public List<SearchPubShowInfo> getPubInfo() {
    return pubInfo;
  }

  public void setPubInfo(List<SearchPubShowInfo> pubInfo) {
    this.pubInfo = pubInfo;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Integer getCount() {
    if(NumberUtils.isZero(count))   {
      count = showInfos.size();
    }
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Page<GrpBaseinfo> getPage() {
    return page;
  }

  public void setPage(Page<GrpBaseinfo> page) {
    this.page = page;
  }

  public List<GrpBaseShowInfo> getShowInfos() {
    return showInfos;
  }

  public void setShowInfos(List<GrpBaseShowInfo> showInfos) {
    this.showInfos = showInfos;
  }

  public String getSearchKey() {
    if(StringUtils.isNotBlank(searchKey)){
      searchKey = searchKey.toUpperCase();
    }
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Long getPsnId() {
    if(NumberUtils.isNullOrZero(psnId)){
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDes3PsnId() {
    if (this.psnId != null && des3PsnId == null) {
      des3PsnId = ServiceUtil.encodeToDes3(this.psnId.toString());
    }
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }
}
