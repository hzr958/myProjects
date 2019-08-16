package com.smate.center.open.service.publication;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.model.publication.PublicationXml;

/**
 * 
 * @author ajb
 * 
 */
@Service("publicationXmlService")
@Transactional(rollbackFor = Exception.class)
public class PublicationXmlServiceImp implements PublicationXmlService {
  private static final long serialVersionUID = 533618978437244145L;

  @Resource(name = "pubXmlStoreService")
  private PubXmlStoreService pubXmlStoreService;

  @Override
  public PublicationXml getById(Long pubId) throws Exception {

    return pubXmlStoreService.get(pubId);
  }

}
