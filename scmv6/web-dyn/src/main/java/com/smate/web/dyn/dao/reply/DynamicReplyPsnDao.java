package com.smate.web.dyn.dao.reply;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.reply.DynamicReplyPsn;

/**
 * 人员回复Dao.
 * 
 * @author zk
 * 
 */
@Repository
public class DynamicReplyPsnDao extends SnsHibernateDao<DynamicReplyPsn, Long> {
  /**
   * 查询动态评论列表
   * 
   * @param resId
   * @param resType
   * @param maxresult
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DynamicReplyPsn> getDynReply(Long resId, int resType, int pageNo, int maxResult) {
    String hql =
        " select new DynamicReplyPsn(t1.recordId, t1.replyId, t1.replyerId, t1.syncFlag, t1.replyerName, t1.replyerEnName, t1.replyerAvatar, t1.replyContent, "
            + "t1.replyDate , t1.replyAddResId ) "
            + "from DynamicReplyPsn t1,DynamicReplyRes t2  where t2.resType=:resType and t1.replyId=t2.replyId and t2.resId=:resId order by t1.replyDate desc";
    return super.createQuery(hql).setParameter("resId", resId).setParameter("resType", resType)
        .setFirstResult((pageNo - 1) * maxResult).setMaxResults(maxResult).list();

  }

  /**
   * 查询动态评论列表
   * 
   * @param resId
   * @param resType
   * @param maxresult
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DynamicReplyPsn> getDynReply2(Long resId, DynamicForm form) {
    String hql =
        " select new DynamicReplyPsn(t1.recordId, t1.replyId, t1.replyerId, t1.syncFlag, t1.replyerName, t1.replyerEnName, t1.replyerAvatar, t1.replyContent, "
            + "t1.replyDate , t1.replyAddResId ) "
            + "from DynamicReplyPsn t1,DynamicReplyRes t2  where  t1.replyId=t2.replyId and t2.resId=:resId order by t1.replyDate desc";

    List<DynamicReplyPsn> list = super.createQuery(hql).setParameter("resId", resId).list();
    form.setTotalCount(list.size());
    return super.createQuery(hql).setParameter("resId", resId)
        .setFirstResult((form.getPageNumber() - 1) * form.getPageSize()).setMaxResults(form.getPageSize()).list();

  }

  /**
   * 查询动态评论列表
   * 
   * @param resId
   * @param resType
   * @param maxresult
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DynamicReplyPsn> getSampleDynReply(Long resId, int resType, int pageNo, int maxResult) {
    String hql =
        " select new DynamicReplyPsn(t1.recordId, t1.replyId, t1.replyerId, t1.syncFlag, t1.replyerName, t1.replyerEnName, t1.replyerAvatar, t1.replyContent, "
            + "t1.replyDate , t1.replyAddResId ) "
            + "from DynamicReplyPsn t1,DynamicReplyRes t2  where t2.resType=:resType and t1.replyId=t2.replyId and t2.resId=:resId order by t1.replyDate asc";
    return super.createQuery(hql).setParameter("resId", resId).setParameter("resType", resType)
        .setFirstResult((pageNo - 1) * maxResult).setMaxResults(maxResult).list();

  }

  /**
   * 查看当天psnA对某条资源的评论次数
   */
  public int getCommentCount(Long resId, Long psnId, Integer resType) {
    String hql =
        "select p.recordId from DynamicReplyPsn p,DynamicReplyRes r where p.replyId=r.replyId and p.replyerId=:psnId and r.resId=:resId and r.resType=:resType  and to_char(p.replyDate,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd')";
    int count = super.createQuery(hql).setParameter("psnId", psnId).setParameter("resId", resId)
        .setParameter("resType", resType).list().size();
    return count;
  }
}
