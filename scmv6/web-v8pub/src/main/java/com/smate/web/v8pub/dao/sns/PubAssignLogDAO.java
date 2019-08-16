package com.smate.web.v8pub.dao.sns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubAssignLogPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;

@Repository
public class PubAssignLogDAO extends SnsHibernateDao<PubAssignLogPO, Long> {

  @SuppressWarnings("unchecked")
  public void queryPubConfirmIdList(PubQueryDTO pubQueryDTO) {
    String hql =
        "select t.pdwhPubId from PubAssignLogPO t  where t.confirmResult = 0 and t.psnId = :psnId and t.status=0 and nvl(t.score,0) > 0 order by nvl(t.score,0) desc,t.pdwhPubId desc";
    Query query = super.createQuery(hql).setParameter("psnId", pubQueryDTO.getSearchPsnId());
    String countHql =
        "select count( t.pdwhPubId) from PubAssignLogPO t where t.confirmResult=0 and t.psnId = :psnId and t.status=0 and nvl(t.score,0) > 0";
    Long totalCount =
        (Long) super.createQuery(countHql).setParameter("psnId", pubQueryDTO.getSearchPsnId()).uniqueResult();
    pubQueryDTO.setTotalCount(totalCount);
    if (pubQueryDTO.getIsAll() == 1) {// 不分页查询 一次全部查出所有
      List list = query.setMaxResults(400).list();
      pubQueryDTO.setPubIds(list);
    } else {
      List list = query.setFirstResult(pubQueryDTO.getFirst() - pubQueryDTO.getConfirmCount())
          .setMaxResults(pubQueryDTO.getPageSize()).list();
      pubQueryDTO.setPubIds(list);
    }
  }

  @SuppressWarnings("unchecked")
  public void queryPubConfirmIdIgnoreStatusList(PubQueryDTO pubQueryDTO) {
    String hql =
        "select t.pdwhPubId from PubAssignLogPO t  where  t.confirmResult in(0,1) and  t.psnId = :psnId and t.status=0  and nvl(t.score,0) > 0 order by nvl(t.score,0) desc,t.pdwhPubId desc";
    Query query = super.createQuery(hql).setParameter("psnId", pubQueryDTO.getSearchPsnId());
    String countHql =
        "select count( t.pdwhPubId) from PubAssignLogPO t where t.confirmResult in(0,1) and  t.psnId = :psnId and t.status=0  and nvl(t.score,0) > 0";
    Long totalCount =
        (Long) super.createQuery(countHql).setParameter("psnId", pubQueryDTO.getSearchPsnId()).uniqueResult();
    pubQueryDTO.setTotalCount(totalCount);
    if (pubQueryDTO.getIsAll() == 1) {// 不分页查询 一次全部查出所有
      List list = query.setMaxResults(400).list();
      pubQueryDTO.setPubIds(list);
    } else {
      List list = query.setFirstResult(pubQueryDTO.getFirst() - pubQueryDTO.getConfirmCount())
          .setMaxResults(pubQueryDTO.getPageSize()).list();
      pubQueryDTO.setPubIds(list);
    }
  }


