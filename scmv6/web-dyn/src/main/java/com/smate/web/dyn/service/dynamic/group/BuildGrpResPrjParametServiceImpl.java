package com.smate.web.dyn.service.dynamic.group;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.dyn.constant.GroupDynConstant;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;

public class BuildGrpResPrjParametServiceImpl extends BuildResParametServiceBase {
  @Autowired
  private ProjectDao projectDao;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public Boolean doVerify(GroupDynamicForm form) {
    Project prj = projectDao.get(form.getResId());
    if (prj == null) {
      return false;
    }
    return true;
  }

  @Override
  public void doHandler(Map<String, Object> data, GroupDynamicForm form) {
    String des3PrjId = Des3Utils.encodeToDes3(form.getResId().toString());
    Project prj = projectDao.get(form.getResId());
    if (prj != null) {
      String zhTitle = StringUtils.isNotBlank(prj.getZhTitle()) ? prj.getZhTitle() : prj.getEnTitle();
      String enTitle = StringUtils.isNotBlank(prj.getEnTitle()) ? prj.getEnTitle() : prj.getZhTitle();
      String zhDescr = StringUtils.isNotBlank(prj.getBriefDesc()) ? prj.getBriefDesc() : prj.getBriefDescEn();
      String enDescr = StringUtils.isNotBlank(prj.getBriefDescEn()) ? prj.getBriefDescEn() : prj.getBriefDesc();
      String authorNameZh = StringUtils.isBlank(prj.getAuthorNames()) ? prj.getAuthorNamesEn() : prj.getAuthorNames();
      String authorNameEn = StringUtils.isBlank(prj.getAuthorNamesEn()) ? prj.getAuthorNames() : prj.getAuthorNamesEn();
      String url = domainscm + "/prjweb/project/detailsshow?des3PrjId=" + des3PrjId;
      String img = "/resmod/images_v5/images2016/file_img.jpg";

      data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ID, form.getResId());
      data.put(GroupDynConstant.TEMPLATE_DATA_DES3_RES_ID, des3PrjId);
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_NAME, zhTitle);
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_NAME, enTitle);
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_EN_DESC, enDescr);
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_ZH_DESC, zhDescr);

      data.put(GroupDynConstant.TEMPLATE_DATA_PUB_INDEX_URL, url);

      data.put(GroupDynConstant.TEMPLATE_DATA_RES_AUTHOR_NAMES, authorNameZh);
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_FULL_TEXT_IMAGE, img);


    }

  }

}
