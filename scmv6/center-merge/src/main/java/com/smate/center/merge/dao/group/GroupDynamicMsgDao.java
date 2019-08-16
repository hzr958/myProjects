package com.smate.center.merge.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.group.GroupDynamicMsg;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组动态 内容 信息 dao
 * 
 * @author tsz
 *
 */
@Repository
public class GroupDynamicMsgDao extends SnsHibernateDao<GroupDynamicMsg, Long> {

  /**
   * 得到当前人的动态消息
   * 
   * @param commentPsnId
   * @return
   */
  public List<GroupDynamicMsg> getListByPsnId(Long producer) {
    String hql = "from GroupDynamicMsg g where g.producer =:producer   and g.status = 0 "
        + " and  exists(select t1.grpId from GrpBaseinfo t1 where t1.grpId=g.groupId and t1.status='01' )";
    return this.createQuery(hql).setParameter("producer", producer).list();

  }

}
