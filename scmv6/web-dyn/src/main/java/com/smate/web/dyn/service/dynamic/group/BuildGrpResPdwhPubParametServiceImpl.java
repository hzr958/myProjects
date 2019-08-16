package com.smate.web.dyn.service.dynamic.group;

import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.constant.GroupDynConstant;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubFullTextDAO;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.pub.GrpPubIndexUrlDao;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;
import com.smate.web.dyn.model.pub.GrpPubIndexUrl;
import com.smate.web.dyn.model.pub.PdwhPubFullTextPO;
import com.smate.web.dyn.model.pub.PubPdwhPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * 群组动态-基准库成果数据处理
 * 
 * @author zzx
 *
 */
public class BuildGrpResPdwhPubParametServiceImpl extends BuildResParametServiceBase {
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private GrpPubIndexUrlDao grpPubIndexUrlDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public Boolean doVerify(GroupDynamicForm form) {
    if (form.getResId() == null) {
      return false;
    }
    PubPdwhPO p = pubPdwhDAO.get(form.getResId());
    if (p == null) {
      return false;
    }
    return true;
  }

  @Override
  public void doHandler(Map<String, Object> data, GroupDynamicForm form) {
    PubPdwhPO pdwhPublication = pubPdwhDAO.get(form.getResId());
    data.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_AUTHOR_NAMES, pdwhPublication.getAuthorNames());
    // 部分中英文数据处理
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_NAME, pdwhPublication.getTitle());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_DESC, pdwhPublication.getBriefDesc());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_NAME, pdwhPublication.getTitle());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_DESC, pdwhPublication.getBriefDesc());
    data.put(GroupDynConstant.TEMPLATE_DATA_PDWH_URL, form.getDatabaseType());
    data.put(GroupDynConstant.TEMPLATE_DATA_PDWH_DBID, form.getDbId());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
    data.put(GroupDynConstant.TEMPLATE_DATA_DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));

    GrpPubIndexUrl pubIndexUrl = grpPubIndexUrlDao.findByGrpIdAndPubId(form.getGroupId(), form.getResId());
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      data.put(GroupDynConstant.TEMPLATE_DATA_PUB_INDEX_URL,
          domainscm + "/" + ShortUrlConst.B_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
    }

    PdwhPubFullTextPO pdwhFullTextFile = pdwhPubFullTextDAO.getFullTextByPubId(form.getResId());
    if (pdwhFullTextFile != null && null != pdwhFullTextFile.getFileId()) {
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_FULL_TEXT_ID, pdwhFullTextFile.getFileId());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_FULL_TEXT_IMAGE, pdwhFullTextFile.getThumbnailPath());
    }

  }

}
