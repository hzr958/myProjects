package com.smate.center.batch.service.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.dao.dynamic.InspgDao;
import com.smate.center.batch.dao.dynamic.InspgDynRelationInspgDao;
import com.smate.center.batch.dao.dynamic.InspgDynamicNewsAddDao;
import com.smate.center.batch.dao.dynamic.InspgDynamicRefreshDao;
import com.smate.center.batch.dao.dynamic.InspgNewsDao;
import com.smate.center.batch.model.dynamic.DynTemplateEnum;
import com.smate.center.batch.model.dynamic.Inspg;
import com.smate.center.batch.model.dynamic.InspgDynRelationInspg;
import com.smate.center.batch.model.dynamic.InspgDynamicNewsAdd;
import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;
import com.smate.center.batch.model.dynamic.InspgNews;



/**
 * 动态构建链-新增新闻
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public class DynInspgNewsAddServiceImpl extends ExecuteTaskBaseServiceImpl implements ExecuteTaskService {
  private final static Integer TEMPLATE = DynTemplateEnum.DYN_INSPG_NEWS_ADD.toInt();
  @Autowired
  private InspgDao inspgDao;
  @Autowired
  private InspgDynRelationInspgDao inspgDynRelationInspgDao;
  @Autowired
  private InspgNewsDao inspgNewsDao;
  @Autowired
  private InspgDynamicNewsAddDao inspgDynamicNewsAddDao;
  @Autowired
  private InspgDynamicRefreshDao inspgDynamicRefreshDao;

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

    // 插入详情表数据
    String parentIds = obj.getIdOriginal(); // 新闻有多个
    String[] parentIdsArray = parentIds.split(",");
    List<InspgNews> newsList = new ArrayList<InspgNews>();
    Long id = null;
    for (String idStr : parentIdsArray) {
      id = Long.parseLong(idStr);
      InspgNews news = inspgNewsDao.get(id);
      if (news != null) {
        newsList.add(news);
      }
    }
    if (newsList.size() > 0) {
      inspgDynRelationInspgDao.saveDyn(relation);
      List<InspgDynamicNewsAdd> list = formInspgDynamicNewsAdd(inspg, newsList, obj, relation);
      if (list != null && list.size() > 0) {
        for (InspgDynamicNewsAdd dyn : list) {
          // 插入详情表数据
          inspgDynamicNewsAddDao.saveDyn(dyn);
        }
      }
    }
    // 删除刷新表记录
    inspgDynamicRefreshDao.delete(obj.getId());
  }

  private List<InspgDynamicNewsAdd> formInspgDynamicNewsAdd(Inspg inspg, List<InspgNews> newsList,
      InspgDynamicRefresh refresh, InspgDynRelationInspg relation) {
    List<InspgDynamicNewsAdd> result = new ArrayList<InspgDynamicNewsAdd>();
    for (InspgNews news : newsList) {
      InspgDynamicNewsAdd obj = new InspgDynamicNewsAdd();
      obj.setDynId(relation.getId());
      obj.setHasComment(1);
      obj.setCommentParentId(news.getId());
      obj.setCommentType(1001);
      obj.setHasLike(1);
      obj.setLikeParentId(news.getId());
      obj.setLikeType(1001);
      obj.setInspgId(inspg.getId());
      obj.setCreateTime(refresh.getCreateTime().getTime());
      obj.setNewsId(news.getId());
      obj.setNewsTitle(news.getTitle());
      obj.setNewsUrl(null);// 模板拼接
      result.add(obj);
    }
    return result;
  }

}
