package com.smate.web.management.dao.analysis.sns;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.analysis.sns.PersonTaught;

/**
 * 所教课程Dao.
 * 
 * @author zym
 * 
 */
@Repository
public class PersonTaughtDao extends SnsHibernateDao<PersonTaught, Long> {
  public void savePersonTaught(PersonTaught personTaught) {
    super.getSession().save(personTaught);
  }

  public void updateTaught(Long savePsnId, Long delPsnId) {
    super.createQuery("update PersonTaught t set t.psnId = ? where t.psnId = ?", savePsnId, delPsnId).executeUpdate();
  }

  public List<String> queryPersonTaught(Long psnId) {
    String taught =
        (String) super.createQuery("select t.content from PersonTaught t where t.psnId=?", new Object[] {psnId})
            .uniqueResult();
    List<String> taughtList = new ArrayList<String>();
    String[] taughts = StringUtils.split(taught, ";");
    if (ArrayUtils.isNotEmpty(taughts)) {
      for (String temp : taughts) {
        taughtList.add(temp.toLowerCase());
      }
    }

    return taughtList;
  }


  /**
   * 获取人员的所教课程hash列表
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPersonTaughHashList(Long psnId) throws Exception {
    String hql = "select distinct t.tauhtHash from PersonTaughtHash t where t.psnId = ? ";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 统计psnId和coPsnId相同所教课程数量.
   * 
   * @param psnId
   * @param coPsnId
   * @return
   * @throws DaoException
   */
  public Long personTaughtEqualCount(Long psnId, Long coPsnId) throws Exception {
    String hql =
        "select count(*) from PersonTaughtHash t1,PersonTaughtHash t2 where t1.psnId=? and t2.psnId=? and t1.tauhtHash=t2.tauhtHash";
    return findUnique(hql, psnId, coPsnId);
  }


}
