package com.smate.core.base.dyn.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.core.base.dyn.model.DynamicContent;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.template.SmateFreeMarkerTemplateUtil;
import com.smate.core.base.utils.url.URLParseUtils;

import freemarker.template.Configuration;

/**
 * 动态内容Dao
 * 
 * @author zk
 *
 */
@Repository
public class DynamicContentDao extends BaseMongoDAO<DynamicContent> {

  protected final Logger logger = LoggerFactory.getLogger(SmateFreeMarkerTemplateUtil.class);

  @Resource(name = "freemarkerConfiguration")
  private Configuration freemarkerConfiguration;

  /**
   * 获取动态内容
   * 
   * @param dynId
   * @param locale
   * @return
   * @throws DynException
   */
  public String getDynContent(Long dynId, String locale, String platform) {
    try {
      DynamicContent dynamicContent = super.findById(dynId.toString());
      if (dynamicContent == null) {
        return null;

      }
      if ("mobile".equals(platform)) {
        updateDynContent(dynamicContent, dynId);// 没有移动端动态数据就更新
      }
      if (StringUtils.isBlank(locale)) {
        locale = LocaleContextHolder.getLocale().toString();
      }
      if ("en_US".equals(locale)) {// 获取动态内容
        if ("mobile".equals(platform)) {
          return StringUtils.isBlank(dynamicContent.getMobileDynContentEn()) == true
              ? dynamicContent.getMobileDynContentZh()
              : dynamicContent.getMobileDynContentEn();
        } else {
          return StringUtils.isBlank(dynamicContent.getDynContentEn()) == true ? dynamicContent.getDynContentZh()
              : dynamicContent.getDynContentEn();
        }
      } else {
        if ("mobile".equals(platform)) {
          return StringUtils.isBlank(dynamicContent.getMobileDynContentZh()) == true
              ? dynamicContent.getMobileDynContentEn()
              : dynamicContent.getMobileDynContentZh();
        } else {
          return StringUtils.isBlank(dynamicContent.getDynContentZh()) == true ? dynamicContent.getDynContentEn()
              : dynamicContent.getDynContentZh();
        }
      }
    } catch (Throwable e) {
      return null;
    }

  }

  /**
   * 没有移动端数据就更新
   * 
   * @throws DynException
   */
  private void updateDynContent(DynamicContent dynamicContent, Long dynId) throws DynException {
    if (StringUtils.isBlank(dynamicContent.getMobileDynContentEn())
        && StringUtils.isBlank(dynamicContent.getMobileDynContentZh())) {//
      // 移动端中英文内容为空则创建并保存，解决旧数据
      Map<String, Object> dynMap = JacksonUtils.jsonToMap(dynamicContent.getResDetails());
      if ("dyn_B1_template_normalnew_zh_CN.ftl".equals(dynMap.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString())
          && dynMap.get("PARENT_DYNID") != null && dynId != dynMap.get("PARENT_DYNID")) {// 老数据处理使用父类的魔板生成
        dynMap = handleOldDynContent(dynamicContent, dynId);// 处理老数据
      }
      dynMap = dealMap(dynMap);
      if (dynMap != null) {
        String mobileContentZh =
            produceTemplate(dynMap, "mobile_" + dynMap.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString());
        String mobileContentEn =
            produceTemplate(dynMap, "mobile_" + dynMap.get(DynTemplateConstant.DYN_TEMPLATE_EN).toString());
        dynamicContent.setResDetails(JacksonUtils.mapToJsonStr(dynMap));
        dynamicContent.setMobileDynContentEn(mobileContentEn);
        dynamicContent.setMobileDynContentZh(mobileContentZh);
        super.save(dynamicContent);
      }
    }
  }

