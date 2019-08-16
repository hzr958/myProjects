package com.smate.center.batch.service.dynamic;

import com.smate.center.batch.model.dynamic.DynTemplateEnum;
import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;



/**
 * 动态构建链-文件上传
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public class DynInspgImgUploadServiceImpl extends ExecuteTaskBaseServiceImpl implements ExecuteTaskService {
  private final static Integer TEMPLATE = DynTemplateEnum.DYN_INSPG_IMG_UPLOAD.toInt();

  @Override
  public boolean isThisDyn(InspgDynamicRefresh obj) throws Exception {
    if (TEMPLATE.equals(obj.getDynType())) {
      return true;
    }
    return false;
  }

  @Override
  public void build(InspgDynamicRefresh obj) throws Exception {
    // TODO Auto-generated method stub

  }

}
