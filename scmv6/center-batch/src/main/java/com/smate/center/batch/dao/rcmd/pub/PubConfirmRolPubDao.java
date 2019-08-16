package com.smate.center.batch.dao.rcmd.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmRolPub;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 成果提交持久层.
 * 
 * @author LY
 * 
 */
@Repository
public class PubConfirmRolPubDao extends RcmdHibernateDao<PubConfirmRolPub, Long> {

  public void deleteById(Long rolPubId) throws DaoException {
    String sql = "delete from PubConfirmRolPub t where t.rolPubId=?";
    this.createQuery(sql, new Object[] {rolPubId}).executeUpdate();
  }

  /**
   * 获取成果数据,成果认领推广，旧
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getCfmRolPubBypubId(List<Long> rolPubId) throws DaoException {
    String hql =
        "select rolPubId as rolPubId ,nvl(zhTitle,enTitle) as title,nvl(authorNames,'') as authorNames,nvl(briefDesc,'') as briefDesc,nvl(fulltextFileid,'')  as fulltextFileid   from PubConfirmRolPub where rolPubId in (:rolPubId)";
    return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameterList("rolPubId", rolPubId).list();
  }

  /**
   * 获取成果数据 ，成果认领推广,新
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Map> getCfmRolPubBypubIdNew(Long rolPubId) throws DaoException {
    String hql =
        "select rolPubId as rolPubId ,nvl(sourceDbId,4) as dbId,nvl(zhTitle,enTitle) as title,nvl(fulltextFileid,'')  as fulltextFileid   from PubConfirmRolPub where rolPubId = ? ";
    return super.createQuery(hql, rolPubId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  /**
   * 根据时间判断成果是否更新.
   * 
   * @param rolPubId
   * @param updateDate
   * @return
   */
  public boolean isPubUpdate(Long rolPubId, Date updateDate) {
    if (updateDate == null) {
      return true;
    }
    String hql = "select count(t.rolPubId) from PubConfirmRolPub t where t.rolPubId=? and t.updateDate = ? ";
    Long ct = super.findUnique(hql, rolPubId, updateDate);
    if (ct > 0) {
      return false;
    }
    return true;
  }

  /**
   * 获取成果的引用次数和收录.
   * 
   * @param rolPubId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("rawtypes")
  public Map queryCfmRolPubBypubId(Long rolPubId) throws DaoException {
    return (Map) super.createQuery(
        "select t.citedTimes as citedTimes, t.pubList as pubList from PubConfirmRolPub t where t.rolPubId=?", rolPubId)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
  }

}
