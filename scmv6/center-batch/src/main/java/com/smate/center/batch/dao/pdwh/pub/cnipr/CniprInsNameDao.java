package com.smate.center.batch.dao.pdwh.pub.cnipr;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnipr.CniprInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 单位CNIPR别名表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CniprInsNameDao extends PdwhHibernateDao<CniprInsName, Long> {

  /**
   * 查询指定单位ID的CNKI别名.
   * 
   * @param insId
   * @return
   */
  public List<CniprInsName> getCniprInsName(Long insId) {

    String hql = "from CniprInsName where insId = ? ";
    return super.find(hql, insId);
  }

  public void removeCniprInsName(Long id) {

    String hql = "delete from CniprInsName t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 增加匹配上的次数.
   * 
   * @param id
   */
  public void increateFreq(Long id) {

    String hql = "update CniprInsName set freq = freq + 1,lastUse = sysdate  where id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取所有机构ID.
   * 
   * @return
   */
  public List<Long> getAllInsId() {

    String hql = "select distinct insId from CniprInsName ";
    return super.find(hql);
  }

  /**
   * 保存CniprInsName.
   * 
   * @param insId
   * @param cnkiName
   * @return
   */
  public CniprInsName saveCniprInsName(Long insId, String cnkiName) {

    String lower = StringUtils.trimToEmpty(StringUtils.lowerCase(cnkiName));
    // 查重
    String hql = "select count(id) from CniprInsName t where t.insId = ? and t.lowerName = ? ";
    Long count = super.findUnique(hql, insId, lower);
    if (count > 0) {
      return null;
    }
    CniprInsName cnkiInsName = new CniprInsName(getCniprInsNameId(), insId, cnkiName, lower, lower.length());
    super.save(cnkiInsName);
    return cnkiInsName;
  }

  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getCniprInsNameId() {
    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_CNIPR_INS_NAME.nextval from dual").uniqueResult();
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

    String hql = "select t.insId,count(t.id) from CniprInsName t where t.insId in(:insIds) group by t.insId";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 判断单位是否存在别名.
   * 
   * @param insId
   * @return
   */
  public boolean isInsNameExists(Long insId) {
    String hql = "select count(id) from CniprInsName t where t.insId = ? ";
    Long count = super.findUnique(hql, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
