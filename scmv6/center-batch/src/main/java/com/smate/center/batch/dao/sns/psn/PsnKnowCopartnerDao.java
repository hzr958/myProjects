package com.smate.center.batch.dao.sns.psn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.psn.PsnKnowCopartner;
import com.smate.center.batch.model.sns.pub.FriendTempSys;
import com.smate.center.batch.util.pub.FsysPage;
import com.smate.core.base.utils.common.HqlUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;


/**
 * @author lcw
 * 
 */
public class PsnKnowCopartnerDao extends SnsHibernateDao<PsnKnowCopartner, Long> {

  /**
   * 查询至多maxNum位合作者id
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Map<String, Object> getPsnKnowCopSix(Long psnId, String type, Integer maxNum) throws DaoException {

    String pubCpt = "";
    String prjCpt = "";
    if (StringUtils.isNotBlank(type)) {
      String[] cptTypes = StringUtils.split(type, ",");
      pubCpt = cptTypes[0];
      if (cptTypes.length > 1) {
        prjCpt = cptTypes[1];
      }
    }
    String countSql = " select count(copartner_id) ";
    String listSql = " select copartner_id ";
    String listSqlEx = " where rownum<= ?";

    String sql =
        " from ( select t.copartner_id from PSN_KNOW_COPARTNER t where t.psn_id= ?  and (t.copartner_types like ? or t.copartner_types like ?)"
            + " and (t.is_friend is null or t.is_friend <> 1) and t.copartner_id not in(select b.TEMP_PSN_ID from  PSN_FRIEND_SYS b where b.psn_id =?) "
            + " and t.copartner_id not in (select c.TEMP_PSN_ID from PSN_FRIEND_TEMP c where c.psn_id =? and c.temp_psn_id is not null) "
            + " order by nvl(t.pub_score,0) desc ) ";
    Integer count = super.queryForInt(countSql + sql, new Object[] {psnId, pubCpt, prjCpt, psnId, psnId});
    List<Map> listMap =
        super.queryForList(listSql + sql + listSqlEx, new Object[] {psnId, pubCpt, prjCpt, psnId, psnId, maxNum});

    Map<String, Object> returnMap = new HashMap<String, Object>();

    returnMap.put("count", count);
    returnMap.put("listMap", listMap);
    return returnMap;
  }

  public PsnKnowCopartner getPsnKnowCopartner(Long psnId, Long cptPsnId) {
    String hql = "from PsnKnowCopartner where psnId=? and cptPsnId=?";
    return super.findUnique(hql, psnId, cptPsnId);
  }

  public void delPsnKnowCopartnerByPsnId(Long psnId) {
    String hql = "delete from PsnKnowCopartner where psnId=?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PsnKnowCopartner> getPsnKnowCopartner(Long psnId) throws DaoException {
    return super.createQuery("from PsnKnowCopartner where psnId=? or cptPsnId=?", psnId, psnId).list();
  }

  public Long getPsnByCptCount(Long psnId, String cptType) {
    String hql =
        "select count(*) from PsnKnowCopartner t where t.psnId=? and t.cptTypes like ? and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t6.tempPsnId from FriendTemp t6 where t6.psnId=? and t6.tempPsnId is not null)";
    return findUnique(hql, psnId, "%" + cptType + "%", psnId, psnId);
  }

  public Long findPartnersCount(Long psnId, String cptType) throws DaoException {
    String hql =
        "select count(*) from PsnKnowCopartner t where t.psnId=? and t.cptTypes like ? and t.cptPsnId not in(select f.friendPsnId from Friend f where f.psnId=?) and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t6.tempPsnId from FriendTemp t6 where t6.psnId=? and t6.tempPsnId is not null)";
    return findUnique(hql, psnId, "%" + cptType + "%", psnId, psnId, psnId);
  }

  public Page<PsnKnowCopartner> findPsnKnowCopartnerPage(Page<PsnKnowCopartner> page, Long psnId, String cptType)
      throws DaoException {
    String hql =
        "from PsnKnowCopartner t where t.psnId=? and t.cptTypes like ? and t.cptPsnId not in(select f.friendPsnId from Friend f where f.psnId=?) and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t6.tempPsnId from FriendTemp t6 where t6.psnId=? and t6.tempPsnId is not null)";
    return super.findPage(page, hql, psnId, "%" + cptType + "%", psnId, psnId, psnId);

  }

  /**
   * 由于用到Oracle的随机排序Order By dbms_random.value，所以用Sql分页查询
   * 
   * @param page
   * @param psnId
   * @param cptType
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<PsnKnowCopartner> findPsnKnowCopartnerRandom(Page<PsnKnowCopartner> page, Long psnId, String cptType)
      throws DaoException {
    String sql = "select t.* from PSN_KNOW_COPARTNER t where t.psn_id=? and t.copartner_types like ?"
        + "and (t.is_friend is null or t.is_friend <> 1) and t.copartner_id not in(select b.TEMP_PSN_ID from  PSN_FRIEND_SYS b where b.psn_id =?)"
        + "and t.copartner_id not in (select c.TEMP_PSN_ID from PSN_FRIEND_TEMP c where c.psn_id =? and c.temp_psn_id is not null) Order By dbms_random.value";
    return super.QueryTable(sql, new Object[] {psnId, "%" + cptType + "%", psnId, psnId}, page);

  }

  /**
   * 分页随机查询合作者
   * 
   * @param page
   * @param psnId
   * @param pubCpt
   * @param prjCpt
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<PsnKnowCopartner> findPsnKnowCopartnerRandom(Page<PsnKnowCopartner> page, Long psnId, String pubCpt,
      String prjCpt) throws DaoException {
    String sql =
        "select t.* from PSN_KNOW_COPARTNER t where t.psn_id=? and (t.copartner_types like ? or t.copartner_types like ?)"
            + "and (t.is_friend is null or t.is_friend <> 1) and not exists (select 1 from  PSN_FRIEND_SYS b where b.TEMP_PSN_ID=t.copartner_id and b.psn_id =?)"
            + "and not exists (select 1 from PSN_FRIEND_TEMP c where t.copartner_id=c.TEMP_PSN_ID and c.psn_id =? and c.temp_psn_id is not null) Order By dbms_random.value";
    return super.QueryTable(sql, new Object[] {psnId, pubCpt, prjCpt, psnId, psnId}, page);

  }

  /**
   * 不使用Oracle的随机排序"Order By dbms_random.value"进行分页查询
   * 
   * @param page
   * @param psnId
   * @param cptType
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Page<PsnKnowCopartner> findPsnKnowCopartnerList(Page<PsnKnowCopartner> page, Long psnId, String cptType)
      throws DaoException {
    String sql = "select t.* from PSN_KNOW_COPARTNER t where t.psn_id=? and t.copartner_types like ?"
        + "and (t.is_friend is null or t.is_friend <> 1) and t.copartner_id not in(select b.TEMP_PSN_ID from  PSN_FRIEND_SYS b where b.psn_id =?)"
        + "and t.copartner_id not in (select c.TEMP_PSN_ID from PSN_FRIEND_TEMP c where c.psn_id =? and c.temp_psn_id is not null)";
    return super.QueryTable(sql, new Object[] {psnId, "%" + cptType + "%", psnId, psnId}, page);

  }

  /**
   * 得到合作者人员id,用于可能认识的人查询psnHtml
   * 
   * @param psnId
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPsnKnowCopPsnIds(Long psnId, @SuppressWarnings("rawtypes") FsysPage page) {
    String hql =
        "select cptPsnId from PsnKnowCopartner t where t.psnId=? and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.cptPsnId not in(select f.friendPsnId from Friend f where f.psnId=?)";
    Query q = createQuery(hql, psnId, psnId, psnId, psnId);
    Long totalCount = 0L;
    if (page.isAutoCount()) {
      totalCount = countHqlResult(hql, psnId, psnId, psnId, psnId);
      page.setTotalCount(totalCount);
    }
    if (page.getPageSize() < 10) {
      totalCount = totalCount > 30 ? 30L : totalCount;
      Long random = HqlUtils.getRandom(0, totalCount.intValue() - page.getPageSize());
      random = random < 0 ? 0L : random;
      q.setFirstResult(random.intValue());
      q.setMaxResults(random.intValue() + page.getPageSize());
    } else {
      q.setFirstResult(page.getFirst() - 1);
      q.setMaxResults(page.getPageSize());
    }
    return q.list();
  }

  @SuppressWarnings("unchecked")
  public FsysPage<FriendTempSys> findFriendTempAutoSys(Long psnId, @SuppressWarnings("rawtypes") FsysPage page) {
    List<FriendTempSys> newList = new ArrayList<FriendTempSys>();
    String hql =
        "from PsnKnowCopartner t where t.psnId=? and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.cptPsnId not in(select f.friendPsnId from Friend f where f.psnId=?)";
    Query q = createQuery(hql, psnId, psnId, psnId, psnId);
    Long totalCount = 0L;
    if (page.isAutoCount()) {
      totalCount = countHqlResult(hql, psnId, psnId, psnId, psnId);
      page.setTotalCount(totalCount);
    }
    if (page.getPageSize() < 10) {
      totalCount = totalCount > 30 ? 30L : totalCount;
      Long random = HqlUtils.getRandom(0, totalCount.intValue() - page.getPageSize());
      random = random < 0 ? 0L : random;
      q.setFirstResult(random.intValue());
      q.setMaxResults(random.intValue() + page.getPageSize());
    } else {
      q.setFirstResult(page.getFirst() - 1);
      q.setMaxResults(page.getPageSize());
    }

    List<PsnKnowCopartner> list = q.list();
    if (CollectionUtils.isNotEmpty(list)) {
      for (PsnKnowCopartner rec : list) {
        FriendTempSys sys = new FriendTempSys();
        sys.setPsnId(psnId);
        sys.setTempPsnId(rec.getCptPsnId());
        sys.setTempPsnName(rec.getCptName());
        sys.setTempPsnFirstName(rec.getCptFirstName());
        sys.setTempPsnLastName(rec.getCptLastName());
        sys.setTempPsnHeadUrl(rec.getCptHeadUrl());
        sys.setTempPsnTitel(rec.getCptViewTitel());
        if (rec.getCptTypes().indexOf("4") != -1 || rec.getCptTypes().indexOf("5") != -1) {
          sys.setIscpt(true);
        }
        newList.add(sys);
      }
    }
    page.setResult(newList);
    return page;
  }

  /**
   * 得到合作者psnid,用于psnhtml_zk
   * 
   * @param psnId
   * @param pubCpt
   * @param prjCpt
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findFriendListTempAutoSysPsnIds(Long psnId, String pubCpt, String prjCpt) throws DaoException {
    String hql =
        "select t.cptPsnId from PsnKnowCopartner t where t.psnId=? and (t.cptTypes like ? or t.cptTypes like ?) and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.cptPsnId not in(select f.friendPsnId from Friend f where f.psnId=?)";
    if (pubCpt.indexOf("4") != -1 && prjCpt.indexOf("5") == -1) {
      hql += " order by t.pubScore desc,t.id";
    } else if (prjCpt.indexOf("5") != -1 && pubCpt.indexOf("4") == -1) {
      hql += " order by t.prjScore desc,t.id";
    } else if (pubCpt.indexOf("4") != -1 && prjCpt.indexOf("5") != -1) {
      hql += " order by t.pubScore desc,t.prjScore desc,t.id";
    }
    return createQuery(hql, psnId, pubCpt, prjCpt, psnId, psnId, psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<FriendTempSys> findFriendListTempAutoSys(Long psnId, String pubCpt, String prjCpt) throws DaoException {
    List<FriendTempSys> newList = new ArrayList<FriendTempSys>();
    String hql =
        "from PsnKnowCopartner t where t.psnId=? and (t.cptTypes like ? or t.cptTypes like ?) and t.cptPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.cptPsnId not in(select t7.tempPsnId from FriendTemp t7 where t7.psnId=? and t7.tempPsnId is not null) and t.cptPsnId not in(select f.friendPsnId from Friend f where f.psnId=?)";
    if (pubCpt.indexOf("4") != -1 && prjCpt.indexOf("5") == -1) {
      hql += " order by t.pubScore desc,t.id";
    } else if (prjCpt.indexOf("5") != -1 && pubCpt.indexOf("4") == -1) {
      hql += " order by t.prjScore desc,t.id";
    } else if (pubCpt.indexOf("4") != -1 && prjCpt.indexOf("5") != -1) {
      hql += " order by t.pubScore desc,t.prjScore desc,t.id";
    }
    Query q = createQuery(hql, psnId, pubCpt, prjCpt, psnId, psnId, psnId);

    List<PsnKnowCopartner> list = q.list();
    if (CollectionUtils.isNotEmpty(list)) {
      for (PsnKnowCopartner rec : list) {
        FriendTempSys sys = new FriendTempSys();
        sys.setPsnId(psnId);
        sys.setTempPsnId(rec.getCptPsnId());
        sys.setTempPsnName(rec.getCptName());
        sys.setTempPsnFirstName(rec.getCptFirstName());
        sys.setTempPsnLastName(rec.getCptLastName());
        sys.setTempPsnHeadUrl(rec.getCptHeadUrl());
        sys.setTempPsnTitel(rec.getCptViewTitel());
        sys.setIscpt(true);
        newList.add(sys);
      }
    }
    return newList;
  }

  @SuppressWarnings("unchecked")
  public List<Long> findCptPsnIdList(Long psnId, String pubCpt, String prjCpt) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("select t.cptPsnId");
    hql.append(" from PsnKnowCopartner t");
    hql.append(" where");
    List<Object> params = new ArrayList<Object>();
    hql.append(" t.psnId=?");
    params.add(psnId);
    hql.append(" and (t.cptTypes like ? or t.cptTypes like ?)");
    params.add(pubCpt);
    params.add(prjCpt);
    hql.append(" and not exists (select 1 from FriendTempSys t5 where t.cptPsnId=t5.tempPsnId and t5.psnId=?)");
    params.add(psnId);
    hql.append(
        " and not exists (select 1 from FriendTemp t7 where t.cptPsnId=t7.tempPsnId and t7.psnId=? and t7.tempPsnId is not null)");
    params.add(psnId);
    hql.append(" and not exists (select 1 from Friend f where t.cptPsnId=f.friendPsnId and f.psnId=?)");
    params.add(psnId);
    Query queryResult = super.createQuery(hql.toString(), params.toArray());

    return queryResult.list();
  }

  /**
   * 判断是否为合作者.
   * 
   * @param psnId
   * @param coPsnId
   * @return
   * @throws DaoException
   */
  public Long isCopartner(Long psnId, Long coPsnId) throws DaoException {
    String hql = "select count(*) from PsnKnowCopartner where psnId=? and cptPsnId=?";
    return findUnique(hql, psnId, coPsnId);
  }
}
