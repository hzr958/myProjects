package com.smate.web.dyn.service.dynamic;

import java.util.HashMap;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * B2 类动态模板
 */
public class DynamicB2typeServiceImpl extends DynamicDataDealBaseService {

  public final static String DYN_TEMPLATE_ZH = "dyn_B2_template_normalnew_zh_CN.ftl";
  public final static String DYN_TEMPLATE_EN = "dyn_B2_template_normalnew_en_CN.ftl";
  public final static String MOBILE_DYN_TEMPLATE_ZH = "mobile_dyn_B2_template_normalnew_zh_CN.ftl";
  public final static String MOBILE_DYN_TEMPLATE_EN = "mobile_dyn_B2_template_normalnew_en_CN.ftl";
  // 基金

  @Override
  public String checkData(DynamicForm form) {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      return "模板人员  id为空！";
    }
    if (form.getResType() == null && form.getResType() != 1) {
      return "资源类型为空或不等于1！";
    }
    if (form.getResId() == null && form.getPubId() == null) {
      return "resid为空！";
    }
    // 获取操作类型operatorType（1：评论了，2：赞了 ，3：分享了）
    if (form.getOperatorType() == null) {
      return "操作类型为空！";
    }
    return null;
  }

  @Override
  public void dealData(DynamicForm form) throws DynException {
    HashMap<String, Object> dataMap = new HashMap<String, Object>();
    if (form.getResId() == null && form.getPubId() != null) {
      form.setResId(form.getPubId());
    }
    if (form.getPubId() == null && form.getResId() != null) {
      form.setPubId(form.getResId());
    }

    dataMap.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));

    // 操作类型、描述
    String OPERATOR_TYPE_EN = "";
    String OPERATOR_TYPE_ZH = "";
    // 根据资源类型构造对应的数据----
    switch (form.getResType()) {
      case DynamicConstants.RES_TYPE_REF:
      case DynamicConstants.RES_TYPE_PUB:// 成果
        dataMap.put(DynTemplateConstant.PUB_ID, form.getResId());
        this.handlePubInfo(form, dataMap);
        OPERATOR_TYPE_EN = DynTemplateConstant.OPERATOR_VAL_EN_NEW[form.getOperatorType() - 1];
        OPERATOR_TYPE_ZH = DynTemplateConstant.OPERATOR_VAL_NEW[form.getOperatorType() - 1];
        break;
      case DynamicConstants.RES_TYPE_FUND:// 基金
        this.handleFundInfo2(form, dataMap);
        form.setShareFund(true);
        OPERATOR_TYPE_EN = DynTemplateConstant.FUND_OPERATOR_VAL_EN_NEW[form.getOperatorType() - 2];
        OPERATOR_TYPE_ZH = DynTemplateConstant.FUND_OPERATOR_VAL_NEW[form.getOperatorType() - 2];
        break;
      case DynamicConstants.RES_TYPE_PUB_PDWH:// 基准库成果
      case DynamicConstants.RES_TYPE_JOURNALAWARD:
        this.producePdwhDynamic(form, dataMap);
        dataMap.put(DynTemplateConstant.DB_ID, form.getDbId());
        OPERATOR_TYPE_EN = DynTemplateConstant.OPERATOR_VAL_EN_NEW[form.getOperatorType() - 1];
        OPERATOR_TYPE_ZH = DynTemplateConstant.OPERATOR_VAL_NEW[form.getOperatorType() - 1];
        break;
      case DynamicConstants.RES_TYPE_PRJ:// 项目
        this.handlePrjInfo(form, dataMap);
        OPERATOR_TYPE_EN = DynTemplateConstant.PRJ_OPERATOR_VAL_EN_NEW[form.getOperatorType() - 1];
        OPERATOR_TYPE_ZH = DynTemplateConstant.PRJ_OPERATOR_VAL_NEW[form.getOperatorType() - 1];
        break;
      case DynamicConstants.RES_TYPE_AGENCY:// 资助机构
        this.handleAgencyInfo(form, dataMap);
        OPERATOR_TYPE_EN = DynTemplateConstant.AGENCY_OPERATOR_VAL_EN_NEW[form.getOperatorType() - 2];
        OPERATOR_TYPE_ZH = DynTemplateConstant.AGENCY_OPERATOR_VAL_NEW[form.getOperatorType() - 2];
        break;
      case DynamicConstants.RES_TYPE_NEWS:
        this.handleNewsInfo(form, dataMap);
        OPERATOR_TYPE_EN = DynTemplateConstant.FUND_OPERATOR_VAL_EN_NEW[form.getOperatorType() - 2];
        OPERATOR_TYPE_ZH = DynTemplateConstant.FUND_OPERATOR_VAL_NEW[form.getOperatorType() - 2];
        break;
      // TODO 以后有其他资源类型以此类推添加
      default:
        break;
    }
    dataMap.put(DynTemplateConstant.OPERATOR_TYPE_EN, OPERATOR_TYPE_EN);
    dataMap.put(DynTemplateConstant.OPERATOR_TYPE_ZH, OPERATOR_TYPE_ZH);

    // 人员信息
    if (form.getPsnId() != null) {
      dataMap = this.handlePsnInfo(form, dataMap);
      dataMap = this.handleInsInfo(form, dataMap);
    }
    // 基本参数
    form.setPubId(form.getResId());
    dataMap.put(DynTemplateConstant.PUB_ID, form.getPubId());
    dataMap.put(DynTemplateConstant.DES3_PUB_ID, Des3Utils.encodeToDes3(form.getPubId().toString()));
    dataMap.put(DynTemplateConstant.RES_ID, form.getResId());
    dataMap.put(DynTemplateConstant.DES3_RES_ID, form.getDes3ResId());
    dataMap.put(DynTemplateConstant.DYN_ID, form.getDynId());
    dataMap.put(DynTemplateConstant.DES3_DYN_ID, Des3Utils.encodeToDes3(form.getDynId().toString()));// 加密动态id
    dataMap.put(DynTemplateConstant.PARENT_DYN_ID, form.getParentDynId());
    dataMap.put(DynTemplateConstant.DES3_PARENT_DYN_ID, Des3Utils.encodeToDes3(form.getParentDynId().toString()));// 加密父动态id
    dataMap.put(DynTemplateConstant.RES_TYPE, form.getResType());
    dataMap.put(DynTemplateConstant.DYN_TYPE, form.getDynType());
    dataMap.put(DynTemplateConstant.OPERATE_STATUS, form.getOperatorType());

    dataMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, DYN_TEMPLATE_ZH);
    dataMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, DYN_TEMPLATE_EN);
    dataMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, MOBILE_DYN_TEMPLATE_ZH);
    dataMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, MOBILE_DYN_TEMPLATE_EN);
    form.setDynMap(dataMap);
  }

}
