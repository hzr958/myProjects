package com.smate.center.batch.service.dynamic;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.dao.dynamic.InspgDao;
import com.smate.center.batch.dao.dynamic.InspgDynRelationInspgDao;
import com.smate.center.batch.dao.dynamic.InspgDynamicLinkShareDao;
import com.smate.center.batch.model.dynamic.DynTemplateEnum;
import com.smate.center.batch.model.dynamic.Inspg;
import com.smate.center.batch.model.dynamic.InspgDynRelationInspg;
import com.smate.center.batch.model.dynamic.InspgDynamicLinkShare;
import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;

/**
 * 动态构建链-发表新鲜事-普通(包括链接)
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public class DynInspgLinkShareServiceImpl extends ExecuteTaskBaseServiceImpl implements ExecuteTaskService {
  private final static Integer TEMPLATE = DynTemplateEnum.DYN_INSPG_LINK_SHARE.toInt();
  @Autowired
  private InspgDao inspgDao;
  @Autowired
  private InspgDynRelationInspgDao inspgDynRelationInspgDao;
  @Autowired
  private InspgDynamicLinkShareDao inspgDynamicLinkShareDao;


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
    InspgDynamicLinkShare link = formInspgDynamicLinkShare(inspg, obj, relation);
    inspgDynamicLinkShareDao.saveDyn(link);
  }

  private InspgDynamicLinkShare formInspgDynamicLinkShare(Inspg inspg, InspgDynamicRefresh refresh,
      InspgDynRelationInspg relation) {
    InspgDynamicLinkShare result = new InspgDynamicLinkShare();
    result.setDynId(relation.getId());
    result.setHasComment(1);
    result.setCommentParentId(relation.getId());
    result.setCommentType(5001);
    result.setHasLike(1);
    result.setLikeParentId(relation.getId());
    result.setLikeType(5001);
    result.setInspgId(inspg.getId());
    result.setCreateTime(refresh.getCreateTime().getTime());
    return result;
  }



}
