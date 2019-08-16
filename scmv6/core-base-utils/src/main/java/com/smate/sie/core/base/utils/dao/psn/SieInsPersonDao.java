package com.smate.sie.core.base.utils.dao.psn;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.exception.SysDataException;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;
import com.smate.sie.core.base.utils.model.psn.SieInsPersonPk;

/**
 * 单位人员表
 * 
 * @author hd
 *
 */
@Repository
public class SieInsPersonDao extends SieHibernateDao<SieInsPerson, SieInsPersonPk> {

  /**
   * 通过用户ID获取所在单位的关系数据(包括所有状态).
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public SieInsPerson findPsnInsAllStatus(Long psnId, Long insId) {
    String hql = "from SieInsPerson where pk.psnId=? and pk.insId=?";
    return findUnique(hql, psnId, insId);
  }

  /**
   * 统计单位人员总数.
   * 
   * @param insId
   * @return
   */
  public Long getPsnTotalNumByInsId(Long insId) {
    String hql = "select count(pk.insId) from SieInsPerson where status = 1 and pk.insId = ? ";
    return super.findUnique(hql, insId);
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnByInsId(Long insId) {
    String hql = "select pk.psnId from SieInsPerson where status = 1 and pk.insId = ?";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 统计部门人员总数.
   * 
   * @param unitId
   * @return
   */
  public Long getPsnTotalNumByUnitId(Long unitId) {
    String hql = "select count(pk.insId) from SieInsPerson where status = 1 and unitId = ? ";
    return super.findUnique(hql, unitId);
  }

  /**
   * @param psnId
   * @param insId
   * @return boolean
   * @throws DaoException
   */
  public boolean findPsnIsIns(Long psnId, Long insId) {
    SieInsPerson rolpsn = findUnique("from SieInsPerson where pk.psnId=? and pk.insId=? and status=1", psnId, insId);
    return rolpsn != null ? true : false;
  }

  /**
   * 通过用户ID获取所在单位的关系数据.
   * 
   * @param psnId
   * @param insId
   * @return RolPsnIns
   * @throws DaoException
   */
  public SieInsPerson findPsnIns(Long psnId, Long insId) throws SysDataException {
    String hql = "from SieInsPerson where pk.psnId=? and pk.insId=? and status=1";
    return findUnique(hql, psnId, insId);
  }

  /**
   * 通过人员的名字来获取人员的主键的ID
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdByPsnName(String psnname) {
    String hql = "select t.pk.psnId from SieInsPerson t where t.zhName=? and t.status=1";
    return super.createQuery(hql, psnname).list();
  }

  @SuppressWarnings("unchecked")
  public List<SieInsPerson> getListByInsId(Long insId) {
    String hql = " from SieInsPerson t where t.pk.insId= ?";
    return super.createQuery(hql, insId).list();
  }

  public boolean deleteByInsId(Long mergeid) {
    boolean status = false;
    String hql = "delete from SieInsPerson t where t.pk.insId= ? ";
    int temp = super.createQuery(hql, mergeid).executeUpdate();
    if (temp != -1) {
      status = true;
    }
    return status;
  }

  public void updateInsIdByPsnId(Long psnId, Long insId) {
    String sql = "update PSN_INS set ins_id=:insId where psn_id=:psnId ";
    super.getSession().createSQLQuery(sql).setParameter("insId", insId).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 通过用户ID获取单位id.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public List<Long> findPsnInsIds(Long psnId) {
    String hql = "select pk.insId from SieInsPerson where pk.psnId=? and status=1 order by pk.insId";
    List<Long> insIds = super.createQuery(hql, psnId).list();
    return insIds;
  }

  /**
   * 通过用户ID获取SieInsPerson.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public List<SieInsPerson> findPsnInsListByPsnId(Long psnId) {
    String hql = "from SieInsPerson where pk.psnId=? and status=1 order by pk.insId";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 通过人员的名字来获取人员的主键的ID
   */
  @SuppressWarnings("unchecked")
  public Page<SieInsPerson> getPsnIdByStatus(Page<SieInsPerson> page) {
    String hql = "select distinct new SieInsPerson(t.pk.psnId) from SieInsPerson t where t.status=1";
    Query q = createQuery(hql);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<SieInsPerson> result = q.list();
    page.setResult(result);
    return page;
  }

  /**
   * 统计部门人员.
   * 
   * @param unitId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnByUnitId(Long unitId) {
    String hql = "select distinct pk.psnId from SieInsPerson where status = 1 and unitId = ? ";
    return super.createQuery(hql, unitId).list();
  }



}
