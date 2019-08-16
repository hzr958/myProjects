package com.smate.center.task.dao.rcmd.quartz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.task.model.rcmd.quartz.PubConfirmForm;
import com.smate.center.task.model.rcmd.quartz.PublicationConfirm;
import com.smate.center.task.model.rcmd.quartz.PublicationConfirmHi;
import com.smate.core.base.utils.data.RcmdHibernateDao;

@Repository
public class PublicationConfirmDao extends RcmdHibernateDao<PublicationConfirm, Long> {

  /**
   * 个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认，加载符合条件的数据.
   * 
   * @param startId
   * @return
   */
  public List<PublicationConfirm> loadReconfirmList(Long startId) throws Exception {

    String hql =
        "from PublicationConfirm t where t.confirmResult = 1 and syncStatus = 0 and confirmDate < ? and t.syncNum <=3 and t.id > ? order by t.id asc ";
    // 30分钟之前
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, -30);
    Date date = cal.getTime();
    return super.createQuery(hql, date, startId).setMaxResults(50).list();
  }

  /**
   * 删除pubConfirm中的记录
   * 
   * @param id
   */
  public void remove(Long id) {
    String Hql = "delete from PublicationConfirm t where t.id=:id";
    super.createQuery(Hql).setParameter("id", id).executeUpdate();
  }

  /**
   * 删除PublicationConfirmHi中的数据
   * 
   * @param id
   */
  public void removeHi(Long id) {

    String hql = "delete from PublicationConfirmHi t where t.id =:id ";
    super.createQuery(hql).setParameter("id", id).executeUpdate();
  }

  public void saveHistory(PublicationConfirm confirm) {
    this.removeHi(confirm.getId());
    super.getSession().save(new PublicationConfirmHi(confirm));

  }

  public PublicationConfirm getPublicationConfirmRol(Long psnId, Long rolpubId) {
    String hql = "from PublicationConfirm t where t.psnId=:psnId and rolPubId=:rolpubId";
    return (PublicationConfirm) super.createQuery(hql).setParameter("psnId", psnId).setParameter("rolpubId", rolpubId)
        .uniqueResult();
  }

  public Long savePublicationConfirm(PublicationConfirm confirm) {
    return (Long) super.getSession().save(confirm);
  }

  /**
   * 按条件查询需要确认的成果总数.
   * 
   * @param psnId
   * @param object
   */
  public Long queryPubConfirmCount(Long psnId, PubConfirmForm form) {
    List<Object> params = new ArrayList<Object>();
    StringBuilder hql = new StringBuilder("select count(p.id) ");
    hql.append(
        "from PubConfirmRolPub t,PublicationConfirm p where t.rolPubId=p.rolPubId and p.confirmResult=0 and p.psnId=?");
    params.add(psnId);
    // 选择了发表年份
    if (form != null && form.getPublishYear() != null) {
      if (form.getPublishYear() > 0) {
        hql.append(" and t.publishYear = ?  ");
        params.add(form.getPublishYear());
      } else if (form.getPublishYear() == -1) {
        hql.append(" and t.publishYear is null ");
      }
    }

    // 选择了匹配作者名称
    if (form != null && StringUtils.isNotBlank(form.getQueryPubMember())) {
      String queryMember = HtmlUtils.htmlUnescape(form.getQueryPubMember());
      hql.append(
          " and exists(select pm.id from PubConfirmRolPubMember pm where pm.rolPubId = p.rolPubId  and pm.seqNo = p.assignSeqNo and pm.name = ? )");
      params.add(queryMember);
    }
    return super.findUnique(hql.toString(), params.toArray());
  }

}
