package com.smate.web.dyn.service.dynamic;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.dyn.dao.DynamicContentDao;
import com.smate.core.base.dyn.model.DynamicContent;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.url.URLParseUtils;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.service.psn.InsInfoService;
import com.smate.web.dyn.service.psn.PersonQueryservice;

/**
 * B1 类动态模板
 */
public class DynamicB1typeServiceImpl extends DynamicDataDealBaseService {

  public final static String DYN_TEMPLATE_ZH = "dyn_B1_template_normalnew_zh_CN.ftl";
  public final static String DYN_TEMPLATE_EN = "dyn_B1_template_normalnew_en_CN.ftl";
  public final static String MOBILE_DYN_TEMPLATE_ZH = "mobile_dyn_B1_template_normalnew_zh_CN.ftl";
  public final static String MOBILE_DYN_TEMPLATE_EN = "mobile_dyn_B1_template_normalnew_en_CN.ftl";
  @Autowired
  private DynamicContentDao dynamicContentDao;

  @Override
  public String checkData(DynamicForm form) {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      return "模板人员  id为空！";
    }
    if (form.getResType() == null) {
      return "资源类型为空！";
    }
    if (form.getParentDynId() == null) {
      return "old的动态id为空！";
    }
    return null;
  }

  @Override
  public void dealData(DynamicForm form) throws DynException {
    HashMap<String, Object> dataMap = new HashMap<String, Object>();
    // 获取旧的动态信息
    if (form.getParentDynId() == null || form.getParentDynId() == 0L) {
      throw new DynException("parentDynId为空！");
    }
    if (form.getPsnId() != null) {
      dataMap = this.handlePsnInfo(form, dataMap);
      dataMap = this.handleInsInfo(form, dataMap);
      dataMap.put(DynTemplateConstant.DES3_PSN_A_ID, Des3Utils.encodeToDes3(form.getPsnId().toString()));
    }
    if (form.getResId() != null && form.getPubId() == null) {
      form.setPubId(form.getResId());
    }
    dataMap.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    // 如果resId为null，则给dynId
    if (form.getResId() == null) {
      form.setResId(form.getDynId());
    } else {
      // 注释理由：B1类型是读取数据库的动态信息，不需要去查资源信息
      /*
       * //根据资源类型构造对应的数据---- switch (form.getResType()) { case DynamicConstants.RES_TYPE_PUB://成果
       * dataMap.put(DynTemplateConstant.PUB_ID, form.getResId()); this.handlePubInfo(form, dataMap);
       * break; case DynamicConstants.RES_TYPE_FUND://基金 this.handleFundInfo2(form, dataMap);
       * form.setShareFund(true); break; case DynamicConstants.RES_TYPE_PUB_PDWH://基准库成果 case
       * DynamicConstants.RES_TYPE_JOURNALAWARD: this.producePdwhDynamic(form, dataMap); break; //TODO
       * 以后有其他资源类型以此类推添加 default: break; }
       */
    }
    // 删除快速分享，B1添加动态内容
    dataMap.put(DynTemplateConstant.USER_ADD_CONTENT, form.getDynText());// 动态内容
    dataMap = this.handleOldDynContent(form, dataMap);
    // 获取操作类型（1：评论了，2：赞了 ，3：分享了）
    if (form.getOperatorType() == null && form.getOperatorType() == 0) {
      throw new DynException("operatorType 操作类型不能为空  或者 不是数字类型！");
    }
    dataMap.put(DynTemplateConstant.DES3_PUB_ID, Des3Utils.encodeToDes3(form.getPubId().toString()));
    dataMap.put(DynTemplateConstant.PUB_ID, form.getPubId());
    dataMap.put(DynTemplateConstant.OPERATOR_TYPE_EN, DynTemplateConstant.OPERATOR_VAL_EN[form.getOperatorType() - 1]);
    dataMap.put(DynTemplateConstant.OPERATOR_TYPE_ZH, DynTemplateConstant.OPERATOR_VAL[form.getOperatorType() - 1]);
    dataMap.put(DynTemplateConstant.OPERATE_STATUS, form.getOperatorType());
    dataMap.put(DynTemplateConstant.DYN_ID, form.getDynId());
    dataMap.put(DynTemplateConstant.DES3_DYN_ID, Des3Utils.encodeToDes3(form.getDynId().toString()));
    dataMap.put(DynTemplateConstant.PARENT_DYN_ID, form.getParentDynId());
    dataMap.put(DynTemplateConstant.DES3_PARENT_DYN_ID, Des3Utils.encodeToDes3(form.getParentDynId().toString()));
    // 评论的内容 如果评论内容为空，就用成果标题
    if (form.getOperatorType() == 1) {
      if (StringUtils.isBlank(form.getReplyContent())) {
        dataMap.put(DynTemplateConstant.OPERATOR_COMMENT, form.getReplyPubTitle());
      } else {
        dataMap.put(DynTemplateConstant.OPERATOR_COMMENT, form.getReplyContent());
      }
    }
    dataMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, MOBILE_DYN_TEMPLATE_ZH);
    dataMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, MOBILE_DYN_TEMPLATE_EN);
    dataMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, DYN_TEMPLATE_ZH);
    dataMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, DYN_TEMPLATE_EN);
    form.setDynMap(dataMap);;
  }

  /**
   * 处理老数据
   * 
   * @param form
   */
  private HashMap<String, Object> handleOldDynContent(DynamicForm form, HashMap<String, Object> dataMap)
      throws DynException {

    DynamicContent oldDynContent = dynamicContentDao.findById(form.getParentDynId().toString());
    if (oldDynContent == null || StringUtils.isBlank(oldDynContent.getDynContentEn())
        || StringUtils.isBlank(oldDynContent.getDynContentZh())) {
      throw new DynException("处理老动态数据出错,parentDynId=" + form.getParentDynId());
    }
    String mobileOriginalZh =
        oldDynContent.getMobileDynContentZh().replaceAll("dynamic mt20", "").replaceAll("expand_more", "");
    String mobileOriginalEn =
        oldDynContent.getMobileDynContentEn().replaceAll("dynamic mt20", "").replaceAll("expand_more", "");

    String originalZh = oldDynContent.getDynContentZh().replaceAll("dynamic mt20", "").replaceAll("expand_more", "");
    String originalEn = oldDynContent.getDynContentEn().replaceAll("dynamic mt20", "").replaceAll("expand_more", "");

    try {

      Document docZh = URLParseUtils.getDocByString(originalZh, "");
      docZh.select("#time_" + form.getParentDynId()).get(0).attr("id", "time_" + form.getDynId());
      originalZh = docZh.toString();
      Document docEn = URLParseUtils.getDocByString(originalEn, "");
      docEn.select("#time_" + form.getParentDynId()).get(0).attr("id", "time_" + form.getDynId());
      originalEn = docEn.toString();

      Document mobiledocZh = URLParseUtils.getDocByString(mobileOriginalZh, "");
      mobiledocZh.select("#time_" + form.getParentDynId()).get(0).attr("id", "time_" + form.getDynId());
      mobileOriginalZh = mobiledocZh.toString();
      Document mobiledocEn = URLParseUtils.getDocByString(mobileOriginalEn, "");
      mobiledocEn.select("#time_" + form.getParentDynId()).get(0).attr("id", "time_" + form.getDynId());
      mobileOriginalEn = mobiledocEn.toString();
    } catch (Exception e) {
      throw new DynException("老模板解析document出错..", e);
    }

    dataMap.put(DynTemplateConstant.MOBILE_ORIGINAL_TEMPLATE_ZH, mobileOriginalZh);// 移动端用
    dataMap.put(DynTemplateConstant.MOBILE_ORIGINAL_TEMPLATE_EN, mobileOriginalEn);

    dataMap.put(DynTemplateConstant.ORIGINAL_TEMPLATE_ZH, originalZh);
    dataMap.put(DynTemplateConstant.ORIGINAL_TEMPLATE_EN, originalEn);
    dataMap.put(DynTemplateConstant.PARENT_DYNID, form.getParentDynId());
    return dataMap;
  }

}
