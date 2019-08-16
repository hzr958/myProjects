package com.smate.web.data.form.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.data.model.pub.HKeywordsItem;

/**
 * 成果图表展示form
 * 
 * @author lhd
 *
 */
public class PubEchartsForm {

  private Long pubId;// 成果id
  private String des3PubId;// 加密的成果id
  private List<String> groupList;// 学部
  private String locale;// 语言环境
  private Map<String, Object> showMap;// 页面展示用
  private Boolean zhAndEn = false;// 中英文关键词 true:中英文都有
  private String keywords;// 关键词,多个用分号分割
  private List<HKeywordsItem> showList;

  public Long getPubId() {
    if (pubId == null && StringUtils.isNotBlank(des3PubId)) {
      if (StringUtils.isNotBlank(ServiceUtil.decodeFromDes3(des3PubId))) {
        pubId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PubId));
      }
    }
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getDes3PubId() {
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public List<String> getGroupList() {
    return groupList;
  }

  public void setGroupList(List<String> groupList) {
    this.groupList = groupList;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public Map<String, Object> getShowMap() {
    return showMap;
  }

  public void setShowMap(Map<String, Object> showMap) {
    this.showMap = showMap;
  }

  public Boolean getZhAndEn() {
    return zhAndEn;
  }

  public void setZhAndEn(Boolean zhAndEn) {
    this.zhAndEn = zhAndEn;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public List<HKeywordsItem> getShowList() {
    return showList;
  }

  public void setShowList(List<HKeywordsItem> showList) {
    this.showList = showList;
  }

}
