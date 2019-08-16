package com.smate.web.group.dao.group;


import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.pub.Discipline;

@Repository
public class DisciplineDao extends SnsHibernateDao<Discipline, Long> {
  /**
   * 获取一级学科
   * 
   * @return
   * @throws Exception
   */
  public List<Map<Object, Object>> getTopDisciplineLists() throws Exception {
    String hql =
        "select distinct topCategoryId as topCategoryId,topCategoryZhName as topCategoryZhName,topCategoryEnName as topCategoryEnName from Discipline order by topCategoryId asc";
    return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

  }

  /**
   * 根据一级学科获取二级学科
   * 
   * @return
   * @throws Exception
   */
  public List<Discipline> getsecondDisciplineLists(Long topCategoryId) throws Exception {
    String hql =
        "select  new Discipline(secondCategoryZhName,secondCategoryId,secondCategoryEnName) from Discipline where topCategoryId=:topCategoryId order by secondCategoryId asc";
    return super.createQuery(hql).setParameter("topCategoryId", topCategoryId).list();
  }

  /**
   * 根据二级学科获取一级学科
   * 
   * @return
   * @throws Exception
   */
  public Discipline getTopDisciplineById(Long secondCategoryId) throws Exception {
    String hql =
        "select  new Discipline(topCategoryId,topCategoryZhName,topCategoryEnName) from Discipline where secondCategoryId=? ";
    return super.findUnique(hql, secondCategoryId);
  }

  /**
   * 根据二级学科获取二级名称
   * 
   * @return
   * @throws Exception
   */
  public Discipline getSecondDisciplinetById(Long secondCategoryId) throws Exception {
    String hql =
        "select  new Discipline(secondCategoryZhName, secondCategoryId,secondCategoryEnName) from Discipline where secondCategoryId=? ";
    return super.findUnique(hql, secondCategoryId);
  }

  /**
   * 根据一级学科获取一级名称
   * 
   * @return
   * @throws Exception
   */
  public Discipline getFirstDisciplinetNameById(Integer topCategoryId) {
    if (topCategoryId == null) {
      return null;
    }
    String hql =
        "select new Discipline(t.topCategoryId,t.topCategoryZhName,t.topCategoryEnName) from Discipline t where t.topCategoryId=:topCategoryId";
    List<Discipline> list =
        super.createQuery(hql).setParameter("topCategoryId", Long.parseLong(topCategoryId.toString())).list();
    if (list != null) {
      return list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 根据二级学科获取二级名称
   * 
   * @return
   * @throws Exception
   */
  public Discipline getSecondDisciplinetNameById(Integer secondCategoryId) {
    if (secondCategoryId == null) {
      return null;
    }
    String hql =
        "select new Discipline(t.secondCategoryZhName,t.secondCategoryEnName) from Discipline t where t.secondCategoryId=:secondCategoryId";
    return (Discipline) super.createQuery(hql)
        .setParameter("secondCategoryId", Long.parseLong(secondCategoryId.toString())).uniqueResult();
  }

}
