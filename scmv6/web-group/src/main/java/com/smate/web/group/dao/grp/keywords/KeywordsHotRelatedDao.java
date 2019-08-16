package com.smate.web.group.dao.grp.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.keywords.model.KeywordsHot;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.web.group.model.grp.keywords.KeywordsHotRelated;
import com.smate.web.group.model.grp.keywords.PsnKwRmcRefHotkw;

/**
 * 关键词热词相关词.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class KeywordsHotRelatedDao extends HibernateDao<KeywordsHotRelated, Long> {

  /**
   * 获取人员熟悉的关键词相关热词.
   * 
   * @param rids
   * @return
   */
  public List<PsnKwRmcRefHotkw> getKeywordsHotRelated(List<Long> rids, Long psnId) {

    List<PsnKwRmcRefHotkw> kwhtplist = new ArrayList<PsnKwRmcRefHotkw>();
    List<KeywordsHotRelated> kwhtrlist = getKeywordsHotRelatedNew(rids);
    for (KeywordsHotRelated kwhtr : kwhtrlist) {
      PsnKwRmcRefHotkw kwhtp = new PsnKwRmcRefHotkw(psnId, kwhtr.getCid(), kwhtr.getSeqNo(), kwhtr.getRid());
      kwhtplist.add(kwhtp);
    }
    return kwhtplist;
  }

  /**
   * 获取关键词相关热词.
   * 
   * @param rids
   * @return
   */
  @SuppressWarnings("unchecked")
  @Deprecated
  public List<KeywordsHotRelated> getKeywordsHotRelated(List<Long> rids) {

    List<KeywordsHotRelated> kwhtrlist = new ArrayList<KeywordsHotRelated>();
    String hql = "from KeywordsHotRelated t where t.rid = ? order by relSim desc ";
    // 每个关键词取5个相关热词，然后优先每个取一条，不够再补的原则
    for (int i = 0; i < rids.size(); i++) {
      Long rid = rids.get(i);
      List<KeywordsHotRelated> list = super.createQuery(hql, rid).setMaxResults(5).list();
      if (CollectionUtils.isEmpty(list)) {
        continue;
      }
      SEN_LOOP: for (int j = 0; j < list.size(); j++) {
        KeywordsHotRelated kwr = (KeywordsHotRelated) list.get(j);

        // 内部查重，怕多个相关词指向一个热词
        for (KeywordsHotRelated kwp : kwhtrlist) {
          // 如果已经重复，继续下一个热词，排除重复
          if (kwr.getCid().equals(kwp.getCid())) {
            continue SEN_LOOP;
          }
        }

        // 序号规则，每个关键词的相关热词按轮次设定序号，总共5轮
        // 1001,1002...,2001,2002....
        kwr.setSeqNo(((j + 1) * 1000) + i + 1);
        kwhtrlist.add(kwr);
      }
    }
    return kwhtrlist;
  }

  /**
   * 获取关键词相关热词.(新算法，采用聚合)
   * 
   * @param rids
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<KeywordsHotRelated> getKeywordsHotRelatedNew(List<Long> rids) {

    if (CollectionUtils.isNotEmpty(rids)) {
      Integer maxSize = 25;
      // 返回值
      List<KeywordsHotRelated> kwhtrlist = new ArrayList<KeywordsHotRelated>();
      String hql =
          "select t.cid as cid,count(t.rid) as co from KeywordsHotRelated t where t.rid in (:rids) group by t.cid order by count(t.rid) desc , sum(t.relSim) desc";
      // 聚合值
      List<Map> refKwMapList = super.createQuery(hql).setParameterList("rids", rids).setMaxResults(maxSize)
          .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
      return this.handlerRefKw(kwhtrlist, refKwMapList, rids);
    }
    return null;
  }

  /**
   * 处理关键词相关热词
   * 
   * @param kwhtrlist
   * @param refKwIdList
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private List<KeywordsHotRelated> handlerRefKw(List<KeywordsHotRelated> kwhtrlist, List<Map> refKwMapList,
      List<Long> rids) {
    if (CollectionUtils.isNotEmpty(refKwMapList)) {
      List<Long> refKwIds = new ArrayList<Long>();
      // 热词id和聚合个数匹配关系
      Map<Long, Integer> refHkCoIdMap = new HashMap<Long, Integer>();
      // 取热词id
      for (Map map : refKwMapList) {
        Long cid = Long.valueOf(ObjectUtils.toString(map.get("cid")));
        refKwIds.add(cid);
        refHkCoIdMap.put(cid, Integer.valueOf(ObjectUtils.toString(map.get("co"))));
      }
      String hql = "from KeywordsHotRelated t where t.rid in (:rids) and t.cid in (:cids)";
      kwhtrlist = super.createQuery(hql).setParameterList("rids", rids).setParameterList("cids", refKwIds).list();
      // 补充序号(聚合的个数)
      if (CollectionUtils.isNotEmpty(kwhtrlist)) {
        for (KeywordsHotRelated khr : kwhtrlist) {
          khr.setSeqNo(refHkCoIdMap.get(khr.getCid()));
        }
      }
    }
    return kwhtrlist;
  }

  /**
   * 获取关键词相关热词.
   * 
   * @param rids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KeywordsHotRelated> getKeywordsHotRelated(Long rid) {

    String hql = "from KeywordsHotRelated t where t.rid = ? order by relSim desc ";
    // 每个关键词取3个相关热词
    List<KeywordsHotRelated> list = super.createQuery(hql, rid).setMaxResults(3).list();
    return list;
  }

  /**
   * 获取关键词相关热词.
   * 
   * @param rids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KeywordsHotRelated> getKeywordsHotRelated(Long rid, List<Long> excHotId) {
    if (CollectionUtils.isEmpty(excHotId)) {
      return getKeywordsHotRelated(rid);
    }
    String hql = "from KeywordsHotRelated t where t.rid = :rid and t.cid not in(:cid) order by relSim desc ";
    // 每个关键词取3个相关热词
    List<KeywordsHotRelated> list =
        super.createQuery(hql).setParameter("rid", rid).setParameterList("cid", excHotId).setMaxResults(3).list();
    return list;
  }

  /**
   * 加载热词相关词.
   * 
   * @param hotKeyIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, List<KeywordsHot>> loadHotKeysRelatedKw(List<Long> hotKeyIds) {
    // 一次性取出，再分类
    String hql =
        "select new KeywordsHot(t.id,  t.keywords,  t.kwTxt, t.tf,  t.ekeywords,  t.ekwTxt,t.etf, t2.cid) from KeywordsHot t, KeywordsHotRelated t2 where t2.cid in(:cids) and t.id = t2.rid order by t2.cid asc,t2.relSim desc ";
    List<KeywordsHot> relateKws = super.createQuery(hql).setParameterList("cids", hotKeyIds).list();
    Map<Long, List<KeywordsHot>> map = new HashMap<Long, List<KeywordsHot>>();
    for (Long hotKeyId : hotKeyIds) {
      List<KeywordsHot> relateKwList = new ArrayList<KeywordsHot>();
      for (KeywordsHot kwh : relateKws) {
        if (hotKeyId.equals(kwh.getRelHotKey())) {
          relateKwList.add(kwh);
        }
      }
      map.put(hotKeyId, relateKwList);

    }
    return map;
  }

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

}
