package com.smate.center.batch.dao.sns.wechat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.OpenDataGetOpenUserUnionException;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 第三方系统与SNS关联表Dao
 * 
 * @since 6.0.1
 * 
 */
@Repository
public class OpenUserUnionDao extends SnsHibernateDao<OpenUserUnion, Long> {

  /**
   * 通过openId，token查询唯一记录
   * 
   * @Parameter openId，token
   */
  public OpenUserUnion getOpenUserUnion(Long openId, String token) throws BatchTaskException {

    try {
      String hql = "from OpenUserUnion t where t.openId = ? and t.token = ?";
      return super.findUnique(hql, openId, token);
    } catch (Exception e) {
      logger.error("根据openId,token从数据库获取open人员关联对象异常! openId=" + openId + ";token=" + token);
      throw new BatchTaskException(e);
    }
  }

  /**
   * 通过psnId查询openId
   * 
   * @Parameter psnId
   */

  public Long getOpenIdByPsnId(Long psnId) {
    if (psnId == null) {
      return null;
    } else {
      String hql = "select distinct(t.openId) from OpenUserUnion t where t.psnId = ?";
      return super.findUnique(hql, psnId);
    }
  }

  /**
   * 保存OpenUserUnion类
   * 
   * @Parameter openUserUnion
   */
  public void saveOpenUserUnion(OpenUserUnion openUserUnion) {

    try {
      super.save(openUserUnion);
    } catch (Exception e) {
      logger.error("保存openId与用户的关联关系异常 " + openUserUnion.toString());
      throw new OpenDataGetOpenUserUnionException(e);
    }
  }

  /**
   * 通过openId查询第一条记录
   * 
   * @Parameter openId
   */
  @SuppressWarnings("unchecked")
  public OpenUserUnion getOpenUserUnionByOpenId(Long openId) {
    try {
      String hql = "from OpenUserUnion t where t.openId = ?";
      List<OpenUserUnion> openUserUnionList = new ArrayList<OpenUserUnion>();
      openUserUnionList = super.createQuery(hql, openId).list();

      if (CollectionUtils.isEmpty(openUserUnionList)) {
        return null;
      } else {
        return openUserUnionList.get(0);
      }
    } catch (Exception e) {
      logger.error("根据openId从数据库获取open人员关联对象异常! openId=" + openId);
      throw new OpenDataGetOpenUserUnionException(e);
    }

  }

  public boolean ifRegisteredInTheIns(Long psnId, String token) {
    String hql = "select count(1) from OpenUserUnion t where t.psnId =:psnId and t.token =:token";
    Long counts =
        (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("token", token).uniqueResult();
    if (counts > 0) {
      return true;
    }
    return false;
  }

  public Long getPsnIdByOpenIdAndToken(Long openId, String token) {
    String hql = "select t.psnId from OpenUserUnion t where t.openId =:openId and t.token =:token";
    return (Long) super.createQuery(hql).setParameter("openId", openId).setParameter("token", token).uniqueResult();
  }
}
