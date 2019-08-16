package com.smate.web.prj.dao.sns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.PsnIns;
import com.smate.core.base.utils.model.security.PsnInsPk;

/**
 * 
 * Rol 人员与单位关系表DAO.
 * 
 * @author lichangwen
 * 
 */
@Repository
public class PsnInsDao extends SnsHibernateDao<PsnIns, PsnInsPk> {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 根据人员ID获取单位关系列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnIns> findPsnInsList(Long psnId) {

    return super.createQuery("from PsnIns  t where t.pk.psnId = ? and t.status=1", psnId).list();
  }


  /**
   * @param psnId
   * @param insId
   * @return boolean
   * @throws DaoException
   */
  public boolean findPsnIsIns(Long psnId, Long insId) {
    PsnIns rolpsn = findUnique("from PsnIns where pk.psnId=? and pk.insId=? and status=1", psnId, insId);
    return rolpsn != null ? true : false;
  }

  /**
   * 通过用户ID获取所在单位的关系数据.
   * 
   * @param psnId
   * @param insId
   * @return PsnIns
   * @throws DaoException
   */
  public PsnIns findPsnIns(Long psnId, Long insId) {
    String hql = "from PsnIns where pk.psnId=? and pk.insId=? and status=1";
    return findUnique(hql, psnId, insId);
  }

  /**
   * 获取等待审核的单位人员数.
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public Long getNeedApprovePsnNum(Long insId, Long unitId) {

    List<Object> param = new ArrayList<Object>();
    String hql = "select count(t.pk.psnId) from PsnIns t where t.status = 0 and t.pk.insId = ? ";
    param.add(insId);
    if (unitId != null && unitId != 0) {
      hql += " and (t.unitId = ? or t.superUnitId = ? ) ";
      param.add(unitId);
      param.add(unitId);
    }
    return super.findUnique(hql, param.toArray());
  }

  /**
   * 获取无部门人员统计数.
   * 
   * @param insId
   * @return
   */
  public Long getNoUnitPsnNum(Long insId) {

    String hql =
        "select count(t.pk.psnId) from PsnIns t where t.status = 1 and t.pk.insId = ? and t.unitId is null and t.cyFlag = 1 ";
    return super.findUnique(hql, insId);
  }

  /**
   * 获取人员所在单位部门ID.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Long getPsnUnitId(Long psnId, Long insId) {
    String hql = "select unitId from PsnIns t where t.pk.insId = ? and t.pk.psnId = ? ";
    List<Long> list = super.createQuery(hql, insId, psnId).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 通过用户ID判断人员是否在单位.
   * 
   * @param psnId
   * @param insId
   * @return boolean
   * @throws DaoException
   */
  public boolean isPsnInIns(Long psnId, Long insId) {
    String hql = "select count(pk.psnId) from PsnIns where pk.psnId=? and pk.insId=? and status=1";
    Long count = findUnique(hql, psnId, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 通过用户ID获取所在单位的关系数据(包括所有状态).
   * 
   * @param psnId
   * @param insId
   * @return PsnIns
   * @throws DaoException
   */
  public PsnIns findPsnInsAllStatus(Long psnId, Long insId) {
    String hql = "from PsnIns where pk.psnId=? and pk.insId=?";
    return findUnique(hql, psnId, insId);
  }

  /**
   * 获取人员所在单位部门.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnIns> getPsnUnitId(Long insId, Collection<Long> psnIds) {
    if (psnIds != null && psnIds.size() > 0) {
      String hql =
          "select new PsnIns(t.pk,t.unitId,t.superUnitId) from PsnIns t where t.status = 1 and (t.unitId is not null or t.superUnitId is not null ) and t.pk.psnId  in(:psnIds) and t.pk.insId = :insId ";
      return super.createQuery(hql).setParameterList("psnIds", psnIds).setParameter("insId", insId).list();
    }
    return null;
  }

  /**
   * 获取用户firstName,lastName,且不能为空.
   * 
   * @param psnId
   * @return
   */
  public PsnIns getPsnInsName(Long psnId) {
    String hql =
        "select new PsnIns(pk, firstName, lastName,otherName,zhName) from PsnIns where pk.psnId = ? and firstName is not null and lastName is not null and  status=1 and pk.insId <> 2565 ";
    List<PsnIns> list = super.find(hql, psnId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getAllPsnIdByInsId(Long insId) {
    String hql = "select pk.psnId from PsnIns where pk.insId=:insId and status=1";
    return super.createQuery(hql).setParameter("insId", insId).list();
  }
}
