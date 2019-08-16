package com.smate.center.batch.dao.pdwh.pub.pubmed;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.pubmed.PubmedInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 单位pubmed别名表.
 * 
 * @author linyueqin
 * 
 */
@Repository
public class PubmedInsNameDao extends PdwhHibernateDao<PubmedInsName, Long> {

  /**
   * 查询指定单位ID的pubmed别名.
   * 
   * @param insId
   * @return
   */
  public List<PubmedInsName> getPubmedInsName(Long insId) {

    String hql =
        "select new PubmedInsName(id, insId, pubmedName, pubmednLength) from PubmedInsName where insId = ? order by pubmednLength desc";
    return super.find(hql, insId);
  }

  /**
   * 查询指定单位ID的单位别名.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubmedInsName> getPubmedInsName(List<Long> insIds) {

    String hql =
        "select new PubmedInsName(id, insId, pubmedName, pubmednLength) from PubmedInsName where insId in(:insIds) order by pubmednLength desc";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 删除pubmed别名.
   * 
   * @param id
   */
  public void removePubmedInsName(Long id) {

    String hql = "delete from PubmedInsName t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 增加匹配上的次数.
   * 
   * @param id
   */
  public void increateFreq(Long id) {

    String hql = "update PubmedInsName set freq = freq + 1,lastUse = sysdate  where id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取所有机构ID.
   * 
   * @return
   */
  public List<Long> getAllInsId() {

    String hql = "select distinct insId from PubmedInsName ";
    return super.find(hql);
  }

  /**
   * 保存PubmedInsName.
   * 
   * @param insId
   * @param PubmedName
   * @return
   */
  public PubmedInsName savePubmedInsName(Long insId, String pubmedName) {

    String lower = StringUtils.trimToEmpty(StringUtils.lowerCase(pubmedName));
    // 查重
    String hql = "select count(id) from PubmedInsName t where t.insId = ? and t.pubmedName = ? ";
    Long count = super.findUnique(hql, insId, lower);
    if (count > 0) {
      return null;
    }
    PubmedInsName pubmedInsName = new PubmedInsName(getPubmedInsNameId(), insId, pubmedName, pubmedName.length());
    super.save(pubmedInsName);
    return pubmedInsName;
  }

  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getPubmedInsNameId() {
    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_PUBMED_INS_NAME.nextval from dual").uniqueResult();
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

    String hql = "select t.insId,count(t.id) from PubmedInsName t where t.insId in(:insIds) group by t.insId";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 判断单位是否存在别名.
   * 
   * @param insId
   * @return
   */
  public boolean isInsNameExists(Long insId) {
    String hql = "select count(id) from PubmedInsName t where t.insId = ? ";
    Long count = super.findUnique(hql, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
