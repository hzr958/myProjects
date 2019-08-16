package com.smate.web.dyn.service.dynamic.group;

import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.constant.GroupDynConstant;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubFullTextDAO;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.pub.GrpPubIndexUrlDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubSnsFullTextDAO;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;
import com.smate.web.dyn.model.pub.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional(rollbackFor = Exception.class)
public class BuildResPubParametServiceImpl extends BuildResParametServiceBase {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private GrpPubIndexUrlDao grpPubIndexUrlDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private PubSnsFullTextDAO pubSnsFullTextDAO;

  @Override
  public Boolean doVerify(GroupDynamicForm form) {

    if (form.getPubSimpleMap() != null) {
      return true;
    }
    if (null != form.getDatabaseType() && form.getDatabaseType() == 2) {
      PubPdwhPO pdwhPublication = pubPdwhDAO.get(form.getResId());
      if (pdwhPublication == null) {
        return false;
      }
    } else {
      PubSnsPO pub = pubSnsDAO.get(form.getResId());
      if (pub == null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void doHandler(Map<String, Object> data, GroupDynamicForm form) {
    // SecurityUtils.getCurrentUserId 取不到psnId时，参数设置
    Long ownerPsnId = SecurityUtils.getCurrentUserId();
    if (NumberUtils.isNullOrZero(ownerPsnId)) {
      ownerPsnId = form.getPsnId();
    }
    data.put(DynTemplateConstant.DYN_OWNER_DES3_ID, ServiceUtil.encodeToDes3(ownerPsnId.toString()));

    data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
    data.put(GroupDynConstant.TEMPLATE_DATA_DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
    if (form.getPubSimpleMap() != null) {
      data.putAll(form.getPubSimpleMap());
      // 新建成果添加
      data.put(DynTemplateConstant.RES_OWNER_DES3ID, ServiceUtil.encodeToDes3(ownerPsnId.toString()));
    } else if (form.getDatabaseType() != null && form.getDatabaseType() == 2) {
      PubPdwhPO pdwhPublication = pubPdwhDAO.get(form.getResId());
      PdwhPubFullTextPO pdwhFullTextFile = pdwhPubFullTextDAO.getFullTextByPubId(form.getResId());
      GrpPubIndexUrl pubIndexUrl = grpPubIndexUrlDao.findByGrpIdAndPubId(form.getGroupId(), form.getResId());

      if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
        data.put(GroupDynConstant.TEMPLATE_DATA_PUB_INDEX_URL,
            domainscm + "/" + ShortUrlConst.B_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
      }
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_AUTHOR_NAMES, pdwhPublication.getAuthorNames());
      // 部分中英文数据处理
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_NAME, pdwhPublication.getTitle());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_DESC, pdwhPublication.getBriefDesc());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_NAME, pdwhPublication.getTitle());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_DESC, pdwhPublication.getBriefDesc());
      data.put(GroupDynConstant.TEMPLATE_DATA_PDWH_URL, form.getDatabaseType());
      data.put(GroupDynConstant.TEMPLATE_DATA_PDWH_DBID, form.getDbId());
      if (pdwhFullTextFile != null && null != pdwhFullTextFile.getFileId()) {
        data.put(GroupDynConstant.TEMPLATE_DATA_RES_FULL_TEXT_ID, pdwhFullTextFile.getFileId());
      }
      // 全文图片
      /*
       * if (StringUtils.isNoneBlank(pub.getFullTextField())) {
       * data.put(GroupDynConstant.TEMPLATE_DATA_RES_FULL_TEXT_ID, pub.getFullTextField());
       * data.put(GroupDynConstant.TEMPLATE_DATA_RES_FULL_TEXT_IMAGE,
       * pubFulltextDao.getPubFulltextImageByPubId(form.getResId())); }
       */
    } else {
      PubSnsPO pub = pubSnsDAO.get(form.getResId());
      String ownerPubId = "";
      if (pub != null) {
        ownerPubId = String.valueOf(pub.getCreatePsnId().toString());
      }
      // 添加资源拥有者id
      data.put(DynTemplateConstant.RES_OWNER_DES3ID,
          StringUtils.isNotBlank(ownerPubId) ? ServiceUtil.encodeToDes3(ownerPubId) : 0);
      GrpPubIndexUrl pubIndexUrl = grpPubIndexUrlDao.findByGrpIdAndPubId(form.getGroupId(), form.getResId());
      PubIndexUrl psnPubIndexUrl = pubIndexUrlDao.get(form.getResId());
      PubFullTextPO fullText = pubSnsFullTextDAO.getPubFullTextByPubId(form.getResId());
      if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
        data.put(GroupDynConstant.TEMPLATE_DATA_PUB_INDEX_URL,
            domainscm + "/" + ShortUrlConst.B_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
      }else if(psnPubIndexUrl != null &&StringUtils.isNotBlank(psnPubIndexUrl.getPubIndexUrl()) ){
        data.put(GroupDynConstant.TEMPLATE_DATA_PUB_INDEX_URL,
        domainscm + "/" + ShortUrlConst.A_TYPE + "/" + psnPubIndexUrl.getPubIndexUrl());
      }
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_AUTHOR_NAMES, pub.getAuthorNames());
      // 部分中英文数据处理
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_NAME, pub.getTitle());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_DESC, pub.getBriefDesc());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_NAME, pub.getTitle());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_DESC, pub.getBriefDesc());
      // 全文图片
      if (fullText != null && StringUtils.isNoneBlank(String.valueOf(fullText.getFileId()))) {
        data.put(GroupDynConstant.TEMPLATE_DATA_RES_FULL_TEXT_ID, fullText.getFileId());
        data.put(GroupDynConstant.TEMPLATE_DATA_RES_FULL_TEXT_IMAGE, fullText.getThumbnailPath());
      }
    }
  }

}
