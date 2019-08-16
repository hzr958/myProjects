package com.smate.web.management.dao.other.fund;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.management.model.other.fund.ConstFundPosition;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundPositionDao extends RcmdHibernateDao<ConstFundPosition, Long> {


  public List<ConstFundPosition> getFundLeftMenu() {
    String hql = "from ConstFundPosition where code=? order by seqNo";
    return super.find(hql, ConstFundPosition.FUND_LEFT_MENU);
  }

  /**
   * @param typeId 1:机构类别，2：学位，3：职称
   * @return
   */
  public List<ConstFundPosition> findFundPositionByParantId(Long typeId) {
    String hql = "from ConstFundPosition where parentId=? order by seqNo";
    return super.find(hql, typeId);
  }


}
