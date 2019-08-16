package com.smate.center.merge.dao.thirdparth;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.thirdparty.OpenUserUnion;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 第三方系统与SNS关联表Dao.
 * 
 * 
 */
@Repository
public class OpenUserUnionDao extends SnsHibernateDao<OpenUserUnion, Long> {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 通过psnId获取第一条记录的openId.
   * 
   * @Parameter psnId
   */
  @SuppressWarnings("unchecked")
  public Long getOpenIdByPsnId(Long psnId) {
    String hql = "select t.openId from OpenUserUnion t where t.psnId = :psnId";
    List<Long> openIdList = new ArrayList<Long>();
    openIdList = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (CollectionUtils.isEmpty(openIdList)) {
      return null;
    } else {
      return openIdList.get(0);
    }
  }

  /**
   * 获取所有的关联记录.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<OpenUserUnion> getUnionByPsnId(Long psnId) {
    String hql = "from OpenUserUnion t where t.psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 通过psnId，token查询唯一记录.
   * 
   */
  public OpenUserUnion getOpenUserUnionByPsnIdAndToken(Long psnId, String token) {
    String hql = "from OpenUserUnion t where t.psnId = ? and t.token = ?";
    return super.findUnique(hql, psnId, token);
  }
}
