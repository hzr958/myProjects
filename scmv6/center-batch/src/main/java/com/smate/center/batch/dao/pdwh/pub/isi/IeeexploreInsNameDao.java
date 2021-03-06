package com.smate.center.batch.dao.pdwh.pub.isi;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.IeeexploreInsName;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 单位IEEEXplore别名表.
 * 
 * @author linyueqin
 * 
 */
@Repository
public class IeeexploreInsNameDao extends PdwhHibernateDao<IeeexploreInsName, Long> {

  /**
   * 查询指定单位ID的IEEEXplore别名.
   * 
   * @param insId
   * @return
   */
  public List<IeeexploreInsName> getIeeexploreInsName(Long insId) {

    String hql = "from IeeexploreInsName where insId = ? order by ieeexplorenLength desc";
    return super.find(hql, insId);
  }

  /**
   * 删除IEEEXplore别名.
   * 
   * @param id
   */
  public void removeIeeexploreInsName(Long id) {

    String hql = "delete from IeeexploreInsName t where t.id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 增加匹配上的次数.
   * 
   * @param id
   */
  public void increateFreq(Long id) {

    String hql = "update IeeexploreInsName set freq = freq + 1,lastUse = sysdate  where id = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  /**
   * 获取所有机构ID.
   * 
   * @return
   */
  public List<Long> getAllInsId() {

    String hql = "select distinct insId from IeeexploreInsName ";
    return super.find(hql);
  }

  /**
   * 保存IeeexploreInsName.
   * 
   * @param insId
   * @param ieeexploreName
   * @return
   */
  public IeeexploreInsName saveIeeexploreInsName(Long insId, String ieeexploreName) {

    String lower = StringUtils.trimToEmpty(StringUtils.lowerCase(ieeexploreName));
    // 查重
    String hql = "select count(id) from IeeexploreInsName t where t.insId = ? and t.ieeexploreName = ? ";
    Long count = super.findUnique(hql, insId, lower);
    if (count > 0) {
      return null;
    }
    IeeexploreInsName ieeexploreInsName =
        new IeeexploreInsName(getIeeexploreInsNameId(), insId, ieeexploreName, ieeexploreName.length());
    super.save(ieeexploreInsName);
    return ieeexploreInsName;
  }

  /**
   * 获取机构别名ID.
   * 
   * @return
   */
  public Long getIeeexploreInsNameId() {
    BigDecimal groupId = (BigDecimal) super.getSession()
        .createSQLQuery("select SEQ_IEEEXPLORE_INS_NAME.nextval from dual").uniqueResult();
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

    String hql = "select t.insId,count(t.id) from IeeexploreInsName t where t.insId in(:insIds) group by t.insId";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 判断单位是否存在别名.
   * 
   * @param insId
   * @return
   */
  public boolean isInsNameExists(Long insId) {
    String hql = "select count(id) from IeeexploreInsName t where t.insId = ? ";
    Long count = super.findUnique(hql, insId);
    if (count > 0) {
      return true;
    }
    return false;
  }
}
