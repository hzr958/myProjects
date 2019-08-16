package com.smate.center.task.dyn.dao.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.dyn.model.base.DynamicMsg;
import com.smate.core.base.utils.data.SnsHibernateDao;



/**
 * 动态信息表dao
 * 
 * @author zk
 *
 */
@Repository
public class DynamicMsgDao extends SnsHibernateDao<DynamicMsg, Long> {


  /**
   * 查找需要处理关系的动态列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DynamicMsg> findDynNeedDeal(Integer size) {
    String hql =
        "select new DynamicMsg(dm.dynId,dm.producer ,dm.dynType) from DynamicMsg dm where dm.relDealStatus=0 and dm.delstatus=0 ";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  /**
   * 更新动态待处理关系字段
   * 
   * @param dynId
   * @param relDealStatus
   */
  public void updateDynRelDealStatus(Long dynId, Integer relDealStatus) {
    String hql = "update DynamicMsg dm set dm.relDealStatus=:status,dm.updateDate=sysdate where dm.dynId=:dynId";
    super.createQuery(hql).setParameter("dynId", dynId).setParameter("status", relDealStatus).executeUpdate();
  }


  /**
   * 创建动态主键Id
   * 
   * @return Long
   */
  public Long createDynId() {
    String sql = "select V_SEQ_DYNAMIC_MSG.nextval from dual";
    return super.queryForLong(sql);
  }

  /**
   * 获取动态创建人
   * 
   * @param dynId
   * @return
   */

  public Long getDynProducer(Long dynId) {
    String hql = "select  d.producer  from  DynamicMsg d where d.dynId=:dynId  ";
    return (Long) this.createQuery(hql).setParameter("dynId", dynId).uniqueResult();
  }

  /**
   * 获取动态类型及模版
   * 
   * @param dynId
   * @return
   */
  public DynamicMsg getDynTypeAndTmp(Long dynId) {
    return null;
  }

  /**
   * 更新动态信息表状态
   * 
   * @param dynId
   * @param type
   */
  public void updateDynStatus(Long dynId, Integer type) {

  }

  /**
   * 获取个人所有动态列表
   * 
   * @param psnId
   * @param batchSize
   * @param status
   * @return
   * 
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDynListForPsnAll(Long psnId, Integer batchSize, Integer firstResult) {

    String sql = "select tt.dynId from (select max(dm.dyn_id) dynId  from v_dynamic_msg dm,v_dynamic_relation dr where"
        + " dm.dyn_id=dr.dyn_id and dr.receiver=? group by dm.res_id) tt order by tt.dynId desc";
    List<HashMap<String, BigDecimal>> queryList =
        this.queryForList(sql, new Object[] {psnId}, batchSize, (firstResult - 1) * batchSize);
    if (CollectionUtils.isNotEmpty(queryList)) {
      List<Long> resultList = new ArrayList<Long>(batchSize);
      for (HashMap<String, BigDecimal> map : queryList) {
        resultList.add(map.get("DYNID").longValue());
      }
      return resultList;
    } else {
      return null;
    }
  }

  /**
   * 获取与自己有关的动态列表
   * 
   * @param psnId
   * @param batchSize
   * @param integer
   * @return
   */
  public List<DynamicMsg> getDynListForPsn(Long psnId, Integer batchSize, Long lastDynId) {
    return null;
  }

  /**
   * 获取新鲜事动态列表
   * 
   * @param psnId
   * @param batchSize
   * @param lastDynId
   * @return
   */
  public List<DynamicMsg> getDynListForNews(Long psnId, Integer batchSize, Long lastDynId) {
    return null;
  }

  /**
   * 获取与好友有关动态列表
   * 
   * @param psnId
   * @param batchSize
   * @param lastDynId
   * @return
   */
  public List<DynamicMsg> getDynListForPsnFriends(Long psnId, Integer batchSize, Long lastDynId) {
    return null;
  }

  /**
   * 获取当天发送X类型动态的数量
   * 
   * @param psnId
   * @param resType
   * @param dynType
   * @return
   */
  public int getPubDynamicToday(Long psnId, Integer resType, String dynType) {
    String hql =
        "select count(dynId) from DynamicMsg where producer=:psnId and resType=:resType and dynType=:dynType and to_char(createDate,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd')";
    Long a = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("resType", resType)
        .setParameter("dynType", dynType).uniqueResult();
    return a.intValue();
  }

  /**
   * 获取新增成果，分享资助机构业务信息(SIE同步动态专用方法)
   * 
   * @param sieLastTime
   * @author yxy
   */
  @SuppressWarnings("unchecked")
  public List<DynamicMsg> getSieBusinessList(Long first, Long size, Date sieLastTime) {
    StringBuilder sbl = new StringBuilder();
    sbl.append(
        " select new DynamicMsg(t.dynId, t.producer,t.dynType,t.dynTmp,t.permission,t.delstatus,t.relDealStatus, t.sameFlag,t.fromType,t.targetId,t.createDate,t.updateDate, t.resId,t.resType,t.platform)");
    sbl.append(" from DynamicMsg t where t.delstatus=0 ");
    if (sieLastTime != null) {
      sbl.append(" and t.createDate > :sieLastTime ");
    }
    sbl.append(" and( 1 =( case  when  t.dynType in ('B2TEMP','ATEMP') and t.resType=25 then 1 else 0 end ) ");
    sbl.append(" or 1=( case  when  t.dynType='B3TEMP' and t.resType=1 then 1 else 0 end ) )");
    sbl.append(" order by t.updateDate desc ");
    Query query = super.createQuery(sbl + "");
    if (sieLastTime != null) {
      query.setParameter("sieLastTime", sieLastTime);
    }
    query.setFirstResult(first.intValue() - 1);
    query.setMaxResults(size.intValue());
    return query.list();
  }

}
