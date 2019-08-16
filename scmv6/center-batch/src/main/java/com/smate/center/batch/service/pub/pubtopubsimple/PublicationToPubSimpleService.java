package com.smate.center.batch.service.pub.pubtopubsimple;

import java.util.List;

import com.smate.center.batch.model.pub.pubtopubsimple.PubToPubSimpleErrorLog;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.ScmPubXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;



/**
 * 成果的信息添加到pubsimple新增的相关表中
 * 
 * @author lxz
 *
 */
public interface PublicationToPubSimpleService {

  /**
   * 入口
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pub
   * @throws Exception
   */
  public Long savePubSimpleData(Publication pub, PubXmlDocument doc) throws Exception;

  public void copyPubSimpleData(Publication pub, ScmPubXml scmPubXml) throws Exception;

  public List<Long> getSnsPublication(Integer size, Long lastPubId, Long startPubId, Long endPubId);

  public Publication getSnsPublicationById(Long pubId);

  public void saveError(PubToPubSimpleErrorLog pubToPubSimpleErrorLog);

  public List<Long> getPubIdList(Integer size);

  public void savePubToPubSimpleIntermediate(Long pubId, Integer status);

  void constructTmpPub();

  void copyPubXmlToDataStore(ScmPubXml scmPubXml);

  List<Long> getPubsByPsnId(Long pubId);

  List<Long> getSnsPubSimpleIds(Integer size, Long startPubId, Long endPubId);
}
