package com.smate.center.oauth.dao.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.oauth.exception.DaoException;
import com.smate.center.oauth.exception.OauthException;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 第三方系统与SNS关联表Dao
 * 
 * 
 */
@Repository
public class OpenUserUnionDao extends HibernateDao<OpenUserUnion, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  protected Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * 通过openId查询第一条记录
   * 
   * @Parameter openId
   */
  @SuppressWarnings("unchecked")
  public Long getOpenUserUnionPsnIdByOpenId(Long openId) {
    try {
      String hql = "select psnId from OpenUserUnion t where t.openId = ?";
      List<Long> openUserUnionPsnIdList = new ArrayList<Long>();
      openUserUnionPsnIdList = super.createQuery(hql, openId).list();

      if (CollectionUtils.isEmpty(openUserUnionPsnIdList)) {
        return null;
      } else {
        return openUserUnionPsnIdList.get(0);
      }
    } catch (Exception e) {
      logger.error("根据openId从数据库获取open人员关联对象异常! openId=" + openId);
      throw new OauthException(e);
    }

  }

  /**
   * 通过psnId获取第一条记录的openId
   * 
   * @Parameter psnId
   */
  @SuppressWarnings("unchecked")
  public Long getOpenIdByPsnId(Long psnId) throws DaoException {
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
      throw new DaoException(e);
    }
  }

  /**
   * 通过openId查询第一条记录
   * 
   * @Parameter openId
   */
  @SuppressWarnings("unchecked")
  public OpenUserUnion getOpenUserUnionByOpenId(Long openId) throws DaoException {
    try {
      String hql = "from OpenUserUnion t where t.openId = :openId";
      List<OpenUserUnion> openUserUnionList = new ArrayList<OpenUserUnion>();
      openUserUnionList = super.createQuery(hql).setParameter("openId", openId).list();

      if (CollectionUtils.isEmpty(openUserUnionList)) {
        return null;
      } else {
        return openUserUnionList.get(0);
      }
    } catch (Exception e) {
      logger.error("根据openId从数据库获取open人员关联对象异常! openId=" + openId);
      throw new DaoException(e);
    }

  }

  /**
   * 保存OpenUserUnion类
   * 
   * @Parameter openUserUnion
   */
  public void saveOpenUserUnion(OpenUserUnion openUserUnion) throws DaoException {
    try {
      super.save(openUserUnion);
    } catch (Exception e) {
      logger.error("保存openId与用户的关联关系异常 " + openUserUnion.toString());
      throw new DaoException(e);
    }
  }

  /**
   * 通过openId，token查询唯一记录
   * 
   * @Parameter openId，token
   */
  public OpenUserUnion getOpenUserUnion(Long openId, String token) throws DaoException {

    try {
      String hql = "from OpenUserUnion t where t.openId = ? and t.token = ?";
      return super.findUnique(hql, openId, token);
    } catch (Exception e) {
      logger.error("根据openId,token从数据库获取open人员关联对象异常! openId=" + openId + ";token=" + token);
      throw new DaoException(e);
    }
  }

}
