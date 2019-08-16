package com.smate.center.batch.dao.pdwh.pub.ei;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.ei.EiInsName;
import com.smate.center.batch.model.pdwh.pub.isi.IsiInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 单位EI别名表.
 * 
 * @author linyueqin
 * 
 */
@Repository
public class EiInsNameDao extends PdwhHibernateDao<EiInsName, Long> {

  /**
   * 查询指定单位ID的EI别名.
   * 
   * @param insId
   * @return
   */
  public List<EiInsName> getEiInsName(Long insId) {

    String hql = "from EiInsName where insId = ? order by einLength desc";
    return super.find(hql, insId);
  }

  /**
   * 查询指定单位ID的EI别名.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<EiInsName> getEiInsName(List<Long> insIds) {

    String hql =
        "select new EiInsName(id, insId, eiName, einLength) from EiInsName where insId in (:insIds) order by einLength desc";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 删除EI别名.
   * 
   * @param id
   */
  public void removeEiInsName(Long id) {

    String hql = "delete from EiInsName t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 增加匹配上的次数.
   * 
   * @param id
   */
  public void increateFreq(Long id) {

    String hql = "update EiInsName set freq = freq + 1,lastUse = sysdate  where id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取所有机构ID.
   * 
   * @return
   */
  public List<Long> getAllInsId() {

    String hql = "select distinct insId from EiInsName ";
    return super.find(hql);
  }

  /**
   * 保存EiInsName.
   * 
   * @param insId
   * @param eiName
   * @return
   */
  public EiInsName saveEiInsName(Long insId, String eiName) {

    String lower = StringUtils.trimToEmpty(StringUtils.lowerCase(eiName));
    // 查重
    String hql = "select count(id) from EiInsName t where t.insId = ? and t.eiName = ? ";
    Long count = super.findUnique(hql, insId, lower);
    if (count > 0) {
      return null;
    }
    EiInsName eiInsName = new EiInsName(getEiInsNameId(), insId, eiName, eiName.length());
    super.save(eiInsName);
    return eiInsName;
  }

  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getEiInsNameId() {
    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_EI_INS_NAME.nextval from dual").uniqueResult();
    return groupId.longValue();
  }

  /**
   * 统计单位别名个数.
   * 
   * @param insIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getInsNameStat(List<Long> insIds) {

    String hql = "select t.insId,count(t.id) from EiInsName t where t.insId in(:insIds) group by t.insId";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 判断单位是否存在别名.
   * 
   * @param insId
   * @return
   */
  public boolean isInsNameExists(Long insId) {
    String hql = "select count(id) from EiInsName t where t.insId = ? ";
    Long count = super.findUnique(hql, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
