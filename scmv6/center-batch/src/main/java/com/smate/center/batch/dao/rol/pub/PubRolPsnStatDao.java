package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.PubPsnRol;
import com.smate.center.batch.model.rol.pub.PubRolPsnStat;
import com.smate.center.batch.model.rol.pub.PubRolSubmissionStatKey;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 发送认领邮件.
 * 
 * @author lichangwen
 * 
 */
@Repository
public class PubRolPsnStatDao extends RolHibernateDao<PubRolPsnStat, PubRolSubmissionStatKey> {

  @Autowired
  private RolPsnInsDao rolPsnInsDao;

  /**
   * 按单位刷新个人待认领成果数据.
   * 
   * @param insId
   * @throws DaoException
   */
  public void refreshPsnStatByIns(Long insId) throws DaoException {
    String hql = "call pkg_pub_assign_person.refresh_pub_psn_stat_byIns(?)";
    super.getSession().createSQLQuery(hql).setParameter(0, insId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public void refreshPsnStatByPubId(Long pubId, Long insId) throws DaoException {
    String hql =
        "select t from PubPsnRol t,PublicationRol t1 where  t.insId = ? and t.pubId = ? and t.pubId = t1.id and t1.status = 2  ";
    List<PubPsnRol> list = super.createQuery(hql, insId, pubId).list();
    if (list != null && list.size() > 0) {
      for (PubPsnRol pubpsn : list) {
        this.refreshPsnStatByPsn(insId, pubpsn.getPsnId());
      }
    }
  }

  /**
   * 按单位/人刷新个人待认领成果数据.
   * 
   * @param insId
   * @throws DaoException
   */
  public void refreshPsnStatByPsn(Long insId, Long psnId) throws DaoException {
    // String hql =
    // "call pkg_pub_assign_person.refresh_pub_psn_stat_byPsn(?,?)";
    // super.getSession().createSQLQuery(hql).setParameter(0,
    // insId).setParameter(1, psnId).executeUpdate();
    PubRolPsnStat stat = this.getPubRolPsnStat(psnId, insId);
    if (stat == null) {
      stat = new PubRolPsnStat(psnId, insId);
      super.save(stat);
    }
    // 作废，实时统计
    // 查询已确认的成果总数
    // String hql =
    // "select count(t.id) from PubPsnRol t,PublicationRol t1 where t.psnId = ? and t.insId = ? and
    // t.pubId = t1.id and t1.status = 2 and t.confirmResult = 1 ";
    // Long count = super.findUnique(hql, psnId, insId);
    // stat.setConfirmTotal(count.intValue());
    //
    // hql =
    // "select count(t.id) from PubPsnRol t ,PublicationRol t1 where t.psnId = ? and t.insId = ? and
    // t.pubId = t1.id and t1.status = 2 and t.confirmResult = 0 ";
    // count = super.findUnique(hql, psnId, insId);
    // stat.setPubTotal(count.intValue());
    super.save(stat);
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnIds(Long insId) throws DaoException {
    return createQuery("select key.psnId from PubRolPsnStat where key.insId=?", insId).list();
  }

  /**
   * 获取单个数据.
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubRolPsnStat getPubRolPsnStat(Long psnId, Long insId) throws DaoException {
    return super.findUnique("from PubRolPsnStat where key.psnId = ? and key.insId = ? ", psnId, insId);
  }

  /**
   * 获取单个数据，如果不存在，则创建返回.
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubRolPsnStat getPubRolPsnStatWithCreate(Long psnId, Long insId) throws DaoException {
    PubRolPsnStat stat = this.getPubRolPsnStat(psnId, insId);
    if (stat == null) {
      stat = new PubRolPsnStat(psnId, insId);
      super.save(stat);
      return stat;
    }
    return stat;
  }

  /**
   * 获取需发送认领成果邮件人员列表.
   * 
   * @param page
   * @param unitStr TODO
   * @param unitId TODO
   * @param pName TODO
   * @param email TODO
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void queryAssignMailList(Page<PubRolPsnStat> page, Long insId, String unitStr, String simpleSearchContent,
      Long unitId, String pName, String email) {
    // TODO
  }

  @SuppressWarnings("unchecked")
  public List<PubRolPsnStat> queryPubRolPsnStatByPsnId(Long psnId) throws DaoException {
    return super.getSession().createSQLQuery(
        "select t.* from PUB_PSN_STAT t where t.PSN_ID=:psnId union select t.* from PUB_PSN_STAT t where t.LAST_SEND_BY=:sendBy")
        .addEntity("t", PubRolPsnStat.class).setParameter("psnId", psnId).setParameter("sendBy", psnId).list();
  }

  /**
   * 获取某个单位某个人的待认领成果数.
   * 
   * @param insId
   * @param psnId
   * @return
   */
  public Long getNoConfirmPubNum(Long insId, Long psnId) {
    StringBuilder hql = new StringBuilder();
    hql.append("select count(t.psnId) from PubPsnRol t where t.insId = ? and t.psnId = ? and t.confirmResult = 0");
    return super.findUnique(hql.toString(), insId, psnId);
  }

}
