package com.smate.center.task.dao.publicpub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.common.CrossrefYearCount;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class CrossrefYearCountDao extends SnsHibernateDao<CrossrefYearCount, Long> {

  @SuppressWarnings("unchecked")
  public List<CrossrefYearCount> getYearCount() {
    String hql = "from CrossrefYearCount t where t.status=0 order by t.count desc";
    return super.createQuery(hql).list();
  }

  public void updateYearCountStatus(Long year) {
    String hql = "update CrossrefYearCount t set t.status=1 where t.year= :year";
    super.createQuery(hql).setParameter("year", year).executeUpdate();
  }

	@SuppressWarnings("unchecked")
	public List<CrossrefYearCount> getImportCrossrefData() {
		String hql = "from CrossrefYearCount t where t.status=1 order by t.count desc";
		return super.createQuery(hql).list();
	}
}
