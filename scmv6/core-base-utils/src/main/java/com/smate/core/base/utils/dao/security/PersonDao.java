package com.smate.core.base.utils.dao.security;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;

/**
 * 
 * 邮件服务 persondao,只做查询
 * 
 * @author zk
 * 
 */
@Repository
public class PersonDao extends SnsHibernateDao<Person, Long> {
  public List<Person> findListByIds(List<Long> psnIdList) {
    String hql = "select new Person(t.personId,t.birthday,t.sex,t.posId,t.insId) from Person t "
        + "where t.personId in(:psnIdList)";
    return super.createQuery(hql).setParameterList("psnIdList", psnIdList).list();
  }

  /**
   * 查询人员单位和职称
   * 
   * @param psnId
   * @return
   */
  public Person findPersonInsAndPos(Long psnId) {
    String hql =
        "select new Person(personId,name,firstName,lastName,avatars,insName,position,insId,posId) from Person p where p.personId=:psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 判断人员是否存在
   * 
   * @param psnId
   * @return
   */
  public Long existsPerson(Long psnId) {
    String hql = "select p.personId from Person p where p.personId=:psnId";
    Object obj = super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;
  }

  /**
   * 获取人员id,姓名，系统语言
   * 
   * @param psnId
   * @return
   */
  public Person getPersonForSnsMenu(Long psnId) {
    String hql =
        "select new Person(p.name,p.firstName,p.lastName,p.personId,p.localLanguage,p.avatars) from Person p where p.personId=:psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取人员id,姓名
   * 
   * @param psnId
   * @return
   */
  public Person getPersonName(Long psnId) {
    String hql = "from Person p where p.personId=:psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取人员 p.name,p.firstName,p.lastName,p.ename,p.personId,p.insId
   * 
   * @param psnId
   * @return
   */
  public Person getPersonNameIns(Long psnId) {
    String hql =
        "select new Person(p.name,p.firstName,p.lastName,p.ename,p.personId,p.insId) from Person p where p.personId=:psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取人员id,姓名
   * 
   * @param psnId
   * @returnString firstName, String lastName, String name, String ename
   */
  public Person getPersonNameNotId(Long psnId) {
    String hql = "select new Person(p.firstName,p.lastName,p.name,p.ename) from Person p where p.personId=:psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取人员id,姓名，接收邮箱语言、性别
   * 
   * @param psnId
   * @return
   */
  public Person getPeronsForEmail(Long psnId) {
    return super.findUnique(
        "select new Person(p.personId,p.email,p.name,p.lastName,p.firstName,p.emailLanguageVersion,p.sex) from Person p where p.personId = ? ",
        psnId);
  }

  /**
   * 获取人员邮箱接收语言
   * 
   * @param psnId
   * @return
   */
  public String getEmailLanguByPsnId(Long psnId) {
    String hql = "select p.emailLanguageVersion from Person p where p.personId = ?";
    String queryStr = (String) super.createQuery(hql, psnId).uniqueResult();
    return StringUtils.isBlank(queryStr) || "zh_CN".equals(queryStr) || "zh".equals(queryStr) ? "zh_CN" : "en_US";
  }

  /**
   * 获取人员系统语言
   * 
   * @param psnId
   * @return
   */
  public String getLangByPsnId(Long psnId) {
    String hql = "select p.localLanguage from Person p where p.personId = ?";
    String queryStr = (String) super.createQuery(hql, psnId).uniqueResult();
    return StringUtils.isBlank(queryStr) || "zh_CN".equals(queryStr) || "zh".equals(queryStr) ? "zh_CN" : "en_US";
  }

  /**
   * 根据用户id获得头像
   */
  public String getPsnImgByObjectId(Long psnId) {
    String hql = "select p.avatars from Person p where p.personId=?";
    return (String) super.createQuery(hql, psnId).uniqueResult();
  }

  /**
   * 根据用户id获得地区
   */
  public Long getPsnRegionIdByObjectId(Long psnId) {
    String hql = "select p.regionId from Person p where p.personId=?";
    return (Long) super.createQuery(hql, psnId).uniqueResult();
  }

  /**
   * 用于弹出窗中人员信息
   * 
   * @param psnId
   * @return
   */
  public Person findPerson(Long psnId) {
    String hql =
        "select new Person(p.personId,p.firstName,p.lastName,p.name,p.ename,p.sex,p.avatars,p.defaultAffiliation) from Person p where p.personId= :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 用于弹出窗中人员信息
   * 
   * @param psnId
   * @return
   */
  public Person findPersonByPsnId(Long psnId) {
    String hql = "from Person p where p.personId= :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 用于弹出窗中人员信息
   * 
   * @param psnIdList
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Person> findPerson(List<Long> psnIdList) {
    String hql =
        "select new Person(p.personId,p.firstName,p.lastName,p.name,p.sex,p.avatars,p.defaultAffiliation) from Person p where p.personId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIdList).list();
  }

  /**
   * 获取 人员姓名、姓、名、头像、人员id
   * 
   * @param psnId
   * @return
   */
  public Person findPersonBase(Long psnId) {
    String hql =
        "select new Person(p.name,p.firstName,p.lastName,p.avatars,p.personId,p.ename,p.insName) from Person p where p.personId= :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取 人员姓名、姓、名、头像、人员id , 机构信息
   * 
   * @param psnId
   * @return
   */
  public Person findPersonBaseIncludeIns(Long psnId) {
    String hql =
        "select new Person(p.name,p.firstName,p.lastName,p.avatars,p.personId,p.ename ,p.insName, p.insId,p.email) from Person p where p.personId= :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取 人员姓名、othername、头像、人员id , 机构信息
   * 
   * @param psnId
   * @return
   */
  public Person findPersonInfoIncludeIns(Long psnId) {
    String hql =
        "select new Person(p.personId,p.firstName,p.lastName,p.name,p.otherName,p.ename, p.insId,p.insName) from Person p where p.personId= :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  // 用于检索时查询User
  @SuppressWarnings("unchecked")
  public List<Person> findUserByBatchSize(Long lastId, int batchSize) {
    String hql = "from Person t where t.personId > :lastId order by t.personId asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }

  // 批量获取人员姓名
  @SuppressWarnings("unchecked")
  public List<String> findUserNameByBatchSize(Integer pageNo, int batchSize) {
    String hql = " select t.name from Person t order by t.personId asc";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * batchSize).setMaxResults(batchSize).list();
  }

  /**
   * 批量获取人员头像.
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getAvatarUrls(List<Long> psnIds) {
    String hql =
        "select new Map(p.personId as psnId,p.avatars as avatarUrl) from Person p where p.personId in(:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 批量获取获取 人员姓名、姓、名、头像、人员id , 机构信息
   */
  @SuppressWarnings({"unchecked"})
  public List<Person> getPersonBasePage(List<Long> psnIds) {
    String hql =
        "select new Person(p.name,p.firstName,p.lastName,p.avatars,p.personId,p.ename ,p.insName) from Person p where p.personId in(:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 批量获取获取 人员姓名、姓、名、头像、人员id , 机构信息
   */
  @SuppressWarnings({"unchecked"})
  public List<Person> getPersonBaseIncludeIns(List<Long> psnIds) {
    String hql =
        "select new Person(p.name,p.firstName,p.lastName,p.avatars,p.personId,p.ename ,p.insName) from Person p where p.personId in(:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 用于互联互通人员信息
   * 
   * @param psnId
   * @return
   */
  public Person findUnionPerson(Long psnId) {
    String hql =
        "select new Person(p.personId,p.firstName,p.lastName,p.name,p.position,p.sex,p.avatars,p.insId ,p.insName) from Person p where p.personId=:psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取好友人员信息
   * 
   * @param psnId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Person> getMyfriendList(Long psnId, Integer size) {
    String hql =
        "select new Person(t.name,t.firstName,t.lastName,t.avatars,t.personId,t.ename) from Person t where t.personId in(select p.friendPsnId  from Friend p where p.psnId =:psnId) ";
    if (size != null && size != 0) {
      return super.createQuery(hql).setParameter("psnId", psnId).setMaxResults(size).list();
    }
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Person> getPersonByNameAndEmail(String name, String email) {
    String hql = "from Person t where t.name =:name and t.email =:email";
    return super.createQuery(hql).setParameter("name", name).setParameter("email", email).list();
  }

  public List<Long> getPsnIdByNameAndOrg(String name, String org) {
    String hql = "select  t.personId from Person t where t.name =:name and insName=:org";
    return super.createQuery(hql).setParameter("name", name).setParameter("org", org).list();
  }

  @SuppressWarnings("unchecked")
  public List<Person> getPersonByEmail(String email) {
    String hql = "select new Person(t.personId, t.name, t.ename)from Person t where t.email =:email";
    return super.createQuery(hql).setParameter("email", email).list();
  }

  @SuppressWarnings("unchecked")
  public List<Person> getAllPsn(Page page, String nameSearchCount, String emailSearchCount, Integer SearchType) {
    String countHql = "select count(t.personId) ";
    String listHql =
        "select new Person(t.personId, t.name,t.firstName,t.lastName,t.email,t.avatars,t.titolo,t.insName,t.position,t.department,t.regData) ";
    StringBuilder hql = new StringBuilder();
    hql.append("from Person t ");
    if (StringUtils.isNotBlank(nameSearchCount) || StringUtils.isNotBlank(emailSearchCount)) {
      if (SearchType == 1) {// 精确查找
        if (StringUtils.isNotBlank(nameSearchCount) && StringUtils.isNotBlank(emailSearchCount)) {
          hql.append(
              "where t.email = :emailSearchCount and (t.name = :nameSearchCount or t.firstName ||' '|| t.lastName = :nameSearchCount ) ");
        } else {
          hql.append(
              "where t.email = :emailSearchCount or (t.name = :nameSearchCount or t.firstName ||' '|| t.lastName = :nameSearchCount ) ");
        }
      } else {// 模糊查询
        if (StringUtils.isNotBlank(nameSearchCount) && StringUtils.isNotBlank(emailSearchCount)) {
          hql.append("where instr(upper(t.email),upper(:emailSearchCount))>0 and "
              + "( instr(upper(t.name),upper(:nameSearchCount))>0 or instr(upper(t.firstName ||' '|| t.lastName ),upper(:nameSearchCount))>0  or"
              + " instr(upper(t.firstName),upper(:nameSearchCount))>0 or instr(upper(t.lastName),upper(:nameSearchCount))>0 ) ");
        } else {
          hql.append("where instr(upper(t.email),upper(:emailSearchCount))>0 or "
              + "instr(upper(t.name),upper(:nameSearchCount))>0 or instr(upper(t.firstName ||' '|| t.lastName ),upper(:nameSearchCount))>0  or"
              + " instr(upper(t.firstName),upper(:nameSearchCount))>0 or instr(upper(t.lastName),upper(:nameSearchCount))>0");
        }
      }
      page.setTotalCount((Long) super.createQuery(countHql + hql).setParameter("nameSearchCount", nameSearchCount)
          .setParameter("emailSearchCount", emailSearchCount).uniqueResult());
      return super.createQuery(listHql + hql).setParameter("nameSearchCount", nameSearchCount)
          .setParameter("emailSearchCount", emailSearchCount).setFirstResult(page.getFirst() - 1)
          .setMaxResults(page.getPageSize()).list();
    } else {
      page.setTotalCount((Long) super.createQuery(countHql + hql).uniqueResult());
      return super.createQuery(listHql + hql).setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize())
          .list();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Long> getNeedInitPsnId(Integer index, Integer batchSize) {
    String hql = "select t.personId from Person t";
    return this.createQuery(hql).setFirstResult(batchSize * (index - 1)).setMaxResults(batchSize).list();
  }

  public Long getPsnCount() {
    String hql = "select count(t.personId ) from Person t";
    return (Long) super.createQuery(hql).uniqueResult();
  }

  /**
   * 列出好友地区分类最多的前5个
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> sortPersonByRegId(Long psnId) {
    String sql = "select t.region_Id as regionId,count(t.region_Id) as regionCount "
        + "from Person t where t.psn_id in (select friend_psn_id from psn_friend where psn_id=:psnId) "
        + "and t.region_Id is not null group by t.region_Id order by regionCount desc,t.region_Id desc";
    return super.getSession().createSQLQuery(sql).setParameter("psnId", psnId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).setFirstResult(0).setMaxResults(5).list();
  }

  /**
   * 发送邮件用人员信息
   * 
   * @param psnId
   * @return
   */
  public Person findPsnInfoForEmail(Long psnId) {
    String hql =
        "select new Person(personId, firstName, lastName, name, email, regionId, titolo, avatars, position, insName, emailLanguageVersion, department) "
            + " from Person t where t.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> findFriendList(Long psnId, String searchKey, Page page) {
    String count = "select count(1) ";
    String sql = "select t.psn_id,t.NAME,t.ENAME,t.FIRST_NAME,t.LAST_NAME,t.AVATARS,t.ins_name,t.department ";
    String ordre = " order by nvl(t2.selected_date,to_date('1900-01-01','yyyy-mm-dd')) desc, t.psn_id desc";
    StringBuilder sb = new StringBuilder();
    sb.append(" from PERSON t left join RECENT_SELECTED t2 on t.psn_id=t2.selected_psn_id and t2.psn_Id =:psnId ");
    sb.append(" where exists(select 1 from PSN_FRIEND t3 where t3.psn_id=:psnId and t3.friend_psn_id=t.psn_id) ");
    if (StringUtils.isNotBlank(searchKey)) {
      sb.append(" and instr(upper(nvl(nvl(t.name,t.first_Name||t.last_Name),t.ename)),:searchKey)>0 ");
    }
    if (StringUtils.isBlank(searchKey)) {
      page.setTotalCount(((BigDecimal) this.getSession().createSQLQuery(count + sb.toString())
          .setParameter("psnId", psnId).uniqueResult()).longValue());
      return this.getSession().createSQLQuery(sql + sb.toString() + ordre).setParameter("psnId", psnId)
          .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    } else {
      page.setTotalCount(((BigDecimal) this.getSession().createSQLQuery(count + sb.toString())
          .setParameter("psnId", psnId).setParameter("searchKey", searchKey.toUpperCase()).uniqueResult()).longValue());
      return this.getSession().createSQLQuery(sql + sb.toString() + ordre).setParameter("psnId", psnId)
          .setParameter("searchKey", searchKey.toUpperCase()).setFirstResult(page.getFirst() - 1)
          .setMaxResults(page.getPageSize()).list();
    }
  }

  @SuppressWarnings("unchecked")
  public List<Person> findPersonList(String searchKey, Page page) {
    String count = "select count(1) ";
    String hql = "select new Person(personId,name,firstName,lastName,avatars,insName,position,insId,posId) ";
    StringBuilder sb = new StringBuilder();
    sb.append(" from Person t where ");
    sb.append("  instr(upper(nvl(nvl(t.name,t.firstName||t.lastName),t.ename)),:searchKey)>0 ");
    String orderhql =
        " order by instr(upper(nvl(nvl(t.name,t.firstName||t.lastName),t.ename)),:searchKey) ," + " t.personId ";
    page.setTotalCount((Long) this.createQuery(count + sb.toString()).setParameter("searchKey", searchKey.toUpperCase())
        .uniqueResult());
    return this.createQuery(hql + sb.toString() + orderhql).setParameter("searchKey", searchKey.toUpperCase())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
  }

  public List<Person> findPersonList(List<Long> psnIds) {
    StringBuilder sb = new StringBuilder();
    sb.append(" from Person t where  t.personId in (:psnIds)");
    return this.createQuery(sb.toString()).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 查询psnId的数量
   * 
   * @param searchKey
   * @return
   */
  public Long findPsnIdCount(String searchKey) {
    String countHql = "select count(1) " + " from Person t "
        + " where   instr(upper(nvl(nvl(t.name,t.firstName||t.lastName),t.ename)),:searchKey)>0 "
        + "  order by  t.personId";
    return (Long) this.createQuery(countHql).setParameter("searchKey", searchKey.toUpperCase()).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findPsnIdList(String searchKey, Integer pageNo, Integer pageSize) {
    String hql = " select   t.personId  " + " from Person t "
        + " where  instr(upper(nvl(nvl(t.name,t.firstName||t.lastName),t.ename)),:searchKey)>0 "
        + " order by t.personId ";
    return this.createQuery(hql).setParameter("searchKey", searchKey.toUpperCase())
        .setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
  }

  /**
   * 根据psnId更新人员头像地址
   * 
   * @param psnId
   * @param avatars
   */
  public void updateAvatarsByPsnId(Long psnId, String avatars) {
    String hql = "update Person set avatars=:avatars where personId=:psnId";
    super.createQuery(hql).setParameter("avatars", avatars).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 更新用户的首要邮箱
   * 
   * @param psnId
   * @param email
   */
  public void updateEmailByPsnId(Long psnId, String email) {
    String hql = "update Person set email=:email where personId=:psnId";
    super.createQuery(hql).setParameter("email", email).setParameter("psnId", psnId).executeUpdate();
  }

  public List<Person> getPersonByMobile(String mobile) {
    String hql = "from Person where tel=:mobile";
    return super.createQuery(hql).setParameter("mobile", mobile).list();
  }

  /**
   * 通过email查询psnId
   * 
   * @param email
   * @return
   */
  public Long findPsnIdByEmail(String email) {
    String hql = "select t.personId from Person t where t.email=:email";
    List<Long> list = this.createQuery(hql).setParameter("email", email).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 批量获取人员头像.
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<String> findPsnAvatarUrls(List<Long> psnIds) {
    String hql = "select p.avatars from Person p where p.personId in(:psnIds) order by instr(:psnIdstr,p.personId)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds)
        .setParameter("psnIdstr", StringUtils.strip(psnIds.toString(), "[]")).list();
  }

  public boolean isFriend(Long psnId, Long friendPsnId) {
    String hql = "select count(id) from PSN_FRIEND where psn_Id=:psnId and friend_Psn_Id=:friendPsnId";
    Object c = (Object) this.getSession().createSQLQuery(hql).setParameter("psnId", psnId)
        .setParameter("friendPsnId", friendPsnId).uniqueResult();
    Long count = Long.valueOf(c.toString());
    if (count != null && count > 0) {
      return true;
    } else {
      return false;
    }
  }

  public Long getPsnInsId(Long psnId) {
    String hql = "select insId from Person where personId=:psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  public Person getPsnInfo(Long psnId) {
    String hql = "select new Person(personId,tel,mobile,email,regionId) from Person where personId=:psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getpsnIdbyName(List<String> psnNames) {
    String hql =
        "select t.personId from Person t  where  upper(t.name) in (:psnNames) or upper(t.ename) in (:psnNames)";
    return super.createQuery(hql).setParameterList("psnNames", psnNames).list();
  }

  public List<Person> findHandleISISEnameList(int batchSize) {
    String hql =
        "from Person t where t.personId in( " + "select t1.psnId from OpenUserUnion t1 where t1.token='2bcca485' "
            + ")and (length(t.ename)<>lengthb(t.ename)or length(t.firstName)<>lengthb(t.firstName) "
            + "or length(t.lastName)<>lengthb(t.lastName)) "
            + "and to_char(t.regData,'yyyy-mm')='2018-01' order by t.regData desc";
    return super.createQuery(hql).setMaxResults(batchSize).list();
  }

  public List<Person> findListByMaxId(Integer size, Long dataId) {
    String hql = "from Person t where t.personId>:dataId order by t.personId";
    // TODO Auto-generated method stub
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Person> getPeronsForEmail(Integer size, Long lastPsnId) {
    String hql =
        "select new Person(p.personId,p.email,p.name,p.lastName,p.firstName,p.emailLanguageVersion,p.sex) from Person p where p.personId > :psnId order by p.personId";
    return super.createQuery(hql).setParameter("psnId", lastPsnId).setMaxResults(size).list();
  }

  public List<Long> findList(Long startPsnId, Integer size) {
    String hql = "select t.personId from Person t where t.personId>:startPsnId  order by t.personId";
    return super.createQuery(hql).setParameter("startPsnId", startPsnId).setMaxResults(size).list();
  }

  /**
   * 获取批量的人员信息
   * 
   * @param psnIds
   * @param resultSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Person> findBatchPsnByIds(List<Long> psnIds, int resultSize) {
    String hql = "from Person t where t.personId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).setMaxResults(resultSize).list();
  }

  public List<Person> findListByVerifyPsnInfo(String name, String email, String phone) {
    StringBuilder sb = new StringBuilder();
    sb.append("from Person t where (t.name =?  or t.ename = ? or t.firstName||' '||t.lastName = ?) ");
    List<String> params = new ArrayList<>();
    params.add(name);
    params.add(name);
    params.add(name);
    if (StringUtils.isNotBlank(phone)) {
      sb.append("  and ( t.email = ? or t.tel = ? or t.mobile = ? )");
      params.add(email);
      params.add(phone);
      params.add(phone);
    } else {
      sb.append("  and  t.email = ?  ");
      params.add(email);
    }
    return super.createQuery(sb.toString(), params.toArray()).list();
  }

  /**
   * 从给定的人员ID中去掉不存在的人员ID
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findExistsPsnIds(List<Long> psnIds) {
    String hql = "select t.personId from Person t where t.personId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  public void syncUserLoginToPerson(Long psnId) {
    String hql = "update Person set isLogin=1 where personId =:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdsByStart(Long lastPsnId) {
    String hql = "select t.personId from Person t where insId is not null and t.personId > :lastPsnId "
        + "and not exists (select 1 from RecommendInit b where t.personId=b.psnId and b.fundRecommendInit=1) order by t.personId asc";
    return super.createQuery(hql).setParameter("lastPsnId", lastPsnId).setMaxResults(50).list();
  }


  public Person findPsnInfoWithInsInfo(Long psnId) {
    String hql = "select new Person(p.personId, p.firstName, p.lastName, p.name, p.position, p.sex,"
        + " p.avatars, p.insId, p.insName, p.titolo, p.regionId, p.department, p.ename) from Person p where p.personId = :psnId";
    return (Person) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }
}
