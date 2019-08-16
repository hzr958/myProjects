package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.PubRolSubmissionStat;
import com.smate.center.batch.model.rol.pub.PubRolSubmissionStatKey;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 提交统计信息.
 * 
 * @author yamingd
 * 
 */
@Repository
public class PubRolSubmissionStatDao extends RolHibernateDao<PubRolSubmissionStat, PubRolSubmissionStatKey> {

  /**
   * 读取统计.
   * 
   * @param psnId
   * @param insId
   * @return
   * @throws DaoException
   */
  public PubRolSubmissionStat findByPsnIdInsId(long psnId, long insId) throws DaoException {
    return (PubRolSubmissionStat) super.findUnique("from PubRolSubmissionStat where key.psnId=? and key.insId=?", psnId,
        insId);
  }

  /**
   * 更新发送次数时间.
   * 
   * @param psnId
   * @param insId
   * @param cPsnId
   * @param incr
   * @throws DaoException
   */
  public void updateSend(Long psnId, Long insId, Long cPsnId, int incr) throws DaoException {

    PubRolSubmissionStat stat =
        super.findUnique("from PubRolSubmissionStat where key.psnId=? and key.insId=?", psnId, insId);
    if (stat == null) {
      stat = new PubRolSubmissionStat(psnId, insId, 0L, 0L, 0, 0L);
    }
    stat.setSendTotal((stat.getSendTotal() == null ? 0 : stat.getSendTotal()) + incr);
    stat.setLastSendBy(cPsnId);
    stat.setLastSendAt(new Date());
    super.save(stat);
  }

  /**
   * 记录最后提交时间.
   * 
   * @param psnId
   * @param insId
   */
  public void recodeLastSubmit(Long psnId, Long insId) {
    PubRolSubmissionStat stat =
        super.findUnique("from PubRolSubmissionStat where key.psnId=? and key.insId=?", psnId, insId);
    if (stat == null) {
      stat = new PubRolSubmissionStat(psnId, insId, 0L, 0L, 0, 0L);
    }
    stat.setLastSubmitDate(new Date());
    super.save(stat);
  }

  /**
   * 获取需催交成果人员列表.
   * 
   * @param page
   * @param unitId
   * @param psnName
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public void queryUrgencyList(Page<PubRolSubmissionStat> page, Long insId, Long unitId, String psnName) {
    List param = new ArrayList();
    StringBuilder sb = new StringBuilder();

    sb.append(" from PubRolSubmissionStat t,RolPsnIns p where t.key.insId = ?  and p.allowSubmitPub = 1 ");
    sb.append(" and t.key.insId = p.pk.insId and t.key.psnId = p.pk.psnId and t.totalOutputs is not null ");
    sb.append("  and p.status = 1 and t.snsSubmitTotal is not null  and (t.totalOutputs - t.snsSubmitTotal) > 0 ");
    param.add(insId);

    if (unitId != null) {
      sb.append(" and p.unitId = ? ");
      param.add(unitId);
    }
    if (StringUtils.isNotBlank(psnName)) {
      sb.append(" and (p.zhName like ? or p.enName like ? )");
      param.add("%" + psnName + "%");
      param.add("%" + psnName + "%");
    }
    String hql = sb.toString();

    // 记录数
    Object[] array = param.toArray();
    Long totalCount = super.findUnique("select count(p.pk.psnId) " + hql, array);
    page.setTotalCount(totalCount);

    if (totalCount == 0) {
      return;
    }
    Locale locale = LocaleContextHolder.getLocale();
    String lang = locale.getLanguage();
    String hqlbody =
        " select new PubRolSubmissionStat(t.key, t.totalOutputs, p.zhName, p.enName,p.unitId,t.snsSubmitTotal, t.lastSubmitDate, t.lastSendAt,'"
            + lang + "') ";
    // 查询数据实体
    Query query = super.createQuery(hqlbody + hql, array);
    query.setFirstResult(page.getFirst() - 1);
    query.setMaxResults(page.getPageSize());
    List<PubRolSubmissionStat> result = query.list();
    page.setResult(result);
  }

  /**
   * 更新成果提交数.
   * 
   * @param insId
   */
  public void refreshPubSubmission(Long insId) {
    String hql = "call pkg_pub_submission_person.refresh_pub_submission_byIns(?)";
    super.getSession().createSQLQuery(hql).setParameter(0, insId).executeUpdate();
  }

  /**
   * 更新成果提交数.
   * 
   * @param insId
   * @param psnId
   */
  public void refreshPubSubmission(Long insId, Long psnId) {
    String hql = "call pkg_pub_submission_person.refresh_pub_submission_byPsn(?,?)";
    super.getSession().createSQLQuery(hql).setParameter(0, insId).setParameter(1, psnId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PubRolSubmissionStat> querySubmissionStatByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from PubRolSubmissionStat t where t.key.psnId=? or t.lastSendBy=?", psnId, psnId).list();
  }

}
