package com.smate.center.open.service.publication;

import com.smate.core.base.pub.model.Publication;

/**
 * 成果XML构建抽象接口.
 * 
 * @author pwl
 * 
 */
public interface IrisPubXmlBuildService {

  public static final String ENCODING = "utf-8";

  public String buildPubXml(Publication pub, String xmlData);
}
