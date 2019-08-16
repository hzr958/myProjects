package com.smate.center.batch.service.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.dao.dynamic.InspgDao;
import com.smate.center.batch.dao.dynamic.InspgDynRelationInspgDao;
import com.smate.center.batch.dao.dynamic.InspgDynamicFileShareDao;
import com.smate.center.batch.model.dynamic.DynTemplateEnum;
import com.smate.center.batch.model.dynamic.Inspg;
import com.smate.center.batch.model.dynamic.InspgDynRelationInspg;
import com.smate.center.batch.model.dynamic.InspgDynamicFileShare;
import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.utils.common.WebObjectUtil;

/**
 * 动态构建链- 发表新鲜事-文件
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public class DynInspgFileShareServiceImpl extends ExecuteTaskBaseServiceImpl implements ExecuteTaskService {

  private final static Integer TEMPLATE = DynTemplateEnum.DYN_INSPG_FILE_SHARE.toInt();

  @Autowired
  private InspgDao inspgDao;
  @Autowired
  private ArchiveFileDao inspgArchiveFileDao;
  @Autowired
  private InspgDynRelationInspgDao inspgDynRelationInspgDao;
  @Autowired
  private InspgDynamicFileShareDao inspgDynamicFileShareDao;

  @Override
  public boolean isThisDyn(InspgDynamicRefresh obj) throws Exception {
    if (TEMPLATE.equals(obj.getDynType())) {
      return true;
    }
    return false;
  }

  @Override
  public void build(InspgDynamicRefresh obj) throws Exception {
    Inspg inspg = inspgDao.get(obj.getInspgId());
    // 插入关系表数据 ,使用dynid作为dynid,不去oracle生成dynid了
    InspgDynRelationInspg relation = compareRelation(obj, inspg);
    inspgDynRelationInspgDao.saveDyn(relation);
    // 插入详情表数据
    List<ArchiveFile> fileList = inspgArchiveFileDao.getArchiveFileByIds(obj.getFileIds());
    List<InspgDynamicFileShare> list = formInspgDynamicFileShare(inspg, obj, fileList, relation);
    if (list != null && list.size() > 0) {
      for (InspgDynamicFileShare dyn : list) {
        // 插入详情表数据
        inspgDynamicFileShareDao.saveDyn(dyn);
      }
    }
  }

  /**
   * 构造数据库存储对象
   * 
   * @param inspg
   * @param refresh
   * @param fileList
   * @return
   * @author lxz
   */
  private List<InspgDynamicFileShare> formInspgDynamicFileShare(Inspg inspg, InspgDynamicRefresh refresh,
      List<ArchiveFile> fileList, InspgDynRelationInspg relation) {
    List<InspgDynamicFileShare> result = new ArrayList<InspgDynamicFileShare>();
    for (ArchiveFile file : fileList) {
      InspgDynamicFileShare obj = new InspgDynamicFileShare();
      obj.setDynId(relation.getId());
      obj.setHasComment(1);
      obj.setCommentParentId(file.getFileId());
      obj.setCommentType(5002);
      obj.setHasLike(1);
      obj.setLikeParentId(file.getFileId());
      obj.setLikeType(5002);
      obj.setInspgId(inspg.getId());
      obj.setCreateTime(refresh.getCreateTime().getTime());
      obj.setFileExt(WebObjectUtil.getFileType(file.getFileName()));
      obj.setFileLink(file.getFilePath());// 页面拼接
      obj.setFileTitle(file.getFileName());
      obj.setFileId(file.getFileId());
      result.add(obj);
    }
    return result;
  }

}
