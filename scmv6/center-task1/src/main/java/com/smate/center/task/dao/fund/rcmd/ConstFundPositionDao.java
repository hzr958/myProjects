package com.smate.center.task.dao.fund.rcmd;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.rcmd.ConstFundPosition;
import com.smate.center.task.model.sns.pub.ConstRegion;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundPositionDao extends RcmdHibernateDao<ConstFundPosition, Long> {

  /**
   * @param typeId 1:机构类别，2：学位，3：职称
   * @return
   */
  public List<ConstFundPosition> findFundPositionByParantId(Long typeId) {
    String hql = "from ConstFundPosition where parentId=? order by seqNo";
    return super.find(hql, typeId);
  }

  public List<ConstFundPosition> getFundLeftMenu() {
    String hql = "from ConstFundPosition where code=? order by seqNo";
    return super.find(hql, ConstFundPosition.FUND_LEFT_MENU);
  }

  public List<ConstRegion> getConstRegionBySuperId(Long regionId) {
    String hql =
        "select new ConstRegion(id,zhName,enName) from ConstRegion where superRegionId=? order by nlssort(zhName,'NLS_SORT=SCHINESE_PINYIN_M')";
    return super.find(hql, regionId);
  }

  public List<ConstRegion> getAllConstRegion(Locale locale) {
    String hql = "select new ConstRegion(id,zhName,enName) from ConstRegion where superRegionId is null order by "
        + locale.getLanguage() + "Seq";
    return super.find(hql);
  }

  public Long getLanguageId(String language) {
    String hql = "select t.id from ConstFundPosition t where t.nameZh=? or t.nameEn=?";
    return findUnique(hql, language, language);
  }

  public Long getRegionId(String regionName) {
    String hql = "select t.id from ConstRegion t where t.zhName like ? or t.enName like ?";
    return findUnique(hql, "%" + regionName + "%", "%" + regionName + "%");
  }

  public ConstRegion getConstRegionById(Long regionId) {
    String hql = "from ConstRegion where id = ? ";
    return (ConstRegion) super.find(hql, regionId).get(0);
  }

  /**
   * 获取基金的相关常数列表.
   * 
   * @param posIdList
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstFundPosition> getFundPosById(List<Long> posIdList) {
    List<ConstFundPosition> posList = null;
    if (CollectionUtils.isNotEmpty(posIdList)) {
      String hql = "from ConstFundPosition t where t.id in (:ids) ";
      posList = super.createQuery(hql).setParameterList("ids", posIdList).list();
    }
    return posList;
  }

  public ConstFundPosition getConstFundPositionById(Long positionId) {
    String hql = "from ConstFundPosition where id = ? ";
    return (ConstFundPosition) super.find(hql, positionId).get(0);
  }

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
