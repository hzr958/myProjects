package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.RermcPsnKwHot;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class RermcPsnKwHotDao extends SnsHibernateDao<RermcPsnKwHot, Long> {
  public void deleteByPsnId(Long psnId) {
    String hql = "delete from RermcPsnKwHot h where h.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public void addNewRermcPsnKwHot(Long kid, Long psnId) {
    String sql =
        "insert into RERMC_PSN_KW_HOT(Id,PSN_ID,Kwhot_Id,Keyword,Kw_Text ) select SEQ_RERMC_PSN_KW_HOT.Nextval," + psnId
            + ",id,keyword,kw_txt from disc_keyword_hot where id=?";
    super.update(sql, new Object[] {kid});
  }

  public void updateRermcPsnKwHot(Long kid, Long psnId) {
    String sql = "update RermcPsnKwHot set psnId=:psnId where kwhotId=:kwhotId and psnId is null";
    super.createQuery(sql).setParameter("psnId", psnId).setParameter("kwhotId", kid).executeUpdate();
  }

  // RermcPsnKwHot.id越小，推荐度越大
  @SuppressWarnings("unchecked")
  public List<String> getRermcHotByPsnId(Long psnId, Integer limitSize) {

    List<String> returnKwList = null;
    String hql = "select keyword from RermcPsnKwHot where psnId=? order by id ";
    List<String> hotKwList = super.createQuery(hql, psnId).list();

    if (CollectionUtils.isNotEmpty(hotKwList)) {
      returnKwList = new ArrayList<String>();
      for (String hotKw : hotKwList) {
        if (!returnKwList.contains(hotKw)) {
          returnKwList.add(hotKw);
        }
        if (returnKwList.size() >= limitSize) {
          break;
        }
      }
    }

    return returnKwList;
  }
}
