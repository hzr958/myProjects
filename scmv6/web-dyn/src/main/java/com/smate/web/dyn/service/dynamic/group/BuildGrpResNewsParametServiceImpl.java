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
import com.smate.web.dyn.dao.news.NewsBaseDao;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;
import com.smate.web.dyn.model.news.NewsBase;

@Transactional(rollbackFor = Exception.class)
public class BuildGrpResNewsParametServiceImpl extends BuildResParametServiceBase {

  @Autowired
  private NewsBaseDao newsBaseDao;

  @Override
  public Boolean doVerify(GroupDynamicForm form) {
    NewsBase news = newsBaseDao.get(form.getResId());
    if (news == null) {
      return false;
    }
    return true;
  }

  @Override
  public void doHandler(Map<String, Object> data, GroupDynamicForm form) {
    NewsBase news = newsBaseDao.get(form.getResId());
    data.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));

    if (news != null) {
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
      data.put(GroupDynConstant.TEMPLATE_DATA_DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_NAME, this.dealNullVal(news.getTitle()));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_NAME, this.dealNullVal(news.getTitle()));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_DESC, this.dealNullVal(news.getBrief()));
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_DESC, this.dealNullVal(news.getBrief()));
      data.put(MsgConstants.MSG_NEWS_ID, form.getResId());
      // logo
      String newsUrl = news.getImage();
      if (!StringUtil.isBlank(newsUrl) && newsUrl.contains("http")) {
        data.put(GroupDynConstant.TEMPLATE_DATA_NEWS_LOGO_URL, newsUrl);
      } else {
        data.put(GroupDynConstant.TEMPLATE_DATA_NEWS_LOGO_URL, "/resmod" + this.dealNullVal(newsUrl));
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
}
