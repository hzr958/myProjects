package com.smate.center.batch.dao.sns.pub;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.DynMsgContent;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 动态内容Dao.
 * 
 * @author xys
 * 
 */
@Repository
public class DynMsgContentDao extends SnsHibernateDao<DynMsgContent, Long> {

  /**
   * 获取动态内容.
   * 
   * @param dcIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Map<Long, String> queryDynMsgContent(List<Long> dcIds) throws DaoException {
    Map<Long, String> map = new HashMap<Long, String>();
    if (CollectionUtils.isNotEmpty(dcIds)) {

      // 拆分80一组，如果参数超过100，SQL报错
      Collection<Collection<Long>> container = ServiceUtil.splitList(dcIds, 80);
      String hql = "from DynMsgContent t where t.dcId in(:dcIds) ";
      for (Collection<Long> item : container) {
        List<DynMsgContent> iResultList = super.createQuery(hql).setParameterList("dcIds", item).list();
        if (CollectionUtils.isNotEmpty(iResultList)) {
          for (DynMsgContent dynContent : iResultList) {
            map.put(dynContent.getDcId(), dynContent.getDynJson());
          }
        }
      }
    }
    return map;
  }

  /**
   * 查询动态内容的json字符串.
   * 
   * @param dcId
   * @return
   * @throws DaoException
   */
  public String queryDynMsgContent(Long dcId) throws DaoException {

    return (String) super.createQuery("select t.dynJson from DynMsgContent t where t.dcId = ?", dcId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<String> queryDynContentByParentDynId(List<Long> parantDynIdList) throws DaoException {

    return super.createQuery(
        "select t.dynJson from DynMsgContent t where exists(select 1 from Dynamic t1 where t.dcId = t1.dcId and t1.dynParentId in(:parentDynId))")
            .setParameterList("parentDynId", parantDynIdList).list();
  }

  public Long getDcId() {
    BigDecimal dcId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_DYN_MSG_CONTENT.nextval from dual").uniqueResult();
    return dcId.longValue();
  }
}
