package com.smate.web.dyn.service.dynamic.group;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.constant.GroupDynConstant;
import com.smate.web.dyn.dao.grp.GrpFileDao;
import com.smate.web.dyn.form.dynamic.group.GroupDynamicForm;
import com.smate.web.dyn.model.grp.GrpFile;

@Transactional(rollbackFor = Exception.class)
public class BuildGrpResFileParametServiceImpl extends BuildResParametServiceBase {

  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private ArchiveFileService archiveFileService;

  @Override
  public Boolean doVerify(GroupDynamicForm form) {
    GrpFile grpFile = grpFileDao.get(form.getResId());
    if (grpFile == null) {
      return false;
    }
    return true;
  }

  @Override
  public void doHandler(Map<String, Object> data, GroupDynamicForm form) {
    GrpFile grpFile = grpFileDao.get(form.getResId());
    // 这里的资源id是 群组文件id
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_TYPE, form.getResType());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
    data.put(GroupDynConstant.TEMPLATE_DATA_DES3_RES_ID, Des3Utils.encodeToDes3(form.getResId().toString()));
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_NAME, grpFile.getFileName());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_DESC, grpFile.getFileDesc());
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_FILE_TYPE, grpFile.getFileType());
    data.put(DynTemplateConstant.DYN_OWNER_DES3_ID,
        ServiceUtil.encodeToDes3(SecurityUtils.getCurrentUserId().toString()));
    // 图片文件缩略图
    String imgThumbUrl = archiveFileService.getImgFileThumbUrl(grpFile.getArchiveFileId());
    if (StringUtils.isBlank(imgThumbUrl))
      imgThumbUrl = "/resmod/smate-pc/img/fileicon_default.png";
    data.put(GroupDynConstant.TEMPLATE_DATA_RES_FILE_IMAGE, imgThumbUrl);
  }
}
