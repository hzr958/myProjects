package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.center.batch.model.sns.pub.PubSimpleHash;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

public interface PubSimpleService {

  /**
   * 查询pubSimple和pubDataStore
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   * @return
   */
  public PubSimple queryPubSimpleAndXml(Long pubId);

  /**
   * 查询pubSimple
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   * @return
   */
  public PubSimple queryPubSimple(Long pubId);

  /**
   * 删除v_pub_simple,v_pub_simple_hash,v_pub_data_store表中的记录
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   */
  public void delPubSimpleData(PubSimple pubSimple);

  /**
   * 保存
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubSimple
   */
  public void save(PubSimple pubSimple);

  /**
   * 通过查询V_PUB_SIMPLE_HASH得到重复Ids
   * 
   * @param pubId， enHashCode，zhHashCode
   * @return
   */
  public List<Long> getDupPubIdsFromSimpleHash(Long pubId, String enHashCode, String zhHashCode);

  /**
   * 通过查询V_PUB_SIMPLE_HASH判断是否是重复
   * 
   * @param pubId， enHashCode，zhHashCode
   * @return
   */
  public boolean pubSimpleDupCheck(Long pubId, String enHashCode, String zhHashCode);

  // 临时任务，刷新pubxml中的pubmember的顺序错误
  public List<PubDataStore> getXmlPubMemberRefreshList(Integer SIZE, Long lastPubId, Long startPubId, Long endPubId);

  public void savePubDataStore(PubDataStore pub);

  Long savePubSimpleData(Publication pub, PubXmlDocument doc) throws Exception;

  public PubSimpleHash getPubSimpleHash(Long pubId);

}
