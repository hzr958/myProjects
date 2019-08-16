package com.smate.web.group.action.grp.form;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 群组成果form
 * 
 * @author tsz
 *
 */
public class GrpPubRcmdForm {

  private Long psnId;// 当前人psnId
  private String des3PsnId;
  private Long grpId;// 群组ID
  private Long pubId;// 群组成果id
  private Integer showType = 1; // 1查询一条推荐成果 2.查询多条成果
  private String pubIds; // 多个成果id 用逗号分隔
  private Integer optionType;// 曹组类型 1确认,2拒绝
  private String idD3Str;

  private GrpPubShowInfo pubInfo; // 单条成果显示信息

  private Page<GrpPubShowInfo> page = new Page<GrpPubShowInfo>();

  private String searchKey; // 检索条件

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public Long getPsnId() {
    if (psnId == null) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Override
  public String toString() {
    return "GrpPubRcmdForm [psnId=" + psnId + ", grpId=" + grpId + ", pubId=" + pubId + "]";
  }

  public Integer getShowType() {
    return showType;
  }

  public void setShowType(Integer showType) {
    this.showType = showType;
  }

  public GrpPubShowInfo getPubInfo() {
    return pubInfo;
  }

  public void setPubInfo(GrpPubShowInfo pubInfo) {
    this.pubInfo = pubInfo;
  }

  public Page<GrpPubShowInfo> getPage() {
    return page;
  }

  public void setPage(Page<GrpPubShowInfo> page) {
    this.page = page;
  }

  public String getSearchKey() {
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public Integer getOptionType() {
    return optionType;
  }

  public void setOptionType(Integer optionType) {
    this.optionType = optionType;
  }

  public String getPubIds() {
    return pubIds;
  }

  public void setPubIds(String pubIds) {
    this.pubIds = pubIds;
  }

  public String getIdD3Str() {
    return idD3Str;
  }

  public void setIdD3Str(String idD3Str) {
    this.idD3Str = idD3Str;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }
}
