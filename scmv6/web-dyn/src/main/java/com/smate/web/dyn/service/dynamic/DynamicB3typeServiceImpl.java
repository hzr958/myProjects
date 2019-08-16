package com.smate.web.dyn.service.dynamic;

import java.util.HashMap;
import java.util.Map;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.dyn.form.dynamic.DynamicForm;

/**
 * B3 类动态模板
 */
public class DynamicB3typeServiceImpl extends DynamicDataDealBaseService {

  public final static String DYN_TEMPLATE_ZH = "dyn_B3_template_normalnew_zh_CN.ftl";
  public final static String DYN_TEMPLATE_EN = "dyn_B3_template_normalnew_en_CN.ftl";
  public final static String MOBILE_DYN_TEMPLATE_ZH = "mobile_dyn_B3_template_normalnew_zh_CN.ftl";
  public final static String MOBILE_DYN_TEMPLATE_EN = "mobile_dyn_B3_template_normalnew_en_CN.ftl";

  @Override
  public String checkData(DynamicForm form) {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      return "模板人员  id为空！";
    }
    if (form.getResType() == null && form.getResType() != 1) {
      return "资源类型为空或不等于1！";
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void dealData(DynamicForm form) throws DynException {
    Map<String, Object> paramMap = JacksonUtils.jsonToMap(form.getParamJson());
    HashMap<String, Object> dataMap = new HashMap<String, Object>();
    if (paramMap == null || paramMap.isEmpty()) {
      throw new DynException("json参数paramMap不能为空");
    }
    Long pubId = Long.parseLong(paramMap.get("pubId").toString());
    form.setResId(pubId);
    form.setPubId(pubId);
    if (form.getResId() == null) {
      throw new DynException("导入的第一条成果id为空");
    }
    if (form.getPsnId() != null) {
      dataMap = this.handlePsnInfo(form, dataMap);
      dataMap = this.handleInsInfo(form, dataMap);
    }
    dataMap.put(DynTemplateConstant.DYN_OWNER_DES3_ID, form.getDes3psnId());
    // 根据资源类型构造对应的数据----
    switch (form.getResType()) {
      case DynamicConstants.RES_TYPE_PUB:// 成果
        dataMap.put(DynTemplateConstant.PUB_ID, form.getResId());
        this.handlePubInfo(form, dataMap);
        break;
      case DynamicConstants.RES_TYPE_FUND:// 基金
        this.handleFundInfo2(form, dataMap);
        form.setShareFund(true);
        // 操作类型
        dataMap.put(DynTemplateConstant.OPERATOR_TYPE_EN,
            DynTemplateConstant.FUND_OPERATOR_VAL_EN_NEW[form.getOperatorType() - 2]);
        dataMap.put(DynTemplateConstant.OPERATOR_TYPE_ZH,
            DynTemplateConstant.FUND_OPERATOR_VAL_NEW[form.getOperatorType() - 2]);
        break;
      case DynamicConstants.RES_TYPE_PUB_PDWH:// 基准库成果
      case DynamicConstants.RES_TYPE_JOURNALAWARD:
        this.producePdwhDynamic(form, dataMap);
        dataMap.put(DynTemplateConstant.DB_ID, form.getDbId());
        break;
      case DynamicConstants.RES_TYPE_PRJ:// 项目
        this.handlePrjInfo(form, dataMap);
        break;
      case DynamicConstants.RES_TYPE_AGENCY:// 资助机构
        this.handleAgencyInfo(form, dataMap);
        // 操作类型
        dataMap.put(DynTemplateConstant.OPERATOR_TYPE_EN,
            DynTemplateConstant.AGENCY_OPERATOR_VAL_EN_NEW[form.getOperatorType() - 2]);
        dataMap.put(DynTemplateConstant.OPERATOR_TYPE_ZH,
            DynTemplateConstant.AGENCY_OPERATOR_VAL_NEW[form.getOperatorType() - 2]);
        break;
      case DynamicConstants.RES_TYPE_NEWS:
        this.handleNewsInfo(form, dataMap);
        // 操作类型
        dataMap.put(DynTemplateConstant.OPERATOR_TYPE_EN,
            DynTemplateConstant.FUND_OPERATOR_VAL_EN_NEW[form.getOperatorType() - 2]);
        dataMap.put(DynTemplateConstant.OPERATOR_TYPE_ZH,
            DynTemplateConstant.FUND_OPERATOR_VAL_NEW[form.getOperatorType() - 2]);
        break;
      // TODO 以后有其他资源类型以此类推添加
      default:
        break;
    }
    // 存在成果
    dataMap.put(DynTemplateConstant.PUB_ID, form.getPubId());
    dataMap.put(DynTemplateConstant.DES3_PUB_ID, Des3Utils.encodeToDes3(form.getPubId().toString()));
    dataMap.put(DynTemplateConstant.OPERATOR_TYPE_ZH, "上传了");
    dataMap.put(DynTemplateConstant.OPERATOR_TYPE_EN, "uploaded");
    dataMap.put(DynTemplateConstant.DYN_TYPE, form.getDynType());
    dataMap.put(DynTemplateConstant.DYN_ID, form.getDynId());
    dataMap.put(DynTemplateConstant.DES3_DYN_ID, Des3Utils.encodeToDes3(form.getDynId().toString()));// 加密动态id
    dataMap.put(DynTemplateConstant.PARENT_DYN_ID, form.getParentDynId());
    dataMap.put(DynTemplateConstant.DES3_PARENT_DYN_ID, Des3Utils.encodeToDes3(form.getParentDynId().toString()));// 加密父动态id
    dataMap.put(DynTemplateConstant.RES_TYPE, form.getResType());
    dataMap.put(DynTemplateConstant.RES_ID, form.getResId());
    dataMap.put(DynTemplateConstant.DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));

    dataMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, MOBILE_DYN_TEMPLATE_ZH);
    dataMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, MOBILE_DYN_TEMPLATE_EN);
    dataMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, DYN_TEMPLATE_ZH);
    dataMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, DYN_TEMPLATE_EN);
    form.setDynMap(dataMap);

  }

}
