package com.smate.center.batch.dao.rol.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubInsSync;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * pub-ins同步DAO.
 * 
 */
@Repository
public class PubInsSyncDao extends RolHibernateDao<PubInsSync, Long> {

  /**
   * 按主键读取.
   * 
   * @param snsPubId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubInsSync getPubInsSyncByPK(Long snsPubId, Long insId) throws DaoException {
    return (PubInsSync) super.createQuery("from PubInsSync t where t.id.snsPubId = ? and t.id.insId = ?",
        new Object[] {snsPubId, insId}).uniqueResult();
  }

  /**
   * 保存/添加记录.
   * 
   * @param rec
   * @throws DaoException
   */
  public void savePubInsSync(PubInsSync rec) throws DaoException {
    super.save(rec);
  }

  /**
   * 删除记录.
   * 
   * @param snsPubId
   * @param insId
   * @throws DaoException
   */
  public void deletePubInsSync(Long snsPubId, Long insId) throws DaoException {
    super.createQuery("delete from PubInsSync t where t.id.snsPubId=? and t.id.insId=?", new Object[] {snsPubId, insId})
        .executeUpdate();
  }

  /**
   * 获取单位人员成果总数.
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws DaoException
   */
  public Long getInsPsnPubCount(Long psnId, Long insId) throws DaoException {

    Long count =
        super.findUnique("select count(t.psnId) from PubInsSync t where t.psnId=? and t.id.insId=? ", psnId, insId);
    return count;
  }

  /**
   * 查询未提交的成果状态.
   * 
   * @param insId
   * @param psnId
   * @param startYear
   * @param endYear
   * @param pubTypeId
   * @param pageIndex
   * @param pageSize
   * @param state
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<PubInsSync> queryPrepareOutputsForRO(Long insId, List<Long> psnIds, Integer startYear, Integer endYear,
      Integer pubTypeId, String pubTitle, String orderBy, Integer pageIndex, Integer pageSize) throws DaoException {

    Page<PubInsSync> page = new Page<PubInsSync>();

    if (psnIds == null || psnIds.size() == 0) {
      return page;
    }

    Map params = new HashMap();

    String countHql = "select count(t.id.snsPubId) ";
    String listHql = "";
    String orderHql = " order by t.publishYear desc, t.publishMonth desc, t.publishDay desc, t.id ";

    StringBuilder hql = new StringBuilder();
    hql.append("from PubInsSync t where  t.isSubmited = 0 and t.id.insId = :insId and psnId in (:psnIds) ");
    params.put("insId", insId);
    params.put("psnIds", psnIds);

    if (startYear != null && startYear > 0) {
      hql.append(" and t.publishYear>= :startYear ");
      params.put("startYear", startYear);
    }
    if (endYear != null && endYear > 0) {
      hql.append(" and t.publishYear<=:endYear ");
      params.put("endYear", endYear);
    }
    if (pubTypeId != null && pubTypeId > 0) {
      hql.append(" and t.typeId = :pubTypeId");
      params.put("pubTypeId", pubTypeId);
    }

    if (StringUtils.isNotBlank(pubTitle)) {
      hql.append(" and t.title like :title ");
      params.put("title", pubTitle.toLowerCase());
    }

    // 记录数
    Long totalCount = (Long) super.findUnique(countHql + hql, params);
    page.setTotalCount(totalCount);

    if (StringUtils.isNotBlank(orderBy)) {
      orderHql = " order by " + orderBy + ",t.id.snsPubId ";
    }
    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql, params);
    queryResult.setFirstResult(pageIndex - 1);
    queryResult.setMaxResults(pageSize);
    page.setResult(queryResult.list());
    return page;
  }

  @SuppressWarnings("unchecked")
  public List<PubInsSync> queryPubInsSyncByPsnId(Long psnId) throws ServiceException {
    return super.createQuery("from PubInsSync t where t.psnId = ?", psnId).list();
  }
}
