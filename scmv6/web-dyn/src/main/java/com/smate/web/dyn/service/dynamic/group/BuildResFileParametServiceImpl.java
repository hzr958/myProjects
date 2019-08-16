package com.smate.web.dyn.service.dynamic.group;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.constant.GroupDynConstant;
import com.smate.web.dyn.dao.group.GroupFileDao;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;
import com.smate.web.dyn.model.group.GroupFile;

@Transactional(rollbackFor = Exception.class)
public class BuildResFileParametServiceImpl extends BuildResParametServiceBase {

  @Autowired
  private GroupFileDao groupFileDao;

  @Override
  public Boolean doVerify(GroupDynamicForm form) {
    GroupFile groupFile = groupFileDao.getGroupFile(form.getGroupId(), form.getResId());
    if (groupFile == null) {
      return false;
    }
    return true;
  }

  @Override
  public void doHandler(Map<String, Object> data, GroupDynamicForm form) {
    GroupFile groupFile = groupFileDao.getGroupFile(form.getGroupId(), form.getResId());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
    data.put(GroupDynConstant.TEMPLATE_DATA_DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_NAME, groupFile.getFileName());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_DESC, groupFile.getFileDesc());
    data.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    if ("imgIc".equals(groupFile.getFileType())) {
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_FILE_IMAGE,
          "/upfile" + groupFile.getFilePath().substring(0, groupFile.getFilePath().indexOf(".")) + "_c"
              + groupFile.getFilePath().substring(groupFile.getFilePath().indexOf(".")));
    } else {
      data.put(GroupDynConstant.TEMPLATE_DATA_RES_FILE_IMAGE, "/resscmwebsns/images_v5/file-image-default.jpg");
    }

  }

}
