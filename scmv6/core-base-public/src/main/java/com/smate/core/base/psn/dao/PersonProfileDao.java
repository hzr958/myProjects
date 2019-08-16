package com.smate.core.base.psn.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.form.SearchPersonForm;
import com.smate.core.base.psn.model.NodePerson;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;

/**
 * 人员基本信息数据接口
 * 
 * @author zk
 *
 */

@Repository
public class PersonProfileDao extends SnsHibernateDao<Person, Long> {

  /**
   * 更新用户工作单位和头衔
   *
   * @param insId
   * @param insName
   * @param personId
   * @param title
   * @return
   */
  public int updateInsAndTitle(Long insId, String insName, Long personId, String title) {
    String hsql = "update Person set ";
    if (null == insId) {
      hsql += "insId=null, insName=?, titolo=? where personId=?";
      return this.createQuery(hsql, insName, title, personId).executeUpdate();
    } else {
      hsql += " insId=?,insName=?, titolo=? where personId=?";
      return this.createQuery(hsql, insId, insName, title, personId).executeUpdate();
    }

  }

  public Person queryPersonForRegister(Long psnId) {
    String hql =
        "select new Person(t.name, t.firstName, t.lastName, t.avatars,t.email,t.mobile,t.tel,t.personId,t.regData) from Person t where t.personId = :psnIds";
    return (Person) super.createQuery(hql).setParameter("psnIds", psnId).uniqueResult();
  }

  /**
   * 更新首要单位
   *
   * @param insId
   * @param insName
   * @param personId
   * @return
   */
  public int updateIns(Long insId, String insName, Long personId) {
    String hsql = "update Person set ";
    if (null == insId) {
      hsql += "insId=null, insName=? where personId=?";
      return this.createQuery(hsql, insName, personId).executeUpdate();
    } else {
      hsql += " insId=?,insName=? where personId=?";
      return this.createQuery(hsql, insId, insName, personId).executeUpdate();
    }

  }

  /**
   * 注册时候用，需要查出所有信息，不然后面的操作会导致有些信息会丢失
   *
   * @param psnId
   * @return
   * @throws OauthException
   */
  public Person queryPersonForRegisterByPsnId(Long psnId) {
    String hql = "from Person t where t.personId = :psnIds";
    return (Person) super.createQuery(hql).setParameter("psnIds", psnId).uniqueResult();
  }

