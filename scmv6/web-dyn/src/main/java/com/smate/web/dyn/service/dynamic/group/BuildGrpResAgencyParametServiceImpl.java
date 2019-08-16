package com.smate.web.dyn.service.dynamic.group;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.constant.GroupDynConstant;
import com.smate.web.dyn.dao.fund.ConstFundAgencyDao;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;
import com.smate.web.dyn.model.fund.ConstFundAgency;

@Transactional(rollbackFor = Exception.class)
public class BuildGrpResAgencyParametServiceImpl extends BuildResParametServiceBase {

  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;

  @Override
  public Boolean doVerify(GroupDynamicForm form) {
    ConstFundAgency fund = constFundAgencyDao.get(form.getResId());
    if (fund == null) {
      return false;
    }
    return true;
  }

  @Override
  public void doHandler(Map<String, Object> data, GroupDynamicForm form) {
    ConstFundAgency agency = constFundAgencyDao.get(form.getResId());
    data.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    // 这里的资源id是 资助机构id
    if (agency != null) {
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
      data.put(GroupDynConstant.TEMPLATE_DATA_DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_NAME, this.dealNullVal(getShowTitle(agency, "zh_CN")));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_NAME, this.dealNullVal(getShowTitle(agency, "en_US")));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_DESC, this.dealNullVal(agency.getEnAddress()));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_DESC, this.dealNullVal(agency.getAddress()));
      data.put(MsgConstants.MSG_AGENCY_ID, form.getResId());
      // logo
      String fundurl = agency.getLogoUrl();
      if (!StringUtil.isBlank(fundurl) && fundurl.contains("http")) {
        data.put(GroupDynConstant.TEMPLATE_DATA_AGENCY_LOGO_URL, fundurl);
      } else {
        data.put(GroupDynConstant.TEMPLATE_DATA_AGENCY_LOGO_URL, "/resmod" + this.dealNullVal(fundurl));
      }
    }

  }

  /**
   * 处理空值字符串，null的转为""
   * 
   * @param val
   * @return
   */
  private String dealNullVal(String val) {
    if (StringUtils.isBlank(val)) {
      return "";
    } else {
      return val;
    }
  }

  // 获取title
  private String getShowTitle(ConstFundAgency agency, String locale) {
    if ("en_US".equals(locale)) {
      return StringUtils.isNotBlank(agency.getNameEn()) ? agency.getNameEn() : agency.getNameZh();
    } else {
      return StringUtils.isNotBlank(agency.getNameZh()) ? agency.getNameZh() : agency.getNameEn();
    }
  }

}