  /**
   * 处理有pubId
   */
  private Map<String, Object> dealMap(Map<String, Object> dynMap) {
    if (dynMap.get("HAS_DES3_PUB_ID") != null && (boolean) dynMap.get("HAS_DES3_PUB_ID")
        && dynMap.get("PUB_ID") == null) {
      if (dynMap.get("DES3_RES_ID") != null) {
        Long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(dynMap.get("DES3_RES_ID").toString()), 0);
        dynMap.put("PUB_ID", pubId);
      }
    }
    if (dynMap.get("RES_ID") != null && dynMap.get("PUB_ID") == null) {// 有些是只有res_id没有pub_id
      dynMap.put("PUB_ID", dynMap.get("RES_ID"));
    }
    if (dynMap.get("PARENT_DYN_ID") == null) {// 模板需要这个
      dynMap.put("PARENT_DYN_ID", dynMap.get("DYN_ID"));
    }
    if (dynMap.get("DES3_PRODUCER_PSN_ID") == null) {// 模板需要这个
      dynMap.put("DES3_PRODUCER_PSN_ID", dynMap.get("DES3_PSN_A_ID"));
    }
    return dynMap;
  }

  /**
   * 处理老数据
   * 
   * @param form
   */
  private Map<String, Object> handleOldDynContent(DynamicContent dynamicContent, Long dynId) throws DynException {
    Map<String, Object> dynMap = JacksonUtils.jsonToMap(dynamicContent.getResDetails());
    DynamicContent dynamicContentPare = super.findById(dynMap.get("PARENT_DYNID").toString());
    Map<String, Object> dynMapPare = JacksonUtils.jsonToMap(dynamicContentPare.getResDetails());

    dynMapPare = dealMap(dynMapPare);
    String mobileOriginalZh = null;
    String mobileOriginalEn = null;
    if (dynMapPare != null) {
      mobileOriginalZh =
          produceTemplate(dynMapPare, "mobile_" + dynMapPare.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString());
      mobileOriginalEn =
          produceTemplate(dynMapPare, "mobile_" + dynMapPare.get(DynTemplateConstant.DYN_TEMPLATE_EN).toString());

      mobileOriginalZh = mobileOriginalZh.replaceAll("dynamic mt20", "").replaceAll("expand_more", "");
      mobileOriginalEn = mobileOriginalEn.replaceAll("dynamic mt20", "").replaceAll("expand_more", "");
    }
    try {
      Document docZh = URLParseUtils.getDocByString(mobileOriginalZh, "");
      docZh.select("#time_" + dynMap.get("PARENT_DYNID")).get(0).attr("id", "time_" + dynId);
      mobileOriginalZh = docZh.toString();
      Document docEn = URLParseUtils.getDocByString(mobileOriginalEn, "");
      docEn.select("#time_" + dynMap.get("PARENT_DYNID")).get(0).attr("id", "time_" + dynId);
      mobileOriginalEn = docEn.toString();
    } catch (Exception e) {
      throw new DynException("老模板解析document出错..", e);
    }

    dynMap.put(DynTemplateConstant.MOBILE_ORIGINAL_TEMPLATE_ZH, mobileOriginalZh);
    dynMap.put(DynTemplateConstant.MOBILE_ORIGINAL_TEMPLATE_EN, mobileOriginalEn);
    return dynMap;
  }

  /**
   * 获取动态信息
   * 
   * @param dynId
   * @return
   */
  public String getDynInfo(Long dynId) {
    String resDetails = null;
    DynamicContent dynamicContent = super.findById(dynId.toString());
    if (dynamicContent != null) {
      resDetails = dynamicContent.getResDetails();
    }
    return resDetails;
  }

  /**
   * 填充数据构造模板
   * 
   * @param params
   * @return
   * @throws DynException
   */
  public String produceTemplate(Map<String, Object> params, String dynTemplate) throws DynException {
    String content = "";
    try {
      if (StringUtils.isBlank(dynTemplate)) {
        return "";
      }
      content = FreeMarkerTemplateUtils
          .processTemplateIntoString(freemarkerConfiguration.getTemplate(dynTemplate, EmailConstants.ENCODING), params);
    } catch (Exception e) {
      logger.error("生成模板数据出错", e);
      throw new DynException(e);

    }
    return content;

  }
}
