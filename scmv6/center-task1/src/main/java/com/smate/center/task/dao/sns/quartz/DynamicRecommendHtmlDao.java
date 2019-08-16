package com.smate.center.task.dao.sns.quartz;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.DynamicRecommendHtml;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 推荐动态显示HTML内容DAO类_SCM-5912.
 * 
 * @author mjg
 * 
 */
@Repository
public class DynamicRecommendHtmlDao extends SnsHibernateDao<DynamicRecommendHtml, Long> {

  /**
   * 保存推荐动态构建记录.
   * 
   * @param reDynHtml
   */
  public void saveDynReHtml(DynamicRecommendHtml reDynHtml) {
    if (reDynHtml != null) {
      if (reDynHtml.getId() != null) {
        String hql =
            "update DynamicRecommendHtml t set t.dynHtml=?, t.dynHtmlEn=?, t.updateDate=sysdate where t.psnId=? and t.dynReType=? ";
        super.createQuery(hql, reDynHtml.getDynHtml(), reDynHtml.getDynHtmlEn(), reDynHtml.getPsnId(),
            reDynHtml.getDynReType()).executeUpdate();
      } else {
        super.save(reDynHtml);
      }
    }
  }

  /**
   * 获取推荐动态构建记录.
   * 
   * @param psnId
   * @param dynReType
   * @return
   */
  public DynamicRecommendHtml getDynReHtml(Long psnId, int dynReType) {
    String hql = "from DynamicRecommendHtml t where t.psnId=? and t.dynReType=? and rownum=1 ";
    Object obj = super.createQuery(hql, psnId, dynReType).uniqueResult();
    if (obj != null) {
      return (DynamicRecommendHtml) obj;
    }
    return null;
  }

  /**
   * 获取推荐动态构建内容.
   * 
   * @param psnId
   * @param dynReType
   * @return
   */
  public String getDynReHtmlCon(Long psnId, int dynReType) {
    String hql = "select dynHtml from DynamicRecommendHtml t where t.psnId=? and t.dynReType=? and rownum=1 ";
    Object obj = super.createQuery(hql, psnId, dynReType).uniqueResult();
    if (obj != null) {
      return (String) obj;
    }
    return null;
  }

  public String getDynReHtmlCon(String locale, Long psnId, int dynReType) {
    if (StringUtils.equalsIgnoreCase(locale, "zh_CN")) {
      String hql = "select dynHtml from DynamicRecommendHtml t where t.psnId=? and t.dynReType=? and rownum=1 ";
      Object obj = super.createQuery(hql, psnId, dynReType).uniqueResult();
      if (obj != null) {
        return (String) obj;
      }
    } else {
      String hql = "select dynHtmlEn from DynamicRecommendHtml t where t.psnId=? and t.dynReType=? and rownum=1 ";
      Object obj = super.createQuery(hql, psnId, dynReType).uniqueResult();
      if (obj != null) {
        return (String) obj;
      }
    }
    return null;
  }

  /**
   * 删除推荐动态HTML内容.
   * 
   * @param psnId
   * @param dynReType
   */
  public void delDynRecommendHtml(Long psnId, int dynReType) {
    String hql = "delete from DynamicRecommendHtml t where t.psnId=? and t.dynReType=?";
    super.createQuery(hql, psnId, dynReType).executeUpdate();
  }
}
