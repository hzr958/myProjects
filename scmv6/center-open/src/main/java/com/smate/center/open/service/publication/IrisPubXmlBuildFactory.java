package com.smate.center.open.service.publication;

import java.util.Map;

/**
 * 成果xml文件构建工厂.
 * 
 * @author pwl
 * 
 */
public class IrisPubXmlBuildFactory {

  private static ThreadLocal<IrisPubXmlBuildService> irisPubXmlBuildService = new ThreadLocal<IrisPubXmlBuildService>();

  private Map<Integer, IrisPubXmlBuildService> map = null;

  public IrisPubXmlBuildService createPubXmlServiceBean(int pubType) {
    setIrisPubXmlBuildService(map.get(pubType));
    return irisPubXmlBuildService.get();
  }

  public Map<Integer, IrisPubXmlBuildService> getMap() {
    return map;
  }

  public void setMap(Map<Integer, IrisPubXmlBuildService> map) {
    this.map = map;
  }

  public static IrisPubXmlBuildService getIrisPubXmlBuildService() {
    return irisPubXmlBuildService.get();
  }

  public static void setIrisPubXmlBuildService(IrisPubXmlBuildService irisPubXmlBuild) {
    irisPubXmlBuildService.set(irisPubXmlBuild);
  }
}
