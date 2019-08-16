package com.smate.center.batch.dao.pdwh.pub;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.SciencedirectInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 单位ScienceDirect别名表.
 * 
 * @author linyueqin
 * 
 */
@Repository
public class SciencedirectInsNameDao extends PdwhHibernateDao<SciencedirectInsName, Long> {

  /**
   * 查询指定单位ID的ScienceDirect别名.
   * 
   * @param insId
   * @return
   */
  public List<SciencedirectInsName> getSciencedirectInsName(Long insId) {

    String hql = "from SciencedirectInsName where insId = ? order by sciencedirectnLength desc";
    return super.find(hql, insId);
  }

  /**
   * 删除ScienceDirect别名.
   * 
   * @param id
   */
  public void removeSciencedirectInsName(Long id) {

    String hql = "delete from SciencedirectInsName t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 增加匹配上的次数.
   * 
   * @param id
   */
  public void increateFreq(Long id) {

    String hql = "update SciencedirectInsName set freq = freq + 1,lastUse = sysdate  where id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取所有机构ID.
   * 
   * @return
   */
  public List<Long> getAllInsId() {

    String hql = "select distinct insId from SciencedirectInsName ";
    return super.find(hql);
  }

  /**
   * 保存SciencedirectInsName.
   * 
   * @param insId
   * @param sciencedirectName
   * @return
   */
  public SciencedirectInsName saveSciencedirectInsName(Long insId, String sciencedirectName) {

    String lower = StringUtils.trimToEmpty(StringUtils.lowerCase(sciencedirectName));
    // 查重
    String hql = "select count(id) from SciencedirectInsName t where t.insId = ? and t.sciencedirectName = ? ";
    Long count = super.findUnique(hql, insId, lower);
    if (count > 0) {
      return null;
    }
    SciencedirectInsName sciencedirectInsName =
        new SciencedirectInsName(getSciencedirectInsNameId(), insId, sciencedirectName, sciencedirectName.length());
    super.save(sciencedirectInsName);
    return sciencedirectInsName;
  }

  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getSciencedirectInsNameId() {
    BigDecimal groupId = (BigDecimal) super.getSession()
        .createSQLQuery("select SEQ_SCIENCEDIRECT_INS_NAME.nextval from dual").uniqueResult();
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

    String hql = "select t.insId,count(t.id) from SciencedirectInsName t where t.insId in(:insIds) group by t.insId";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 判断单位是否存在别名.
   * 
   * @param insId
   * @return
   */
  public boolean isInsNameExists(Long insId) {
    String hql = "select count(id) from SciencedirectInsName t where t.insId = ? ";
    Long count = super.findUnique(hql, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
