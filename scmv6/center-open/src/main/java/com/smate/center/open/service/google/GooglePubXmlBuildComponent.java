package com.smate.center.open.service.google;


/**
 * 成果XML构建抽象接口.
 * 
 * @author ajb
 * 
 */
public interface GooglePubXmlBuildComponent {

  public static final String ENCODING = "utf-8";

  public String buildPubXml(Long pubId, String xmlData);
}
