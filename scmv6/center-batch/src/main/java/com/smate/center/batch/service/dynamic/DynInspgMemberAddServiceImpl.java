package com.smate.center.batch.service.dynamic;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.dao.dynamic.InspgDao;
import com.smate.center.batch.dao.dynamic.InspgDynRelationInspgDao;
import com.smate.center.batch.dao.dynamic.InspgDynamicMemberAddDao;
import com.smate.center.batch.dao.dynamic.InspgDynamicRefreshDao;
import com.smate.center.batch.dao.dynamic.InspgMembersDao;
import com.smate.center.batch.model.dynamic.DynTemplateEnum;
import com.smate.center.batch.model.dynamic.Inspg;
import com.smate.center.batch.model.dynamic.InspgDynRelationInspg;
import com.smate.center.batch.model.dynamic.InspgDynamicMemberAdd;
import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;
import com.smate.center.batch.model.dynamic.InspgMembers;



/**
 * 动态构建链-新增成员
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public class DynInspgMemberAddServiceImpl extends ExecuteTaskBaseServiceImpl implements ExecuteTaskService {
  private final static Integer TEMPLATE = DynTemplateEnum.DYN_INSPG_MEMBER_ADD.toInt();

  @Autowired
  private InspgDao inspgDao;
  @Autowired
  private InspgMembersDao inspgMembersDao;
  @Autowired
  private InspgDynRelationInspgDao inspgDynRelationInspgDao;
  @Autowired
  private InspgDynamicMemberAddDao inspgDynamicMemberAddDao;
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
    String parentIds = obj.getIdOriginal(); // 成员没有多个 所以parentIds是单个id 新闻有多个
    InspgMembers member = inspgMembersDao.get(Long.parseLong(parentIds));
    if (member != null) {
      inspgDynRelationInspgDao.saveDyn(relation);
      InspgDynamicMemberAdd memberAdd = formInspgDynamicMemberAdd(inspg, obj, member, relation);
      inspgDynamicMemberAddDao.saveDyn(memberAdd);
    }
    // 删除刷新表记录
    inspgDynamicRefreshDao.delete(obj.getId());

  }

  private InspgDynamicMemberAdd formInspgDynamicMemberAdd(Inspg inspg, InspgDynamicRefresh refresh, InspgMembers member,
      InspgDynRelationInspg relation) {
    InspgDynamicMemberAdd result = new InspgDynamicMemberAdd();
    result.setDynId(relation.getId());
    result.setHasComment(1);
    result.setCommentParentId(relation.getId());
    result.setCommentType(5001);
    result.setHasLike(1);
    result.setLikeParentId(relation.getId());
    result.setLikeType(5001);
    result.setInspgId(inspg.getId());
    result.setCreateTime(refresh.getCreateTime().getTime());
    result.setMemberId(member.getId());
    result.setMemberName(member.getName());
    result.setMemberDesp(member.getDescription());
    result.setMemberAvatar(member.getAvatars());
    result.setMemberHomePage(member.getHomePage());
    return result;
  }


}
