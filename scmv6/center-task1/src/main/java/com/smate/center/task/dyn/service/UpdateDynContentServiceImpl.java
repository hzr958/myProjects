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

import com.smate.center.task.dao.sns.quartz.PubSimpleDao;
import com.smate.center.task.dyn.dao.base.DynContentUpdateStatusDao;
import com.smate.center.task.dyn.dao.base.DynamicContentDao;
import com.smate.center.task.dyn.dao.base.DynamicMsgDao;
import com.smate.center.task.dyn.model.base.DynContentUpdateStatus;
import com.smate.center.task.dyn.model.base.DynamicContent;
import com.smate.center.task.dyn.model.base.DynamicMsg;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.template.SmateFreeMarkerTemplateUtil;
import com.smate.core.base.utils.url.URLParseUtils;

@Service("updateDynContentService")
@Transactional(rollbackFor = Exception.class)
public class UpdateDynContentServiceImpl implements UpdateDynContentService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynContentUpdateStatusDao dynContentUpdateStatusDao;
  @Autowired
  private SmateFreeMarkerTemplateUtil smateFreeMarkerTemplateUtil;
  @Autowired
  private DynamicContentDao dynamicContentDao;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;

  @Override
  public List<DynContentUpdateStatus> getDynMsgInfo(int batchSize) throws Exception {
    return dynContentUpdateStatusDao.BatchGetDynList(batchSize);
  }

  @Override
  public void updateDynContentById(DynContentUpdateStatus dynMsg) throws Exception {
    try {
      // 生成新动态内容
      // 获取参数信息
      DynamicContent dynconContent = dynamicContentDao.findById(dynMsg.getDynId().toString());

      if (dynconContent == null) {
        this.saveUpdateStatus(dynMsg, 2, "获取到的动态内容为空，dynId:" + dynMsg.getDynId());
        return;
      }
      String resDetails = dynconContent.getResDetails();
      Map<String, Object> dynMap = JacksonUtils.jsonToMap(resDetails);
      // 处理某些参数中PARENT_DYN_ID为空导致freemark报错问题
      if (dynMap.get("PARENT_DYN_ID") == null) {
        dynMap.put("PARENT_DYN_ID", dynMsg.getDynId());
      }
      DynamicMsg dynamicMsg = dynamicMsgDao.get(dynMsg.getDynId());
      dynMap.put(DynTemplateConstant.DES3_DYN_ID, Des3Utils.encodeToDes3(dynamicMsg.getDynId().toString()));// 加密动态id
      dynMap.put(DynTemplateConstant.DES3_PARENT_DYN_ID, Des3Utils.encodeToDes3(dynamicMsg.getSameFlag().toString()));// 加密父动态id
      dynMap.put(DynTemplateConstant.DES3_RES_ID, Des3Utils.encodeToDes3(dynamicMsg.getResId().toString()));// 加密资源id
      dynMap.put(DynTemplateConstant.DYN_OWNER_DES3_ID, ServiceUtil.encodeToDes3(dynamicMsg.getProducer().toString()));
      if (objIsNotEmpty(dynMap.get(DynTemplateConstant.DES3_PUB_ID))) {
        dynMap.put(DynTemplateConstant.DES3_RES_ID, dynMap.get(DynTemplateConstant.DES3_PUB_ID));// 加密资源id
      }
      if (!"B1TEMP".equals(dynamicMsg.getDynType())) {
        if ("0".equals(dynMap.get(DynTemplateConstant.RES_TYPE).toString())) {
          if (objIsNotEmpty(dynMap.get(DynTemplateConstant.LINK_TITLE_ZH))
              || objIsNotEmpty(dynMap.get(DynTemplateConstant.LINK_TITLE_EN))) {
            dynMap.put(DynTemplateConstant.RES_TYPE, 1);
          }
          if (objIsNotEmpty(dynMap.get(DynTemplateConstant.FUND_TITLE_ZH))
              || objIsNotEmpty(dynMap.get(DynTemplateConstant.FUND_TITLE_EN))) {
            dynMap.put(DynTemplateConstant.RES_TYPE, 11);
          }
          if (objIsNotEmpty(dynMap.get(DynTemplateConstant.DB_ID))) {
            dynMap.put(DynTemplateConstant.RES_TYPE, 22);
          }
        } else {
          if (objIsNotEmpty(dynMap.get(DynTemplateConstant.FUND_TITLE_ZH))
              || objIsNotEmpty(dynMap.get(DynTemplateConstant.FUND_TITLE_EN))) {
            dynMap.put(DynTemplateConstant.RES_TYPE, 11);
          }
          if (objIsNotEmpty(dynMap.get(DynTemplateConstant.DB_ID))) {
            dynMap.put(DynTemplateConstant.RES_TYPE, 22);
          }
        }
        if (dynamicMsg.getResType().equals(11) || "11".equals(dynMap.get(DynTemplateConstant.RES_TYPE).toString())) {
          if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.DES3_PUB_ID))) {
            dynMap.put(DynTemplateConstant.DES3_PUB_ID, dynMap.get(DynTemplateConstant.ENCODE_FUND_ID));
          }
          if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.DES3_RES_ID))) {
            dynMap.put(DynTemplateConstant.DES3_RES_ID, dynMap.get(DynTemplateConstant.ENCODE_FUND_ID));
          }
          if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.LINK_TITLE_EN))) {
            dynMap.put(DynTemplateConstant.LINK_TITLE_EN, dynMap.get(DynTemplateConstant.FUND_TITLE_EN));
          }
          if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.LINK_TITLE_ZH))) {
            dynMap.put(DynTemplateConstant.LINK_TITLE_ZH, dynMap.get(DynTemplateConstant.FUND_TITLE_ZH));
          }
          if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.PUB_DESCR_ZH))) {
            dynMap.put(DynTemplateConstant.PUB_DESCR_ZH, dynMap.get(DynTemplateConstant.FUND_DESC_ZH));
          }
          if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.PUB_DESCR_EN))) {
            dynMap.put(DynTemplateConstant.PUB_DESCR_EN, dynMap.get(DynTemplateConstant.FUND_DESC_EN));
          }
          if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.LINK_IMAGE))) {
            dynMap.put(DynTemplateConstant.LINK_IMAGE, dynMap.get(DynTemplateConstant.FUND_LOGO_URL));
          }
        }
      }
      if (objIsNotEmpty(dynMap.get(DynTemplateConstant.RES_ID))) {
        dynMap.put(DynTemplateConstant.DES3_RES_ID,
            Des3Utils.encodeToDes3(dynMap.get(DynTemplateConstant.RES_ID).toString()));// 加密资源id
      }
      if (objIsNotEmpty(dynMap.get(DynTemplateConstant.PUB_ID))) {
        if (NumberUtils.isNumber(dynMap.get(DynTemplateConstant.PUB_ID).toString())) {
          dynMap.put(DynTemplateConstant.RES_ID, dynMap.get(DynTemplateConstant.PUB_ID));// 加密资源id
          dynMap.put(DynTemplateConstant.DES3_RES_ID,
              Des3Utils.encodeToDes3(dynMap.get(DynTemplateConstant.PUB_ID).toString()));// 加密资源id
        } else {
          dynMap.put(DynTemplateConstant.DES3_RES_ID, dynMap.get(DynTemplateConstant.PUB_ID).toString());// 加密资源id
        }
      }
      if (dynamicMsg.getResType().equals(1) || dynamicMsg.getResType().equals(2)) {
        Long pubOwnerPsnId = pubSimpleDao.getPubOwnerPsnId(dynamicMsg.getResId());
        if (pubOwnerPsnId != null) {
          dynMap.put(DynTemplateConstant.RES_OWNER_DES3ID, Des3Utils.encodeToDes3(pubOwnerPsnId.toString()));
        }
      }
      switch (dynamicMsg.getDynType()) {
        case "ATEMP":
          dynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_A_template_normalnew_zh_CN.ftl");
          dynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_A_template_normalnew_en_CN.ftl");
          dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_A_template_normalnew_zh_CN.ftl");
          dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_A_template_normalnew_en_CN.ftl");
          break;
        case "B1TEMP":
          dynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B1_template_normalnew_zh_CN.ftl");
          dynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B1_template_normalnew_en_CN.ftl");
          dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B1_template_normalnew_zh_CN.ftl");
          dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B1_template_normalnew_en_CN.ftl");
          break;
        case "B2TEMP":
          dynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B2_template_normalnew_zh_CN.ftl");
          dynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B2_template_normalnew_en_CN.ftl");
          dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B2_template_normalnew_zh_CN.ftl");
          dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B2_template_normalnew_en_CN.ftl");
          break;
        case "B3TEMP":
          dynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B3_template_normalnew_zh_CN.ftl");
          dynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B3_template_normalnew_en_CN.ftl");
          dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B3_template_normalnew_zh_CN.ftl");
          dynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B3_template_normalnew_en_CN.ftl");
          break;
        default:
          break;
      }

      // 处理ATEMP中 ,HAS_DES3_PUB_ID为true，pubId不存在的问题
      if ("ATEMP".equals(dynamicMsg.getDynType()) && dynMap.get("HAS_DES3_PUB_ID") != null) {
        try {
          if ((Boolean) dynMap.get("HAS_DES3_PUB_ID") == true && dynMap.get("PUB_ID") == null) {
            dynMap.put("PUB_ID", Long.valueOf(ServiceUtil.decodeFromDes3(dynMap.get("DES3_RES_ID").toString())));
          }
        } catch (NullPointerException | NumberFormatException e) {
          this.saveUpdateStatus(dynMsg, 2, "添加pubId出错，DES3_RES_ID为空");
          return;
        }
      }

      // 包含父动态处理，获取父动态重新生成并更新
      if (dynMap.get("PARENT_DYN_ID").toString().equals(dynMsg.getDynId().toString()) == false
          && "B1TEMP".equals(dynamicMsg.getDynType())) {
        try {
          dynMap = this.handleParent(dynMap);
        } catch (Exception e) {
          logger.error("处理父动态出错！", e);
          throw new Exception(e);
        }

      }

      // 处理历史数据B1 B2类型
      // DES3_PRODUCER_PSN_ID（动态发布人ID）为空，DES3_PSN_A_ID（动态操作人Id）
      if ("B1TEMP".equals(dynamicMsg.getDynType()) || "B2TEMP".equals(dynamicMsg.getDynType())) {
        if (dynMap.get("DES3_PRODUCER_PSN_ID") == null) {
          dynMap.put("DES3_PRODUCER_PSN_ID", dynMap.get("DES3_PSN_A_ID"));
        }
      }
      String contentZh = smateFreeMarkerTemplateUtil.produceTemplate(dynMap,
          dynMap.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString());
      String contentEn = smateFreeMarkerTemplateUtil.produceTemplate(dynMap,
          dynMap.get(DynTemplateConstant.DYN_TEMPLATE_EN).toString());

      String mobileContentZh = smateFreeMarkerTemplateUtil.produceTemplate(dynMap,
          dynMap.get(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH).toString());
      String mobileContentEn = smateFreeMarkerTemplateUtil.produceTemplate(dynMap,
          dynMap.get(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN).toString());
      // 更新动态内容
      dynconContent.setDynContentEn(contentEn);
      dynconContent.setDynContentZh(contentZh);

      dynconContent.setMobileDynContentEn(mobileContentEn);
      dynconContent.setMobileDynContentZh(mobileContentZh);

      dynamicContentDao.update(dynconContent);
      this.saveUpdateStatus(dynMsg, 1, "");
    } catch (Exception e) {
      logger.error("更新动态内容出错，dynId:" + dynMsg.getDynId(), e);
      String msg = null;
      try {
        msg = StringUtils.isEmpty(e.getMessage()) == true ? e.toString() : e.getMessage().substring(0, 200);
      } catch (Exception e2) {
        msg = "更新动态任务错误日志获取出错";
      }
      this.saveUpdateStatus(dynMsg, 2, msg);
    }
  }

  public static void main(String[] args) {
    String s = "qiHKAn1j6XPO%2B4xSy%2Fdouw%3D%3D";
    String decodeFromDes3 = Des3Utils.decodeFromDes3(s);
    System.out.println(decodeFromDes3);
  }

  private boolean objIsNotEmpty(Object obj) {
    if (obj != null && !"".equals(obj.toString())) {
      return true;
    }
    return false;
  }

  /**
   * 处理父动态数据
   * 
   * @param dynMap
   * @return
   * @throws DynException
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> handleParent(Map<String, Object> dynMap) throws DynException {

    Object parentId = dynMap.get("PARENT_DYN_ID");
    DynamicContent parentContent = dynamicContentDao.findById(parentId.toString());
    String parentresDetails = parentContent.getResDetails();

    Map<String, Object> parentdynMap = JacksonUtils.jsonToMap(parentresDetails);
    // 处理某些参数中PARENT_DYN_ID为空导致freemark报错问题
    if (parentdynMap.get("PARENT_DYN_ID") == null) {
      parentdynMap.put("PARENT_DYN_ID", parentId);
    }

    // 处理ATEMP中 ,HAS_DES3_PUB_ID为true，pubId不存在的问题
    if ("ATEMP".equals(parentdynMap.get("DYN_TYPE")) && parentdynMap.get("HAS_DES3_PUB_ID") != null) {

      if ((Boolean) parentdynMap.get("HAS_DES3_PUB_ID") == true && parentdynMap.get("PUB_ID") == null) {
        parentdynMap.put("PUB_ID",
            Long.valueOf(ServiceUtil.decodeFromDes3(parentdynMap.get("DES3_RES_ID").toString())));
      }
    }
    DynamicMsg dynamicMsg = dynamicMsgDao.get(Long.parseLong(parentId.toString()));
    parentdynMap.put(DynTemplateConstant.DES3_DYN_ID, Des3Utils.encodeToDes3(dynamicMsg.getDynId().toString()));// 加密动态id
    parentdynMap.put(DynTemplateConstant.DES3_PARENT_DYN_ID,
        Des3Utils.encodeToDes3(dynamicMsg.getSameFlag().toString()));// 加密父动态id
    parentdynMap.put(DynTemplateConstant.DES3_RES_ID, Des3Utils.encodeToDes3(dynamicMsg.getResId().toString()));// 加密资源id
    parentdynMap.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(dynamicMsg.getProducer().toString()));
    if ("0".equals(parentdynMap.get(DynTemplateConstant.RES_TYPE).toString())) {
      if (objIsNotEmpty(parentdynMap.get(DynTemplateConstant.LINK_TITLE_ZH))
          || objIsNotEmpty(parentdynMap.get(DynTemplateConstant.LINK_TITLE_EN))) {
        parentdynMap.put(DynTemplateConstant.RES_TYPE, 1);
      }
      if (objIsNotEmpty(parentdynMap.get(DynTemplateConstant.FUND_TITLE_ZH))
          || objIsNotEmpty(parentdynMap.get(DynTemplateConstant.FUND_TITLE_EN))) {
        parentdynMap.put(DynTemplateConstant.RES_TYPE, 11);
      }
      if (objIsNotEmpty(parentdynMap.get(DynTemplateConstant.DB_ID))) {
        parentdynMap.put(DynTemplateConstant.RES_TYPE, 22);
      }
    } else {
      if (objIsNotEmpty(parentdynMap.get(DynTemplateConstant.FUND_TITLE_ZH))
          || objIsNotEmpty(parentdynMap.get(DynTemplateConstant.FUND_TITLE_EN))) {
        parentdynMap.put(DynTemplateConstant.RES_TYPE, 11);
      }
      if (objIsNotEmpty(parentdynMap.get(DynTemplateConstant.DB_ID))) {
        parentdynMap.put(DynTemplateConstant.RES_TYPE, 22);
      }
    }

    if (objIsNotEmpty(parentdynMap.get(DynTemplateConstant.DES3_PUB_ID))) {
      parentdynMap.put(DynTemplateConstant.DES3_RES_ID, parentdynMap.get(DynTemplateConstant.DES3_PUB_ID));// 加密资源id
    }
    if (objIsNotEmpty(parentdynMap.get(DynTemplateConstant.RES_ID))) {
      parentdynMap.put(DynTemplateConstant.DES3_RES_ID,
          Des3Utils.encodeToDes3(parentdynMap.get(DynTemplateConstant.RES_ID).toString()));// 加密资源id
    }
    if (objIsNotEmpty(parentdynMap.get(DynTemplateConstant.PUB_ID))) {
      if (NumberUtils.isNumber(parentdynMap.get(DynTemplateConstant.PUB_ID).toString())) {
        parentdynMap.put(DynTemplateConstant.RES_ID, parentdynMap.get(DynTemplateConstant.PUB_ID));// 加密资源id
        parentdynMap.put(DynTemplateConstant.DES3_RES_ID,
            Des3Utils.encodeToDes3(parentdynMap.get(DynTemplateConstant.PUB_ID).toString()));// 加密资源id
      } else {
        parentdynMap.put(DynTemplateConstant.DES3_RES_ID, parentdynMap.get(DynTemplateConstant.PUB_ID).toString());// 加密资源id
      }
    }
    if (dynamicMsg.getResType().equals(1) || dynamicMsg.getResType().equals(2)) {
      Long pubOwnerPsnId = pubSimpleDao.getPubOwnerPsnId(dynamicMsg.getResId());
      if (pubOwnerPsnId != null) {
        parentdynMap.put(DynTemplateConstant.RES_OWNER_DES3ID, Des3Utils.encodeToDes3(pubOwnerPsnId.toString()));
      }
    }
    switch (dynamicMsg.getDynType()) {
      case "ATEMP":
        parentdynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_A_template_normalnew_zh_CN.ftl");
        parentdynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_A_template_normalnew_en_CN.ftl");
        parentdynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_A_template_normalnew_zh_CN.ftl");
        parentdynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_A_template_normalnew_en_CN.ftl");
        break;
      case "B1TEMP":
        parentdynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B1_template_normalnew_zh_CN.ftl");
        parentdynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B1_template_normalnew_en_CN.ftl");
        parentdynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B1_template_normalnew_zh_CN.ftl");
        parentdynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B1_template_normalnew_en_CN.ftl");
        break;
      case "B2TEMP":
        parentdynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B2_template_normalnew_zh_CN.ftl");
        parentdynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B2_template_normalnew_en_CN.ftl");
        parentdynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B2_template_normalnew_zh_CN.ftl");
        parentdynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B2_template_normalnew_en_CN.ftl");
        break;
      case "B3TEMP":
        parentdynMap.put(DynTemplateConstant.DYN_TEMPLATE_ZH, "dyn_B3_template_normalnew_zh_CN.ftl");
        parentdynMap.put(DynTemplateConstant.DYN_TEMPLATE_EN, "dyn_B3_template_normalnew_en_CN.ftl");
        parentdynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH, "mobile_dyn_B3_template_normalnew_zh_CN.ftl");
        parentdynMap.put(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN, "mobile_dyn_B3_template_normalnew_en_CN.ftl");
        break;
      default:
        break;
    }
    if (dynamicMsg.getResType().equals(11) || "11".equals(parentdynMap.get(DynTemplateConstant.RES_TYPE).toString())) {
      if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.DES3_PUB_ID))) {
        parentdynMap.put(DynTemplateConstant.DES3_PUB_ID, dynMap.get(DynTemplateConstant.ENCODE_FUND_ID));
      }
      if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.DES3_RES_ID))) {
        parentdynMap.put(DynTemplateConstant.DES3_RES_ID, dynMap.get(DynTemplateConstant.ENCODE_FUND_ID));
      }
      if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.LINK_TITLE_EN))) {
        parentdynMap.put(DynTemplateConstant.LINK_TITLE_EN, dynMap.get(DynTemplateConstant.FUND_TITLE_EN));
      }
      if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.LINK_TITLE_ZH))) {
        parentdynMap.put(DynTemplateConstant.LINK_TITLE_ZH, dynMap.get(DynTemplateConstant.FUND_TITLE_ZH));
      }
      if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.PUB_DESCR_ZH))) {
        parentdynMap.put(DynTemplateConstant.PUB_DESCR_ZH, dynMap.get(DynTemplateConstant.FUND_DESC_ZH));
      }
      if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.PUB_DESCR_EN))) {
        parentdynMap.put(DynTemplateConstant.PUB_DESCR_EN, dynMap.get(DynTemplateConstant.FUND_DESC_EN));
      }
      if (!objIsNotEmpty(dynMap.get(DynTemplateConstant.LINK_IMAGE))) {
        parentdynMap.put(DynTemplateConstant.LINK_IMAGE, dynMap.get(DynTemplateConstant.FUND_LOGO_URL));
      }
    }

    String originalZh = smateFreeMarkerTemplateUtil.produceTemplate(parentdynMap,
        parentdynMap.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString());
    String originalEn = smateFreeMarkerTemplateUtil.produceTemplate(parentdynMap,
        parentdynMap.get(DynTemplateConstant.DYN_TEMPLATE_EN).toString());

    String mobileContentZh = smateFreeMarkerTemplateUtil.produceTemplate(parentdynMap,
        parentdynMap.get(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH).toString());
    String mobileContentEn = smateFreeMarkerTemplateUtil.produceTemplate(parentdynMap,
        parentdynMap.get(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN).toString());
    mobileContentZh = mobileContentZh.replaceAll("dynamic mt20", "").replaceAll("expand_more", "");
    mobileContentEn = mobileContentEn.replaceAll("dynamic mt20", "").replaceAll("expand_more", "");

    try {
      Document docZh = URLParseUtils.getDocByString(originalZh, "");
      docZh.select("#time_" + parentdynMap.get("DYN_ID")).get(0).attr("id", "time_" + dynMap.get("DYN_ID"));
      originalZh = docZh.toString();
      Document docEn = URLParseUtils.getDocByString(originalEn, "");
      docEn.select("#time_" + parentdynMap.get("DYN_ID")).get(0).attr("id", "time_" + dynMap.get("DYN_ID"));
      originalEn = docEn.toString();

      dynMap.remove("ORIGINAL_TEMPLATE_ZH");
      dynMap.remove("ORIGINAL_TEMPLATE_EN");
      dynMap.put("ORIGINAL_TEMPLATE_ZH", originalZh);
      dynMap.put("ORIGINAL_TEMPLATE_EN", originalEn);
    } catch (Exception e) {
      logger.error("pc端解析旧decument出错");
    }

    try {
      Document docZh2 = URLParseUtils.getDocByString(mobileContentZh, "");
      docZh2.select("#time_" + parentdynMap.get("DYN_ID")).get(0).attr("id", "time_" + dynMap.get("DYN_ID"));
      mobileContentZh = docZh2.toString();
      Document docEn2 = URLParseUtils.getDocByString(mobileContentEn, "");
      docEn2.select("#time_" + parentdynMap.get("DYN_ID")).get(0).attr("id", "time_" + dynMap.get("DYN_ID"));
      mobileContentEn = docEn2.toString();

      dynMap.remove("MOBILE_ORIGINAL_TEMPLATE_ZH");
      dynMap.remove("MOBILE_ORIGINAL_TEMPLATE_EN");
      dynMap.put(DynTemplateConstant.MOBILE_ORIGINAL_TEMPLATE_ZH, mobileContentZh);
      dynMap.put(DynTemplateConstant.MOBILE_ORIGINAL_TEMPLATE_EN, mobileContentEn);
    } catch (Exception e) {
      logger.error("移动端解析旧decument出错");
    }
    return dynMap;

  }

  /**
   * 保存更新状态信息
   * 
   * @param dynMsg
   * @param status
   * @param message
   */
  public void saveUpdateStatus(DynContentUpdateStatus dynMsg, int status, String message) {
    dynMsg.setUpdateDate(new Date());
    dynMsg.setUpdateStatus(status);
    dynMsg.setUpdateMsg(message);
    dynContentUpdateStatusDao.save(dynMsg);
  }

  @Override
  public boolean getTaskStatus() {
    Long needHandleCount = dynContentUpdateStatusDao.getNeedHandleCount();

    if (needHandleCount > 0L) {
      return true;
    }
    return false;
  }

}
