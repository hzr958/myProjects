package com.smate.center.task.dao.pdwh.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.pdwh.quartz.PdwhPublicationAll;
import com.smate.center.task.model.pdwh.quartz.PsnPubAllRecommend;
import com.smate.center.task.model.pdwh.quartz.PsnPubAllRecommendList;
import com.smate.center.task.model.pdwh.quartz.SearchForm;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 人员期刊推荐新算法，推荐出来的文献记录Dao
 * 
 * @author lichangwen
 * 
 */
@Repository
public class PsnPubAllRecommendDao extends PdwhHibernateDao<PsnPubAllRecommend, Long> {

  /**
   * 根据人员删除推荐记录
   * 
   * @param psnId
   * @throws DaoException
   */
  public void delPsnPubAllRecommend(Long psnId, Long puballId) {
    String hql = "delete from PsnPubAllRecommend t where t.psnId = ? and pubAllId=?";
    super.createQuery(hql, psnId, puballId).executeUpdate();
  }

  public void delPsnPubAllRecommend(Long psnId) {
    String hql = "delete from PsnPubAllRecommend t where t.psnId = ?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPubAllRecommend> findPsnPubAllRecommend(Long psnId, int language) {
    String hql = "from PsnPubAllRecommend t where t.psnId = ? and t.language=? ";
    return super.createQuery(hql, psnId, language).list();
  }

  /**
   * @param puballId
   */
  public void delPsnPubAllRecommendByPuballId(Long puballId) {
    String hql = "delete from PsnPubAllRecommend where pubAllId=?";
    super.createQuery(hql, puballId).executeUpdate();
  }

