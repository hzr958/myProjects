package com.smate.center.open.dao.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.exception.OpenDataGetOpenIdException;
import com.smate.center.open.exception.OpenDataGetOpenUserUnionException;
import com.smate.center.open.exception.OpenDataSaveOpenUserUnionException;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 第三方系统与SNS关联表Dao
 * 
 * 
 */
@Repository
public class OpenUserUnionDao extends SnsHibernateDao<OpenUserUnion, Long> {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 通过psnId获取第一条记录的openId
   * 
   * @Parameter psnId
   */
  @SuppressWarnings("unchecked")
  public Long getOpenIdByPsnId(Long psnId) throws OpenDataGetOpenIdException {
    try {
      String hql = "select t.openId from OpenUserUnion t where t.psnId = :psnId";
      List<Long> openIdList = new ArrayList<Long>();
      openIdList = super.createQuery(hql).setParameter("psnId", psnId).list();
      if (CollectionUtils.isEmpty(openIdList)) {
        return null;
      } else {
        return openIdList.get(0);
      }
    } catch (Exception e) {
      logger.error("根据psnId从数据库获取openId异常! psnId=" + psnId);
      throw new OpenDataGetOpenIdException(e);
    }
  }

  /**
   * 通过psnId获取第一条记录的openId
   *
   * @Parameter psnId
   */
  @SuppressWarnings("unchecked")
  public OpenUserUnion getByPsnIdToken(Long psnId, String token) throws OpenDataGetOpenIdException {
    try {
      String hql = "from OpenUserUnion t where t.psnId = :psnId and t.token =:token";
      Object result = super.createQuery(hql).setParameter("psnId", psnId).setParameter("token", token).uniqueResult();
      if (result != null) {
        return (OpenUserUnion) result;
      }
      return null;
    } catch (Exception e) {
      logger.error("根据psnId从数据库获取openId异常! psnId=" + psnId);
      throw new OpenDataGetOpenIdException(e);
    }
  }

  /**
   * 通过openId，token查询唯一记录
   * 
   * @Parameter openId，token
   */
  public OpenUserUnion getOpenUserUnion(Long openId, String token) {

    try {
      String hql = "from OpenUserUnion t where t.openId = ? and t.token = ?";
      return super.findUnique(hql, openId, token);
    } catch (Exception e) {
      logger.error("根据openId,token从数据库获取open人员关联对象异常! openId=" + openId + ";token=" + token);
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

  /**
   * 通过psnId查询第一条记录
   * 
   * @Parameter psnId
   */
  @SuppressWarnings("unchecked")
  public OpenUserUnion getOpenUserUnionByPsnId(Long psnId) {
    try {
      String hql = "from OpenUserUnion t where t.psnId = ?";
      List<OpenUserUnion> openUserUnionList = new ArrayList<OpenUserUnion>();
      openUserUnionList = super.createQuery(hql, psnId).list();

      if (CollectionUtils.isEmpty(openUserUnionList)) {
        return null;
      } else {
        return openUserUnionList.get(0);
      }
    } catch (Exception e) {
      logger.error("根据psnId从数据库获取open人员关联对象异常! psnId=" + psnId);
      throw new OpenDataGetOpenUserUnionException(e);
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
      throw new OpenDataSaveOpenUserUnionException(e);
    }
  }

  /**
   * 通过openId，token删除唯一记录
   * 
   * @throws Exception
   * 
   * @Parameter openId，token
   */

  public int deleteOpenUserUnionByTokenIdAndOpenId(String token, Long openId) throws Exception {
    try {

      String hql = "DELETE FROM OpenUserUnion t WHERE t.openId = ? AND t.token = ? ";
      return createQuery(hql, openId, token).executeUpdate();
    } catch (Exception e) {
      logger.error("删除数据失败! openId=" + openId + "  TOKEN=" + token);
      throw new Exception(e);
    }
  }

  /**
   * 通过openId，token删除唯一记录
   *
   * @throws Exception
   *
   * @Parameter openId，token
   */

  public int deleteOpenUserUnionByTokenAndPsnId(Long psnId, String token) throws Exception {
    try {

      String hql = "DELETE FROM OpenUserUnion t WHERE t.psnId = ? AND t.token = ? ";
      return createQuery(hql, psnId, token).executeUpdate();
    } catch (Exception e) {
      logger.error("删除数据失败! openId=" + psnId + "  TOKEN=" + token);
      throw new Exception(e);
    }
  }

  /**
   * 保存
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  public void save(OpenUserUnion temp) {
    super.save(temp);
  }

  /**
   * 通过psnId，token查询唯一记录
   * 
   * @Parameter psnId，token
   */
  public OpenUserUnion getOpenUserUnionByPsnIdAndToken(Long psnId, String token) {

    try {
      String hql = "from OpenUserUnion t where t.psnId = ? and t.token = ?";
      return super.findUnique(hql, psnId, token);
    } catch (Exception e) {
      logger.error("根据psnId,token从数据库获取open人员关联对象异常! psnId=" + psnId + ";token=" + token);
      throw new OpenDataGetOpenUserUnionException(e);
    }
  }

  public boolean ifRegisteredInTheIns(Long personId, String token) {
    String hql = "select count(1) from OpenUserUnion t where t.psnId =:psnId and t.token =:token";
    Long counts =
        (Long) super.createQuery(hql).setParameter("psnId", personId).setParameter("token", token).uniqueResult();
    if (counts > 0) {
      return true;
    }
    return false;
  }

}