  /**
   * 获取人员基本信息--------移动端用 只取一些必要的信息
   *
   * @param psnId
   * @return
   */
  public Person getPersonInfoByPsnIdForFriend(Long psnId) {
    String hql =
        "select new Person(t.name, t.firstName, t.lastName, t.avatars, t.personId,t.insName,t.department,t.position) from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取人员相关信息
   *
   * @param psnId
   * @return
   */
  public Person getPersonBase(Long psnId) {
    String hql =
        "select new Person(t.name, t.firstName, t.lastName, t.avatars,t.email,t.mobile,t.tel,t.personId,t.position,t.insName) from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取人员完整度信息
   *
   * @param psnId
   * @return
   */
  public Integer getPersonComplete(Long psnId) {
    String hql = "select t.complete from Person t where t.personId = :psnId";
    return (Integer) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 人员合并用，请谨慎使用 zk add.
   */
  public void delPersonByPsnId(Long psnId) {
    super.createQuery("delete from Person p where p.personId=?", psnId).executeUpdate();
  }

  /***
   * 获取个人信息用于邮件发送 zk add.
   */
  public Person getPersonForEmail(Long psnId) {
    return super.findUnique(
        "select new Person(email,personId,name,lastName,firstName,emailLanguageVersion) from Person where personId = ? ",
        psnId);
  }

  /**
   * 根据人员邮件获取人员psn_id person表.
   *
   * @param personEmail
   * @return Long @
   */
  public Long findByName(String name) {

    return findUnique("select personId from Person where name = ?", name);
  }

  public boolean isPersonByPsnId(Long psnId) {
    Long pId = super.findUnique("select personId from Person where personId=?", psnId);
    return pId == null ? false : true;
  }

  public Person getPersonByKnow(Long psnId) {
    String hql = "select new Person(personId,nvl(complete,0),nvl(isAdd,0),nvl(isLogin,0)) from Person where personId=?";
    return super.findUnique(hql, psnId);
  }

  /**
   * 获取用户联系方式.
   *
   * @param psnId
   * @return @
   */
  public Person getContact(Long psnId) {

    Person psn = super.findUnique(
        "select new Person(tel, mobile, msnNo, qqNo,email,skype,http) from Person where personId = ? ", psnId);
    if (psn != null) {
      psn.setPersonId(psnId);
    }
    return psn;
  }

  /**
   * @param psnId
   * @return
   */
  public Person getPersonByRecommend(Long psnId) {
    return super.findUnique(
        "select new Person(personId,firstName,lastName,name,titolo,avatars,email,insName,defaultAffiliation) from Person where personId = ? ",
        psnId);
  }

  /**
   * 获取用户头像.
   *
   * @param psnId
   * @return @
   */
  public Person getAvatars(Long psnId) {

    return super.findUnique("select new Person(personId, avatars,sex) from Person where personId = ? ", psnId);
  }

  /**
   * 姓名拆分.
   *
   * @param pubId
   * @param insId
   * @param ruleId @
   */
  public void generatePsnSearchName(long psnId) {
    String sql = "call pkg_psn_profile.generate_name_abbr(" + psnId + ")";
    SQLQuery query = super.getSession().createSQLQuery(sql);
    query.executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Person> findPerson(Long psnId, int pageSize) {
    Query q =
        createQuery("from Person where personId not in(select tempPsnId from FriendTempSys where psnId=?)", psnId);
    q.setFirstResult(0);
    q.setMaxResults(pageSize);
    return q.list();
  }

  /**
   * 获取用户检索用户详情.
   *
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Person> findForUserSearch(List<Long> psnIds) {
    String hql = "from Person t where t.personId in (:personId) ";
    Query q = createQuery(hql).setParameterList("personId", psnIds);
    List<Person> personList = q.list();
    return personList;
  }

  /**
   * 通过psnIds批量获取用户信息（去除隐私人员）.
   *
   * @param psnIds
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Person> queryPersonByPsnIds(List<Long> psnIds) {
    return super.createQuery(
        "from Person t where t.personId in (:personId) and not exists(select 1 from PsnPrivate pp where pp.psnId=t.personId)")
            .setParameterList("personId", psnIds).list();
  }

  /**
   * 通过psnIds批量获取用户信息
   */
  public List<Person> personByPsnIdsList(List<Long> psnIds) {
    if (psnIds != null && psnIds.size() > 1000) {
      psnIds = psnIds.subList(0, 1000);
    }
    return super.createQuery(
        "select new Person(name, personId,ename, avatars, insName) from Person t where t.personId in (:personId)")
            .setParameterList("personId", psnIds).list();
  }

  /**
   * 排除隐私用户.
   *
   * @param psnIds
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Long> excludePrivacyPsn(List<Long> psnIds) {
    return super.createQuery(
        "select t.personId from Person t where t.personId in (:personId) and not exists(select 1 from PsnPrivate pp where pp.psnId=t.personId)")
            .setParameterList("personId", psnIds).list();
  }

  /**
   * 批量获取用户列表.
   *
   * @param minPsnId最小的PSNID值 .
   * @param size一次批量获取的数据量 .
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Person> findPersonBatch(Long minPsnId, Integer size) {
    String hql = "select new  Person(t.personId, t.name, t.ename, t.isLogin) from Person t where t.personId > ? ";
    return createQuery(hql, minPsnId).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Person> findPersonByNameAndEmail(String psnName, String email) {
    String hql = "select p from Person p where nvl(p.name,p.ename)=? and p.email=?";
    return super.createQuery(hql, new Object[] {psnName, email}).list();
  }

  @SuppressWarnings("unchecked")
  public List<Person> findPersonByPsnName(String psnName) {
    String hql = "select p from Person p where nvl(p.name,p.ename)=?";
    return super.createQuery(hql, new Object[] {psnName}).list();
  }

  /**
   * 获得firstName和lastName为空的person
   *
   * @return
   */
  public List<Person> findAllPerson(Long psnId) {
    String hql =
        "select p from Person p where ((trim(p.firstName) = '' or trim(p.firstName) is null) or (trim(p.lastName) = '' or trim(p.lastName) is null)) and p.personId>"
            + psnId + " order by p.personId ";
    Query query = super.createQuery(hql);

    return query.list();
  }

  public Long findAllPersonSize() {
    String hql =
        "select count(p.personId) from Person p where ((trim(p.firstName) = '' or trim(p.firstName) is null) or (trim(p.lastName) = '' or trim(p.lastName) is null)) ";
    return (Long) super.createQuery(hql).uniqueResult();
  }

  /**
   * 获取所有的复姓列表.
   *
   * @return
   */
  public List findAllConstSurname() {
    String sql = "select * from const_surname";
    return super.queryForList(sql);
  }

  public void delPrimaryInsId(Long psnId) {
    super.createQuery("update Person set insId=null where personId=?", psnId).executeUpdate();
  }

  /**
   * 批量获取人员.
   *
   * @param firstPsnId
   * @param batchSize
   * @return
   */
  public List<Person> getPersonBatch(Long firstPsnId, int batchSize) {

    String hql =
        "from Person p where  ((trim(p.firstName) = '' or trim(p.firstName) is null) or (trim(p.lastName) = '' or trim(p.lastName) is null)) and  p.personId > ? order by p.personId ";

    return super.createQuery(hql, new Object[] {firstPsnId}).setMaxResults(batchSize).list();

  }

  /**
   * 保存个人邮件语言版本设置
   *
   * @param psnId
   * @param emailLanVersion
   * @return
   */
  public boolean savePsnEmailLanguageVersion(Long psnId, String emailLanVersion) {
    boolean flag = true;
    try {
      String sql = "update Person t set t.emailLanguageVersion=? where t.personId=?";
      super.createQuery(sql, emailLanVersion, psnId).executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      flag = false;
      logger.debug("保存个人邮件语言版本设置时报错", ex.getMessage());
    }
    return flag;
  }

  public void getPersonProfileListForBpo(Map searchMap, Page<Person> page) {

    StringBuilder sb = new StringBuilder();
    List params = new ArrayList();
    sb.append("From Person p where p.personId in( select distinct(t.psnId) from  DynPersonResume t  where 1=1 ");

    if (StringUtils.isNotEmpty(MapUtils.getString(searchMap, "searchKey"))) {

      // TODO:OYH

    }

    if (MapUtils.getObject(searchMap, "fromDate") != null && MapUtils.getObject(searchMap, "toDate") != null) {

      sb.append(" and t.modifyDate between ?  and ? ");
      params.add(MapUtils.getObject(searchMap, "fromDate"));
      params.add(MapUtils.getObject(searchMap, "toDate"));

    }

    sb.append(")");

    Long totalCount = super.findUnique("select count(p.personId) " + sb.toString(), params.toArray());
    page.setTotalCount(totalCount);
    Query query = super.createQuery(sb.toString(), params.toArray());
    query.setFirstResult(page.getFirst() - 1);
    query.setMaxResults(page.getPageSize());
    List<Person> lst = query.list();
    page.setResult(lst);

  }

  /**
   * @param lastPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPersonByPsnIds(Long lastPsnId, int batchSize) {
    String hql = "select personId from Person where personId>? order by personId asc";
    return super.createQuery(hql, lastPsnId).setMaxResults(batchSize).list();
  }

  public void syncUserLoginToPerson(List<Long> psnIds) {
    String hql = "update Person set isLogin=1 where personId in(:psnIds)";
    super.createQuery(hql).setParameterList("psnIds", psnIds).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public Page<NodePerson> findPersonWork(String psnName, String firstName, String lastName, List<Long> psnIds,
      List<Long> insIds, Page<NodePerson> page) {
    psnName = "%" + psnName + "%";
    firstName = StringUtils.isNotBlank(firstName) ? firstName.trim().replaceAll(" ", "").toLowerCase() : "%%";
    lastName = StringUtils.isNotBlank(lastName) ? lastName.trim().replaceAll(" ", "").toLowerCase() : "%%";
    String nameXX = psnName.replace(" ", "").toLowerCase();
    StringBuffer hql = new StringBuffer(
        "select t.psnId,t.psnName,t.firstName,t.lastName,t.psnEmail,t.psnTitle from NodePerson t,PsnWork t2 ");
    hql.append(
        " where t.psnId=t2.psnId and (t.psnName like:psnName1 or lower(replace(t.psnEname,' ','')) like:psnName2 or lower(replace(t.lastName,' ','')) || lower(replace(t.firstName,' ','')) like:psnName3) ");
    hql.append(
        " and lower(replace(t.firstName,' ','')) like:firstName1 and lower(replace(t.lastName,' ','')) like:lastName1 and not exists(select 1 from PsnPrivate pp where pp.psnId=t.psnId) ");
    hql.append(" and t.psnId not in(:psnIds)");
    hql.append(" and t2.insId in(:insIds)");
    Long count = 0L;
    String fromHql = hql.toString();
    fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
    fromHql = StringUtils.substringBefore(fromHql, "order by");
    String countHql = "select count(t.psnId) " + fromHql;
    Query q1 = createQuery(countHql);
    q1.setParameter("psnName1", psnName);
    q1.setParameter("psnName2", nameXX);
    q1.setParameter("psnName3", nameXX);
    q1.setParameter("firstName1", firstName);
    q1.setParameter("lastName1", lastName);
    q1.setParameterList("psnIds", psnIds.size() > 99 ? psnIds.subList(0, 98) : psnIds);
    q1.setParameterList("insIds", insIds.size() > 99 ? insIds.subList(0, 98) : insIds);
    try {
      count = (Long) q1.uniqueResult();
    } catch (Exception e) {
      throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
    }
    if (count != null && count > 0) {
      Query q2 = createQuery(hql.toString());
      q2.setParameter("psnName1", psnName);
      q2.setParameter("psnName2", nameXX);
      q2.setParameter("psnName3", nameXX);
      q2.setParameter("firstName1", firstName);
      q2.setParameter("lastName1", lastName);
      q2.setParameterList("psnIds", psnIds.size() > 99 ? psnIds.subList(0, 98) : psnIds);
      q2.setParameterList("insIds", insIds.size() > 99 ? insIds.subList(0, 98) : insIds);
      page.setTotalCount(count);
      q2.setFirstResult(page.getFirst() - 1);
      q2.setMaxResults(page.getPageSize());
      List<Object[]> objList = q2.list();
      if (CollectionUtils.isNotEmpty(objList)) {
        List<NodePerson> psnList = new ArrayList<NodePerson>();
        for (Object[] obj : objList) {
          int i = 0;
          NodePerson psn = new NodePerson();
          psn.setPsnId(Long.valueOf(String.valueOf(obj[i])));
          if (obj[++i] != null) {
            psn.setPsnName(String.valueOf(obj[i]));
          }
          if (obj[++i] != null) {
            psn.setFirstName(String.valueOf(obj[i]));
          }
          if (obj[++i] != null) {
            psn.setLastName(String.valueOf(obj[i]));
          }
          psn.setPsnEmail(String.valueOf(obj[++i]));
          if (obj[++i] != null) {
            psn.setPsnTitle(String.valueOf(obj[i]));
          }
          psnList.add(psn);
        }
        page.setResult(psnList);
      }
    }
    return page;
  }

  @SuppressWarnings("unchecked")
  public Page<NodePerson> findPersonEdu(String psnName, String firstName, String lastName, List<Long> psnIds,
      List<Long> insIds, Page<NodePerson> page) {
    psnName = "%" + psnName + "%";
    firstName = StringUtils.isNotBlank(firstName) ? firstName.trim().replaceAll(" ", "").toLowerCase() : "%%";
    lastName = StringUtils.isNotBlank(lastName) ? lastName.trim().replaceAll(" ", "").toLowerCase() : "%%";
    String nameXX = psnName.replace(" ", "").toLowerCase();
    StringBuffer hql = new StringBuffer(
        "select t.psnId,t.psnName,t.firstName,t.lastName,t.psnEmail,t.psnTitle from NodePerson t,PsnEdu t2 ");
    hql.append(
        " where t.psnId=t2.psnId and (t.psnName like:psnName1 or lower(replace(t.psnEname,' ','')) like:psnName2 or lower(replace(t.lastName,' ','')) || lower(replace(t.firstName,' ','')) like:psnName3) ");
    hql.append(
        " and lower(replace(t.firstName,' ','')) like:firstName1 and lower(replace(t.lastName,' ','')) like:lastName1 and not exists(select 1 from PsnPrivate pp where pp.psnId=t.psnId) ");
    hql.append(" and t.psnId not in(:psnIds)");
    hql.append(" and t2.insId in(:insIds)");
    Long count = 0L;
    String fromHql = hql.toString();
    fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
    fromHql = StringUtils.substringBefore(fromHql, "order by");
    String countHql = "select count(t.psnId) " + fromHql;
    Query q1 = createQuery(countHql);
    q1.setParameter("psnName1", psnName);
    q1.setParameter("psnName2", nameXX);
    q1.setParameter("psnName3", nameXX);
    q1.setParameter("firstName1", firstName);
    q1.setParameter("lastName1", lastName);
    q1.setParameterList("psnIds", psnIds.size() > 99 ? psnIds.subList(0, 98) : psnIds);
    q1.setParameterList("insIds", insIds.size() > 99 ? insIds.subList(0, 98) : insIds);
    try {
      count = (Long) q1.uniqueResult();
    } catch (Exception e) {
      throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
    }
    if (count != null && count > 0) {
      Query q2 = createQuery(hql.toString());
      q2.setParameter("psnName1", psnName);
      q2.setParameter("psnName2", nameXX);
      q2.setParameter("psnName3", nameXX);
      q2.setParameter("firstName1", firstName);
      q2.setParameter("lastName1", lastName);
      q2.setParameterList("psnIds", psnIds.size() > 99 ? psnIds.subList(0, 98) : psnIds);
      q2.setParameterList("insIds", insIds.size() > 99 ? insIds.subList(0, 98) : insIds);
      page.setTotalCount(count);
      q2.setFirstResult(page.getFirst() - 1);
      q2.setMaxResults(page.getPageSize());
      List<Object[]> objList = q2.list();
      if (CollectionUtils.isNotEmpty(objList)) {
        List<NodePerson> psnList = new ArrayList<NodePerson>();
        for (Object[] obj : objList) {
          int i = 0;
          NodePerson psn = new NodePerson();
          psn.setPsnId(Long.valueOf(String.valueOf(obj[i])));
          if (obj[++i] != null) {
            psn.setPsnName(String.valueOf(obj[i]));
          }
          if (obj[++i] != null) {
            psn.setFirstName(String.valueOf(obj[i]));
          }
          if (obj[++i] != null) {
            psn.setLastName(String.valueOf(obj[i]));
          }
          psn.setPsnEmail(String.valueOf(obj[++i]));
          if (obj[++i] != null) {
            psn.setPsnTitle(String.valueOf(obj[i]));
          }
          psnList.add(psn);
        }
        page.setResult(psnList);
      }
    }
    return page;
  }

  /**
   * 获取人员首要工作单位列表.
   *
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnInsIds(List<Long> psnIds) {

    String hql = "select distinct t.insId from Person t where t.personId in (:psnIds) and t.insId is not null";

    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 获取人员首要工作单位列表.
   *
   * @param psnIds
   * @return
   */
  public Long getPsnInsId(Long psnId) {
    String hql = "select t.insId from Person t where t.personId =:psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnInsIdsByOrder(List<Long> psnIds) {
    String hql =
        "select p.insId  from Person p where p.personId in (:psnIds) and p.insId is not null group by p.insId order by count(personId) desc";

    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  public List<Person> findPersonByEmailAndName(String email, String zhName) {
    String hql = "from Person t where t.email = ? and t.name = ?";
    return super.createQuery(hql, email, zhName).list();
  }

  /**
   * 后台任务，根据当前psnId,批量获取大于当前psnId值的psnId<增加过滤是否登录属性以便减少需计算的工作量>_MJG_SCM-5978.
   *
   * @author liangguokeng
   * @param lastPsnId
   * @param batchSize @
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdBatch(Long lastPsnId, int batchSize) {

    String hql = "select p.personId from Person p where p.isLogin=1 and p.personId > ? order by p.personId asc ";
    return super.createQuery(hql, new Object[] {lastPsnId}).setMaxResults(batchSize).list();

  }

  /**
   * 后台任务，根据当前psnId,批量获取大于当前psnId值的人员
   *
   * @author liangguokeng
   * @param lastPsnId
   * @param batchSize @
   */
  @SuppressWarnings("unchecked")
  public List<Person> getPersonBatchBylastPsnId(Long lastPsnId, int batchSize) {
    String hql = "from Person p where p.personId > ? order by p.personId asc ";
    return super.createQuery(hql, new Object[] {lastPsnId}).setMaxResults(batchSize).list();
  }

  /**
   * 批量查询同行专家信息.
   *
   * @param psnIdList
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Person> queryExpertBatch(List<Long> psnIdList) {
    return super.createQuery(
        "select new Person(p.personId, p.firstName, p.lastName, p.name, p.titolo, p.avatars, p.email, p.insName)"
            + " from Person p, ConstInsType it, ConstPosition cp"
            + " where ((it.insType='A' and cp.posType in('A', 'B')) or (it.insType='B' and cp.posType='A')) and p.insId=it.insId and p.posId=cp.id"
            + " and p.personId in(:psnIdList) and not exists(select 1 from PsnPrivate pp where pp.psnId=p.personId)")
                .setParameterList("psnIdList", psnIdList).list();
  }

  /**
   * 批量查询合作者信息.
   *
   * @param psnIdList
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Person> queryCooperatorBatch(Long insId, List<Long> psnIdList) {
    String hql =
        "select new Person(p.personId, p.firstName, p.lastName, p.name, p.titolo, p.avatars, p.email, p.insName)";
    if (insId == null || insId == 0) {
      return super.createQuery(hql
          + " from Person p where p.insId is not null and p.personId in(:personId) and not exists(select 1 from PsnPrivate pp where pp.psnId=p.personId)")
              .setParameterList("personId", psnIdList).list();
    } else {
      return super.createQuery(hql + " from Person p, ConstInsType it"
          + " where it.insType in (select cit.insType from ConstInsType cit where cit.insId=:insId) and p.insId=it.insId"
          + " and p.personId in(:personId) and not exists(select 1 from PsnPrivate pp where pp.psnId=p.personId)")
              .setParameter("insId", insId).setParameterList("personId", psnIdList).list();
    }
  }

  /**
   * 批量获取合作者信息.
   *
   * @param psnIdList
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Person> queryCooperatorBatch(List<Long> psnIdList) {
    String hql =
        "select new Person(p.personId, p.firstName, p.lastName, p.name, p.titolo, p.avatars, p.email, p.insName)"
            + " from Person p where p.personId in(:personId) and not exists(select 1 from PsnPrivate pp where pp.psnId=p.personId)";

    return super.createQuery(hql).setParameterList("personId", psnIdList).list();

  }

  /**
   * 查询专家用户id.
   *
   * @param psnIdList
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryExpertPsnId(List<Long> psnIdList) {
    return super.createQuery("select p.personId from Person p, ConstInsType it, ConstPosition cp"
        + " where ((it.insType='A' and cp.posType in('A', 'B')) or (it.insType='B' and cp.posType='A')) and p.insId=it.insId and p.posId=cp.id"
        + " and p.personId in(:psnIdList) and not exists(select 1 from PsnPrivate pp where pp.psnId=p.personId)")
            .setParameterList("psnIdList", psnIdList).list();
  }

  /**
   * 查询合作者用户id.
   *
   * @param psnIdList
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryCooperatorPsnId(Long insId, List<Long> psnIdList) {
    if (insId == null || insId == 0) {
      return super.createQuery(
          "select p.personId from Person p where p.insId is not null and p.personId in(:personId) and not exists(select 1 from PsnPrivate pp where pp.psnId=p.personId)")
              .setParameterList("personId", psnIdList).list();
    } else {
      return super.createQuery("select p.personId from Person p, ConstInsType it"
          + " where it.insType in (select cit.insType from ConstInsType cit where cit.insId=:insId) and p.insId=it.insId"
          + " and p.personId in(:personId) and not exists(select 1 from PsnPrivate pp where pp.psnId=p.personId)")
              .setParameter("insId", insId).setParameterList("personId", psnIdList).list();
    }
  }

  /**
   * 获取所有系统用户的ID(需改为分页处理).
   *
   * @return
   */
  public List<Long> getAllPersonId() {
    String hql = "select personId from Person t order by complete desc ";
    return super.createQuery(hql).list();
  }

  /**
   * 获取用户所有名字.
   *
   * @param psnId
   * @return
   */
  public Person getPsnAllName(Long psnId) {
    String hql =
        "select new Person( firstName,lastName, name, otherName,ename,personId) from Person t where personId = ? ";
    return super.findUnique(hql, psnId);
  }

  /**
   * 获取人员经验信息数据.
   *
   * @param psnId
   * @return
   */
  public Person getPersonExperience(Long psnId) {
    String hql =
        "select new Person( personId,  degree,  titolo,  position,  posId,  posGrades, defaultAffiliation,  degreeName) from Person t where personId = ? ";
    return super.findUnique(hql, psnId);
  }

  /**
   * 获取人员辅助信息数据.
   *
   * @param psnId
   * @return
   */
  public Person getPersonAdditInfo(Long psnId) {
    String hql =
        "select new Person(personId, regionId, birthday, sex,avatars,complete,regData,isAdd,isUpdated,isLogin) from Person t where personId = ? ";
    return super.findUnique(hql, psnId);
  }

  /**
   * 获取人员职称.
   *
   * @param psnId
   * @return
   */
  public Person getPsnPostion(Long psnId) {

    String hql = "select new Person(personId, position, posId) from Person t where personId = ?  ";
    return super.findUnique(hql, psnId);
  }

  /**
   * 获取人员学位.
   *
   * @param psnId
   * @return
   */
  public Person getPsnDegree(Long psnId) {

    String hql = "select new Person(personId, degree, insId, degreeName) from Person t where personId = ?  ";
    return super.findUnique(hql, psnId);
  }

  @SuppressWarnings("unchecked")
  public List<Person> queryPersonList(String psnName, String insName) {
    return super.createQuery(
        "select new Person(t.personId, t.name, t.firstName, t.lastName, t.avatars, t.insId, t.insName, t.email) from Person t where nvl(t.name, t.ename)=? and t.insName=? order by t.personId desc",
        psnName, insName).list();
  }

  @SuppressWarnings("unchecked")
  public List<Person> queryPersonListByEmail(String email) {
    return super.createQuery(
        "select new Person(t.personId, t.name, t.firstName, t.lastName, t.avatars, t.insId, t.insName, t.email) from Person t where t.email=? order by t.personId desc",
        email).list();
  }

  /**
   * 获取用户的受邀工作单位信息_MJG_SCM-4552.
   *
   * @param psnId
   * @return
   */
  public Person getPsnInsData(Long psnId) {
    String hql = "select new Person(personId, insId, insName) from Person t where t.personId=? ";
    return super.findUnique(hql, psnId);
  }

  public Person queryPsnNameAndAvatars(Long psnId) {
    return (Person) super.createQuery(
        "select new Person(t.name, t.firstName, t.lastName, t.avatars, t.personId) from Person t where t.personId=:psnId")
            .setParameter("psnId", psnId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Person> queryPersonForPsnHtml(List<Long> psnIds) {
    String hql =
        "select new Person(t.name, t.firstName, t.lastName, t.avatars,t.email,t.mobile,t.tel,t.personId) from Person t where t.personId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 获取单位匹配得人员记录.
   *
   * @param insIdList
   * @param insNameList
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getMatchedPsnByIns(List<Long> insIdList, List<String> insNameList, int size) {
    String hql =
        "select personId from Person t where t.insId in (:insId) or t.insName in (:insName) order by t.complete desc";
    return super.createQuery(hql).setParameterList("insId", insIdList).setParameterList("insName", insNameList)
        .setMaxResults(size).list();
  }

  /**
   * 根据名称和单位或邮件查找人员.
   *
   * @param psnName
   * @param insName
   * @param email
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<Person> queryPersonList(String psnName, String insName, String email) {
    String hql =
        "select new Person(t.personId, t.name, t.firstName, t.lastName, t.avatars, t.insId, t.insName, t.email) from Person t where 1=1 ";
    List<String> params = new ArrayList<String>();
    if (StringUtils.isNotBlank(psnName)) {
      hql += " and t.name = ?";
      params.add(psnName);
    }
    if (StringUtils.isNotBlank(insName)) {
      hql += " and t.insName = ?";
      params.add(insName);
    }
    if (StringUtils.isNotBlank(email)) {
      hql += " and t.email = ?";
      params.add(email);
    }
    return super.createQuery(hql + " order by t.personId desc", params.toArray()).list();
  }

  @SuppressWarnings("unchecked")
  public List<Person> queryPersonList(List<Long> psnIdList) {
    return super.createQuery(
        "select new Person(t.name, t.firstName, t.lastName, t.avatars, t.personId) from Person t where t.personId in(:psnIdList) order by t.personId desc")
            .setParameterList("psnIdList", psnIdList).list();
  }

  public Person queryPersonByPsnId(Long psnId) {
    return (Person) super.createQuery(
        "select new Person(t.personId, t.name, t.firstName, t.lastName, t.avatars, t.insId, t.insName, t.email) from Person t where t.personId=?",
        psnId).uniqueResult();
  }

  /**
   * 获取人的联络方式
   *
   * @author zk
   * @param psnIdList
   * @return @
   */
  @SuppressWarnings({"unchecked"})
  public List<Person> queryPersonContact(List<Long> psnIdList) {

    String hql =
        "select new  Person(t.personId ,t.email ,t.mobile ,t.tel)  from Person t where t.personId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIdList).list();
  }

  /**
   * 获取用户设置.
   *
   * @param psnId
   * @return
   */
  public Person getPersonSetting(Long psnId) {

    String hql =
        "select new  Person(personId, dynEmail,emailLanguageVersion,localLanguage) from Person t where t.personId = ? ";
    return super.findUnique(hql, psnId);
  }

  /**
   * 获取人员信息-----人员列表用
   *
   * @param psnId
   * @return
   */
  public Person getUnifiedPsnInfoByPsnId(Long psnId) {
    String hql =
        "select new Person(personId, firstName, lastName,name, position, sex, avatars, insId, insName, titolo, regionId, department,ename, email,mobile ,tel) from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找人员联系信息-------邮箱、电话
   *
   * @return
   */
  public Person findPersonContactInfoById(Long psnId) {
    String hql = "select new  Person(t.personId ,t.email ,t.mobile ,t.tel) from Person t where t.personId = :psnId ";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 更新人员联系信息---------邮件、电话
   *
   * @param psnId
   * @param email
   * @param tel
   */
  public void updatePsnContactInfo(Long psnId, String email, String tel) {
    String hql = "update Person t set t.email = :email, t.tel = :tel where t.personId = :psnId";
    super.createQuery(hql).setParameter("email", email).setParameter("tel", tel).setParameter("psnId", psnId)
        .executeUpdate();
  }

  /**
   * 查找人员信息保存工作经历时用到
   *
   * @param psnId
   * @return
   */
  public Person findPersonInfoForWrok(Long psnId) {
    String hql =
        "select new Person(personId, name, ename, regionId, titolo, avatars) from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取人员列表展示所需信息
   *
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Person> getPsnListInfoByPsnIds(List<Long> psnIds) {
    String hql =
        "select new Person(personId, firstName, lastName,name, position, sex, avatars, insId, insName, titolo, regionId, department, ename) from Person t where t.personId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 获取机构地区所需要的信息
   */
  public List<Map<String, Long>> getInsListInfoByPsnIds(List<Long> psnIds) {
    String hql =
        "select t.insId as insId ,count(insId) as count from Person t where t.personId in (:psnIds) and t.insId is not null group by t.insId order by count(t.insId) desc";

    return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameterList("psnIds", psnIds).setFirstResult(0).setMaxResults(10).list();
  }

  /**
   * 获取机构的总数
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getInsCountByPsnIds(List<Long> psnIds, List<Long> regionId,
      List<Integer> scienceAreaId, List<Long> insId) {
    /*
     * psnIds.clear(); psnIds.add(1000000727771L); psnIds.add(1000000727634L);
     * psnIds.add(1000000727252L);
     */
    String hql =
        "select t.insId as insId ,count(insId) as count  from Person t where t.personId in (:psnIds) and t.insId in(:insId) and t.insId is not null ";
    if (CollectionUtils.isNotEmpty(regionId) && CollectionUtils.isNotEmpty(scienceAreaId)) {
      hql = hql
          + " and t.regionId in(:regionId) and exists(select 1 from PsnScienceArea f where f.psnId = t.personId and f.status=1 and f.psnId in (:psnIds) and f.scienceAreaId in(:scienceAreaId))  group by t.insId order by count(t.insId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnIds", psnIds).setParameterList("insId", insId).setParameterList("regionId", regionId)
          .setParameterList("scienceAreaId", scienceAreaId).list();
    } else if (CollectionUtils.isNotEmpty(regionId) && CollectionUtils.isEmpty(scienceAreaId)) {
      hql = hql + " and t.regionId in(:regionId) group by t.insId order by count(t.insId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnIds", psnIds).setParameterList("insId", insId).setParameterList("regionId", regionId)
          .list();
    } else if (CollectionUtils.isEmpty(regionId) && CollectionUtils.isNotEmpty(scienceAreaId)) {
      hql = hql
          + " and exists(select 1 from PsnScienceArea f where f.psnId = t.personId   and f.status=1 and f.psnId in (:psnIds) and f.scienceAreaId in(:scienceAreaId)) group by t.insId order by count(t.insId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnIds", psnIds).setParameterList("insId", insId)
          .setParameterList("scienceAreaId", scienceAreaId).list();
    } else {
      hql = hql + "  group by t.insId order by count(t.insId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnIds", psnIds).setParameterList("insId", insId).list();
    }

  }

  /**
   * 批量查找人员头像
   *
   * @param psnIds
   * @return
   */
  public List<Person> getPsnAvatarsByPsnIds(List<Long> psnIds) {
    String hql = "select new Person(personId, avatars,sex) from Person t where t.personId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 获取地区所需要的信息
   *
   * @param psnIds
   * @return
   */
  public List<Map<String, Object>> getRegListInfoByPsnIds(List<Long> psnIds) {
    String hql =
        "select t.regionId as regionId ,count(regionId) as count from Person t where t.personId in (:psnIds) and t.regionId is not null "
            + "and exists(select 1 from ConstRegion a where a.id=t.regionId) group by t.regionId order by count(t.regionId) desc";
    return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameterList("psnIds", psnIds).setFirstResult(0).setMaxResults(5).list();
  }

  /**
   * 构建数据回显的地区
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getRegIdCount(List<Long> psnIds, List<Long> insId, List<Integer> scienceAreaId,
      List<Long> regionId) {

    String hql =
        "select t.regionId as regionId ,count(regionId) as count from Person t where t.personId in (:psnIds) and t.regionId in(:regionId) and t.regionId is not null ";
    if (CollectionUtils.isNotEmpty(insId) && CollectionUtils.isNotEmpty(scienceAreaId)) {
      hql = hql
          + " and t.insId in(:insId) and exists(select 1 from PsnScienceArea f where f.psnId = t.personId and f.status=1 and f.psnId in (:psnIds) and f.scienceAreaId in(:scienceAreaId)) group by t.regionId order by count(t.regionId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnIds", psnIds).setParameterList("scienceAreaId", scienceAreaId)
          .setParameterList("regionId", regionId).setParameterList("insId", insId).list();
    } else if (CollectionUtils.isNotEmpty(insId) && CollectionUtils.isEmpty(scienceAreaId)) {
      hql = hql + " and t.insId in(:insId)  group by t.regionId order by count(t.regionId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnIds", psnIds).setParameterList("regionId", regionId).setParameterList("insId", insId)
          .list();
    } else if (CollectionUtils.isEmpty(insId) && CollectionUtils.isNotEmpty(scienceAreaId)) {
      hql = hql
          + " and exists(select 1 from PsnScienceArea f where f.psnId = t.personId and f.status=1 and f.psnId in (:psnIds) and f.scienceAreaId  in(:scienceAreaId)) group by t.regionId order by count(t.regionId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnIds", psnIds).setParameterList("regionId", regionId)
          .setParameterList("scienceAreaId", scienceAreaId).list();
    } else {
      hql = hql + " group by t.regionId order by count(t.regionId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnIds", psnIds).setParameterList("regionId", regionId).list();
    }
  }

  /**
   * 构建科技领域回显数据
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getScienceAreaIdCount(List<Long> psnIds, List<Long> insId, List<Long> regionId,
      List<Integer> scienceAreaId) {
    String hql =
        "select t.scienceAreaId as scienceAreaId,count(scienceAreaId) as count from PsnScienceArea t where t.psnId in(:psnId) and t.scienceAreaId in(:scienceAreaId) and t.status=1";
    if (CollectionUtils.isNotEmpty(insId) && CollectionUtils.isNotEmpty(regionId)) {
      hql = hql
          + " and exists(select 1 from Person f where f.personId=t.psnId and f.personId in(:psnId) and f.insId in(:insId) and f.regionId in(:regionId)) group by t.scienceAreaId order by count(t.scienceAreaId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnId", psnIds).setParameterList("insId", insId)
          .setParameterList("scienceAreaId", scienceAreaId).setParameterList("regionId", regionId).list();
    } else if (CollectionUtils.isEmpty(insId) && CollectionUtils.isNotEmpty(regionId)) {
      hql = hql
          + " and exists(select 1 from Person f where f.personId=t.psnId and f.personId in(:psnId) and f.regionId in(:regionId)) group by t.scienceAreaId order by count(t.scienceAreaId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnId", psnIds).setParameterList("scienceAreaId", scienceAreaId)
          .setParameterList("regionId", regionId).list();

    } else if (CollectionUtils.isEmpty(regionId) && CollectionUtils.isNotEmpty(insId)) {
      hql = hql
          + " and exists(select 1 from Person f where f.personId=t.psnId and f.personId in(:psnId) and f.insId in(:insId)) group by t.scienceAreaId order by count(t.scienceAreaId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnId", psnIds).setParameterList("scienceAreaId", scienceAreaId)
          .setParameterList("insId", insId).list();

    } else {
      hql = hql
          + " and exists (select 1 from Person f where f.personId=t.psnId) group by t.scienceAreaId order by count(t.scienceAreaId) desc";
      return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("psnId", psnIds).setParameterList("scienceAreaId", scienceAreaId).list();
    }
  }

  /**
   * 查询人员信息用于构建互相关注对象
   *
   * @param psnId
   * @return
   */
  public Person findPsnInfoForAttPerson(Long psnId) {
    String hql =
        "select new Person(personId, firstName, lastName, name, titolo, avatars, email, insName) from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 找到人员所有姓名-------新加了中文的姓和名字段
   *
   * @param psnId
   * @return
   */
  public Person findPsnAllName(Long psnId) {
    String hql =
        "select new Person(firstName, lastName, name, otherName, ename, personId, zhFirstName, zhLastName) from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找人员所在地区ID
   *
   * @param psnId
   * @return
   */
  public Long findPsnRegionId(Long psnId) {
    String hql = "select t.regionId from Person t where t.personId = :psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找人员职称和学位信息
   *
   * @param psnId
   * @return
   */
  public Person findPsnDegreeAndPosition(Long psnId) {
    String hql =
        "select new Person(personId, position, posId, posGrades, degreeName) from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public Person getPsnInfo(Long psnId) {
    String hql =
        "select new Person(personId, firstName, lastName, name, position, sex, avatars, insId, insName, titolo, regionId, department, ename, email, mobile, tel) from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 根据psnId查出对应人员信息，然后根据人员姓名，单位名称匹配，完全匹配则返回人员信息，不完全匹配或者不匹配返回null
   * 
   * @author ChuanjieHou
   * @date 2017年10月28日
   * @param psnId
   * @param psnName
   * @param firstName
   * @param lastName
   * @param insName
   * @return
   * @throws Exception
   */
  public Person matchPerson(Long psnId, String psnName, String firstName, String lastName, String insName)
      throws Exception {
    if (psnId != null) {
      String hql = "select t from Person t where t.personId=:psnId ";
      Person person =
          (Person) super.createQuery(hql + " order by t.personId desc").setParameter("psnId", psnId).uniqueResult();
      // 旧逻辑
      /*
       * if (person == null) { return null; } else if (StringUtils.isNotBlank(person.getName())) { return
       * person.getName().equalsIgnoreCase(psnName) ? person : null; } else if
       * (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) { if
       * (firstName.equalsIgnoreCase(person.getFirstName()) &&
       * lastName.equalsIgnoreCase(person.getLastName())) return person; }
       */
      // SCM-10735 新修改的逻辑，要匹配人员单位
      if (person != null) {
        // 匹配人员单位名称
        boolean insNameMatchable =
            person.getInsName() != null ? StringUtils.equalsIgnoreCase(person.getInsName(), insName) : false;
        if (!insNameMatchable) {
          return null;
        } // 匹配人员姓名
        boolean psnNameMatchable = false;
        if (person.getName() != null) {
          psnNameMatchable = StringUtils.equalsIgnoreCase(person.getName(), psnName);
        } else if (person.getFirstName() != null && person.getLastName() != null) {
          psnNameMatchable = StringUtils.equalsIgnoreCase(person.getFirstName(), firstName)
              && StringUtils.equalsIgnoreCase(person.getLastName(), lastName);
        }
        if (psnNameMatchable && insNameMatchable) {
          return person;
        }
      }
    }
    return null;
  }

  /**
   *
   * @param psnId
   * @param psnName
   * @return
   * @throws Exception
   */
  public Person matchPersonByName(Long psnId, String psnName) throws Exception {
    if (psnId != null) {
      String hql =
          "select new Person(t.personId, t.name, t.firstName, t.lastName, t.avatars, t.insId, t.insName, t.email) from Person t where t.personId=:psnId ";
      Person person =
          (Person) super.createQuery(hql + " order by t.personId desc").setParameter("psnId", psnId).uniqueResult();

      if (person == null) {
        return null;
      } else if (StringUtils.isNotBlank(person.getName())) {
        return person.getName().equalsIgnoreCase(psnName) ? person : null;
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        if ((person.getFirstName() + " " + person.getLastName()).equalsIgnoreCase(psnName)) {
          return person;
        }
      } else if (StringUtils.isNotBlank(person.getEname()) && person.getEname().equalsIgnoreCase(psnName)) {
        return person;
      }
    }
    return null;
  }

  /**
   * || p.ename=:username || p.firstName+' '+p.lastName =:username )
   *
   * @param email
   * @param username
   * @return
   */
  public List<Long> findPsnIdByEmailAndUsername(String email, String username) {
    String hql =
        " select   p.personId  from Person p    where p.email =:email  and (  p.name=:username    or  p.ename=:username   or   p.firstName||' '||p.lastName =:username )  ";
    List list = this.createQuery(hql).setParameter("email", email).setParameter("username", username).list();
    return list;
  }

  /**
   *
   * @param email
   * @return
   */
  public List<Person> findPersonByEmail(String email) {
    String hql = " from Person p    where p.email =:email ";
    List list = this.createQuery(hql).setParameter("email", email).list();
    return list;
  }

  /**
   * 查询新人员的psn_id.
   *
   * @return
   * @throws ServiceException
   */
  public Long findNewPsnId() {
    String sql = "select seq_person.nextval from dual";
    return super.queryForLong(sql);
  }

  @SuppressWarnings("unchecked")
  public List<String> getSendNewYearEnMail(Long minPsnId, Long maxPsnId) {
    String hql =
        "select distinct(email) from Person t where exists(select 1 from PsnStatistics f where t.personId=f.psnId and (f.pubSum != 0 or f.prjSum != 0 or f.patentSum !=0)) and t.personId>=:minPersonId and t.personId<=:maxPersonId";
    return super.createQuery(hql).setParameter("minPersonId", minPsnId).setParameter("maxPersonId", maxPsnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Person> getSendNewYearMailPsnIds(Integer size, Long lastPsnId) {
    String hql =
        "select new Person(personId,email) from Person t where exists(select 1 from PsnStatistics f where t.personId=f.psnId and (f.pubSum != 0 or f.prjSum != 0 or f.patentSum !=0)) and t.personId>:personId order by t.personId asc";
    return super.createQuery(hql).setParameter("personId", lastPsnId).setMaxResults(size).list();
  }

  /**
   * 接口检索人员
   * 
   * @param form
   * @return
   */
  @SuppressWarnings("unchecked")
  public void searchPerson(SearchPersonForm form) {
    String countHql = " select count(1)  ";
    String hql = "from Person t where 1 = ? ";
    List<Object> params = new ArrayList<Object>();
    params.add(1);
    if (StringUtils.isBlank(form.getSearchKey())) {
      if (StringUtils.isNotBlank(form.getName())) {
        hql +=
            "and (instr( lower(t.name),?)>0 or instr(lower(t.firstName||' '||t.lastName),?)>0 or instr(lower(t.ename),?)>0) ";
        params.add(form.getName());
        params.add(form.getName());
        params.add(form.getName());
      }
      if (StringUtils.isNotBlank(form.getPosition())) {
        hql += "and  lower(position)=? ";
        params.add(form.getPosition());
      }
      if (StringUtils.isNotBlank(form.getInsName())) {
        hql += "and instr(lower(t.insName),?)>0 ";
        params.add(form.getInsName());
      }
      if (StringUtils.isNotBlank(form.getEmail())) {
        hql += "and email = ?  ";
        params.add(form.getEmail());
      }
    } else {
      hql +=
          "and (instr(lower(t.name),?)>0 or instr(lower(t.ename),?)>0 or position=? or instr(lower(t.insName),?)>0) ";
      params.add(form.getSearchKey());
      params.add(form.getSearchKey());
      params.add(form.getSearchKey());
      params.add(form.getSearchKey());
    }

    hql += " and not exists (select 1 from PsnPrivate pp where pp.psnId = t.personId)";
    String orderHql = " order by nvl(nvl(t.name,t.firstName||t.lastName),t.ename),t.personId";
    Query query = this.createQuery(hql + orderHql, params.toArray());
    Object count = this.createQuery(countHql + hql + orderHql, params.toArray()).uniqueResult();
    if (count != null) {
      form.setCount(Integer.parseInt(count.toString()));
    }
    List list =
        query.setFirstResult((form.getPageNo() - 1) * form.getPageSize()).setMaxResults(form.getPageSize()).list();
    form.setResult(list);

  }

  @SuppressWarnings("unchecked")
  public List<Person> getHandlePsnList(Integer size) {
    String hql =
        "Select new Person(t.personId,t.insId,t.insName,t.position,t.department,t.degree) from Person t where t.insName Is Not Null And Not Exists(Select 1 From PsnEdu m Where m.psnId=t.personId)"
            + "And Not Exists(Select 1 From PsnWork n Where n.psnId=t.personId)";
    return this.createQuery(hql).setMaxResults(size).list();
  }

}
