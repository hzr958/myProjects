package com.smate.center.task.dao.sns.psn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.search.UserSearch;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 用户查找.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class UserSearchDao extends SnsHibernateDao<UserSearch, Long> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * 根据名称检索结果内容.
   * 
   * @param params
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<UserSearch> findUserList(Map<String, Object> params) throws DaoException {
    String zhInfo = ObjectUtils.toString(params.get("zhInfo"));
    String enInfo = ObjectUtils.toString(params.get("enInfo"));
    int maxSize = Integer.valueOf(ObjectUtils.toString(params.get("maxSize")));
    StringBuffer hql = new StringBuffer();
    // 删除隐私项过滤，过滤逻辑迁移到UserSearchService_MJG_SCM-5534.
    String resultHql = "from UserSearch t where 1=1 ";
    String sortHql = "order by t.scoreNum desc ";
    // 查询数据实体
    List<UserSearch> resultList = new ArrayList<UserSearch>();
    try {
      List<Object> conditions = null;
      // 检索中文记录符合的结果.
      if (StringUtils.isNotBlank(zhInfo)) {
        conditions = new ArrayList<Object>();
        hql.append(resultHql);
        // 根据人员中文索引检索人员记录.
        hql.append("and lower(t.zhInfo) like ? ");
        conditions.add("%" + zhInfo.trim().toLowerCase() + "%");
        hql.append(sortHql);
        resultList.addAll(super.createQuery(hql.toString(), conditions.toArray()).setMaxResults(maxSize).list());
      }
      // 检索英文记录符合的结果.
      if (StringUtils.isNotBlank(enInfo)) {
        hql = new StringBuffer();
        conditions = new ArrayList<Object>();
        hql.append(resultHql);
        /*
         * 兼容匹配各种英文名不匹配的问题_MJG_2014-06-27. <因生产机发现有一个现象：人员英文名为Jian MA ，而输入各种组合的英文名均不能匹配到用户>
         */
        hql.append("and (lower(replace(t.enInfo, ' ', '')) like ? or lower(t.enInfo) like ? or t.enInfo like ?) ");
        String trimEnInfo = enInfo.replaceAll(" ", "");
        conditions.add("%" + trimEnInfo.toLowerCase() + "%");
        conditions.add("%" + enInfo.toLowerCase() + "%");
        conditions.add("%" + enInfo + "%");
        hql.append(sortHql);
        resultList.addAll(super.createQuery(hql.toString(), conditions.toArray()).setMaxResults(maxSize).list());
      }

      return resultList;
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  /**
   * 首页-页脚链接检索人员.
   * 
   * @param userInfo
   * @param locale
   * @param limitSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked"})
  public List<UserSearch> findIndexUserList(String userInfo, String locale, int limitSize) throws DaoException {
    // 删除隐私项过滤，过滤逻辑迁移到UserSearchService_MJG_SCM-5534.
    String hql = "from UserSearch t where t.selfLogin='1' and t.nodeId<>'0' ";
    String sortHql = "order by t.scoreNum desc ";
    try {
      List<Object> conditions = new ArrayList<Object>();
      if (StringUtils.isNotBlank(userInfo) && !StringUtils.equalsIgnoreCase("other", userInfo)) {
        // 根据人员中文索引检索人员记录.
        if (Locale.CHINA.toString().equals(locale)) {
          hql += "and lower(t.zhInfoIndex) like ? ";
          conditions.add(userInfo.toLowerCase() + "%");
        } else {
          hql += "and lower(t.enInfo) like ? ";
          conditions.add(userInfo.toLowerCase() + "%");
        }
      }
      // 查询数据实体
      List<UserSearch> resultList =
          (List<UserSearch>) super.createQuery(hql + sortHql, conditions.toArray()).setMaxResults(limitSize).list();
      return resultList;
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  /**
   * 根据人员ID获取结果记录.
   * 
   * @param psnIdList
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<UserSearch> getSearchByIdList(List<Long> psnIdList, int size) {
    // 删除隐私项过滤，过滤逻辑迁移到UserSearchService_MJG_SCM-5534.
    String hql = "from UserSearch t where t.psnId in (:psnId) ";
    return super.createQuery(hql).setParameterList("psnId", psnIdList).setMaxResults(size).list();
  }

  /**
   * 过滤特殊字符.
   * 
   * @param info
   * @return
   */
  public String filterSpecChar(String info) {

    return StringUtils.trimToNull(info.replaceAll(
        "[\\~|\\`|\\!|\\@|\\#|\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\-|\\|\\_|\\=|\\+|\\{|\\}|\\[|\\]|\\:|\\;|\"|\\'|\\||\\|\\<|\\>|\\,|\\.|\\?|\\/|『|』|￥|…|×|（|）|——|]",
        ""));
  }


  /**
   * 获取对应单位名称的人员记录.
   * 
   * @param insName
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<UserSearch> getSearchByIns(String insName, int maxSize) {
    String hql =
        "from UserSearch t where t.insNameZh like ? or t.insNameEn like ? or t.insNameAbbr=? order by t.scoreNum desc ";
    return super.createQuery(hql, insName + "%", insName + "%", insName).setMaxResults(maxSize).list();
  }



  /**
   * 更新人员信息的分数.
   * 
   * @param psnId
   * @param scoreNum
   */
  public void updateUserSearchScore(Long psnId, Integer scoreNum) {
    String hql = "update UserSearch t set t.scoreNum=?,t.indexFlag = 0 where t.psnId=? ";
    super.createQuery(hql, scoreNum, psnId).executeUpdate();
  }

}