  /**
   * 获取推荐论文的人员ID列表_MJG_SCM-6129
   * 
   * @param pubAllId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnListByPuballId(Long pubAllId) {
    String hql = "select psnId from PsnPubAllRecommend t where t.pubAllId=?";
    return super.createQuery(hql, pubAllId).list();
  }

  public String getPsnRefRecommendByKeyword(Long psnId, Long puballId) {
    String hql = "select keywords from PsnPubAllRecommend where psnId=? and pubAllId=?";
    return findUnique(hql, psnId, puballId);
  }

  public PsnPubAllRecommend getPsnRefRecommend(Long psnId, Long puballId) {
    String hql = "from PsnPubAllRecommend where psnId=? and pubAllId=?";
    return findUnique(hql, psnId, puballId);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Page<PdwhPublicationAll> findPsnRefRecommend(Page page, SearchForm form) {
    String hql =
        "select new PdwhPublicationAll(t1.id,t1.pubId,t1.dbid,t1.zhTitle,t1.enTitle,t1.authorNames,t1.briefDescZh,t1.briefDescEn,t2.keywords)  from PdwhPublicationAll t1,PsnPubAllRecommend t2 where t1.id=t2.pubAllId and t2.psnId=? ";
    List<Object> params = new ArrayList<Object>();
    params.add(form.getPsnId());
    if (NumberUtils.isNumber(form.getLanguage())) {
      hql += " and t2.language=?";
      params.add(NumberUtils.toInt(form.getLanguage()));
    }
    hql += " order by t2.score desc,t2.id ";
    Query q = createQuery(hql, params.toArray());
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, params.toArray());
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List result = q.list();
    page.setResult(result);
    return page;
  }

  /**
   * 获取基准库推荐成果论文记录_MJG_SCM-5988.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  public PdwhPublicationAll findPsnRefRecommendSimple(Long psnId) {
    StringBuilder hql = new StringBuilder();
    hql.append(
        "select t1.id,t1.pubId,t1.dbid,t1.zhTitle,t1.enTitle,t1.authorNames,t1.briefDescZh,t1.briefDescEn,t2.keywords  from PdwhPublicationAll t1,PsnPubAllRecommend t2 where t1.id=t2.pubAllId and t2.psnId=? ");
    hql.append("order by t2.score desc,t2.id ");
    List list = super.createQuery(hql.toString(), psnId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      Object[] objArr = (Object[]) list.get(0);
      PdwhPublicationAll pubAll = new PdwhPublicationAll();
      pubAll.setId(Long.valueOf(ObjectUtils.toString(objArr[0])));
      pubAll.setPubId(Long.valueOf(ObjectUtils.toString(objArr[1])));
      pubAll.setDbid(Integer.valueOf(ObjectUtils.toString(objArr[2])));
      if (objArr[3] != null && StringUtils.isNotBlank(ObjectUtils.toString(objArr[3]))) {
        pubAll.setZhTitle(ObjectUtils.toString(objArr[3]));
      }
      if (objArr[4] != null && StringUtils.isNotBlank(ObjectUtils.toString(objArr[4]))) {
        pubAll.setEnTitle(ObjectUtils.toString(objArr[4]));
      }
      if (objArr[5] != null && StringUtils.isNotBlank(ObjectUtils.toString(objArr[5]))) {
        pubAll.setAuthorNames(ObjectUtils.toString(objArr[5]));
      }
      if (objArr[6] != null && StringUtils.isNotBlank(ObjectUtils.toString(objArr[6]))) {
        pubAll.setBriefDescZh(ObjectUtils.toString(objArr[6]));
      }
      if (objArr[7] != null && StringUtils.isNotBlank(ObjectUtils.toString(objArr[7]))) {
        pubAll.setBriefDescEn(ObjectUtils.toString(objArr[7]));
      }
      if (objArr[8] != null && StringUtils.isNotBlank(ObjectUtils.toString(objArr[8]))) {
        pubAll.setPsnMatchKeywords(ObjectUtils.toString(objArr[8]));
      }
      return pubAll;
    }
    return null;
  }

  /**
   * 查找推荐给psnId的成果.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnPubIdList(Long startPsnId, int timeLimit, int maxSize) {
    String hql = "select distinct psnId from PsnPubAllRecommend where psnId>? and recDate>? order by psnId ";
    // 计算限制的时间(以当前时间往前推算起始计算时间).
    Date date = new Date(new Date().getTime() + timeLimit * 60 * 60 * 1000);
    return super.createQuery(hql, startPsnId, date).setMaxResults(maxSize).list();
  }

  @SuppressWarnings("unchecked")
  public PsnPubAllRecommendList getPsnPubRcmOrerbyScore(Long psnId, Integer sizeLimit) {

    String hql = " from PsnPubAllRecommend where psnId=? ";

    Long count = super.countHqlResult(hql, psnId);
    if (count == 0) {
      return null;
    }

    PsnPubAllRecommendList pubAllRmdList = new PsnPubAllRecommendList();

    pubAllRmdList.setCount(count);
    String sufHql = " order by score desc";
    pubAllRmdList.setPubAllRmdList(super.createQuery(hql + sufHql, psnId).setMaxResults(sizeLimit).list());

    return pubAllRmdList;
  }

  /**
   * 保存人员推荐论文记录_MJG_SCM-5988.
   * 
   * @param pubAllRecommend
   */
  public void savePubAllRecommend(PsnPubAllRecommend pubAllRecommend) {
    if (pubAllRecommend != null) {
      if (pubAllRecommend.getId() != null) {
        String hql =
            "update PsnPubAllRecommend t set t.keywords=?,t.language=?,t.recDate=?,t.score=? where t.psnId=? and t.pubAllId=? ";
        super.createQuery(hql, pubAllRecommend.getKeywords(), pubAllRecommend.getLanguage(),
            pubAllRecommend.getRecDate(), pubAllRecommend.getScore(), pubAllRecommend.getPsnId(),
            pubAllRecommend.getPubAllId()).executeUpdate();
      } else {
        super.save(pubAllRecommend);
      }
    }
  }

}
