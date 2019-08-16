package com.smate.sie.core.base.utils.dao.validate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.validate.KpiValidateDetail;

/**
 * 科研验证详细表 Dao
 * 
 * @author hd
 *
 */
@Repository
public class KpiValidateDetailDao extends SieHibernateDao<KpiValidateDetail, Long> {

  /**
   * 根据uuId 获取需要处理的数据
   * 
   * @param uuId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KpiValidateDetail> getByUUID(String uuId) {
    String hql = "from KpiValidateDetail where interfaceStatus = 0 and uuId = ?";
    Query queryResult = super.createQuery(hql);
    queryResult.setString(0, uuId);
    return queryResult.list();
  }


  /**
   * 根据uuId 从detail表中待处理的记录数
   * 
   * @param uuId
   * @return
   */
  public Long countNeedHandleKeyId(String uuId) {
    String hql = "select count(id) from KpiValidateDetail where interfaceStatus = 0 and uuId = ?";
    return findUnique(hql, uuId);
  }

  @SuppressWarnings("unchecked")
  public List<KpiValidateDetail> getParamsOuts(String uuid, Integer type) {
    String hql = "from KpiValidateDetail k where k.interfaceStatus = 1 and k.uuId = ? and k.type = ? order by k.id asc";
    Query query = super.createQuery(hql);
    query.setString(0, uuid);
    query.setInteger(1, type);
    return query.list();
  }

  public Long getVdDetailCount(String uuid, int type) {
    String hql = "select count(k.id) from KpiValidateDetail k where k.uuId = ? and k.type = ? ";// k.interfaceStatus = 1
                                                                                                // and
    return findUnique(hql, uuid, type);
  }

  public Long countStatusIsNull(String uuId) {
    String hql =
        "select count(k.id) from KpiValidateDetail k where k.interfaceStatus !=0  and k.status is NULL and k.uuId = ?";
    return findUnique(hql, uuId);
  }

  /**
   * 根据uuid 和 parentId , type 查询kpi_validate_detail
   * 
   * @param uuid
   * @param parentId
   * @param type
   * @return
   */
  public Long getVdDetailCountWithParentId(String uuid, Long parentId, Integer type) {
    String hql = "select count(k.id) from KpiValidateDetail k where k.uuId = ? and k.type = ? and k.parentId = ?"; // k.interfaceStatus
                                                                                                                   // =
                                                                                                                   // 1
                                                                                                                   // and
    return findUnique(hql, uuid, type, parentId);
  }

  /**
   * 根据uuid 和 parentId , type 查询kpi_validate_detail
   * 
   * @param uuid
   * @param parentId
   * @param type
   * @return
   */
  public List<KpiValidateDetail> getParamsOutsWithParentId(String uuid, Long parentId, Integer type) {
    String hql =
        "from KpiValidateDetail k where k.interfaceStatus = 1 and k.uuId = ? and k.type = ? and k.parentId = ? order by k.id asc";
    Query query = super.createQuery(hql);
    query.setString(0, uuid);
    query.setInteger(1, type);
    query.setLong(2, parentId);
    return query.list();
  }


  /**
   * 根据uuId 从detail表中处理失败的记录数
   * 
   * @param uuId
   * @return
   */
  public Long getErrorCount(String uuId) {
    String hql = "select count(id) from KpiValidateDetail where interfaceStatus = 2 and uuId = ?";
    return findUnique(hql, uuId);
  }

  /**
   * 根据uuId 获取需要处理的数据
   * 
   * @param uuId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KpiValidateDetail> getByUUID2(String uuId) {
    String hql = "from KpiValidateDetail where uuId = ?";
    Query queryResult = super.createQuery(hql);
    queryResult.setString(0, uuId);
    return queryResult.list();
  }


  // 查询列表用，不用排序
  @SuppressWarnings("unchecked")
  public List<KpiValidateDetail> findAllDetailByUuid(String uuid) {
    String hql = "from KpiValidateDetail where uuid = ?";
    return super.createQuery(hql, new Object[] {uuid}).list();
  }

  // 申请书
  @SuppressWarnings("unchecked")
  public List<KpiValidateDetail> findDetailByUuid(String uuid) {
    String hql = "from KpiValidateDetail where uuid = ? order by type,id asc";
    return super.createQuery(hql, new Object[] {uuid}).list();
  }

  // 进展报告，结题报告
  @SuppressWarnings("unchecked")
  public List<KpiValidateDetail> findDetailByUuidAndType(String uuid) {
    String hql = "from KpiValidateDetail where uuid = ? and type = 3 order by type,id asc";
    return super.createQuery(hql, new Object[] {uuid}).list();
  }
}
