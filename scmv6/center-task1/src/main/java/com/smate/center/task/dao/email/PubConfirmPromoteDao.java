package com.smate.center.task.dao.email;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rcmd.quartz.PublicationConfirm;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 成果认领推广 服务类
 * 
 * @author zk
 */

@Repository
public class PubConfirmPromoteDao extends RcmdHibernateDao<PublicationConfirm, Long> {

  // 获取psnid指派分数最高的至多size条成果id
  @SuppressWarnings("unchecked")
  public List<PublicationConfirm> getPublicationConfirmByScore(Long psnId, Integer size) throws DaoException {
    String hql =
        "select new PublicationConfirm(pc.rolPubId,pc.insId) from PublicationConfirm pc , PubConfirmRolPub pr where  pr.rolPubId = pc.rolPubId  and pc.confirmResult = 0   and pc.psnId = ?  order by pc.assignScore desc";
    return super.createQuery(hql, psnId).setMaxResults(size).list();
  }

  // psnid拥有多少条未认领成果
  public Long getCfmPubCount(Long psnId) throws DaoException {
    String hql =
        "select pc.rolPubId from PublicationConfirm pc , PubConfirmRolPub pr where  pr.rolPubId = pc.rolPubId and pc.psnId = ? and pc.confirmResult=0 ";
    return super.countHqlResult(hql, psnId);
  }

  // 获取最大年份
  public Integer getInsLastYear(Long psnId) throws DaoException {
    String sql =
        " select max(c.publish_year) as maxYear from PUB_CONFIRM  p ,pub_confirm_rolpub c where p.psn_id = ? and p.rol_pub_id = c.rolpub_id and c.article_type=1 ";
    // return (Map) super.queryForList(sql, new Object[] { psnId }).get(0);
    return super.queryForInt(sql, new Object[] {psnId});
  }

  // 获取最大年份至多三条成果
  @SuppressWarnings("rawtypes")
  public List getLastYearThreePub(Long psnId, Integer lastYear) throws DaoException {
    String sql =
        "select nvl(c.zh_title,c.en_title) as title,nvl(c.author_names,'') as authorNames,nvl(c.brief_desc,'') as briefDesc ,nvl(FULLTEXT_FILEID,'') from PUB_CONFIRM  p ,pub_confirm_rolpub c where p.psn_id = ? and p.rol_pub_id = c.rolpub_id  and c.article_type=1 and c.publish_year=? and rownum<=3";
    return super.queryForList(sql, new Object[] {psnId, lastYear});
  }

  // 获取最大年份成果数
  public Integer getLastYearSum(Long psnId, Integer lastYear) throws DaoException {
    String sql =
        "  select count(c.rolpub_id) from PUB_CONFIRM  p ,pub_confirm_rolpub c where p.psn_id = ? and p.rol_pub_id = c.rolpub_id and c.article_type=1 and c.publish_year=? ";
    return super.queryForInt(sql, new Object[] {psnId, lastYear});
  }
}
