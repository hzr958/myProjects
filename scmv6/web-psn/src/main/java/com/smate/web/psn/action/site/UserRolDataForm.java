package com.smate.web.psn.action.site;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.security.TheadLocalInsId;

public class UserRolDataForm {

  private String locale;

  private Long rolInsId = 0L; // 当前机构站点 ，默认为科研之友

  private List<InsPortalShow> portal;

  private String des3PsnId;
  private Long psnId;

  private Long sieVersion = 0L;

  public Long getRolInsId() {
    if (!(TheadLocalInsId.getInsId() == null)) {
      rolInsId = TheadLocalInsId.getInsId();
    }
    return rolInsId;
  }

  public void setRolInsId(Long rolInsId) {
    this.rolInsId = rolInsId;
  }

  public List<InsPortalShow> getPortal() {
    return portal;
  }

  public void setPortal(List<InsPortalShow> portal) {
    this.portal = portal;
  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  public Long getPsnId() {
    if (this.psnId == null) {
      this.psnId = SecurityUtils.getCurrentUserId();
    }
    if ((this.psnId == null || this.psnId == 0L) && StringUtils.isNotBlank(this.des3PsnId)) {
      this.psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3PsnId));
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getSieVersion() {
    return sieVersion;
  }

  public void setSieVersion(Long sieVersion) {
    this.sieVersion = sieVersion;
  }

  public String getLocale() {
    if (StringUtils.isBlank(locale)) {
      locale = LocaleContextHolder.getLocale().toString();
    }
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

}
