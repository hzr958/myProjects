package com.smate.web.management.dao.journal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.journal.BaseJournalTempCheck;

@Repository
public class BaseJournalTempCheckDao extends PdwhHibernateDao<BaseJournalTempCheck, Long> {


  @SuppressWarnings("unchecked")
  public Page<BaseJournalTempCheck> findByTempCheck(Page page, BaseJournalTempCheck tempCheck) {
    List<Object> params = new ArrayList<>();
    StringBuilder hql = new StringBuilder("from BaseJournalTempCheck where 1=1 ");
    if (tempCheck.getStatus() != null) {
      hql.append(" and status =?");
      params.add(tempCheck.getStatus());
    }
    if (tempCheck.getTempBatchId() != null && "batch".equals(tempCheck.getIsTemp())) {
      hql.append(" and tempBatchId =?");
      params.add(tempCheck.getTempBatchId());
    }
    if (StringUtils.isNotBlank(tempCheck.getIsTemp()) && "batch".equals(tempCheck.getIsTemp())) {
      hql.append(" and tempBatchId is not null");
    }
    if (StringUtils.isNotBlank(tempCheck.getIsTemp()) && "isiIf".equals(tempCheck.getIsTemp())) {
      hql.append(" and tempIsiIfId is not null");
    }
    if (StringUtils.isNotBlank(tempCheck.getTitleXx())) {
      hql.append(" and titleXx like ? or titleEn like ?");
      params.add("%" + StringUtils.trimToEmpty(tempCheck.getTitleXx()) + "%");
      params.add("%" + StringUtils.trimToEmpty(tempCheck.getTitleXx()) + "%");
    }
    if (StringUtils.isNotBlank(tempCheck.getPissn())) {
      hql.append(" and pissn =?");
      params.add(StringUtils.trimToEmpty(tempCheck.getPissn()));
    }
    if (tempCheck.getStatus() == null) {
      hql.append(" and pissn =0");
    }
    hql.append(" order by tempCheckId desc");
    return super.findPage(page, hql.toString(), params.toArray());
  }



}
