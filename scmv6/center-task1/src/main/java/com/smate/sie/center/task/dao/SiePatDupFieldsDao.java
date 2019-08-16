package com.smate.sie.center.task.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SiePatDupFields;

/**
 * 专利查重Dao.
 * 
 * @author jszhou
 *
 */
@Repository
public class SiePatDupFieldsDao extends SieHibernateDao<SiePatDupFields, Long> {

  /**
   * 更新专利查重表 专利所有者
   * 
   * @param patId
   * @param ownerId
   */
  public void updatePatDupOwner(Long patId, Long ownerId) {
    String hql = "update SiePatDupFields t set t.ownerId=? where t.patId=?";
    super.createQuery(hql, ownerId, patId).executeUpdate();
  }

  /**
   * 查询SiePatDupFields .
   * 
   * @param patId
   * @return
   */
  public SiePatDupFields getPatDupFields(Long patId) {
    return super.get(patId);
  }

  /**
   * 删除查重数据.
   * 
   * @param patId
   */
  public void deleteById(Long patId) {

    String hql = "delete from SiePatDupFields where patId = ? ";
    super.createQuery(hql, patId).executeUpdate();
  }

  /**
   * 申请号查重.
   * 
   * @param pubYear
   * @param ownerId
   * @return
   */
  public List<SiePatDupFields> findDupByPatent(Long patentNoHash, Long ownerId) {
    return this.findDupByPatent(patentNoHash, ownerId, SiePatDupFields.NORMAL_STATUS);
  }

  /**
   * 
   * 申请号查重
   * 
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePatDupFields> findDupByPatent(Long patentNoHash, Long ownerId, Integer status) {
    Assert.notNull(ownerId, "ownerId不能为空");
    List<Object> params = new ArrayList<Object>();
    String hql = "select new SiePatDupFields(patId, sourceDbId) from SiePatDupFields where ownerId = ? and status = ? ";
    params.add(ownerId);
    params.add(status);
    if (patentNoHash != null) {
      hql += " and patentNoHash = ? ";
      params.add(patentNoHash);
    } else {
      return null;
    }
    return super.createQuery(hql, params.toArray()).list();
  }

  /**
   * 通过标题查找重复成果.
   * 
   * @param pubYear
   * @param ownerId
   * @return
   */
  public List<SiePatDupFields> findDupByTitle(Long zhTitleHash, Long enTitleHash, Long ownerId, Integer pubYear) {
    return this.findDupByTitle(zhTitleHash, enTitleHash, ownerId, SiePatDupFields.NORMAL_STATUS, pubYear);
  }

  /**
   * 通过标题查找重复成果.
   * 
   * @param pubYear
   * @param ownerId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePatDupFields> findDupByTitle(Long zhTitleHash, Long enTitleHash, Long ownerId, Integer status,
      Integer pubYear) {
    if ((zhTitleHash == null && enTitleHash == null) || ownerId == null) {
      return null;
    }
    ArrayList<Object> param = new ArrayList<Object>();
    zhTitleHash = zhTitleHash == null ? enTitleHash : zhTitleHash;
    enTitleHash = enTitleHash == null ? zhTitleHash : enTitleHash;
    StringBuilder sb = new StringBuilder();
    sb.append(
        "select new SiePatDupFields(patId, sourceDbId) from SiePatDupFields where (zhTitleHash = ? or enTitleHash = ? ) and ownerId = ? and status = ? ");
    param.add(zhTitleHash);
    param.add(enTitleHash);
    param.add(ownerId);
    param.add(status);
    if (pubYear != null) {
      sb.append(" and pubYear = ? ");
      param.add(pubYear);
    }
    return super.createQuery(sb.toString(), param.toArray()).list();
  }

  /**
   * set un dup status.
   * 
   * @param patIds
   */
  public void setDupDisabled(List<Long> patIds) {
    String hql = "update SiePatDupFields t set t.status = 0 where t.patId in(:patIds) ";
    super.createQuery(hql).setParameterList("patIds", patIds).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<SiePatDupFields> getListByInsId(Long insId) {
    String hql = " from SiePatDupFields t where t.ownerId= ? order by t.patId ";
    return super.createQuery(hql, insId).list();
  }
}
