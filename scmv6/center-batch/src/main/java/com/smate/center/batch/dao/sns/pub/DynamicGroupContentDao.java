package com.smate.center.batch.dao.sns.pub;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.DynamicGroupContent;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组动态参数DAO类_SCM-5912.
 * 
 * @author mjg
 * 
 */
@Repository
public class DynamicGroupContentDao extends SnsHibernateDao<DynamicGroupContent, Long> {

  /**
   * 查询动态内容的json字符串.
   * 
   * @param dynId
   * @return
   * @throws DaoException
   */
  public String queryDynamicContent(Long dynId) throws DaoException {

    return (String) super.createQuery("select t.dynJson from DynamicGroupContent t where t.groupDynId = ?", dynId)
        .uniqueResult();
  }

  /**
   * 保存群组动态参数表.
   * 
   * @param dynGroupCon
   */
  public void saveDynGroupCon(DynamicGroupContent dynGroupCon) {
    if (dynGroupCon != null) {
      super.getSession().saveOrUpdate(dynGroupCon);
    }
  }

  /**
   * 获取群组动态参数记录.
   * 
   * @param dynId
   * @return
   */
  public DynamicGroupContent getDynGroupCon(Long dynId) {
    String hql = "from DynamicGroupContent t where t.groupDynId=? ";
    Object obj = super.createQuery(hql, dynId).uniqueResult();
    if (obj != null) {
      return (DynamicGroupContent) obj;
    }
    return null;
  }

  /**
   * 获取动态内容.
   * 
   * @param dynIdList
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Map<Long, String> queryDynamicContent(List<Long> dynIdList) throws DaoException {
    Map<Long, String> map = new HashMap<Long, String>();
    if (CollectionUtils.isNotEmpty(dynIdList)) {

      // 拆分80一组，如果参数超过100，SQL报错
      Collection<Collection<Long>> container = ServiceUtil.splitList(dynIdList, 80);
      String hql = "from DynamicGroupContent t where t.groupDynId in(:dynIdList) ";
      for (Collection<Long> item : container) {
        List<DynamicGroupContent> iResultList = super.createQuery(hql).setParameterList("dynIdList", item).list();
        if (CollectionUtils.isNotEmpty(iResultList)) {
          for (DynamicGroupContent dynGroupContent : iResultList) {
            map.put(dynGroupContent.getGroupDynId(), dynGroupContent.getDynJson());
          }
        }
      }
    }
    return map;
  }
}
