package com.smate.web.dyn.service.dynamic;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * 动态 A模板 数据拼接
 * 
 * @author AiJiangBin
 *
 */
public class DynamicAtypeServiceImpl extends DynamicDataDealBaseService {

  /*
   * A 类动态模板
   */
  public final static String DYN_TEMPLATE_ZH = "dyn_A_template_normalnew_zh_CN.ftl";
  public final static String DYN_TEMPLATE_EN = "dyn_A_template_normalnew_en_CN.ftl";
  public final static String MOBILE_DYN_TEMPLATE_ZH = "mobile_dyn_A_template_normalnew_zh_CN.ftl";
  public final static String MOBILE_DYN_TEMPLATE_EN = "mobile_dyn_A_template_normalnew_en_CN.ftl";

  @Override
  public String checkData(DynamicForm form) {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      return "模板人员  id为空！";
    }
    if (form.getResType() == null) {
      return "资源类型为空！";
    }
    if (form.getDynId() == null) {
      return "动态ID为空！";
    }
    return null;
  }

  @Override
  public void dealData(DynamicForm form) throws DynException {
    HashMap<String, Object> dataMap = new HashMap<String, Object>();
    // 构造个人信息
    if (form.getPsnId() != null) {
      dataMap = this.handlePsnInfo(form, dataMap);
      dataMap = this.handleInsInfo(form, dataMap);
    }
    // 如果resId为空，默认给动态id
    if (form.getResId() == null) {
      form.setResId(form.getDynId());
    }
    // 存在资源,则把资源id给resId ---其实默认应该有resId，而不是在这里传，以后会删除
    if (form.getPubId() != null) {
      form.setResId(form.getPubId());
    }
    if (form.getPubId() == null) {
      form.setPubId(form.getResId());
    }
    dataMap.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    // 根据资源类型构造对应的数据----
    switch (form.getResType()) {
      case DynamicConstants.RES_TYPE_NORMAL:// 没有资源
        break;
      case DynamicConstants.RES_TYPE_PUB:// sns成果
        dataMap.put(DynTemplateConstant.PUB_ID, form.getResId());
        this.handlePubInfo(form, dataMap);
        break;
      case DynamicConstants.RES_TYPE_FUND:// 基金
        // this.handleFundInfo(form, dataMap);
        this.handleFundInfo2(form, dataMap);
        form.setShareFund(true);
        break;
      case DynamicConstants.RES_TYPE_PUB_PDWH:// 基准库成果
      case DynamicConstants.RES_TYPE_JOURNALAWARD:
        this.producePdwhDynamic(form, dataMap);
        break;
      case DynamicConstants.RES_TYPE_PRJ:// 项目
        this.handlePrjInfo(form, dataMap);
        break;
      case DynamicConstants.RES_TYPE_AGENCY:
        this.handleAgencyInfo(form, dataMap);
        break;
      case DynamicConstants.RES_TYPE_NEWS:
        this.handleNewsInfo(form, dataMap);
        break;
      // TODO 以后有其他资源类型以此类推添加
      default:
        break;
    }
    // 定义快速需要分享的动态
    if (form.getResType() == DynamicConstants.RES_TYPE_PUB && StringUtils.isNotBlank(form.getDynText())) {
      dataMap.put(DynTemplateConstant.QUICK_SHARE_DYN, true);
    } else {
      dataMap.put(DynTemplateConstant.QUICK_SHARE_DYN, false);
    }
    // 动态的内容--不要修改这些固定的数据
    dataMap.put(DynTemplateConstant.DYN_TYPE, form.getDynType());// 动态类型
    dataMap.put(DynTemplateConstant.USER_ADD_CONTENT, form.getDynText());// 动态内容
    dataMap.put(DynTemplateConstant.DYN_ID, form.getDynId());// 动态id
    dataMap.put(DynTemplateConstant.DES3_DYN_ID, Des3Utils.encodeToDes3(form.getDynId().toString()));// 加密动态id
    dataMap.put(DynTemplateConstant.PARENT_DYN_ID, form.getParentDynId());// 父动态id
    dataMap.put(DynTemplateConstant.DES3_PARENT_DYN_ID, Des3Utils.encodeToDes3(form.getParentDynId().toString()));// 加密父动态id
    dataMap.put(DynTemplateConstant.RES_ID, form.getResId());// 资源id
    dataMap.put(DynTemplateConstant.DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));// 加密资源id
    dataMap.put(DynTemplateConstant.RES_TYPE, form.getResType());// 动态类型

    dataMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, DYN_TEMPLATE_ZH);
    dataMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, DYN_TEMPLATE_EN);
    dataMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, MOBILE_DYN_TEMPLATE_ZH);
    dataMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, MOBILE_DYN_TEMPLATE_EN);
    form.setDynMap(dataMap);
  }

}
