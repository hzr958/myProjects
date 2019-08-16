package com.smate.center.batch.dao.sns.psn;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PsnJnlRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 持久化个人期刊统计刷新数据.
 * 
 * @author WeiLong Peng
 * 
 */
@Repository
public class PsnJnlRefreshDao extends SnsHibernateDao<PsnJnlRefresh, Long> {

  /**
   * 查询指定类型的指定成果刷新纪录.
   * 
   * @param pubId
   * @param articleType
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public PsnJnlRefresh queryPsnJnlRefresh(Long pubId, Integer articleType) throws DaoException {

    List<PsnJnlRefresh> list =
        super.createQuery("from PsnJnlRefresh t where t.articleType = ? and t.pubId = ?", articleType, pubId).list();

    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 删除指定人员信息（ 人员合并用）zk add.
   */
  public void delPsnJnlRefresh(Long psnId) throws DaoException {
    super.createQuery("delete from PsnJnlRefresh where  psnId=?", psnId).executeUpdate();
  }

  public List<PsnJnlRefresh> getPsnJnlRefresh(Long psnId) throws DaoException {
    return super.createQuery("from PsnJnlRefresh where  psnId=?", psnId).list();
  }
}
