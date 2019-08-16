package com.smate.web.psn.dao.attention;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.attention.AttPerson;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;
import com.smate.web.psn.model.setting.UserSettings;

/**
 * 人员关注DAO
 *
 * @author wsn
 * @createTime 2017年6月21日 下午4:13:31
 *
 */
@Repository
public class AttPersonDao extends SnsHibernateDao<AttPerson, Long> {

  /**
   * 查找关注记录
   * 
   * @param psnId
   * @param refPsnId
   * @return
   * @throws DaoException
   */
  public AttPerson find(Long psnId, Long refPsnId) throws DaoException {
    String hql = "From AttPerson t where t.psnId=:psnId and t.refPsnId=:refPsnId";
    return (AttPerson) super.createQuery(hql).setParameter("psnId", psnId).setParameter("refPsnId", refPsnId)
        .uniqueResult();
  }

  /**
   * 通过psnId查找关注人数
   * 
   */
  public Long getFollowingPsnCount(Long psnId) {
    String hql = "select count(1) from AttPerson t where t.psnId=:psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 通过psnId查找关注人员psnId
   * 
   */
  @SuppressWarnings("unchecked")
  public List<Long> getFollowingPsnIds(Long psnId) {
    String hql = "select t.refPsnId  from AttPerson t where t.psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public List<AttPerson> loadAttPersonList(UserSettings userSettings) throws DaoException {

    String hql = "From AttPerson t where t.psnId=?  and exists (select 1 from Person  p where p.personId = t.refPsnId)";
    // .setFirstResult(userSettings.getBegin()).setMaxResults(userSettings.getFetchSize()
    // + 1)默认多查询一条，判断是否还存下一页
    List<AttPerson> attPersons = super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId()}).list();//
    if (attPersons != null) {
      for (AttPerson aps : attPersons) {
        aps.setRefDes3PsnId(ServiceUtil.encodeToDes3(aps.getRefPsnId().toString()));
      }
    }
    return attPersons;

  }

  public Long getAllAttPsnCount(Long psnId) throws DaoException {

    String hql =
        "select count(*) from  AttPerson t where t.psnId=?  and t.refPsnId<>? and exists (select 1 from Person  p where p.personId = t.refPsnId)";
    return findUnique(hql, new Object[] {psnId, psnId});

  }

  @SuppressWarnings("unchecked")
  public List<AttPerson> getCurAttPersonList(UserSettings userSettings) throws DaoException {
    String hql = "From AttPerson where psnId=?";
    List<AttPerson> attPersons = super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId()})
        .setFirstResult(userSettings.getBegin()).setMaxResults(userSettings.getFetchSize()).list();
    return attPersons;

  }

  public void updatePersonInfo(SnsPersonSyncMessage msg) throws DaoException {
    try {
      String hql =
          "update AttPerson t set t.refPsnName=?,t.refFirstName=?,t.refLastName=?,t.refHeadUrl=?,t.refTitle=?,t.refInsName=? where t.refPsnId=?";
      super.createQuery(hql, new Object[] {msg.getName(), msg.getFirstName(), msg.getLastName(), msg.getAvatars(),
          msg.getTitolo(), msg.getInsName(), msg.getPsnId()}).executeUpdate();
    } catch (Exception e) {

      logger.error("同步关注人员{}冗余数据失败！", msg.getPsnId());
      throw new DaoException(e);

    }
  }
}
