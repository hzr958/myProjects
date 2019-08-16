package com.smate.center.task.dyn.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dyn.dao.base.DynamicContentDao;
import com.smate.center.task.dyn.dao.base.DynamicMsgDao;
import com.smate.center.task.dyn.dao.base.MobileDynContentUpdateDao;
import com.smate.center.task.dyn.model.base.DynamicContent;
import com.smate.center.task.dyn.model.base.DynamicMsg;
import com.smate.center.task.dyn.model.base.MobileDynContentUpdate;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.template.SmateFreeMarkerTemplateUtil;
import com.smate.core.base.utils.url.URLParseUtils;

@Service("updateMobileDynContentService")
@Transactional(rollbackFor = Exception.class)
public class UpdateMobileDynContentServiceImpl implements UpdateMobileDynContentService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MobileDynContentUpdateDao mobileDynContentUpdateDao;
  @Autowired
  private SmateFreeMarkerTemplateUtil smateFreeMarkerTemplateUtil;
  @Autowired
  private DynamicContentDao dynamicContentDao;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;

  @Override
  public List<MobileDynContentUpdate> getDynMsgInfo(int batchSize) throws Exception {
    return mobileDynContentUpdateDao.BatchGetDynList(batchSize);
  }

  @Override
  public void updateMobileDynContentById(MobileDynContentUpdate dynMsg) throws Exception {
    try {
      // 生成新动态内容
      // 获取参数信息
      DynamicContent dynconContent = dynamicContentDao.findById(dynMsg.getDynId().toString());

      if (dynconContent == null) {
        this.saveUpdateStatus(dynMsg, 2, "获取到的动态内容为空，dynId:" + dynMsg.getDynId());
        return;
      }
      updateDynContent(dynconContent, dynMsg.getDynId());
      this.saveUpdateStatus(dynMsg, 1, "");
    } catch (Exception e) {
      logger.error("更新动态内容出错，dynId:" + dynMsg.getDynId());
      String msg = null;
      try {
        msg = StringUtils.isEmpty(e.getMessage()) == true ? e.toString() : e.getMessage().substring(0, 200);
      } catch (Exception e2) {
        msg = "更新动态任务错误日志获取出错";
      }
      this.saveUpdateStatus(dynMsg, 2, msg);
    }
  }

  /**
   * 动端数据就更新
   * 
   * @throws DynException
   */
  private void updateDynContent(DynamicContent dynamicContent, Long dynId) throws DynException {
    Map<String, Object> dynMap = JacksonUtils.jsonToMap(dynamicContent.getResDetails());
    if ("dyn_B1_template_normalnew_zh_CN.ftl".equals(dynMap.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString())
        && dynMap.get("PARENT_DYNID") != null && dynId != dynMap.get("PARENT_DYNID")) {// 老数据处理使用父类的魔板生成
      dynMap = handleOldDynContent(dynamicContent, dynId);// 处理老数据
    }
    dynMap = dealMap(dynMap);
    if (dynMap != null) {
      DynamicMsg dynamicMsg = dynamicMsgDao.get(dynId);
      dynMap.put(DynTemplateConstant.DES3_DYN_ID, Des3Utils.encodeToDes3(dynamicMsg.getDynId().toString()));// 加密动态id
      dynMap.put(DynTemplateConstant.DES3_PARENT_DYN_ID, Des3Utils.encodeToDes3(dynamicMsg.getSameFlag().toString()));// 加密父动态id
      dynMap.put(DynTemplateConstant.DES3_RES_ID, Des3Utils.encodeToDes3(dynamicMsg.getResId().toString()));// 加密资源id
      dynMap.put(DynTemplateConstant.DYN_OWNER_DES3_ID, ServiceUtil.encodeToDes3(dynamicMsg.getProducer().toString()));
      if (dynamicMsg.getResType() == 11) {
        if (ObjectUtils.toString(dynMap.get(DynTemplateConstant.DES3_PUB_ID)) == "") {
          dynMap.put(DynTemplateConstant.DES3_PUB_ID, dynMap.get(DynTemplateConstant.ENCODE_FUND_ID));
        }
        if (ObjectUtils.toString(dynMap.get(DynTemplateConstant.DES3_RES_ID)) == "") {
          dynMap.put(DynTemplateConstant.DES3_RES_ID, dynMap.get(DynTemplateConstant.ENCODE_FUND_ID));
        }
        if (ObjectUtils.toString(dynMap.get(DynTemplateConstant.LINK_TITLE_EN)) == "") {
          dynMap.put(DynTemplateConstant.LINK_TITLE_EN, dynMap.get(DynTemplateConstant.FUND_TITLE_EN));
        }
        if (ObjectUtils.toString(dynMap.get(DynTemplateConstant.LINK_TITLE_ZH)) == "") {
          dynMap.put(DynTemplateConstant.LINK_TITLE_ZH, dynMap.get(DynTemplateConstant.FUND_TITLE_ZH));
        }
        if (ObjectUtils.toString(dynMap.get(DynTemplateConstant.PUB_DESCR_ZH)) == "") {
          dynMap.put(DynTemplateConstant.PUB_DESCR_ZH, dynMap.get(DynTemplateConstant.FUND_DESC_ZH));
        }
        if (ObjectUtils.toString(dynMap.get(DynTemplateConstant.PUB_DESCR_EN)) == "") {
          dynMap.put(DynTemplateConstant.PUB_DESCR_EN, dynMap.get(DynTemplateConstant.FUND_DESC_EN));
        }
        if (ObjectUtils.toString(dynMap.get(DynTemplateConstant.LINK_IMAGE)) == "") {
          dynMap.put(DynTemplateConstant.LINK_IMAGE, dynMap.get(DynTemplateConstant.FUND_LOGO_URL));
        }
        switch (dynamicMsg.getDynType()) {
          case "ATEMP":
            dynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_A_template_normalnew_en_CN.ftl");
            dynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_A_template_normalnew_zh_CN.ftl");
            dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_A_template_normalnew_en_CN.ftl");
            dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_A_template_normalnew_zh_CN.ftl");
            break;
          case "B1TEMP":
            dynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B1_template_normalnew_en_CN.ftl");
            dynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B1_template_normalnew_zh_CN.ftl");
            dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B1_template_normalnew_en_CN.ftl");
            dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B1_template_normalnew_zh_CN.ftl");
            break;
          case "B2TEMP":
            dynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B2_template_normalnew_en_CN.ftl");
            dynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B2_template_normalnew_zh_CN.ftl");
            dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B2_template_normalnew_en_CN.ftl");
            dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B2_template_normalnew_zh_CN.ftl");
            break;
          case "B3TEMP":
            dynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B3_template_normalnew_en_CN.ftl");
            dynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B3_template_normalnew_zh_CN.ftl");
            dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B3_template_normalnew_en_CN.ftl");
            dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B3_template_normalnew_zh_CN.ftl");
            break;
          default:
            break;
        }
      }

      String mobileContentZh = smateFreeMarkerTemplateUtil.produceTemplate(dynMap,
          "mobile_" + dynMap.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString());
      String mobileContentEn = smateFreeMarkerTemplateUtil.produceTemplate(dynMap,
          "mobile_" + dynMap.get(DynTemplateConstant.DYN_TEMPLATE_EN).toString());
      dynamicContent.setResDetails(JacksonUtils.mapToJsonStr(dynMap));
      dynamicContent.setMobileDynContentEn(mobileContentEn);
      dynamicContent.setMobileDynContentZh(mobileContentZh);
      dynamicContentDao.update(dynamicContent);
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
    DynamicContent dynamicContentPare = dynamicContentDao.findById(dynMap.get("PARENT_DYNID").toString());
    Map<String, Object> dynMapPare = JacksonUtils.jsonToMap(dynamicContentPare.getResDetails());

    dynMapPare = dealMap(dynMapPare);
    String mobileOriginalZh = null;
    String mobileOriginalEn = null;
    if (dynMapPare != null) {
      mobileOriginalZh = smateFreeMarkerTemplateUtil.produceTemplate(dynMapPare,
          "mobile_" + dynMapPare.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString());
      mobileOriginalEn = smateFreeMarkerTemplateUtil.produceTemplate(dynMapPare,
          "mobile_" + dynMapPare.get(DynTemplateConstant.DYN_TEMPLATE_EN).toString());

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
   * 保存更新状态信息
   * 
   * @param dynMsg
   * @param status
   * @param message
   */
  public void saveUpdateStatus(MobileDynContentUpdate dynMsg, int status, String message) {
    dynMsg.setUpdateDate(new Date());
    dynMsg.setUpdateStatus(status);
    dynMsg.setUpdateMsg(message);
    mobileDynContentUpdateDao.save(dynMsg);
  }

  @Override
  public boolean getTaskStatus() {
    Long needHandleCount = mobileDynContentUpdateDao.getNeedHandleCount();

    if (needHandleCount > 0L) {
      return true;
    }
    return false;
  }

}
