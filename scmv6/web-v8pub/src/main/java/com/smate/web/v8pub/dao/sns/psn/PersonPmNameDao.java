package com.smate.web.v8pub.dao.sns.psn;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PersonPmName;

/**
 * PERSON人员姓名变换记录表
 * 
 * @author LIJUN
 * @date 2018年3月20日
 */
@Repository
public class PersonPmNameDao extends SnsHibernateDao<PersonPmName, Long> {

  /**
   * 根据人名获取psnId
   * 
   * @param name
   * @return
   * @author LIJUN
   * @date 2018年3月20日
   */
  @SuppressWarnings("unchecked")
  public List<PersonPmName> getPsnByName(String name) {
    String hql = "from PersonPmName where name=:name";
    return super.createQuery(hql).setParameter("name", name).list();

  }

  /**
   * 根据人名和insId获取记录（联合人员工作经历和教育经历查询）
   *
   * @param name
   * @return
   * @author LIJUN
   * @date 2018年3月20日
   */
  @SuppressWarnings("unchecked")
  public List<PersonPmName> getPsnByNameAndInsId(String name, Long insId) {
    String hql =
        "from PersonPmName t where  t.name=:name and (t.insId=:insId or exists (select 1 from WorkHistory t2 where t2.psnId=t.psnId and t2.insId=:insId) or exists(select 1 from EducationHistory t3 where t3.psnId=t.psnId and t3.insId=:insId))";
    return super.createQuery(hql).setParameter("name", name).setParameter("insId", insId).list();

  }


  @SuppressWarnings("unchecked")
  public List<String> getUserNameByPsnId(Long psnId) {
    String hql = "select distinct(t.name) from PersonPmName t where t.psnId =:psnId ";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getUserNameByPsnIdByType(Long psnId, Integer type) {
    String hql = "select distinct(t.name) from PersonPmName t where t.psnId =:psnId and t.type =:type";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("type", type).list();
  }
}
