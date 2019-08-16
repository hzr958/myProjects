package com.smate.center.batch.dao.rcmd.journal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rcmd.journal.RcmdPsnJournal;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 人员期刊.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class RcmdPsnJournalDao extends RcmdHibernateDao<RcmdPsnJournal, Long> {

  /**
   * 获取人员所有期刊.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RcmdPsnJournal> getPsnAllJournal(Long psnId) {
    return super.createQuery("from RcmdPsnJournal t where t.psnId = ? ", psnId).list();
  }

  /**
   * 删除人员期刊信息.
   * 
   * @param id
   */
  public void delPsnJournalById(Long id) {

    super.createQuery("delete from RcmdPsnJournal t where t.id = ? ", id).executeUpdate();
  }

  /**
   * 删除人员所有期刊.
   * 
   * @param psnId
   */
  public void delPsnJournal(Long psnId) {

    super.createQuery("delete from RcmdPsnJournal t where t.psnId = ? ", psnId).executeUpdate();
  }

  /**
   * 获取用户发表期刊最多档，必须是大于一次的
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("rawtypes")
  public int getPsnJnlMaxGrade(Long psnId) {
    String hql =
        "select t.grade from RcmdPsnJournal t where t.psnId=? group by t.grade order by sum(t.tf) desc,t.grade desc";
    List list = super.createQuery(hql, psnId).setMaxResults(1).list();
    return CollectionUtils.isEmpty(list) ? 0 : (Integer) list.get(0);
  }

  @SuppressWarnings("rawtypes")
  public int getPsnJnlCountByIssn(Long psnId, String issn) {
    String hql = "select tf  from RcmdPsnJournal where psnId=? and issnTxt=?";
    List list = super.createQuery(hql, psnId, issn.toLowerCase()).list();
    return CollectionUtils.isEmpty(list) ? 0 : (Integer) list.get(0);
  }

  public List<String> getPsnPubCopartnerByJnl(Long psnId) {
    String hql =
        "select distinct t1.issnTxt from RcmdPsnJournal t1,PsnKnowCopartner t2 where t1.psnId=t2.cptPsnId and t2.psnId=?";
    return super.find(hql, psnId);
  }

  public int getPsnFriendJnlCountByIssn(Long psnId, String issn) {
    String hql =
        "select count(t1.id) from RcmdPsnJournal t1 where t1.issnTxt=? and t1.psnId in(select t2.friendPsnId from Friend t2 where t2.psnId=?)";
    return ((Long) findUnique(hql, issn.toLowerCase(), psnId)).intValue();
  }

  public List<RcmdPsnJournal> getPsnFriendJnl(Long psnId) {
    String hql = "select t1 from RcmdPsnJournal t1,Friend t2 where t1.psnId=t2.friendPsnId and t2.psnId=?";
    return find(hql, psnId);
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getPsnListByIssn(String issn) {
    String hql =
        "select t.psnId as psnId,count(t.id) as count from RcmdPsnJournal t where t.issnTxt=:issnTxt group by t.psnId";
    return super.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
        .setParameter("issnTxt", issn.toLowerCase()).list();
  }

  /**
   * 获取人员期刊领域大类top n个.
   * 
   * @param psnId
   * @param n
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getPsnJouralClassifyTopN(Long psnId, int n) {

    String hql =
        "select t1.classify from RcmdPsnJournal t,JournalAreaClassify t1 where t.psnId=? and t.issnTxt = t1.issnTxt  group by t1.classify order by sum(t.tf) desc";
    return super.createQuery(hql, psnId).setMaxResults(n).list();
  }

  /**
   * 统计psnId和coPsnId相同期刊数量.
   * 
   * @param psnId
   * @param coPsnId
   * @return
   * @throws DaoException
   */
  public Long psnJournalEqualCount(Long psnId, Long coPsnId) throws DaoException {
    String hql =
        "select count(*) from RcmdPsnJournal t1,RcmdPsnJournal t2 where t1.psnId=? and t2.psnId=? and t1.issnTxt=t2.issnTxt";
    return findUnique(hql, psnId, coPsnId);
  }

  /**
   * 获取人员的issn列表．
   * 
   * @param psnId
   * @return
   */
  public List<String> getPsnIssnTxtList(Long psnId) throws DaoException {
    String hql = "select distinct lower(t.issnTxt) from RcmdPsnJournal t where t.psnId=?";
    return super.find(hql, psnId);
  }

  /**
   * 获取有相同期刊的人员
   * 
   * @param issnStr
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getSameIssnPsnId(String issnStr, List<Long> psnIds) {

    List<String> issnList = rebuildIssnList(issnStr);
    if (CollectionUtils.isEmpty(issnList) || CollectionUtils.isEmpty(psnIds)) {
      return null;
    }
    String hql = "select t.psnId from RcmdPsnJournal t where t.issnTxt in (:issns) and t.psnId in (:psnIds)";
    return super.createQuery(hql).setParameterList("issns", issnList).setParameterList("psnIds", psnIds).list();
  }

  private List<String> rebuildIssnList(String issnStr) {
    if (StringUtils.isBlank(issnStr)) {
      return null;
    }
    String[] issns = issnStr.split(",");
    return Arrays.asList(issns);
  }
}