  @SuppressWarnings("unchecked")
  public void queryPubAssignListForOpen(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    countHql.append(" select count(1) ");
    hql.append(" from PubAssignLogPO t ");
    listHql.append("select t.pdwhPubId");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where ");
    if (pubQueryDTO.getSearchPsnIdList() != null && pubQueryDTO.getSearchPsnIdList().size() > 0) {
      String psnIds = "";
      for (Long psnId : pubQueryDTO.getSearchPsnIdList()) {
        psnIds = psnIds + psnId + ",";
      }
      psnIds = psnIds.substring(0, psnIds.length() - 1);
      hql.append(" t.psnId in (" + psnIds + ") and t.status=0 and nvl(t.score,0) > 0");

    } else {
      hql.append("t.psnId = ? and t.status=0 and nvl(t.score,0) > 0");
      params.add(pubQueryDTO.getSearchPsnId());
    }
    if (pubQueryDTO.getPubUpdateDate() != null) {
      // 包括更新的成果
      hql.append(" and (t.createDate >= ? or t.confirmDate >= ?) ");
      params.add(pubQueryDTO.getPubUpdateDate());
      params.add(pubQueryDTO.getPubUpdateDate());
    }
    String orderString = "order by nvl(t.score,0) desc,t.pdwhPubId desc";
    // 记录数
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List resultList = super.createQuery(listHql.toString() + hql + orderString, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setPubIds(resultList);
  }

  /**
   * 查找所欲的 认领成果， 包括删除的
   * 
   * @param pubQueryDTO
   */
  public void queryAllPubAssignListForOpen(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    countHql.append(" select count(1) ");
    hql.append(" from PubAssignLogPO t ");
    listHql.append("select t.pdwhPubId");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where ");
    if (pubQueryDTO.getSearchPsnIdList() != null && pubQueryDTO.getSearchPsnIdList().size() > 0) {
      String psnIds = "";
      for (Long psnId : pubQueryDTO.getSearchPsnIdList()) {
        psnIds = psnIds + psnId + ",";
      }
      psnIds = psnIds.substring(0, psnIds.length() - 1);
      hql.append(" t.psnId in (" + psnIds + ")  and nvl(t.score,0) > 0");

    } else {
      hql.append("t.psnId = ?  and nvl(t.score,0) > 0");
      params.add(pubQueryDTO.getSearchPsnId());
    }
    if (pubQueryDTO.getPubUpdateDate() != null) {
      // 包括更新的成果
      hql.append(" and (t.createDate >= ? or t.confirmDate >= ? or t.updateDate >=?) ");
      params.add(pubQueryDTO.getPubUpdateDate());
      params.add(pubQueryDTO.getPubUpdateDate());
      params.add(pubQueryDTO.getPubUpdateDate());
    }
    String orderString = "order by nvl(t.score,0) desc,t.pdwhPubId desc";
    // 记录数
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List resultList = super.createQuery(listHql.toString() + hql + orderString, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setPubIds(resultList);
  }

  /**
   * 获取需要进行自动认领的成果列表
   * 
   * @param dupPubIds
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> listNeedConfirmPubId(List<Long> dupPubIds, Long psnId) {
    String hql =
        "select distinct(t.pdwhPubId) from PubAssignLogPO t where t.psnId = :psnId and t.pdwhPubId in (:dupPubIds) and t.status=0";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("dupPubIds", dupPubIds).list();
  }

  /**
   * 根据基准库成果id和人员id获取成果认领对象
   * 
   * @param pdwhPubId
   * @param psnId
   * @return
   */
  public PubAssignLogPO getPubAssign(Long pdwhPubId, Long psnId) {
    String hql =
        "from PubAssignLogPO t where t.psnId = :psnId and t.pdwhPubId = :pdwhPubId and t.confirmResult=0 and t.status=0";
    return (PubAssignLogPO) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pdwhPubId", pdwhPubId)
        .uniqueResult();
  }

  /**
   * 通过pubId和psnId 找到未认领的成果
   * 
   * @param pubQueryDTO
   * @return
   */
  public List<PubAssignLogPO> findByIds(PubQueryDTO pubQueryDTO) {
    String hql = "from PubAssignLogPO p where  p.pdwhPubId in (:ids) and p.status=0 ";
    if (pubQueryDTO.getSearchPsnIdList() != null && pubQueryDTO.getSearchPsnIdList().size() > 0) {
      String psnIds = "";
      for (Long psnId : pubQueryDTO.getSearchPsnIdList()) {
        psnIds = psnIds + psnId + ",";
      }
      psnIds = psnIds.substring(0, psnIds.length() - 1);
      hql += " and p.psnId in (" + psnIds + ") ";
      return this.createQuery(hql).setParameterList("ids", pubQueryDTO.getPubIds()).list();
    } else {
      hql += " and p.psnId =:psnId ";
      return this.createQuery(hql).setParameterList("ids", pubQueryDTO.getPubIds())
          .setParameter("psnId", pubQueryDTO.getSearchPsnId()).list();
    }

  }

  /**
   * 通过pubId和psnId 找到未认领的成果 ，包括删除的
   *
   * @param pubQueryDTO
   * @return
   */
  public List<PubAssignLogPO> findAllByIds(PubQueryDTO pubQueryDTO) {
    String hql = "from PubAssignLogPO p where  p.pdwhPubId in (:ids)  ";
    if (pubQueryDTO.getSearchPsnIdList() != null && pubQueryDTO.getSearchPsnIdList().size() > 0) {
      String psnIds = "";
      for (Long psnId : pubQueryDTO.getSearchPsnIdList()) {
        psnIds = psnIds + psnId + ",";
      }
      psnIds = psnIds.substring(0, psnIds.length() - 1);
      hql += " and p.psnId in (" + psnIds + ") ";
      return this.createQuery(hql).setParameterList("ids", pubQueryDTO.getPubIds()).list();
    } else {
      hql += " and p.psnId =:psnId ";
      return this.createQuery(hql).setParameterList("ids", pubQueryDTO.getPubIds())
          .setParameter("psnId", pubQueryDTO.getSearchPsnId()).list();
    }

  }

  /**
   * 不要添加状态 2019-04-22 open 接口调用
   * 
   * @param pubConfirmId
   * @return
   */
  public PubAssignLogPO getAssignLogByPubConfirmId(Long pubConfirmId) {
    String hql = "from PubAssignLogPO t where  t.id=:pubConfirmId";
    List list = super.createQuery(hql).setParameter("pubConfirmId", pubConfirmId).list();
    if (list != null && list.size() > 0) {
      return (PubAssignLogPO) list.get(0);
    }
    return null;
  }

  public void updateConfirmResult(Long pdwhPubId, Long psnId, Long snsPubId, Integer result) {
    String hql =
        "update PubAssignLogPO t set t.confirmResult = :result ,t.confirmDate= :confirmDate,t.snsPubId = :snsPubId where t.pdwhPubId= :pdwhPubId and t.psnId=:psnId and t.status=0";
    super.createQuery(hql).setParameter("result", result).setParameter("confirmDate", new Date())
        .setParameter("snsPubId", snsPubId).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId)
        .executeUpdate();
  }

  public void updateStatus(Long pdwhPubId, Integer status) {
    String hql =
        "update PubAssignLogPO t set t.status = :status,t.updateDate=:updateDate where t.pdwhPubId= :pdwhPubId";
    super.createQuery(hql).setParameter("status", status).setParameter("updateDate", new Date())
        .setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

  public List<Long> queryPubConfirmCount(Long psnId) {
    String hql =
        "select  t.pdwhPubId from PubAssignLogPO t where t.confirmResult=0 and t.psnId = :psnId and t.status=0 and nvl(t.score,0) > 0";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 根据成果id获取除自己以外且 未认领（confirm_result=0）分数(score>0)的合作者id
   * 
   * @param confirmPsnId 认领人id
   * @param pubConfirmId 认领成果id
   * @return
   */
  public List<Long> getPartnerPsnIdsByPubConfirmId(Long pubConfirmId, Long confirmPsnId) {
    String hql =
        "select t.psnId from PubAssignLogPO t where t.pdwhPubId=:pubConfirmId and t.confirmResult=0 and nvl(t.score,0) > 0  and t.psnId!=:confirmPsnId";
    return this.createQuery(hql).setParameter("pubConfirmId", pubConfirmId).setParameter("confirmPsnId", confirmPsnId)
        .list();
  }

  public PubAssignLogPO getPubAssignLog(Long pdwhPubId, Long psnId) {
    String hql =
        "from PubAssignLogPO t where t.psnId = :psnId and t.pdwhPubId = :pdwhPubId  and (nvl(t.score,0) > 0 or t.confirmResult =1 )";
    return (PubAssignLogPO) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pdwhPubId", pdwhPubId)
        .uniqueResult();
  }
}
