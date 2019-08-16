package com.smate.core.base.dyn.dao;

import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 动态信息表dao
 * 
 * @author zk
 *
 */
@Repository
public class DynamicMsgDao extends SnsHibernateDao<DynamicMsg, Long> {
  /**
   * 获取父动态id
   * 
   * @param dynId
   * @return
   */
  public Long getParentDynId(Long dynId) {
    String hql = "select t.sameFlag from DynamicMsg t where t.dynId=:dynId";
    return (Long) super.createQuery(hql).setParameter("dynId", dynId).uniqueResult();
  }

  /**
   * 获取动态模板类型
   * 
   * @param dynId
   * @return
   */
  public String getDynTypeDynId(Long dynId) {
    String hql = "select t.dynType from DynamicMsg t where t.dynId=:dynId";
    return (String) super.createQuery(hql).setParameter("dynId", dynId).uniqueResult();
  }

  /**
   * 查找需要处理关系的动态列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DynamicMsg> findDynNeedDeal(Integer size) {
    String hql =
        "select new DynamicMsg(dm.dynId,dm.producer) from DynamicMsg dm where dm.relDealStatus=0 and dm.delstatus=0 ";
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
  public void updateDynStatus(Long dynId, Integer delstatus) {
    String hql = "update DynamicMsg dm  set  dm.delstatus=:delstatus  where dm.dynId=:dynId";
    this.createQuery(hql).setParameter("delstatus", delstatus).setParameter("dynId", dynId).executeUpdate();
  }

  /**
   * 获取个人所有动态列表
   * 
   * form.getPsnId(), form.getPage().getPageSize(), form .getPage().getPageNo()
   * 
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDynListForPsnAll(Long psnId, Integer batchSize, Integer firstResult) {
    String sql = "select tt.dynId from (select max(dm.dyn_id) dynId  from v_dynamic_msg dm,v_dynamic_relation dr where"
        + " dm.del_status = 0 and  dm.dyn_id=dr.dyn_id and dr.receiver=?   and dr.deal_status= 0  group by dm.res_id) tt order by tt.dynId desc";

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
   * 获取动态列表的总数
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Long getDynListForPsnCount(Long psnId) {

    String countSql =
        "select count(1) from (select max(dm.dyn_id) dynId  from v_dynamic_msg dm,v_dynamic_relation dr where"
            + " dm.del_status = 0  and  dm.dyn_id=dr.dyn_id and dr.receiver=?   and dr.deal_status= 0  group by dm.res_id) tt order by tt.dynId desc";
    Long count = super.queryForLong(countSql, new Object[] {psnId});

    if (count == null) {
      return 0L;
    }
    return count;
  }

  /**
   * 获取个人所有动态列表数量
   * 
   * @param psnId
   * @return
   */
  public Integer getPsnDynListCount(Long psnId) {
    String countSql = " select  count(1)  from v_dynamic_msg dm,v_dynamic_relation dr where"
        + " dm.dyn_id=dr.dyn_id and dr.receiver=? group by dm.res_id  ";
    Object obj = this.queryForList(countSql, new Object[] {psnId});
    if (obj != null) {
      return ((List) obj).size();
    }
    return 0;
  }

  /**
   * 根据获取个人动态列表
   * 
   * @param psnId
   * @param batchSize
   * @param status
   * @return
   * 
   */
  @SuppressWarnings("unchecked")
  public List<Long> getDynListByDynId(Long psnId, Long dynId, Integer flag) {
    String sql = null;
    Integer maxSize = 10;
    // 1表示获取老的
    if (flag == 1) {
      sql = "select tt.dynId from (select max(dm.dyn_id) dynId  from v_dynamic_msg dm,v_dynamic_relation dr where"
          + " dm.dyn_id=dr.dyn_id and dr.receiver=? and dr.deal_status= 0 and dm.del_status = 0 group by dm.res_id) tt  where tt.dynId <=?  order by tt.dynId desc";
    } // 2表示获取新的
    if (flag == 2) {
      sql = "select tt.dynId from (select max(dm.dyn_id) dynId  from v_dynamic_msg dm,v_dynamic_relation dr where"
          + " dm.dyn_id=dr.dyn_id and dr.receiver=?  and dr.deal_status= 0 and dm.del_status = 0  group by dm.res_id) tt  where tt.dynId >=? order by tt.dynId desc";
    }

    List<HashMap<String, BigDecimal>> queryList = this.queryForList(sql, new Object[] {psnId, dynId}, maxSize, 0);
    if (CollectionUtils.isNotEmpty(queryList)) {
      List<Long> resultList = new ArrayList<Long>(maxSize);
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
   * 查找需要屏蔽的动态id
   * 
   * @param dynId
   * @param resId
   * @param resType
   * @return
   */
  public List<DynamicMsg> findShieldDynamicMsg(Long dynId, Long resId, Integer resType) {
    String hql =
        "  from    DynamicMsg dm  where  dm.dynId<=:dynId and  dm.resId=:resId  and dm.resType=:resType   and dm.delstatus=0  order by dm.dynId asc";
    return (List<DynamicMsg>) this.createQuery(hql).setParameter("dynId", dynId).setParameter("resId", resId)
        .setParameter("resType", resType).list();
  }

  /**
   * 查看当天的动态信息的状态(0:正常，99:删除)
   */
  public Integer getDelstatus(Long resId, Integer resType, Long producer) {
    String hql =
        "select t.delstatus from DynamicMsg t where t.resId=:resId and t.resType=:resType and t.producer=:producer "
            + "and to_char(t.createDate,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd')";
    return (Integer) this.createQuery(hql).setParameter("resId", resId).setParameter("resType", resType)
        .setParameter("producer", producer).uniqueResult();
  }

  /**
   * 查找推荐人名单的动态id，用于新用户登陆后产生动态
   */
  public List<DynamicMsg> getDynIdsByPsnIds(List<Long> psnIds, Integer size) {
    String hql = "from DynamicMsg t where t.producer in (:psnIds) order by t.dynId desc";
    return super.createQuery(hql).setParameterList("psnIds", psnIds).setMaxResults(size).list();
  }
}
