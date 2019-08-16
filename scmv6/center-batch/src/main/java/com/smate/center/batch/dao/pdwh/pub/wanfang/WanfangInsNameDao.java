package com.smate.center.batch.dao.pdwh.pub.wanfang;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.wanfang.WanfangInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 单位万方别名表.
 * 
 * @author linyueqin
 * 
 */
@Repository
public class WanfangInsNameDao extends PdwhHibernateDao<WanfangInsName, Long> {

  /**
   * 查询指定单位ID的万方别名.
   * 
   * @param insId
   * @return
   */
  public List<WanfangInsName> getWanfangInsName(Long insId) {

    String hql = "from WanfangInsName where insId = ? order by wanfangnLength desc";
    return super.find(hql, insId);
  }

  /**
   * 删除万方别名.
   * 
   * @param id
   */
  public void removeWanfangInsName(Long id) {

    String hql = "delete from WanfangInsName t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 增加匹配上的次数.
   * 
   * @param id
   */
  public void increateFreq(Long id) {

    String hql = "update WanfangInsName set freq = freq + 1,lastUse = sysdate  where id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取所有机构ID.
   * 
   * @return
   */
  public List<Long> getAllInsId() {

    String hql = "select distinct insId from WanfangInsName ";
    return super.find(hql);
  }

  /**
   * 保存WanfangInsName.
   * 
   * @param insId
   * @param WanfangName
   * @return
   */
  public WanfangInsName saveWanfangInsName(Long insId, String wanfangName) {

    String lower = StringUtils.trimToEmpty(StringUtils.lowerCase(wanfangName));
    // 查重
    String hql = "select count(id) from WanfangInsName t where t.insId = ? and t.wanfangName = ? ";
    Long count = super.findUnique(hql, insId, lower);
    if (count > 0) {
      return null;
    }
    WanfangInsName wanfangInsName = new WanfangInsName(getWanfangInsNameId(), insId, wanfangName, wanfangName.length());
    super.save(wanfangInsName);
    return wanfangInsName;
  }

  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getWanfangInsNameId() {
    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_WANFANG_INS_NAME.nextval from dual").uniqueResult();
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

    String hql = "select t.insId,count(t.id) from WanfangInsName t where t.insId in(:insIds) group by t.insId";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 判断单位是否存在别名.
   * 
   * @param insId
   * @return
   */
  public boolean isInsNameExists(Long insId) {
    String hql = "select count(id) from WanfangInsName t where t.insId = ? ";
    Long count = super.findUnique(hql, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
