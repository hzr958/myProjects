package com.smate.center.batch.dao.pdwh.pub.sps;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.sps.SpsInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 单位SCOPUS别名.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SpsInsNameDao extends PdwhHibernateDao<SpsInsName, Long> {

  /**
   * 查询指定单位ID的isi别名.
   * 
   * @param insId
   * @return
   */
  public List<SpsInsName> getSpsInsName(Long insId) {

    String hql =
        "select new SpsInsName(id, insId, spsName, spsnLength) from SpsInsName where insId = ? order by spsnLength desc";
    return super.find(hql, insId);
  }

  /**
   * 删除scopus别名.
   * 
   * @param id
   */
  public void removeSpsInsName(Long id) {
    String hql = " delete from SpsInsName t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 查询指定单位ID的单位别名.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SpsInsName> getSpsInsName(List<Long> insIds) {

    String hql =
        "select new SpsInsName(id, insId, spsName, spsnLength) from SpsInsName where insId in(:insIds) order by spsnLength desc";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 保存SpsInsName.
   * 
   * @param insId
   * @param spsName
   * @return
   */
  public SpsInsName saveSpsInsName(Long insId, String spsName) {

    // 查重
    String hql = "select count(id) from SpsInsName t where t.insId = ? and t.spsName = ? ";
    Long count = super.findUnique(hql, insId, spsName);
    if (count > 0) {
      return null;
    }
    SpsInsName spsInsName = new SpsInsName(getSpsInsNameId(), insId, spsName, spsName.length());
    super.save(spsInsName);
    return spsInsName;
  }

  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getSpsInsNameId() {
    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_SPS_INS_NAME.nextval from dual").uniqueResult();
    return groupId.longValue();
  }

  /**
   * 增加匹配上的次数.
   * 
   * @param id
   */
  public void increateFreq(Long id) {

    String hql = "update SpsInsName set freq = freq + 1,lastUse = sysdate  where id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取所有机构别名.
   * 
   * @return
   */
  public List<SpsInsName> getAllByLength() {
    String hql = "from SpsInsName order by spsnLength asc";
    return super.find(hql);
  }

  /**
   * 获取所有机构ID.
   * 
   * @return
   */
  public List<Long> getAllInsId() {

    String hql = "select distinct insId from SpsInsName ";
    return super.find(hql);
  }

  /**
   * 统计单位别名个数.
   * 
   * @param insIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getInsNameStat(List<Long> insIds) {

    String hql = "select t.insId,count(t.id) from SpsInsName t where t.insId in(:insIds) group by t.insId";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 判断单位是否存在别名.
   * 
   * @param insId
   * @return
   */
  public boolean isInsNameExists(Long insId) {
    String hql = "select count(id) from SpsInsName t where t.insId = ? ";
    Long count = super.findUnique(hql, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
