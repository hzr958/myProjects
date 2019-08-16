package com.smate.center.batch.dao.rcmd.pub;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rcmd.pub.PublicationConfirm;
import com.smate.center.batch.model.rcmd.pub.PublicationConfirmHi;
import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 成果提交持久层.
 * 
 * @author LY
 * 
 */
@Repository
public class PublicationConfirmDao extends RcmdHibernateDao<PublicationConfirm, Long> {

  /**
   * 删除.
   * 
   * @param id
   */
  public void remove(Long id) {

    String hql = "delete from PublicationConfirm t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 删除.
   * 
   * @param id
   */
  public void removeHi(Long id) {

    String hql = "delete from PublicationConfirmHi t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 删除.
   * 
   * @param id
   */
  public void removeHi(Long rolPubId, Long psnId) {

    String hql = "delete from PublicationConfirmHi t where t.rolPubId = ? and  t.psnId = ? ";
    super.createQuery(hql, rolPubId, psnId).executeUpdate();
  }

  /**
   * 保存历史记录.
   * 
   * @param pc
   */
  public void saveHistory(PublicationConfirm pc) {
    this.removeHi(pc.getId());
    super.getSession().save(new PublicationConfirmHi(pc));
  }


  /**
   * 保存历史记录.
   * 
   * @param pc
   */
  /*
   * public void saveHistory(PublicationConfirm pc) { this.removeHi(pc.getId());
   * super.getSession().save(new PublicationConfirmHi(pc)); }
   */

  /**
   * 按PSNID查询个人可以确认的成的单位列表.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> findPubConfirmInsList(Long psnId) throws DaoException {

    // 不需要判断单位是否是审核状态
    String hql =
        "select t.id,t.zhName,t.enName from Institution t where exists(select p.id from PublicationConfirm p where p.psnId=? and p.confirmResult = 0 and p.insId = t.id )";
    return super.createQuery(hql, new Object[] {psnId}).list();
  }

  /**
   * 获取单位人员待确认成果年份，以及年份统计数.
   * 
   * @param insId
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Integer, Long> findPubConfirmYearList(Long insId, Long psnId) {

    String hql =
        "select nvl(t.publishYear,-1),count(t.rolPubId) from PubConfirmRolPub t where exists(select t1.id from PublicationConfirm t1 where t1.psnId = ? and t1.confirmResult = 0 and t.rolPubId = t1.rolPubId) group by t.publishYear order by nvl(t.publishYear,-1) desc ";

    List<Object[]> list = super.createQuery(hql, psnId).list();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    Map<Integer, Long> map = new LinkedHashMap<Integer, Long>();
    for (Object[] obj : list) {
      map.put((Integer) obj[0], (Long) obj[1]);
    }
    return map;
  }



  public PublicationConfirm getPublicationConfirm(Long psnId, Long pubId) throws DaoException {
    String hql = "from PublicationConfirm t where t.psnId=? and t.pubId=?";
    return super.findUnique(hql, new Object[] {psnId, pubId});
  }

  public PublicationConfirm getPublicationConfirmRol(Long psnId, Long rolpubId) {
    String hql = "from PublicationConfirm t where t.psnId=? and rolPubId=?";
    return super.findUnique(hql, new Object[] {psnId, rolpubId});
  }

  public Long savePublicationConfirm(PublicationConfirm confirm) throws DaoException {
    return (Long) super.getSession().save(confirm);
  }

  public PublicationConfirm getPublicationConfirmById(Long psnId, Long cfmId) {
    String hql = "from PublicationConfirm t where t.psnId=? and t.id=?";
    return super.findUnique(hql, new Object[] {psnId, cfmId});
  }

  /**
   * 获取被确认成果所对应的人员psnId.
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPubConfirmByPsnId(List<Long> psnIds) {
    String hql =
        "select distinct t1.psnId from PublicationConfirm t1 where t1.confirmResult=1 and t1.psnId in(:psnIds) order by t1.psnId asc";
    return createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  // 查询人员已经确认rol发出现的成果id
  @SuppressWarnings("unchecked")
  public List<Long> findPubConfirmRolPubIdByPsnId(Long psnId, Long lastRolPubId) {
    String hql =
        "select rolPubId from PublicationConfirm where psnId=? and confirmResult=1 and rolPubId>? order by rolPubId asc";
    Query q = createQuery(hql, psnId, lastRolPubId);
    q.setMaxResults(90);
    return q.list();
  }


  @SuppressWarnings("unchecked")
  public List<Long> findPubPartners(Long psnId, List<Long> rolPubIdList) {
    String hql =
        "select distinct t1.psnId from PublicationConfirm t1,Person t2 where t1.psnId=t2.personId and t2.isAddFrd=0 and t2.isLogin=1 and not exists(select 1 from PsnPrivate pp where pp.psnId=t1.psnId) and t1.psnId not in(select t3.friendPsnId from Friend t3 where t3.psnId=:psnId1) and t1.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId2) and t1.psnId not in(select t6.tempPsnId from FriendTemp t6 where t6.psnId=:psnId3 and t6.tempPsnId is not null) and t1.rolPubId in(:rolPubIdList) and t1.confirmResult=1";
    Query q = createQuery(hql).setParameter("psnId1", psnId).setParameter("psnId2", psnId).setParameter("psnId3", psnId)
        .setParameterList("rolPubIdList", rolPubIdList);
    return q.list();
  }

  /**
   * 批量获取成果确认数据.
   * 
   * @param startId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationConfirm> getPubConfirmBatch(Long startId, int size) {
    String hql = "from PublicationConfirm t where t.id > ? order by t.id asc ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认，加载符合条件的数据.
   * 
   * @param startId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationConfirm> loadReconfirmList(Long startId) {

    String hql =
        "from PublicationConfirm t where t.confirmResult = 1 and syncStatus = 0 and confirmDate < ? and t.syncNum <=3 and t.id > ? order by t.id asc ";
    // 30分钟之前
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, -30);
    Date date = cal.getTime();

    return super.createQuery(hql, date, startId).setMaxResults(50).list();
  }


  /**
   * 更新成果确认历史记录的PSN_ID.
   * 
   * @param id
   * @param psnId
   */
  public void updatePubConfirmHisById(Long id, Long psnId) {
    super.createQuery("update PublicationConfirmHi t set t.psnId=? where t.id=?", psnId, id).executeUpdate();
  }



  @SuppressWarnings("unchecked")
  public List<PublicationConfirm> queryPubConfirmByPsnId(Long psnId) throws DaoException {

    // 不需要判断单位是否是审核状态
    String hql = "from PublicationConfirm t where t.psnId=?";
    return super.createQuery(hql, new Object[] {psnId}).list();
  }


  /**
   * 获取ROL成果ID.
   * 
   * @param cfmId
   * @return
   */
  public Long getRolPubIdByCfmId(Long cfmId) {

    String hql = "select rolPubId from PublicationConfirm t where t.id = ? ";
    return super.findUnique(hql, cfmId);
  }


  @SuppressWarnings("unchecked")
  public List<Long> queryRolPubId(List<Long> rolPubIdList) throws DaoException {
    return super.createQuery(
        "select t1.rolPubId from PubConfirmRolPubPdwhGroup t1 where exists(select 1 from PubConfirmRolPubPdwhGroup t where t1.id = t.id and t.rolPubId in(:rolPubIdList))")
            .setParameterList("rolPubIdList", rolPubIdList).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> queryRolPubIdBySourceId(Long sourceId, String propertyName) throws DaoException {
    return super.createQuery("select t.rolPubId from PubConfirmRolPubPdwh t where t." + propertyName + " = ?", sourceId)
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> queryConfirmPsnIdByRolPubId(List<Long> rolPubIdList) throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("select distinct t.psnId from PublicationConfirmHi t where t.confirmResult = 1");

    Collection<Collection<Long>> container = ServiceUtil.splitList(rolPubIdList, 80);

    if (container != null) {
      hql.append(" and t.rolPubId in(:rolPubId").append(0).append(")");
    }

    for (int i = 1; i < container.size(); i++) {
      hql.append(" or t.rolPubId in(:rolPubId").append(i).append(")");
    }

    Query query = super.createQuery(hql.toString());

    int j = 0;
    for (Collection<Long> c : container) {
      query.setParameterList("rolPubId" + j, c);
      j++;
    }

    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findAllConfirmPubIds(Long psnId) throws DaoException {
    String hql =
        "select t.rolPubId from PubConfirmRolPub t,PublicationConfirm p where t.rolPubId=p.rolPubId and p.confirmResult=0 and p.psnId=?";
    List<Long> list = super.createQuery(hql, psnId).list();
    return list;
  }

  @SuppressWarnings("unchecked")
  public List<Long> findUncomfirmPubIds(Long psnId) {
    String hql =
        "select t.rolPubId from PublicationConfirm t where t.psnId = ? and t.comfirmResult = 0 and t.assignScore > 30";
    return super.createQuery(hql, psnId).list();
  }


  /**
   * 查询需要确认的成果总数.
   * 
   * @param psnId
   * @return
   */
  public Long queryPubConfirmCount(Long psnId) {

    String hql =
        "select count(p.id) from PubConfirmRolPub t,PublicationConfirm p where t.rolPubId=p.rolPubId and p.confirmResult=0 and p.psnId=?";

    return super.findUnique(hql, psnId);

  }
}
