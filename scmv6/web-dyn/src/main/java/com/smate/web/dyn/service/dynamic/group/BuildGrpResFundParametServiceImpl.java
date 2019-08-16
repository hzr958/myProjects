package com.smate.web.dyn.service.dynamic.group;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.constant.GroupDynConstant;
import com.smate.web.dyn.dao.fund.ConstFundAgencyDao;
import com.smate.web.dyn.dao.fund.ConstFundCategoryDao;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;
import com.smate.web.dyn.model.fund.ConstFundAgency;
import com.smate.web.dyn.model.fund.ConstFundCategory;

@Transactional(rollbackFor = Exception.class)
public class BuildGrpResFundParametServiceImpl extends BuildResParametServiceBase {

  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;

  @Override
  public Boolean doVerify(GroupDynamicForm form) {
    ConstFundCategory fund = constFundCategoryDao.get(form.getResId());
    if (fund == null) {
      return false;
    }
    return true;
  }

  @Override
  public void doHandler(Map<String, Object> data, GroupDynamicForm form) {
    ConstFundCategory fund = constFundCategoryDao.get(form.getResId());
    data.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    // 这里的资源id是 基金id
    if (fund != null) {
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
      data.put(GroupDynConstant.TEMPLATE_DATA_DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_NAME, this.dealNullVal(getShowTitle(fund, "zh_CN")));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_NAME, this.dealNullVal(getShowTitle(fund, "en_US")));
      // String agencyName = this.buildFundAgencyName(fund);
      // String scienceArea = "科技领域";
      // String applyTime = this.dealNullVal(buildFundApplyTime(fund));
      if (StringUtils.isNotBlank(form.getResInfoJson())) {
        Map<String, Object> map = JacksonUtils.jsonToMap(form.getResInfoJson());
        if (map != null) {
          data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_DESC, map.get("fund_desc_en"));
          data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_DESC, map.get("fund_desc_zh"));
        }
      } else {
        String agencyName = this.buildFundAgencyName(fund);
        String applyTime = this.dealNullVal(buildFundApplyTime(fund));
        data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_DESC, this.buildFundDesc(agencyName, null, applyTime));
        data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_DESC, this.buildFundDesc(agencyName, null, applyTime));
      }
      // 资助机构，科技领域，起止时间
      // data.put(GroupDynConstant.TEMPLATE_DATA_RES_DESC,
      // this.buildFundDesc(agencyName, scienceArea, applyTime));
      data.put(MsgConstants.MSG_FUND_ID, form.getResId());
      // logo
      if (fund.getAgencyId() != null) {
        String fundurl = constFundAgencyDao.findFundAgencyLogoUrl(fund.getAgencyId());
        if (!StringUtil.isBlank(fundurl) && fundurl.contains("http")) {
          data.put(GroupDynConstant.TEMPLATE_DATA_FUND_LOGO_URL, fundurl);
        } else {
          data.put(GroupDynConstant.TEMPLATE_DATA_FUND_LOGO_URL, "/resmod" + this.dealNullVal(fundurl));
        }
      }
      // 标题
      // data.put(GroupDynConstant.TEMPLATE_DATA_RES_NAME,
      // this.dealNullVal(getShowTitle(fund,
      // LocaleContextHolder.getLocale().toString())));
    }

  }

  private String buildFundApplyTime(ConstFundCategory fund) {
    SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
    Date startTime = fund.getStartDate();
    Date endTime = fund.getEndDate();
    String start = "";
    String end = "";
    if (startTime != null) {
      start = smf.format(startTime);
    }
    if (endTime != null) {
      end = smf.format(endTime);
    }
    if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
      return start + " ~ " + end;
    } else {
      return start + end;
    }
  }

  private String buildFundAgencyName(ConstFundCategory fund) {
    String viewAgencyName = "";
    if (fund.getAgencyId() > 0) {
      ConstFundAgency agency = constFundAgencyDao.findFundAgencyName(fund.getAgencyId());
      if (agency != null) {
        String locale = LocaleContextHolder.getLocale().toString();
        if ("en_US".equals(locale)) {
          viewAgencyName = StringUtils.isNotBlank(agency.getNameEn()) ? agency.getNameEn() : agency.getNameZh();
        } else {
          viewAgencyName = StringUtils.isNotBlank(agency.getNameZh()) ? agency.getNameZh() : agency.getNameEn();
        }
      }
    }
    return viewAgencyName;
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
  private String getShowTitle(ConstFundCategory fund, String locale) {
    if ("en_US".equals(locale)) {
      return StringUtils.isNotBlank(fund.getNameEn()) ? fund.getNameEn() : fund.getNameZh();
    } else {
      return StringUtils.isNotBlank(fund.getNameZh()) ? fund.getNameZh() : fund.getNameEn();
    }
  }

  // 构建基金描述信息
  public String buildFundDesc(String agencyName, String scienceArea, String applyTime) {
    // String locale = LocaleContextHolder.getLocale().toString();
    String showDesc = "";
    if (StringUtils.isNotBlank(agencyName)) {
      showDesc += agencyName;
    }
    if (StringUtils.isNotBlank(scienceArea)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += ", " + scienceArea;
      } else {
        showDesc += scienceArea;
      }
    }
    if (StringUtils.isNotBlank(applyTime)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += ", " + applyTime;
      } else {
        showDesc += applyTime;
      }
    }
    return showDesc;
  }

}
