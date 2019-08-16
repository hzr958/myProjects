package com.smate.center.oauth.dao.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.profile.PersonTaught;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 所教课程Dao.
 * 
 * @author wsn
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



}
