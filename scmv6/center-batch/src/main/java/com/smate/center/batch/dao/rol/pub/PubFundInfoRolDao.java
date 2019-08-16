package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubFundInfoNum;
import com.smate.center.batch.model.rol.pub.PubFundInfoRol;
import com.smate.center.batch.model.rol.pub.PubFundPrjRefresh;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果项目信息.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubFundInfoRolDao extends RolHibernateDao<PubFundInfoRol, Long> {

  /**
   * 保存成果项目信息.
   * 
   * @param pubId
   * @param insId
   * @param fundInfo
   * @param fundNums
   */
  public void savePubFundInfo(Long pubId, Long insId, String fundInfo, Long fundHash, List<Long> fundNums) {
    // 如果成果项目信息为空，清理数据
    if (StringUtils.isBlank(fundInfo)) {
      this.remove(pubId);
      return;
    }
    // 保存
    String lfundInfo = StringUtils.lowerCase(fundInfo);
    super.save(new PubFundInfoRol(pubId, insId, fundInfo, lfundInfo, fundHash));

    // 保存成果项目信息中抽取出连续的数字.
    this.savePubFundInfoNum(pubId, insId, fundNums);
  }

  /**
   * 保存成果项目信息中抽取出连续的数字.
   * 
   * @param pubId
   * @param fundNums
   */
  @SuppressWarnings("unchecked")
  public void savePubFundInfoNum(Long pubId, Long insId, List<Long> fundNums) {

    // 如果数字没有，清理
    if (CollectionUtils.isEmpty(fundNums)) {
      super.createQuery("delete from PubFundInfoNum t where t.pubId = ? ", pubId).executeUpdate();
      return;
    }

    String hql = "from PubFundInfoNum t where t.pubId = ?";
    List<PubFundInfoNum> list = super.createQuery(hql, pubId).list();

    outLoop: for (PubFundInfoNum finum : list) {
      for (int i = 0; i < fundNums.size(); i++) {
        Long num = fundNums.get(i);
        // 已存在，不需要继续添加，滤过
        if (num.equals(finum.getFundNum())) {
          fundNums.remove(i);
          continue outLoop;
        }
      }
      // 删除不存在的
      super.getSession().delete(finum);
    }
    // 保存新数字
    for (Long num : fundNums) {
      super.getSession().save(new PubFundInfoNum(pubId, insId, num));
    }
  }

  /**
   * 删除成果项目信息.
   * 
   * @param pubId
   */
  public void remove(Long pubId) {
    super.createQuery("delete from PubFundInfoNum t where t.pubId = ? ", pubId).executeUpdate();
    super.createQuery("delete from PubFundInfoRol t where t.pubId = ? ", pubId).executeUpdate();
  }

  /**
   * 获取成果项目信息hashcode.
   * 
   * @param pubId
   * @return
   */
  public Long getFundHash(Long pubId) {

    return super.findUnique("select fundHash from PubFundInfoRol t where t.pubId = ? ", pubId);
  }

  /**
   * 成果项目信息通过项目编号连续数字查找成果ID.
   * 
   * @param insId
   * @param fundInfoNum
   * @return
   */
  @SuppressWarnings("unchecked")
  public Collection<Long> loadPubByFundInfoNum(Long insId, List<Long> fundInfoNums) {

    String hql = "select pubId from PubFundInfoNum t where t.insId = :insId and t.fundNum in(:fundNums) ";
    List<Long> pubIds =
        super.createQuery(hql).setParameter("insId", insId).setParameterList("fundNums", fundInfoNums).list();
    if (CollectionUtils.isEmpty(pubIds)) {
      return null;
    }
    // 转换成set，去重
    Set<Long> pubIdsSet = new HashSet<Long>();
    pubIdsSet.addAll(pubIds);
    return pubIdsSet;
  }

  /**
   * 通过loadPubByFundInfoNum的项目编号数字过滤出具体成果ID，再传入项目编号like过滤.
   * 
   * @param insId
   * @param fundNo
   * @param filterPubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> loadPubByFundNo(Long insId, String fundNo, Collection<Long> filterPubIds) {

    String hql =
        "select pubId from PubFundInfoRol t where t.insId = :insId and t.lfundInfo like :lfundInfo and t.pubId in(:pubIds)";
    // 拆分参数，参数可能>100
    Collection<Collection<Long>> container = ServiceUtil.splitList(filterPubIds, 80);
    List<Long> listResult = new ArrayList<Long>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameter("insId", insId)
          .setParameter("lfundInfo", "%" + fundNo.toLowerCase().trim() + "%").setParameterList("pubIds", item).list());
    }
    return listResult;
  }

  /**
   * 传入项目编号like查找成果中项目信息一致的列表.
   * 
   * @param insId
   * @param fundNo
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> loadPubByFundNo(Long insId, String fundNo) {

    String hql = "select pubId from PubFundInfoRol t where t.insId = :insId and t.lfundInfo like :lfundInfo ";

    return super.createQuery(hql).setParameter("insId", insId)
        .setParameter("lfundInfo", "%" + fundNo.toLowerCase().trim() + "%").list();
  }

  /**
   * 获取需要刷新成果关联项目的成果列表.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubFundPrjRefresh> loadRefreshList() {

    String hql = "from PubFundPrjRefresh t where t.status = 0 ";
    return super.createQuery(hql).setMaxResults(100).list();
  }

  /**
   * 标注刷新成果关联项目失败.
   * 
   * @param pubId
   */
  public void markRefreshError(Long pubId) {

    String hql = "update PubFundPrjRefresh t set t.status = 9 where t.pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 删除刷新成果关联项目信息.
   * 
   * @param pubId
   */
  public void removeRefresh(Long pubId) {

    String hql = "delete from PubFundPrjRefresh t where t.pubId = ?";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 项目信息改变，刷新关联的成果信息.
   * 
   * @param prjId
   * @param insId
   */
  public void addRefresh(Long pubId, Long insId) {

    String hql = "from PubFundPrjRefresh t where t.pubId = ? ";
    PubFundPrjRefresh refresh = super.findUnique(hql, pubId);
    if (refresh == null) {
      refresh = new PubFundPrjRefresh(pubId, insId);
    }
    refresh.setStatus(0);
    super.getSession().save(refresh);
  }

  /**
   * 查找指定成果的项目信息.
   * 
   * @param insId
   * @param fundNo
   * @param filterPubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubFundInfoRol> loadPubFundInfo(Collection<Long> pubIds) {

    String hql = "select new PubFundInfoRol(pubId, fundInfo) from PubFundInfoRol t where t.pubId in(:pubIds)";
    // 拆分参数，参数可能>100
    Collection<Collection<Long>> container = ServiceUtil.splitList(pubIds, 80);
    List<PubFundInfoRol> listResult = new ArrayList<PubFundInfoRol>();
    for (Collection<Long> item : container) {
      listResult.addAll(super.createQuery(hql).setParameterList("pubIds", item).list());
    }
    return listResult;
  }
}
