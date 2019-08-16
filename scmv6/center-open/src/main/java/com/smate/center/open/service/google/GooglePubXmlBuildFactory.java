package com.smate.center.open.service.google;



import java.util.Map;

/**
 * 成果xml文件构建工厂.
 * 
 * @author ajb
 * 
 */
public class GooglePubXmlBuildFactory {

  private static ThreadLocal<GooglePubXmlBuildComponent> googlePubXmlBuildComponent =
      new ThreadLocal<GooglePubXmlBuildComponent>();

  private Map<Integer, GooglePubXmlBuildComponent> map = null;

  public GooglePubXmlBuildComponent createPubXmlServiceBean(int pubType) {
    setIrisPubXmlBuildService(map.get(pubType));
    return googlePubXmlBuildComponent.get();
  }

  public Map<Integer, GooglePubXmlBuildComponent> getMap() {
    return map;
  }

  public void setMap(Map<Integer, GooglePubXmlBuildComponent> map) {
    this.map = map;
  }

  public static GooglePubXmlBuildComponent getIrisPubXmlBuildService() {
    return googlePubXmlBuildComponent.get();
  }

  public static void setIrisPubXmlBuildService(GooglePubXmlBuildComponent irisPubXmlBuild) {
    googlePubXmlBuildComponent.set(irisPubXmlBuild);
  }
}
