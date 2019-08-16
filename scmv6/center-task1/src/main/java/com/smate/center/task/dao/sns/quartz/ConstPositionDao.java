package com.smate.center.task.dao.sns.quartz;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.ConstPosition;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 职务常量.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstPositionDao extends SnsHibernateDao<ConstPosition, Long> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5720335155047151985L;


  /**
   * 批量获取lang='zh_Cn'的职称名称
   * 
   * @param pageNo
   * @param pageSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> findPositionName(Integer pageNo, Integer pageSize) {
    String hql = "select cp.name from ConstPosition cp where cp.lang='zh_CN' order by cp.id asc";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 获取自动匹配的职务列表.
   * 
   * @param pos
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstPosition> getPosLike(String pos, int size) {
    pos = pos.toLowerCase();
    String hql = "from ConstPosition t where lower(t.name) like ? and t.lang=? order by seqNo";
    List<ConstPosition> list =
        super.createQuery(hql, "%" + pos + "%", ObjectUtils.toString(LocaleContextHolder.getLocale()))
            .setMaxResults(size).list();
    return list;
  }

  /**
   * 获取指定职务ID的等级.
   * 
   * @param id
   * @return
   */
  public Integer getPosGrades(Long id) {

    String hql = "select grades from ConstPosition t where t.id = ?";
    return super.findUnique(hql, id);
  }

  /**
   * 通过名称查找职称等级.
   * 
   * @param pos
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public ConstPosition getPosByName(String name) {

    if (StringUtils.isBlank(name)) {
      return null;
    }
    String hql = "from ConstPosition t where lower(t.name) = ? order by seqNo";
    List<ConstPosition> list = super.createQuery(hql, name.toLowerCase()).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<ConstPosition> getPosListByName(String name) {
    String hql = "from ConstPosition t where instr(?,t.name)>0 order by t.grades desc";
    return super.createQuery(hql, new Object[] {name}).list();
  }

  @SuppressWarnings("unchecked")
  public List<ConstPosition> getPosAllByLocale() {
    String hql = "from ConstPosition t ";
    return super.createQuery(hql).list();
  }

  /**
   * 通过id获取对应中英文职称
   * 
   * @param id
   * @param language
   * @return
   */
  @SuppressWarnings("rawtypes")
  public Map getConstPosition(Long id) {
    String sql =
        "select c.name as ZHNAME,c2.name as ENNAME from const_position c ,const_position c2 where ( c.id=:id or c2.id=:id) and c.code=c2.code  and c.id<>c2.id  and c.lang='zh_CN' and c2.lang='en_US' ";
    return (Map) super.getSession().createSQLQuery(sql).setParameter("id", id)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
  }

  public List<ConstPosition> findByCode(Long code) {
    String hql = "from ConstPosition t where t.code = ?";
    return super.createQuery(hql, code).list();
  }
}
