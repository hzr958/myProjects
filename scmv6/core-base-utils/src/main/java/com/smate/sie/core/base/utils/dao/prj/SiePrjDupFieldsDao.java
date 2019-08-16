package com.smate.sie.core.base.utils.dao.prj;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.prj.SiePrjDupFields;

/**
 * 字段查重DAO.
 * 
 * @author yexingyuan
 */
@Repository
public class SiePrjDupFieldsDao extends SieHibernateDao<SiePrjDupFields, Long> {

  @SuppressWarnings("unchecked")
  public List<SiePrjDupFields> getListByInsId(Long insId) {
    String hql = " from SiePrjDupFields t where t.ownerInsId= ? order by t.prjId ";
    return super.createQuery(hql, insId).list();
  }

  /**
   * 查找一条有相同标题 项目名称+资助机构编号
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPrjId(Integer ztefigerPrint, Integer eteFigerPrint, Long ownerInsId) {
    List<Object> params = new ArrayList<Object>();
    String hql = "select t.prjId  from SiePrjDupFields t where t.ownerInsId = ? ";
    StringBuilder sb = new StringBuilder();
    sb.append(hql);
    params.add(ownerInsId);
    if (ztefigerPrint != null && eteFigerPrint != null) {
      sb.append(" and (ztefigerPrint = ? or eteFigerPrint = ? )");
      params.add(ztefigerPrint);
      params.add(eteFigerPrint);
    } else if (ztefigerPrint != null) {
      sb.append(" and ztefigerPrint = ? ");
      params.add(ztefigerPrint);
    } else if (eteFigerPrint != null) {
      sb.append(" and eteFigerPrint = ? ");
      params.add(eteFigerPrint);
    }
    // 查询结果并返回
    return super.createQuery(sb.toString(), params.toArray()).list();
  }

  /**
   * 查找一条有相同标题 项目名称+项目来源
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDupPrjId2(Integer zeffigerPrint, Integer etfFigerPrint, Long ownerInsId) {
    List<Object> params = new ArrayList<Object>();
    String hql = "select t.prjId  from SiePrjDupFields t where t.ownerInsId = ? ";
    StringBuilder sb = new StringBuilder();
    sb.append(hql);
    params.add(ownerInsId);
    if (zeffigerPrint != null && etfFigerPrint != null) {
      sb.append(" and (zeffigerPrint = ? or etfFigerPrint = ? )");
      params.add(zeffigerPrint);
      params.add(etfFigerPrint);
    } else if (zeffigerPrint != null) {
      sb.append(" and zeffigerPrint = ? ");
      params.add(zeffigerPrint);
    } else if (etfFigerPrint != null) {
      sb.append(" and etfFigerPrint = ? ");
      params.add(etfFigerPrint);
    }
    // 查询结果并返回
    return super.createQuery(sb.toString(), params.toArray()).list();
  }

  /**
   * 
   *
   * @descript 查询重复数据
   */
  public SiePrjDupFields getByPrjId(Long prjId) {
    return (SiePrjDupFields) super.createQuery("from SiePrjDupFields t where t.prjId = ?", prjId).uniqueResult();
  }

}
