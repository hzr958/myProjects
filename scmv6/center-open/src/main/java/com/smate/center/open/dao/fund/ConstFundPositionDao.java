package com.smate.center.open.dao.fund;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.fund.ConstFundPosition;
import com.smate.core.base.utils.data.RcmdHibernateDao;

@Repository
public class ConstFundPositionDao extends RcmdHibernateDao<ConstFundPosition, Long> {

  /**
   * 获取基金的相关常数最低要求值.
   * 
   * @param posIdList
   * @return
   */
  @SuppressWarnings("unchecked")
  public Integer getLowestRequirementById(List<Long> posIdList) {
    List<String> codeList = null;
    if (CollectionUtils.isNotEmpty(posIdList)) {
      String hql = "select t.code from ConstFundPosition t where t.id in (:ids) ";
      codeList = super.createQuery(hql).setParameterList("ids", posIdList).list();
    } else {
      return 0;
    }
    Integer maxCodeValue = null;// 无要求，code对应至const_position中的grades
    for (String code : codeList) {
      try {
        Integer value = Integer.parseInt(code);
        if (value == -1) {
          return 0;
        } else if (maxCodeValue == null || (value != 0 && value > maxCodeValue)) {
          maxCodeValue = value;
        }
      } catch (Exception e) {
        continue;
      }
    }
    // 0无要求，4,3,2,1对应从低到高的职称要求
    return maxCodeValue;
  }

}
