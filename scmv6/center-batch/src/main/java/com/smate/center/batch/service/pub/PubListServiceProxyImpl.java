package com.smate.center.batch.service.pub;

import javax.annotation.Resource;

import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;

/**
 * 收录情况代理类，由于sns要添加收录情况同步的jms，而单位rol那边不需要，所以做一个代理类
 * 
 * @author Scy
 * 
 */
@Service("pubListServiceProxy")
@Transactional(rollbackFor = Exception.class)
public class PubListServiceProxyImpl implements PublicationListService {


  @Resource(name = "publicationListService")
  private PublicationListService publicationListService;

  @Override
  public String convertPubListSourceToString(PublicationList pubList) {
    return publicationListService.convertPubListSourceToString(pubList);
  }

  @Override
  public String convertPubListToString(PublicationList pubList) {
    return publicationListService.convertPubListToString(pubList);
  }

  @Override
  public PublicationList elementConvertPubList(Element node) throws ServiceException {
    return publicationListService.elementConvertPubList(node);
  }

  @Override
  public PublicationList getPublicationList(Long pubId) throws ServiceException {
    return publicationListService.getPublicationList(pubId);
  }

  @Override
  public PublicationList prasePubList(PubXmlDocument doc) {
    PublicationList pubList = publicationListService.prasePubList(doc);
    // 收录情况来源改变时，需要同步申请书成果那边的信息
    // try {
    // this.convertPubListToString(pubList), this
    // .convertPubListSourceToString(pubList));
    // } catch (ServiceException e) {
    // logger.error("收录情况来源同步更新出现异常！", e);
    // e.printStackTrace();
    // }
    return pubList;
  }

  @Override
  public PublicationList praseSourcePubList(PubXmlDocument doc) {
    return publicationListService.praseSourcePubList(doc);
  }

  @Override
  public void saveOrUpdatePublictionList(PublicationList pubList) throws ServiceException {
    publicationListService.saveOrUpdatePublictionList(pubList);
  }

  @Override
  public void savePublictionList(PublicationList pubList) throws ServiceException {
    publicationListService.savePublictionList(pubList);

  }

  @Override
  public PublicationList wrapPublicationList(String[] pubLists, String[] pubListsSource) {
    return publicationListService.wrapPublicationList(pubLists, pubListsSource);
  }

}
