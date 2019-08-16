package com.smate.center.task.model.pdwh.pub;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.IsiInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 单位ISI别名.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class IsiInsNameDao extends PdwhHibernateDao<IsiInsName, Long> {

  /**
   * 查询指定单位ID的isi别名.
   * 
   * @param insId
   * @return
   */
  public List<IsiInsName> getIsiInsName(Long insId) {

    String hql =
        "select new IsiInsName(id, insId, isiName, isinLength) from IsiInsName where insId = ? order by isinLength desc";
    return super.find(hql, insId);
  }

  /**
   * 删除ISI单位别名.
   * 
   * @param id
   */
  public void removeIsiInsName(Long id) {

    String hql = "delete from IsiInsName t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 查询指定单位ID的isi别名.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<IsiInsName> getIsiInsName(List<Long> insIds) {

    String hql =
        "select new IsiInsName(id, insId, isiName, isinLength) from IsiInsName where insId in (:insIds) order by isinLength desc";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 保存IsiInsName.
   * 
   * @param insId
   * @param isiName
   * @return
   */
  public IsiInsName saveIsiInsName(Long insId, String isiName) {

    // 查重
    String hql = "select count(id) from IsiInsName t where t.insId = ? and t.isiName = ? ";
    Long count = super.findUnique(hql, insId, isiName);
    if (count > 0) {
      return null;
    }
    IsiInsName isiInsName = new IsiInsName(getIsiInsNameId(), insId, isiName, isiName.length());
    super.save(isiInsName);
    return isiInsName;
  }

  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getIsiInsNameId() {
    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_ISI_INS_NAME.nextval from dual").uniqueResult();
    return groupId.longValue();
  }

  /**
   * 增加匹配上的次数.
   * 
   * @param id
   */
  public void increateFreq(Long id) {

    String hql = "update IsiInsName set freq = freq + 1,lastUse = sysdate  where id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取所有机构别名.
   * 
   * @return
   */
  public List<IsiInsName> getAllByLength() {
    String hql = "from IsiInsName order by isinLength asc";
    return super.find(hql);
  }

  /**
   * 获取所有机构ID.
   * 
   * @return
   */
  public List<Long> getAllInsId() {

    String hql = "select distinct insId from IsiInsName ";
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

    String hql = "select t.insId,count(t.id) from IsiInsName t where t.insId in(:insIds) group by t.insId";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 判断单位是否存在别名.
   * 
   * @param insId
   * @return
   */
  public boolean isInsNameExists(Long insId) {
    String hql = "select count(id) from IsiInsName t where t.insId = ? ";
    Long count = super.findUnique(hql, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 通过insName查其他的别名
   * 
   * @param insName
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getSimilarInsName(String isiName) {
    String hql =
        "select isiName from  IsiInsName where insId in(select insId from IsiInsName where isiName =:isiName )";
    return super.createQuery(hql).setParameter("isiName", isiName).list();
  }

}
